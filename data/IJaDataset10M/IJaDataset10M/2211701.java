package net.sf.doolin.sqm.model;

import java.util.Date;

public abstract class Entity {

    private int id;

    private Date creationDate;

    private Account creator;

    private Date editionDate;

    private Account editor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Account getCreator() {
        return creator;
    }

    public void setCreator(Account creator) {
        this.creator = creator;
    }

    public Date getEditionDate() {
        return editionDate;
    }

    public void setEditionDate(Date editionDate) {
        this.editionDate = editionDate;
    }

    public Account getEditor() {
        return editor;
    }

    public void setEditor(Account editor) {
        this.editor = editor;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && getClass().isInstance(obj)) {
            Entity e = (Entity) obj;
            if (this.id == e.id) {
                return (this.id != 0);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
