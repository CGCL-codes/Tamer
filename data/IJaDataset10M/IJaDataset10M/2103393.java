package org.theo.aftokinito;

import java.awt.Graphics2D;
import org.cove.ape.Vector;
import org.theo.aftokinito.Car;

public class Scroll {

    public double offsetX = 0;

    public double offsetY = 0;

    private double offsetXBy = 0;

    private double offsetYBy = 0;

    private Vector boundX = new Vector(0, 0);

    private Vector boundY = new Vector(0, 0);

    private int maxScroll = 50;

    private int divideScreen = 20;

    private Car _focusObject;

    public Scroll() {
    }

    public void setFocusOn(Car focusObject) {
        _focusObject = focusObject;
    }

    public void init(double posX, double posY) {
    }

    public void step() {
        boundX.x = 300 - (_focusObject.getVelocity().x * 20);
        boundX.y = 400 - (_focusObject.getVelocity().x * 20);
        boundY.x = 300;
        boundY.y = 340;
        if ((offsetX + _focusObject.getpx()) < boundX.x) offsetXBy = maxScroll * Math.abs(((offsetX + _focusObject.getpx()) / (boundX.x)) - 1); else if ((offsetX + _focusObject.getpx()) > boundX.y) {
            offsetXBy = -maxScroll * Math.abs(((offsetX + _focusObject.getpx()) / (boundX.y)) - 1);
        }
        if ((offsetY + _focusObject.getpy()) < boundY.x) offsetYBy = maxScroll * Math.abs(((offsetY + _focusObject.getpy()) / (boundY.x)) - 1); else if ((offsetY + _focusObject.getpy()) > boundY.y) {
            offsetYBy = -maxScroll * Math.abs(((offsetY + _focusObject.getpy()) / (boundY.y)) - 1);
        }
        offsetX = offsetX + offsetXBy;
        offsetY = offsetY + offsetYBy;
    }

    public void paint(Graphics2D g) {
    }
}
