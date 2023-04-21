package ws_cef;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for getSaldo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getSaldo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idAgencia" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="idConta" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Senha" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSaldo", propOrder = { "idAgencia", "idConta", "senha" })
public class GetSaldo {

    protected int idAgencia;

    protected int idConta;

    @XmlElement(name = "Senha")
    protected String senha;

    /**
     * Gets the value of the idAgencia property.
     * 
     */
    public int getIdAgencia() {
        return idAgencia;
    }

    /**
     * Sets the value of the idAgencia property.
     * 
     */
    public void setIdAgencia(int value) {
        this.idAgencia = value;
    }

    /**
     * Gets the value of the idConta property.
     * 
     */
    public int getIdConta() {
        return idConta;
    }

    /**
     * Sets the value of the idConta property.
     * 
     */
    public void setIdConta(int value) {
        this.idConta = value;
    }

    /**
     * Gets the value of the senha property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Sets the value of the senha property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenha(String value) {
        this.senha = value;
    }
}
