package com.isa.jump.plugin;

import com.vividsolutions.jts.geom.Coordinate;

public class Circle extends Arc {

    public Circle(Coordinate center, double radius) {
        super(center, new Coordinate(center.x + radius, center.y), 360.0);
    }

    public Circle(Coordinate center, double radius, double arcTolerance) {
        this(center, radius);
        this.arcTolerance = arcTolerance;
    }
}
