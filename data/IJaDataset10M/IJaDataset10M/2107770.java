package org.jsorb.rmi;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.NotSerializableException;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Unmarshals a graph from XML. 
 * 
 * @author Brad Koehn
 */
public class Unmarshaller {

    /** a simple map of the names of the primitives to their classes */
    private Map primitiveNamesToClasses;

    {
        primitiveNamesToClasses = new HashMap();
        primitiveNamesToClasses.put("long", Long.class);
        primitiveNamesToClasses.put("int", Integer.class);
        primitiveNamesToClasses.put("short", Short.class);
        primitiveNamesToClasses.put("byte", Byte.class);
        primitiveNamesToClasses.put("char", Character.class);
        primitiveNamesToClasses.put("string", String.class);
        primitiveNamesToClasses.put("double", Double.class);
        primitiveNamesToClasses.put("float", Float.class);
        primitiveNamesToClasses.put("boolean", Boolean.class);
        primitiveNamesToClasses.put("object", Object.class);
        primitiveNamesToClasses.put("date", Date.class);
        primitiveNamesToClasses.put("string-array", String[].class);
        primitiveNamesToClasses.put("long-array", long[].class);
        primitiveNamesToClasses.put("int-array", int[].class);
        primitiveNamesToClasses.put("short-array", short[].class);
        primitiveNamesToClasses.put("byte-array", byte[].class);
        primitiveNamesToClasses.put("char-array", char[].class);
        primitiveNamesToClasses.put("double-array", double[].class);
        primitiveNamesToClasses.put("float-array", float[].class);
        primitiveNamesToClasses.put("boolean-array", boolean[].class);
    }

    /**
	 * Unmarshals an entire XML document from a reader
	 * 
	 * @param reader		the reader supplying our document
	 * @return			the Object graph
	 * @throws Exception	if anything goes wrong
	 */
    public Object unmarshal(Reader reader) throws Exception {
        SAXReader saxReader = new SAXReader();
        saxReader.setDocumentFactory(DocumentFactory.getInstance());
        Document document = saxReader.read(reader);
        return unmarshalFromDocument(document);
    }

    /**
	 * unmarshals from a Document object
	 * 
	 * @param document	our Document
	 * @return			our object graph
	 * @throws Exception	if anything goes wrong
	 */
    public Object unmarshalFromDocument(Document document) throws Exception {
        Element root = document.getRootElement();
        return unmarshalFromElement(root, null, new HashMap());
    }

    /**
	 * Unmarshals an element into an object. Determines its class and type and calls the apropriate helper
	 * 
	 * @param element			the element we're unmarshaling from
	 * @param clazz				the class of the object we're unmarshalling to (if known)
	 * @param unmarshalContext	the unmarshaling context
	 * @return					the object
	 * @throws Exception			if anything goes wrong
	 */
    public Object unmarshalFromElement(Element element, Class clazz, Map unmarshalContext) throws Exception {
        if (element.getName().equals("null")) {
            return null;
        }
        if (clazz == null) {
            String classname = element.getName();
            clazz = (Class) primitiveNamesToClasses.get(classname);
            if (clazz == null) {
                if (classname.endsWith("-array")) {
                    String arrayClassType = classname.substring(0, classname.length() - "-array".length());
                    clazz = Array.newInstance(safeGetClass(arrayClassType), 0).getClass();
                } else {
                    clazz = safeGetClass(classname);
                }
            }
        }
        if (element.attribute("class") != null) {
            clazz = safeGetClass(element.attribute("class").getText());
        }
        Object result;
        if (clazz.isArray()) {
            result = unmarshalArrayFromElement(element, clazz, unmarshalContext);
        } else if (clazz.isPrimitive()) {
            result = unmarshalPrimitiveFromElement(element, clazz);
        } else if (Marshaller.PRIMITIVE_CLASSNAMES.matcher(clazz.getName()).matches()) {
            result = unmarshalPrimitiveFromElement(element, clazz);
        } else if (Set.class.isAssignableFrom(clazz)) {
            result = unmarshalCollectionFromElement(element, clazz, unmarshalContext);
        } else if (List.class.isAssignableFrom(clazz)) {
            result = unmarshalCollectionFromElement(element, clazz, unmarshalContext);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            result = unmarshalCollectionFromElement(element, ArrayList.class, unmarshalContext);
        } else if (Map.class.isAssignableFrom(clazz)) {
            result = unmarshalMapFromElement(element, clazz, unmarshalContext);
        } else if (Date.class.isAssignableFrom(clazz)) {
            result = unmarshalDateFromElement(element, unmarshalContext);
        } else {
            result = unmarshalEntityFromElement(element, clazz, unmarshalContext);
        }
        return result;
    }

