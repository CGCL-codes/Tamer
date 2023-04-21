package com.nsharmon.iTranslate.dao.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import com.google.appengine.api.datastore.Key;
import com.nsharmon.iTranslate.dao.ParentEntity;

@Entity
public class User extends ParentEntity<TranslationJob> {

    @Id
    @Basic
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key id;

    private String userId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = TranslationJob.class)
    @JoinTable(name = "User_TranslationJob")
    List<TranslationJob> translationJobs = new ArrayList<TranslationJob>();

    public User() {
        super(TranslationJob.class);
    }

    public Key getId() {
        return id;
    }

    public void setId(Key id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<TranslationJob> getTranslationJobs() {
        return translationJobs;
    }

    public void setTranslationJobs(List<TranslationJob> translationJobs) {
        this.translationJobs = translationJobs;
    }

    @Transient
    public List<TranslationJob> getChildren() {
        return getTranslationJobs();
    }

    @Transient
    public void setChildren(List<TranslationJob> children) {
        setTranslationJobs(children);
    }
}
