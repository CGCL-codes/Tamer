package org.ujac.util.xml;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.ujac.util.BeanException;
import org.ujac.util.BeanUtils;
import org.ujac.util.DefaultTypeConverter;
import org.ujac.util.TypeConverter;
import org.ujac.util.TypeConverterException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Name: DynamicObjectSerializer<br>
 * Description: A dynamic configurable object serializer.
 * 
 * @author lauerc
 */
public class DynamicObjectSerializer extends BaseObjectSerializer {

    /** The forced root element definition. */
    private ElementDefinition rootElement = null;

    /** Holds the tag definitions by their names. */
    private Map tagDefinitions = new HashMap();

    /** Holds the tag definitions by their classes. */
    private Map classDefinitions = new HashMap();

    /** The type converter to use. */
    private TypeConverter typeConverter = null;

    /**
   * Constructs a DynamicObjectSerializer instance with no specific attributes.
   */
    public DynamicObjectSerializer() {
        this(null, new DefaultTypeConverter());
    }

    /**
   * Constructs a DynamicObjectSerializer instance with specific attributes.
   * @param encoding The encoding to use.
   */
    public DynamicObjectSerializer(String encoding) {
        this(encoding, new DefaultTypeConverter());
    }

    /**
   * Constructs a DynamicObjectSerializer instance with specific attributes.
   * @param encoding The encoding to use.
   * @param typeConverter The type converter to use.
   */
    public DynamicObjectSerializer(String encoding, TypeConverter typeConverter) {
        super(encoding);
        this.typeConverter = typeConverter;
        registerComplexTypes();
    }

    /**
   * Gets the root element definition.
   * @return The current root element definition.
   */
    public ElementDefinition getRootElement() {
        return rootElement;
    }

    /**
   * Sets the root element definition.
   * @param rootElement The root element definition.
   */
    public void setRootElement(ElementDefinition rootElement) {
        this.rootElement = rootElement;
    }

    /**
   * Registers the base complex types.
   */
    protected void registerComplexTypes() {
        setComplexType(new ComplexTypeDefinition(List.class) {

            public Object createInstance() {
                return new ArrayList();
            }

            protected void analyzeClass() {
            }

            public Iterator elementIterator(Object instance) {
                return ((Collection) instance).iterator();
            }

            public boolean hasBody() {
                return true;
            }
        });
        setComplexType(new ComplexTypeDefinition(Set.class) {

            public Object createInstance() {
                return new HashSet();
            }

            protected void analyzeClass() {
            }

            public Iterator elementIterator(Object instance) {
                return ((Collection) instance).iterator();
            }

            public boolean hasBody() {
                return true;
            }
        });
    }

    /**
   * Sets the element class for the given tag name.
   * @param clazz The element class to define.
   */
    public void setComplexType(ComplexTypeDefinition clazz) {
        tagDefinitions.put(clazz.getName(), clazz);
        classDefinitions.put(clazz.getType(), clazz);
    }

    /**
   * Gets the element class for the given tag name.
   * @param name The tag name to retrieve the element class for. 
   * @return The according element class.
   */
    public ComplexTypeDefinition getComplexType(String name) {
        return (ComplexTypeDefinition) tagDefinitions.get(name);
    }

    /**
   * Gets the element class for the given tag name.
   * @param clazz The object class to retrieve the type definition for. 
   * @return The according element class.
   */
    public ComplexTypeDefinition getComplexType(Class clazz) {
        if (clazz == null) {
            return null;
        }
        ComplexTypeDefinition ct = (ComplexTypeDefinition) classDefinitions.get(clazz);
        if (ct != null) {
            return ct;
        }
        Iterator iterElements = classDefinitions.entrySet().iterator();
        while (iterElements.hasNext()) {
            Map.Entry cdElem = (Map.Entry) iterElements.next();
            Class elemClazz = (Class) cdElem.getKey();
            if (elemClazz.isAssignableFrom(clazz)) {
                return (ComplexTypeDefinition) cdElem.getValue();
            }
        }
        return null;
    }

    /**
   * Writes an object to the given print writer.
   * @param writer The writer to write the WebForm to.
   * @param obj The instance to write.
   * @exception IOException In case an I/O failure occurred.
   */
    public void write(PrintWriter writer, Object obj) throws IOException {
        super.write(writer, obj);
        writeObject(writer, null, obj, 0);
    }

