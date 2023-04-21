package org.hyperimage.client.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for getFlexMetadataRecords complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getFlexMetadataRecords">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="baseID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getFlexMetadataRecords", propOrder = { "baseID" })
public class GetFlexMetadataRecords {

    protected long baseID;

    /**
     * Gets the value of the baseID property.
     * 
     */
    public long getBaseID() {
        return baseID;
    }

    /**
     * Sets the value of the baseID property.
     * 
     */
    public void setBaseID(long value) {
        this.baseID = value;
    }
}
