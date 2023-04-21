package skycastle.huntergatherers.model;

import skycastle.huntergatherers.model.health.Health;
import skycastle.huntergatherers.model.health.HealthImpl;
import skycastle.huntergatherers.model.location.Location;
import skycastle.huntergatherers.model.location.LocationImpl;
import skycastle.huntergatherers.model.sensor.Sensor;
import skycastle.huntergatherers.model.sensor.SensorImpl;

/**
 * Entity implementation.  Delegates the method calls to various specialized classes that manage specific aspects of the
 * entity functionality.
 *
 * @author Hans H�ggstr�m
 */
public class EntityImpl extends GameObjectImpl implements Entity {

    private final Health myHealth;

    private final Location myLocation;

    private final Sensor mySensor;

    public EntityImpl() {
        myHealth = new HealthImpl();
        myLocation = new LocationImpl();
        mySensor = new SensorImpl(myLocation);
    }

    public Health getHealth() {
        return myHealth;
    }

    public Location getLocation() {
        return myLocation;
    }

    public Sensor getSensor() {
        return mySensor;
    }
}
