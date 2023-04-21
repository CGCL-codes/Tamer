package org.matsim.mobsim.queuesim;

import java.util.List;
import org.matsim.interfaces.basic.v01.population.BasicPlanElement;
import org.matsim.interfaces.core.v01.Leg;
import org.matsim.interfaces.core.v01.Link;
import org.matsim.interfaces.core.v01.Person;

public interface DriverAgent {

    public void setVehicle(final QueueVehicle veh);

    public void setCurrentLink(final Link link);

    public Link getCurrentLink();

    public Link getDestinationLink();

    public void incCurrentNode();

    /**
	 * Returns the next link the vehicle will drive along.
	 *
	 * @return The next link the vehicle will drive on, or null if an error has happened.
	 */
    public Link chooseNextLink();

    public Person getPerson();

    public void reachActivity(final double now, final QueueLink currentQueueLink);

    public void leaveActivity(final double now);

    public Leg getCurrentLeg();

    public int getCurrentNodeIndex();

    public int getNextActivity();

    public List<? extends BasicPlanElement> getActsLegs();
}
