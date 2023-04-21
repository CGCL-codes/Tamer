package org.ujac.util.xml;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ujac.util.CollectionUtils;
import org.ujac.util.StringUtils;

/**
 * Name: ComplexTypeDefinition<br>
 * Description: Definition of complex types.
 * 
 * @author lauerc
 */
public class ComplexTypeDefinition {

    /** The serial version UID. */
    private static final long serialVersionUID = 3310404559360886282L;

    /** The attribute name. */
    private String name = null;

    /** The attribute type. */
    private Class type = null;

    /** The property which stores the tags body. */
    private String bodyProperty = null;

    /** The attribute map. */
    private Map attributeMap = new HashMap();

    /** The list of attributes. */
    private List attributes = new ArrayList();

    /** The element map. */
    private Map elementMap = new HashMap();

    /** The list of elements. */
    private List elements = new ArrayList();

    /** The set of suppressed attributes. */
    private Set suppressedAttributes = null;

    /**
   * Constructs a ComplexTypeDefinition instance with specific attributes.
   * @param type The tag class.
   */
    public ComplexTypeDefinition(Class type) {
        this(null, type, null, null);
    }

    /**
   * Constructs a ComplexTypeDefinition instance with specific attributes.
   * @param name The tag name.
   * @param type The tag class.
   */
    public ComplexTypeDefinition(String name, Class type) {
        this(name, type, null, null);
    }

    /**
   * Constructs a ComplexTypeDefinition instance with specific attributes.
   * @param name The tag name.
   * @param type The tag class.
   * @param suppressedAttributes The set up suppressed attributes.
   */
    public ComplexTypeDefinition(String name, Class type, Set suppressedAttributes) {
        this(name, type, null, suppressedAttributes);
    }

    /**
   * Constructs a ComplexTypeDefinition instance with specific attributes.
   * @param name The tag name.
   * @param type The tag class.
   * @param suppressedAttributes The set up suppressed attributes.
   */
    public ComplexTypeDefinition(String name, Class type, String[] suppressedAttributes) {
        this(name, type, null, CollectionUtils.createHashSet(suppressedAttributes));
    }

    /**
   * Constructs a ComplexTypeDefinition instance with specific attributes.
   * @param name The tag name.
   * @param type The tag class.
   * @param bodyProperty The name of the property which represents the tag body.
   */
    public ComplexTypeDefinition(String name, Class type, String bodyProperty) {
        this(name, type, bodyProperty, null);
    }

    /**
   * Constructs a ComplexTypeDefinition instance with specific attributes.
   * @param name The tag name.
   * @param type The tag class.
   * @param bodyProperty The name of the property which represents the tag body.
   * @param suppressedAttributes The set up suppressed attributes.
   */
    public ComplexTypeDefinition(String name, Class type, String bodyProperty, Set suppressedAttributes) {
        this.name = name;
        this.type = type;
        this.bodyProperty = bodyProperty;
        this.suppressedAttributes = suppressedAttributes;
        analyzeClass();
    }

    /**
   * Getter method for the the property name.
   * @return The current value of property name.
   */
    public String getName() {
        return name;
    }

    /**
   * Getter method for the the property type.
   * @return The current value of property type.
   */
    public Class getType() {
        return type;
    }

    /**
   * Create an instance of the definec type
   * @return A new instance of the definec type
   * @exception InstantiationException In case the instantiation failed.
   * @exception IllegalAccessException In case the constructor may not be called.
   */
    public Object createInstance() throws InstantiationException, IllegalAccessException {
        Class clazz = getType();
        if (clazz == null) {
            return null;
        }
        return clazz.newInstance();
    }

    /**
   * Getter method for the the property bodyProperty.
   * @return The current value of property bodyProperty.
   */
    public String getBodyProperty() {
        return bodyProperty;
    }

    /**
   * Setter method for the the property bodyProperty.
   * @param bodyProperty The value to set for the property bodyProperty.
   */
    public void setBodyProperty(String bodyProperty) {
        this.bodyProperty = bodyProperty;
    }

