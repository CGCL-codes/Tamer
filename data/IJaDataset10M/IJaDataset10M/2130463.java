package com.st.rrd.model;

import java.awt.Paint;

public class ColoredText {

    private Paint color;

    private String text;

    public ColoredText(Paint color, String text) {
        super();
        this.color = color;
        this.text = text;
    }

    public Paint getColor() {
        return color;
    }

    public void setColor(Paint color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
