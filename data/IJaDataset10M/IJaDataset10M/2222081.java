package org.biocatalogue.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for UsersParameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UsersParameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="filters" type="{http://www.biocatalogue.org/2009/xml/rest}FiltersParameters"/>
 *         &lt;element name="query" type="{http://www.biocatalogue.org/2009/xml/rest}SearchQueryParameter"/>
 *         &lt;element ref="{http://www.biocatalogue.org/2009/xml/rest}sortBy"/>
 *         &lt;element ref="{http://www.biocatalogue.org/2009/xml/rest}sortOrder"/>
 *         &lt;element ref="{http://www.biocatalogue.org/2009/xml/rest}page"/>
 *         &lt;element ref="{http://www.biocatalogue.org/2009/xml/rest}pageSize"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UsersParameters", propOrder = { "filters", "query", "sortBy", "sortOrder", "page", "pageSize" })
public class UsersParameters {

    @XmlElement(required = true)
    protected FiltersParameters filters;

    @XmlElement(required = true)
    protected SearchQueryParameter query;

    @XmlElement(required = true)
    protected SortByParameter sortBy;

    @XmlElement(required = true)
    protected SortOrderParameter sortOrder;

    @XmlElement(required = true)
    protected PageParameter page;

    @XmlElement(required = true)
    protected PageSizeParameter pageSize;

    /**
     * Gets the value of the filters property.
     * 
     * @return
     *     possible object is
     *     {@link FiltersParameters }
     *     
     */
    public FiltersParameters getFilters() {
        return filters;
    }

    /**
     * Sets the value of the filters property.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltersParameters }
     *     
     */
    public void setFilters(FiltersParameters value) {
        this.filters = value;
    }

    /**
     * Gets the value of the query property.
     * 
     * @return
     *     possible object is
     *     {@link SearchQueryParameter }
     *     
     */
    public SearchQueryParameter getQuery() {
        return query;
    }

    /**
     * Sets the value of the query property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchQueryParameter }
     *     
     */
    public void setQuery(SearchQueryParameter value) {
        this.query = value;
    }

    /**
     * Gets the value of the sortBy property.
     * 
     * @return
     *     possible object is
     *     {@link SortByParameter }
     *     
     */
    public SortByParameter getSortBy() {
        return sortBy;
    }

    /**
     * Sets the value of the sortBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link SortByParameter }
     *     
     */
    public void setSortBy(SortByParameter value) {
        this.sortBy = value;
    }

    /**
     * Gets the value of the sortOrder property.
     * 
     * @return
     *     possible object is
     *     {@link SortOrderParameter }
     *     
     */
    public SortOrderParameter getSortOrder() {
        return sortOrder;
    }

    /**
     * Sets the value of the sortOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link SortOrderParameter }
     *     
     */
    public void setSortOrder(SortOrderParameter value) {
        this.sortOrder = value;
    }

    /**
     * Gets the value of the page property.
     * 
     * @return
     *     possible object is
     *     {@link PageParameter }
     *     
     */
    public PageParameter getPage() {
        return page;
    }

    /**
     * Sets the value of the page property.
     * 
     * @param value
     *     allowed object is
     *     {@link PageParameter }
     *     
     */
    public void setPage(PageParameter value) {
        this.page = value;
    }

    /**
     * Gets the value of the pageSize property.
     * 
     * @return
     *     possible object is
     *     {@link PageSizeParameter }
     *     
     */
    public PageSizeParameter getPageSize() {
        return pageSize;
    }

    /**
     * Sets the value of the pageSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link PageSizeParameter }
     *     
     */
    public void setPageSize(PageSizeParameter value) {
        this.pageSize = value;
    }
}
