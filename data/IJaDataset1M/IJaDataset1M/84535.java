package com.sds.fw.util;

import java.lang.reflect.Method;

/**
 * A class inspecting the values of Bean Object.
 * For example:
 * <pre>
 * 		BeanUtil.print(studentVO);
 * 		String studentVOAsString = BeanUtil.getString(studentVO);
 * </pre>
 * @author  SDS
 */
public class BeanUtil {

    /**
     * Prints the values of Bean object at the screen.
     *
     * @param     obj  the object to inspect.
     */
    public static void print(Object obj) {
        try {
            if (obj == null) return;
            Class c = obj.getClass();
            System.out.println("============" + c.getName() + " �� ��  ���� ======================");
            Method[] ms = c.getDeclaredMethods();
            for (int i = 0; i < ms.length; i++) {
                if (ms[i].getName().indexOf("get") == 0) {
                    System.out.print(ms[i].getName().substring(3) + " : ");
                    Object value = ms[i].invoke(obj, null);
                    System.out.println(value);
                }
            }
            System.out.println("============" + c.getName() + " �� ��  ���� ======================");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * returns the values of a Bean object as a String object.
     *
     * @param     obj  the object to inspect.
     * @return    the string value of the values of a Bean object.
     */
    public static String getString(Object obj) {
        StringBuffer sb = new StringBuffer();
        try {
            if (obj == null) return null;
            Class c = obj.getClass();
            sb.append("============" + c.getName() + " �� ��  ���� ======================\n");
            Method[] ms = c.getDeclaredMethods();
            for (int i = 0; i < ms.length; i++) {
                if (ms[i].getName().indexOf("get") == 0) {
                    sb.append(ms[i].getName().substring(3) + " : ");
                    Object value = ms[i].invoke(obj, null);
                    sb.append(value + "\n");
                }
            }
            sb.append("============" + c.getName() + " �� ��  ���� ======================\n");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return sb.toString();
    }
}
