package org.ajcampos.ad.sistra.plugin.firma.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for informacioCertificat complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="informacioCertificat">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="clasificacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaNacimiento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idEmisor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nifCif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nifResponsable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreResponsable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroSerie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="primerApellidoResponsable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="razonSocial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="segundoApellidoResponsable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoCertificado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "informacioCertificat", propOrder = { "clasificacion", "email", "fechaNacimiento", "idEmisor", "nifCif", "nifResponsable", "nombreResponsable", "numeroSerie", "primerApellidoResponsable", "razonSocial", "segundoApellidoResponsable", "subject", "tipoCertificado" })
public class InformacioCertificat {

    protected String clasificacion;

    protected String email;

    protected String fechaNacimiento;

    protected String idEmisor;

    protected String nifCif;

    protected String nifResponsable;

    protected String nombreResponsable;

    protected String numeroSerie;

    protected String primerApellidoResponsable;

    protected String razonSocial;

    protected String segundoApellidoResponsable;

    protected String subject;

    protected String tipoCertificado;

    /**
     * Gets the value of the clasificacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClasificacion() {
        return clasificacion;
    }

    /**
     * Sets the value of the clasificacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClasificacion(String value) {
        this.clasificacion = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the fechaNacimiento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Sets the value of the fechaNacimiento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaNacimiento(String value) {
        this.fechaNacimiento = value;
    }

    /**
     * Gets the value of the idEmisor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdEmisor() {
        return idEmisor;
    }

    /**
     * Sets the value of the idEmisor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdEmisor(String value) {
        this.idEmisor = value;
    }

    /**
     * Gets the value of the nifCif property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNifCif() {
        return nifCif;
    }

    /**
     * Sets the value of the nifCif property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNifCif(String value) {
        this.nifCif = value;
    }

    /**
     * Gets the value of the nifResponsable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNifResponsable() {
        return nifResponsable;
    }

    /**
     * Sets the value of the nifResponsable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNifResponsable(String value) {
        this.nifResponsable = value;
    }

    /**
     * Gets the value of the nombreResponsable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreResponsable() {
        return nombreResponsable;
    }

    /**
     * Sets the value of the nombreResponsable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreResponsable(String value) {
        this.nombreResponsable = value;
    }

    /**
     * Gets the value of the numeroSerie property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroSerie() {
        return numeroSerie;
    }

    /**
     * Sets the value of the numeroSerie property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroSerie(String value) {
        this.numeroSerie = value;
    }

    /**
     * Gets the value of the primerApellidoResponsable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimerApellidoResponsable() {
        return primerApellidoResponsable;
    }

    /**
     * Sets the value of the primerApellidoResponsable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimerApellidoResponsable(String value) {
        this.primerApellidoResponsable = value;
    }

    /**
     * Gets the value of the razonSocial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * Sets the value of the razonSocial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazonSocial(String value) {
        this.razonSocial = value;
    }

    /**
     * Gets the value of the segundoApellidoResponsable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSegundoApellidoResponsable() {
        return segundoApellidoResponsable;
    }

    /**
     * Sets the value of the segundoApellidoResponsable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSegundoApellidoResponsable(String value) {
        this.segundoApellidoResponsable = value;
    }

    /**
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubject(String value) {
        this.subject = value;
    }

    /**
     * Gets the value of the tipoCertificado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoCertificado() {
        return tipoCertificado;
    }

    /**
     * Sets the value of the tipoCertificado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoCertificado(String value) {
        this.tipoCertificado = value;
    }
}
