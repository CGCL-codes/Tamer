package ru.ispu.gemini.dro.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute ref="{}pict"/>
 *       &lt;attribute ref="{}actionId use="required""/>
 *       &lt;attribute ref="{}desc default="""/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "toolbarItem")
public class ToolbarItem {

    @XmlAttribute
    protected String pict;

    @XmlAttribute(required = true)
    protected String actionId;

    @XmlAttribute
    protected String desc;

    /**
     * Gets the value of the pict property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPict() {
        return pict;
    }

    /**
     * Sets the value of the pict property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPict(String value) {
        this.pict = value;
    }

    /**
     * Gets the value of the actionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionId() {
        return actionId;
    }

    /**
     * Sets the value of the actionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionId(String value) {
        this.actionId = value;
    }

    /**
     * Gets the value of the desc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesc() {
        if (desc == null) {
            return "";
        } else {
            return desc;
        }
    }

    /**
     * Sets the value of the desc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesc(String value) {
        this.desc = value;
    }
}
