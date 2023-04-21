package wizworld.navigate.stream;

import wizworld.navigate.geo.*;

/** Tidal stream table record object
 * @author (c) Stephen Denham 2002
 * @version 0.1
 */
public final class TidalStreamRecord {

    /** Port name */
    private String portName;

    /** Position */
    private Cartesian position;

    /** Set and drift
   */
    private double setDrift[][];

    /** Description */
    private String description;

    /** Default constructor */
    public TidalStreamRecord() {
        this.portName = "";
        try {
            this.position = new Cartesian(new Latitude(0), new Longitude(0));
        } catch (AngleException ex) {
        }
        this.setDrift = new double[TidalStreamTable.SET_AND_DRIFT_ENTRIES][SetDriftAttribute.SET_DRIFT_VALUES];
        this.description = "";
    }

    /** Accessor for port name
   * @param	portName Port name
   */
    public void setPortName(String portName) {
        this.portName = portName;
    }

    /** Accessor for position
   * @param	position Position
   */
    public void setPosition(Cartesian position) {
        this.position = position;
    }

    /** Accessor for set and drift data, 0 to 12 (13 hours).
   * Set Sp, set Np, drift Sp, drift Np
   * @param	setDrift set and drift data
   */
    public void setSetDrift(double[][] setDrift) {
        this.setDrift = setDrift;
    }

    /** Accessor for description
   * @param	description Description
   */
    public void setDescription(String description) {
        this.description = description;
    }

    /** Accessor for port name
   * @return Port name
   */
    public String getPortName() {
        return this.portName;
    }

    /** Accessor for position
   * @return	Position
   */
    public Cartesian getPosition() {
        return this.position;
    }

    /** Accessor for set and drift data, 0 to 12 (13 hours). Set Sp, set Np, drift Sp, drift Np.
   * @return set and drift data
   */
    public double[][] getSetDrift() {
        return this.setDrift;
    }

    /** Accessor for description
   * @return	Description
   */
    public String getDescription() {
        return this.description;
    }
}
