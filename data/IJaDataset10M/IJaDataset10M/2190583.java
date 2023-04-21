package oasis.names.tc.xacml._2_0.policy.schema.os;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ResourceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResourceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}ResourceMatch" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceType", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", propOrder = { "resourceMatch" })
public class ResourceType {

    @XmlElement(name = "ResourceMatch", namespace = "urn:oasis:names:tc:xacml:2.0:policy:schema:os", required = true)
    protected List<ResourceMatchType> resourceMatch;

    /**
     * Gets the value of the resourceMatch property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceMatch property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceMatch().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceMatchType }
     * 
     * 
     */
    public List<ResourceMatchType> getResourceMatch() {
        if (resourceMatch == null) {
            resourceMatch = new ArrayList<ResourceMatchType>();
        }
        return this.resourceMatch;
    }
}
