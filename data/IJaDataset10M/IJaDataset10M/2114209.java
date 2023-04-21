package org.jaffa.patterns.library.object_viewer_meta_2_0.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for source complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="source">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Object" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Package" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="JoinFields" type="{}joinFields"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "source", propOrder = { "object", "_package", "joinFields" })
public class Source {

    @XmlElement(name = "Object", required = true)
    protected String object;

    @XmlElement(name = "Package", required = true)
    protected String _package;

    @XmlElement(name = "JoinFields", required = true)
    protected JoinFields joinFields;

    /**
     * Gets the value of the object property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObject() {
        return object;
    }

    /**
     * Sets the value of the object property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObject(String value) {
        this.object = value;
    }

    /**
     * Gets the value of the package property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackage() {
        return _package;
    }

    /**
     * Sets the value of the package property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackage(String value) {
        this._package = value;
    }

    /**
     * Gets the value of the joinFields property.
     * 
     * @return
     *     possible object is
     *     {@link JoinFields }
     *     
     */
    public JoinFields getJoinFields() {
        return joinFields;
    }

    /**
     * Sets the value of the joinFields property.
     * 
     * @param value
     *     allowed object is
     *     {@link JoinFields }
     *     
     */
    public void setJoinFields(JoinFields value) {
        this.joinFields = value;
    }
}
