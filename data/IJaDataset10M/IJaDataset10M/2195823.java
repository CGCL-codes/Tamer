package com.javaeedev.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.javaeedev.annotation.Form;
import com.javaeedev.util.Constants;

/**
 * Tag for Article and Resource.
 * 
 * @author Xuefeng
 */
@Entity
@Table(name = "e_tag")
public class Tag extends AbstractId {

    private String title;

    private String description = "";

    private int articleCount;

    private int resourceCount;

    /**
     * Tag name, automatically transformed to lowercase, e.g. spring, hibernate.
     */
    @Column(unique = true, nullable = false, updatable = false, length = Constants.VARCHAR_USERNAME_LENGTH)
    public String getTitle() {
        return title;
    }

    @Form
    public void setTitle(String title) {
        this.title = title.trim().toLowerCase();
    }

    @Column(nullable = true, length = Constants.VARCHAR_DESCRIPTION_LENGTH)
    public String getDescription() {
        return description;
    }

    @Form
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(nullable = false)
    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    @Column(nullable = false)
    public int getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }
}
