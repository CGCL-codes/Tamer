package progl.test;

import processing.core.PApplet;
import progl.ProGLGraphics;
import progl.ProGLShape;

public class LightningTestQuad extends PApplet {

    ProGLGraphics g;

    public void setup() {
        size(800, 600, ProGLGraphics.graphics());
        g = (ProGLGraphics) super.g;
        smooth();
        noStroke();
        fill(255, 102, 153);
    }

    float angle = 0;

    public void draw() {
        background(0);
        lights();
        angle += 0.01;
        translate(400, 300, -200);
        rotateX(PI / 4f);
        rotateY(angle);
        beginShape(QUAD_STRIP);
        fill(102, 153, 255);
        vertex(200, 200, 200);
        vertex(200, -200, 200);
        vertex(-200, 200, 200);
        vertex(-200, -200, 200);
        fill(255, 102, 153);
        vertex(-200, 200, -200);
        vertex(-200, -200, -200);
        vertex(200, 200, -200);
        vertex(200, -200, -200);
        vertex(200, 200, 200);
        vertex(200, -200, 200);
        endShape();
        println(frameRate);
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { LightningTestQuad.class.getName() });
    }
}
