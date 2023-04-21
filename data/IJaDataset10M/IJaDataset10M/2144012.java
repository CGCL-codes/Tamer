package com.timothy.chart;

public class Bar3D extends Bar2D {

    private int thickness;

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public Bar3D(int thickness) {
        super();
        this.thickness = thickness;
    }
}
