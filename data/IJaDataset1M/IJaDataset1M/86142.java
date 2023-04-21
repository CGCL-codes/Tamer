package org.cantaloop.cgimlet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.cantaloop.tools.logging.Logger;
import org.cantaloop.tools.logging.LoggingManager;
import org.cantaloop.tools.logging.NullLogger;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

/**
 * <p>A PropertyParser reads property-definitions and replaces their
 * occurences in a XML file.</p>
 *
 * <p>A property is defined like this:</p>
 * <code>&lt;property name="basedir" value="."/&gt;</code>
 *
 * <p>This sets the property `basedir' to the value `.'. From this
 * point on the value of the property can be accessed in all
 * attributes and all text-only elements by writing
 * <code>${basedir}</code>. You can customize the prefix character
 * <code>$</code> by invoking {@link setPropertyReferencePrefix}.</p>
 *
 * <p>It is an error to use a property that is not defined or to
 * define a property a second time.</p>
 *
 * <p>A property can also be composed of existing properties.</p>
 * <code>
 *  &lt;property name="dir.src" value="${basedir}/src"&gt;
 * </code><br>
 *
 *
 * <p>You can costomize the <code>PropertyParser</code> by setting the namespace
 * and the name of the element that holds the properties. You can also use different
 * name for the attributes that hold the name and the value of the property. This
 * customization is done in the constructor.</p>
 *
 * @created Mon Jan  7 22:24:04 2002
 *
 * @author <a href="mailto:david@cantaloop.org">David Leuschner</a>
 * @author <a href="mailto:stefan@cantaloop.org">Stefan Heimann</a>
 *
 * @version @version@ ($Revision: 1.13 $) */
public class PropertyParser {

    protected QName m_elemName;

    protected String m_keyAttr, m_valueAttr;

    protected Properties m_props;

    private String m_propertyReferencePrefix = "$";

    /**
   * Shorthand for:<br>
   * <code>PropertyParser(new QName("property", ns))</code>.
   *
   * @param ns the namespace the property-element is declared in.
   */
    public PropertyParser(Namespace ns) {
        this(new QName("property", ns));
    }

    /**
   * Shorthand for:<br>
   * <code>PropertyParser(elemName, "name", "value")</code>.
   *
   * @param elemName the name of the element that defines the properties.
   */
    public PropertyParser(QName elemName) {
        this(elemName, "name", "value");
    }

    /**
   * Creates a new customized <code>PropertyParser</code> instance.
   *
   * @param elemName the <code>QName</code> of a element that
   * holds a property
   * @param keyAttr the name of the attribute of the element that
   * holds the key of the property
   * @param valueAttr the name of the attribute of the element that
   * holds the value of the property
   */
    public PropertyParser(QName elemName, String keyAttr, String valueAttr) {
        this.m_elemName = elemName;
        this.m_keyAttr = keyAttr;
        this.m_valueAttr = valueAttr;
        m_props = new Properties();
    }

    /**
   * Returns the character that is used to start a property reference.
   * The prefix is the character before the ``{''.
   * 
   * <p>The default value is <code>$</code>.</p>
   */
    public char getPropertyReferencePrefix() {
        return m_propertyReferencePrefix.charAt(0);
    }

    /**
   * Sets the character that is used to start a property reference.
   * The prefix is the character before the ``{''.
   * 
   * <p>The default value is <code>$</code>.</p>
   */
    public void setPropertyReferencePrefix(char propertyReferencePrefix) {
        m_propertyReferencePrefix = String.valueOf(propertyReferencePrefix);
    }

    /**
   * Returns the properties the parser has read.
   *
   * @return a <code>Properties</code> value
   */
    public Properties getProperties() {
        return m_props;
    }

    /**
   * Shorthand for:<br>
   * <code>
   * initProperties(root);<br>
   * replaceProperties(root);<br>
   * getProperties();</code>
   *
   * @param root an <code>Element</code> value
   * @return a <code>Properties</code> value
   */
    public Properties apply(Element root) {
        initProperties(root);
        replaceProperties(root);
        return getProperties();
    }

