package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    @Autowired
    ArticleRepository articleRepository;


    public List<Article> index() {
        return articleRepository.findAll();
    }

    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleForm form) {
        Article created=form.toEntity();
        if(created.getId()!=null)
            return null;
        return articleRepository.save(created);
    }

    public Article update(Long id, ArticleForm dto) {
        Article article = dto.toEntity();
        Article target =articleRepository.findById(id).orElse(null);
        if(target ==null || id!=target.getId())
            return null;
        target.patch(article);
        Article updated = articleRepository.save(target);
        return updated;

    }

    public Article delete(Long id) {

        Article target = articleRepository.findById(id).orElse(null);
        if (target == null) {
            return null;
        }

        articleRepository.delete(target);
        return target;

    }
    @Transactional
    public List<Article> createArticles(List<ArticleForm> dtos) {
        List<Article> articleList=dtos.stream()
                .map(dto->dto.toEntity())
                .collect(Collectors.toList());

        articleList.stream()
                .forEach(article -> articleRepository.save(article) );

        articleRepository.findById(-1L).orElseThrow(()->new IllegalArgumentException("결제 실패"));

        return articleList;

    }
}
