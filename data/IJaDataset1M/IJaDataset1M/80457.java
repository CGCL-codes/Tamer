package com.keithpower.gekmlib;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class ColorStyle extends ObjectNode {

    protected java.awt.Color color;

    private boolean isColorDirty;

    protected String colorMode;

    private boolean isColorModeDirty;

    public ColorStyle() {
        super();
    }

    public ColorStyle(Node parent) {
        super(parent);
    }

    public java.awt.Color getColor() {
        return this.color;
    }

    public void setColor(String hexValue) {
        if (hexValue.length() != 8) {
            return;
        }
        int alpha = Integer.valueOf(hexValue.substring(0, 2), 16).intValue();
        int r = Integer.valueOf(hexValue.substring(2, 4), 16).intValue();
        int g = Integer.valueOf(hexValue.substring(4, 6), 16).intValue();
        int b = Integer.valueOf(hexValue.substring(6, 8), 16).intValue();
        java.awt.Color newCol = new java.awt.Color(r, g, b, alpha);
        this.color = newCol;
        this.isColorDirty = true;
        setDirty();
    }

    public void setColor(java.awt.Color aColor) {
        this.color = aColor;
        this.isColorDirty = true;
        setDirty();
    }

    public String getColorMode() {
        return this.colorMode;
    }

    public void setColorMode(String value) {
        this.colorMode = value;
        this.isColorModeDirty = true;
        setDirty();
    }

    public String toKML() {
        return toKML(false);
    }

    public String toKML(boolean suppressEnclosingTags) {
        String kml = "";
        kml += super.toKML(true);
        if (this.color != null) {
            kml += "<color>" + SpecialCaseFormatter.toKMLString(this.color) + "</color>\n";
        }
        if (this.colorMode != null) {
            kml += "<colorMode>" + SpecialCaseFormatter.toKMLString(this.colorMode) + "</colorMode>\n";
        }
        return kml;
    }

    public String toUpdateKML() {
        return toUpdateKML(false);
    }

    public String toUpdateKML(boolean suppressEnclosingTags) {
        if (!isDirty()) {
            return "";
        }
        String change = "";
        change += super.toUpdateKML(true);
        if (this.color != null && this.isColorDirty) {
            change += "<color>" + SpecialCaseFormatter.toKMLString(this.color) + "</color>\n";
            this.isColorDirty = false;
        }
        if (this.colorMode != null && this.isColorModeDirty) {
            change += "<colorMode>" + SpecialCaseFormatter.toKMLString(this.colorMode) + "</colorMode>\n";
            this.isColorModeDirty = false;
        }
        setNotDirty();
        return change;
    }

    public Object clone() throws CloneNotSupportedException {
        ColorStyle result = (ColorStyle) super.clone();
        return result;
    }

    public void setRecursiveNotDirty() {
        super.setRecursiveNotDirty();
        this.isColorDirty = false;
        this.isColorModeDirty = false;
    }
}
