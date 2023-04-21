package com.webeclubbin.mynpr;

import java.io.Serializable;

public class Podcast implements Cloneable, Serializable {

    /**
	 * Serial id number
	 */
    private static final long serialVersionUID = -2472794637052392218L;

    String title = null;

    String description = null;

    String pubDate = null;

    String audioUrl = null;

    String storyLink = null;

    String geoTag = null;

    String stationName = null;

    public static final String TITLE = "title";

    public static final String DESCRIPTION = "description";

    public static final String PUBDATE = "pubDate";

    public static final String AUDIOURL = "enclosure";

    public static final String STORYLINK = "link";

    public static final String GEOTAG = "georss:point";

    public void setTitle(String temp) {
        title = temp;
    }

    public void setDescription(String temp) {
        description = temp;
    }

    public void setPubDate(String temp) {
        pubDate = temp;
    }

    public void setAudioUrl(String temp) {
        audioUrl = temp;
    }

    public void setStoryLink(String temp) {
        storyLink = temp;
    }

    public void setGeoTag(String temp) {
        geoTag = temp;
    }

    public void setStation(String temp) {
        stationName = temp;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public String getStoryLink() {
        return storyLink;
    }

    public String getGeoTag() {
        return geoTag;
    }

    public String getStation() {
        return stationName;
    }

    public Object clone() throws CloneNotSupportedException {
        Podcast another = (Podcast) super.clone();
        return another;
    }
}
