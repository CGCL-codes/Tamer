package org.apache.struts2.plugin.json.reflection;

import org.apache.struts2.plugin.json.model.JSONNumber;

/**
 * @author VERDOÏA Laurent <verdoialaurent@gmail.com>
 */
public class JSONNumberReflection {

    public static JSONNumber reflection(Object o) {
        if (o instanceof Number) {
            return new JSONNumber((Number) o);
        }
        return null;
    }
}
