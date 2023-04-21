package net.spatula.tally_ho.service.beans;

import java.io.Serializable;

public class SectionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private long id;

    private String description;

    public SectionBean() {
    }

    public SectionBean(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
