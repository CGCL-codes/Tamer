package org.slaatsoi.business.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This element contains information about a single report receiver
 * 
 * <p>Java class for ReceiverType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReceiverType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ReportFormat" type="{http://www.slaatsoi.org/BusinessSchema}ReportFormatType"/>
 *         &lt;element name="DeliveryMethod" type="{http://www.slaatsoi.org/BusinessSchema}DeliveryMethodType"/>
 *         &lt;element name="DeliveryEndPoint" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReceiverType", propOrder = { "reportFormat", "deliveryMethod", "deliveryEndPoint" })
public class ReceiverType implements Serializable {

    @XmlElement(name = "ReportFormat", required = true)
    protected ReportFormatType reportFormat;

    @XmlElement(name = "DeliveryMethod", required = true)
    protected DeliveryMethodType deliveryMethod;

    @XmlElement(name = "DeliveryEndPoint", required = true)
    protected String deliveryEndPoint;

    /**
     * Gets the value of the reportFormat property.
     * 
     * @return
     *     possible object is
     *     {@link ReportFormatType }
     *     
     */
    public ReportFormatType getReportFormat() {
        return reportFormat;
    }

    /**
     * Sets the value of the reportFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportFormatType }
     *     
     */
    public void setReportFormat(ReportFormatType value) {
        this.reportFormat = value;
    }

    /**
     * Gets the value of the deliveryMethod property.
     * 
     * @return
     *     possible object is
     *     {@link DeliveryMethodType }
     *     
     */
    public DeliveryMethodType getDeliveryMethod() {
        return deliveryMethod;
    }

    /**
     * Sets the value of the deliveryMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeliveryMethodType }
     *     
     */
    public void setDeliveryMethod(DeliveryMethodType value) {
        this.deliveryMethod = value;
    }

    /**
     * Gets the value of the deliveryEndPoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryEndPoint() {
        return deliveryEndPoint;
    }

    /**
     * Sets the value of the deliveryEndPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryEndPoint(String value) {
        this.deliveryEndPoint = value;
    }
}
