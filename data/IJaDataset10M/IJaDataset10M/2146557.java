package org.tolven.admin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for InvitationDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InvitationDetail">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:tolven-org:admin:1.0}AdministrativeDetail">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="invitationId" type="{http://www.w3.org/2001/XMLSchema}long" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvitationDetail")
public abstract class InvitationDetail extends AdministrativeDetail {

    @XmlAttribute
    protected Long invitationId;

    /**
     * Gets the value of the invitationId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getInvitationId() {
        return invitationId;
    }

    /**
     * Sets the value of the invitationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setInvitationId(Long value) {
        this.invitationId = value;
    }
}
