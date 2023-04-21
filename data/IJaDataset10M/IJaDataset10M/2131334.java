package de.fhkl.vr.particlespace.model;

/**
 *
 * @author stefan
 */
public class CirclicEventHorizon implements IEventHorizonTester {

    private double radius = 0;

    public CirclicEventHorizon(double radius) {
        this.radius = radius;
    }

    public boolean isObjectInsideEventHorizon(IParticleSpaceObject obj, Point2 gmPosition) {
        return (obj.getPosition().getDistanceToPoint(gmPosition) <= this.radius);
    }
}
