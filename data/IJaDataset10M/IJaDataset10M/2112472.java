package org.galab.saveableobject.controller;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Image;
import java.io.*;
import org.galab.saveableobject.*;
import org.galab.util.*;

public class Node extends VisualObject {

    public Node() {
        super();
    }

    public Node(Polygon newShape, Color newColor, V2 newPos, Angle newDir, File newImageFile, SaveableObject newParent) {
        super(newShape, newColor, newPos, newDir, newImageFile, newParent);
        setActivation(0);
    }

    public Node(Node other) {
        super(other);
        setActivation(0);
    }

    public void setActivation(double newActivation) {
        activation = newActivation;
    }

    public void augmentActivation(double augActivation) {
        activation += augActivation;
    }

    public void resetActivation() {
        activation = 0;
    }

    public void storeCurrentActivation() {
        currentActivation = activation;
    }

    public double getCurrentActivation() {
        return currentActivation;
    }

    public double getActivation() {
        return activation;
    }

    public void setControllerComponent(ControllerComponent newComponent) {
        controllerComponent = newComponent;
    }

    public ControllerComponent getControllerComponent() {
        return controllerComponent;
    }

    public void setColorFromActivation() {
        setCurrentColor(Util.getColorFromValue(getCurrentActivation(), -8, 8));
    }

    private double activation;

    private double currentActivation;

    private ControllerComponent controllerComponent;
}
