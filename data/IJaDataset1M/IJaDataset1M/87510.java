package org.processing.examples;

import java.awt.Point;
import processing.core.PApplet;

public class TrianguleFlower extends PApplet {

    private static final long serialVersionUID = 7818843151186088642L;

    Point[] p = new Point[3];

    float shift = 1F;

    float fade = 0;

    float fillCol = 0;

    float rot = 0;

    float spin = 0;

    int color = 0xFF00AABB;

    public void setup() {
        super.setup();
        size(200, 200);
        background(0);
        smooth();
        fade = 255 / (width / 3F / shift);
        spin = 360 / (width / 2.0F / shift);
        p[0] = new Point(-width / 2, height / 2);
        p[1] = new Point(width / 2, height / 2);
        p[2] = new Point(0, -height / 2);
        noStroke();
        translate(width / 2, height / 2);
        triBlur();
    }

    private void triBlur() {
        fill(fillCol);
        fillCol += fade;
        rotate(spin);
        triangle(p[0].x += shift, p[0].y -= shift / 2, p[1].x -= shift, p[1].y -= shift / 2, p[2].x, p[2].y += shift);
        if (p[0].x < 0) {
            triBlur();
        }
    }
}
