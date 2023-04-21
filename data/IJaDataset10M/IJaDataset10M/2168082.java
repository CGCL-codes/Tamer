package com.google.code.linkedinapi.schema;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}connect-type"/>
 *         &lt;element ref="{}authorization"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
public interface InvitationRequest extends SchemaEntity {

    /**
     * Gets the value of the connectType property.
     * 
     * @return
     *     possible object is
     *     {@link InviteConnectType }
     *     
     */
    InviteConnectType getConnectType();

    /**
     * Sets the value of the connectType property.
     * 
     * @param value
     *     allowed object is
     *     {@link InviteConnectType }
     *     
     */
    void setConnectType(InviteConnectType value);

    /**
     * Gets the value of the authorization property.
     * 
     * @return
     *     possible object is
     *     {@link Authorization }
     *     
     */
    Authorization getAuthorization();

    /**
     * Sets the value of the authorization property.
     * 
     * @param value
     *     allowed object is
     *     {@link Authorization }
     *     
     */
    void setAuthorization(Authorization value);
}
