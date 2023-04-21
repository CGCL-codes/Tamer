package org.openrtk.idl.epprtk;

/**
 * Contains the information of the XML element from the request 
 * which cause the server to return an error.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epprtk/epp_ResultValue.java,v 1.1 2004/12/07 15:27:49 ewang2004 Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2004/12/07 15:27:49 $<br>
 * @see org.openrtk.idl.epprtk.epp_Result#setValues(epp_ResultValue[])
 */
public class epp_ResultValue implements org.omg.CORBA.portable.IDLEntity {

    /**
   * The namespace of the XML element.
   * Can be null/empty if the XML is not parsable.
   */
    public String m_namespace = null;

    /**
   * The entire XML string of the offending element.
   * Always set.
   */
    public String m_xml_string = null;

    /**
   * The name of the XML element.
   * Can be null/empty if the XML is not parsable.
   */
    public String m_element_name = null;

    /**
   * The value of the element.
   * Can be null if the XML is unparsable.
   */
    public String m_element_value = null;

    public epp_ResultValue() {
    }

    public epp_ResultValue(String _m_namespace, String _m_xml_string, String _m_element_name, String _m_element_value) {
        m_namespace = _m_namespace;
        m_xml_string = _m_xml_string;
        m_element_name = _m_element_name;
        m_element_value = _m_element_value;
    }

    public void setNamespace(String value) {
        m_namespace = value;
    }

    public String getNamespace() {
        return m_namespace;
    }

    public void setXmlString(String value) {
        m_xml_string = value;
    }

    public String getXmlString() {
        return m_xml_string;
    }

    public void setElementName(String value) {
        m_element_name = value;
    }

    public String getElementName() {
        return m_element_name;
    }

    public void setElementValue(String value) {
        m_element_value = value;
    }

    public String getElementValue() {
        return m_element_value;
    }

    /**
   * Converts this class into a string.
   * Typically used to view the object in debug output.
   * @return The string representation of this object instance
   */
    public String toString() {
        return this.getClass().getName() + ": { m_namespace [" + m_namespace + "] m_xml_string [" + m_xml_string + "] m_element_name [" + m_element_name + "] m_element_value [" + m_element_value + "] }";
    }
}
