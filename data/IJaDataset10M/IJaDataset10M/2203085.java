package xades4j.xml.bind.xades;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>Java class for UnsignedSignaturePropertiesType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="UnsignedSignaturePropertiesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CounterSignature" type="{http://uri.etsi.org/01903/v1.3.2#}CounterSignatureType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SignatureTimeStamp" type="{http://uri.etsi.org/01903/v1.3.2#}XAdESTimeStampType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="CompleteCertificateRefs" type="{http://uri.etsi.org/01903/v1.3.2#}CompleteCertificateRefsType" minOccurs="0"/>
 *         &lt;element name="CompleteRevocationRefs" type="{http://uri.etsi.org/01903/v1.3.2#}CompleteRevocationRefsType" minOccurs="0"/>
 *         &lt;element name="AttributeCertificateRefs" type="{http://uri.etsi.org/01903/v1.3.2#}CompleteCertificateRefsType" minOccurs="0"/>
 *         &lt;element name="AttributeRevocationRefs" type="{http://uri.etsi.org/01903/v1.3.2#}CompleteRevocationRefsType" minOccurs="0"/>
 *         &lt;element name="SigAndRefsTimeStamp" type="{http://uri.etsi.org/01903/v1.3.2#}XAdESTimeStampType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="RefsOnlyTimeStamp" type="{http://uri.etsi.org/01903/v1.3.2#}XAdESTimeStampType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="CertificateValues" type="{http://uri.etsi.org/01903/v1.3.2#}CertificateValuesType" minOccurs="0"/>
 *         &lt;element name="RevocationValues" type="{http://uri.etsi.org/01903/v1.3.2#}RevocationValuesType" minOccurs="0"/>
 *         &lt;element name="AttrAuthoritiesCertValues" type="{http://uri.etsi.org/01903/v1.3.2#}CertificateValuesType" minOccurs="0"/>
 *         &lt;element name="AttributeRevocationValues" type="{http://uri.etsi.org/01903/v1.3.2#}RevocationValuesType" minOccurs="0"/>
 *         &lt;element name="ArchiveTimeStamp" type="{http://uri.etsi.org/01903/v1.3.2#}XAdESTimeStampType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UnsignedSignaturePropertiesType", propOrder = { "counterSignature", "signatureTimeStamp", "completeCertificateRefs", "completeRevocationRefs", "attributeCertificateRefs", "attributeRevocationRefs", "sigAndRefsTimeStamp", "refsOnlyTimeStamp", "certificateValues", "revocationValues", "attrAuthoritiesCertValues", "attributeRevocationValues", "archiveTimeStamp", "any" })
public class XmlUnsignedSignaturePropertiesType {

    @XmlElement(name = "CounterSignature")
    protected List<XmlCounterSignatureType> counterSignature;

    @XmlElement(name = "SignatureTimeStamp")
    protected List<XmlXAdESTimeStampType> signatureTimeStamp;

    @XmlElement(name = "CompleteCertificateRefs")
    protected XmlCompleteCertificateRefsType completeCertificateRefs;

    @XmlElement(name = "CompleteRevocationRefs")
    protected XmlCompleteRevocationRefsType completeRevocationRefs;

    @XmlElement(name = "AttributeCertificateRefs")
    protected XmlCompleteCertificateRefsType attributeCertificateRefs;

    @XmlElement(name = "AttributeRevocationRefs")
    protected XmlCompleteRevocationRefsType attributeRevocationRefs;

    @XmlElement(name = "SigAndRefsTimeStamp")
    protected List<XmlXAdESTimeStampType> sigAndRefsTimeStamp;

    @XmlElement(name = "RefsOnlyTimeStamp")
    protected List<XmlXAdESTimeStampType> refsOnlyTimeStamp;

    @XmlElement(name = "CertificateValues")
    protected XmlCertificateValuesType certificateValues;

    @XmlElement(name = "RevocationValues")
    protected XmlRevocationValuesType revocationValues;

    @XmlElement(name = "AttrAuthoritiesCertValues")
    protected XmlCertificateValuesType attrAuthoritiesCertValues;

    @XmlElement(name = "AttributeRevocationValues")
    protected XmlRevocationValuesType attributeRevocationValues;

    @XmlElement(name = "ArchiveTimeStamp")
    protected List<XmlXAdESTimeStampType> archiveTimeStamp;

    @XmlAnyElement(lax = true)
    protected List<Object> any;

    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the counterSignature property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the counterSignature property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCounterSignature().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlCounterSignatureType }
     *
     *
     */
    public List<XmlCounterSignatureType> getCounterSignature() {
        if (counterSignature == null) counterSignature = new ArrayList<XmlCounterSignatureType>();
        return this.counterSignature;
    }

    /**
     * Gets the value of the signatureTimeStamp property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signatureTimeStamp property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignatureTimeStamp().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlXAdESTimeStampType }
     *
     *
     */
    public List<XmlXAdESTimeStampType> getSignatureTimeStamp() {
        if (signatureTimeStamp == null) signatureTimeStamp = new ArrayList<XmlXAdESTimeStampType>();
        return this.signatureTimeStamp;
    }

    /**
     * Gets the value of the completeCertificateRefs property.
     *
     * @return
     *     possible object is
     *     {@link XmlCompleteCertificateRefsType }
     *
     */
    public XmlCompleteCertificateRefsType getCompleteCertificateRefs() {
        return completeCertificateRefs;
    }

    /**
     * Sets the value of the completeCertificateRefs property.
     *
     * @param value
     *     allowed object is
     *     {@link XmlCompleteCertificateRefsType }
     *
     */
    public void setCompleteCertificateRefs(XmlCompleteCertificateRefsType value) {
        this.completeCertificateRefs = value;
    }

    /**
     * Gets the value of the completeRevocationRefs property.
     *
     * @return
     *     possible object is
     *     {@link XmlCompleteRevocationRefsType }
     *
     */
    public XmlCompleteRevocationRefsType getCompleteRevocationRefs() {
        return completeRevocationRefs;
    }

    /**
     * Sets the value of the completeRevocationRefs property.
     *
     * @param value
     *     allowed object is
     *     {@link XmlCompleteRevocationRefsType }
     *
     */
    public void setCompleteRevocationRefs(XmlCompleteRevocationRefsType value) {
        this.completeRevocationRefs = value;
    }

    /**
     * Gets the value of the attributeCertificateRefs property.
     *
     * @return
     *     possible object is
     *     {@link XmlCompleteCertificateRefsType }
     *
     */
    public XmlCompleteCertificateRefsType getAttributeCertificateRefs() {
        return attributeCertificateRefs;
    }

    /**
     * Sets the value of the attributeCertificateRefs property.
     *
     * @param value
     *     allowed object is
     *     {@link XmlCompleteCertificateRefsType }
     *
     */
    public void setAttributeCertificateRefs(XmlCompleteCertificateRefsType value) {
        this.attributeCertificateRefs = value;
    }

    /**
     * Gets the value of the attributeRevocationRefs property.
     *
     * @return
     *     possible object is
     *     {@link XmlCompleteRevocationRefsType }
     *
     */
    public XmlCompleteRevocationRefsType getAttributeRevocationRefs() {
        return attributeRevocationRefs;
    }

    /**
     * Sets the value of the attributeRevocationRefs property.
     *
     * @param value
     *     allowed object is
     *     {@link XmlCompleteRevocationRefsType }
     *
     */
    public void setAttributeRevocationRefs(XmlCompleteRevocationRefsType value) {
        this.attributeRevocationRefs = value;
    }

    /**
     * Gets the value of the sigAndRefsTimeStamp property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sigAndRefsTimeStamp property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSigAndRefsTimeStamp().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlXAdESTimeStampType }
     *
     *
     */
    public List<XmlXAdESTimeStampType> getSigAndRefsTimeStamp() {
        if (sigAndRefsTimeStamp == null) sigAndRefsTimeStamp = new ArrayList<XmlXAdESTimeStampType>();
        return this.sigAndRefsTimeStamp;
    }

    /**
     * Gets the value of the refsOnlyTimeStamp property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the refsOnlyTimeStamp property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRefsOnlyTimeStamp().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlXAdESTimeStampType }
     *
     *
     */
    public List<XmlXAdESTimeStampType> getRefsOnlyTimeStamp() {
        if (refsOnlyTimeStamp == null) refsOnlyTimeStamp = new ArrayList<XmlXAdESTimeStampType>();
        return this.refsOnlyTimeStamp;
    }

    /**
     * Gets the value of the certificateValues property.
     *
     * @return
     *     possible object is
     *     {@link XmlCertificateValuesType }
     *
     */
    public XmlCertificateValuesType getCertificateValues() {
        return certificateValues;
    }

    /**
     * Sets the value of the certificateValues property.
     *
     * @param value
     *     allowed object is
     *     {@link XmlCertificateValuesType }
     *
     */
    public void setCertificateValues(XmlCertificateValuesType value) {
        this.certificateValues = value;
    }

    /**
     * Gets the value of the revocationValues property.
     *
     * @return
     *     possible object is
     *     {@link XmlRevocationValuesType }
     *
     */
    public XmlRevocationValuesType getRevocationValues() {
        return revocationValues;
    }

    /**
     * Sets the value of the revocationValues property.
     *
     * @param value
     *     allowed object is
     *     {@link XmlRevocationValuesType }
     *
     */
    public void setRevocationValues(XmlRevocationValuesType value) {
        this.revocationValues = value;
    }

    /**
     * Gets the value of the attrAuthoritiesCertValues property.
     *
     * @return
     *     possible object is
     *     {@link XmlCertificateValuesType }
     *
     */
    public XmlCertificateValuesType getAttrAuthoritiesCertValues() {
        return attrAuthoritiesCertValues;
    }

    /**
     * Sets the value of the attrAuthoritiesCertValues property.
     *
     * @param value
     *     allowed object is
     *     {@link XmlCertificateValuesType }
     *
     */
    public void setAttrAuthoritiesCertValues(XmlCertificateValuesType value) {
        this.attrAuthoritiesCertValues = value;
    }

    /**
     * Gets the value of the attributeRevocationValues property.
     *
     * @return
     *     possible object is
     *     {@link XmlRevocationValuesType }
     *
     */
    public XmlRevocationValuesType getAttributeRevocationValues() {
        return attributeRevocationValues;
    }

    /**
     * Sets the value of the attributeRevocationValues property.
     *
     * @param value
     *     allowed object is
     *     {@link XmlRevocationValuesType }
     *
     */
    public void setAttributeRevocationValues(XmlRevocationValuesType value) {
        this.attributeRevocationValues = value;
    }

    /**
     * Gets the value of the archiveTimeStamp property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the archiveTimeStamp property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArchiveTimeStamp().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlXAdESTimeStampType }
     *
     *
     */
    public List<XmlXAdESTimeStampType> getArchiveTimeStamp() {
        if (archiveTimeStamp == null) archiveTimeStamp = new ArrayList<XmlXAdESTimeStampType>();
        return this.archiveTimeStamp;
    }

    /**
     * Gets the value of the any property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     *
     *
     */
    public List<Object> getAny() {
        if (any == null) any = new ArrayList<Object>();
        return this.any;
    }

    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setId(String value) {
        this.id = value;
    }
}
