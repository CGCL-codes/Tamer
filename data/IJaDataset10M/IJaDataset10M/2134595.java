package org.herasaf.xacml.core.context.impl.jibx;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="StatusDetailType">
 *   &lt;xs:sequence>
 *     &lt;xs:any minOccurs="0" maxOccurs="unbounded" processContents="lax" namespace="##any"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class StatusDetailType implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4279374548373833859L;

    private List<Element> statusDetailTypeList = new ArrayList<Element>();

    /** 
     * Get the list of 'StatusDetailType' complexType items.
     * 
     * @return list
     */
    public List<Element> getStatusDetailTypes() {
        return statusDetailTypeList;
    }

    /** 
     * Set the list of 'StatusDetailType' complexType items.
     * 
     * @param list
     */
    public void setStatusDetailTypes(List<Element> list) {
        statusDetailTypeList = list;
    }
}
