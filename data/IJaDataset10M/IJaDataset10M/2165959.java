package org.gbif.portal.dto.occurrence;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * ImageRecordDTO. Image record information
 * 
 * @author Donald Hobern
 */
public class ImageRecordDTO {

    /** The key for the record */
    protected String key;

    /** The key for the data provider record */
    protected String dataProviderKey;

    /** The provider name */
    protected String dataProviderName;

    /** The key for the data resource record */
    protected String dataResourceKey;

    /** The data resource name */
    protected String dataResourceName;

    /** The key for the occurrence record */
    protected String occurrenceRecordKey;

    /** The catalogue number for the occurrence record */
    protected String occurrenceRecordCatalogueNumber;

    /** Concept within the taxonomy */
    protected String taxonConceptKey;

    /** Image type */
    protected Integer imageType;

    /** URL */
    protected String url;

    /** Description */
    protected String description;

    /** Rights */
    protected String rights;

    /** HTML for display (when thumbnail not possible) */
    protected String htmlForDisplay;

    /** The image height in pixels*/
    protected Integer heightInPixels;

    /** The image width in pixels*/
    protected Integer widthInPixels;

    /** Title of this image */
    protected String title;

    /**
	 * @return the dataProviderKey
	 */
    public String getDataProviderKey() {
        return dataProviderKey;
    }

    /**
	 * @param dataProviderKey the dataProviderKey to set
	 */
    public void setDataProviderKey(String dataProviderKey) {
        this.dataProviderKey = dataProviderKey;
    }

    /**
	 * @return the dataResourceKey
	 */
    public String getDataResourceKey() {
        return dataResourceKey;
    }

    /**
	 * @param dataResourceKey the dataResourceKey to set
	 */
    public void setDataResourceKey(String dataResourceKey) {
        this.dataResourceKey = dataResourceKey;
    }

    /**
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description the description to set
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return the imageType
	 */
    public Integer getImageType() {
        return imageType;
    }

    /**
	 * @param imageType the imageType to set
	 */
    public void setImageType(Integer imageType) {
        this.imageType = imageType;
    }

    /**
	 * @return the key
	 */
    public String getKey() {
        return key;
    }

    /**
	 * @param key the key to set
	 */
    public void setKey(String key) {
        this.key = key;
    }

    /**
	 * @return the occurrenceRecordKey
	 */
    public String getOccurrenceRecordKey() {
        return occurrenceRecordKey;
    }

    /**
	 * @param occurrenceRecordKey the occurrenceRecordKey to set
	 */
    public void setOccurrenceRecordKey(String occurrenceRecordKey) {
        this.occurrenceRecordKey = occurrenceRecordKey;
    }

    /**
	 * @return the rights
	 */
    public String getRights() {
        return rights;
    }

    /**
	 * @param rights the rights to set
	 */
    public void setRights(String rights) {
        this.rights = rights;
    }

    /**
	 * @return the taxonConceptKey
	 */
    public String getTaxonConceptKey() {
        return taxonConceptKey;
    }

    /**
	 * @param taxonConceptKey the taxonConceptKey to set
	 */
    public void setTaxonConceptKey(String taxonConceptKey) {
        this.taxonConceptKey = taxonConceptKey;
    }

    /**
	 * @return the url
	 */
    public String getUrl() {
        return url;
    }

    /**
	 * @param url the url to set
	 */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
	 * @return the htmlForDisplay
	 */
    public String getHtmlForDisplay() {
        return htmlForDisplay;
    }

    /**
	 * @param htmlForDisplay the htmlForDisplay to set
	 */
    public void setHtmlForDisplay(String htmlForDisplay) {
        this.htmlForDisplay = htmlForDisplay;
    }

    /**
	 * @return the heightInPixels
	 */
    public Integer getHeightInPixels() {
        return heightInPixels;
    }

    /**
	 * @param heightInPixels the heightInPixels to set
	 */
    public void setHeightInPixels(Integer heightInPixels) {
        this.heightInPixels = heightInPixels;
    }

    /**
	 * @return the title
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * @param title the title to set
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * @return the widthInPixels
	 */
    public Integer getWidthInPixels() {
        return widthInPixels;
    }

    /**
	 * @param widthInPixels the widthInPixels to set
	 */
    public void setWidthInPixels(Integer widthInPixels) {
        this.widthInPixels = widthInPixels;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
	 * @return the dataProviderName
	 */
    public String getDataProviderName() {
        return dataProviderName;
    }

    /**
	 * @param dataProviderName the dataProviderName to set
	 */
    public void setDataProviderName(String dataProviderName) {
        this.dataProviderName = dataProviderName;
    }

    /**
	 * @return the dataResourceName
	 */
    public String getDataResourceName() {
        return dataResourceName;
    }

    /**
	 * @param dataResourceName the dataResourceName to set
	 */
    public void setDataResourceName(String dataResourceName) {
        this.dataResourceName = dataResourceName;
    }

    /**
	 * @return the occurrenceRecordCatalogueNumber
	 */
    public String getOccurrenceRecordCatalogueNumber() {
        return occurrenceRecordCatalogueNumber;
    }

    /**
	 * @param occurrenceRecordCatalogueNumber the occurrenceRecordCatalogueNumber to set
	 */
    public void setOccurrenceRecordCatalogueNumber(String occurrenceRecordCatalogueNumber) {
        this.occurrenceRecordCatalogueNumber = occurrenceRecordCatalogueNumber;
    }
}
