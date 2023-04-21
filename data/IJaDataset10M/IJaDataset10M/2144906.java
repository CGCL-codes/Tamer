package org.cantaloop.jiomask.form;

import java.lang.reflect.Field;

/**
 * Typesafe enumeration of all possible node types.
 *
 * @created 2002-03-03 19:01:00 CET 
 * @author <a href="mailto:david@cantaloop.org">David Leuschner</a>
 * @version @version@ ($Revision: 1.3 $)
 */
public final class XMLNodeType {

    public static final XMLNodeType ELEMENT = new XMLNodeType("ELEMENT");

    public static final XMLNodeType ATTRIBUTE = new XMLNodeType("ATTRIBUTE");

    public static final XMLNodeType COMMENT = new XMLNodeType("COMMENT");

    public static final XMLNodeType TEXT = new XMLNodeType("TEXT");

    public static final XMLNodeType DOCUMENT = new XMLNodeType("DOCUMENT");

    public static final XMLNodeType PROCESSING_INSTRUCTION = new XMLNodeType("PROCESSING_INSTRUCTION");

    private String m_name;

    private Field m_field;

    private XMLNodeType(String name) {
        m_name = name;
        try {
            m_field = XMLNodeType.class.getField(m_name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException("XMLNodeType name <-> static field name mismatch.");
        }
    }

    public String getTypeName() {
        return m_name;
    }

    public Field getField() {
        return m_field;
    }

    public String toString() {
        return "[XMLNodeType: " + m_name + "]";
    }
}
