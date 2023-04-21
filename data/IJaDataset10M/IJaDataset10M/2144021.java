package uk.icat3.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for deleteDataSetParameter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deleteDataSetParameter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sessionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="datasetParameterPK" type="{client.icat3.uk}datasetParameterPK" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteDataSetParameter", propOrder = { "sessionId", "datasetParameterPK" })
public class DeleteDataSetParameter {

    protected String sessionId;

    protected DatasetParameterPK datasetParameterPK;

    /**
     * Gets the value of the sessionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Sets the value of the sessionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionId(String value) {
        this.sessionId = value;
    }

    /**
     * Gets the value of the datasetParameterPK property.
     * 
     * @return
     *     possible object is
     *     {@link DatasetParameterPK }
     *     
     */
    public DatasetParameterPK getDatasetParameterPK() {
        return datasetParameterPK;
    }

    /**
     * Sets the value of the datasetParameterPK property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatasetParameterPK }
     *     
     */
    public void setDatasetParameterPK(DatasetParameterPK value) {
        this.datasetParameterPK = value;
    }
}
