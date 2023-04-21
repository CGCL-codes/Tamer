package com.google.api.chart;

public final class AxisStyleMaker {

    public static AxisStyleMaker style() {
        return new AxisStyleMaker();
    }

    private int alignment = -1;

    private int size = -1;

    private ColorMaker color = ColorMaker.EMPTY;

    private ColorMaker tickmarkColor = null;

    private String drawingControl = "";

    private int paramLevel = 0;

    private AxisStyleMaker() {
    }

    private void updateParamLevel(int newLevel) {
        if (newLevel > paramLevel) paramLevel = newLevel;
    }

    @Override
    public String toString() {
        String r = color.toString();
        if (paramLevel >= 1) r += "," + size;
        if (paramLevel >= 2) r += "," + alignment;
        if (paramLevel >= 3) r += "," + drawingControl;
        if (paramLevel >= 4) r += "," + tickmarkColor.toString();
        return r;
    }

    public AxisStyleMaker color(ColorMaker color) {
        this.color = color;
        return this;
    }

    public AxisStyleMaker fontSize(int size) {
        this.size = size;
        updateParamLevel(1);
        return this;
    }

    public AxisStyleMaker alignLeft() {
        this.alignment = -1;
        updateParamLevel(2);
        return this;
    }

    public AxisStyleMaker alignCenter() {
        this.alignment = 0;
        updateParamLevel(2);
        return this;
    }

    public AxisStyleMaker alignRight() {
        this.alignment = 1;
        updateParamLevel(2);
        return this;
    }

    public AxisStyleMaker tickmarkColor(ColorMaker color) {
        this.tickmarkColor = color;
        updateParamLevel(4);
        return this;
    }

    public AxisStyleMaker drawLinesOnly() {
        this.drawingControl = "l";
        updateParamLevel(3);
        return this;
    }

    public AxisStyleMaker drawTickmarksOnly() {
        this.drawingControl = "t";
        updateParamLevel(3);
        return this;
    }

    public AxisStyleMaker drawBoth() {
        this.drawingControl = "lt";
        updateParamLevel(3);
        return this;
    }
}
