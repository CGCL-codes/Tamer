package com.jspx.sioc.type;

import com.jspx.utils.StringUtil;

/**
 * Created by IntelliJ IDEA.
 * User:chenYuan (mail:cayurain@21cn.com)
 * Date: 2007-3-22
 * Time: 22:19:41
 */
public class BooleanArrayXmlType extends ArrayXmlType {

    public String getTypeString() {
        return "array";
    }

    public Object getTypeObject() {
        String[] stringArray = StringUtil.split((String) value, ",");
        boolean[] result = new boolean[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            result[i] = StringUtil.toBoolean(stringArray[i]);
        }
        return result;
    }

    /**
     * 返回XML结果
     *
     * @return
     */
    public String getXMLString() {
        if (value instanceof boolean[]) {
            boolean[] theValue = (boolean[]) value;
            if (theValue == null || theValue.length < 1) return "";
            StringBuffer sb = new StringBuffer();
            sb.append("<array name=\"").append(name).append("\" class=\"").append("boolean").append("\">\r\n");
            for (boolean o : theValue) {
                sb.append("<value>").append(o).append("</value>\r\n");
            }
            sb.append("</array>\r\n");
            return sb.toString();
        }
        Boolean[] theValue = (Boolean[]) value;
        if (theValue == null || theValue.length < 1) return "";
        StringBuffer sb = new StringBuffer();
        sb.append("<array name=\"").append(name).append("\" class=\"").append("boolean").append("\">\r\n");
        for (Boolean o : theValue) {
            sb.append("<value>").append(o).append("</value>\r\n");
        }
        sb.append("</array>\r\n");
        return sb.toString();
    }
}
