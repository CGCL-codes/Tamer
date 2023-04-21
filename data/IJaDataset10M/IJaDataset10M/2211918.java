package org.mobicents.slee.container.component.deployment.jaxb.slee11.profile;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "compareOrRangeMatchOrLongestPrefixMatchOrHasPrefixOrAndOrOrOrNot" })
@XmlRootElement(name = "and")
public class And {

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;

    @XmlElements({ @XmlElement(name = "compare", type = Compare.class), @XmlElement(name = "range-match", type = RangeMatch.class), @XmlElement(name = "longest-prefix-match", type = LongestPrefixMatch.class), @XmlElement(name = "has-prefix", type = HasPrefix.class), @XmlElement(name = "and", type = And.class), @XmlElement(name = "or", type = Or.class), @XmlElement(name = "not", type = Not.class) })
    protected List<Object> compareOrRangeMatchOrLongestPrefixMatchOrHasPrefixOrAndOrOrOrNot;

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
     * Gets the value of the compareOrRangeMatchOrLongestPrefixMatchOrHasPrefixOrAndOrOrOrNot property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the compareOrRangeMatchOrLongestPrefixMatchOrHasPrefixOrAndOrOrOrNot property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCompareOrRangeMatchOrLongestPrefixMatchOrHasPrefixOrAndOrOrOrNot().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Compare }
     * {@link RangeMatch }
     * {@link LongestPrefixMatch }
     * {@link HasPrefix }
     * {@link And }
     * {@link Or }
     * {@link Not }
     * 
     * 
     */
    public List<Object> getCompareOrRangeMatchOrLongestPrefixMatchOrHasPrefixOrAndOrOrOrNot() {
        if (compareOrRangeMatchOrLongestPrefixMatchOrHasPrefixOrAndOrOrOrNot == null) {
            compareOrRangeMatchOrLongestPrefixMatchOrHasPrefixOrAndOrOrOrNot = new ArrayList<Object>();
        }
        return this.compareOrRangeMatchOrLongestPrefixMatchOrHasPrefixOrAndOrOrOrNot;
    }
}
