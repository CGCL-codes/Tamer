package org.technbolts.sample;

import java.util.Date;

public class SimpleComposite {

    private String name;

    private Date date;

    private int count;

    private SimpleModel[] models;

    public SimpleComposite() {
    }

    public SimpleComposite(String name, Date date, int count) {
        super();
        this.name = name;
        this.date = date;
        this.count = count;
    }

    public void setModels(SimpleModel... models) {
        this.models = models;
    }

    public SimpleModel[] getModels() {
        return models;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
