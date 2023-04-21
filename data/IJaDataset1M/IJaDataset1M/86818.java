package org.authsum.stitches.services;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.authsum.stitches.external.AttributeHolder;

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
 *         &lt;element name="out" type="{http://external.stitches.authsum.org}AttributeHolder"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "out" })
@XmlRootElement(name = "this$2$getAttributeHolderResponse")
public class This$2$GetAttributeHolderResponse implements Serializable {

    @XmlElement(required = true, nillable = true)
    protected AttributeHolder out;

    /**
     * Gets the value of the out property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeHolder }
     *     
     */
    public AttributeHolder getOut() {
        return out;
    }

    /**
     * Sets the value of the out property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeHolder }
     *     
     */
    public void setOut(AttributeHolder value) {
        this.out = value;
    }
}
