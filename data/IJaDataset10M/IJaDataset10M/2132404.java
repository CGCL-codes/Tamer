package org.riverock.dbrevision.schema.db.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for DbDataSchema complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DbDataSchema">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Ts" type="{http://dbrevision.sourceforge.net/xsd/dbrevision-structure-3.0.xsd}DbDataTable" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DbDataSchema", propOrder = { "ts" })
public class DbDataSchema {

    @XmlElement(name = "Ts")
    protected List<DbDataTable> ts;

    /**
     * Gets the value of the ts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DbDataTable }
     * 
     * 
     */
    public List<DbDataTable> getTs() {
        if (ts == null) {
            ts = new ArrayList<DbDataTable>();
        }
        return this.ts;
    }
}
