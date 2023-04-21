package com.faceye.components.navigation.dao.model;

import java.util.HashSet;
import java.util.Set;
import com.faceye.core.dao.hibernate.model.BaseObject;

/**
 * 
 * @author：宋海鹏
 * @Connection:E_mail:ecsun@sohu.com/myecsun@hotmail.com QQ:82676683
 * @Copy Right:www.faceye.com
 * @System:www.faceye.com网络支持系统
 * @Create Time:2007-9-22
 * @Package com.faceye.components.navigation.dao.model.Category.java
 * @Description:网站分类
 */
public class Category extends BaseObject {

    private Category parentCategory;

    private Set childrenCategories = new HashSet(0);

    private Set feeds = new HashSet(0);

    private Set traditions = new HashSet(0);

    public Set getFeeds() {
        return feeds;
    }

    public void setFeeds(Set feeds) {
        this.feeds = feeds;
    }

    public Set getTraditions() {
        return traditions;
    }

    public void setTraditions(Set traditions) {
        this.traditions = traditions;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Set getChildrenCategories() {
        return childrenCategories;
    }

    public void setChildrenCategories(Set childrenCategories) {
        this.childrenCategories = childrenCategories;
    }

    public String json() {
        StringBuffer sb = new StringBuffer("{");
        sb.append("\"id\":");
        sb.append("\"");
        sb.append(this.getId());
        sb.append("\",");
        sb.append("\"name\":");
        sb.append("\"");
        sb.append(this.getName());
        sb.append("\"");
        if (this.getParentCategory() != null) {
            sb.append(",");
            sb.append("\"parentId\":");
            sb.append("\"");
            sb.append(this.getParentCategory().getId());
            sb.append("\",");
            sb.append("\"parentName\":");
            sb.append("\"");
            sb.append(this.getParentCategory().getName());
            sb.append("\"");
        } else {
            sb.append(",");
            sb.append("\"parentId\":");
            sb.append("\"");
            sb.append("source");
            sb.append("\",");
            sb.append("\"parentName\":");
            sb.append("\"");
            sb.append("网站分类");
            sb.append("\"");
        }
        sb.append("}");
        return sb.toString();
    }
}
