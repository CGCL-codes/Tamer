package uk.ac.ebi.rhea.ws.response.cmlreact;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 *         &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}description" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}atomTypeList"/>
 *           &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}bondTypeList"/>
 *           &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}atomSet"/>
 *           &lt;element ref="{http://www.xml-cml.org/schema/cml2/react}bondSet"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.xml-cml.org/schema/cml2/react}convention"/>
 *       &lt;attGroup ref="{http://www.xml-cml.org/schema/cml2/react}id"/>
 *       &lt;attGroup ref="{http://www.xml-cml.org/schema/cml2/react}dictRef"/>
 *       &lt;attGroup ref="{http://www.xml-cml.org/schema/cml2/react}title"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "description", "atomTypeListOrBondTypeListOrAtomSet" })
@XmlRootElement(name = "reactiveCentre")
public class ReactiveCentre {

    protected Description description;

    @XmlElements({ @XmlElement(name = "bondSet", type = BondSet.class), @XmlElement(name = "atomSet", type = AtomSet.class), @XmlElement(name = "bondTypeList", type = BondTypeList.class), @XmlElement(name = "atomTypeList", type = AtomTypeList.class) })
    protected List<java.lang.Object> atomTypeListOrBondTypeListOrAtomSet;

    @XmlAttribute
    protected String convention;

    @XmlAttribute
    protected String id;

    @XmlAttribute
    protected String dictRef;

    @XmlAttribute
    protected String title;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link Description }
     *     
     */
    public Description getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link Description }
     *     
     */
    public void setDescription(Description value) {
        this.description = value;
    }

    /**
     * Gets the value of the atomTypeListOrBondTypeListOrAtomSet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the atomTypeListOrBondTypeListOrAtomSet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAtomTypeListOrBondTypeListOrAtomSet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BondSet }
     * {@link AtomSet }
     * {@link BondTypeList }
     * {@link AtomTypeList }
     * 
     * 
     */
    public List<java.lang.Object> getAtomTypeListOrBondTypeListOrAtomSet() {
        if (atomTypeListOrBondTypeListOrAtomSet == null) {
            atomTypeListOrBondTypeListOrAtomSet = new ArrayList<java.lang.Object>();
        }
        return this.atomTypeListOrBondTypeListOrAtomSet;
    }

    /**
     * Gets the value of the convention property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConvention() {
        return convention;
    }

    /**
     * Sets the value of the convention property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConvention(String value) {
        this.convention = value;
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

    /**
     * Gets the value of the dictRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDictRef() {
        return dictRef;
    }

    /**
     * Sets the value of the dictRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDictRef(String value) {
        this.dictRef = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }
}
