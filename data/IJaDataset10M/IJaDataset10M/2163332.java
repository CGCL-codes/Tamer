package org.softnetwork.xml;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.softnetwork.beans.BeanTools;
import org.softnetwork.xml.dom.ElementList;
import org.softnetwork.xml.dom.ElementService;
import org.softnetwork.xml.dom.IOElement;
import org.softnetwork.xml.dom.xpath.XPath;
import org.softnetwork.xml.util.XMLUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author $Author: smanciot $
 * 
 * @version $Revision: 97 $
 */
public class XMLObjectFactory {

    private static final String ISO8859_1 = "ISO8859_1";

    private static final BeanTools beanTools = BeanTools.getInstance();

    /**
	 * Constructor for XMLComponentFactory.
	 */
    private XMLObjectFactory() {
        super();
    }

    public static XMLObjectFactory getInstance() {
        return new XMLObjectFactory();
    }

    public IOElement toXML(String n, Object vo) throws IllegalAccessException, InvocationTargetException, UnsupportedEncodingException, IOException {
        return toXML(n, vo, new ArrayList());
    }

    /**
	 * @param n
	 * @param vo
	 * @param forbidden
	 * @return IOElement
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
    public IOElement toXML(String n, Object vo, List forbidden) throws IllegalAccessException, InvocationTargetException, UnsupportedEncodingException, IOException {
        Class c = vo.getClass();
        String name = c.getName();
        IOElement io = new XMLIOElement(n);
        io.setAttribute("class", c.getName());
        PropertyDescriptor[] properties = beanTools.getBeanProperties(vo.getClass());
        if (vo instanceof Node) {
            forbidden.add("parentNode");
        }
        if (vo instanceof IOElement) {
            forbidden.add("bytes");
        }
        if (properties == null || properties.length == 0) {
            if (beanTools.isInstanceOf(c, String.class) || beanTools.isInstanceOf(c, Boolean.class) || beanTools.isInstanceOf(c, boolean.class) || beanTools.isInstanceOf(c, Character.class) || beanTools.isInstanceOf(c, char.class) || beanTools.isInstanceOf(c, Float.class) || beanTools.isInstanceOf(c, float.class) || beanTools.isInstanceOf(c, Double.class) || beanTools.isInstanceOf(c, double.class) || beanTools.isInstanceOf(c, Long.class) || beanTools.isInstanceOf(c, long.class) || beanTools.isInstanceOf(c, Short.class) || beanTools.isInstanceOf(c, short.class) || beanTools.isInstanceOf(c, Integer.class) || beanTools.isInstanceOf(c, int.class) || beanTools.isInstanceOf(c, Time.class) || beanTools.isInstanceOf(c, Date.class)) {
                io.setNodeValueAsObject(vo);
            } else if (beanTools.isInstanceOf(c, byte[].class)) {
                io.setNodeValue(new String((byte[]) vo));
            } else if (beanTools.isInstanceOf(c, char[].class)) {
                io.setNodeValue(new String((char[]) vo));
            }
        }
        if (properties != null) for (int i = 0; i < properties.length; i++) {
            PropertyDescriptor property = properties[i];
            name = property.getName();
            if (!forbidden.contains(name)) {
                Method read = property.getReadMethod();
                Object value = read.invoke(vo, null);
                if (value != null) {
                    c = value.getClass();
                    if (beanTools.isInstanceOf(c, String.class) || beanTools.isInstanceOf(c, Boolean.class) || beanTools.isInstanceOf(c, boolean.class) || beanTools.isInstanceOf(c, Float.class) || beanTools.isInstanceOf(c, float.class) || beanTools.isInstanceOf(c, Double.class) || beanTools.isInstanceOf(c, double.class) || beanTools.isInstanceOf(c, Long.class) || beanTools.isInstanceOf(c, long.class) || beanTools.isInstanceOf(c, Short.class) || beanTools.isInstanceOf(c, short.class) || beanTools.isInstanceOf(c, Integer.class) || beanTools.isInstanceOf(c, int.class) || beanTools.isInstanceOf(c, Time.class) || beanTools.isInstanceOf(c, Date.class)) {
                        XMLElement elt = new XMLElement(name);
                        elt.setAttribute("class", c.getName());
                        elt.setNodeValueAsObject(value);
                        io.appendChild(elt);
                    } else if (beanTools.isInstanceOf(c, byte[].class)) {
                        Element elt = new XMLElement(name);
                        elt.setAttribute("class", c.getName());
                        elt.setNodeValue(new String((byte[]) value, ISO8859_1));
                        io.appendChild(elt);
                    } else if (beanTools.isInstanceOf(c, InputStream.class)) {
                        Element elt = new XMLElement(name);
                        elt.setAttribute("class", c.getName());
                        byte[] bs = new byte[((InputStream) value).available()];
                        ((InputStream) value).read(bs);
                        elt.setNodeValue(new String(bs, ISO8859_1));
                        io.appendChild(elt);
                    } else if (beanTools.isInstanceOf(c, char[].class)) {
                        Element elt = new XMLElement(name);
                        elt.setAttribute("class", c.getName());
                        elt.setNodeValue(new String((char[]) value));
                        io.appendChild(elt);
                    } else if (beanTools.isInstanceOf(c, Collection.class)) {
                        int size = ((Collection) value).size();
                        if (size > 0) {
                            Element elt = new XMLElement(name);
                            elt.setAttribute("class", c.getName());
                            XMLAttribute attr = new XMLAttribute("size", "" + size);
                            elt.setAttributeNode(attr);
                            Iterator it = ((Collection) value).iterator();
                            while (it.hasNext()) {
                                elt.appendChild(toXML(c.getName().substring(c.getName().lastIndexOf('.') + 1), it.next(), forbidden));
                            }
                            io.appendChild(elt);
                        }
                    } else {
                        String className = c.getName();
                        if (className.startsWith("[L") && className.endsWith(";")) {
                            int length = Array.getLength(value);
                            if (length > 0) {
                                Element elt = new XMLElement(name);
                                elt.setAttribute("class", className);
                                XMLAttribute attr = new XMLAttribute("length", "" + length);
                                elt.setAttributeNode(attr);
                                String componentClass = className.substring(2, className.length() - 1);
                                for (int j = 0; j < length; j++) {
                                    elt.appendChild(toXML(componentClass.substring(componentClass.lastIndexOf('.') + 1), Array.get(value, j), forbidden));
                                }
                                io.appendChild(elt);
                            }
                        } else {
                            try {
                                Constructor ctr = beanTools.getConstructor(c, null);
                                if (ctr != null) io.appendChild(toXML(name, value, forbidden));
                            } catch (NoSuchMethodException m) {
                            }
                        }
                    }
                }
            }
        }
        return io;
    }

    public Object newInstance(Element io) throws XMLException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, UnsupportedEncodingException {
        Object o = null;
        String componentClass = null;
        boolean array = false;
        boolean collection = false;
        Attr cl = io.getAttributeNode("class");
        if (cl != null) {
            String className = cl.getValue();
            if (className.startsWith("[L") && className.endsWith(";")) {
                componentClass = className.substring(2, className.length() - 1);
                Attr l = io.getAttributeNode("length");
                if (l != null) {
                    int length = Integer.parseInt(l.getValue());
                    if (length > 0) {
                        o = Array.newInstance(Class.forName(componentClass), length);
                        array = true;
                    }
                }
            } else {
                o = Class.forName(className).newInstance();
            }
        }
        if (o != null) {
            if (beanTools.isInstanceOf(o.getClass(), Collection.class)) collection = true;
            ElementList list = ElementService.getNamedElements(io, XPath.XPATH_ALL);
            int index = 0;
            for (int i = 0; i < list.getLength(); i++) {
                Element elt = list.element(i);
                String propertyName = elt.getTagName();
                cl = elt.getAttributeNode("class");
                if (cl != null) {
                    String propertyClass = cl.getValue();
                    Class c = Class.forName(propertyClass);
                    Method write = null;
                    PropertyDescriptor property = beanTools.getBeanProperty(o.getClass(), propertyName);
                    if (property != null) {
                        write = property.getWriteMethod();
                    }
                    if (beanTools.isInstanceOf(c, String.class) || beanTools.isInstanceOf(c, Boolean.class) || beanTools.isInstanceOf(c, boolean.class) || beanTools.isInstanceOf(c, Float.class) || beanTools.isInstanceOf(c, float.class) || beanTools.isInstanceOf(c, Double.class) || beanTools.isInstanceOf(c, double.class) || beanTools.isInstanceOf(c, Long.class) || beanTools.isInstanceOf(c, long.class) || beanTools.isInstanceOf(c, Short.class) || beanTools.isInstanceOf(c, short.class) || beanTools.isInstanceOf(c, Integer.class) || beanTools.isInstanceOf(c, int.class) || beanTools.isInstanceOf(c, Time.class) || beanTools.isInstanceOf(c, Date.class)) {
                        if (write != null) write.invoke(o, new Object[] { beanTools.castProperty(c, XMLUtil.getTextContent(elt)) }); else if (array) {
                            Array.set(o, index, beanTools.castProperty(Class.forName(componentClass), XMLUtil.getTextContent(elt)));
                        } else if (collection) {
                            ((Collection) o).add(beanTools.castProperty(c, XMLUtil.getTextContent(elt)));
                        }
                    } else if (beanTools.isInstanceOf(c, InputStream.class)) {
                        ByteArrayInputStream bis = new ByteArrayInputStream(XMLUtil.getTextContent(elt).getBytes(ISO8859_1));
                        if (write != null) write.invoke(o, new Object[] { bis }); else if (array) Array.set(o, index, bis); else if (collection) ((Collection) o).add(bis);
                    } else if (beanTools.isInstanceOf(c, byte[].class)) {
                        if (write != null) write.invoke(o, new Object[] { XMLUtil.getTextContent(elt).getBytes(ISO8859_1) }); else if (array) Array.set(o, index, XMLUtil.getTextContent(elt).getBytes(ISO8859_1)); else if (collection) ((Collection) o).add(XMLUtil.getTextContent(elt).getBytes(ISO8859_1));
                    } else if (beanTools.isInstanceOf(c, char[].class)) {
                        if (write != null) write.invoke(o, new Object[] { XMLUtil.getTextContent(elt).toCharArray() }); else if (array) Array.set(o, index, XMLUtil.getTextContent(elt).toCharArray()); else if (collection) ((Collection) o).add(XMLUtil.getTextContent(elt).toCharArray());
                    } else {
                        if (write != null) write.invoke(o, new Object[] { newInstance(elt) }); else if (array) Array.set(o, index, newInstance(elt)); else if (collection) ((Collection) o).add(newInstance(elt));
                    }
                }
                index++;
            }
        }
        return o;
    }
}
