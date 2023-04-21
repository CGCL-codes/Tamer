package com.proyecto.bigbang.core.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for Window complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Window">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="bean" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="heigth" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="icon" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="pos-x" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="pos-y" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Window")
public class Window {

    @XmlAttribute
    protected String bean;

    @XmlAttribute
    protected Integer heigth;

    @XmlAttribute
    protected String icon;

    @XmlAttribute
    protected String name;

    @XmlAttribute(name = "pos-x")
    protected Integer posX;

    @XmlAttribute(name = "pos-y")
    protected Integer posY;

    @XmlAttribute
    protected Integer width;

    /**
     * Gets the value of the bean property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBean() {
        return bean;
    }

    /**
     * Sets the value of the bean property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBean(String value) {
        this.bean = value;
    }

    /**
     * Gets the value of the heigth property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHeigth() {
        return heigth;
    }

    /**
     * Sets the value of the heigth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHeigth(Integer value) {
        this.heigth = value;
    }

    /**
     * Gets the value of the icon property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the value of the icon property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIcon(String value) {
        this.icon = value;
    }

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
     * Gets the value of the posX property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPosX() {
        return posX;
    }

    /**
     * Sets the value of the posX property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPosX(Integer value) {
        this.posX = value;
    }

    /**
     * Gets the value of the posY property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPosY() {
        return posY;
    }

    /**
     * Sets the value of the posY property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPosY(Integer value) {
        this.posY = value;
    }

    /**
     * Gets the value of the width property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setWidth(Integer value) {
        this.width = value;
    }
}
