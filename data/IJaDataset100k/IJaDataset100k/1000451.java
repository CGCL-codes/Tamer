package com.germinus.xpression.cms.lucene.beans;

/**
 * Indica los contenidos a indexar.
 * @author Jesús Jáimez Rodríguez <jesusjaimez@germinus.com>
 */
public class ContentRestrictionsBean {

    private String contentId;

    private String contentTitle;

    private String contentTypeId;

    private String worldId;

    private String xmlData;

    public ContentRestrictionsBean() {
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentTypeId() {
        return contentTypeId;
    }

    public void setContentTypeId(String contentTypeId) {
        this.contentTypeId = contentTypeId;
    }

    public String getWorldId() {
        return worldId;
    }

    public void setWorldId(String worldId) {
        this.worldId = worldId;
    }

    public String getXmlData() {
        return xmlData;
    }

    public void setXmlData(String xmlData) {
        this.xmlData = xmlData;
    }
}
