package com.anasoft.os.bookworm.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.entity.RooEntity;

@Entity
@RooEntity
@Configurable
public class Category {

    private String code;

    private String text;

    @ManyToOne
    @JoinColumn
    private Document document;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
    private Set<Category> childs = new HashSet<Category>();

    @ManyToOne
    @JoinColumn
    private Category parent;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Set<Category> getChilds() {
        return this.childs;
    }

    public void setChilds(Set<Category> childs) {
        this.childs = childs;
    }

    public Category getParent() {
        return this.parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id: ").append(getId()).append(", ");
        sb.append("version: ").append(getVersion()).append(", ");
        sb.append("code: ").append(getCode()).append(", ");
        sb.append("text: ").append(getText()).append(", ");
        sb.append("document: ").append(getDocument()).append(", ");
        sb.append("childs: ").append(getChilds() == null ? "null" : getChilds().size()).append(", ");
        sb.append("parent: ").append(getParent());
        return sb.toString();
    }
}
