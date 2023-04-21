package playground.mrieser.core.mobsim.transit;

import java.util.Collection;
import org.matsim.core.mobsim.qsim.pt.PassengerAgent;
import org.matsim.core.mobsim.qsim.pt.TransitStopHandler;
import playground.mrieser.core.mobsim.api.MobsimVehicle;

public interface TransitMobsimVehicle extends MobsimVehicle {

    /**
	 * Adds a passenger to this vehicle.
	 *
	 * @param passenger
	 * @return <tt>true</tt> when the agent was added as a passenger (as per the general contract of the Collection.add method).
	 */
    public boolean addPassenger(final PassengerAgent passenger);

    /**
	 * Removes the passenger from this vehicle.
	 *
	 * @param passenger
	 * @return <tt>true</tt> when the agent was removed as a passenger, <tt>false</tt> if the agent was not a passenger of this vehicle or could not be removed for other reasons
	 */
    public boolean removePassenger(final PassengerAgent passenger);

    /**
	 * @return an immutable Collection of all passengers in this vehicle
	 */
    public Collection<PassengerAgent> getPassengers();

    /**
	 * @return the still available capacity in the vehicle.
	 */
    public double getFreeCapacity();

    public TransitStopHandler getStopHandler();
}
