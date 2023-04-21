package org.matsim.ptproject.qsim.helpers;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.mobsim.framework.PersonDriverAgent;
import org.matsim.ptproject.qsim.interfaces.QVehicle;
import org.matsim.vehicles.Vehicle;

public class QVehicleImpl implements QVehicle {

    private double linkEnterTime = Double.NaN;

    private double earliestLinkExitTime = 0;

    private PersonDriverAgent driver = null;

    private final Id id;

    private Link currentLink = null;

    private final double sizeInEquivalents;

    private final Vehicle basicVehicle;

    public QVehicleImpl(final Vehicle basicVehicle) {
        this(basicVehicle, 1.0);
    }

    public QVehicleImpl(final Vehicle basicVehicle, final double sizeInEquivalents) {
        this.id = basicVehicle.getId();
        this.sizeInEquivalents = sizeInEquivalents;
        this.basicVehicle = basicVehicle;
    }

    public double getLinkEnterTime() {
        return this.linkEnterTime;
    }

    public void setLinkEnterTime(final double time) {
        this.linkEnterTime = time;
    }

    public double getEarliestLinkExitTime() {
        return this.earliestLinkExitTime;
    }

    public void setEarliestLinkExitTime(final double time) {
        this.earliestLinkExitTime = time;
    }

    public Link getCurrentLink() {
        return this.currentLink;
    }

    public void setCurrentLink(final Link link) {
        this.currentLink = link;
    }

    public PersonDriverAgent getDriver() {
        return this.driver;
    }

    public void setDriver(final PersonDriverAgent driver) {
        this.driver = driver;
    }

    public Id getId() {
        return this.id;
    }

    public double getSizeInEquivalents() {
        return this.sizeInEquivalents;
    }

    public Vehicle getVehicle() {
        return this.basicVehicle;
    }

    @Override
    public String toString() {
        return "Vehicle Id " + getId() + ", driven by (personId) " + this.driver.getPerson().getId() + ", on link " + this.currentLink.getId();
    }
}
