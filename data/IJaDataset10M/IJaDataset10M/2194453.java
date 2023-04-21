package es.randres.jaxb;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{}maxima"/>
 *         &lt;element ref="{}minima"/>
 *         &lt;element ref="{}dato" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "maxima", "minima", "dato" })
@XmlRootElement(name = "sens_termica")
public class SensTermica {

    protected byte maxima;

    protected byte minima;

    protected List<Dato> dato;

    /**
     * Gets the value of the maxima property.
     * 
     */
    public byte getMaxima() {
        return maxima;
    }

    /**
     * Sets the value of the maxima property.
     * 
     */
    public void setMaxima(byte value) {
        this.maxima = value;
    }

    /**
     * Gets the value of the minima property.
     * 
     */
    public byte getMinima() {
        return minima;
    }

    /**
     * Sets the value of the minima property.
     * 
     */
    public void setMinima(byte value) {
        this.minima = value;
    }

    /**
     * Gets the value of the dato property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dato property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDato().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Dato }
     * 
     * 
     */
    public List<Dato> getDato() {
        if (dato == null) {
            dato = new ArrayList<Dato>();
        }
        return this.dato;
    }
}
