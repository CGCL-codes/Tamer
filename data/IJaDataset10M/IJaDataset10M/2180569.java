package skycastle.huntergatherers.model;

import skycastle.huntergatherers.model.health.Health;
import skycastle.huntergatherers.model.location.Location;
import skycastle.huntergatherers.model.sensor.Sensor;

/**
 * A physical object in the game world.  Has a position, movement, mass, a bounding sphere/box, appearance,
 * can be damaged, etc.
 * <p/>
 * CHECK: Should we separate non-physical but location objects?  What about roads, rivers, etc. that are larger,
 * but also location?  Well, introduce a new superinterface for those, between GameObject and Entity, when needed.
 *
 * @author Hans H�ggstr�m
 */
public interface Entity extends GameObject {

    /**
     * @return an interface for manipulating the health of this entity, afflicting damage to it, healing it,
     *         or directly manipulating the hitpoints.
     */
    Health getHealth();

    /**
     * @return an interface for manipulating the location of this entity.
     */
    Location getLocation();

    /**
     * @return the sensor associated with this entity.  Can be used for listening to close by entity
     *         appearances and dissappearances.
     */
    Sensor getSensor();
}
