package com.esri.gpt.catalog.search;

/**
 * The Interface ISearchFilterContentTypes.   Defines
 * a content types filter object.
 */
public interface ISearchFilterContentTypes extends ISearchFilter {

    /**
 * Gets the selected content type.
 * 
 * @return the selected content type
 */
    public String getSelectedContentType();

    /**
 * Sets the selected content type.
 * 
 * @param themeTypes the new selected content type
 */
    public void setSelectedContentType(String themeTypes);
}
