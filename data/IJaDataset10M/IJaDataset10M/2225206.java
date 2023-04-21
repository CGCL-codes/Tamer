package net.brutex.xmlbridge.entities.gen;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ItemDataListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ItemDataListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ItemData" type="{http://www.brutex.net/schemas/xmlbridge/ItemData}ItemDataType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemDataListType", propOrder = { "itemData" })
public class ItemDataListType {

    @XmlElement(name = "ItemData")
    protected List<ItemDataType> itemData;

    /**
     * Gets the value of the itemData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itemData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItemData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ItemDataType }
     * 
     * 
     */
    public List<ItemDataType> getItemData() {
        if (itemData == null) {
            itemData = new ArrayList<ItemDataType>();
        }
        return this.itemData;
    }
}
