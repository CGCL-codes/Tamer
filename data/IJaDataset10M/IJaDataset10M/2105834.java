package de.sepp.aigaebeditormodule.jaxb.gaeb.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * CPV-Nomenklatur
 * 
 * <p>Java class for tgCPVCode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tgCPVCode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CPVNo" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgNormalizedString12"/>
 *         &lt;element name="CPVText" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgNormalizedString100"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tgCPVCode", propOrder = { "cpvNo", "cpvText" })
public class TgCPVCode {

    @XmlElement(name = "CPVNo", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String cpvNo;

    @XmlElement(name = "CPVText", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String cpvText;

    /**
     * Gets the value of the cpvNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCPVNo() {
        return cpvNo;
    }

    /**
     * Sets the value of the cpvNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCPVNo(String value) {
        this.cpvNo = value;
    }

    /**
     * Gets the value of the cpvText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCPVText() {
        return cpvText;
    }

    /**
     * Sets the value of the cpvText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCPVText(String value) {
        this.cpvText = value;
    }
}
