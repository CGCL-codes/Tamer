package blue.utility;

import java.lang.reflect.Field;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @author steven
 */
public class ValuesUtility {

    public static void checkNullString(Object obj) {
        checkNullString(obj, false);
    }

    public static void checkNullString(Object obj, boolean printMessages) {
        Class c = obj.getClass();
        Field[] fields = c.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getType() == String.class) {
                try {
                    if (fields[i].get(obj) == null) {
                        if (printMessages) {
                            System.err.println("ValuesUtility: Null String found in " + c.getName() + " field: " + fields[i].getName());
                        }
                        fields[i].set(obj, "");
                    }
                } catch (IllegalAccessException iae) {
                    iae.printStackTrace();
                }
            }
        }
    }

    public static void main(String args[]) {
        Object obj = new Object() {

            String val1 = "test";

            String val2 = "test2";

            String val3 = null;
        };
        System.out.println(ToStringBuilder.reflectionToString(obj));
        ValuesUtility.checkNullString(obj, true);
        System.out.println(ToStringBuilder.reflectionToString(obj));
    }
}
