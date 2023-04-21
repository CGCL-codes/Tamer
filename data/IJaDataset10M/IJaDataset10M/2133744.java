package net.taylor.xml.jaxb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Entity {

    public enum Status {

        Submitted, Approved
    }

    private Long id;

    private String name;

    private Status status;

    private Date date;

    private Boolean active = Boolean.FALSE;

    private Entity reference;

    private List<Entity> children = new ArrayList<Entity>();

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Entity getReference() {
        return reference;
    }

    public void setReference(Entity reference) {
        this.reference = reference;
    }

    public List<Entity> getChildren() {
        return children;
    }

    public void setChildren(List<Entity> children) {
        this.children = children;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Entity other = (Entity) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }
}
