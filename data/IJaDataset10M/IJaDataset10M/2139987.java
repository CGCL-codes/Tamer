package xml_utils.schemas.xlstoxmlmappingschema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://xml-utils/schemas/XlsToXmlMappingSchema.xsd}XmlDirectives"/>
 *         &lt;element ref="{http://xml-utils/schemas/XlsToXmlMappingSchema.xsd}XmlRootElement"/>
 *         &lt;element ref="{http://xml-utils/schemas/XlsToXmlMappingSchema.xsd}RangeMapping" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "xmlDirectives", "xmlRootElement", "rangeMapping" })
@javax.xml.bind.annotation.XmlRootElement(name = "XlsToXmlMapping")
public class XlsToXmlMapping {

    @XmlElement(name = "XmlDirectives", required = true)
    protected XmlDirectives xmlDirectives;

    @XmlElement(name = "XmlRootElement", required = true)
    protected xml_utils.schemas.xlstoxmlmappingschema.XmlRootElement xmlRootElement;

    @XmlElement(name = "RangeMapping", required = true)
    protected List<RangeMapping> rangeMapping;

    /**
     * Gets the value of the xmlDirectives property.
     * 
     * @return
     *     possible object is
     *     {@link XmlDirectives }
     *     
     */
    public XmlDirectives getXmlDirectives() {
        return xmlDirectives;
    }

    /**
     * Sets the value of the xmlDirectives property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlDirectives }
     *     
     */
    public void setXmlDirectives(XmlDirectives value) {
        this.xmlDirectives = value;
    }

    /**
     * Gets the value of the xmlRootElement property.
     * 
     * @return
     *     possible object is
     *     {@link xml_utils.schemas.xlstoxmlmappingschema.XmlRootElement }
     *     
     */
    public xml_utils.schemas.xlstoxmlmappingschema.XmlRootElement getXmlRootElement() {
        return xmlRootElement;
    }

    /**
     * Sets the value of the xmlRootElement property.
     * 
     * @param value
     *     allowed object is
     *     {@link xml_utils.schemas.xlstoxmlmappingschema.XmlRootElement }
     *     
     */
    public void setXmlRootElement(xml_utils.schemas.xlstoxmlmappingschema.XmlRootElement value) {
        this.xmlRootElement = value;
    }

    /**
     * Gets the value of the rangeMapping property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rangeMapping property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRangeMapping().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RangeMapping }
     * 
     * 
     */
    public List<RangeMapping> getRangeMapping() {
        if (rangeMapping == null) {
            rangeMapping = new ArrayList<RangeMapping>();
        }
        return this.rangeMapping;
    }
}
