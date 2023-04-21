package org.zkoss.zrss;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.zkoss.image.Image;

/**
 * A Facade Interface for RSS Feed no matter what version or standard.
 * @author Ian Tsai
 * @date 2007/5/22
 */
public interface RssFeed extends Serializable, Cloneable {

    /**
     * get web site's favorits icon.
     * @return
     */
    Image getIcon();

    /**
     * get web site's favorits icon url.
     * @return
     */
    String getIconLink();

    /**
     * get feed provided image.
     * @return
     */
    Image getFeedImage();

    /**
     * get feed provided image.
     * @return
     */
    String getFeedImageLink();

    /**
     * get title of this feed.
     * @return
     */
    String getFeedTitle();

    /**
     * 
     * @return
     */
    String getAuthor();

    /**
     * get Feed url Link.
     * @return
     */
    String getFeedLink();

    /**
     * get the description of this feed.
     * @return
     */
    String getFeedDesc();

    /**
     * get copyright.
     * @return
     */
    String getCopyright();

    /**
     * get news entry List.
     * @return
     */
    List<RssEntry> getFeedEntries();

    /**
     * get version and standard of this feed.
     * @return
     */
    String getFeedType();

    /**
     * get native implementation from ROME project.
     * @return
     */
    Object getNativeFeed();

    /**
     * get Newist Publish date.
     * @return
     */
    Date getPublishedDate();

    /**
     * See 
     * @return
     */
    String getUri();

    /**
     * 
     * @param string
     */
    void setFeedType(String string);

    /**
     * 
     * @param string
     */
    void setTitle(String string);

    /**
     * 
     * @param string
     */
    void setLink(String string);

    /**
     * 
     * @param string
     */
    void setDescription(String string);

    /**
     * 
     * @param string
     */
    void setCopyright(String string);

    /**
     * 
     * @param string
     */
    void setAuthor(String string);

    /**
     * get Newist Publish date.
     * @return
     */
    void setPublishedDate(Date date);

    /**
     * 
     * @param entries
     */
    void setEntries(List<RssEntry> entries);

    /**
     * output Feed As XML format String.
     * @return
     */
    String output();
}
