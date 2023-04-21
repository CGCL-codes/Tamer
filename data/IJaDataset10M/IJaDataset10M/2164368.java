package org.matsim.ptproject.qsim.interfaces;

import org.matsim.api.core.v01.Identifiable;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.mobsim.framework.PersonDriverAgent;
import org.matsim.vehicles.Vehicle;
import org.matsim.vis.snapshots.writers.VisVehicle;

public interface QVehicle extends Identifiable, VisVehicle {

    public PersonDriverAgent getDriver();

    public void setDriver(final PersonDriverAgent driver);

    public Link getCurrentLink();

    public void setCurrentLink(final Link link);

    public double getSizeInEquivalents();

    public double getLinkEnterTime();

    public void setLinkEnterTime(final double time);

    public double getEarliestLinkExitTime();

    public void setEarliestLinkExitTime(final double time);

    /**
	 * @return the <code>Vehicle</code> that this simulation vehicle represents
	 */
    public Vehicle getVehicle();
}
