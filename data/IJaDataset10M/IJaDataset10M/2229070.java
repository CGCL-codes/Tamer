package org.tolven.plugin.registry.xml.bean;

import java.util.ArrayList;
import java.util.List;
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
 *       &lt;sequence>
 *         &lt;element ref="{urn:tolven-org:tpf:1.0}doc" minOccurs="0"/>
 *         &lt;element ref="{urn:tolven-org:tpf:1.0}parameter" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="plugin-id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="point-id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "doc", "parameter" })
@XmlRootElement(name = "extension")
public class Extension {

    protected Doc doc;

    protected List<Parameter> parameter;

    @XmlAttribute(name = "plugin-id", required = true)
    protected String pluginId;

    @XmlAttribute(name = "point-id", required = true)
    protected String pointId;

    @XmlAttribute(required = true)
    protected String id;

    /**
     * Gets the value of the doc property.
     * 
     * @return
     *     possible object is
     *     {@link Doc }
     *     
     */
    public Doc getDoc() {
        return doc;
    }

    /**
     * Sets the value of the doc property.
     * 
     * @param value
     *     allowed object is
     *     {@link Doc }
     *     
     */
    public void setDoc(Doc value) {
        this.doc = value;
    }

    /**
     * Gets the value of the parameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Parameter }
     * 
     * 
     */
    public List<Parameter> getParameter() {
        if (parameter == null) {
            parameter = new ArrayList<Parameter>();
        }
        return this.parameter;
    }

    /**
     * Gets the value of the pluginId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPluginId() {
        return pluginId;
    }

    /**
     * Sets the value of the pluginId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPluginId(String value) {
        this.pluginId = value;
    }

    /**
     * Gets the value of the pointId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPointId() {
        return pointId;
    }

    /**
     * Sets the value of the pointId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPointId(String value) {
        this.pointId = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }
}
