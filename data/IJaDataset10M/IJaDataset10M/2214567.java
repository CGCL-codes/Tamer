package org.jaffa.patterns.library.object_maintenance_meta_1_0.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for field complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="field">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DataType" type="{}dataTypes"/>
 *         &lt;element name="Display" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Mandatory" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Layout" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Label" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Width" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DomainField" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InitialValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BreakUp" type="{}breakup" minOccurs="0"/>
 *         &lt;element name="FunctionGuardName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "field", propOrder = { "name", "dataType", "display", "mandatory", "layout", "label", "width", "domainField", "initialValue", "breakUp", "functionGuardName" })
public class Field {

    @XmlElement(name = "Name", required = true)
    protected String name;

    @XmlElement(name = "DataType", required = true)
    protected String dataType;

    @XmlElement(name = "Display")
    protected boolean display;

    @XmlElement(name = "Mandatory")
    protected boolean mandatory;

    @XmlElement(name = "Layout")
    protected String layout;

    @XmlElement(name = "Label", required = true)
    protected String label;

    @XmlElement(name = "Width", required = true)
    protected String width;

    @XmlElement(name = "DomainField", required = true)
    protected String domainField;

    @XmlElement(name = "InitialValue")
    protected String initialValue;

    @XmlElement(name = "BreakUp")
    protected Breakup breakUp;

    @XmlElement(name = "FunctionGuardName")
    protected String functionGuardName;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the dataType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Sets the value of the dataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataType(String value) {
        this.dataType = value;
    }

    /**
     * Gets the value of the display property.
     * 
     */
    public boolean isDisplay() {
        return display;
    }

    /**
     * Sets the value of the display property.
     * 
     */
    public void setDisplay(boolean value) {
        this.display = value;
    }

    /**
     * Gets the value of the mandatory property.
     * 
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * Sets the value of the mandatory property.
     * 
     */
    public void setMandatory(boolean value) {
        this.mandatory = value;
    }

    /**
     * Gets the value of the layout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLayout() {
        return layout;
    }

    /**
     * Sets the value of the layout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLayout(String value) {
        this.layout = value;
    }

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the width property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWidth(String value) {
        this.width = value;
    }

    /**
     * Gets the value of the domainField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainField() {
        return domainField;
    }

    /**
     * Sets the value of the domainField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainField(String value) {
        this.domainField = value;
    }

    /**
     * Gets the value of the initialValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitialValue() {
        return initialValue;
    }

    /**
     * Sets the value of the initialValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitialValue(String value) {
        this.initialValue = value;
    }

    /**
     * Gets the value of the breakUp property.
     * 
     * @return
     *     possible object is
     *     {@link Breakup }
     *     
     */
    public Breakup getBreakUp() {
        return breakUp;
    }

    /**
     * Sets the value of the breakUp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Breakup }
     *     
     */
    public void setBreakUp(Breakup value) {
        this.breakUp = value;
    }

    /**
     * Gets the value of the functionGuardName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFunctionGuardName() {
        return functionGuardName;
    }

    /**
     * Sets the value of the functionGuardName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFunctionGuardName(String value) {
        this.functionGuardName = value;
    }
}
