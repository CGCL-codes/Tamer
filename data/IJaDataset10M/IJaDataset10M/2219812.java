package com.javaeedev.cache;

import java.util.List;

/**
 * Facade for all DAO.
 * 
 * @author Xuefeng
 */
public interface Facade {

    List<ArticleCategory> queryArticleCategories();

    void createArticleCategory(ArticleCategory category);

    void updateArticleCategory(ArticleCategory category);

    void deleteArticleCategory(ArticleCategory category);
}
