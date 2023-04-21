package ietf.params.xml.ns.sppp.base._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for RteRecRefType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RteRecRefType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rrKey" type="{urn:ietf:params:xml:ns:sppp:base:1}ObjKeyType"/>
 *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}unsignedShort"/>
 *         &lt;element name="extRteRecRef" type="{urn:ietf:params:xml:ns:sppp:base:1}ExtAnyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RteRecRefType", propOrder = { "rrKey", "priority", "extRteRecRef" })
public class RteRecRefType {

    @XmlElement(required = true)
    protected ObjKeyType rrKey;

    @XmlSchemaType(name = "unsignedShort")
    protected int priority;

    protected ExtAnyType extRteRecRef;

    /**
     * Gets the value of the rrKey property.
     * 
     * @return
     *     possible object is
     *     {@link ObjKeyType }
     *     
     */
    public ObjKeyType getRrKey() {
        return rrKey;
    }

    /**
     * Sets the value of the rrKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjKeyType }
     *     
     */
    public void setRrKey(ObjKeyType value) {
        this.rrKey = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     */
    public void setPriority(int value) {
        this.priority = value;
    }

    /**
     * Gets the value of the extRteRecRef property.
     * 
     * @return
     *     possible object is
     *     {@link ExtAnyType }
     *     
     */
    public ExtAnyType getExtRteRecRef() {
        return extRteRecRef;
    }

    /**
     * Sets the value of the extRteRecRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtAnyType }
     *     
     */
    public void setExtRteRecRef(ExtAnyType value) {
        this.extRteRecRef = value;
    }
}
