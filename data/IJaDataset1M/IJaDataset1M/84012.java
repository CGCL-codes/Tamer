package com.ballroomregistrar.compinabox.online.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Dance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "style_id", nullable = false, insertable = false, updatable = false)
    private Style style;

    private String name;

    private String abbreviation;

    public Dance() {
    }

    public Dance(String name) {
        this.name = name;
    }

    public Dance(String n, String a) {
        name = n;
        abbreviation = a;
    }

    public Dance(Long id, String n, String a) {
        this.id = id;
        name = n;
        abbreviation = a;
    }

    public Dance(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Style getStyle() {
        return style;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
