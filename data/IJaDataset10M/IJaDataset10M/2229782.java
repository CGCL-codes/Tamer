package org.hyperimage.client.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for adminRemoveSetFromTemplate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="adminRemoveSetFromTemplate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="templateID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="setID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "adminRemoveSetFromTemplate", propOrder = { "templateID", "setID" })
public class AdminRemoveSetFromTemplate {

    protected long templateID;

    protected long setID;

    /**
     * Gets the value of the templateID property.
     * 
     */
    public long getTemplateID() {
        return templateID;
    }

    /**
     * Sets the value of the templateID property.
     * 
     */
    public void setTemplateID(long value) {
        this.templateID = value;
    }

    /**
     * Gets the value of the setID property.
     * 
     */
    public long getSetID() {
        return setID;
    }

    /**
     * Sets the value of the setID property.
     * 
     */
    public void setSetID(long value) {
        this.setID = value;
    }
}
