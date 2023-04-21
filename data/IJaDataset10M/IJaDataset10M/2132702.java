package com.hp.hpl.guess.ui;

public interface NodeListener extends GraphElementListener {

    public void setLocation(double x, double y);

    public void setLocation(double x, double y, double width, double height);

    public void addFieldToLabel(String field);

    public void removeFieldFromLabel(String field);
}
