package net.martinimix.domain.syndication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Provides a syndication feed been.
 * 
 * @author Scott Rossillo
 *
 */
public class SyndicationFeed implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String link;

    private String summary;

    private Long createdBy;

    private Long modifiedBy;

    private Calendar createDate;

    private Calendar modifyDate;

    private List entries;

    private String urn;

    /**
	 * Creates a new syndication feed.
	 */
    public SyndicationFeed() {
        entries = new ArrayList();
    }

    public void addEntry(SyndicationFeedEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException("[SyndicationFeedEntry] cannot be null!");
        }
        this.entries.add(entry);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List getEntries() {
        return entries;
    }

    public void setEntries(List entries) {
        this.entries = entries;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Calendar createDate) {
        this.createDate = createDate;
    }

    public Calendar getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Calendar modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
	 * Returns the uniform resource name for this feed.
	 * 
	 * @return
	 */
    public String getUrn() {
        return urn;
    }

    /**
	 * Sets the uniform resource name for this feed.
	 * 
	 * @param urn
	 */
    public void setUrn(String urn) {
        this.urn = urn;
    }
}