    /**
	 * Prevents security problems by only loading classes that we should be
	 * loading. 
	 * 
	 * @param name	the name of the class to load
	 * @return	the class
	 * @throws ClassNotFoundException if the class cannot or should not be loaded
	 * @throws NotSerializableException if the class is not serializable
	 */
    private Class safeGetClass(String name) throws ClassNotFoundException, NotSerializableException {
        Class clazz = Class.forName(name, false, this.getClass().getClassLoader());
        if (clazz.isPrimitive()) {
            return clazz;
        }
        if (!Serializable.class.isAssignableFrom(clazz)) {
            throw new NotSerializableException("Cannot unmarshal instance of class which does not implement java.io.Serializable: " + name);
        }
        return clazz;
    }

    /**
	 * Unmarshals a date as &lt;date&gt;[milliseconds since 1/1/1970 GMT]&lt;/date&gt; 
	 * 
	 * Note that dates are not converted to the local time zone. 
	 * 
	 * @param element			The element from which we're unmarshalling
	 * @param unmarshalContext	the unmarshal context
	 * @return					the date
	 */
    private Object unmarshalDateFromElement(Element element, Map unmarshalContext) {
        String text = element.getTextTrim();
        long time = Long.parseLong(text);
        Date date = new Date(time);
        return date;
    }

    /**
	 * Unmarshals an entity from an element.
	 * 
	 * @param element			The element from which we're unmarshalling
	 * @param clazz				The Class of the object into which we're unmarshalling
	 * @param unmarshalContext	the unmarshal context
	 * @return					the populated object
	 * @throws Exception			if anything goes wrong
	 */
    private Object unmarshalEntityFromElement(Element element, Class clazz, Map unmarshalContext) throws Exception {
        Object object = clazz.newInstance();
        Attribute referenceAttribute = element.attribute("reference");
        if (referenceAttribute != null) {
            return unmarshalContext.get(referenceAttribute.getText());
        }
        Attribute idAttribute = element.attribute("id");
        if (idAttribute != null) {
            unmarshalContext.put(idAttribute.getText(), object);
        }
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (Iterator iter = element.elementIterator(); iter.hasNext(); ) {
            Element subElement = (Element) iter.next();
            String name = subElement.getName();
            int i = 0;
            for (; i < propertyDescriptors.length; i++) {
                if (propertyDescriptors[i].getName().equals(name)) {
                    break;
                }
            }
            Method method = propertyDescriptors[i].getWriteMethod();
            Object value = unmarshalFromElement(subElement, propertyDescriptors[i].getPropertyType(), unmarshalContext);
            if (method != null) {
                method.invoke(object, new Object[] { value });
            }
        }
        return object;
    }

    /**
	 * Unmarshals a map from an element
	 * 
	 * @param element			the element containing our map (&lt;entry&gt; elements)
	 * @param clazz				the class of the map into which we're loading data
	 * @param unmarshalContext	the unmarshal context
	 * @return					the loaded map
	 * @throws Exception			if anything goes wrong
	 */
    private Map unmarshalMapFromElement(Element element, Class clazz, Map unmarshalContext) throws Exception {
        Attribute referenceAttribute = element.attribute("reference");
        if (referenceAttribute != null) {
            return (Map) unmarshalContext.get(referenceAttribute.getText());
        }
        if (clazz == Map.class) {
            clazz = java.util.HashMap.class;
        }
        Map map = (Map) clazz.newInstance();
        Attribute idAttribute = element.attribute("id");
        if (idAttribute != null) {
            unmarshalContext.put(idAttribute.getText(), map);
        }
        List entryElements = element.elements();
        for (Iterator iter = entryElements.iterator(); iter.hasNext(); ) {
            Element entryElement = (Element) iter.next();
            Object key = unmarshalFromElement((Element) entryElement.elements().get(0), null, unmarshalContext);
            Object value = unmarshalFromElement((Element) entryElement.elements().get(1), null, unmarshalContext);
            map.put(key, value);
        }
        return map;
    }

