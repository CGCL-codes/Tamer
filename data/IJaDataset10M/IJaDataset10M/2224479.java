package org.riverock.dbrevision.schema.db.v3;

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
 *         &lt;element name="F" type="{http://dbrevision.sourceforge.net/xsd/dbrevision-structure-3.0.xsd}DbDataFieldData" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DbDataRecord", propOrder = { "f" })
public class DbDataRecord {

    @XmlElement(name = "F", required = true)
    protected List<DbDataFieldData> f;

    /**
     * Gets the value of the f property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the f property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getF().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DbDataFieldData }
     * 
     * 
     */
    public List<DbDataFieldData> getF() {
        if (f == null) {
            f = new ArrayList<DbDataFieldData>();
        }
        return this.f;
    }
}
