package us.wthr.jdem846.kml;

import org.dom4j.Element;

public class Vec2Type extends KmlElement {

    private double x = -1;

    private double y = -1;

    private XYUnitsEnum xUnits = XYUnitsEnum.FRACTION;

    private XYUnitsEnum yUnits = XYUnitsEnum.FRACTION;

    private String fieldName = "overlayXY";

    public Vec2Type() {
    }

    public Vec2Type(String fieldName) {
        setFieldName(fieldName);
    }

    public Vec2Type(double x, double y) {
        setX(x);
        setY(y);
    }

    public Vec2Type(String fieldName, double x, double y) {
        setFieldName(fieldName);
        setX(x);
        setY(y);
    }

    public Vec2Type(double x, double y, XYUnitsEnum xUnits, XYUnitsEnum yUnits) {
        setX(x);
        setY(y);
        setxUnits(xUnits);
        setyUnits(yUnits);
    }

    public Vec2Type(String fieldName, double x, double y, XYUnitsEnum xUnits, XYUnitsEnum yUnits) {
        setFieldName(fieldName);
        setX(x);
        setY(y);
        setxUnits(xUnits);
        setyUnits(yUnits);
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public XYUnitsEnum getxUnits() {
        return xUnits;
    }

    public void setxUnits(XYUnitsEnum xUnits) {
        this.xUnits = xUnits;
    }

    public XYUnitsEnum getyUnits() {
        return yUnits;
    }

    public void setyUnits(XYUnitsEnum yUnits) {
        this.yUnits = yUnits;
    }

    protected void loadKmlChildren(Element element) {
        super.loadKmlChildren(element);
        element.addAttribute("x", "" + x);
        element.addAttribute("y", "" + y);
        if (xUnits != null) {
            element.addAttribute("xUnits", xUnits.text());
        }
        if (yUnits != null) {
            element.addAttribute("yUnits", yUnits.text());
        }
    }

    public void toKml(Element parent) {
        if (fieldName == null) {
            fieldName = "overlayXY";
        }
        Element element = parent.addElement(fieldName);
        loadKmlChildren(element);
    }
}
