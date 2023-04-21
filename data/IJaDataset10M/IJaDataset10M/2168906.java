package org.mcisb.bioinformatics.algorithms.blast.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for tSSSR complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tSSSR">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="iterations" type="{http://www.ebi.ac.uk/schema}tIterations"/>
 *         &lt;element name="hits" type="{http://www.ebi.ac.uk/schema}tHits"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tSSSR", propOrder = { "iterations", "hits" })
public class TSSSR {

    protected TIterations iterations;

    protected THits hits;

    /**
     * Gets the value of the iterations property.
     * 
     * @return
     *     possible object is
     *     {@link TIterations }
     *     
     */
    public TIterations getIterations() {
        return iterations;
    }

    /**
     * Sets the value of the iterations property.
     * 
     * @param value
     *     allowed object is
     *     {@link TIterations }
     *     
     */
    public void setIterations(TIterations value) {
        this.iterations = value;
    }

    /**
     * Gets the value of the hits property.
     * 
     * @return
     *     possible object is
     *     {@link THits }
     *     
     */
    public THits getHits() {
        return hits;
    }

    /**
     * Sets the value of the hits property.
     * 
     * @param value
     *     allowed object is
     *     {@link THits }
     *     
     */
    public void setHits(THits value) {
        this.hits = value;
    }
}
