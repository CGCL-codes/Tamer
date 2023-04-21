package com.serena.xmlbridge.adapter.ttwebservice.gen;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for TTItemList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TTItemList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ItemData" type="{urn:ttwebservices}ItemData" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TTItemList", propOrder = { "itemData" })
public class TTItemList {

    @XmlElement(name = "ItemData", namespace = "urn:ttwebservices", required = true)
    protected List<ItemData> itemData;

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
     * {@link ItemData }
     * 
     * 
     */
    public List<ItemData> getItemData() {
        if (itemData == null) {
            itemData = new ArrayList<ItemData>();
        }
        return this.itemData;
    }
}
