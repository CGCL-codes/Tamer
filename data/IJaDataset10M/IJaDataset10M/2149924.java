package ietf.params.xml.ns.sppp.base._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for DelEgrRteRqstType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DelEgrRteRqstType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:ietf:params:xml:ns:sppp:base:1}BasicUpdateRqstType">
 *       &lt;sequence>
 *         &lt;element name="objKey" type="{urn:ietf:params:xml:ns:sppp:base:1}ObjKeyType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DelEgrRteRqstType", propOrder = { "objKey" })
public class DelEgrRteRqstType extends BasicUpdateRqstType {

    @XmlElement(required = true)
    protected ObjKeyType objKey;

    /**
     * Gets the value of the objKey property.
     * 
     * @return
     *     possible object is
     *     {@link ObjKeyType }
     *     
     */
    public ObjKeyType getObjKey() {
        return objKey;
    }

    /**
     * Sets the value of the objKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjKeyType }
     *     
     */
    public void setObjKey(ObjKeyType value) {
        this.objKey = value;
    }
}
