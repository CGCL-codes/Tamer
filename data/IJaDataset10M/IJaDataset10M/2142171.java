package eu.fbk.hlt.common.etaf;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for EntailmentPair complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EntailmentPair">
 *   &lt;complexContent>
 *     &lt;extension base="{}AttributeHolder">
 *       &lt;sequence>
 *         &lt;element name="t" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="h" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tAnnotation" type="{}AnnotatedText" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="hAnnotation" type="{}AnnotatedText" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="entailment" type="{}Truth" />
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="length" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="task" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntailmentPair", propOrder = { "t", "h", "tAnnotation", "hAnnotation" })
public class EntailmentPair extends AttributeHolder {

    @XmlElement(required = true)
    protected String t;

    @XmlElement(required = true)
    protected String h;

    protected List<AnnotatedText> tAnnotation;

    protected List<AnnotatedText> hAnnotation;

    @XmlAttribute
    protected Truth entailment;

    @XmlAttribute
    protected String id;

    @XmlAttribute
    protected String length;

    @XmlAttribute
    protected String task;

    /**
     * Gets the value of the t property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getT() {
        return t;
    }

    /**
     * Sets the value of the t property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setT(String value) {
        this.t = value;
    }

    /**
     * Gets the value of the h property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getH() {
        return h;
    }

    /**
     * Sets the value of the h property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setH(String value) {
        this.h = value;
    }

    /**
     * Gets the value of the tAnnotation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tAnnotation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTAnnotation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AnnotatedText }
     * 
     * 
     */
    public List<AnnotatedText> getTAnnotation() {
        if (tAnnotation == null) {
            tAnnotation = new ArrayList<AnnotatedText>();
        }
        return this.tAnnotation;
    }

    /**
     * Gets the value of the hAnnotation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hAnnotation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHAnnotation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AnnotatedText }
     * 
     * 
     */
    public List<AnnotatedText> getHAnnotation() {
        if (hAnnotation == null) {
            hAnnotation = new ArrayList<AnnotatedText>();
        }
        return this.hAnnotation;
    }

    /**
     * Gets the value of the entailment property.
     * 
     * @return
     *     possible object is
     *     {@link Truth }
     *     
     */
    public Truth getEntailment() {
        return entailment;
    }

    /**
     * Sets the value of the entailment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Truth }
     *     
     */
    public void setEntailment(Truth value) {
        this.entailment = value;
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
     * Gets the value of the length property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLength(String value) {
        this.length = value;
    }

    /**
     * Gets the value of the task property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTask() {
        return task;
    }

    /**
     * Sets the value of the task property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTask(String value) {
        this.task = value;
    }
}
