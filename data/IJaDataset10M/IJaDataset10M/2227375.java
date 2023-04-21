package com.m4f.utils.feeds.events.model;

import java.io.Serializable;
import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Dump implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private Date launched;

    @Persistent
    private String description;

    @Persistent
    private String ownerClass;

    @Persistent
    private Long owner;

    public Long getId() {
        return this.id;
    }

    public void setId(Long i) {
        this.id = i;
    }

    public Date getLaunched() {
        return this.launched;
    }

    public void setLaunched(Date d) {
        this.launched = d;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getOwnerClass() {
        return this.ownerClass;
    }

    public void setOwnerClass(String clazz) {
        this.ownerClass = clazz;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public Long getOwner() {
        return owner;
    }
}
