package org.jaffa.components.navigation.domain;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * 
 *     A url-action is used to define a basic url link on the menu. If required it can be tied
 *     into the Jaffa security layer to control certain users from seeing this option
 *    
 * 
 * <p>Java class for url-action complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="url-action">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="url">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="append-final" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="target" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requires-component-access" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="requires-function-access" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "url-action", propOrder = { "url", "target", "requiresComponentAccess", "requiresFunctionAccess" })
public class UrlAction {

    @XmlElement(required = true)
    protected UrlAction.Url url;

    protected String target;

    @XmlElement(name = "requires-component-access")
    protected List<String> requiresComponentAccess;

    @XmlElement(name = "requires-function-access")
    protected List<String> requiresFunctionAccess;

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link UrlAction.Url }
     *     
     */
    public UrlAction.Url getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link UrlAction.Url }
     *     
     */
    public void setUrl(UrlAction.Url value) {
        this.url = value;
    }

    /**
     * Gets the value of the target property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTarget() {
        return target;
    }

    /**
     * Sets the value of the target property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTarget(String value) {
        this.target = value;
    }

    /**
     * Gets the value of the requiresComponentAccess property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the requiresComponentAccess property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRequiresComponentAccess().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getRequiresComponentAccess() {
        if (requiresComponentAccess == null) {
            requiresComponentAccess = new ArrayList<String>();
        }
        return this.requiresComponentAccess;
    }

    /**
     * Gets the value of the requiresFunctionAccess property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the requiresFunctionAccess property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRequiresFunctionAccess().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getRequiresFunctionAccess() {
        if (requiresFunctionAccess == null) {
            requiresFunctionAccess = new ArrayList<String>();
        }
        return this.requiresFunctionAccess;
    }

    /**
     * 
     *        append-final is a boolean (true/false), it defaults to false. If true the system defined
     *        '(?|&)finalUrl=xxx' parameter will be added to the URL specified. This can be useful if you 
     *        are using this as a custom way of invoking a Jaffa Component, and want to leave the framework
     *        to decided what this value should be set to when your custom component exits.
     *       
     * 
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="append-final" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "value" })
    public static class Url {

        @XmlValue
        protected String value;

        @XmlAttribute(name = "append-final")
        protected Boolean appendFinal;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the appendFinal property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public boolean isAppendFinal() {
            if (appendFinal == null) {
                return false;
            } else {
                return appendFinal;
            }
        }

        /**
         * Sets the value of the appendFinal property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setAppendFinal(Boolean value) {
            this.appendFinal = value;
        }
    }
}
