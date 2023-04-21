package ietf.params.xml.ns.sppp.base._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for BasicUpdateRqstType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BasicUpdateRqstType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ext" type="{urn:ietf:params:xml:ns:sppp:base:1}ExtAnyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BasicUpdateRqstType", propOrder = { "ext" })
@XmlSeeAlso({ AddRteRecRqstType.class, AddPubIdRqstType.class, AddRteGrpRqstType.class, DelRteGrpOfferRqstType.class, AcceptRteGrpOfferRqstType.class, AddEgrRteRqstType.class, AddDestGrpRqstType.class, DelRteGrpRqstType.class, RejectRteGrpOfferRqstType.class, DelEgrRteRqstType.class, DelPubIdRqstType.class, DelDestGrpRqstType.class, AddRteGrpOfferRqstType.class, DelRteRecRqstType.class })
public abstract class BasicUpdateRqstType {

    protected ExtAnyType ext;

    /**
     * Gets the value of the ext property.
     * 
     * @return
     *     possible object is
     *     {@link ExtAnyType }
     *     
     */
    public ExtAnyType getExt() {
        return ext;
    }

    /**
     * Sets the value of the ext property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtAnyType }
     *     
     */
    public void setExt(ExtAnyType value) {
        this.ext = value;
    }
}
