package uchicago.src.xml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class XMLFactory {

    String xmlName;

    Object target;

    ArrayList propNames;

    public XMLFactory(String name, Object target, ArrayList propNames) {
        xmlName = name;
        this.target = target;
        this.propNames = propNames;
    }

    public String getXMLStart() {
        StringBuffer b = new StringBuffer("<");
        b.append(xmlName);
        b.append(" ");
        return b.toString();
    }

    public String getXMLEnd() {
        StringBuffer b = new StringBuffer("</");
        b.append(xmlName);
        b.append(">");
        return b.toString();
    }

    public String getXMLAttributes() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class clazz = target.getClass();
        StringBuffer b = new StringBuffer(" ");
        for (int i = 0; i < propNames.size(); i++) {
            String propName = (String) propNames.get(i);
            b.append(propName);
            b.append("=\"");
            Method method = clazz.getMethod("get" + propName, new Class[] {});
            Object o = method.invoke(target, new Object[] {});
            b.append(o.toString());
            b.append("\" ");
        }
        return b.toString();
    }

    public String getXMLStartAttrib() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        StringBuffer b = new StringBuffer(getXMLStart());
        b.append(getXMLAttributes());
        return b.toString();
    }

    public String getXML() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        StringBuffer b = new StringBuffer(getXMLStart());
        b.append(getXMLAttributes());
        b.append("/>");
        return b.toString();
    }
}
