package com.javaeedev.web;

import java.util.Date;
import java.util.List;

/**
 * Represent an XML-style rss data.
 * 
 * @author Xuefeng
 */
public class RssData {

    private String title;

    private String link;

    private String description;

    private String date;

    private List<RssItem> items;

    public RssData(String title, String link, String description, long pubDate, List<RssItem> items) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.date = new Date(pubDate).toString();
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public List<RssItem> getItems() {
        return items;
    }
}
