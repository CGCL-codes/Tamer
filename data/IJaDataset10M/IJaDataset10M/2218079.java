package org.hardtokenmgmt.admin.common;

import java.io.Serializable;
import java.util.Date;

/**
 * VO object for a news item, contains information about validity, title, 
 * content, URL etc about a news.
 * 
 * @author Philip Vendil 7 may 2009
 *
 */
public class NewsDataVO implements Comparable<NewsDataVO>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * Unique identifier
	 */
    private int newsId = 0;

    private String title = null;

    private String url = null;

    private String description = null;

    private String application = null;

    private String orgId = null;

    private String author = null;

    private long created = 0;

    private long expireDate = 0;

    private Integer priority = 1;

    /**
	 * Empty Constructor
	 */
    public NewsDataVO() {
    }

    /**
	 * Default constructor.
	 * 
	 * @param title title of the news.
	 * @param description description, null if no description should be used.
	 * @param uRL to provide link to for more information, null indicates that no link should be provided.
	 * @param application application that the news is relevant to, Constants.ALL_APPLICATIONS means applicable to all applications.
	 * @param orgId organization that the news is relevant to, Constants.ALL_ORGANIZATIONS means applicable to all applications.
	 * @param Author id of administrator that wrote the news.
	 * @param expireDate time when the news isn't relevant any more and should be removed from DB.
	 * @param priority a value between 1 and 100 how important the news is, i.e in which order it should be presented, the highest first.
	 */
    public NewsDataVO(String title, String description, String uRL, String application, String orgId, String author, long expireDate, int priority) {
        this.title = title;
        this.description = description;
        this.url = uRL;
        this.application = application;
        this.orgId = orgId;
        this.author = author;
        this.expireDate = expireDate;
        this.priority = priority;
    }

    /**
	 * @return Unique identifier, should be set by JPA and not manually.
	 */
    public int getNewsId() {
        return newsId;
    }

    /**
	 * Unique identifier, should be set by JPA and not manually.
	 */
    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    /**
	 * @return title of the news.
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * 
	 * @param title title of the news.
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * 
	 * @return URL to provide link to for more information, null indicates that no link should be provided.
	 */
    public String getURL() {
        return url;
    }

    /**
	 * @param url URL to provide link to for more information, null indicates that no link should be provided.
	 */
    public void setURL(String url) {
        this.url = url;
    }

    /**
	 * 
	 * @return description, null if no description should be used.
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * 
	 * @param description, null if no description should be used.
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return  application that the news is relevant to, Constants.ALL_APPLICATIONS means applicable to all applications.
	 */
    public String getApplication() {
        return application;
    }

    /**
	 * 
	 * @param application application that the news is relevant to, Constants.ALL_APPLICATIONS means applicable to all applications.
	 */
    public void setApplication(String application) {
        this.application = application;
    }

    /**
	 * 
	 * @return Id of organization that the news is relevant to, Constants.ALL_ORGANIZATIONS means applicable to all applications.
	 */
    public String getOrgId() {
        return orgId;
    }

    /**
	 * 
	 * @param orgId  organization that the news is relevant to, Constants.ALL_ORGANIZATIONS means applicable to all applications.
	 */
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**
	 * 
	 * @return date when the news entry was created. Set by server.
	 */
    public long getCreated() {
        return created;
    }

    /**
	 * 
	 * @param created date when the news entry was created. Set by server.
	 */
    public void setCreated(long created) {
        this.created = created;
    }

    /**
	 * 
	 * @return time when the news isn't relevant any more and should be removed from DB.
	 */
    public long getExpireDate() {
        return expireDate;
    }

    /**
	 * 
	 * @param expireDate time when the news isn't relevant any more and should be removed from DB.
	 */
    public void setExpireDate(long expireDate) {
        this.expireDate = expireDate;
    }

    /**
	 * 
	 * @return id of administrator that created the news.
	 */
    public String getAuthor() {
        return author;
    }

    /**
	 * 
	 * @param createdBy id of administrator that created the news.
	 */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
	 * 
	 * @return a value between 1 and 100 how important the news is, i.e in which order it should be presented, the highest first.
	 */
    public int getPriority() {
        return priority;
    }

    /**
	 * 
	 * @param priority a value between 1 and 100 how important the news is, i.e in which order it should be presented, the highest first.
	 */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(NewsDataVO news) {
        if (priority != null) {
            if (!priority.equals(news.getPriority())) {
                return priority.compareTo(news.getPriority());
            } else {
                return (new Date(created)).compareTo(new Date(news.getCreated()));
            }
        } else {
            return -1;
        }
    }
}
