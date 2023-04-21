package joelib.desc.result;

import wsi.ra.text.DecimalFormatHelper;
import wsi.ra.text.DecimalFormatter;
import java.io.LineNumberReader;
import java.io.StringReader;
import org.apache.log4j.Category;
import joelib.data.JOEDataType;
import joelib.data.JOEPairData;
import joelib.desc.DescResult;
import joelib.desc.NativeValue;
import joelib.desc.NumberFormatDescResult;
import joelib.io.IOType;
import joelib.io.IOTypeHolder;
import joelib.util.JHM;

/**
 * Double value descriptor which was calculated by using an atom property ({@link joelib.molecule.types.AtomProperties}).
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.15 $, $Date: 2004/08/30 12:58:12 $
 */
public class APropDoubleResult extends JOEPairData implements Cloneable, DescResult, NativeValue, NumberFormatDescResult, java.io.Serializable {

    /**
     * Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance("joelib.desc.result.APropDoubleResult");

    private static final String basicFormat = "<atom_property>\n" + "64-bit floating point value IEEE 754";

    private static final String lineFormat = "<atom_property>\n" + "64-bit floating point value IEEE 754";

    /**
     *  Description of the Field
     */
    public String atomProperty;

    /**
     *  Description of the Field
     */
    public double value;

    /**
     *  Constructor for the IntResult object
     */
    public APropDoubleResult() {
        dataType = JOEDataType.JOE_PAIR_DATA;
        this.setAttribute("DescriptorResult");
        this._value = this;
    }

    /**
     *  Sets the double attribute of the DoubleResult object
     *
     * @param _v  The new double value
     */
    public void setDouble(double _v) {
        value = _v;
    }

    /**
     *  Gets the double attribute of the DoubleResult object
     *
     * @return   The double value
     */
    public double getDouble() {
        return value;
    }

    /**
     * Sets the doubleNV attribute of the APropDoubleResult object
     *
     * @param _value  The new doubleNV value
     */
    public void setDoubleNV(double _value) {
        value = _value;
    }

    /**
     * Gets the doubleNV attribute of the APropDoubleResult object
     *
     * @return   The doubleNV value
     */
    public double getDoubleNV() {
        return value;
    }

    /**
     * Gets the doubleNV attribute of the APropDoubleResult object
     *
     * @return   The doubleNV value
     */
    public boolean isDoubleNV() {
        return true;
    }

    /**
     * Sets the intNV attribute of the APropDoubleResult object
     *
     * @param _value  The new intNV value
     */
    public void setIntNV(int _value) {
        value = (double) _value;
    }

    /**
     * Gets the intNV attribute of the APropDoubleResult object
     *
     * @return   The intNV value
     */
    public int getIntNV() {
        return (int) value;
    }

    /**
     * Gets the intNV attribute of the APropDoubleResult object
     *
     * @return   The intNV value
     */
    public boolean isIntNV() {
        return false;
    }

    /**
     * Sets the stringNV attribute of the APropDoubleResult object
     *
     * @param _value  The new stringNV value
     */
    public void setStringNV(String _value) {
        value = Double.parseDouble(_value);
    }

    /**
     * Gets the stringNV attribute of the APropDoubleResult object
     *
     * @return   The stringNV value
     */
    public String getStringNV() {
        return DecimalFormatHelper.instance().format(value);
    }

    public APropDoubleResult clone(APropDoubleResult _target) {
        _target.atomProperty = this.atomProperty;
        _target.value = this.value;
        return _target;
    }

    public Object clone() {
        APropDoubleResult newObj = new APropDoubleResult();
        return clone(newObj);
    }

    /**
     *  Description of the Method
     *
     * @param ioType  Description of the Parameter
     * @return        Description of the Return Value
     */
    public String formatDescription(IOType ioType) {
        if (ioType.equals(IOTypeHolder.instance().getIOType("SDF"))) {
            return lineFormat;
        } else {
            return basicFormat;
        }
    }

    /**
     *  Description of the Method
     *
     * @param pairData  Description of the Parameter
     * @param ioType    Description of the Parameter
     * @return          Description of the Return Value
     */
    public boolean fromPairData(IOType ioType, JOEPairData pairData) {
        this.setAttribute(pairData.getAttribute());
        Object value = pairData.getValue();
        boolean success = false;
        if ((value != null) && (value instanceof String)) {
            success = fromString(ioType, (String) value);
        }
        return success;
    }

    /**
     *  Description of the Method
     *
     * @param sValue  Description of the Parameter
     * @param ioType  Description of the Parameter
     * @return        Description of the Return Value
     */
    public boolean fromString(IOType ioType, String sValue) {
        StringReader sr = new StringReader(sValue);
        LineNumberReader lnr = new LineNumberReader(sr);
        String tmp = null;
        if (ioType.equals(IOTypeHolder.instance().getIOType("FLAT")) == false) {
            try {
                atomProperty = lnr.readLine();
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        } else {
            int index = sValue.indexOf("\n");
            if (index == -1) {
                logger.error(this.getClass().getName() + " missing atom property.");
                return false;
            }
            atomProperty = sValue.substring(index).trim();
        }
        if (ioType.equals(IOTypeHolder.instance().getIOType("UNDEFINED"))) {
            int index = sValue.indexOf("\n");
            if (index == -1) {
                logger.error(this.getClass().getName() + " missing double value.");
                return false;
            }
            tmp = sValue.substring(index).trim();
        } else if (ioType.equals(IOTypeHolder.instance().getIOType("FLAT")) == false) {
            int index = sValue.indexOf("\n");
            if (index == -1) {
                logger.error(this.getClass().getName() + " missing double value.");
                return false;
            }
            tmp = sValue.substring(index).trim();
        }
        try {
            value = Double.parseDouble(tmp);
        } catch (NumberFormatException ex) {
            if (ex.toString().lastIndexOf("Inf") != -1) {
                value = Double.POSITIVE_INFINITY;
            } else if ((ex.toString().lastIndexOf("NaN") != -1) || (ex.toString().lastIndexOf("-Na") != -1)) {
                value = Double.NaN;
            } else if (ex.toString().lastIndexOf("-In") != -1) {
                value = Double.NEGATIVE_INFINITY;
            } else {
                throw ex;
            }
        }
        return true;
    }

    public boolean init(String _descName) {
        this.setAttribute(_descName);
        return true;
    }

    /**
     *  Description of the Method
     *
     * @param ioType  Description of the Parameter
     * @return        Description of the Return Value
     */
    public String toString(IOType ioType) {
        return toString(ioType, DecimalFormatHelper.instance());
    }

    /**
     *  Description of the Method
     *
     * @param ioType  Description of the Parameter
     * @return        Description of the Return Value
     */
    public String toString(IOType ioType, DecimalFormatter format) {
        StringBuffer sb = new StringBuffer();
        if (ioType.equals(IOTypeHolder.instance().getIOType("FLAT"))) {
            sb.append(format.format(value));
        } else {
            sb.append(atomProperty);
            sb.append(JHM.eol);
            sb.append(format.format(value));
        }
        return sb.toString();
    }
}
