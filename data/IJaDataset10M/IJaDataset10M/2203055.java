package org.light.portlets.blog.model;

import javax.persistence.Column;
import javax.persistence.Table;
import org.hibernate.annotations.Index;
import org.light.portal.core.model.Entity;

@javax.persistence.Entity
@Table(name = "light_blog_category")
@org.hibernate.annotations.Table(appliesTo = "light_blog_category", indexes = { @Index(name = "index_userId", columnNames = { "userId" }) })
public class BlogCategory extends Entity {

    private static final long serialVersionUID = -8669339417517876234L;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private long parentId;

    @Column
    private long userId;

    @Column
    private int status;

    public BlogCategory() {
        super();
    }

    public BlogCategory(String name, String description, long userId, long orgId) {
        this();
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.setOrgId(orgId);
    }

    public BlogCategory(String name, String desc, long parentId, long userId, long orgId, int status) {
        this(name, desc, userId, orgId);
        this.parentId = parentId;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
