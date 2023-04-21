package org.jaffa.soa.services.configdomain;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 *         Describes a SOA Event
 *         
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ul xmlns:jxb="http://java.sun.com/xml/ns/jaxb" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;&lt;li&gt;&lt;b&gt;name&lt;/b&gt;: Name of a SOA Event&lt;/li&gt;&lt;li&gt;&lt;b&gt;label&lt;/b&gt;: Label for the SOA Event&lt;/li&gt;&lt;li&gt;&lt;b&gt;description&lt;/b&gt;: Describes the SOA Event&lt;/li&gt;&lt;li&gt;&lt;b&gt;param&lt;/b&gt;: Collection of SOA Event Parameters&lt;/li&gt;&lt;li&gt;&lt;b&gt;inject-domain-fact&lt;/b&gt;: Collection of domain objects to be added as Facts into the Drools context during the processing of a SOA Event&lt;/li&gt;
 *         &lt;/ul&gt;
 * </pre>
 * 
 *       
 * 
 * <p>Java class for soa-event-info complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="soa-event-info">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="param" type="{}param" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="inject-domain-fact" type="{}inject-domain-fact" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "soa-event-info", propOrder = { "param", "injectDomainFact" })
public class SoaEventInfo {

    protected List<Param> param;

    @XmlElement(name = "inject-domain-fact")
    protected List<InjectDomainFact> injectDomainFact;

    @XmlAttribute(required = true)
    protected String name;

    @XmlAttribute
    protected String label;

    @XmlAttribute
    protected String description;

    /**
     * Gets the value of the param property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the param property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Param }
     * 
     * 
     */
    public List<Param> getParam() {
        if (param == null) {
            param = new ArrayList<Param>();
        }
        return this.param;
    }

    /**
     * Gets the value of the injectDomainFact property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the injectDomainFact property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInjectDomainFact().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InjectDomainFact }
     * 
     * 
     */
    public List<InjectDomainFact> getInjectDomainFact() {
        if (injectDomainFact == null) {
            injectDomainFact = new ArrayList<InjectDomainFact>();
        }
        return this.injectDomainFact;
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
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }
}
