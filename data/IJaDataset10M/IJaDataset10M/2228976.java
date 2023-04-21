package org.wybecom.talk.jtapi.stateserver.client.config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * \brief Java class for xmlstateserverclient complex type.
 * <p>Java class for xmlstateserverclient complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="xmlstateserverclient">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="wsdlurl" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xmlstateserverclient", propOrder = { "wsdlurl", "event" })
public class Xmlstateserverclient {

    @XmlElement(required = true)
    protected String wsdlurl;

    protected List<Xmlevent> event;

    /**
     * Gets the value of the wsdlurl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWsdlurl() {
        return wsdlurl;
    }

    /**
     * Sets the value of the wsdlurl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWsdlurl(String value) {
        this.wsdlurl = value;
    }

    public List<Xmlevent> getEvent() {
        if (event == null) {
            event = new ArrayList<Xmlevent>();
        }
        return this.event;
    }
}
