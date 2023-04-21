package org.hip.kernel.util;

import java.util.Vector;
import java.util.StringTokenizer;
import org.hip.kernel.sys.VSys;
import org.hip.kernel.sys.Assert;

/**
 * This is the default implementation for the name value support.
 */
public class DefaultNameValue extends AbstractNameValue {

    /**
	 * DefaultNameValue default constructor.
	 *
	 * @param inOwingList org.hip.kernel.util.NameValueList
	 * @param inName java.lang.String
	 * @param inValue java.lang.Object
	 */
    public DefaultNameValue(NameValueList inOwingList, String inName, Object inValue) {
        super(inOwingList, inName, inValue);
    }

    /**
	 * @param inName java.lang.String
	 * @exception org.hip.kernel.util.VInvalidNameException
	 */
    protected void checkName(String inName) throws VInvalidNameException {
    }

    /**
	 * @param inValue java.lang.Object
	 * @exception org.hip.kernel.util.VInvalidValueException
	 */
    protected void checkValue(Object inValue) throws VInvalidValueException {
    }

    /**
	 * The incoming string must have the form "<name>=<value>,".
	 *
	 * @return java.util.Vector Vector of NameValues extracted from the specified inSource
	 * @param inSource java.lang.String
	 */
    public static Vector<NameValue> extract(String inSource) {
        Vector<NameValue> outNameValues = new Vector<NameValue>();
        if (VSys.assertNotNull(DefaultNameValue.class, "extract", inSource) == Assert.FAILURE) return outNameValues;
        StringTokenizer lTokenizer = new StringTokenizer(inSource, ",");
        while (lTokenizer.hasMoreElements()) {
            String lNext = lTokenizer.nextToken();
            int lPosition = lNext.indexOf("=");
            String lName = lNext.substring(0, Math.min(lPosition, lNext.length())).trim();
            String lValue = lNext.substring(lPosition + 1).trim();
            NameValue lNameValue = new DefaultNameValue(null, lName, lValue);
            outNameValues.addElement(lNameValue);
        }
        return outNameValues;
    }
}
