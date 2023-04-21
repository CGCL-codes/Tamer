package net.livebookstore.util;

import java.lang.reflect.Method;

public class BeanUtil {

    /**
     * Copy all basic properties (boolean, int, String, Date) 
     * but ignore reference properties.
     * 
     * @param srcObj
     * @param destObj
     */
    public static void copyProperties(Object srcObj, Object destObj) {
        Class dest = destObj.getClass();
        Class src = srcObj.getClass();
        Method[] ms = dest.getMethods();
        for (int i = 0; i < ms.length; i++) {
            String name = ms[i].getName();
            if (name.startsWith("set")) {
                Class[] cc = ms[i].getParameterTypes();
                if (cc.length == 1) {
                    String type = cc[0].getName();
                    if (type.equals("java.lang.String") || type.equals("int") || type.equals("java.lang.Integer") || type.equals("long") || type.equals("java.lang.Long") || type.equals("boolean") || type.equals("java.lang.Boolean") || type.equals("java.util.Date")) {
                        try {
                            String getMethod = "g" + name.substring(1);
                            Method getM = src.getMethod(getMethod, new Class[0]);
                            Object ret = getM.invoke(srcObj, new Object[] {});
                            if (ret != null) {
                                System.out.println("call: " + ms[i].getName() + ": " + getMethod);
                                ms[i].invoke(destObj, new Object[] { ret });
                            }
                        } catch (Exception e) {
                            System.out.println("Invoke method " + ms[i].getName() + " failed: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }
}
