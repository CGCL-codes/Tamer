package com.privilege.entity.troubleshoot;

import java.util.HashSet;
import java.util.Set;
import com.privilege.entity.Entity;
import com.privilege.entity.Label;
import com.privilege.entity.Tag;

public class Priority implements Entity {

    private long id;

    private Tag tag;

    private String name;

    private void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public Tag getTag() {
        return this.tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void associate(Entity entity) {
        if (entity instanceof Tag) {
            this.tag = (Tag) entity;
        }
    }

    public void dissociate(Entity entity) {
        if (entity instanceof Tag) {
            if (entity.equals(tag)) this.tag = null;
        }
    }
}
