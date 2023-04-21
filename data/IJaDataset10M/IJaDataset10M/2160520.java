package xades4j.xml.bind.xades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import xades4j.xml.bind.xmldsig.XmlSignatureType;

/**
 * <p>Java class for CounterSignatureType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CounterSignatureType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CounterSignatureType", propOrder = { "signature" })
public class XmlCounterSignatureType {

    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
    protected XmlSignatureType signature;

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link XmlSignatureType }
     *     
     */
    public XmlSignatureType getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlSignatureType }
     *     
     */
    public void setSignature(XmlSignatureType value) {
        this.signature = value;
    }
}
