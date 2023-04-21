package com.scholardesk.rss;

import java.util.Date;
import java.util.Properties;

public class GenericFeedEntry extends Properties implements FeedEntry {

    private Date m_modified_date = null;

    public GenericFeedEntry(String _title, String _url, String _description) {
        setTitle(_title);
        setURL(_url);
        setDescription(_description);
    }

    public GenericFeedEntry(String _title, String _url, String _description, Date _modified_date) {
        this(_title, _url, _description);
        setLastModifiedDate(_modified_date);
    }

    public String getCreator() {
        return getProperty("creator");
    }

    public String getDescription() {
        return getProperty("description");
    }

    public String getTitle() {
        return getProperty("title");
    }

    public String getURL() {
        return getProperty("url");
    }

    public Date getLastModifiedDate() {
        return m_modified_date;
    }

    public void setCreator(String _creator) {
        setProperty("creator", _creator);
    }

    public void setDescription(String _description) {
        setProperty("description", _description);
    }

    public void setTitle(String _title) {
        setProperty("title", _title);
    }

    public void setURL(String _url) {
        setProperty("url", _url);
    }

    public void setLastModifiedDate(Date _date) {
        m_modified_date = _date;
    }
}
