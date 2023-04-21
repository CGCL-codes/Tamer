package org.jaffa.patterns.library.object_viewer_meta_2_0.domain;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for relatedObjectJoinFields complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="relatedObjectJoinFields">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RelatedObjectJoinBetween" type="{}relatedObjectJoinBetween" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relatedObjectJoinFields", propOrder = { "relatedObjectJoinBetween" })
public class RelatedObjectJoinFields {

    @XmlElement(name = "RelatedObjectJoinBetween", required = true)
    protected List<RelatedObjectJoinBetween> relatedObjectJoinBetween;

    /**
     * Gets the value of the relatedObjectJoinBetween property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the relatedObjectJoinBetween property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelatedObjectJoinBetween().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RelatedObjectJoinBetween }
     * 
     * 
     */
    public List<RelatedObjectJoinBetween> getRelatedObjectJoinBetween() {
        if (relatedObjectJoinBetween == null) {
            relatedObjectJoinBetween = new ArrayList<RelatedObjectJoinBetween>();
        }
        return this.relatedObjectJoinBetween;
    }
}
