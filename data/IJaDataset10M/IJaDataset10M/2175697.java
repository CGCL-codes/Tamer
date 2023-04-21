package com.nodeshop.entity;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

/**
 * 实体类 - 文章分类
 
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 
 
 
 
 
 * KEY: nodeshopA5C3E6A36433995CEACED1A2B0507513
 
 */
@Entity
public class ArticleCategory extends BaseEntity {

    private static final long serialVersionUID = -5132652107151648662L;

    public static final String PATH_SEPARATOR = ",";

    private String name;

    private String metaKeywords;

    private String metaDescription;

    private Integer orderList;

    private String path;

    private ArticleCategory parent;

    private Set<ArticleCategory> children;

    private Set<Article> articleSet;

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 5000)
    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    @Column(length = 5000)
    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    @Column(nullable = false)
    public Integer getOrderList() {
        return orderList;
    }

    public void setOrderList(Integer orderList) {
        this.orderList = orderList;
    }

    @Column(nullable = true, length = 10000)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public ArticleCategory getParent() {
        return parent;
    }

    public void setParent(ArticleCategory parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("orderList asc")
    public Set<ArticleCategory> getChildren() {
        return children;
    }

    public void setChildren(Set<ArticleCategory> children) {
        this.children = children;
    }

    @OneToMany(mappedBy = "articleCategory", fetch = FetchType.LAZY)
    public Set<Article> getArticleSet() {
        return articleSet;
    }

    public void setArticleSet(Set<Article> articleSet) {
        this.articleSet = articleSet;
    }

    @Transient
    public Integer getLevel() {
        return path.split(PATH_SEPARATOR).length - 1;
    }
}
