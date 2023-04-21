package samplegame.planets;

import geom.PolygonOrCircle;
import java.awt.geom.Rectangle2D;
import physics.MovingEntity;
import physics.RangedForceEntity;

public abstract class BasePlanet extends RangedForceEntity {

    public BasePlanet(double planetRadius, double planetMass) {
        super(null, new PolygonOrCircle(0, 0, planetRadius), planetMass / 10);
        this.setMass(planetMass);
        this.setVisible(true);
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    @Override
    public boolean IntersectsWith(MovingEntity entity) {
        if (entity == this) return false;
        return true;
    }

    @Override
    public Rectangle2D getEffectiveBounds() {
        return null;
    }
}
