package org.ljtoolkit.data;

import java.net.URL;

/**
 * This class represents a Live Journal picture.
 * 
 * @author Troy Bourdon
 *
 */
public class Picture {

    private String keyword;

    private URL url;

    /**
	 * Get the picture keyword.
	 * 
	 * @return String
	 */
    public String getKeyword() {
        return keyword;
    }

    /**
	 * Set the picture keyword.
	 * 
	 * @param keyword
	 */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
	 * Get the picture url.
	 * 
	 * @return URL
	 */
    public URL getUrl() {
        return url;
    }

    /**
	 * Set the picture url.
	 * 
	 * @param url
	 */
    public void setUrl(URL url) {
        this.url = url;
    }
}
