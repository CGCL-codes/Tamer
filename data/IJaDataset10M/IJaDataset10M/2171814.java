package fr.esrf.tangoatk.core.attribute;

import fr.esrf.Tango.*;
import fr.esrf.TangoApi.*;

class UCharImageHelper extends ANumberImageHelper {

    public UCharImageHelper(AAttribute attribute) {
        init(attribute);
    }

    protected void insert(double[][] d) {
        double dUnitFactor = 1.0;
        double[] flatd;
        DeviceAttribute da = this.attribute.getAttribute();
        dUnitFactor = this.attribute.getDisplayUnitFactor();
        flatd = NumberAttributeHelper.flatten(d);
        short[] tmp = new short[flatd.length];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = (short) (flatd[i] / dUnitFactor);
        }
        da.insert_uc(tmp, d[0].length, d.length);
    }

    void setMinAlarm(double d) {
        setProperty("min_alarm", new Double(d));
    }

    void setMaxAlarm(double d) {
        setProperty("max_alarm", new Double(d));
    }

    void setMinValue(double d) {
        setProperty("min_value", new Double(d));
    }

    void setMaxValue(double d) {
        setProperty("max_value", new Double(d));
    }

    void setMinWarning(double d) {
        setProperty("min_warning", new Double(d));
    }

    void setMaxWarning(double d) {
        setProperty("max_warning", new Double(d));
    }

    void setDeltaT(double d) {
        setProperty("delta_t", new Double(d));
    }

    void setDeltaVal(double d) {
        setProperty("delta_val", new Double(d));
    }

    void setMinWarning(double d, boolean writable) {
        setProperty("min_warning", new Double(d), writable);
    }

    void setMaxWarning(double d, boolean writable) {
        setProperty("max_warning", new Double(d), writable);
    }

    void setDeltaT(double d, boolean writable) {
        setProperty("delta_t", new Double(d), writable);
    }

    void setDeltaVal(double d, boolean writable) {
        setProperty("delta_val", new Double(d), writable);
    }

    void setMinAlarm(double d, boolean writable) {
        setProperty("min_alarm", new Double(d), writable);
    }

    void setMaxAlarm(double d, boolean writable) {
        setProperty("max_alarm", new Double(d), writable);
    }

    void setMinValue(double d, boolean writable) {
        setProperty("min_value", new Double(d), writable);
    }

    void setMaxValue(double d, boolean writable) {
        setProperty("max_value", new Double(d), writable);
    }

    double[][] getNumberImageValue(DeviceAttribute deviceAttribute) throws DevFailed {
        int ydim = deviceAttribute.getDimY();
        int xdim = deviceAttribute.getDimX();
        if (ydim != retval.length || xdim != retval[0].length) {
            retval = new double[ydim][xdim];
        }
        short[] tmp = deviceAttribute.extractUCharArray();
        int k = 0;
        for (int i = 0; i < ydim; i++) for (int j = 0; j < xdim; j++) retval[i][j] = (double) tmp[k++];
        return retval;
    }

    double[][] getNumberImageDisplayValue(DeviceAttribute deviceAttribute) throws DevFailed {
        short[] tmp;
        double dUnitFactor;
        tmp = deviceAttribute.extractUCharArray();
        dUnitFactor = this.attribute.getDisplayUnitFactor();
        int ydim = deviceAttribute.getDimY();
        int xdim = deviceAttribute.getDimX();
        if (ydim != retval.length || xdim != retval[0].length) {
            retval = new double[ydim][xdim];
        }
        int k = 0;
        for (int i = 0; i < ydim; i++) for (int j = 0; j < xdim; j++) retval[i][j] = (double) tmp[k++] * dUnitFactor;
        return retval;
    }

    String[][] getImageValueAsString(DeviceAttribute deviceAttribute) throws DevFailed {
        short[] tmp = deviceAttribute.extractUCharArray();
        int ydim = deviceAttribute.getDimY();
        int xdim = deviceAttribute.getDimX();
        String[][] retval_str = new String[ydim][xdim];
        int k = 0;
        for (int i = 0; i < ydim; i++) for (int j = 0; j < xdim; j++) {
            retval_str[i][j] = Integer.toString(tmp[k++]);
        }
        return retval_str;
    }

    public String getVersion() {
        return "$Id: UCharImageHelper.java 15105 2010-09-24 13:11:54Z poncet $";
    }
}
