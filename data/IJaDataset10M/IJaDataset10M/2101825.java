package playground.droeder.realTimeNavigation.movingObjects;

import org.matsim.api.core.v01.Id;
import playground.droeder.Vector2D;
import playground.droeder.realTimeNavigation.velocityObstacles.VelocityObstacle;
import com.vividsolutions.jts.geom.Geometry;

/**
 * @author droeder
 *
 */
public abstract class AbstractMovingObject implements MovingObject {

    private Vector2D currentPosition;

    private Vector2D newPosition;

    private Vector2D speed;

    private Id id;

    private Geometry geometry;

    private boolean positionUpdate = false;

    public AbstractMovingObject(Vector2D position, Id id, Geometry g) {
        this.id = id;
        this.currentPosition = position;
        this.geometry = g;
    }

    public Geometry getGeometry() {
        return this.geometry;
    }

    public Id getId() {
        return this.id;
    }

    public Vector2D getSpeed() {
        return this.speed;
    }

    public Vector2D getCurrentPosition() {
        return this.currentPosition;
    }

    protected void setCurrentPosition(Vector2D c) {
        this.currentPosition = c;
    }

    protected void setNewPosition(Vector2D c) {
        this.positionUpdate = true;
        this.newPosition = c;
    }

    public boolean processNextStep() {
        if (positionUpdate) {
            this.currentPosition = newPosition;
            this.positionUpdate = false;
            return true;
        } else {
            return false;
        }
    }

    protected void setSpeed(Vector2D v) {
        this.speed = v;
    }

    public abstract void calculateNextStep(double stepSize, VelocityObstacle obstacle);
}
