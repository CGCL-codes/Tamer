package xml_utils.schemas.xmltoxlsmappingschema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{http://xml-utils/schemas/XmlToXlsMappingSchema.xsd}CellStyle"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "cellStyle" })
@XmlRootElement(name = "ColumnCellStyle")
public class ColumnCellStyle {

    @XmlElement(name = "CellStyle", required = true)
    protected CellStyle cellStyle;

    /**
     * Gets the value of the cellStyle property.
     * 
     * @return
     *     possible object is
     *     {@link CellStyle }
     *     
     */
    public CellStyle getCellStyle() {
        return cellStyle;
    }

    /**
     * Sets the value of the cellStyle property.
     * 
     * @param value
     *     allowed object is
     *     {@link CellStyle }
     *     
     */
    public void setCellStyle(CellStyle value) {
        this.cellStyle = value;
    }
}
