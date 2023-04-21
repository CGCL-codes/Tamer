package org.km.xplane.airports.model;

import org.km.xplane.airports.parser.LineCodes;

public class Helipad extends PositionedObject {

    private String _heading;

    private String _length;

    private String _width;

    private String _designator;

    private int _surface;

    private int _markings;

    private int _shoulder;

    private String _smooth;

    private int _edgeLights;

    public Helipad(Airport airport) {
        super(airport);
    }

    public String getDesignator() {
        return _designator;
    }

    public void setDesignator(String designator) {
        _designator = designator;
        fireChanged(this);
    }

    public String getHeading() {
        return _heading;
    }

    public String getLength() {
        return _length;
    }

    public String getWidth() {
        return _width;
    }

    public void setPositionAndSize(String lat, String longitude, String heading, String length, String width) {
        setPosition(lat, longitude);
        _heading = heading;
        _length = length;
        _width = width;
        fireChanged(this);
    }

    public int getSurface() {
        return _surface;
    }

    public int getMarkings() {
        return _markings;
    }

    public int getShoulder() {
        return _shoulder;
    }

    public String getSmooth() {
        return _smooth;
    }

    public int getEdgeLights() {
        return _edgeLights;
    }

    public void setFlags(int surface, int markings, int shoulder, String smooth, int edgelights) {
        _surface = surface;
        _markings = markings;
        _shoulder = shoulder;
        _smooth = smooth;
        _edgeLights = edgelights;
        fireChanged(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(LineCodes.HELIPAD_LINE_CODE);
        builder.append(" ");
        builder.append(_designator);
        builder.append(" ");
        builder.append(super.toString());
        builder.append(" ");
        builder.append(_heading);
        builder.append(" ");
        builder.append(_length);
        builder.append(" ");
        builder.append(_width);
        builder.append(" ");
        builder.append(_surface);
        builder.append(" ");
        builder.append(_markings);
        builder.append(" ");
        builder.append(_shoulder);
        builder.append(" ");
        builder.append(_smooth);
        builder.append(" ");
        builder.append(_edgeLights);
        return builder.toString();
    }
}
