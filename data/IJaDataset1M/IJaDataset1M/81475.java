package org.gridtrust.ppm.impl.policy.bind.jibx;

import org.gridtrust.ppm.impl.policy.bind.jibx.XPathVersion;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:ns="urn:oasis:names:tc:xacml:2.0:policy:schema:os" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="DefaultsType">
 *   &lt;xs:sequence>
 *     &lt;xs:element ref="ns:XPathVersion"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class DefaultsType {

    private XPathVersion defaultsType;

    /** 
     * Get the 'XPathVersion' element value.
     * 
     * @return value
     */
    public XPathVersion getDefaultsType() {
        return defaultsType;
    }

    /** 
     * Set the 'XPathVersion' element value.
     * 
     * @param defaultsType
     */
    public void setDefaultsType(XPathVersion defaultsType) {
        this.defaultsType = defaultsType;
    }
}
