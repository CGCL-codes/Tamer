package uk.ac.rdg.resc.ncwms.datareader;

import java.util.List;
import java.util.Vector;
import ucar.ma2.Range;

/**
 * Stores the metadata for a {@link GeoGrid}: saves reading in the metadata every
 * time the dataset is opened (a significant performance hit especially for
 * large NcML aggregations.
 *
 * @author Jon Blower
 * $Revision$
 * $Date$
 * $Log$
 */
public class VariableMetadata {

    private String id;

    private String title;

    private String abstr;

    private String units;

    private String zUnits;

    private double[] zValues;

    private boolean zPositive;

    private double[] tValues;

    private double[] bbox;

    private double validMin;

    private double validMax;

    private EnhancedCoordAxis xaxis;

    private EnhancedCoordAxis yaxis;

    /** Creates a new instance of VariableMetadata */
    VariableMetadata() {
        this.title = null;
        this.abstr = null;
        this.zUnits = null;
        this.zValues = null;
        this.tValues = null;
        this.bbox = new double[] { -180.0, -90.0, 180.0, 90.0 };
        this.xaxis = null;
        this.yaxis = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstract() {
        return abstr;
    }

    public void setAbstract(String abstr) {
        this.abstr = abstr;
    }

    public String getZunits() {
        return zUnits;
    }

    public void setZunits(String zUnits) {
        this.zUnits = zUnits;
    }

    public double[] getZvalues() {
        return zValues;
    }

    public void setZvalues(double[] zValues) {
        this.zValues = zValues;
    }

    public double[] getTvalues() {
        return tValues;
    }

    public void setTvalues(double[] tValues) {
        this.tValues = tValues;
    }

    public double[] getBbox() {
        return bbox;
    }

    public void setBbox(double[] bbox) {
        this.bbox = bbox;
    }

    public boolean getZpositive() {
        return zPositive;
    }

    public void setZpositive(boolean zPositive) {
        this.zPositive = zPositive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getValidMin() {
        return validMin;
    }

    public void setValidMin(double validMin) {
        this.validMin = validMin;
    }

    public double getValidMax() {
        return validMax;
    }

    public void setValidMax(double validMax) {
        this.validMax = validMax;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public EnhancedCoordAxis getXaxis() {
        return xaxis;
    }

    public void setXaxis(EnhancedCoordAxis xaxis) {
        this.xaxis = xaxis;
    }

    public EnhancedCoordAxis getYaxis() {
        return yaxis;
    }

    public void setYaxis(EnhancedCoordAxis yaxis) {
        this.yaxis = yaxis;
    }
}
