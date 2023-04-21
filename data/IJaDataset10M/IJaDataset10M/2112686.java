package org.matsim.jaxb.lightsignalsystems10;

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
 *         &lt;element name="lanesToLinkAssignment" type="{http://www.matsim.org/files/dtd}lanesToLinkAssignmentType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="lightSignalSystemDefinition" type="{http://www.matsim.org/files/dtd}lightSignalSystemDefinitionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="lightSignalGroupDefinition" type="{http://www.matsim.org/files/dtd}lightSignalGroupDefinitionType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "lanesToLinkAssignment", "lightSignalSystemDefinition", "lightSignalGroupDefinition" })
@XmlRootElement(name = "lightSignalSystems")
public class XMLLightSignalSystems {

    protected List<XMLLanesToLinkAssignmentType> lanesToLinkAssignment;

    protected List<XMLLightSignalSystemDefinitionType> lightSignalSystemDefinition;

    protected List<XMLLightSignalGroupDefinitionType> lightSignalGroupDefinition;

    /**
     * Gets the value of the lanesToLinkAssignment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lanesToLinkAssignment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLanesToLinkAssignment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLLanesToLinkAssignmentType }
     * 
     * 
     */
    public List<XMLLanesToLinkAssignmentType> getLanesToLinkAssignment() {
        if (lanesToLinkAssignment == null) {
            lanesToLinkAssignment = new ArrayList<XMLLanesToLinkAssignmentType>();
        }
        return this.lanesToLinkAssignment;
    }

    /**
     * Gets the value of the lightSignalSystemDefinition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lightSignalSystemDefinition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLightSignalSystemDefinition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLLightSignalSystemDefinitionType }
     * 
     * 
     */
    public List<XMLLightSignalSystemDefinitionType> getLightSignalSystemDefinition() {
        if (lightSignalSystemDefinition == null) {
            lightSignalSystemDefinition = new ArrayList<XMLLightSignalSystemDefinitionType>();
        }
        return this.lightSignalSystemDefinition;
    }

    /**
     * Gets the value of the lightSignalGroupDefinition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lightSignalGroupDefinition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLightSignalGroupDefinition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLLightSignalGroupDefinitionType }
     * 
     * 
     */
    public List<XMLLightSignalGroupDefinitionType> getLightSignalGroupDefinition() {
        if (lightSignalGroupDefinition == null) {
            lightSignalGroupDefinition = new ArrayList<XMLLightSignalGroupDefinitionType>();
        }
        return this.lightSignalGroupDefinition;
    }
}
