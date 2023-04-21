package net.virtualearth.dev.webservices.v1.route;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ArrayOfItineraryItemWarning complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfItineraryItemWarning">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ItineraryItemWarning" type="{http://dev.virtualearth.net/webservices/v1/route}ItineraryItemWarning" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfItineraryItemWarning", propOrder = { "itineraryItemWarning" })
public class ArrayOfItineraryItemWarning implements Serializable {

    private static final long serialVersionUID = 2461660169443089969L;

    @XmlElement(name = "ItineraryItemWarning", nillable = true)
    protected List<ItineraryItemWarning> itineraryItemWarning;

    /**
     * Gets the value of the itineraryItemWarning property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itineraryItemWarning property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItineraryItemWarning().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ItineraryItemWarning }
     * 
     * 
     */
    public List<ItineraryItemWarning> getItineraryItemWarning() {
        if (itineraryItemWarning == null) {
            itineraryItemWarning = new ArrayList<ItineraryItemWarning>();
        }
        return this.itineraryItemWarning;
    }
}
