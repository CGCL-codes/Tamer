package wow.play.cricket.common;

import java.lang.reflect.Method;

/**
 *
 * @author NURUL SIDDIK
 */
public class LCUtils {

    /** Creates a new instance of LCUtils */
    public LCUtils() {
    }

    public static boolean isNullOrBlank(String str) {
        if (str == null || str.equals("")) {
            return true;
        }
        return false;
    }

    /**
     * This function checks for variables which are empty string by calling their 
     * getter methods and then set those variables to null by calling their setter
     * methods. This is usefull for VO objects before sending the object for database
     * transaction.
     * @param obj The Value Object(VO) whose variable is to be converted
     * @return The same VO after conversion.
     * @throws java.lang.Exception Exception
     */
    public static Object convertBlankToNull(Object obj) throws Exception {
        for (Method objMethod : obj.getClass().getMethods()) {
            String methodName = objMethod.getName();
            if ("get".equals(methodName.substring(0, 3)) && objMethod.getModifiers() == Method.DECLARED && objMethod.getReturnType().equals(String.class)) {
                String rawMethodName = methodName.substring(3);
                String retValue = (String) objMethod.invoke(obj);
                if ("".equals(retValue)) {
                    Method setMethod = obj.getClass().getMethod("set" + rawMethodName, String.class);
                    setMethod.invoke(obj, new Object[] { null });
                }
            }
        }
        return obj;
    }
}
