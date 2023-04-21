package org.riverock.dbrevision.annotation.schema.db;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for DbDataRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DbDataRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FieldData" type="{http://dbrevision.sourceforge.net/xsd/dbrevision-structure.xsd}DbDataFieldData" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DbDataRecord", propOrder = { "fieldData" })
public class DbDataRecord {

    @XmlElement(name = "FieldData", required = true)
    protected List<DbDataFieldData> fieldData;

    /**
     * Gets the value of the fieldData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fieldData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFieldData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DbDataFieldData }
     * 
     * 
     */
    public List<DbDataFieldData> getFieldData() {
        if (fieldData == null) {
            fieldData = new ArrayList<DbDataFieldData>();
        }
        return this.fieldData;
    }
}
