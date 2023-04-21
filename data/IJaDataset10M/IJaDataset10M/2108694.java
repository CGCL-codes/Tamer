package org.mars_sim.msp.simulation.equipment;

import org.mars_sim.msp.simulation.*;
import org.mars_sim.msp.simulation.person.*;
import java.io.Serializable;

/** 
 * The EVASuit class represents an EVA suit which provides life support
 * for a person during a EVA operation.
 */
public class EVASuit extends Equipment implements LifeSupport, Serializable {

    private double BASE_MASS = 45D;

    private double OXYGEN_CAPACITY = 1D;

    private double WATER_CAPACITY = 4D;

    private double NORMAL_AIR_PRESSURE = 1D;

    private double NORMAL_TEMP = 25D;

    /**
     * Constructs a EVASuit object.
     */
    public EVASuit(Coordinates location, Mars mars) {
        super("EVA Suit", location, mars);
        malfunctionManager.addScopeString("EVASuit");
        malfunctionManager.addScopeString("LifeSupport");
        baseMass = BASE_MASS;
        inventory.setTotalCapacity(100D);
        inventory.setResourceCapacity(Resource.OXYGEN, OXYGEN_CAPACITY);
        inventory.setResourceCapacity(Resource.WATER, WATER_CAPACITY);
        inventory.addResource(Resource.OXYGEN, OXYGEN_CAPACITY);
        inventory.addResource(Resource.WATER, WATER_CAPACITY);
    }

    /** 
     * Returns true if life support is working properly and is not out
     * of oxygen or water.
     * @return true if life support is OK
     */
    public boolean lifeSupportCheck() {
        boolean result = true;
        if (inventory.getResourceMass(Resource.OXYGEN) <= 0D) {
            result = false;
        }
        if (inventory.getResourceMass(Resource.WATER) <= 0D) {
            result = false;
        }
        if (malfunctionManager.getOxygenFlowModifier() < 100D) {
            result = false;
        }
        if (malfunctionManager.getWaterFlowModifier() < 100D) {
            result = false;
        }
        if (getAirPressure() != NORMAL_AIR_PRESSURE) {
            result = false;
        }
        if (getTemperature() != NORMAL_TEMP) {
            result = false;
        }
        return result;
    }

    /** 
     * Gets the number of people the life support can provide for.
     * @return the capacity of the life support system.
     */
    public int getLifeSupportCapacity() {
        return 1;
    }

    /** 
     * Gets oxygen from system.
     * @param amountRequested the amount of oxygen requested from system (kg)
     * @return the amount of oxygen actually received from system (kg)
     */
    public double provideOxygen(double amountRequested) {
        return inventory.removeResource(Resource.OXYGEN, amountRequested) * (malfunctionManager.getOxygenFlowModifier() / 100D);
    }

    /**
     * Gets water from the system.
     * @param amountRequested the amount of water requested from system (kg)
     * @return the amount of water actually received from system (kg)
     */
    public double provideWater(double amountRequested) {
        return inventory.removeResource(Resource.WATER, amountRequested) * (malfunctionManager.getWaterFlowModifier() / 100D);
    }

    /**
     * Gets the air pressure of the life support system.
     * @return air pressure (atm)
     */
    public double getAirPressure() {
        double result = NORMAL_AIR_PRESSURE * (malfunctionManager.getAirPressureModifier() / 100D);
        double ambient = mars.getWeather().getAirPressure(location);
        if (result < ambient) return ambient; else return result;
    }

    /**
     * Gets the temperature of the life support system.
     * @return temperature (degrees C)
     */
    public double getTemperature() {
        double result = NORMAL_TEMP * (malfunctionManager.getTemperatureModifier() / 100D);
        double ambient = mars.getWeather().getTemperature(location);
        if (result < ambient) return ambient; else return result;
    }

    /** 
     * Checks to see if the inventory is at full capacity with oxygen and water.
     * @return true if oxygen and water stores at full capacity
     */
    public boolean isFullyLoaded() {
        boolean result = true;
        double oxygen = inventory.getResourceMass(Resource.OXYGEN);
        if (oxygen != OXYGEN_CAPACITY) result = false;
        double water = inventory.getResourceMass(Resource.WATER);
        if (water != WATER_CAPACITY) result = false;
        return result;
    }

    /**
     * Time passing for EVA suit.
     * @param time the amount of time passing (millisols)
     */
    public void timePassing(double time) {
        Unit container = getContainerUnit();
        if (container instanceof Person) malfunctionManager.activeTimePassing(time);
        malfunctionManager.timePassing(time);
    }

    /**
     * Gets a collection of people affected by this entity.
     * @return person collection
     */
    public PersonCollection getAffectedPeople() {
        PersonCollection people = super.getAffectedPeople();
        if (containerUnit instanceof Person) {
            if (!people.contains((Person) containerUnit)) people.add((Person) containerUnit);
        }
        return people;
    }
}
