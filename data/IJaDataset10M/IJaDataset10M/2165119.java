package com.servengine.portal;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Annotation implements java.io.Serializable {

    private static final long serialVersionUID = -2355221717300287469L;

    protected Integer id;

    protected Date date;

    protected Portal portal;

    protected String body;

    public Annotation() {
    }

    public Annotation(Portal portal, String annotationText) {
        this.portal = portal;
        date = new Date();
        this.body = annotationText;
    }

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public boolean equals(Object o) {
        return ((Annotation) o).getId().equals(id);
    }

    public java.util.Date getDate() {
        return date;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public Portal getPortal() {
        return portal;
    }

    @Lob
    public String getBody() {
        return body;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }

    public void setBody(String annotationText) {
        this.body = annotationText;
    }
}
