package org.authsum.stitches.services;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.authsum.stitches.external.FolderRequest;

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
 *         &lt;element name="in0" type="{http://external.stitches.authsum.org}FolderRequest"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "in0" })
@XmlRootElement(name = "saveFolder")
public class SaveFolder implements Serializable {

    @XmlElement(required = true, nillable = true)
    protected FolderRequest in0;

    /**
     * Gets the value of the in0 property.
     * 
     * @return
     *     possible object is
     *     {@link FolderRequest }
     *     
     */
    public FolderRequest getIn0() {
        return in0;
    }

    /**
     * Sets the value of the in0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link FolderRequest }
     *     
     */
    public void setIn0(FolderRequest value) {
        this.in0 = value;
    }
}
