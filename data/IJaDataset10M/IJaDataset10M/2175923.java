package org.jazzteam.edu.oop.interfaceJob;

public class Pencill implements MyInterface {

    private int resourse;

    private int width;

    private String name;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResourse() {
        return resourse;
    }

    public void setResourse(int resourse) {
        this.resourse = resourse;
    }

    public Pencill(int resourse, int width, String name) {
        super();
        this.resourse = resourse;
        this.width = width;
        this.name = name;
    }

    @Override
    public void write() {
        resourse--;
    }
}
