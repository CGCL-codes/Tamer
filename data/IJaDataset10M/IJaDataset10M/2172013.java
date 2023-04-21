package com.empower.model;

/**
 * @author Alok Ranjan
 *
 */
public class ExchangeFileMetadata {

    private String commonFieldName;

    private String exchangeFieldName;

    private Integer positionInCsv;

    private String type;

    private Integer length;

    private String description;

    private String format;

    /**
	 * 
	 */
    public ExchangeFileMetadata() {
        ;
    }

    public ExchangeFileMetadata(String commonFieldName, String exchangeFieldName, Integer positionInCsv) {
        this.commonFieldName = commonFieldName;
        this.exchangeFieldName = exchangeFieldName;
        this.positionInCsv = positionInCsv;
    }

    /**
	 * @return the commonFieldName
	 */
    public String getCommonFieldName() {
        return commonFieldName;
    }

    /**
	 * @param commonFieldName the commonFieldName to set
	 */
    public void setCommonFieldName(String commonFieldName) {
        this.commonFieldName = commonFieldName;
    }

    /**
	 * @return the exchangeFieldName
	 */
    public String getExchangeFieldName() {
        return exchangeFieldName;
    }

    /**
	 * @param exchangeFieldName the exchangeFieldName to set
	 */
    public void setExchangeFieldName(String exchangeFieldName) {
        this.exchangeFieldName = exchangeFieldName;
    }

    /**
	 * @return the positionInCsv
	 */
    public Integer getPositionInCsv() {
        return positionInCsv;
    }

    /**
	 * @param positionInCsv the positionInCsv to set
	 */
    public void setPositionInCsv(Integer positionInCsv) {
        this.positionInCsv = positionInCsv;
    }

    /**
	 * @return the type
	 */
    public String getType() {
        return type;
    }

    /**
	 * @param type the type to set
	 */
    public void setType(String type) {
        this.type = type;
    }

    /**
	 * @return the length
	 */
    public Integer getLength() {
        return length;
    }

    /**
	 * @param length the length to set
	 */
    public void setLength(Integer length) {
        this.length = length;
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
	 * @return the format
	 */
    public String getFormat() {
        return format;
    }

    /**
	 * @param format the format to set
	 */
    public void setFormat(String format) {
        this.format = format;
    }
}