    /**
	 * Unmarshals a collection (set or list)
	 * 
	 * @param element			The element we're unmarshaling from
	 * @param clazz				the class of the collection we're filling
	 * @param unmarshalContext	the unmarshalling context
	 * @return					the loaded collection
	 * @throws Exception		if anything goes wrong
	 */
    private Collection unmarshalCollectionFromElement(Element element, Class clazz, Map unmarshalContext) throws Exception {
        Attribute referenceAttribute = element.attribute("reference");
        if (referenceAttribute != null) {
            return (Collection) unmarshalContext.get(referenceAttribute.getText());
        }
        if (clazz == java.util.List.class) {
            clazz = java.util.ArrayList.class;
        } else if (clazz == java.util.Set.class) {
            clazz = java.util.HashSet.class;
        }
        Collection collection = (Collection) clazz.newInstance();
        Attribute idAttribute = element.attribute("id");
        if (idAttribute != null) {
            unmarshalContext.put(idAttribute.getText(), collection);
        }
        List entryElements = element.elements();
        for (Iterator iter = entryElements.iterator(); iter.hasNext(); ) {
            Element entryElement = (Element) iter.next();
            collection.add(unmarshalFromElement((Element) entryElement, null, unmarshalContext));
        }
        return collection;
    }

    /**
	 * Creates an array of the correct type and loads it full of values
	 * 
	 * @param element			the element from which we're loading the array
	 * @param clazz				the class of the array (Object[], int[])
	 * @param unmarshalContext	our unmarshalling context (for references)
	 * @return					the populated array
	 * @throws Exception			if anything goes wrong
	 */
    private Object unmarshalArrayFromElement(Element element, Class clazz, Map unmarshalContext) throws Exception {
        List subElements = element.elements();
        Object array = Array.newInstance(clazz.getComponentType(), subElements.size());
        for (int i = 0; i < subElements.size(); i++) {
            Element subElement = (Element) subElements.get(i);
            Object value = unmarshalFromElement(subElement, null, unmarshalContext);
            Array.set(array, i, value);
        }
        return array;
    }

    /**
	 * Gets a primitive and returns it in its wrapper
	 * 
	 * @param element	the Element from which we're getting the value
	 * @param clazz		the class of the primitive or wrapper
	 * @return			an object wrapping the primitive (e.g., Integer, not int)
	 */
    private Object unmarshalPrimitiveFromElement(Element element, Class clazz) {
        Object result;
        String text = element.getTextTrim();
        if ((clazz == long.class) || (clazz == Long.class)) {
            result = new Long(Long.parseLong(text));
        } else if ((clazz == int.class) || (clazz == Integer.class)) {
            result = new Integer(Integer.parseInt(text));
        } else if ((clazz == short.class) || (clazz == Short.class)) {
            result = new Short(Short.parseShort(text));
        } else if ((clazz == byte.class) || (clazz == Byte.class)) {
            result = new Byte(Byte.parseByte(text));
        } else if ((clazz == double.class) || (clazz == Double.class)) {
            result = new Double(Double.parseDouble(text));
        } else if ((clazz == float.class) || (clazz == Float.class)) {
            result = new Float(Float.parseFloat(text));
        } else if ((clazz == char.class) || (clazz == Character.class)) {
            result = new Character(text.charAt(0));
        } else if ((clazz == boolean.class) || (clazz == Boolean.class)) {
            result = new Boolean("true".equalsIgnoreCase(text));
        } else {
            result = element.getText();
        }
        return result;
    }
}
