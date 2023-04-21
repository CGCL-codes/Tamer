package com.mindquarry.persistence.api;

/**
 * Add summary documentation here.
 *
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">your full name</a>
 */
public abstract class EntityBase {

    protected String id;

    public EntityBase() {
    }

    /**
     * Getter for id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for id.
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
}
