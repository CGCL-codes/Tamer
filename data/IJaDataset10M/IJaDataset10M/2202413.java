package org.openxdm.xcap.client.appusage.presrules.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;

/**
 * <p>Java class for provideServicePermission complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="provideServicePermission">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="all-services">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;choice>
 *             &lt;element ref="{urn:ietf:params:xml:ns:pres-rules}service-uri"/>
 *             &lt;element ref="{urn:ietf:params:xml:ns:pres-rules}service-uri-scheme"/>
 *             &lt;element ref="{urn:ietf:params:xml:ns:pres-rules}occurrence-id"/>
 *             &lt;element ref="{urn:ietf:params:xml:ns:pres-rules}class"/>
 *             &lt;any/>
 *           &lt;/choice>
 *         &lt;/sequence>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "provideServicePermission", namespace = "urn:ietf:params:xml:ns:pres-rules", propOrder = { "allServices", "serviceUriOrServiceUriSchemeOrOccurrenceId" })
@XmlRootElement(name = "provide-services", namespace = "urn:ietf:params:xml:ns:pres-rules")
public class ProvideServicePermission {

    @XmlElement(name = "all-services")
    protected ProvideServicePermission.AllServices allServices;

    @XmlElementRefs({ @XmlElementRef(name = "class", namespace = "urn:ietf:params:xml:ns:pres-rules", type = JAXBElement.class), @XmlElementRef(name = "service-uri-scheme", namespace = "urn:ietf:params:xml:ns:pres-rules", type = JAXBElement.class), @XmlElementRef(name = "service-uri", namespace = "urn:ietf:params:xml:ns:pres-rules", type = JAXBElement.class), @XmlElementRef(name = "occurrence-id", namespace = "urn:ietf:params:xml:ns:pres-rules", type = JAXBElement.class) })
    @XmlAnyElement(lax = true)
    protected List<Object> serviceUriOrServiceUriSchemeOrOccurrenceId;

    /**
     * Gets the value of the allServices property.
     * 
     * @return
     *     possible object is
     *     {@link ProvideServicePermission.AllServices }
     *     
     */
    public ProvideServicePermission.AllServices getAllServices() {
        return allServices;
    }

    /**
     * Sets the value of the allServices property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProvideServicePermission.AllServices }
     *     
     */
    public void setAllServices(ProvideServicePermission.AllServices value) {
        this.allServices = value;
    }

    /**
     * Gets the value of the serviceUriOrServiceUriSchemeOrOccurrenceId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceUriOrServiceUriSchemeOrOccurrenceId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceUriOrServiceUriSchemeOrOccurrenceId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link Element }
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<Object> getServiceUriOrServiceUriSchemeOrOccurrenceId() {
        if (serviceUriOrServiceUriSchemeOrOccurrenceId == null) {
            serviceUriOrServiceUriSchemeOrOccurrenceId = new ArrayList<Object>();
        }
        return this.serviceUriOrServiceUriSchemeOrOccurrenceId;
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class AllServices {
    }
}
