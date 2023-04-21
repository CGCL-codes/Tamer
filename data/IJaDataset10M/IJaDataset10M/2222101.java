package info.psidev.psi.pi.mzquantml._1_0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * A collection of data relating to the objects within the parent list type (e.g. PeptideConsensus, Protein or ProteinGroup)
 * 
 * <p>Java class for QuantLayerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QuantLayerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DataType" type="{http://psidev.info/psi/pi/mzQuantML/1.0.0-rc2}cvParamRefType"/>
 *         &lt;element name="ColumnIndex" type="{http://www.w3.org/2001/XMLSchema}IDREFS"/>
 *         &lt;element name="DataMatrix" type="{http://psidev.info/psi/pi/mzQuantML/1.0.0-rc2}DataMatrixType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuantLayerType", propOrder = { "dataType", "columnIndex", "dataMatrix" })
public class QuantLayerType {

    @XmlElement(name = "DataType", required = true)
    protected CvParamRefType dataType;

    @XmlList
    @XmlElement(name = "ColumnIndex", required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREFS")
    protected List<Object> columnIndex;

    @XmlElement(name = "DataMatrix", required = true)
    protected DataMatrixType dataMatrix;

    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the dataType property.
     * 
     * @return
     *     possible object is
     *     {@link CvParamRefType }
     *     
     */
    public CvParamRefType getDataType() {
        return dataType;
    }

    /**
     * Sets the value of the dataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CvParamRefType }
     *     
     */
    public void setDataType(CvParamRefType value) {
        this.dataType = value;
    }

    /**
     * Gets the value of the columnIndex property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the columnIndex property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getColumnIndex().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getColumnIndex() {
        if (columnIndex == null) {
            columnIndex = new ArrayList<Object>();
        }
        return this.columnIndex;
    }

    /**
     * Gets the value of the dataMatrix property.
     * 
     * @return
     *     possible object is
     *     {@link DataMatrixType }
     *     
     */
    public DataMatrixType getDataMatrix() {
        return dataMatrix;
    }

    /**
     * Sets the value of the dataMatrix property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataMatrixType }
     *     
     */
    public void setDataMatrix(DataMatrixType value) {
        this.dataMatrix = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }
}