    /**
   * <p>Read the properties from the elements that are direct children
   * of <code>root</code>. If a property element has not the
   * attributes that were given in the constructor of the
   * <code>PropertyParser</code>, a <code>CodeGenerationException</code>
   * is thrown.
   *
   * <p>You can get the properties that were read by calling
   * {@link #getProperties}.</p>
   *
   * @param root an <code>Element</code> value
   */
    public void initProperties(Element root) throws CodeGenerationException {
        if (root == null) {
            return;
        }
        String key, value;
        Element e;
        Iterator it = root.elementIterator(m_elemName);
        while (it.hasNext()) {
            e = (Element) it.next();
            key = e.attributeValue(m_keyAttr);
            value = e.attributeValue(m_valueAttr);
            if (key == null || "".equals(key) || value == null) {
                throw new CodeGenerationException(e.asXML() + " is not a valid property declaration");
            } else if (m_props.containsKey(key)) {
                throw new CodeGenerationException(e.asXML() + " tries to set a property that " + "already exists.");
            } else {
                m_props.put(key, parsePropertyString(value));
                if (m_logger.isInfoEnabled()) {
                    m_logger.info("registered property '" + key + "' with value '" + value + "'.");
                }
            }
        }
    }

    /**
   * Replace all properties in the xml tree starting with element
   * <code>root</code>. The value of all attributes and the textual
   * content of all text-only elements is replaced.
   *
   * @param root an <code>Element</code> value
   * @throws CodeGenerationException if the name of a property does not exist.
   */
    public void replaceProperties(Element root) throws CodeGenerationException {
        if (root == null) {
            return;
        }
        Iterator attrIt = root.attributeIterator();
        Attribute a;
        String val;
        while (attrIt.hasNext()) {
            a = (Attribute) attrIt.next();
            val = a.getValue();
            a.setValue(parsePropertyString(val));
        }
        if (root.isTextOnly()) {
            val = root.getTextTrim();
            root.setText(parsePropertyString(val));
        } else {
            Iterator elemIt = root.elementIterator();
            while (elemIt.hasNext()) {
                replaceProperties((Element) elemIt.next());
            }
        }
    }

    /**
   * Parse <code>value</code> and resolve all properties in the given
   * string.<br>
   *
   * Throws a <code>CodeGenerationException</code> if a property
   * cannot be resolved.
   *
   * @param value a <code>String</code> value
   * @return the parsed property
   */
    public String parsePropertyString(String value) {
        if (value == null) {
            return null;
        }
        List fragments = new LinkedList();
        List propertyRefs = new LinkedList();
        parsePropertyString(value, fragments, propertyRefs);
        StringBuffer sb = new StringBuffer();
        Iterator i = fragments.iterator();
        Iterator j = propertyRefs.iterator();
        while (i.hasNext()) {
            String fragment = (String) i.next();
            if (fragment == null) {
                String propertyName = (String) j.next();
                if (!m_props.containsKey(propertyName)) {
                    throw new CodeGenerationException("Property ${" + propertyName + "} is used before it has been set.");
                }
                fragment = (String) m_props.get(propertyName);
            }
            sb.append(fragment);
        }
        return sb.toString();
    }

    /**
   * Taken from ant (www.jakarta.apache.org/ant)
   *
   * This method will parse a string containing ${value} style 
   * property values into two lists. The first list is a collection
   * of text fragments, while the other is a set of string property names.
   * null entries in the first list indicate a property reference from the
   * second list.
   */
    private void parsePropertyString(String value, List fragments, List propertyRefs) throws CodeGenerationException {
        int prev = 0;
        int pos;
        while ((pos = value.indexOf(m_propertyReferencePrefix, prev)) >= 0) {
            if (pos > 0) {
                fragments.add(value.substring(prev, pos));
            }
            if (pos == (value.length() - 1)) {
                fragments.add(m_propertyReferencePrefix);
                prev = pos + 1;
            } else if (value.charAt(pos + 1) != '{') {
                fragments.add(value.substring(pos + 1, pos + 2));
                prev = pos + 2;
            } else {
                int endName = value.indexOf('}', pos);
                if (endName < 0) {
                    throw new CodeGenerationException("Syntax error in property: " + value);
                }
                String propertyName = value.substring(pos + 2, endName);
                fragments.add(null);
                propertyRefs.add(propertyName);
                prev = endName + 1;
            }
        }
        if (prev < value.length()) {
            fragments.add(value.substring(prev));
        }
    }

    private Logger m_logger = NullLogger.getInstance();

    public void setupLogging(String topic) {
        m_logger = LoggingManager.getInstance().getLogger(topic, getClass());
    }
}
