package net.sf.adastra.model;

public interface Updateable {

    void beforeStep(float timeStep);

    void afterStep(float timeStep);
}
