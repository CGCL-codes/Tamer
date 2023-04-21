package com.cntinker.dto;

import java.io.Serializable;

/**
 * @autohr: bin_liu
 *
 */
public class UrlDto implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("com.cntinker.dto.UrlDto: [urlValue:").append(urlValue);
        sb.append(",title: ").append(title);
        sb.append("]");
        return sb.toString();
    }

    private String urlValue;

    private String title;

    public String getUrlValue() {
        return urlValue;
    }

    public void setUrlValue(String urlValue) {
        this.urlValue = urlValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
