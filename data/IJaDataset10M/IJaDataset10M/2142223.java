package org.datanucleus.sql4o.model;

/**
 * 
 */
public class City {

    private Integer id;

    private String name;

    public City(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getId() {
        return id;
    }

    public String toString() {
        return "City [" + id + "]: name=" + name;
    }
}
