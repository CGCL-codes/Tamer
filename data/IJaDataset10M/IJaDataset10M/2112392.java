package eu.vph.predict.vre.in_silico.webservice.schema.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="ResultsReady" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "resultsReady" })
@XmlRootElement(name = "GetResultsReadyResponse")
public class GetResultsReadyResponse {

    @XmlElement(name = "ResultsReady")
    protected boolean resultsReady;

    /**
     * Gets the value of the resultsReady property.
     * 
     */
    public boolean isResultsReady() {
        return resultsReady;
    }

    /**
     * Sets the value of the resultsReady property.
     * 
     */
    public void setResultsReady(boolean value) {
        this.resultsReady = value;
    }
}
