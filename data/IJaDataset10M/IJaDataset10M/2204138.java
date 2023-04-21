package org.n52.sos.ogc.om;

import java.util.Date;
import java.util.HashMap;

/**
 * class represents a single valueTuple of a generic observation
 * 
 * @author Christoph Stasch
 *
 */
public class SosGenObsValueTuple {

    /**timestamp of this tuple*/
    private Date timeStamp;

    /** foi of this tuple*/
    private String foiID;

    /**map, which maps the values (values of map) to the corresponding phenomena (keys)*/
    private HashMap<String, String> phenValues;

    /**
     * constructor
     * 
     * @param sampleTime
     *          time of the value tuple
     * @param foiID
     *          id of the feature of interest, which this tuple belongs to
     */
    public SosGenObsValueTuple(Date sampleTime, String foiID) {
        this.timeStamp = sampleTime;
        this.foiID = foiID;
        this.phenValues = new HashMap<String, String>();
    }

    /**
     * methods adds a value for the passed phenomenon id to this value tuple
     * 
     * @param phenID
     * @param value
     */
    public void addPhenValue(String phenID, String value) {
        if (!phenValues.containsKey(phenID)) {
            phenValues.put(phenID, value);
        }
    }

    /**
     * @return the foiID
     */
    public String getFoiID() {
        return foiID;
    }

    /**
     * @param foiID the foiID to set
     */
    public void setFoiID(String foiID) {
        this.foiID = foiID;
    }

    /**
     * @return the timeStamp
     */
    public Date getTimeStamp() {
        return timeStamp;
    }

    /**
     * @param timeStamp the timeStamp to set
     */
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * @return the phenValues
     */
    public HashMap<String, String> getPhenValues() {
        return phenValues;
    }
}
