package org.jcvi.vics.server.jaxb.reference_record;

/**
 * Java content class for anyType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at  line unknown)
 * <p/>
 * <pre>
 * &lt;complexType name="anyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;any/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
public interface AnyType {

    /**
     * Gets the value of the Content property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Content property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link java.lang.String}
     * {@link java.lang.Object}
     */
    java.util.List getContent();
}
