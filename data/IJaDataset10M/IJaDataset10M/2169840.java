package uk.ac.ebi.rhea.ws.response.search;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ResultSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResultSet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rheaReaction" type="{http://www.ebi.ac.uk/rhea/ws}RheaReaction" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="numberofrecordsmatched" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="numberofrecordsreturned" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="resulttype" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultSet", propOrder = { "rheaReaction" })
public class ResultSet {

    protected List<RheaReaction> rheaReaction;

    @XmlAttribute
    protected Integer numberofrecordsmatched;

    @XmlAttribute
    protected Integer numberofrecordsreturned;

    @XmlAttribute
    protected String resulttype;

    /**
     * Gets the value of the rheaReaction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rheaReaction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRheaReaction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RheaReaction }
     * 
     * 
     */
    public List<RheaReaction> getRheaReaction() {
        if (rheaReaction == null) {
            rheaReaction = new ArrayList<RheaReaction>();
        }
        return this.rheaReaction;
    }

    /**
     * Gets the value of the numberofrecordsmatched property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberofrecordsmatched() {
        return numberofrecordsmatched;
    }

    /**
     * Sets the value of the numberofrecordsmatched property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberofrecordsmatched(Integer value) {
        this.numberofrecordsmatched = value;
    }

    /**
     * Gets the value of the numberofrecordsreturned property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberofrecordsreturned() {
        return numberofrecordsreturned;
    }

    /**
     * Sets the value of the numberofrecordsreturned property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberofrecordsreturned(Integer value) {
        this.numberofrecordsreturned = value;
    }

    /**
     * Gets the value of the resulttype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResulttype() {
        return resulttype;
    }

    /**
     * Sets the value of the resulttype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResulttype(String value) {
        this.resulttype = value;
    }
}
