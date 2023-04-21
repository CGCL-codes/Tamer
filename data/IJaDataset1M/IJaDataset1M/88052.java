package org.systemsbiology.apml.impl;

/**
 * An XML CoordinateType(@http://www.systemsbiology.org/apml).
 *
 * This is a complex type.
 */
public class CoordinateTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.systemsbiology.apml.CoordinateType {

    public CoordinateTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final javax.xml.namespace.QName SCANRANGE$0 = new javax.xml.namespace.QName("http://www.systemsbiology.org/apml", "scan_range");

    private static final javax.xml.namespace.QName TIMERANGE$2 = new javax.xml.namespace.QName("http://www.systemsbiology.org/apml", "time_range");

    private static final javax.xml.namespace.QName MZRANGE$4 = new javax.xml.namespace.QName("http://www.systemsbiology.org/apml", "mz_range");

    private static final javax.xml.namespace.QName MZ$6 = new javax.xml.namespace.QName("", "mz");

    private static final javax.xml.namespace.QName RT$8 = new javax.xml.namespace.QName("", "rt");

    private static final javax.xml.namespace.QName INTENSITY$10 = new javax.xml.namespace.QName("", "intensity");

    private static final javax.xml.namespace.QName MASS$12 = new javax.xml.namespace.QName("", "mass");

    private static final javax.xml.namespace.QName APEXINTENSITY$14 = new javax.xml.namespace.QName("", "apex_intensity");

    private static final javax.xml.namespace.QName APEXSCAN$16 = new javax.xml.namespace.QName("", "apex_scan");

    private static final javax.xml.namespace.QName BACKGROUNDESTIMATE$18 = new javax.xml.namespace.QName("", "background_estimate");

    private static final javax.xml.namespace.QName MEDIANESTIMATE$20 = new javax.xml.namespace.QName("", "median_estimate");

    private static final javax.xml.namespace.QName CHARGE$22 = new javax.xml.namespace.QName("", "charge");

