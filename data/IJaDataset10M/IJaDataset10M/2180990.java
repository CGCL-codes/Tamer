package org.richa.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class provides utility methods for the store tag
 * @author ram
 *
 */
public class StoreUtils {

    protected static Log log = LogFactory.getLog(StoreUtils.class);

    /**
	 * Get all the get methods in the bean as a JSON Map
	 */
    public static String getFieldsAsMap(Object bean) {
        boolean first = true;
        AppendingStringBuffer fields = new AppendingStringBuffer();
        PropertyDescriptor[] props = PropertyDescriptorCache.get(bean);
        for (int i = 0; i < props.length; i++) {
            if (!props[i].getName().equals("class")) {
                if (!first) fields.appendln("       ,{name: '" + props[i].getName() + "'}"); else fields.appendln("        {name: '" + props[i].getName() + "'}");
                first = false;
            }
        }
        return fields.toString();
    }

    /**
	 * Get all the get methods in the bean as a JSON Array
	 */
    public static String getFieldsAsArray(Object bean) {
        boolean first = true;
        AppendingStringBuffer fields = new AppendingStringBuffer();
        PropertyDescriptor[] props = PropertyDescriptorCache.get(bean);
        for (int i = 0; i < props.length; i++) {
            if (!props[i].getName().equals("class")) {
                if (!first) fields.append(",'" + props[i].getName() + "'"); else fields.append("'" + props[i].getName() + "'");
                first = false;
            }
        }
        return fields.toString();
    }

    /**
	 * Serialize a list into a JSON to an array of Maps
	 */
    public static JSONArray serializeListAsMap(List results) throws InvocationTargetException, IllegalAccessException, JSONException {
        Iterator iter;
        JSONArray rows = new JSONArray();
        iter = results.iterator();
        while (iter.hasNext()) {
            JSONObject row = new JSONObject();
            Object bean = iter.next();
            PropertyDescriptor[] props = PropertyDescriptorCache.get(bean);
            for (int i = 0; i < props.length; i++) {
                if (!props[i].getName().equals("class")) {
                    Object result = props[i].getReadMethod().invoke(bean, (Object[]) null);
                    row.put(props[i].getName(), result);
                }
            }
            rows.put(row);
        }
        return rows;
    }

    /**
	 * Serialize a list into a JSON to an array of Array
	 */
    public static JSONArray serializeListAsArray(List results) throws InvocationTargetException, IllegalAccessException, JSONException {
        Iterator iter;
        JSONArray rows = new JSONArray();
        iter = results.iterator();
        while (iter.hasNext()) {
            JSONArray row = new JSONArray();
            Object bean = iter.next();
            PropertyDescriptor[] props = PropertyDescriptorCache.get(bean);
            for (int i = 0; i < props.length; i++) {
                if (!props[i].getName().equals("class")) {
                    Object result = props[i].getReadMethod().invoke(bean, (Object[]) null);
                    row.put(result);
                }
            }
            rows.put(row);
        }
        return rows;
    }
}
