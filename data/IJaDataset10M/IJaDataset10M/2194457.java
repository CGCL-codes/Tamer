package org.processing.examples.demodrama.scenes;

import processing.core.*;
import processing.xml.*;
import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;
import java.util.regex.*;

public class Intro implements Scene {

    float angleZ = 0;

    float angleY = 0;

    float scaleF = 1.0f;

    PImage face;

    PImage fondo;

    PImage titulo;

    PImage negro;

    PGraphics light;

    public void setup(PApplet pApplet) {
        face = pApplet.loadImage("face.png");
        fondo = pApplet.loadImage("2.png");
        negro = pApplet.loadImage("negro.jpg");
        negro.resize(fondo.width, fondo.height);
        titulo = pApplet.loadImage("1a.jpg");
        titulo.resize(fondo.width, fondo.height);
        light = pApplet.createGraphics(1024, 768, pApplet.P3D);
    }

    public void draw(PApplet applet) {
        light.beginDraw();
        light.background(0);
        light.stroke(255);
        light.rect(applet.mouseX, applet.mouseY, 160, 100);
        light.endDraw();
        titulo.mask(light);
        applet.image(titulo, -face.width, -face.width, applet.width + face.width * 2, applet.height + face.width * 2);
    }

    public void mousePressed() {
    }

    public void keyPressed(char key) {
        switch(key) {
            case 'o':
                angleZ -= 3;
                break;
            case 'p':
                angleZ += 3;
                break;
            case ',':
                angleY -= 3;
                break;
            case '.':
                angleY += 3;
                break;
            case 'q':
                scaleF -= 3;
                break;
            case 'a':
                scaleF += 3;
                break;
        }
    }
}
