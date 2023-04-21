package uk.icat3.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for searchByRunNumber complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="searchByRunNumber">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sessionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="instruments" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="startRun" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="endRun" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "searchByRunNumber", propOrder = { "sessionId", "instruments", "startRun", "endRun" })
public class SearchByRunNumber {

    protected String sessionId;

    protected List<String> instruments;

    protected float startRun;

    protected float endRun;

    /**
     * Gets the value of the sessionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Sets the value of the sessionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionId(String value) {
        this.sessionId = value;
    }

    /**
     * Gets the value of the instruments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the instruments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInstruments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getInstruments() {
        if (instruments == null) {
            instruments = new ArrayList<String>();
        }
        return this.instruments;
    }

    /**
     * Gets the value of the startRun property.
     * 
     */
    public float getStartRun() {
        return startRun;
    }

    /**
     * Sets the value of the startRun property.
     * 
     */
    public void setStartRun(float value) {
        this.startRun = value;
    }

    /**
     * Gets the value of the endRun property.
     * 
     */
    public float getEndRun() {
        return endRun;
    }

    /**
     * Sets the value of the endRun property.
     * 
     */
    public void setEndRun(float value) {
        this.endRun = value;
    }
}
