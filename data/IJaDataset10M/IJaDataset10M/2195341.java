package org.matsim.core.basic.v01.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.EnumSet;
import java.util.Set;
import org.matsim.api.basic.v01.Coord;
import org.matsim.api.basic.v01.Id;
import org.matsim.api.basic.v01.TransportMode;
import org.matsim.api.basic.v01.network.BasicLink;
import org.matsim.api.basic.v01.network.BasicNode;
import org.matsim.core.network.NetworkLayer;
import org.matsim.core.utils.geometry.CoordImpl;
import org.matsim.core.utils.geometry.CoordUtils;
import org.matsim.world.AbstractLocation;

public class BasicLinkImpl extends AbstractLocation implements BasicLink {

    protected BasicNode from = null;

    protected BasicNode to = null;

    protected double length = Double.NaN;

    protected double freespeed = Double.NaN;

    protected double capacity = Double.NaN;

    protected double nofLanes = Double.NaN;

    protected EnumSet<TransportMode> allowedModes = EnumSet.of(TransportMode.car);

    protected BasicLinkImpl(final NetworkLayer network, final Id id, final BasicNode from, final BasicNode to) {
        super(network, id, new CoordImpl(0.5 * (from.getCoord().getX() + to.getCoord().getX()), 0.5 * (from.getCoord().getY() + to.getCoord().getY())));
        this.from = from;
        this.to = to;
    }

    @Override
    public double calcDistance(final Coord coord) {
        return CoordUtils.calcDistance(this.center, coord);
    }

    public BasicNode getFromNode() {
        return this.from;
    }

    public BasicNode getToNode() {
        return this.to;
    }

    public final boolean setFromNode(final BasicNode node) {
        this.from = node;
        return true;
    }

    public final boolean setToNode(final BasicNode node) {
        this.to = node;
        return true;
    }

    /**
	 * This method returns the capacity as set in the xml defining the network. Be aware
	 * that this capacity is not normalized in time, it depends on the period set
	 * in the network file (the capperiod attribute).
	 *
 	 * @param time - the current time
	 * @return the capacity per network's capperiod timestep
	 */
    public double getCapacity(final double time) {
        return this.capacity;
    }

    public void setCapacity(final double capacity) {
        this.capacity = capacity;
    }

    /**
	 * This method returns the freespeed velocity in meter per seconds.
	 *
	 * @param time - the current time
	 * @return freespeed
	 */
    public double getFreespeed(final double time) {
        return this.freespeed;
    }

    /**
	 * Sets the freespeed velocity of the link in meter per seconds.
	 */
    public void setFreespeed(final double freespeed) {
        this.freespeed = freespeed;
    }

    public double getLength() {
        return this.length;
    }

    public void setLength(final double length) {
        this.length = length;
    }

    public double getNumberOfLanes(final double time) {
        return this.nofLanes;
    }

    public void setNumberOfLanes(final double lanes) {
        this.nofLanes = lanes;
    }

    public final void setAllowedModes(final Set<TransportMode> modes) {
        this.allowedModes.clear();
        this.allowedModes.addAll(modes);
    }

    public final Set<TransportMode> getAllowedModes() {
        return this.allowedModes.clone();
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        this.from.addOutLink(this);
        this.to.addInLink(this);
    }
}
