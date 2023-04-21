package org.tolven.plugin.registry.xml.bean;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element ref="{urn:tolven-org:tpf:1.0}parameter-def" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="parent-plugin-id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="parent-point-id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="extension-multiplicity" default="any">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="any"/>
 *             &lt;enumeration value="one"/>
 *             &lt;enumeration value="one-per-plugin"/>
 *             &lt;enumeration value="none"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "doc", "parameterDef" })
@XmlRootElement(name = "extension-point")
public class ExtensionPoint {

    protected Doc doc;

    @XmlElement(name = "parameter-def")
    protected List<ParameterDef> parameterDef;

    @XmlAttribute(required = true)
    protected String id;

    @XmlAttribute(name = "parent-plugin-id")
    protected String parentPluginId;

    @XmlAttribute(name = "parent-point-id")
    protected String parentPointId;

    @XmlAttribute(name = "extension-multiplicity")
    protected String extensionMultiplicity;

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
     * Gets the value of the parameterDef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameterDef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameterDef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParameterDef }
     * 
     * 
     */
    public List<ParameterDef> getParameterDef() {
        if (parameterDef == null) {
            parameterDef = new ArrayList<ParameterDef>();
        }
        return this.parameterDef;
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

    /**
     * Gets the value of the parentPluginId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentPluginId() {
        return parentPluginId;
    }

    /**
     * Sets the value of the parentPluginId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentPluginId(String value) {
        this.parentPluginId = value;
    }

    /**
     * Gets the value of the parentPointId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentPointId() {
        return parentPointId;
    }

    /**
     * Sets the value of the parentPointId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentPointId(String value) {
        this.parentPointId = value;
    }

    /**
     * Gets the value of the extensionMultiplicity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtensionMultiplicity() {
        if (extensionMultiplicity == null) {
            return "any";
        } else {
            return extensionMultiplicity;
        }
    }

    /**
     * Sets the value of the extensionMultiplicity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtensionMultiplicity(String value) {
        this.extensionMultiplicity = value;
    }
}
