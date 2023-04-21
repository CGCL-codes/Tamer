package com.amazonaws.s3.doc._2006_03_01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for CreateBucketResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateBucketResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BucketName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateBucketResult", propOrder = { "bucketName" })
public class CreateBucketResult {

    @XmlElement(name = "BucketName", required = true)
    protected String bucketName;

    /**
     * Gets the value of the bucketName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBucketName() {
        return bucketName;
    }

    /**
     * Sets the value of the bucketName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBucketName(String value) {
        this.bucketName = value;
    }
}
