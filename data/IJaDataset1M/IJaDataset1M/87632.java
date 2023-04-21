package ru.ispu.twins.pure.td;

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
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{}label"/>
 *           &lt;element ref="{}objectButton"/>
 *           &lt;element ref="{}edit"/>
 *         &lt;/choice>
 *         &lt;element ref="{}doneButton"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{}actionId use="required""/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "labelOrObjectButtonOrEdit", "doneButton" })
@XmlRootElement(name = "actionResponce")
public class ActionResponce {

    @XmlElements({ @XmlElement(name = "edit", type = Edit.class), @XmlElement(name = "label", type = Label.class), @XmlElement(name = "objectButton", type = ObjectButton.class) })
    protected List<Object> labelOrObjectButtonOrEdit;

    @XmlElement(required = true)
    protected DoneButton doneButton;

    @XmlAttribute(required = true)
    protected String actionId;

    /**
     * Gets the value of the labelOrObjectButtonOrEdit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the labelOrObjectButtonOrEdit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLabelOrObjectButtonOrEdit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Edit }
     * {@link Label }
     * {@link ObjectButton }
     * 
     * 
     */
    public List<Object> getLabelOrObjectButtonOrEdit() {
        if (labelOrObjectButtonOrEdit == null) {
            labelOrObjectButtonOrEdit = new ArrayList<Object>();
        }
        return this.labelOrObjectButtonOrEdit;
    }

    /**
     * Gets the value of the doneButton property.
     * 
     * @return
     *     possible object is
     *     {@link DoneButton }
     *     
     */
    public DoneButton getDoneButton() {
        return doneButton;
    }

    /**
     * Sets the value of the doneButton property.
     * 
     * @param value
     *     allowed object is
     *     {@link DoneButton }
     *     
     */
    public void setDoneButton(DoneButton value) {
        this.doneButton = value;
    }

    /**
     * Gets the value of the actionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionId() {
        return actionId;
    }

    /**
     * Sets the value of the actionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionId(String value) {
        this.actionId = value;
    }
}
