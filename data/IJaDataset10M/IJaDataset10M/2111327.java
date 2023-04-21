package eu.europa.tmsearch.services.schemas.trademark.representative;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for TransactionHeaderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransactionHeaderType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SenderDetails">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="RequestProducerDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionHeaderType", propOrder = { "senderDetails" })
public class TransactionHeaderType implements Serializable {

    private static final long serialVersionUID = 12345L;

    @XmlElement(name = "SenderDetails", required = true)
    protected TransactionHeaderType.SenderDetails senderDetails;

    /**
     * Gets the value of the senderDetails property.
     * 
     * @return
     *     possible object is
     *     {@link TransactionHeaderType.SenderDetails }
     *     
     */
    public TransactionHeaderType.SenderDetails getSenderDetails() {
        return senderDetails;
    }

    /**
     * Sets the value of the senderDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionHeaderType.SenderDetails }
     *     
     */
    public void setSenderDetails(TransactionHeaderType.SenderDetails value) {
        this.senderDetails = value;
    }

    public boolean isSetSenderDetails() {
        return (this.senderDetails != null);
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
     *       &lt;sequence>
     *         &lt;element name="RequestProducerDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "requestProducerDateTime" })
    public static class SenderDetails implements Serializable {

        private static final long serialVersionUID = 12345L;

        @XmlElement(name = "RequestProducerDateTime", required = true)
        protected XMLGregorianCalendar requestProducerDateTime;

        /**
         * Gets the value of the requestProducerDateTime property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getRequestProducerDateTime() {
            return requestProducerDateTime;
        }

        /**
         * Sets the value of the requestProducerDateTime property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setRequestProducerDateTime(XMLGregorianCalendar value) {
            this.requestProducerDateTime = value;
        }

        public boolean isSetRequestProducerDateTime() {
            return (this.requestProducerDateTime != null);
        }
    }
}
