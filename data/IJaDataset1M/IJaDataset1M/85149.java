package org.yaoqiang.bpmn.model.elements.bpmndi;

import org.yaoqiang.bpmn.model.elements.XMLAttribute;
import org.yaoqiang.bpmn.model.elements.XMLComplexElement;
import org.yaoqiang.bpmn.model.elements.XMLElement;

/**
 * Bounds
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class Bounds extends XMLComplexElement {

    public Bounds(XMLElement parent) {
        super(parent);
    }

    protected void fillStructure() {
        XMLAttribute attrX = new XMLAttribute(this, "x");
        XMLAttribute attrY = new XMLAttribute(this, "y");
        XMLAttribute attrWidth = new XMLAttribute(this, "width");
        XMLAttribute attrHeight = new XMLAttribute(this, "height");
        add(attrX);
        add(attrY);
        add(attrWidth);
        add(attrHeight);
    }

    public double getX() {
        try {
            return Double.parseDouble(get("x").toValue());
        } catch (Exception e) {
            return -1;
        }
    }

    public double getY() {
        try {
            return Double.parseDouble(get("y").toValue());
        } catch (Exception e) {
            return -1;
        }
    }

    public double getWidth() {
        try {
            return Double.parseDouble(get("width").toValue());
        } catch (Exception e) {
            return -1;
        }
    }

    public double getHeight() {
        try {
            return Double.parseDouble(get("height").toValue());
        } catch (Exception e) {
            return -1;
        }
    }

    public void setX(double x) {
        set("x", String.valueOf(x));
    }

    public void setY(double y) {
        set("y", String.valueOf(y));
    }

    public void setWidth(double width) {
        set("width", String.valueOf(width));
    }

    public void setHeight(double height) {
        set("height", String.valueOf(height));
    }
}
