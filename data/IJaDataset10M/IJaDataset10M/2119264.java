package fi.iki.asb.util.config.handler;

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import fi.iki.asb.util.config.*;

/**
 * This class handled &lt;string&gt; elements.
 *
 * @author Antti S. Brax
 * @version 1.0
 */
public class ElementHandler_staticCall extends ElementHandler {

    /**
     * Handle a &lt;string&gt; element.
     *
     * @param elem the &lt;string&gt; element
     * @param conf the configuration that is loaded
     */
    public Value handle(Node elem, Map conf) throws XmlConfigException {
        String name = getAttributeValue(elem, "name", conf);
        if (name == null) {
            throw new XmlConfigException("No name attribute in <staticCall>");
        }
        String className = getAttributeValue(elem, "class", conf);
        if (className == null) {
            throw new XmlConfigException("No class attribute in <staticCall>");
        }
        Class clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new XmlConfigException("Class " + className + " not found", ex);
        }
        List params = XmlTool.getChildrenByTagName((Element) elem, "param");
        Object[] values = new Object[params.size()];
        Class[] classes = new Class[params.size()];
        for (int i = 0; i < params.size(); i++) {
            Value value = handleNode((Node) params.get(i), conf);
            values[i] = value.getValue();
            classes[i] = value.getValueClass();
        }
        Method method = getDeclaredMethod(clazz, name, classes);
        if (method == null) {
            throw new XmlConfigException("Class " + clazz + " does " + "not contain method " + getMethodName(name, classes));
        }
        Value value;
        try {
            Object o = method.invoke(null, values);
            if (o != null) {
                value = new Value(o, o.getClass());
            } else {
                value = new Value(null, Object.class);
            }
        } catch (Exception ex) {
            throw new XmlConfigException("Could not access method " + clazz.getName() + "." + getMethodName(name, classes), ex);
        }
        return value;
    }
}
