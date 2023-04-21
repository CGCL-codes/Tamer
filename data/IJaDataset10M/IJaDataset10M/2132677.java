package org.herasaf.xacml.core.context.impl.jibx;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" mixed="true" name="AttributeValueType">
 *   &lt;xs:sequence>
 *     &lt;xs:any minOccurs="0" maxOccurs="unbounded" processContents="lax" namespace="##any"/>
 *   &lt;/xs:sequence>
 *   &lt;xs:anyAttribute processContents="lax" namespace="##any"/>
 * &lt;/xs:complexType>
 * </pre>
 */
public class AttributeValueType implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1128268886821671830L;

    private List<Element> attributeValueTypeList = new ArrayList<Element>();

    /** 
     * Get the list of 'AttributeValueType' complexType items.
     * 
     * @return list
     */
    public List<Element> getAttributeValueTypes() {
        return attributeValueTypeList;
    }

    /** 
     * Set the list of 'AttributeValueType' complexType items.
     * 
     * @param list
     */
    public void setAttributeValueTypes(List<Element> list) {
        attributeValueTypeList = list;
    }
}