    /**
   * Gets the desired attribute definition.
   * @param name The name of the desired attribute.
   * @return The attribute definition or null.
   */
    public AttributeDefinition getAttribute(String name) {
        return (AttributeDefinition) attributeMap.get(name);
    }

    /**
   * Gets the attribute list.
   * @return The list of current attribute definitions.
   */
    public List getAttributes() {
        return attributes;
    }

    /**
   * Tells whether or not the type has body elements.
   * @return true if the type has nested elements.
   */
    public boolean hasBody() {
        return !elements.isEmpty();
    }

    /**
   * Gets the desired element definition.
   * @param name The name of the desired element.
   * @return The element definition or null.
   */
    public ElementDefinition getElementDefinition(String name) {
        return (ElementDefinition) elementMap.get(name);
    }

    /**
   * Gets the element list.
   * @return The list of current element definitions.
   */
    public List getElementDefinitions() {
        return elements;
    }

    /**
   * Gets the element iterator.
   * @param instance The instance to iterate elements for.
   * @return The list of current element definitions.
   */
    public Iterator elementIterator(Object instance) {
        return null;
    }

    /**
   * Analyzes the class.
   */
    protected void analyzeClass() {
        attributeMap.clear();
        attributes.clear();
        elementMap.clear();
        elements.clear();
        Class clazz = getType();
        Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            String methodName = method.getName();
            String attributeName = getAttributeName(methodName);
            if (attributeName == null) {
                continue;
            }
            if (CollectionUtils.contains(suppressedAttributes, attributeName)) {
                continue;
            }
            AttributeDefinition attr = (AttributeDefinition) attributeMap.get(attributeName);
            ElementDefinition elem = (ElementDefinition) elementMap.get(attributeName);
            if (methodName.startsWith("set")) {
                if (method.getParameterTypes().length != 1) {
                    continue;
                }
                Class attrType = method.getParameterTypes()[0];
                if (isLegalAttributeType(attrType)) {
                    if (attr == null) {
                        attr = new AttributeDefinition(attributeName, attrType);
                        attributeMap.put(attributeName, attr);
                        attributes.add(attr);
                    }
                    attr.setSetter(method);
                } else {
                    if (elem == null) {
                        elem = new ElementDefinition(attributeName, attrType);
                        elementMap.put(attributeName, elem);
                        elements.add(elem);
                    }
                    elem.setSetter(method);
                }
                continue;
            }
            if (method.getParameterTypes().length != 0) {
                continue;
            }
            if ("class".equals(attributeName)) {
                continue;
            }
            Class attrType = method.getReturnType();
            if (isLegalAttributeType(attrType)) {
                if (attr == null) {
                    attr = new AttributeDefinition(attributeName, attrType);
                    attributeMap.put(attributeName, attr);
                    attributes.add(attr);
                }
                attr.setGetter(method);
            } else {
                if (elem == null) {
                    elem = new ElementDefinition(attributeName, attrType);
                    elementMap.put(attributeName, elem);
                    elements.add(elem);
                }
                elem.setGetter(method);
            }
        }
        Collections.sort(attributes);
        Collections.sort(elements);
    }

    /**
   * Tells whether or not the given type is a legal attribute.
   * @param type The type to check.
   * @return true if the type is a legal attribute, else false.
   */
    private boolean isLegalAttributeType(Class type) {
        if (type.isPrimitive()) {
            return true;
        }
        if (Number.class.isAssignableFrom(type)) {
            return true;
        }
        if (type.equals(String.class)) {
            return true;
        }
        return false;
    }

    /**
   * Gets the attribute name out of the given method name.
   * @param methodName The possible getter/setter method name.
   * @return The determined attribute name.
   */
    private String getAttributeName(String methodName) {
        int methodNameLength = StringUtils.getLength(methodName);
        if (methodNameLength < 4) {
            return null;
        }
        if (methodName.startsWith("get") || methodName.startsWith("set")) {
            if (methodNameLength == 4) {
                return methodName.substring(3).toLowerCase();
            }
            return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
        }
        if (methodName.startsWith("is")) {
            if (methodNameLength == 3) {
                return methodName.substring(2).toLowerCase();
            }
            return Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
        }
        return null;
    }
}
