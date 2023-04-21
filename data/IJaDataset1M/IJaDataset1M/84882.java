package com.codefactory.gwt.client;

public class ColorPickerColor {

    protected String RGBvalue;

    protected String title;

    protected String id;

    public ColorPickerColor(String rgb, String title) {
        this.RGBvalue = rgb;
        this.title = title;
    }

    public ColorPickerColor(String rgb, String title, String id) {
        this.RGBvalue = rgb;
        this.title = title;
        this.id = id;
    }

    public String getRGBvalue() {
        return (RGBvalue == null) ? "" : RGBvalue;
    }

    public String getTitle() {
        return (title == null) ? "" : title;
    }

    public String getId() {
        return (id == null) ? "" : id;
    }
}
