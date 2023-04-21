package com.ynhenc.gis.projection;

import com.ynhenc.gis.model.shape.PntShort;

public class Wgs extends PntShort {

    private Wgs(double x, double y) {
        super(x, y);
    }

    public static Wgs wgs(double x, double y) {
        return new Wgs(x, y);
    }
}
