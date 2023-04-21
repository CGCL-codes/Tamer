package org.mindhaus.bdb.browser.entities;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

/**
 * @author amarch
 *
 */
@Entity(version = 2)
public class MyEntity {

    @PrimaryKey(sequence = "sequence")
    private Long id;

    private String name;

    private boolean dirty;

    private MyEntity parent;

    public MyEntity() {
    }

    public MyEntity(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public MyEntity getParent() {
        return parent;
    }

    public void setParent(MyEntity parent) {
        this.parent = parent;
    }
}
