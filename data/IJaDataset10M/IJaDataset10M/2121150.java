package com.kosongkosong.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ifnu
 */
@PersistenceCapable
@XmlRootElement(name = "team")
public class Team {

    private String name;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
