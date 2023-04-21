package xml_utils.schemas.xlstoxmlmappingschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element ref="{http://xml-utils/schemas/XlsToXmlMappingSchema.xsd}CellMapping"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "cellMapping" })
@XmlRootElement(name = "XmlAttributeRendering")
public class XmlAttributeRendering {

    @XmlElement(name = "CellMapping", required = true)
    protected CellMapping cellMapping;

    @XmlAttribute(required = true)
    protected String name;

    /**
     * Gets the value of the cellMapping property.
     * 
     * @return
     *     possible object is
     *     {@link CellMapping }
     *     
     */
    public CellMapping getCellMapping() {
        return cellMapping;
    }

    /**
     * Sets the value of the cellMapping property.
     * 
     * @param value
     *     allowed object is
     *     {@link CellMapping }
     *     
     */
    public void setCellMapping(CellMapping value) {
        this.cellMapping = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }
}
