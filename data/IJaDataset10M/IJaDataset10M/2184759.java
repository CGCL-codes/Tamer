package com.narirelays.ems.persistence.orm;

import java.util.HashSet;
import java.util.Set;

/**
 * ResourceCategory entity. @author MyEclipse Persistence Tools
 */
public class ResourceCategory implements java.io.Serializable {

    private String id;

    private String name;

    private Set resourceses = new HashSet(0);

    /** default constructor */
    public ResourceCategory() {
    }

    /** minimal constructor */
    public ResourceCategory(String id) {
        this.id = id;
    }

    /** full constructor */
    public ResourceCategory(String id, String name, Set resourceses) {
        this.id = id;
        this.name = name;
        this.resourceses = resourceses;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set getResourceses() {
        return this.resourceses;
    }

    public void setResourceses(Set resourceses) {
        this.resourceses = resourceses;
    }
}
