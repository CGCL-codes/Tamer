package ru.cos.sim.vehicle;

import ru.cos.sim.agents.TrafficAgent;
import ru.cos.sim.road.objects.RectangleRoadObject;

/**
 * General vehicle.
 * @author zroslaw
 */
public interface Vehicle extends RectangleRoadObject, TrafficAgent {

    public enum VehicleClass {

        /**
		 * Lightweight automobile
		 */
        Car, /**
		 * Heavyweight vehicle
		 */
        Truck
    }

    public enum VehicleType {

        RegularVehicle
    }

    /**
	 * Get vehicle id.<br>
	 * Vehicle id is the unique string that identify this vehicle.
	 * For example, it may be vehicle's plate number.
	 * @return vehicle unique id
	 */
    public String getVehicleId();

    /**
	 * Get vehicle's speed
	 * @return vehicle's speed
	 */
    @Override
    public float getSpeed();

    /**
	 * Get vehicle type
	 * @return vehicle type
	 */
    public VehicleType getVehicleType();

    /**
	 * Get vehicle's class
	 * @return vehicle's class
	 */
    public VehicleClass getVehicleClass();

    /**
	 * Get half the vehicle's length.<br>
	 * Just for convenience.
	 * @return getLentg()/2.0
	 */
    public float getHalfLength();

    /**
	 * Returns vehicle's current travel time
	 * @return vehicle's travel time
	 */
    public float getTravelTime();

    /**
	 * Return current travel distance, odometer
	 * @return vehicle's travel distance
	 */
    public float getTravelDistance();
}
