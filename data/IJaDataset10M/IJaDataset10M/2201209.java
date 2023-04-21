package org.imsglobal.xsd.imsqti_v2p1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for templateCondition.Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="templateCondition.Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.imsglobal.org/xsd/imsqti_v2p1}templateCondition.ContentGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "templateCondition.Type", propOrder = { "templateIf", "templateElseIf", "templateElse" })
public class TemplateConditionType {

    @XmlElement(required = true)
    protected TemplateIfType templateIf;

    protected List<TemplateElseIfType> templateElseIf;

    protected TemplateElseType templateElse;

    /**
     * Gets the value of the templateIf property.
     * 
     * @return
     *     possible object is
     *     {@link TemplateIfType }
     *     
     */
    public TemplateIfType getTemplateIf() {
        return templateIf;
    }

    /**
     * Sets the value of the templateIf property.
     * 
     * @param value
     *     allowed object is
     *     {@link TemplateIfType }
     *     
     */
    public void setTemplateIf(TemplateIfType value) {
        this.templateIf = value;
    }

    /**
     * Gets the value of the templateElseIf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the templateElseIf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTemplateElseIf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TemplateElseIfType }
     * 
     * 
     */
    public List<TemplateElseIfType> getTemplateElseIf() {
        if (templateElseIf == null) {
            templateElseIf = new ArrayList<TemplateElseIfType>();
        }
        return this.templateElseIf;
    }

    /**
     * Gets the value of the templateElse property.
     * 
     * @return
     *     possible object is
     *     {@link TemplateElseType }
     *     
     */
    public TemplateElseType getTemplateElse() {
        return templateElse;
    }

    /**
     * Sets the value of the templateElse property.
     * 
     * @param value
     *     allowed object is
     *     {@link TemplateElseType }
     *     
     */
    public void setTemplateElse(TemplateElseType value) {
        this.templateElse = value;
    }
}
