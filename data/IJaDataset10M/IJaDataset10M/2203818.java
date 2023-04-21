package edu.unc.med.lccc.tcgasra.xsdobj.run;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 *                 The SraLinkType mechanism encodes local references between SRA objects.  These 
 *                 references are local to the Home Archive and during archival are resolved to
 *                 exportable references suitable for mirroring between Archives.  SraLinks can be used
 *                 as temporary place holders for pre-published or post-suppressed relationships that
 *                 will not be mirrored between Archives.
 *             
 * 
 * <p>Java class for SraLinkType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SraLinkType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attGroup ref="{}RefNameGroup"/>
 *       &lt;attribute name="sra_object_type">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="STUDY"/>
 *             &lt;enumeration value="SAMPLE"/>
 *             &lt;enumeration value="ANALYSIS"/>
 *             &lt;enumeration value="EXPERIMENT"/>
 *             &lt;enumeration value="RUN"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SraLinkType")
public class SraLinkType {

    @XmlAttribute(name = "sra_object_type")
    protected String sraObjectType;

    @XmlAttribute
    protected String refname;

    @XmlAttribute
    protected String refcenter;

    @XmlAttribute
    protected String accession;

    /**
     * Gets the value of the sraObjectType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSraObjectType() {
        return sraObjectType;
    }

    /**
     * Sets the value of the sraObjectType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSraObjectType(String value) {
        this.sraObjectType = value;
    }

    /**
     * Gets the value of the refname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefname() {
        return refname;
    }

    /**
     * Sets the value of the refname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefname(String value) {
        this.refname = value;
    }

    /**
     * Gets the value of the refcenter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefcenter() {
        return refcenter;
    }

    /**
     * Sets the value of the refcenter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefcenter(String value) {
        this.refcenter = value;
    }

    /**
     * Gets the value of the accession property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccession() {
        return accession;
    }

    /**
     * Sets the value of the accession property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccession(String value) {
        this.accession = value;
    }
}
