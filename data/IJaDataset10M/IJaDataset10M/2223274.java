package com.c2b2.ipoint.presentation.portlets.jsr168.dd;

/**
 * Expriation-cache defines expiration-based caching for this
 * portlet. The parameter indicates
 * the time in seconds after which the portlet output expires.
 * -1 indicates that the output never expires.
 * Used in: portlet
 * 
 * Java content class for expiration-cacheType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Documents%20and%20Settings/Steve/My%20Documents/C2B2%20Consulting%20Ltd/General/Unsorted%20Tech%20Docs/portal/portlet1_0/portlet-app_1_0.xsd line 154)
 * <p>
 * <pre>
 * &lt;complexType name="expiration-cacheType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>int">
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ExpirationCacheType {

    /**
     * Gets the value of the value property.
     * 
     */
    int getValue();

    /**
     * Sets the value of the value property.
     * 
     */
    void setValue(int value);
}
