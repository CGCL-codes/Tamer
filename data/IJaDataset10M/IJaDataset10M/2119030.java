package org.riverock.dbrevision.annotation.schema.db;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for DbSchema complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DbSchema">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Table" type="{http://dbrevision.sourceforge.net/xsd/dbrevision-structure.xsd}DbTable" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="View" type="{http://dbrevision.sourceforge.net/xsd/dbrevision-structure.xsd}DbView" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ViewReplacement" type="{http://dbrevision.sourceforge.net/xsd/dbrevision-structure.xsd}DbViewReplacement" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Sequence" type="{http://dbrevision.sourceforge.net/xsd/dbrevision-structure.xsd}DbSequence" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SequenceReplacement" type="{http://dbrevision.sourceforge.net/xsd/dbrevision-structure.xsd}DbSequenceReplacement" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DbSchema", propOrder = { "tables", "views", "viewReplacement", "sequences", "sequenceReplacement" })
public class DbSchema {

    @XmlElement(name = "Table")
    protected List<DbTable> tables;

    @XmlElement(name = "View")
    protected List<DbView> views;

    @XmlElement(name = "ViewReplacement")
    protected List<DbViewReplacement> viewReplacement;

    @XmlElement(name = "Sequence")
    protected List<DbSequence> sequences;

    @XmlElement(name = "SequenceReplacement")
    protected DbSequenceReplacement sequenceReplacement;

    /**
     * Gets the value of the tables property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tables property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTables().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DbTable }
     * 
     * 
     */
    public List<DbTable> getTables() {
        if (tables == null) {
            tables = new ArrayList<DbTable>();
        }
        return this.tables;
    }

    /**
     * Gets the value of the views property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the views property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getViews().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DbView }
     * 
     * 
     */
    public List<DbView> getViews() {
        if (views == null) {
            views = new ArrayList<DbView>();
        }
        return this.views;
    }

    /**
     * Gets the value of the viewReplacement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the viewReplacement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getViewReplacement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DbViewReplacement }
     * 
     * 
     */
    public List<DbViewReplacement> getViewReplacement() {
        if (viewReplacement == null) {
            viewReplacement = new ArrayList<DbViewReplacement>();
        }
        return this.viewReplacement;
    }

    /**
     * Gets the value of the sequences property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sequences property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSequences().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DbSequence }
     * 
     * 
     */
    public List<DbSequence> getSequences() {
        if (sequences == null) {
            sequences = new ArrayList<DbSequence>();
        }
        return this.sequences;
    }

    /**
     * Gets the value of the sequenceReplacement property.
     * 
     * @return
     *     possible object is
     *     {@link DbSequenceReplacement }
     *     
     */
    public DbSequenceReplacement getSequenceReplacement() {
        return sequenceReplacement;
    }

    /**
     * Sets the value of the sequenceReplacement property.
     * 
     * @param value
     *     allowed object is
     *     {@link DbSequenceReplacement }
     *     
     */
    public void setSequenceReplacement(DbSequenceReplacement value) {
        this.sequenceReplacement = value;
    }
}
