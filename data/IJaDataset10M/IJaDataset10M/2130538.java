package net.sf.vorg.routecalculator.models;

import net.sf.vorg.core.VORGConstants;
import net.sf.vorg.core.models.GeoLocation;
import net.sf.vorg.core.models.Polars;
import net.sf.vorg.core.models.SailConfiguration;
import net.sf.vorg.core.models.WindCell;
import net.sf.vorg.core.singletons.FormatSingletons;
import net.sf.vorg.routecalculator.core.AbstractRouteCell;

/**
 * A RouteCell defines the geographical part of a route that compresses a single wind cell. Contains the
 * reference points and the wind cell that will be used at the time the cell is crossed. And performs all the
 * mathematical calculations needed to time the route and manage the angles.<br>
 * This type of cell only controls the calculations when the route leg fits completely on a single wind cell.
 * If there is a wind change during the time used to cross the cell, it will be replaced at the ProxyCell by
 * another version for multi-wind cells.
 */
public class RouteCell extends AbstractRouteCell {

    /** The wind cell contained on this route cell. Contains the wind information and location. */
    protected WindCell cell;

    /** Geographical location when entering the cell. */
    protected GeoLocation entryLocation;

    /** Geographical location when exiting the cell. */
    protected GeoLocation exitLocation;

    /** Difference between the exit and entry latitudes. */
    private double deltaLat;

    /** Difference between the exit and entry longitudes. */
    private double deltaLon;

    protected int alpha;

    private int apparentHeading;

    /**
	 * Time To Cross the cell. The elapsed time to move from the entry to the exit at the current speed. This
	 * time is expressed in hours.
	 */
    private double ttc;

    /** The speed reached depending on the wind angle, wind direction and heading. */
    private double speed;

    /** The recommended sail to reach the speed. */
    private Object sail;

    private double loxDistance;

    private RouteControl leftControl;

    private RouteControl rightControl;

    /** Empty constructor needed to create empty cells before entry and exit point initializations. */
    public RouteCell() {
    }

    /** Load the cell data with the required wind information and the entry and exit locations. */
    public RouteCell(final WindCell windCell, final GeoLocation entry, final GeoLocation exit) {
        cell = windCell;
        entryLocation = entry;
        exitLocation = exit;
        this.calculateCell();
    }

    public int calcApparent() {
        int angle = Math.abs(cell.getWindDir() - alpha);
        if (angle > 180) angle = angle - 360;
        return Math.abs(angle);
    }

    @Override
    public void optimize() {
    }

    public void calculateCell() {
        deltaLat = exitLocation.getLat() - entryLocation.getLat();
        deltaLon = exitLocation.getLon() - entryLocation.getLon();
        if (Math.abs(deltaLon) > 180.0) deltaLon = 360.0 - (Math.abs(deltaLon) * Math.signum(deltaLon));
        if ((deltaLat == 0.0) && (deltaLon == 0.0)) {
            alpha = 0;
            loxDistance = 0.0;
            ttc = 0.0;
            return;
        }
        loxDistance = this.entryLocation.distance(exitLocation);
        alpha = new Long(Math.round(entryLocation.angleTo(exitLocation))).intValue();
        apparentHeading = this.calcApparent();
        final SailConfiguration configuration = Polars.lookup(apparentHeading, cell.getWindSpeed());
        speed = configuration.getSpeed();
        sail = configuration.getSail();
        if (0.0 == speed) ttc = Double.POSITIVE_INFINITY; else ttc = loxDistance / speed;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getApparent() {
        return apparentHeading;
    }

    @Override
    public WindCell getCell() {
        return cell;
    }

    public double getDistance() {
        return loxDistance;
    }

    @Override
    public GeoLocation getEntryLocation() {
        return entryLocation;
    }

    @Override
    public GeoLocation getExitLocation() {
        return exitLocation;
    }

    public double getSpeed() {
        return speed;
    }

    /**
	 * Return the time to cross the cell from the start to end to the selected speed and polars expressed in
	 * hours.
	 */
    @Override
    public double getTTC() {
        return ttc;
    }

    public int getWindDirection() {
        return cell.getWindDir();
    }

    public double getWindSpeed() {
        return cell.getWindSpeed();
    }

    @Override
    public String printReport(final int waypointId) {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("W").append(waypointId).append('\t');
        buffer.append(exitLocation.formatLatitude()).append("\t").append(exitLocation.formatLongitude()).append("\t");
        buffer.append(exitLocation.getLat()).append('\t');
        buffer.append(exitLocation.getLon()).append('\t');
        buffer.append(alpha).append('\t');
        buffer.append(speed).append('\t');
        buffer.append(sail).append('\t');
        buffer.append(deltaLat).append('\t');
        buffer.append(deltaLon).append('\t');
        buffer.append(loxDistance).append('\t');
        if (ttc == Double.POSITIVE_INFINITY) buffer.append(100).append('\t'); else buffer.append(ttc).append('\t');
        buffer.append(cell.getWindSpeed()).append('\t');
        buffer.append(cell.getWindDir()).append('\t');
        buffer.append(cell.getTimeStamp());
        return buffer.toString();
    }

    @Override
    public String printStartReport() {
        final StringBuffer buffer = new StringBuffer("START").append('\t');
        buffer.append(entryLocation.formatLatitude()).append("\t").append(entryLocation.formatLongitude()).append("\t");
        buffer.append(entryLocation.getLat()).append('\t');
        buffer.append(entryLocation.getLon()).append('\t');
        buffer.append("\t\t\t\t\t\t\t");
        buffer.append(cell.getWindSpeed()).append('\t');
        buffer.append(cell.getWindDir()).append('\t');
        buffer.append(cell.getTimeStamp());
        return buffer.toString();
    }

    @Override
    public void setEntryLocation(final GeoLocation entryLocation) {
        this.entryLocation = entryLocation;
        this.calculateCell();
    }

    @Override
    public void setExitLocation(final GeoLocation exitLocation) {
        this.exitLocation = exitLocation;
        this.calculateCell();
    }

    @Override
    public void setWindData(final WindCell windData) {
        cell = windData;
        this.calculateCell();
    }

    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(VORGConstants.NEWLINE).append("[RouteCell ");
        buffer.append("cell=").append(cell).append("");
        buffer.append(VORGConstants.NEWLINE).append("                ").append("alpha=").append(alpha).append(",");
        buffer.append("distance=").append(loxDistance).append(",");
        buffer.append("speed=").append(speed).append(",");
        buffer.append("ttc=").append(ttc).append("");
        buffer.append(VORGConstants.NEWLINE).append("                ").append("entrylocation=").append(entryLocation).append("");
        buffer.append(VORGConstants.NEWLINE).append("                ").append("exitlocation=").append(exitLocation).append("");
        buffer.append("]");
        return buffer.toString();
    }

    @Override
    public String vrtoolReport(int index) {
        final StringBuffer buffer = new StringBuffer("P; ");
        buffer.append(FormatSingletons.nf5.format(this.entryLocation.getLat())).append(";");
        buffer.append(FormatSingletons.nf5.format(this.entryLocation.getLon() * -1.0)).append(";");
        if (index == 0) buffer.append("START").append(VORGConstants.NEWLINE); else buffer.append("W").append(index).append(VORGConstants.NEWLINE);
        return buffer.toString();
    }

    public RouteControl getLeftControl() {
        return leftControl;
    }

    public RouteControl getRightControl() {
        return rightControl;
    }

    public void registerControlLeft(RouteControl control) {
        leftControl = control;
    }

    public void registerControlRight(RouteControl control) {
        rightControl = control;
    }
}
