package org.matsim.utils.vis.netvis.visNet;

import java.util.Map;
import java.util.TreeMap;
import org.matsim.basic.v01.Id;
import org.matsim.interfaces.networks.basicNet.BasicLink;
import org.matsim.interfaces.networks.basicNet.BasicNode;
import org.matsim.utils.geometry.CoordI;
import org.matsim.utils.vis.netvis.drawableNet.DrawableNodeI;

/**
 * @author gunnar
 */
public class DisplayNode implements DrawableNodeI, BasicNode {

    public static final double RADIUS_M = 5;

    private final Map<Id, BasicLink> inLinks;

    private final Map<Id, BasicLink> outLinks;

    private CoordI coord = null;

    private double displayValue;

    private String displayLabel;

    protected Id id;

    public DisplayNode(Id id, DisplayNet network) {
        this.id = id;
        inLinks = new TreeMap<Id, BasicLink>();
        outLinks = new TreeMap<Id, BasicLink>();
    }

    public void setDisplayValue(double value) {
        this.displayValue = value;
    }

    public void setDisplayText(String label) {
        this.displayLabel = label;
    }

    public boolean addInLink(BasicLink link) {
        inLinks.put(link.getId(), link);
        return true;
    }

    public boolean addOutLink(BasicLink link) {
        outLinks.put(link.getId(), link);
        return true;
    }

    public Map<Id, ? extends BasicLink> getInLinks() {
        return inLinks;
    }

    public Map<Id, ? extends BasicLink> getOutLinks() {
        return outLinks;
    }

    public void setCoord(CoordI coord) {
        this.coord = coord;
    }

    public CoordI getCoord() {
        return coord;
    }

    public double getDisplayValue() {
        return displayValue;
    }

    public String getDisplayText() {
        return displayLabel;
    }

    public double getNorthing() {
        return getCoord().getY();
    }

    public double getEasting() {
        return getCoord().getX();
    }

    @Override
    public String toString() {
        return super.toString() + " at (" + getEasting() + "/" + getNorthing() + ")";
    }

    public Id getId() {
        return this.id;
    }
}
