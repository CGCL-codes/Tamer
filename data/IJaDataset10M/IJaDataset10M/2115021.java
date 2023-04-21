package com.synweb.server;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ObjectFactory {

    public static String convertPropertyName(String name) {
        String lowerName = name.toLowerCase();
        String[] pieces = lowerName.split("_");
        if (pieces.length == 1) {
            return lowerName;
        }
        StringBuffer result = new StringBuffer(pieces[0]);
        for (int i = 1; i < pieces.length; i++) {
            result.append(Character.toUpperCase(pieces[i].charAt(0)));
            result.append(pieces[i].substring(1));
        }
        return result.toString();
    }

    public static List convertToObjects(ResultSet rs, Class cl) {
        List result = new ArrayList();
        try {
            int colCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object item = cl.newInstance();
                for (int i = 1; i <= colCount; i += 1) {
                    String colName = rs.getMetaData().getColumnName(i);
                    String propertyName = convertPropertyName(colName);
                    Object value = rs.getObject(i);
                    PropertyDescriptor pd = new PropertyDescriptor(propertyName, cl);
                    Method mt = pd.getWriteMethod();
                    mt.invoke(item, new Object[] { value });
                }
                result.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
