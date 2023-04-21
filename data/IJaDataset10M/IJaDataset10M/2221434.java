package net.sf.jaspertags.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletRequest;
import net.sf.jaspertags.model.Report;

/**
 * Utility that fills parameters for a report from ServletRequest parameters. It
 * also checks the datatypes and performs datatype conversion if that is
 * required
 * 
 * @author zapodot
 */
public class ReportParameterTranslator {

    /**
	 * 
	 * Creates a new Map object whith parameters for a report based on a ServletRequest object
	 * 
	 * Supports automatic conversion of the datatypes - java.lang.Integer -
	 * java.lang.Double - java.lang.Long - java.lang.Boolean -
	 * java.math.BigInteger - java.math.BigDecimal
	 * 
	 * All other types will be converted to java.lang.String
	 * 
	 * @param report
	 * @param request
	 * @return
	 */
    public static Map fillParameters(Report report, ServletRequest request) {
        HashMap map = new HashMap();
        Map rapportParamMap = report.getParametre();
        Iterator iterator = rapportParamMap.keySet().iterator();
        String paramKey = null;
        while (iterator.hasNext()) {
            paramKey = (String) iterator.next();
            Object o = request.getParameter(paramKey);
            Object translatedObject = null;
            if (o != null && !report.isSystemDefined(paramKey)) {
                Class c = report.getParameterType(paramKey);
                translatedObject = convertString((String) o, c);
            }
            if (translatedObject != null) {
                map.put(paramKey, translatedObject);
            }
        }
        return map;
    }

    /**
	 * Tries to convert a object to the specified type
	 * 
	 * Supported types are: java.lang.Integer, java.lang.Double, java.lang.Long,
	 * java.lang.Boolean, java.math.BigInteger and java.math.BigDecimal
	 * 
	 * @param convertString
	 *            Object to convert
	 * @param clazz
	 *            Specifies the type to convert to
	 * @return the converted object value of the input parameter object if the
	 *         conversion could not be made
	 */
    public static Object convertString(String convertString, Class clazz) {
        if (convertString == null || convertString.equals("")) {
            return null;
        }
        Object translatedObject = null;
        if (clazz != null) {
            try {
                if (clazz.equals(Integer.class)) {
                    translatedObject = new Integer(convertString);
                } else if (clazz.equals(Double.class)) {
                    translatedObject = new Double(convertString);
                } else if (clazz.equals(Boolean.class)) {
                    translatedObject = new Boolean(convertString);
                } else if (clazz.equals(Long.class)) {
                    translatedObject = new Long(convertString);
                } else if (clazz.equals(Float.class)) {
                    translatedObject = new Float(convertString);
                } else if (clazz.equals(BigDecimal.class)) {
                    translatedObject = new BigDecimal(convertString);
                } else if (clazz.equals(BigInteger.class)) {
                    translatedObject = new BigInteger(convertString);
                }
            } catch (NumberFormatException e) {
            }
        }
        if (translatedObject == null) {
            translatedObject = convertString;
        }
        return translatedObject;
    }
}
