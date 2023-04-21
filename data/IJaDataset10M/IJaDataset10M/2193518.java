package info.fedora.definitions._1._0.types;

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
 *         &lt;element name="disseminator" type="{http://www.fedora.info/definitions/1/0/types/}Disseminator" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "disseminator" })
@XmlRootElement(name = "getDisseminatorHistoryResponse")
public class GetDisseminatorHistoryResponse {

    protected List<Disseminator> disseminator;

    /**
     * Gets the value of the disseminator property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the disseminator property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisseminator().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Disseminator }
     * 
     * 
     */
    public List<Disseminator> getDisseminator() {
        if (disseminator == null) {
            disseminator = new ArrayList<Disseminator>();
        }
        return this.disseminator;
    }
}
