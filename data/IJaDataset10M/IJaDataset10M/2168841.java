package localhost;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="cuentaOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bancoOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cuentaDestino" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bancoDestino" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="claveCuentaOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="monto" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="correoUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "cuentaOrigen", "bancoOrigen", "cuentaDestino", "bancoDestino", "claveCuentaOrigen", "monto", "correoUsuario" })
@XmlRootElement(name = "realizarPagoSuCertificado")
public class RealizarPagoSuCertificado {

    protected String cuentaOrigen;

    protected String bancoOrigen;

    protected String cuentaDestino;

    protected String bancoDestino;

    protected String claveCuentaOrigen;

    protected int monto;

    protected String correoUsuario;

    /**
     * Gets the value of the cuentaOrigen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuentaOrigen() {
        return cuentaOrigen;
    }

    /**
     * Sets the value of the cuentaOrigen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuentaOrigen(String value) {
        this.cuentaOrigen = value;
    }

    /**
     * Gets the value of the bancoOrigen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBancoOrigen() {
        return bancoOrigen;
    }

    /**
     * Sets the value of the bancoOrigen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBancoOrigen(String value) {
        this.bancoOrigen = value;
    }

    /**
     * Gets the value of the cuentaDestino property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuentaDestino() {
        return cuentaDestino;
    }

    /**
     * Sets the value of the cuentaDestino property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuentaDestino(String value) {
        this.cuentaDestino = value;
    }

    /**
     * Gets the value of the bancoDestino property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBancoDestino() {
        return bancoDestino;
    }

    /**
     * Sets the value of the bancoDestino property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBancoDestino(String value) {
        this.bancoDestino = value;
    }

    /**
     * Gets the value of the claveCuentaOrigen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaveCuentaOrigen() {
        return claveCuentaOrigen;
    }

    /**
     * Sets the value of the claveCuentaOrigen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaveCuentaOrigen(String value) {
        this.claveCuentaOrigen = value;
    }

    /**
     * Gets the value of the monto property.
     * 
     */
    public int getMonto() {
        return monto;
    }

    /**
     * Sets the value of the monto property.
     * 
     */
    public void setMonto(int value) {
        this.monto = value;
    }

    /**
     * Gets the value of the correoUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorreoUsuario() {
        return correoUsuario;
    }

    /**
     * Sets the value of the correoUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorreoUsuario(String value) {
        this.correoUsuario = value;
    }
}
