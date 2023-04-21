package com.projectmanagement.helpers.i18n.data;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Languages")
public class Language implements Serializable {

    private static final long serialVersionUID = -432771966030146973L;

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "isDefault")
    private boolean isDefault;

    public Language() {
        name = "";
        isDefault = false;
    }

    public Language(String name) {
        this();
        this.name = name;
    }

    public Language(String name, boolean isDefault) {
        this();
        this.name = name;
        this.isDefault = isDefault;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
