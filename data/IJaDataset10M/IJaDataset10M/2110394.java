package joelib2.feature.result;

import joelib2.molecule.types.BondProperties;

/**
 * Double results of bond properties.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.7 $, $Date: 2005/02/17 16:48:30 $
 */
public class BondDoubleResult extends DoubleArrayResult implements BondProperties, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *  Constructor for the DoubleResult object
     */
    public BondDoubleResult() {
        super();
    }

    public Object clone() {
        BondDoubleResult newObj = new BondDoubleResult();
        newObj.value = new double[this.value.length];
        return clone(newObj);
    }

    public BondDoubleResult clone(BondDoubleResult other) {
        return (BondDoubleResult) super.clone(other);
    }

    public double getDoubleValue(int bondIdx) {
        return value[bondIdx - 1];
    }

    public int getIntValue(int bondIdx) {
        return (int) value[bondIdx - 1];
    }

    public String getStringValue(int bondIdx) {
        return Double.toString(value[bondIdx - 1]);
    }

    public Object getValue(int bondIdx) {
        return new Double(value[bondIdx - 1]);
    }

    public void setDoubleValue(int bondIdx, double _value) {
        value[bondIdx - 1] = _value;
    }

    public void setIntValue(int bondIdx, int _value) {
        value[bondIdx - 1] = (double) _value;
    }

    public void setStringValue(int bondIdx, String _value) {
        value[bondIdx - 1] = Double.parseDouble(_value);
    }

    public void setValue(int bondIdx, Object _value) {
        value[bondIdx - 1] = ((Double) _value).doubleValue();
    }
}
