package com.amazonaws.ec2.doc._2009_04_04;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for BundleInstanceS3StorageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BundleInstanceS3StorageType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bucket" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="prefix" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="awsAccessKeyId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uploadPolicy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uploadPolicySignature" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BundleInstanceS3StorageType", propOrder = { "bucket", "prefix", "awsAccessKeyId", "uploadPolicy", "uploadPolicySignature" })
public class BundleInstanceS3StorageType {

    @XmlElement(required = true)
    protected String bucket;

    @XmlElement(required = true)
    protected String prefix;

    protected String awsAccessKeyId;

    protected String uploadPolicy;

    protected String uploadPolicySignature;

    /**
     * Gets the value of the bucket property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBucket() {
        return bucket;
    }

    /**
     * Sets the value of the bucket property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBucket(String value) {
        this.bucket = value;
    }

    /**
     * Gets the value of the prefix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the value of the prefix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrefix(String value) {
        this.prefix = value;
    }

    /**
     * Gets the value of the awsAccessKeyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAwsAccessKeyId() {
        return awsAccessKeyId;
    }

    /**
     * Sets the value of the awsAccessKeyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAwsAccessKeyId(String value) {
        this.awsAccessKeyId = value;
    }

    /**
     * Gets the value of the uploadPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUploadPolicy() {
        return uploadPolicy;
    }

    /**
     * Sets the value of the uploadPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUploadPolicy(String value) {
        this.uploadPolicy = value;
    }

    /**
     * Gets the value of the uploadPolicySignature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUploadPolicySignature() {
        return uploadPolicySignature;
    }

    /**
     * Sets the value of the uploadPolicySignature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUploadPolicySignature(String value) {
        this.uploadPolicySignature = value;
    }
}
