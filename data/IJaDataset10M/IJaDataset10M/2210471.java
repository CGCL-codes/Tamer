package fr.esrf.tangoatk.core.attribute;

import fr.esrf.tangoatk.core.*;
import fr.esrf.Tango.*;
import fr.esrf.TangoApi.*;

public class LongSpectrumHelper extends ANumberSpectrumHelper {

    public LongSpectrumHelper(AAttribute attribute) {
        init(attribute);
    }

    @Override
    void init(AAttribute attribute) {
        super.init(attribute);
    }

    void insert(double[] d) {
        double dUnitFactor = 1.0;
        int[] tmp = new int[d.length];
        dUnitFactor = this.attribute.getDisplayUnitFactor();
        DeviceAttribute da = this.attribute.getAttribute();
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = (int) (d[i] / dUnitFactor);
        }
        da.insert(tmp);
    }

    protected INumberSpectrumHistory[] getNumberSpectrumAttHistory(DeviceDataHistory[] attPollHist) {
        NumberSpectrumHistory[] hist = new NumberSpectrumHistory[attPollHist.length];
        NumberSpectrumHistory histElem;
        fr.esrf.Tango.AttrQuality attq;
        int i;
        double dUnitFactor = 1.0;
        if (attPollHist.length <= 0) return null;
        dUnitFactor = this.attribute.getDisplayUnitFactor();
        if (dUnitFactor <= 0) dUnitFactor = 1.0;
        for (i = 0; i < attPollHist.length; i++) {
            histElem = new NumberSpectrumHistory();
            try {
                histElem.setTimestamp(attPollHist[i].getTime());
            } catch (Exception ex) {
                histElem.setTimestamp(0);
            }
            try {
                attq = attPollHist[i].getAttrQuality();
                if (AttrQuality._ATTR_VALID == attq.value()) {
                    histElem.setState(IAttribute.VALID);
                } else {
                    if (AttrQuality._ATTR_INVALID == attq.value()) {
                        histElem.setState(IAttribute.INVALID);
                    } else {
                        if (AttrQuality._ATTR_ALARM == attq.value()) {
                            histElem.setState(IAttribute.ALARM);
                        } else {
                            if (AttrQuality._ATTR_WARNING == attq.value()) {
                                histElem.setState(IAttribute.WARNING);
                            } else {
                                if (AttrQuality._ATTR_CHANGING == attq.value()) {
                                    histElem.setState(IAttribute.CHANGING);
                                } else histElem.setState(IAttribute.UNKNOWN);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                histElem.setState(IAttribute.UNKNOWN);
            }
            try {
                int[] vals = attPollHist[i].extractLongArray();
                double[] newVals = new double[vals.length];
                for (int j = 0; j < vals.length; j++) newVals[j] = (double) vals[j] * dUnitFactor;
                histElem.setValue(newVals);
            } catch (Exception ex) {
                histElem.setValue(new double[0]);
            }
            hist[i] = histElem;
        }
        return hist;
    }

    protected INumberSpectrumHistory[] getNumberSpectrumDeviceAttHistory(DeviceDataHistory[] attPollHist) {
        NumberSpectrumHistory[] hist = new NumberSpectrumHistory[attPollHist.length];
        NumberSpectrumHistory histElem;
        fr.esrf.Tango.AttrQuality attq;
        int i;
        if (attPollHist.length <= 0) return null;
        for (i = 0; i < attPollHist.length; i++) {
            histElem = new NumberSpectrumHistory();
            try {
                histElem.setTimestamp(attPollHist[i].getTime());
            } catch (Exception ex) {
                histElem.setTimestamp(0);
            }
            try {
                attq = attPollHist[i].getAttrQuality();
                if (AttrQuality._ATTR_VALID == attq.value()) {
                    histElem.setState(IAttribute.VALID);
                } else {
                    if (AttrQuality._ATTR_INVALID == attq.value()) {
                        histElem.setState(IAttribute.INVALID);
                    } else {
                        if (AttrQuality._ATTR_ALARM == attq.value()) {
                            histElem.setState(IAttribute.ALARM);
                        } else {
                            if (AttrQuality._ATTR_WARNING == attq.value()) {
                                histElem.setState(IAttribute.WARNING);
                            } else {
                                if (AttrQuality._ATTR_CHANGING == attq.value()) {
                                    histElem.setState(IAttribute.CHANGING);
                                } else histElem.setState(IAttribute.UNKNOWN);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                histElem.setState(IAttribute.UNKNOWN);
            }
            try {
                int[] vals = attPollHist[i].extractLongArray();
                double[] newVals = new double[vals.length];
                for (int j = 0; j < vals.length; j++) newVals[j] = (double) vals[j];
                histElem.setValue(newVals);
            } catch (Exception ex) {
                histElem.setValue(new double[0]);
            }
            hist[i] = histElem;
        }
        return hist;
    }

    protected IAttributeSpectrumHistory[] getSpectrumDeviceAttHistory(DeviceDataHistory[] attPollHist) {
        return (getNumberSpectrumDeviceAttHistory(attPollHist));
    }

    protected IAttributeSpectrumHistory[] getSpectrumAttHistory(DeviceDataHistory[] attPollHist) {
        return (getNumberSpectrumAttHistory(attPollHist));
    }

    void setMinAlarm(double d) {
        setProperty("min_alarm", new Long((long) d));
    }

    void setMaxAlarm(double d) {
        setProperty("max_alarm", new Long((long) d));
    }

    void setMinValue(double d) {
        setProperty("min_value", new Long((long) d));
    }

    void setMaxValue(double d) {
        setProperty("max_value", new Long((long) d));
    }

    void setMinWarning(double d) {
        setProperty("min_warning", new Long((long) d));
    }

    void setMaxWarning(double d) {
        setProperty("max_warning", new Long((long) d));
    }

    void setDeltaT(double d) {
        setProperty("delta_t", new Long((long) d));
    }

    void setDeltaVal(double d) {
        setProperty("delta_val", new Long((long) d));
    }

    void setMinWarning(double d, boolean writable) {
        setProperty("min_warning", new Long((long) d), writable);
    }

    void setMaxWarning(double d, boolean writable) {
        setProperty("max_warning", new Long((long) d), writable);
    }

    void setDeltaT(double d, boolean writable) {
        setProperty("delta_t", new Long((long) d), writable);
    }

    void setDeltaVal(double d, boolean writable) {
        setProperty("delta_val", new Long((long) d), writable);
    }

    void setMinAlarm(double d, boolean writable) {
        setProperty("min_alarm", new Long((long) d), writable);
    }

    void setMaxAlarm(double d, boolean writable) {
        setProperty("max_alarm", new Long((long) d), writable);
    }

    void setMinValue(double d, boolean writable) {
        setProperty("min_value", new Long((long) d), writable);
    }

    void setMaxValue(double d, boolean writable) {
        setProperty("max_value", new Long((long) d), writable);
    }

    double[] getNumberSpectrumValue(DeviceAttribute da) throws DevFailed {
        int[] tmp = da.extractLongArray();
        int nbReadElements = da.getNbRead();
        double[] retval = new double[nbReadElements];
        for (int i = 0; i < nbReadElements; i++) {
            retval[i] = (double) tmp[i];
        }
        return retval;
    }

    double[] getNumberSpectrumSetPoint(DeviceAttribute da) throws DevFailed {
        int[] tmp = da.extractLongArray();
        int nbReadElements = da.getNbRead();
        int nbSetElements = tmp.length - nbReadElements;
        if (nbSetElements <= 0) {
            return getNumberSpectrumValue(da);
        } else {
            double[] retval = new double[nbSetElements];
            int j = 0;
            for (int i = nbReadElements; i < tmp.length; i++) {
                retval[j] = (double) tmp[i];
                j++;
            }
            return retval;
        }
    }

    double[] getNumberSpectrumDisplayValue(DeviceAttribute da) throws DevFailed {
        int[] tmp;
        double dUnitFactor;
        double[] retSpectVal;
        tmp = da.extractLongArray();
        dUnitFactor = this.attribute.getDisplayUnitFactor();
        int nbReadElements = da.getNbRead();
        retSpectVal = new double[nbReadElements];
        for (int i = 0; i < nbReadElements; i++) {
            retSpectVal[i] = (double) tmp[i] * dUnitFactor;
        }
        return retSpectVal;
    }

    double[] getNumberSpectrumDisplaySetPoint(DeviceAttribute da) throws DevFailed {
        int[] tmp;
        double dUnitFactor;
        tmp = da.extractLongArray();
        dUnitFactor = this.attribute.getDisplayUnitFactor();
        int nbReadElements = da.getNbRead();
        int nbSetElements = tmp.length - nbReadElements;
        if (nbSetElements <= 0) {
            return getNumberSpectrumDisplayValue(da);
        } else {
            double[] retval = new double[nbSetElements];
            int j = 0;
            for (int i = nbReadElements; i < tmp.length; i++) {
                retval[j] = (double) tmp[i] * dUnitFactor;
                j++;
            }
            return retval;
        }
    }

    public String getVersion() {
        return "$Id: LongSpectrumHelper.java 15105 2010-09-24 13:11:54Z poncet $";
    }
}
