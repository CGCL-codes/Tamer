package com.itextpdf.tool.xml.svg.graphic;

import java.util.Map;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.tool.xml.svg.tags.Graphic;

public class Circle extends Graphic {

    float x, y, radius;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
        return radius;
    }

    public Circle(float x, float y, float radius, Map<String, String> css) {
        super(css);
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public void draw(PdfContentByte cb) {
        cb.circle(x, y, radius);
    }
}
