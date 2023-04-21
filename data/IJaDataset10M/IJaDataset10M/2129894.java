package com.technoetic.xplanner.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import javax.servlet.ServletRequest;

public class RequestUtils {

    public static String toString(ServletRequest request) {
        Map parameterMap = request.getParameterMap();
        ArrayList names = new ArrayList(parameterMap.keySet());
        Collections.sort(names);
        StringBuffer str = new StringBuffer();
        str.append("{\n");
        for (int i = 0; i < names.size(); i++) {
            str.append("   ");
            String name = (String) names.get(i);
            str.append(name);
            str.append("=");
            Object o = parameterMap.get(name);
            str.append(ToStringUtils.arrayToString((Object[]) o));
            str.append("\n");
        }
        str.append("}\n");
        return str.toString();
    }

    public static boolean isParameterTrue(ServletRequest request, String paramName) {
        return Boolean.valueOf(request.getParameter(paramName)).booleanValue();
    }

    public static boolean isAttributeTrue(ServletRequest request, String attrName) {
        Object value = request.getAttribute(attrName);
        if (value == null || !(value instanceof Boolean)) return false;
        return ((Boolean) value).booleanValue();
    }
}
