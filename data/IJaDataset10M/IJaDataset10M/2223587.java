package com.jplt.cobosoda;

/**
 *
 * @author Jacob Joaquin
 */
public class ProcessingModelScore {

    String fileName;

    double distance;

    String name;

    public ProcessingModelScore(String f, double d, String n) {
        fileName = f;
        distance = d;
        name = n;
    }

    public String getFileName() {
        return fileName;
    }

    public double getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }

    public Model getModel() {
        return World.loadModel("arcade/" + fileName);
    }
}
