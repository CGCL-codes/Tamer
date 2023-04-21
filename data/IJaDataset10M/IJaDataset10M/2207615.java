package com.hilaver.dzmis.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.util.Date;
import java.util.Set;
import com.hilaver.dzmis.Constants;
import com.hilaver.dzmis.basicinfo.BiFranceCustomer;

public class SimpleObj2XML {

    private static String GET = "get";

    public static String toXMLFragment(Object obj) throws Exception {
        if (obj == null) {
            return "";
        }
        Field[] fields;
        if (obj.getClass().getName().indexOf("$") != -1) {
            fields = obj.getClass().getSuperclass().getDeclaredFields();
        } else {
            fields = obj.getClass().getDeclaredFields();
        }
        StringBuffer sb = new StringBuffer();
        for (Field field : fields) {
            String methodName = GET + firstLetterToUpcase(field.getName());
            Method method;
            try {
                method = obj.getClass().getMethod(methodName, new Class[] {});
            } catch (Exception e) {
                method = obj.getClass().getMethod(GET + field.getName(), new Class[] {});
            }
            Object value = method.invoke(obj, new Object[] {});
            sb.append("<" + field.getName() + ">");
            if (isSimpleType(method.getReturnType())) {
                sb.append(Constants.XML_CDATA[0]);
                if (field.getName().indexOf("photoName") != -1 || field.getName().indexOf("PhotoName") != -1) {
                    sb.append(value == null ? "noimage.gif" : value);
                } else if (field.getName().indexOf("photo1Name") != -1 || field.getName().indexOf("Photo1Name") != -1) {
                    sb.append(value == null ? "noimage.gif" : value);
                } else if (field.getName().indexOf("photo2Name") != -1 || field.getName().indexOf("Photo2Name") != -1) {
                    sb.append(value == null ? "noimage.gif" : value);
                } else if (field.getName().indexOf("photo3Name") != -1 || field.getName().indexOf("Photo3Name") != -1) {
                    sb.append(value == null ? "noimage.gif" : value);
                } else if (field.getName().indexOf("photo4Name") != -1 || field.getName().indexOf("Photo4Name") != -1) {
                    sb.append(value == null ? "noimage.gif" : value);
                } else if (field.getName().indexOf("Date") != -1 || field.getName().indexOf("date") != -1 || field.getName().indexOf("Time") != -1) {
                    try {
                        String dateStr = StringUtils.toString((Date) value);
                        sb.append(dateStr == null ? "" : dateStr);
                    } catch (Exception e) {
                        sb.append(value);
                    }
                } else {
                    sb.append(value == null ? "" : value);
                }
                sb.append(Constants.XML_CDATA[1]);
            }
            sb.append("</" + field.getName() + ">");
        }
        return sb.toString();
    }

    private static String firstLetterToUpcase(String in) {
        return in.substring(0, 1).toUpperCase() + in.substring(1);
    }

    public static Boolean isSimpleType(Class clazz) {
        if (clazz.equals(Integer.class)) {
            return true;
        }
        if (clazz.equals(Float.class)) {
            return true;
        }
        if (clazz.equals(Double.class)) {
            return true;
        }
        if (clazz.equals(String.class)) {
            return true;
        }
        if (clazz.equals(Date.class)) {
            return true;
        }
        return false;
    }
}
