package xml.rss.document;

import java.util.Date;

/**
 * Represents data forming the header block of an RSS document.
 * @author Ben HIll
 * @version 0.1
 */
public class Header {

    private String title;

    private String link;

    private String description;

    private String language;

    private Date pubDate;

    private Date lastBuildDate;

    private String docs;

    private String generator;

    private String managingEditor;

    private String webMaster;

    public Header() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public void setLastBuildDate(Date lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public void setDocs(String docs) {
        this.docs = docs;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public void setManagingEditor(String managingEditor) {
        this.managingEditor = managingEditor;
    }

    public void setWebMaster(String webMaster) {
        this.webMaster = webMaster;
    }

    public String getTitle() {
        return this.title;
    }

    public String getLink() {
        return this.link;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLanguage() {
        return this.language;
    }

    public Date getPubDate() {
        return this.pubDate;
    }

    public Date getLastBuildDate() {
        return this.lastBuildDate;
    }

    public String getDocs() {
        return this.docs;
    }

    public String getGenerator() {
        return this.generator;
    }

    public String getManagingEditor() {
        return this.managingEditor;
    }

    public String getWebMaster() {
        return this.webMaster;
    }
}
