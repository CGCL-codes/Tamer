package com.serie402.common.bo;

import java.util.Calendar;

public class AbstractArticle {

    private int articleId;

    private String category;

    private String subCategory;

    private String title;

    private String description;

    private Author author;

    private Calendar date;

    public int getArticleId() {
        return articleId;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Author getAuthor() {
        return author;
    }

    public Calendar getDate() {
        return date;
    }

    public void setArticleId(int _articleId) {
        articleId = _articleId;
    }

    public void setCategory(String _category) {
        category = _category;
    }

    public void setSubCategory(String _subCategory) {
        subCategory = _subCategory;
    }

    public void setTitle(String _title) {
        title = _title;
    }

    public void setDescription(String _description) {
        description = _description;
    }

    public void setAuthor(Author _author) {
        author = _author;
    }

    public void setDate(Calendar _date) {
        date = _date;
    }
}
