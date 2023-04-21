package org.mlc.xml.mapper;

import org.w3c.dom.Node;
import org.apache.xerces.parsers.DOMParser;
import java.io.FileInputStream;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import java.util.Iterator;
import org.mlc.xml.utils.XMLUtils;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;
import java.lang.IllegalAccessException;
import java.lang.NoSuchMethodException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.text.ParseException;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.io.InputStream;

/**
 * This class is used to create objects from an object xml file and a mapping file.
 *
 * @author  Michael Connor (all rights reserved)
 * @version 1.0
 */
public class Mapper {

    MapHolder mapHolder;

    Map uniqueReferenceMap = new HashMap();

    /**
     * Creates a Mapper instance based on a file
     * name.
     *
     * @param mapFile The name of the map file
     */
    public Mapper(String mapFile) throws Exception {
        mapHolder = new MapHolder(mapFile);
    }

    /**
     * Creates a Mapper instance based on an input stream.
     *
     * @param stream The map file stream
     */
    public Mapper(InputStream stream) throws Exception {
        mapHolder = new MapHolder(stream);
    }

    /**
     * Returns an object based on an input stream file.  This method
     * extracts the mapping between xml and Java classes.  The root node is 
     * constructed and the hierarchy is built recursively.  Note that this
     * method differs from getObjects which ignores the root node of the 
     * objectFile and constructs only the children.  Note that the
     * input stream should be closed by the caller.
     *
     * @param objectFile The name of the object file
     * @return The newly constructed objects
     * @throws Exception
     */
    public Object getObject(InputStream inputStream) throws Exception {
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(inputStream));
        Document document = parser.getDocument();
        Node root = XMLUtils.getChildren(document)[0];
        return getObject(root);
    }

    /**
     * Returns an object based on an object file.  This method
     * extracts the mapping between xml and Java classes.  The root node is 
     * constructed and the hierarchy is built recursively.  Note that this
     * method differs from getObjects which ignores the root node of the 
     * objectFile and constructs only the children.
     *
     * @param objectFile The name of the object file
     * @return The newly constructed objects
     * @throws Exception
     */
    public Object getObject(String objectFile) throws Exception {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(objectFile);
            return getObject(stream);
        } finally {
            if (stream != null) stream.close();
        }
    }

    /**
     * Returns an object based on a mapping file and a MapHolder file.  This method
     * extracts the mapping between xml and Java classes.  The root node is 
     * constructed and the hierarchy is built recursively.  Note that this
     * method differs from getObjects which ignores the root node of the 
     * objectFile and constructs only the children.
     *
     * @param mapFile The name of the mapping file
     * @param objectFile The name of the object file
     * @return The newly constructed objects
     * @throws Exception
     */
    public static Object getObject(String mapFile, String objectFile) throws Exception {
        Mapper mapper = new Mapper(mapFile);
        return mapper.getObject(objectFile);
    }

    /**
     * Returns a List of objects based on an file.  This method
     * extracts the mapping between xml and Java classes.  The root node is ignored and
     * only the children of the root are converted into objects.
     *
     * @param objectFile The name of the object file
     * @return The newly constructed objects
     * @throws Exception
     */
    public static List getObjects(String mapFile, String objectFile) throws Exception {
        Mapper mapper = new Mapper(mapFile);
        return mapper.getObjects(objectFile);
    }

    /**
     * Returns a List of objects based on a mapping file and a MapHolder file.  This method
     * extracts the mapping between xml and Java classes.  The root node is ignored and
     * only the children of the root are converted into objects.
     *
     * @param objectStream The stream containing the object xml
     * @return The newly constructed objects
     * @throws Exception
     */
    public List getObjects(InputStream stream) throws Exception {
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(stream));
        Document document = parser.getDocument();
        Node root = XMLUtils.getChildren(document)[0];
        return getObjects(root);
    }

    /**
     * Returns a List of objects based on a mapping file and a MapHolder file.  This method
     * extracts the mapping between xml and Java classes.  The root node is ignored and
     * only the children of the root are converted into objects.
     *
     * @param objectFile The name of the object file
     * @return The newly constructed objects
     * @throws Exception
     */
    public List getObjects(String objectFile) throws Exception {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(objectFile);
            return getObjects(stream);
        } finally {
            if (stream != null) stream.close();
        }
    }

    /**
     * Returns a List of objects based on a Node.  This method
     * extracts the mapping between xml and Java classes.  The children of
     * the node parameter are returned while the root node is ignored.
     *
     * @param node The node to create from an object
     * @return The newly constructed objects
     * @throws Exception
     */
    private List getObjects(Node node) throws Exception {
        Node[] objectNodes = XMLUtils.getChildren(node);
        List objects = new ArrayList(objectNodes.length);
        for (int i = 0; i < objectNodes.length; i++) {
            try {
                Object object = getObject(objectNodes[i]);
                objects.add(object);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Error building child of node with name '" + objectNodes[i].getNodeName() + "'. " + e.getMessage());
            }
        }
        return objects;
    }

    /**
     * Returns an object based on a Node and the MapHolder.  This method
     * extracts the mapping between xml and Java classes.
     *
     * @param node The node to create from an object
     * @return The newly constructed object
     * @throws Exception
     */
    private Object getObject(Node node) throws Exception {
        String nodeName = node.getNodeName();
        ClassBinding classBinding = mapHolder.getClassBinding(nodeName);
        if (classBinding == null) throw new Exception("Object with name '" + nodeName + "' could not be converted automatically and " + "does not have a matching class definition");
        Class objectClass = null;
        try {
            objectClass = Class.forName(classBinding.getClassName());
        } catch (ClassNotFoundException e) {
            throw new Exception("The class specified '" + classBinding.getClassName() + "' by xml-name " + classBinding.getXmlName() + " was not found in the classpath");
        }
        if (this.convertableType(objectClass)) {
            return this.convert(objectClass, XMLUtils.getText(node), null);
        }
        Object instance = classBinding.getUniqueField() != null ? fetchObjectReference(classBinding, node) : null;
        if (instance == null) {
            instance = objectClass.newInstance();
            List fields = new ArrayList();
            fields.addAll(Arrays.asList(XMLUtils.getAttributes(node)));
            fields.addAll(Arrays.asList(XMLUtils.getChildren(node)));
            for (Iterator i = fields.iterator(); i.hasNext(); ) {
                Node field = (Node) i.next();
                String fieldName = field.getNodeName();
                String fieldValue = field.getNodeType() == Node.ATTRIBUTE_NODE ? field.getNodeValue() : XMLUtils.getText(field);
                FieldBinding fieldBinding = classBinding.getField(fieldName);
                if (fieldBinding == null) {
                    String fieldType = field.getNodeType() == Node.ATTRIBUTE_NODE ? "attribute" : "element";
                    throw new Exception("Unable to find binding for " + fieldType + " " + fieldName + " in node " + node.getNodeName());
                }
                Class propertyClass = null;
                try {
                    propertyClass = getPropertyClass(fieldBinding.getProperty(), objectClass);
                } catch (NoSuchMethodException ex) {
                    throw new Exception("No property named " + fieldBinding.getProperty() + " exists in class " + objectClass.getName() + " for node " + node.getNodeName());
                }
                Object propertyValue = null;
                if (List.class.isAssignableFrom(propertyClass)) {
                    propertyValue = getObjects(field);
                } else if (Map.class.isAssignableFrom(propertyClass)) {
                    propertyValue = getMap(field, propertyClass, fieldBinding.getHint());
                } else if (Set.class.isAssignableFrom(propertyClass)) {
                    propertyValue = getSet(field, propertyClass);
                } else {
                    if (convertableType(propertyClass)) {
                        try {
                            propertyValue = convert(propertyClass, fieldValue, fieldBinding.getHint());
                        } catch (Exception e) {
                            throw new Exception("Conversion exception while trying to convert '" + fieldValue + " to type " + propertyClass.getName() + " " + e.toString());
                        }
                    } else {
                        Node[] objectAttribute = XMLUtils.getChildren(field);
                        if (objectAttribute.length > 0) {
                            propertyValue = getObject(objectAttribute[0]);
                        }
                    }
                }
                if (propertyValue != null) setProperty(fieldBinding.getProperty(), instance, propertyValue);
            }
            if (classBinding.getUniqueField() != null) this.storeObjectReference(classBinding, node, instance);
        }
        return instance;
    }

    /**
     * Stores the object based on it's unique field.  This method assumes that
     * the classBinding actually has a uniqueField.
     *
     * @param classBinding The class binding for the object
     * @param node The node we are working from
     * @param The java object that was created from the node
     */
    public void storeObjectReference(ClassBinding classBinding, Node node, Object object) {
        Map classReferences = (Map) this.uniqueReferenceMap.get(classBinding);
        if (classReferences == null) {
            classReferences = new HashMap();
            this.uniqueReferenceMap.put(classBinding, classReferences);
        }
        Node keyNode = XMLUtils.hasAttribute(node, classBinding.getUniqueField()) ? XMLUtils.getAttributeNode(node, classBinding.getUniqueField()) : XMLUtils.hasChild(node, classBinding.getUniqueField()) ? XMLUtils.getChild(node, classBinding.getUniqueField()) : null;
        if (keyNode != null) {
            String key = keyNode.getNodeType() == Node.ATTRIBUTE_NODE ? keyNode.getNodeValue() : XMLUtils.getText(keyNode);
            if (key != null) {
                classReferences.put(key, object);
            }
        }
    }

    /**
     * This method will return an object if the class binding supports
     * a unique field.
     *
     * @param classBinding The classBinding associated with the node
     * @param node The node we are working on
     * @return an object if it is found, or null otherwise
     */
    public Object fetchObjectReference(ClassBinding classBinding, Node node) {
        Map classReferences = (Map) this.uniqueReferenceMap.get(classBinding);
        if (classReferences == null) return null;
        Node keyNode = XMLUtils.hasAttribute(node, classBinding.getUniqueField()) ? XMLUtils.getAttributeNode(node, classBinding.getUniqueField()) : XMLUtils.hasChild(node, classBinding.getUniqueField()) ? XMLUtils.getChild(node, classBinding.getUniqueField()) : null;
        if (keyNode == null) return null;
        String key = keyNode.getNodeType() == Node.ATTRIBUTE_NODE ? keyNode.getNodeValue() : XMLUtils.getText(keyNode);
        return key != null ? classReferences.get(key) : null;
    }

    /**
     * Returns a Set of the type setType. This method
     * extracts the mapping between xml and Java classes.
     *
     * @param node The node to create from an object
     * @param mapType The type of set to instantiate
     * @return The newly constructed set
     * @throws Exception
     */
    private Set getSet(Node node, Class setType) throws Exception {
        Set set = null;
        try {
            if (setType.isInterface()) set = new HashSet(); else {
                set = (Set) setType.newInstance();
            }
            List children = getObjects(node);
            set.addAll(children);
        } catch (Exception e) {
            throw new Exception("Error while trying to construct set for node '" + node.getNodeName() + "'.  Detail : " + e.toString());
        }
        return set;
    }

    /**
     * Returns an object based on a Node and a MapHolder.  This method
     * extracts the mapping between xml and Java classes.
     *
     * @param node The node to create from an object
     * @param mapType The type of map to instantiate
     * @return The newly constructed object
     * @throws Exception
     */
    private Object getMap(Node node, Class mapType, String xmlPropertyBinding) throws Exception {
        Map map = null;
        try {
            if (mapType.isInterface()) map = new HashMap(); else {
                map = (Map) mapType.newInstance();
            }
            Node[] mapNodes = XMLUtils.getChildren(node);
            for (int i = 0; i < mapNodes.length; i++) {
                Node mapMember = mapNodes[i];
                ClassBinding classBinding = mapHolder.getClassBinding(mapMember.getNodeName());
                if (classBinding == null) throw new Exception("Error while trying to construct map for node '" + node.getNodeName() + "'.  Child with name '" + mapMember.getNodeName() + "' does not have a matching class definition");
                FieldBinding fieldBinding = classBinding.getField(xmlPropertyBinding);
                if (fieldBinding == null) throw new Exception("Error while trying to construct map for node '" + node.getNodeName() + "'.  Child with name '" + mapMember.getNodeName() + "does not have a field binding with name '" + xmlPropertyBinding + "'");
                Object bean = null;
                try {
                    bean = getObject(mapMember);
                } catch (Exception e) {
                    throw new Exception("Error while trying to construct map for node '" + node.getNodeName() + "'.  Child with name '" + mapMember.getNodeName() + "could not be constructed.  Detail " + e.toString());
                }
                String beanProperty = fieldBinding.getProperty();
                if (beanProperty == null) throw new Exception("Error while trying to construct map for node '" + node.getNodeName() + "'.  Object type " + classBinding.getClassName() + " with property binding " + xmlPropertyBinding + " did not specify a bean property");
                Object mapKey = null;
                try {
                    mapKey = getProperty(beanProperty, bean);
                } catch (Exception e) {
                    throw new Exception("Error while trying to construct map for node '" + node.getNodeName() + "'.  Property " + xmlPropertyBinding + " on Object type " + classBinding.getClassName() + " could not be read.  Detail : " + e.toString());
                }
                map.put(mapKey, bean);
            }
        } catch (Exception e) {
            throw new Exception("Error while trying to construct map for node '" + node.getNodeName() + "'.  Detail : " + e.toString());
        }
        return map;
    }

    /**
     * This method sets a property on a bean based on the propertyName
     * parameter to the value specified in the propertyValue parameter
     * on the object specified by the bean parameter.
     *
     * @param propertyName The name of the property to set
     * @param bean The bean recipient of the property
     * @param propertyValue The object value of the property
     * @throws Exception
     */
    private static void setProperty(String propertyName, Object bean, Object propertyValue) throws Exception {
        String methodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        Class propertyType = getPropertyClass(propertyName, bean.getClass());
        java.lang.reflect.Method method = bean.getClass().getMethod(methodName, new Class[] { propertyType });
        try {
            method.invoke(bean, new Object[] { propertyValue });
        } catch (Exception e) {
            System.out.println("Exception setting attribute " + propertyName + " to " + propertyValue);
            throw e;
        }
    }

    /**
     * Returns the type of the attribute specified by the propertyName
     * parameter in the beanClass parameter.
     *
     * @param propertyName The name of the property to analyze
     * @param beanClass The class of object we are analyzing
     * @return The Class of the bean property
     * @throws NoSuchMethodException If the property doesn't exist
     * @throws IllegalAccessException If there is a permission violation
     * @throws InvocationTargetException If the bean throws an Exception
     */
    private static Class getPropertyClass(String propertyName, Class beanClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String methodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        java.lang.reflect.Method method = beanClass.getMethod(methodName, new Class[] {});
        return method.getReturnType();
    }

    /**
     * Gets a property on an object using reflection.
     *
     * @param propertyName The name of the property to fetch
     * @param bean The name of the bean to fetch the property from
     * @return The property
     * @throws NoSuchMethodException If I can't find the getter method for the property
     * @throws IllegalAccessException If there is a permission violation
     * @throws InvocationTargetException If the bean throws an Exception
     */
    private static Object getProperty(String propertyName, Object bean) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String methodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        java.lang.reflect.Method method = bean.getClass().getMethod(methodName, new Class[] {});
        Object value = method.invoke(bean, new Object[] {});
        return value;
    }

    /**
     * Returns a flag indicating whether or not we can convert a
     * String to the type specified.
     *
     * @param type The type in question
     * @return boolean indicating whether we can convert to this type
     */
    private static boolean convertableType(Class type) {
        return (type.equals(String.class) || isConvertableNumber(type) || type.equals(Boolean.class) || type.equals(Boolean.TYPE) || type.equals(Date.class) || type.equals(Class.class) || type.equals(Timestamp.class));
    }

    /**
     * Returns whether or not the type is a number that
     * is convertable to a numeric from String.
     *
     * @param type The type to analyze
     * @return boolean indicating whether this is a convertable number
     */
    private static boolean isConvertableNumber(Class type) {
        return Number.class.isAssignableFrom(type) || type.equals(Integer.TYPE) || type.equals(Short.TYPE) || type.equals(Long.TYPE) || type.equals(Double.TYPE) || type.equals(Float.TYPE) || type.equals(Byte.TYPE);
    }

    /**
     * Converts a String into another type.
     *
     * @param type The type to convert to
     * @param value The String value to convert
     * @param hint A hint to help in converting difficult types such as dates
     * @return The converted object 
     * @throws ParseException If a date format cannot be parsed
     * @throws ClassNotFoundException If trying to create a class object that can't be found
     * @throws NumberFormatException If trying to convert to a number of invalid format
     */
    private static Object convert(Class type, String value, String hint) throws ParseException, ClassNotFoundException, NumberFormatException {
        if (type.equals(String.class)) return value; else if (type.equals(Integer.class) || type.equals(Integer.TYPE)) return Integer.valueOf(value); else if (type.equals(Short.class) || type.equals(Short.TYPE)) return Short.valueOf(value); else if (type.equals(Long.class) || type.equals(Long.TYPE)) return Long.valueOf(value); else if (type.equals(Double.class) || type.equals(Double.TYPE)) return Double.valueOf(value); else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) return new Boolean(value); else if (type.equals(Float.class) || type.equals(Float.TYPE)) return Float.valueOf(value); else if (type.equals(Byte.class) || type.equals(Byte.TYPE)) return Byte.valueOf(value); else if (type.equals(BigDecimal.class)) return new BigDecimal(value); else if (type.equals(BigInteger.class)) return new BigInteger(value); else if (type.equals(Date.class)) return getDate(value, hint); else if (type.equals(Class.class)) return Class.forName(value); else if (type.equals(Timestamp.class)) return new Timestamp(getDate(value, hint).getTime());
        throw new RuntimeException("Conversion to type : " + type.getName() + " is not supported");
    }

    private static String defaultFormat = "MM/dd/yyyy";

    /**
     * Returns a date based on a String value and a 
     * hint which is a date format to use in parsing.
     *
     * @param value The String value to convert
     * @param hint A format to use when parsing (defaults to MM/dd/yyyy
     * @return A converted Date object
     */
    private static Date getDate(String value, String hint) throws ParseException {
        String format = hint != null ? hint : defaultFormat;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(value);
    }

    public static void main(String args[]) {
        try {
            System.out.println("\nFetching single object...");
            Object object = getObject("c:/projects/javaxmlmapper/mapping.xml", "c:/projects/javaxmlmapper/objects.xml");
            System.out.println(object);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
