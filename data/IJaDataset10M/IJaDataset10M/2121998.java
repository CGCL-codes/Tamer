package com.google.earth.kml._2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;

/**
 * <p>
 * Java class for MetadataType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;MetadataType&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;any/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MetadataType", propOrder = { "any" })
@SuppressWarnings("all")
public class MetadataType {

    @XmlAnyElement(lax = true)
    protected Object any;

    /**
   * Gets the value of the any property.
   * 
   * @return possible object is {@link Object } {@link Element }
   * 
   */
    public Object getAny() {
        return any;
    }

    /**
   * Sets the value of the any property.
   * 
   * @param value
   *          allowed object is {@link Object } {@link Element }
   * 
   */
    public void setAny(Object value) {
        this.any = value;
    }
}
