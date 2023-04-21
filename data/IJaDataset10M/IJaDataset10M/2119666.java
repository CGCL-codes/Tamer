package joelib.desc.result;

import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.log4j.Category;
import joelib.data.JOEDataType;
import joelib.data.JOEPairData;
import joelib.desc.DescResult;
import joelib.io.IOType;
import joelib.io.IOTypeHolder;
import joelib.io.types.ChemicalMarkupLanguage;
import joelib.io.types.cml.ResultCMLProperties;
import joelib.util.LineMatrixHelper;
import joelib.util.MatrixHelper;
import joelib.util.types.StringString;

/**
 * Integer matrix results of variable size.
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.21 $, $Date: 2004/08/30 12:58:12 $
 */
public class IntMatrixResult extends JOEPairData implements Cloneable, DescResult, ResultCMLProperties, java.io.Serializable {

    /**
     * Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance("joelib.desc.result.IntMatrixResult");

    private static final String basicFormat = "nLines nColumns<<e00,...,e0(nLines-1)>...<e(nColumns-1)0,...,e(nColumns-1)(nLines-1)>>\n" + "with nLines, eX0,...,eX(nLines-1) of type 32-bit integer" + "with nColumns, e0X,...,e(nColumns-1)X of type 32-bit integer";

    private static final String lineFormat = "nLines nColumns\n" + "e00\n" + "e01\n" + "...\n" + "e0(nLines-1)\n" + "n10\n" + "e11\n" + "...\n" + "e1(nLines-1)\n" + "...\n" + "e(nColumns-1)(nLines-1)\n" + "<empty line>\n" + "with nLines, eX0,...,eX(nLines-1) of type 32-bit integer" + "with nColumns, e0X,...,e(nColumns-1)X of type 32-bit integer";

    /**
     *  Description of the Field
     */
    public int[][] value;

    private Hashtable cmlProperties;

    /**
     *  Constructor for the IntMatrixResult object
     */
    public IntMatrixResult() {
        dataType = JOEDataType.JOE_PAIR_DATA;
        this.setAttribute("DescriptorResult");
        this._value = this;
    }

    public Enumeration getCMLProperties() {
        if (cmlProperties == null) {
            return null;
        }
        return cmlProperties.elements();
    }

    public void addCMLProperty(StringString property) {
        if (cmlProperties == null) {
            cmlProperties = new Hashtable();
        }
        cmlProperties.put(property.s1, property);
    }

    public IntMatrixResult clone(IntMatrixResult _target) {
        int s = this.value.length;
        for (int i = 0; i < s; i++) {
            System.arraycopy(this.value[i], 0, _target.value[i], 0, value[i].length);
        }
        _target.cmlProperties = this.cmlProperties;
        return _target;
    }

    public Object clone() {
        IntMatrixResult newObj = new IntMatrixResult();
        newObj.value = new int[this.value.length][this.value[0].length];
        return clone(newObj);
    }

    /**
     *  Description of the Method
     *
     * @param  ioType  Description of the Parameter
     * @return         Description of the Return Value
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
     * @param  pairData  Description of the Parameter
     * @param  ioType    Description of the Parameter
     * @return           Description of the Return Value
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
     * @param  sValue  Description of the Parameter
     * @param  ioType  Description of the Parameter
     * @return         Description of the Return Value
     */
    public boolean fromString(IOType ioType, String sValue) {
        if (ioType.equals(IOTypeHolder.instance().getIOType("SDF"))) {
            value = LineMatrixHelper.intMatrixFromString(sValue);
        } else if (ioType.equals(IOTypeHolder.instance().getIOType("CML"))) {
            if (cmlProperties == null) {
                logger.error("CML properties are missing");
                return false;
            }
            String matrixDelimiter = ((StringString) cmlProperties.get("delimiter")).s2;
            String matrixRows = ((StringString) cmlProperties.get("rows")).s2;
            String matrixColumns = ((StringString) cmlProperties.get("columns")).s2;
            if (matrixDelimiter == null) {
                matrixDelimiter = ChemicalMarkupLanguage.getDefaultDelimiter() + " \t\r\n";
            }
            if (matrixRows == null) {
                logger.error("Number of rows is missing in matrix.");
                return false;
            } else if (matrixColumns == null) {
                logger.error("Number of columns is missing in matrix.");
                return false;
            } else {
                int rows = Integer.parseInt(matrixRows);
                int columns = Integer.parseInt(matrixColumns);
                value = MatrixHelper.intMatrixFromSimpleString(sValue, rows, columns, matrixDelimiter);
            }
        } else {
            value = MatrixHelper.instance().intMatrixFromString(sValue);
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
     * @param  ioType  Description of the Parameter
     * @return         Description of the Return Value
     */
    public String toString(IOType ioType) {
        StringBuffer sb = new StringBuffer();
        if (ioType.equals(IOTypeHolder.instance().getIOType("SDF"))) {
            LineMatrixHelper.toString(sb, value).toString();
        } else if (ioType.equals(IOTypeHolder.instance().getIOType("CML"))) {
            String delimiter = null;
            if (cmlProperties != null) {
                StringString tmp = (StringString) cmlProperties.get("delimiter");
                if (tmp != null) {
                    delimiter = tmp.s2;
                }
            }
            if (delimiter == null) {
                delimiter = ChemicalMarkupLanguage.getDefaultDelimiter();
            }
            MatrixHelper.toTranspRectString(sb, value, delimiter);
        } else {
            MatrixHelper.instance().toString(sb, value).toString();
        }
        return sb.toString();
    }
}
