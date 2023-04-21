package games.simulation.flyer.interfaces.objectsFamily.airPlane.fuelTank;

import games.simulation.flyer.interfaces.objectsFamily.objectsWorld.ObjectsWorldSetable;

/**
 * @author Diego
 * @email diego.costa@si.unifacs.br
 * @14/04/2007 14:19:13
 * @Interface to create an FuelTank to an Airplane.
 */
public interface FuelTank extends ObjectsWorldSetable {

    /**
	 * Set the MAX CAPACITY of FuelTank.
	 * 
	 * @param MAX_CAPACITY
	 */
    public void setMaxCapacity(Double MAX_CAPACITY);

    /**
	 * @return the FuelTank MAX CAPACITY
	 */
    public Double getMaxCapacity();

    /**
	 * @return the curren value of fuel.
	 */
    public Double getCurrentFuelStats();

    /**
	 * @return the String that Inform if the fuel stats is LOW, MEDIUM, FULL.
	 */
    public String getLowMedHigFuelStats();

    /**
	 * Incress fuel on FuelTank.
	 * 
	 * @param fuel
	 */
    public void fillFluelTank(Double fuel);

    /**
	 * Decress some fuel value.
	 */
    public void descressFuel(Double fuel);

    /**
	 * Decess some fuel value based on Engine Potency.
	 * 
	 * @param potency
	 * @return <to complete>
	 */
    public Double fuelToEngine(Double potency);

    /**
	 * @param currentCapacity
	 *            the currentCapacity to set
	 */
    public void setCurrentCapacity(Double currentCapacity);

    /**
	 * @return the currentCapacity
	 */
    public Double getCurrentCapacity();
}
