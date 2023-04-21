package com.centraview.activity;

/**
 * class Resource
 */
public class Resource {

    private int id;

    private String name = null;

    /**
   * constructor with arguments id and name
   * @param id
   * @param name
   */
    Resource(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
   * method getId
   * @return id
   */
    public int getId() {
        return this.id;
    }

    /**
   * method getName
   * @return name
   */
    public String getName() {
        return this.name;
    }
}
