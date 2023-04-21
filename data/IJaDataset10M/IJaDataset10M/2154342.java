package de.campussource.cse.tools.ilias.jaxb.course;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "metaData", "structureObjectOrPageObject", "layout" })
@XmlRootElement(name = "StructureObject")
public class StructureObject {

    @XmlElement(name = "MetaData", required = true)
    protected MetaData metaData;

    @XmlElements({ @XmlElement(name = "StructureObject", required = true, type = StructureObject.class), @XmlElement(name = "PageObject", required = true, type = PageObject.class) })
    protected List<Object> structureObjectOrPageObject;

    @XmlElement(name = "Layout")
    protected Layout layout;

    /**
     * Gets the value of the metaData property.
     * 
     * @return
     *     possible object is
     *     {@link MetaData }
     *     
     */
    public MetaData getMetaData() {
        return metaData;
    }

    /**
     * Sets the value of the metaData property.
     * 
     * @param value
     *     allowed object is
     *     {@link MetaData }
     *     
     */
    public void setMetaData(MetaData value) {
        this.metaData = value;
    }

    /**
     * Gets the value of the structureObjectOrPageObject property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the structureObjectOrPageObject property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStructureObjectOrPageObject().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StructureObject }
     * {@link PageObject }
     * 
     * 
     */
    public List<Object> getStructureObjectOrPageObject() {
        if (structureObjectOrPageObject == null) {
            structureObjectOrPageObject = new ArrayList<Object>();
        }
        return this.structureObjectOrPageObject;
    }

    /**
     * Gets the value of the layout property.
     * 
     * @return
     *     possible object is
     *     {@link Layout }
     *     
     */
    public Layout getLayout() {
        return layout;
    }

    /**
     * Sets the value of the layout property.
     * 
     * @param value
     *     allowed object is
     *     {@link Layout }
     *     
     */
    public void setLayout(Layout value) {
        this.layout = value;
    }
}
