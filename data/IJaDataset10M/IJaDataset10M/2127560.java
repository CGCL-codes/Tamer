package net.virtualearth.dev.webservices.v1.search.contracts;

import java.io.Serializable;
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
 *         &lt;element name="SearchResult" type="{http://dev.virtualearth.net/webservices/v1/search}SearchResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "searchResult" })
@XmlRootElement(name = "SearchResponse")
public class SearchResponse implements Serializable {

    private static final long serialVersionUID = 2461660169443089969L;

    @XmlElement(name = "SearchResult", nillable = true)
    protected net.virtualearth.dev.webservices.v1.search.SearchResponse searchResult;

    /**
     * Gets the value of the searchResult property.
     * 
     * @return
     *     possible object is
     *     {@link net.virtualearth.dev.webservices.v1.search.SearchResponse }
     *     
     */
    public net.virtualearth.dev.webservices.v1.search.SearchResponse getSearchResult() {
        return searchResult;
    }

    /**
     * Sets the value of the searchResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link net.virtualearth.dev.webservices.v1.search.SearchResponse }
     *     
     */
    public void setSearchResult(net.virtualearth.dev.webservices.v1.search.SearchResponse value) {
        this.searchResult = value;
    }
}
