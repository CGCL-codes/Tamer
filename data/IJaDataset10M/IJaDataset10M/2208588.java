package cn.com.wblog.pojo;

import org.nutz.dao.entity.annotation.*;

@Table("wb_article_type")
public class ArticleType {

    @Column
    @Id
    private int id;

    @Column
    private int userId;

    @Column
    private String name;

    @Column
    private int parentId;

    @Column
    private String alias;

    @Column
    private String description;

    @One(target = ArticleType.class, field = "parentId")
    private ArticleType parent;

    public ArticleType getParent() {
        return parent;
    }

    public void setParent(ArticleType parent) {
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return "id: " + this.id + ", userId: " + this.userId + ", name: " + this.name + ", parentId: " + this.parentId + ", alias: " + this.alias + ", desc: " + this.description;
    }
}
