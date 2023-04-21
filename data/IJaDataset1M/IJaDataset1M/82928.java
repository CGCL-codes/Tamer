package net.conselldemallorca.ad.sistra.backoffice.wsdl.bus;

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
 *         &lt;element name="codigoError" type="{urn:es:cim:ws:backoffice:v1:model:Backoffice}TypeCodigoError"/>
 *         &lt;element name="descripcionError" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="documentos" type="{urn:es:cim:ws:backoffice:v1:model:DocumentoConsulta}DocumentosConsulta" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "codigoError", "descripcionError", "documentos" })
@XmlRootElement(name = "realizarConsultaResponse")
public class RealizarConsultaResponse {

    @XmlElement(required = true)
    protected TypeCodigoError codigoError;

    protected String descripcionError;

    protected DocumentosConsulta documentos;

    /**
     * Gets the value of the codigoError property.
     * 
     * @return
     *     possible object is
     *     {@link TypeCodigoError }
     *     
     */
    public TypeCodigoError getCodigoError() {
        return codigoError;
    }

    /**
     * Sets the value of the codigoError property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeCodigoError }
     *     
     */
    public void setCodigoError(TypeCodigoError value) {
        this.codigoError = value;
    }

    /**
     * Gets the value of the descripcionError property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionError() {
        return descripcionError;
    }

    /**
     * Sets the value of the descripcionError property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionError(String value) {
        this.descripcionError = value;
    }

    /**
     * Gets the value of the documentos property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentosConsulta }
     *     
     */
    public DocumentosConsulta getDocumentos() {
        return documentos;
    }

    /**
     * Sets the value of the documentos property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentosConsulta }
     *     
     */
    public void setDocumentos(DocumentosConsulta value) {
        this.documentos = value;
    }
}
