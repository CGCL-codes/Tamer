package locusts.server.behaviours;

import javax.vecmath.Tuple2d;
import javax.vecmath.Tuple2f;
import javax.vecmath.Vector2d;
import locusts.common.entities.Actor;

/**
 * Steering towards a target point.
 *
 * @author Hamish Morgan Morgan
 */
public class Seek extends AbstractBehaviour {

    protected static final double DEFAULT_MAX_DISTANCE = Double.POSITIVE_INFINITY;

    protected Tuple2d target;

    protected double maxDistance;

    public Seek(double weight, Tuple2d target, double maxDistance) {
        super(weight);
        setTarget(target);
        this.maxDistance = maxDistance;
    }

    public Seek(double weight, Vector2d target) {
        this(weight, target, DEFAULT_MAX_DISTANCE);
    }

    public Seek(Vector2d target) {
        super();
        setTarget(target);
        this.maxDistance = DEFAULT_MAX_DISTANCE;
    }

    public void setTarget(Tuple2d target) {
        if (target == null) throw new IllegalArgumentException();
        this.target = target;
    }

    public void clearTarget() {
        this.target = null;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public Vector2d getSteering(Actor boid) {
        Vector2d steering = new Vector2d();
        if (getWeight() == 0) return steering;
        if (target == null) return steering;
        double dx = target.x - boid.getX();
        double dy = target.y - boid.getY();
        double d = (dx * dx + dy * dy);
        if (d > maxDistance * maxDistance) return steering;
        steering.set(dx * (1 / d), dy * (1 / d));
        steering.scale(getWeight());
        return steering;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Seek other = (Seek) obj;
        if (this.target != other.target && (this.target == null || !this.target.equals(other.target))) return false;
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 29 * hash + (this.target != null ? this.target.hashCode() : 0);
        return hash;
    }
}