    /**
   * Writes an object to the given print writer.
   * @param writer The writer to write the WebForm to.
   * @param elementDefinition The definition of the element.
   * @param obj The instance to write.
   * @param level The indention level.
   * @exception IOException In case an I/O failure occurred.
   */
    public void writeObject(PrintWriter writer, ElementDefinition elementDefinition, Object obj, int level) throws IOException {
        if (obj == null) {
            return;
        }
        Object[] getterArgs = new Object[0];
        Class clazz = obj.getClass();
        ComplexTypeDefinition typeDef = getComplexType(clazz);
        if (typeDef == null) {
            throw new IOException("Unable to write element '" + elementDefinition.getName() + "' of unregistered class '" + clazz.getName() + "!");
        }
        String tagName = typeDef.getName();
        if ((level == 0) && (rootElement != null)) {
            tagName = rootElement.getName();
        }
        if (elementDefinition != null) {
            tagName = elementDefinition.getName();
        }
        indent(writer, level);
        writer.print("<");
        writer.print(tagName);
        List attributes = typeDef.getAttributes();
        int numAttributes = attributes.size();
        for (int i = 0; i < numAttributes; i++) {
            AttributeDefinition attr = (AttributeDefinition) attributes.get(i);
            try {
                Method getter = attr.getGetter();
                if (getter != null) {
                    String value = typeConverter.formatValue(attr.getType(), getter.invoke(obj, getterArgs));
                    writeAttribute(writer, attr.getName(), value);
                }
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (TypeConverterException ex) {
                ex.printStackTrace();
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
        }
        if (!typeDef.hasBody() && (level > 0)) {
            writer.println("/>");
            return;
        }
        writer.println(">");
        List elementDefinitions = typeDef.getElementDefinitions();
        int numElementDefinitions = elementDefinitions.size();
        for (int i = 0; i < numElementDefinitions; i++) {
            ElementDefinition elemDef = (ElementDefinition) elementDefinitions.get(i);
            try {
                Object elem = elemDef.getGetter().invoke(obj, BeanUtils.GETTER_ARGS);
                if (elem == null) {
                    continue;
                }
                if (elem instanceof List) {
                    indent(writer, level + 1);
                    writeOpenTag(writer, elemDef.getName(), false, true);
                    List elemList = (List) elem;
                    int numElems = elemList.size();
                    for (int j = 0; j < numElems; j++) {
                        writeObject(writer, null, elemList.get(j), level + 2);
                    }
                    indent(writer, level + 1);
                    writeCloseTag(writer, elemDef.getName(), true);
                } else if (elem instanceof Collection) {
                    indent(writer, level + 1);
                    writeOpenTag(writer, elemDef.getName(), false, true);
                    Collection elems = (Collection) elem;
                    Iterator iterElems = elems.iterator();
                    while (iterElems.hasNext()) {
                        writeObject(writer, null, iterElems.next(), level + 2);
                    }
                    indent(writer, level + 1);
                    writeCloseTag(writer, elemDef.getName(), true);
                } else {
                    writeObject(writer, elemDef, elem, level + 1);
                }
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        Iterator elementIterator = typeDef.elementIterator(obj);
        if (elementIterator != null) {
            while (elementIterator.hasNext()) {
                writeObject(writer, null, elementIterator.next(), level + 1);
            }
        }
        indent(writer, level);
        writeCloseTag(writer, tagName, true);
    }

    /**
   * @see org.ujac.util.xml.BaseObjectSerializer#startElement(org.ujac.util.xml.BaseObjectSerializer.StackElement, org.ujac.util.xml.BaseObjectSerializer.StackElement, java.lang.Object)
   */
    public void startElement(StackElement localElement, StackElement parentElement, Object root) throws SAXException {
        String localName = localElement.getName();
        Class localType = null;
        ComplexTypeDefinition tagDef = null;
        if ((localElement.getLevel() == 0) && (rootElement != null) && BeanUtils.equals(rootElement.getName(), localName)) {
            localType = rootElement.getType();
            tagDef = getComplexType(localType);
            localElement.setTypeDefinition(tagDef);
        } else {
            try {
                if ((parentElement != null) && (parentElement.getData() != null)) {
                    localType = BeanUtils.getPropertyType(parentElement.getData().getClass(), localName, true);
                    tagDef = getComplexType(localType);
                    localElement.setTypeDefinition(tagDef);
                }
            } catch (BeanException ex) {
            }
        }
        if (tagDef == null) {
            tagDef = getComplexType(localName);
            localType = tagDef.getType();
            localElement.setTypeDefinition(tagDef);
        }
        Class tagClazz = tagDef.getType();
        if (tagClazz == null) {
            throw new SAXException("Detected unsupported tag name '" + localName + "'");
        }
        Object instance = null;
        try {
            instance = tagDef.createInstance();
        } catch (InstantiationException ex) {
            throw new SAXException("Failed to create tag element '" + localName + "': " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            throw new SAXException("Failed to create tag element '" + localName + "': " + ex.getMessage());
        }
        Attributes attributes = localElement.getAttributes();
        int numAttributes = attributes.getLength();
        for (int i = 0; i < numAttributes; i++) {
            String attrName = attributes.getLocalName(i);
            AttributeDefinition attr = tagDef.getAttribute(attrName);
            if (attr == null) {
                continue;
            }
            Method setter = attr.getSetter();
            try {
                BeanUtils.invokeSetter(instance, setter, attributes.getValue(i));
            } catch (BeanException ex) {
                ex.printStackTrace();
                throw new SAXException("Failed to set attribute '" + attrName + "' for tag '" + localName + "': " + ex.getMessage(), ex);
            }
        }
        localElement.setData(instance);
        if (parentElement != null) {
            String propertyName = attributeName2PropertyName(localName);
            Object parentObject = parentElement.getData();
            ComplexTypeDefinition parentTagDef = parentElement.getTypeDefinition();
            ElementDefinition elem = parentTagDef.getElementDefinition(propertyName);
            if (elem == null) {
                if (parentObject instanceof List) {
                    ((List) parentObject).add(instance);
                    return;
                }
                if (parentObject instanceof Set) {
                    ((Set) parentObject).add(instance);
                    return;
                }
                throw new SAXException("Failed to set unknown element '" + propertyName + "' for tag '" + parentElement.getName() + "'.");
            }
            Method setter = elem.getSetter();
            try {
                BeanUtils.invokeSetter(parentObject, setter, instance);
            } catch (BeanException ex) {
                ex.printStackTrace();
                throw new SAXException("Failed to set element '" + propertyName + "' for tag '" + parentElement.getName() + "': " + ex.getMessage());
            }
        }
    }

    /**
   * @see org.ujac.util.xml.BaseObjectSerializer#endElement(org.ujac.util.xml.BaseObjectSerializer.StackElement, java.lang.String, org.ujac.util.xml.BaseObjectSerializer.StackElement, java.lang.Object)
   */
    public void endElement(StackElement localElement, String content, StackElement parentElement, Object root) throws SAXException {
        String localName = localElement.getName();
        Object localData = localElement.getData();
        ComplexTypeDefinition tagDef = localElement.getTypeDefinition();
        String bodyPropertyName = tagDef.getBodyProperty();
        if (bodyPropertyName != null) {
            try {
                BeanUtils.setProperty(localData, bodyPropertyName, content);
            } catch (BeanException ex) {
                throw new SAXException("Failed to attribute '" + bodyPropertyName + "' for tag '" + localName + "': " + ex.getMessage(), ex);
            }
        }
    }

    /**
   * Converts the given attribute name (form: parta-partb) into a property name (form: partaPartb).
   * @param attributeName The attribute name to convert.
   * @return The resulting property name.
   */
    public String attributeName2PropertyName(String attributeName) {
        StringBuffer propertyNameBuf = new StringBuffer(attributeName.length());
        StringTokenizer tkn = new StringTokenizer(attributeName, "-");
        while (tkn.hasMoreTokens()) {
            String token = tkn.nextToken();
            if (propertyNameBuf.length() == 0) {
                propertyNameBuf.append(token);
                continue;
            }
            propertyNameBuf.append(Character.toUpperCase(token.charAt(0))).append(token.substring(1));
        }
        return propertyNameBuf.toString();
    }

    /**
   * Converts the given property name (form: partaPartb) into a attribute name (form: parta-partb).
   * @param propertyName The property name to convert.
   * @return The resulting attribute name.
   */
    public String propertyName2AttributeName(String propertyName) {
        StringBuffer attributeNameBuf = new StringBuffer(propertyName.length() + 5);
        int propertyNameLen = propertyName.length();
        int lastTokenStart = 0;
        for (int i = 0; i < propertyNameLen; i++) {
            char c = propertyName.charAt(i);
            if ((c >= 'A') && (c <= 'Z')) {
                attributeNameBuf.append(propertyName.substring(lastTokenStart, i)).append('-').append(Character.toLowerCase(c));
                lastTokenStart = i + 1;
            }
        }
        attributeNameBuf.append(propertyName.substring(lastTokenStart));
        return attributeNameBuf.toString();
    }
}
