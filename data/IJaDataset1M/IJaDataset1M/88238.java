package com.hp.hpl.guess.ui;

import com.hp.hpl.guess.Node;

public class DumbNodeListener implements NodeListener {

    private double x = 500;

    private double y = 500;

    private double width = 10;

    private double height = 10;

    public DumbNodeListener(Node n) {
    }

    public void set(String field, Object o) {
        if (field.equals("x")) {
            x = ((Double) o).doubleValue();
        } else if (field.equals("y")) {
            y = ((Double) o).doubleValue();
        } else if (field.equals("width")) {
            width = ((Double) o).doubleValue();
        } else if (field.equals("height")) {
            height = ((Double) o).doubleValue();
        }
    }

    public Object get(String field) {
        if (field.equals("x")) {
            return (new Double(x));
        } else if (field.equals("y")) {
            return (new Double(y));
        } else if (field.equals("width")) {
            return (new Double(width));
        } else if (field.equals("height")) {
            return (new Double(height));
        }
        return null;
    }

    public void highlight(boolean state) {
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setLocation(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void addFieldToLabel(String field) {
        System.out.println("Not implemented yet");
    }

    public void removeFieldFromLabel(String field) {
        System.out.println("Not implemented yet");
    }
}
