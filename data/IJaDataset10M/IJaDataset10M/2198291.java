package net.sf.istcontract.aws.input.contract;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for Plex-resl.type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Plex-resl.type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.ruleml.org/0.91/xsd}Plex-resl.content"/>
 *       &lt;attGroup ref="{http://www.ruleml.org/0.91/xsd}Plex.attlist"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Plex-resl.type", propOrder = { "slotOrResl" })
public class PlexReslType {

    @XmlElements({ @XmlElement(name = "slot", namespace = "http://www.ruleml.org/0.91/xsd", required = true, type = SlotType.class), @XmlElement(name = "resl", namespace = "http://www.ruleml.org/0.91/xsd", required = true, type = ReslType.class) })
    protected List<Object> slotOrResl;

    /**
     * Gets the value of the slotOrResl property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the slotOrResl property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSlotOrResl().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SlotType }
     * {@link ReslType }
     * 
     * 
     */
    public List<Object> getSlotOrResl() {
        if (slotOrResl == null) {
            slotOrResl = new ArrayList<Object>();
        }
        return this.slotOrResl;
    }
}
