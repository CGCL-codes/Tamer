package es.cim.ESBClient.v1.documentos.cxf.model;

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
 *         &lt;element name="numeroExpediente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipoProcedimiento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="anyo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="referenciaDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "numeroExpediente", "tipoProcedimiento", "anyo", "referenciaDocumento" })
@XmlRootElement(name = "insertarLinkDocumentoExpedienteRequest")
public class InsertarLinkDocumentoExpedienteRequest {

    @XmlElement(required = true)
    protected String numeroExpediente;

    @XmlElement(required = true)
    protected String tipoProcedimiento;

    @XmlElement(required = true)
    protected String anyo;

    @XmlElement(required = true)
    protected String referenciaDocumento;

    /**
     * Gets the value of the numeroExpediente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    /**
     * Sets the value of the numeroExpediente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroExpediente(String value) {
        this.numeroExpediente = value;
    }

    /**
     * Gets the value of the tipoProcedimiento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoProcedimiento() {
        return tipoProcedimiento;
    }

    /**
     * Sets the value of the tipoProcedimiento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoProcedimiento(String value) {
        this.tipoProcedimiento = value;
    }

    /**
     * Gets the value of the anyo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnyo() {
        return anyo;
    }

    /**
     * Sets the value of the anyo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnyo(String value) {
        this.anyo = value;
    }

    /**
     * Gets the value of the referenciaDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenciaDocumento() {
        return referenciaDocumento;
    }

    /**
     * Sets the value of the referenciaDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenciaDocumento(String value) {
        this.referenciaDocumento = value;
    }
}
