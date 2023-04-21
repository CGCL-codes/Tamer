package org.thirdstreet.blogger;

import java.util.Date;
import org.thirdstreet.google.GoogleUtils;
import com.google.gdata.data.DateTime;

/**
 * Value object used to represent a blogger comment
 * 
 * @author John Bramlett
 */
public class BlogComment {

    protected String id;

    protected DateTime published;

    protected DateTime updated;

    protected String title;

    protected String author;

    protected String text;

    protected BlogPost post;

    /**
	 * Constructor
	 */
    public BlogComment() {
        super();
    }

    /**
	 * Gets the author
	 * 
	 * @return the author
	 */
    public String getAuthor() {
        return author;
    }

    /**
	 * Sets the author
	 * 
	 * @param author_ the author to set
	 */
    public void setAuthor(String author_) {
        author = author_;
    }

    /**
	 * Gets the id
	 * 
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * Sets the id
	 * 
	 * @param id_ the id to set
	 */
    public void setId(String id_) {
        id = id_;
    }

    /**
	 * Gets the published
	 * 
	 * @return the published
	 */
    public DateTime getPublished() {
        return published;
    }

    /**
	 * Returns the published date as a java date
	 * 
	 * @return Date The published date as a date
	 */
    public Date getPublishedAsDate() {
        return GoogleUtils.convertDate(published);
    }

    /**
	 * Sets the published
	 * 
	 * @param published_ the published to set
	 */
    public void setPublished(DateTime published_) {
        published = published_;
    }

    /**
	 * Gets the title
	 * 
	 * @return the title
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * Sets the title
	 * 
	 * @param title_ the title to set
	 */
    public void setTitle(String title_) {
        title = title_;
    }

    /**
	 * Gets the updated
	 * 
	 * @return the updated
	 */
    public DateTime getUpdated() {
        return updated;
    }

    /**
	 * Returns the updated date as a java date
	 * 
	 * @return Date The updated date as a date
	 */
    public Date getUpdatedAsDate() {
        return GoogleUtils.convertDate(updated);
    }

    /**
	 * Sets the updated
	 * 
	 * @param updated_ the updated to set
	 */
    public void setUpdated(DateTime updated_) {
        updated = updated_;
    }

    /**
	 * Gets our post id from the id
	 * 
	 * @return String The post id
	 */
    public String getPostId() {
        int index = id.lastIndexOf("-");
        return id.substring(index + 1);
    }

    /**
	 * Gets the text
	 * @return the text
	 */
    public String getText() {
        return text;
    }

    /**
	 * Sets the text
	 * @param text the text to set
	 */
    public void setText(String text) {
        this.text = text;
    }

    /**
	 * Gets the post
	 * @return the post
	 */
    public BlogPost getPost() {
        return post;
    }

    /**
	 * Sets the post
	 * @param post the post to set
	 */
    public void setPost(BlogPost post) {
        this.post = post;
    }
}
