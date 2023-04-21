package nskhan.utilities;

import java.util.ArrayList;
import ecologylab.xml.ElementState;

public class Stroke extends ElementState {

    public class StrokeTypes {

        public static final String UNDEFINED = "undefined";

        public static final String TEXT = "text";

        public static final String SHAPE = "shape";
    }

    int id = 1;

    @xml_attribute
    String strokeType = null;

    @xml_collection("Point")
    ArrayList<Point> points = null;

    @xml_attribute
    String beautified = "false";

    @xml_attribute
    int type = -1;

    public static int autoId = 1;

    public Stroke() {
        strokeType = StrokeTypes.UNDEFINED;
        this.points = new ArrayList<Point>();
        this.id = autoId++;
    }

    public void beginStroke() {
        points = new ArrayList<Point>();
    }

    public void endStroke() {
    }

    public void addPoint(Point p) {
        points.add(p);
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public int getId() {
        return id;
    }

    public String getStrokeType() {
        return strokeType;
    }

    public void setStrokeType(String strokeType) {
        this.strokeType = strokeType;
    }
}
