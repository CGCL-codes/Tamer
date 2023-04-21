package de.nava.informa.impl.basic;

import java.net.URL;
import java.util.Date;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.FeedIF;

/**
 * In-Memory implementation of the FeedIF interface.
 *
 * @author Niko Schmuck
 */
public class Feed implements FeedIF {

    private static final long serialVersionUID = 1349458681404088401L;

    private long id;

    private String title;

    private String text;

    private URL location;

    private URL site;

    private String contentType;

    private String copyright;

    private Date dateFound;

    private Date lastUpdated;

    private ChannelIF feed;

    /**
   * Default constructor.
   */
    public Feed() {
        this("No title");
    }

    /**
   * Convinence constrcutor - creates meta data for a preexisting feed.
   * @param channel
   */
    public Feed(ChannelIF channel) {
        setChannel(channel);
        setTitle(channel.getTitle());
        setLocation(channel.getLocation());
        setSite(channel.getSite());
        setCopyright(channel.getCopyright());
    }

    public Feed(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public String getCopyright() {
        return copyright;
    }

    public Date getDateFound() {
        return dateFound;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public URL getLocation() {
        return location;
    }

    public URL getSite() {
        return site;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public void setContentType(String string) {
        this.contentType = string;
    }

    public void setCopyright(String string) {
        this.copyright = string;
    }

    public void setDateFound(Date date) {
        this.dateFound = date;
    }

    public void setLastUpdated(Date date) {
        this.lastUpdated = date;
    }

    public void setLocation(URL location) {
        this.location = location;
    }

    public void setSite(URL site) {
        this.site = site;
    }

    public void setText(String string) {
        this.text = string;
    }

    public void setTitle(String string) {
        this.title = string;
    }

    public ChannelIF getChannel() {
        return feed;
    }

    public void setChannel(ChannelIF channelIF) {
        feed = channelIF;
    }
}