    /**
     * Gets the "scan_range" element
     */
    public org.systemsbiology.apml.CoordinateType.ScanRange getScanRange() {
        synchronized (monitor()) {
            check_orphaned();
            org.systemsbiology.apml.CoordinateType.ScanRange target = null;
            target = (org.systemsbiology.apml.CoordinateType.ScanRange) get_store().find_element_user(SCANRANGE$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * True if has "scan_range" element
     */
    public boolean isSetScanRange() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(SCANRANGE$0) != 0;
        }
    }

    /**
     * Sets the "scan_range" element
     */
    public void setScanRange(org.systemsbiology.apml.CoordinateType.ScanRange scanRange) {
        synchronized (monitor()) {
            check_orphaned();
            org.systemsbiology.apml.CoordinateType.ScanRange target = null;
            target = (org.systemsbiology.apml.CoordinateType.ScanRange) get_store().find_element_user(SCANRANGE$0, 0);
            if (target == null) {
                target = (org.systemsbiology.apml.CoordinateType.ScanRange) get_store().add_element_user(SCANRANGE$0);
            }
            target.set(scanRange);
        }
    }

    /**
     * Appends and returns a new empty "scan_range" element
     */
    public org.systemsbiology.apml.CoordinateType.ScanRange addNewScanRange() {
        synchronized (monitor()) {
            check_orphaned();
            org.systemsbiology.apml.CoordinateType.ScanRange target = null;
            target = (org.systemsbiology.apml.CoordinateType.ScanRange) get_store().add_element_user(SCANRANGE$0);
            return target;
        }
    }

    /**
     * Unsets the "scan_range" element
     */
    public void unsetScanRange() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SCANRANGE$0, 0);
        }
    }

    /**
     * Gets the "time_range" element
     */
    public org.systemsbiology.apml.CoordinateType.TimeRange getTimeRange() {
        synchronized (monitor()) {
            check_orphaned();
            org.systemsbiology.apml.CoordinateType.TimeRange target = null;
            target = (org.systemsbiology.apml.CoordinateType.TimeRange) get_store().find_element_user(TIMERANGE$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * True if has "time_range" element
     */
    public boolean isSetTimeRange() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(TIMERANGE$2) != 0;
        }
    }

    /**
     * Sets the "time_range" element
     */
    public void setTimeRange(org.systemsbiology.apml.CoordinateType.TimeRange timeRange) {
        synchronized (monitor()) {
            check_orphaned();
            org.systemsbiology.apml.CoordinateType.TimeRange target = null;
            target = (org.systemsbiology.apml.CoordinateType.TimeRange) get_store().find_element_user(TIMERANGE$2, 0);
            if (target == null) {
                target = (org.systemsbiology.apml.CoordinateType.TimeRange) get_store().add_element_user(TIMERANGE$2);
            }
            target.set(timeRange);
        }
    }

    /**
     * Appends and returns a new empty "time_range" element
     */
    public org.systemsbiology.apml.CoordinateType.TimeRange addNewTimeRange() {
        synchronized (monitor()) {
            check_orphaned();
            org.systemsbiology.apml.CoordinateType.TimeRange target = null;
            target = (org.systemsbiology.apml.CoordinateType.TimeRange) get_store().add_element_user(TIMERANGE$2);
            return target;
        }
    }

    /**
     * Unsets the "time_range" element
     */
    public void unsetTimeRange() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TIMERANGE$2, 0);
        }
    }

    /**
     * Gets the "mz_range" element
     */
    public org.systemsbiology.apml.CoordinateType.MzRange getMzRange() {
        synchronized (monitor()) {
            check_orphaned();
            org.systemsbiology.apml.CoordinateType.MzRange target = null;
            target = (org.systemsbiology.apml.CoordinateType.MzRange) get_store().find_element_user(MZRANGE$4, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }

    /**
     * True if has "mz_range" element
     */
    public boolean isSetMzRange() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(MZRANGE$4) != 0;
        }
    }

    /**
     * Sets the "mz_range" element
     */
    public void setMzRange(org.systemsbiology.apml.CoordinateType.MzRange mzRange) {
        synchronized (monitor()) {
            check_orphaned();
            org.systemsbiology.apml.CoordinateType.MzRange target = null;
            target = (org.systemsbiology.apml.CoordinateType.MzRange) get_store().find_element_user(MZRANGE$4, 0);
            if (target == null) {
                target = (org.systemsbiology.apml.CoordinateType.MzRange) get_store().add_element_user(MZRANGE$4);
            }
            target.set(mzRange);
        }
    }

    /**
     * Appends and returns a new empty "mz_range" element
     */
    public org.systemsbiology.apml.CoordinateType.MzRange addNewMzRange() {
        synchronized (monitor()) {
            check_orphaned();
            org.systemsbiology.apml.CoordinateType.MzRange target = null;
            target = (org.systemsbiology.apml.CoordinateType.MzRange) get_store().add_element_user(MZRANGE$4);
            return target;
        }
    }

    /**
     * Unsets the "mz_range" element
     */
    public void unsetMzRange() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(MZRANGE$4, 0);
        }
    }

    /**
     * Gets the "mz" attribute
     */
    public double getMz() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MZ$6);
            if (target == null) {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }

    /**
     * Gets (as xml) the "mz" attribute
     */
    public org.apache.xmlbeans.XmlDouble xgetMz() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble) get_store().find_attribute_user(MZ$6);
            return target;
        }
    }

    /**
     * Sets the "mz" attribute
     */
    public void setMz(double mz) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MZ$6);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(MZ$6);
            }
            target.setDoubleValue(mz);
        }
    }

    /**
     * Sets (as xml) the "mz" attribute
     */
    public void xsetMz(org.apache.xmlbeans.XmlDouble mz) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble) get_store().find_attribute_user(MZ$6);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlDouble) get_store().add_attribute_user(MZ$6);
            }
            target.set(mz);
        }
    }

    /**
     * Gets the "rt" attribute
     */
    public float getRt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(RT$8);
            if (target == null) {
                return 0.0f;
            }
            return target.getFloatValue();
        }
    }

    /**
     * Gets (as xml) the "rt" attribute
     */
    public org.apache.xmlbeans.XmlFloat xgetRt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(RT$8);
            return target;
        }
    }

    /**
     * Sets the "rt" attribute
     */
    public void setRt(float rt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(RT$8);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(RT$8);
            }
            target.setFloatValue(rt);
        }
    }

    /**
     * Sets (as xml) the "rt" attribute
     */
    public void xsetRt(org.apache.xmlbeans.XmlFloat rt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(RT$8);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlFloat) get_store().add_attribute_user(RT$8);
            }
            target.set(rt);
        }
    }

    /**
     * Gets the "intensity" attribute
     */
    public float getIntensity() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(INTENSITY$10);
            if (target == null) {
                return 0.0f;
            }
            return target.getFloatValue();
        }
    }

    /**
     * Gets (as xml) the "intensity" attribute
     */
    public org.apache.xmlbeans.XmlFloat xgetIntensity() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(INTENSITY$10);
            return target;
        }
    }

    /**
     * True if has "intensity" attribute
     */
    public boolean isSetIntensity() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(INTENSITY$10) != null;
        }
    }

    /**
     * Sets the "intensity" attribute
     */
    public void setIntensity(float intensity) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(INTENSITY$10);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(INTENSITY$10);
            }
            target.setFloatValue(intensity);
        }
    }

    /**
     * Sets (as xml) the "intensity" attribute
     */
    public void xsetIntensity(org.apache.xmlbeans.XmlFloat intensity) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(INTENSITY$10);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlFloat) get_store().add_attribute_user(INTENSITY$10);
            }
            target.set(intensity);
        }
    }

    /**
     * Unsets the "intensity" attribute
     */
    public void unsetIntensity() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(INTENSITY$10);
        }
    }

    /**
     * Gets the "mass" attribute
     */
    public double getMass() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MASS$12);
            if (target == null) {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }

    /**
     * Gets (as xml) the "mass" attribute
     */
    public org.apache.xmlbeans.XmlDouble xgetMass() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble) get_store().find_attribute_user(MASS$12);
            return target;
        }
    }

    /**
     * True if has "mass" attribute
     */
    public boolean isSetMass() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(MASS$12) != null;
        }
    }

    /**
     * Sets the "mass" attribute
     */
    public void setMass(double mass) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MASS$12);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(MASS$12);
            }
            target.setDoubleValue(mass);
        }
    }

    /**
     * Sets (as xml) the "mass" attribute
     */
    public void xsetMass(org.apache.xmlbeans.XmlDouble mass) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble) get_store().find_attribute_user(MASS$12);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlDouble) get_store().add_attribute_user(MASS$12);
            }
            target.set(mass);
        }
    }

    /**
     * Unsets the "mass" attribute
     */
    public void unsetMass() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MASS$12);
        }
    }

    /**
     * Gets the "apex_intensity" attribute
     */
    public float getApexIntensity() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(APEXINTENSITY$14);
            if (target == null) {
                return 0.0f;
            }
            return target.getFloatValue();
        }
    }

    /**
     * Gets (as xml) the "apex_intensity" attribute
     */
    public org.apache.xmlbeans.XmlFloat xgetApexIntensity() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(APEXINTENSITY$14);
            return target;
        }
    }

    /**
     * True if has "apex_intensity" attribute
     */
    public boolean isSetApexIntensity() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(APEXINTENSITY$14) != null;
        }
    }

    /**
     * Sets the "apex_intensity" attribute
     */
    public void setApexIntensity(float apexIntensity) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(APEXINTENSITY$14);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(APEXINTENSITY$14);
            }
            target.setFloatValue(apexIntensity);
        }
    }

    /**
     * Sets (as xml) the "apex_intensity" attribute
     */
    public void xsetApexIntensity(org.apache.xmlbeans.XmlFloat apexIntensity) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(APEXINTENSITY$14);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlFloat) get_store().add_attribute_user(APEXINTENSITY$14);
            }
            target.set(apexIntensity);
        }
    }

    /**
     * Unsets the "apex_intensity" attribute
     */
    public void unsetApexIntensity() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(APEXINTENSITY$14);
        }
    }

    /**
     * Gets the "apex_scan" attribute
     */
    public java.math.BigInteger getApexScan() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(APEXSCAN$16);
            if (target == null) {
                return null;
            }
            return target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "apex_scan" attribute
     */
    public org.apache.xmlbeans.XmlNonNegativeInteger xgetApexScan() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlNonNegativeInteger target = null;
            target = (org.apache.xmlbeans.XmlNonNegativeInteger) get_store().find_attribute_user(APEXSCAN$16);
            return target;
        }
    }

    /**
     * True if has "apex_scan" attribute
     */
    public boolean isSetApexScan() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(APEXSCAN$16) != null;
        }
    }

    /**
     * Sets the "apex_scan" attribute
     */
    public void setApexScan(java.math.BigInteger apexScan) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(APEXSCAN$16);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(APEXSCAN$16);
            }
            target.setBigIntegerValue(apexScan);
        }
    }

    /**
     * Sets (as xml) the "apex_scan" attribute
     */
    public void xsetApexScan(org.apache.xmlbeans.XmlNonNegativeInteger apexScan) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlNonNegativeInteger target = null;
            target = (org.apache.xmlbeans.XmlNonNegativeInteger) get_store().find_attribute_user(APEXSCAN$16);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlNonNegativeInteger) get_store().add_attribute_user(APEXSCAN$16);
            }
            target.set(apexScan);
        }
    }

    /**
     * Unsets the "apex_scan" attribute
     */
    public void unsetApexScan() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(APEXSCAN$16);
        }
    }

    /**
     * Gets the "background_estimate" attribute
     */
    public float getBackgroundEstimate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(BACKGROUNDESTIMATE$18);
            if (target == null) {
                return 0.0f;
            }
            return target.getFloatValue();
        }
    }

    /**
     * Gets (as xml) the "background_estimate" attribute
     */
    public org.apache.xmlbeans.XmlFloat xgetBackgroundEstimate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(BACKGROUNDESTIMATE$18);
            return target;
        }
    }

    /**
     * True if has "background_estimate" attribute
     */
    public boolean isSetBackgroundEstimate() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(BACKGROUNDESTIMATE$18) != null;
        }
    }

    /**
     * Sets the "background_estimate" attribute
     */
    public void setBackgroundEstimate(float backgroundEstimate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(BACKGROUNDESTIMATE$18);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(BACKGROUNDESTIMATE$18);
            }
            target.setFloatValue(backgroundEstimate);
        }
    }

    /**
     * Sets (as xml) the "background_estimate" attribute
     */
    public void xsetBackgroundEstimate(org.apache.xmlbeans.XmlFloat backgroundEstimate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(BACKGROUNDESTIMATE$18);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlFloat) get_store().add_attribute_user(BACKGROUNDESTIMATE$18);
            }
            target.set(backgroundEstimate);
        }
    }

    /**
     * Unsets the "background_estimate" attribute
     */
    public void unsetBackgroundEstimate() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(BACKGROUNDESTIMATE$18);
        }
    }

    /**
     * Gets the "median_estimate" attribute
     */
    public float getMedianEstimate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MEDIANESTIMATE$20);
            if (target == null) {
                return 0.0f;
            }
            return target.getFloatValue();
        }
    }

    /**
     * Gets (as xml) the "median_estimate" attribute
     */
    public org.apache.xmlbeans.XmlFloat xgetMedianEstimate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(MEDIANESTIMATE$20);
            return target;
        }
    }

    /**
     * True if has "median_estimate" attribute
     */
    public boolean isSetMedianEstimate() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(MEDIANESTIMATE$20) != null;
        }
    }

    /**
     * Sets the "median_estimate" attribute
     */
    public void setMedianEstimate(float medianEstimate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MEDIANESTIMATE$20);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(MEDIANESTIMATE$20);
            }
            target.setFloatValue(medianEstimate);
        }
    }

    /**
     * Sets (as xml) the "median_estimate" attribute
     */
    public void xsetMedianEstimate(org.apache.xmlbeans.XmlFloat medianEstimate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(MEDIANESTIMATE$20);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlFloat) get_store().add_attribute_user(MEDIANESTIMATE$20);
            }
            target.set(medianEstimate);
        }
    }

    /**
     * Unsets the "median_estimate" attribute
     */
    public void unsetMedianEstimate() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(MEDIANESTIMATE$20);
        }
    }

    /**
     * Gets the "charge" attribute
     */
    public int getCharge() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(CHARGE$22);
            if (target == null) {
                return 0;
            }
            return target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "charge" attribute
     */
    public org.apache.xmlbeans.XmlInt xgetCharge() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt) get_store().find_attribute_user(CHARGE$22);
            return target;
        }
    }

    /**
     * True if has "charge" attribute
     */
    public boolean isSetCharge() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(CHARGE$22) != null;
        }
    }

    /**
     * Sets the "charge" attribute
     */
    public void setCharge(int charge) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(CHARGE$22);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(CHARGE$22);
            }
            target.setIntValue(charge);
        }
    }

    /**
     * Sets (as xml) the "charge" attribute
     */
    public void xsetCharge(org.apache.xmlbeans.XmlInt charge) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt) get_store().find_attribute_user(CHARGE$22);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt) get_store().add_attribute_user(CHARGE$22);
            }
            target.set(charge);
        }
    }

    /**
     * Unsets the "charge" attribute
     */
    public void unsetCharge() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(CHARGE$22);
        }
    }

    /**
     * An XML scan_range(@http://www.systemsbiology.org/apml).
     *
     * This is a complex type.
     */
    public static class ScanRangeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.systemsbiology.apml.CoordinateType.ScanRange {

        public ScanRangeImpl(org.apache.xmlbeans.SchemaType sType) {
            super(sType);
        }

        private static final javax.xml.namespace.QName MIN$0 = new javax.xml.namespace.QName("", "min");

        private static final javax.xml.namespace.QName MAX$2 = new javax.xml.namespace.QName("", "max");

        private static final javax.xml.namespace.QName COUNT$4 = new javax.xml.namespace.QName("", "count");

        /**
         * Gets the "min" attribute
         */
        public java.math.BigInteger getMin() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MIN$0);
                if (target == null) {
                    return null;
                }
                return target.getBigIntegerValue();
            }
        }

        /**
         * Gets (as xml) the "min" attribute
         */
        public org.apache.xmlbeans.XmlNonNegativeInteger xgetMin() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlNonNegativeInteger target = null;
                target = (org.apache.xmlbeans.XmlNonNegativeInteger) get_store().find_attribute_user(MIN$0);
                return target;
            }
        }

        /**
         * Sets the "min" attribute
         */
        public void setMin(java.math.BigInteger min) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MIN$0);
                if (target == null) {
                    target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(MIN$0);
                }
                target.setBigIntegerValue(min);
            }
        }

        /**
         * Sets (as xml) the "min" attribute
         */
        public void xsetMin(org.apache.xmlbeans.XmlNonNegativeInteger min) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlNonNegativeInteger target = null;
                target = (org.apache.xmlbeans.XmlNonNegativeInteger) get_store().find_attribute_user(MIN$0);
                if (target == null) {
                    target = (org.apache.xmlbeans.XmlNonNegativeInteger) get_store().add_attribute_user(MIN$0);
                }
                target.set(min);
            }
        }

        /**
         * Gets the "max" attribute
         */
        public java.math.BigInteger getMax() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MAX$2);
                if (target == null) {
                    return null;
                }
                return target.getBigIntegerValue();
            }
        }

        /**
         * Gets (as xml) the "max" attribute
         */
        public org.apache.xmlbeans.XmlNonNegativeInteger xgetMax() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlNonNegativeInteger target = null;
                target = (org.apache.xmlbeans.XmlNonNegativeInteger) get_store().find_attribute_user(MAX$2);
                return target;
            }
        }

        /**
         * Sets the "max" attribute
         */
        public void setMax(java.math.BigInteger max) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MAX$2);
                if (target == null) {
                    target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(MAX$2);
                }
                target.setBigIntegerValue(max);
            }
        }

        /**
         * Sets (as xml) the "max" attribute
         */
        public void xsetMax(org.apache.xmlbeans.XmlNonNegativeInteger max) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlNonNegativeInteger target = null;
                target = (org.apache.xmlbeans.XmlNonNegativeInteger) get_store().find_attribute_user(MAX$2);
                if (target == null) {
                    target = (org.apache.xmlbeans.XmlNonNegativeInteger) get_store().add_attribute_user(MAX$2);
                }
                target.set(max);
            }
        }

        /**
         * Gets the "count" attribute
         */
        public java.math.BigInteger getCount() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(COUNT$4);
                if (target == null) {
                    return null;
                }
                return target.getBigIntegerValue();
            }
        }

        /**
         * Gets (as xml) the "count" attribute
         */
        public org.apache.xmlbeans.XmlNonNegativeInteger xgetCount() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlNonNegativeInteger target = null;
                target = (org.apache.xmlbeans.XmlNonNegativeInteger) get_store().find_attribute_user(COUNT$4);
                return target;
            }
        }

        /**
         * True if has "count" attribute
         */
        public boolean isSetCount() {
            synchronized (monitor()) {
                check_orphaned();
                return get_store().find_attribute_user(COUNT$4) != null;
            }
        }

        /**
         * Sets the "count" attribute
         */
        public void setCount(java.math.BigInteger count) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(COUNT$4);
                if (target == null) {
                    target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(COUNT$4);
                }
                target.setBigIntegerValue(count);
            }
        }

        /**
         * Sets (as xml) the "count" attribute
         */
        public void xsetCount(org.apache.xmlbeans.XmlNonNegativeInteger count) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlNonNegativeInteger target = null;
                target = (org.apache.xmlbeans.XmlNonNegativeInteger) get_store().find_attribute_user(COUNT$4);
                if (target == null) {
                    target = (org.apache.xmlbeans.XmlNonNegativeInteger) get_store().add_attribute_user(COUNT$4);
                }
                target.set(count);
            }
        }

        /**
         * Unsets the "count" attribute
         */
        public void unsetCount() {
            synchronized (monitor()) {
                check_orphaned();
                get_store().remove_attribute(COUNT$4);
            }
        }
    }

    /**
     * An XML time_range(@http://www.systemsbiology.org/apml).
     *
     * This is a complex type.
     */
    public static class TimeRangeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.systemsbiology.apml.CoordinateType.TimeRange {

        public TimeRangeImpl(org.apache.xmlbeans.SchemaType sType) {
            super(sType);
        }

        private static final javax.xml.namespace.QName MIN$0 = new javax.xml.namespace.QName("", "min");

        private static final javax.xml.namespace.QName MAX$2 = new javax.xml.namespace.QName("", "max");

        /**
         * Gets the "min" attribute
         */
        public float getMin() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MIN$0);
                if (target == null) {
                    return 0.0f;
                }
                return target.getFloatValue();
            }
        }

        /**
         * Gets (as xml) the "min" attribute
         */
        public org.apache.xmlbeans.XmlFloat xgetMin() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlFloat target = null;
                target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(MIN$0);
                return target;
            }
        }

        /**
         * Sets the "min" attribute
         */
        public void setMin(float min) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MIN$0);
                if (target == null) {
                    target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(MIN$0);
                }
                target.setFloatValue(min);
            }
        }

        /**
         * Sets (as xml) the "min" attribute
         */
        public void xsetMin(org.apache.xmlbeans.XmlFloat min) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlFloat target = null;
                target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(MIN$0);
                if (target == null) {
                    target = (org.apache.xmlbeans.XmlFloat) get_store().add_attribute_user(MIN$0);
                }
                target.set(min);
            }
        }

        /**
         * Gets the "max" attribute
         */
        public float getMax() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MAX$2);
                if (target == null) {
                    return 0.0f;
                }
                return target.getFloatValue();
            }
        }

        /**
         * Gets (as xml) the "max" attribute
         */
        public org.apache.xmlbeans.XmlFloat xgetMax() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlFloat target = null;
                target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(MAX$2);
                return target;
            }
        }

        /**
         * Sets the "max" attribute
         */
        public void setMax(float max) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MAX$2);
                if (target == null) {
                    target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(MAX$2);
                }
                target.setFloatValue(max);
            }
        }

        /**
         * Sets (as xml) the "max" attribute
         */
        public void xsetMax(org.apache.xmlbeans.XmlFloat max) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlFloat target = null;
                target = (org.apache.xmlbeans.XmlFloat) get_store().find_attribute_user(MAX$2);
                if (target == null) {
                    target = (org.apache.xmlbeans.XmlFloat) get_store().add_attribute_user(MAX$2);
                }
                target.set(max);
            }
        }
    }

    /**
     * An XML mz_range(@http://www.systemsbiology.org/apml).
     *
     * This is a complex type.
     */
    public static class MzRangeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.systemsbiology.apml.CoordinateType.MzRange {

        public MzRangeImpl(org.apache.xmlbeans.SchemaType sType) {
            super(sType);
        }

        private static final javax.xml.namespace.QName MIN$0 = new javax.xml.namespace.QName("", "min");

        private static final javax.xml.namespace.QName MAX$2 = new javax.xml.namespace.QName("", "max");

        /**
         * Gets the "min" attribute
         */
        public double getMin() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MIN$0);
                if (target == null) {
                    return 0.0;
                }
                return target.getDoubleValue();
            }
        }

        /**
         * Gets (as xml) the "min" attribute
         */
        public org.apache.xmlbeans.XmlDouble xgetMin() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlDouble target = null;
                target = (org.apache.xmlbeans.XmlDouble) get_store().find_attribute_user(MIN$0);
                return target;
            }
        }

        /**
         * Sets the "min" attribute
         */
        public void setMin(double min) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MIN$0);
                if (target == null) {
                    target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(MIN$0);
                }
                target.setDoubleValue(min);
            }
        }

        /**
         * Sets (as xml) the "min" attribute
         */
        public void xsetMin(org.apache.xmlbeans.XmlDouble min) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlDouble target = null;
                target = (org.apache.xmlbeans.XmlDouble) get_store().find_attribute_user(MIN$0);
                if (target == null) {
                    target = (org.apache.xmlbeans.XmlDouble) get_store().add_attribute_user(MIN$0);
                }
                target.set(min);
            }
        }

        /**
         * Gets the "max" attribute
         */
        public double getMax() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MAX$2);
                if (target == null) {
                    return 0.0;
                }
                return target.getDoubleValue();
            }
        }

        /**
         * Gets (as xml) the "max" attribute
         */
        public org.apache.xmlbeans.XmlDouble xgetMax() {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlDouble target = null;
                target = (org.apache.xmlbeans.XmlDouble) get_store().find_attribute_user(MAX$2);
                return target;
            }
        }

        /**
         * Sets the "max" attribute
         */
        public void setMax(double max) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue) get_store().find_attribute_user(MAX$2);
                if (target == null) {
                    target = (org.apache.xmlbeans.SimpleValue) get_store().add_attribute_user(MAX$2);
                }
                target.setDoubleValue(max);
            }
        }

        /**
         * Sets (as xml) the "max" attribute
         */
        public void xsetMax(org.apache.xmlbeans.XmlDouble max) {
            synchronized (monitor()) {
                check_orphaned();
                org.apache.xmlbeans.XmlDouble target = null;
                target = (org.apache.xmlbeans.XmlDouble) get_store().find_attribute_user(MAX$2);
                if (target == null) {
                    target = (org.apache.xmlbeans.XmlDouble) get_store().add_attribute_user(MAX$2);
                }
                target.set(max);
            }
        }
    }
}
