package net.virtualearth.dev.webservices.v1.search;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import net.virtualearth.dev.webservices.v1.common.FilterExpressionBase;

/**
 * <p>Java class for SearchOptions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SearchOptions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AutocorrectQuery" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Count" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Filters" type="{http://dev.virtualearth.net/webservices/v1/common}FilterExpressionBase" minOccurs="0"/>
 *         &lt;element name="ListingType" type="{http://dev.virtualearth.net/webservices/v1/search}ListingType" minOccurs="0"/>
 *         &lt;element name="ParseOnly" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Radius" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="SortOrder" type="{http://dev.virtualearth.net/webservices/v1/search}SortOrder" minOccurs="0"/>
 *         &lt;element name="StartingIndex" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchOptions", propOrder = { "autocorrectQuery", "count", "filters", "listingType", "parseOnly", "radius", "sortOrder", "startingIndex" })
public class SearchOptions implements Serializable {

    private static final long serialVersionUID = 2461660169443089969L;

    @XmlElement(name = "AutocorrectQuery", nillable = true)
    protected Boolean autocorrectQuery;

    @XmlElement(name = "Count", nillable = true)
    protected Integer count;

    @XmlElement(name = "Filters", nillable = true)
    protected FilterExpressionBase filters;

    @XmlElement(name = "ListingType")
    protected ListingType listingType;

    @XmlElement(name = "ParseOnly")
    protected Boolean parseOnly;

    @XmlElement(name = "Radius", nillable = true)
    protected Double radius;

    @XmlElement(name = "SortOrder")
    protected SortOrder sortOrder;

    @XmlElement(name = "StartingIndex")
    protected Integer startingIndex;

    /**
     * Gets the value of the autocorrectQuery property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAutocorrectQuery() {
        return autocorrectQuery;
    }

    /**
     * Sets the value of the autocorrectQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAutocorrectQuery(Boolean value) {
        this.autocorrectQuery = value;
    }

    /**
     * Gets the value of the count property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCount(Integer value) {
        this.count = value;
    }

    /**
     * Gets the value of the filters property.
     * 
     * @return
     *     possible object is
     *     {@link FilterExpressionBase }
     *     
     */
    public FilterExpressionBase getFilters() {
        return filters;
    }

    /**
     * Sets the value of the filters property.
     * 
     * @param value
     *     allowed object is
     *     {@link FilterExpressionBase }
     *     
     */
    public void setFilters(FilterExpressionBase value) {
        this.filters = value;
    }

    /**
     * Gets the value of the listingType property.
     * 
     * @return
     *     possible object is
     *     {@link ListingType }
     *     
     */
    public ListingType getListingType() {
        return listingType;
    }

    /**
     * Sets the value of the listingType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListingType }
     *     
     */
    public void setListingType(ListingType value) {
        this.listingType = value;
    }

    /**
     * Gets the value of the parseOnly property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isParseOnly() {
        return parseOnly;
    }

    /**
     * Sets the value of the parseOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setParseOnly(Boolean value) {
        this.parseOnly = value;
    }

    /**
     * Gets the value of the radius property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRadius() {
        return radius;
    }

    /**
     * Sets the value of the radius property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRadius(Double value) {
        this.radius = value;
    }

    /**
     * Gets the value of the sortOrder property.
     * 
     * @return
     *     possible object is
     *     {@link SortOrder }
     *     
     */
    public SortOrder getSortOrder() {
        return sortOrder;
    }

    /**
     * Sets the value of the sortOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link SortOrder }
     *     
     */
    public void setSortOrder(SortOrder value) {
        this.sortOrder = value;
    }

    /**
     * Gets the value of the startingIndex property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getStartingIndex() {
        return startingIndex;
    }

    /**
     * Sets the value of the startingIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setStartingIndex(Integer value) {
        this.startingIndex = value;
    }
}
