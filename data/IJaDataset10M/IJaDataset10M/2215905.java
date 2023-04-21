package org.sidora.core;

/**
 * Classe que representa una actuacio arqueologica
 * @author Enric Tartera, Juan Manuel Gimeno, Roger Masgoret
 * @version 1.0
 */
public class MeasureSet implements Cloneable {

    private double length;

    private double width;

    private double height;

    public MeasureSet() {
        this.setHeight(0);
        this.setLength(0);
        this.setWidth(0);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        MeasureSet clone = (MeasureSet) super.clone();
        clone.setHeight(getHeight());
        clone.setLength(getLength());
        clone.setWidth(getWidth());
        return clone;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
