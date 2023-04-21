package com.keithpower.gekmlib;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class BalloonStyle extends ObjectNode {

    protected java.awt.Color color;

    private boolean isColorDirty;

    protected java.awt.Color bgColor;

    private boolean isBgColorDirty;

    protected java.awt.Color textColor;

    private boolean isTextColorDirty;

    protected String text;

    private boolean isTextDirty;

    public BalloonStyle() {
        super();
    }

    public BalloonStyle(Node parent) {
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

    public java.awt.Color getBgColor() {
        return this.bgColor;
    }

    public void setBgColor(String hexValue) {
        if (hexValue.length() != 8) {
            return;
        }
        int alpha = Integer.valueOf(hexValue.substring(0, 2), 16).intValue();
        int r = Integer.valueOf(hexValue.substring(2, 4), 16).intValue();
        int g = Integer.valueOf(hexValue.substring(4, 6), 16).intValue();
        int b = Integer.valueOf(hexValue.substring(6, 8), 16).intValue();
        java.awt.Color newCol = new java.awt.Color(r, g, b, alpha);
        this.bgColor = newCol;
        this.isBgColorDirty = true;
        setDirty();
    }

    public void setBgColor(java.awt.Color aColor) {
        this.bgColor = aColor;
        this.isBgColorDirty = true;
        setDirty();
    }

    public java.awt.Color getTextColor() {
        return this.textColor;
    }

    public void setTextColor(String hexValue) {
        if (hexValue.length() != 8) {
            return;
        }
        int alpha = Integer.valueOf(hexValue.substring(0, 2), 16).intValue();
        int r = Integer.valueOf(hexValue.substring(2, 4), 16).intValue();
        int g = Integer.valueOf(hexValue.substring(4, 6), 16).intValue();
        int b = Integer.valueOf(hexValue.substring(6, 8), 16).intValue();
        java.awt.Color newCol = new java.awt.Color(r, g, b, alpha);
        this.textColor = newCol;
        this.isTextColorDirty = true;
        setDirty();
    }

    public void setTextColor(java.awt.Color aColor) {
        this.textColor = aColor;
        this.isTextColorDirty = true;
        setDirty();
    }

    public String getText() {
        return this.text;
    }

    public void setText(String value) {
        this.text = value;
        this.isTextDirty = true;
        setDirty();
    }

    public String toKML() {
        return toKML(false);
    }

    public String toKML(boolean suppressEnclosingTags) {
        String kml = "";
        if (!suppressEnclosingTags) {
            kml += "<BalloonStyle";
            if (this.id != null) {
                kml += " id=\"" + this.getId() + "\"";
            }
            if (this.targetId != null) {
                kml += " targetId=\"" + this.getTargetId() + "\"";
            }
            kml += ">\n";
        }
        kml += super.toKML(true);
        if (this.color != null) {
            kml += "<color>" + SpecialCaseFormatter.toKMLString(this.color) + "</color>\n";
        }
        if (this.bgColor != null) {
            kml += "<bgColor>" + SpecialCaseFormatter.toKMLString(this.bgColor) + "</bgColor>\n";
        }
        if (this.textColor != null) {
            kml += "<textColor>" + SpecialCaseFormatter.toKMLString(this.textColor) + "</textColor>\n";
        }
        if (this.text != null) {
            kml += "<text>" + SpecialCaseFormatter.toKMLString(this.text) + "</text>\n";
        }
        if (!suppressEnclosingTags) {
            kml += "</BalloonStyle>\n";
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
        boolean isPrimDirty = isPrimitiveDirty();
        if (isPrimDirty && !suppressEnclosingTags) {
            change += "<BalloonStyle";
            if (this.id != null) {
                change += " id=\"" + this.getId() + "\"";
            }
            if (this.targetId != null) {
                change += " targetId=\"" + this.getTargetId() + "\"";
            }
            change += ">\n";
        }
        change += super.toUpdateKML(true);
        if (this.color != null && this.isColorDirty) {
            change += "<color>" + SpecialCaseFormatter.toKMLString(this.color) + "</color>\n";
            this.isColorDirty = false;
        }
        if (this.bgColor != null && this.isBgColorDirty) {
            change += "<bgColor>" + SpecialCaseFormatter.toKMLString(this.bgColor) + "</bgColor>\n";
            this.isBgColorDirty = false;
        }
        if (this.textColor != null && this.isTextColorDirty) {
            change += "<textColor>" + SpecialCaseFormatter.toKMLString(this.textColor) + "</textColor>\n";
            this.isTextColorDirty = false;
        }
        if (this.text != null && this.isTextDirty) {
            change += "<text>" + SpecialCaseFormatter.toKMLString(this.text) + "</text>\n";
            this.isTextDirty = false;
        }
        if (isPrimDirty && !suppressEnclosingTags) {
            change += "</BalloonStyle>\n";
        }
        setNotDirty();
        return change;
    }

    public Object clone() throws CloneNotSupportedException {
        BalloonStyle result = (BalloonStyle) super.clone();
        return result;
    }

    public void setRecursiveNotDirty() {
        super.setRecursiveNotDirty();
        this.isColorDirty = false;
        this.isBgColorDirty = false;
        this.isTextColorDirty = false;
        this.isTextDirty = false;
    }
}
