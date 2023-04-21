package de.sepp.aigaebeditormodule.jaxb.gaeb.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Kommunikation
 * In diesem Objekt werden Fragen und Antworten der Austauschpartner abgelegt.
 * 
 * <p>Java class for tgCommunication complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tgCommunication">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Question" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgNormalizedString80"/>
 *         &lt;element name="Response" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgNormalizedString80" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tgCommunication", propOrder = { "question", "response" })
public class TgCommunication {

    @XmlElement(name = "Question", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String question;

    @XmlElement(name = "Response")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String response;

    /**
     * Gets the value of the question property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Sets the value of the question property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuestion(String value) {
        this.question = value;
    }

    /**
     * Gets the value of the response property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponse(String value) {
        this.response = value;
    }
}
