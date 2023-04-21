package net.sublight.webservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ArrayOfSubtitleActions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfSubtitleActions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SubtitleActions" type="{http://www.sublight.si/}SubtitleActions" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfSubtitleActions", propOrder = { "subtitleActions" })
public class ArrayOfSubtitleActions {

    @XmlElement(name = "SubtitleActions", nillable = true)
    protected List<SubtitleActions> subtitleActions;

    /**
     * Gets the value of the subtitleActions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subtitleActions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubtitleActions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SubtitleActions }
     * 
     * 
     */
    public List<SubtitleActions> getSubtitleActions() {
        if (subtitleActions == null) {
            subtitleActions = new ArrayList<SubtitleActions>();
        }
        return this.subtitleActions;
    }
}
