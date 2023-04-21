package ru.ispu.gemini.dro.server;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 *         &lt;element ref="{}actions" minOccurs="0"/>
 *         &lt;group ref="{}objectsGroupChoice" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{}id use="required""/>
 *       &lt;attribute ref="{}name use="required""/>
 *       &lt;attribute ref="{}desc default="""/>
 *       &lt;attribute ref="{}templateId"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "actions", "objectsGroupChoice" })
@XmlRootElement(name = "group")
public class Group {

    protected Actions actions;

    @XmlElements({ @XmlElement(name = "set", type = Set.class), @XmlElement(name = "line", type = Line.class), @XmlElement(name = "arc", type = Arc.class), @XmlElement(name = "crosshair", type = Crosshair.class), @XmlElement(name = "polyline", type = Polyline.class), @XmlElement(name = "group", type = Group.class), @XmlElement(name = "sign", type = Sign.class), @XmlElement(name = "spline", type = Spline.class) })
    protected List<Object> objectsGroupChoice;

    @XmlAttribute(required = true)
    protected String id;

    @XmlAttribute(required = true)
    protected String name;

    @XmlAttribute
    protected String desc;

    @XmlAttribute
    protected String templateId;

    /**
     * Gets the value of the actions property.
     * 
     * @return
     *     possible object is
     *     {@link Actions }
     *     
     */
    public Actions getActions() {
        return actions;
    }

    /**
     * Sets the value of the actions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Actions }
     *     
     */
    public void setActions(Actions value) {
        this.actions = value;
    }

    /**
     * Gets the value of the objectsGroupChoice property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the objectsGroupChoice property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObjectsGroupChoice().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Set }
     * {@link Line }
     * {@link Arc }
     * {@link Crosshair }
     * {@link Polyline }
     * {@link Group }
     * {@link Sign }
     * {@link Spline }
     * 
     * 
     */
    public List<Object> getObjectsGroupChoice() {
        if (objectsGroupChoice == null) {
            objectsGroupChoice = new ArrayList<Object>();
        }
        return this.objectsGroupChoice;
    }

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
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the desc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesc() {
        if (desc == null) {
            return "";
        } else {
            return desc;
        }
    }

    /**
     * Sets the value of the desc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesc(String value) {
        this.desc = value;
    }

    /**
     * Gets the value of the templateId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     * Sets the value of the templateId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemplateId(String value) {
        this.templateId = value;
    }
}
