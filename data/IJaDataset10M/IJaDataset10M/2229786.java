package org.open.force.data.domain;

import java.io.Serializable;

public class OFSoqlCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
