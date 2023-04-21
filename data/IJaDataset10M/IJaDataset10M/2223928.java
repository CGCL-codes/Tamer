package org.isi.monet.applications.frontserver.core.model;

public class Session {

    private String id;

    public Session() {
        id = "-1";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
