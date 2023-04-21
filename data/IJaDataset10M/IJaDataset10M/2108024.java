package net.evil_lair.javalicious;

import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * The interface defining a Post object.  A Post is an entry in the del.icio.us
 * server.  It contains at mimimum a URL, and optionally a description and tags.
 * If the post has been retrieved from the del.icio.us server, then it will also
 * contain a hash and submit date.  
 * 
 */
public interface IPost extends Comparable<IPost> {

    /**
	 * Returns the URL of the Post.
	 * 
	 * @return The URL of the Post.
	 */
    public URL getUrl();

    /**
	 * Returns the description of the Post, or null if it doesn't exist.
	 * 
	 * @return The description of the Post, or null if it doesn't exist.
	 */
    public String getDescription();

    /**
	 * Returns the extended description of the Post, or null if it doesn't exist.
	 * 
	 * @return The extendeddescription of the Post, or null if it doesn't exist.
	 */
    public String getExtended();

    /**
	 * Returns the hash of the Post, or null if it doesn't exist.
	 * 
	 * @return The hash of the Post, or null if it doesn't exist.
	 */
    public String getHash();

    /**
	 * Returns the tags specified on the Post, or null if no tags were set.
	 * 
	 * @return The tags specified on the Post, or null if no tags were set.
	 */
    public List<ITag> getTags();

    /**
	 * Returns the tags specified on the Post, or null if no tags were set, as 
	 * a space delimited string.
	 * 
	 * @return The tags specified on the Post, or null if no tags were set.
	 */
    public String getTagsAsString();

    /**
	 * Returns the time the Post was added, or null if it is not known.
	 * 
	 * @return The time the Post was added, or null if it is not known.
	 */
    public Date getTime();

    /**
	 * Sets the URL on this Post.
	 * 
	 * @param url The URL to set.
	 */
    public void setUrl(URL url);

    /**
	 * Sets the description on this Post.
	 * 
	 * @param description The description to set.
	 */
    public void setDescription(String description);

    /**
	 * Sets the tags on this Post.
	 * 
	 * @param tags The tags to set.
	 */
    public void setTags(List<ITag> tags);

    /**
	 * Validate's this Post.  Ensures that there is a valid URL.
	 * 
	 * @throws InvalidPostException
	 */
    public void validate() throws InvalidPostException;

    /**
	 * Returns an XML representation of this Post.
	 * 
	 * @return An XML representation of this Post.
	 */
    public String toXML();

    /**
	 * Creates a copy of this Post.
	 * 
	 * @return
	 */
    public IPost copy();
}
