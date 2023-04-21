package joelib2.feature.result;

import joelib2.feature.FeatureResult;
import joelib2.io.BasicIOTypeHolder;
import joelib2.io.IOType;
import joelib2.molecule.types.BasicPairDataCML;
import joelib2.molecule.types.PairData;
import wsi.ra.text.UnicodeHelper;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Category;

/**
 * String array results of variable size.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.11 $, $Date: 2005/02/17 16:48:30 $
 */
public class StringVectorResult extends BasicPairDataCML implements Cloneable, FeatureResult, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance(StringVectorResult.class.getName());

    private static final String basicFormat = "n<e0,...e(n-1)>\n" + "with n, e0,...,e(n-1) of type Strings";

    private static final String lineFormat = "n\n" + "e0\n" + "...\n" + "e(n-1)>\n" + "with n, e0,...,e(n-1) of type Strings";

    protected Vector<String> stringVector = new Vector<String>();

    /**
     *  Constructor for the IntArrayResult object
     */
    public StringVectorResult() {
        this.setKey(this.getClass().getName());
        this.setKeyValue(this);
    }

    public void addString(String string) {
        stringVector.add(string);
    }

    public Object clone() {
        StringVectorResult newObj = new StringVectorResult();
        return clone(newObj);
    }

    public StringVectorResult clone(StringVectorResult other) {
        super.clone(other);
        if (this.stringVector != null) {
            other.stringVector = (Vector<String>) this.stringVector.clone();
        } else {
            other.setStringVector(null);
        }
        return other;
    }

    /**
     *  Description of the Method
     *
     * @param ioType  Description of the Parameter
     * @return        Description of the Return Value
     */
    public String formatDescription(IOType ioType) {
        String format = basicFormat;
        if (ioType.equals(BasicIOTypeHolder.instance().getIOType("SDF"))) {
            format = lineFormat;
        }
        return format;
    }

    /**
     *  Description of the Method
     *
     * @param pairData  Description of the Parameter
     * @param ioType    Description of the Parameter
     * @return          Description of the Return Value
     */
    public boolean fromPairData(IOType ioType, PairData pairData) {
        this.setKey(pairData.getKey());
        Object value = pairData.getKeyValue();
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
        int numEntries = 0;
        boolean success = true;
        try {
            numEntries = Integer.parseInt(lnr.readLine());
            StringBuffer buffer = new StringBuffer(numEntries * 100);
            stringVector.ensureCapacity(numEntries);
            for (int index = 0; index < numEntries; index++) {
                stringVector.add(UnicodeHelper.decode(lnr.readLine()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            success = false;
        }
        return success;
    }

    public String getString(int index) {
        return (String) stringVector.get(index);
    }

    /**
     *  Gets the double attribute of the IntArrayResult object
     *
     * @return   The double value
     */
    public List getStringVector() {
        return stringVector;
    }

    public boolean init(String _descName) {
        this.setKey(_descName);
        return true;
    }

    /**
     *  Sets the double attribute of the IntArrayResult object
     *
     * @param _iarray  The new double value
     */
    public void setStringVector(List<String> list) {
        stringVector.clear();
        for (int index = 0; index < list.size(); index++) {
            stringVector.add(list.get(index));
        }
    }

    /**
     *  Description of the Method
     *
     * @param ioType  Description of the Parameter
     * @return        Description of the Return Value
     */
    public String toString(IOType ioType) {
        StringBuffer sb = new StringBuffer(stringVector.size() * 100);
        sb.append(stringVector.size());
        sb.append('\n');
        int vSize = stringVector.size();
        int vSize_1 = vSize - 1;
        for (int index = 0; index < stringVector.size(); index++) {
            sb.append(UnicodeHelper.encode(stringVector.get(index).toString()));
            if (index < vSize_1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}
