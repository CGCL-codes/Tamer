package com.gnizr.db.dao;

import java.io.Serializable;

/**
 * <p>This class provides the representation of a relationship between a URL link and a tag. 
 * Prior to the creation of this relationship, a valid <code>Link</code> and <code>Tag</code> 
 * must have been created in the database with defined ID fields.</p>
 * <p>The ID of this class uniquely identifies a relationship between <code>Link</code>
 * and <code>Tag</code>. This ID is usually assigned by the database system when the relationship
 * is constructed for the first time.</p>
 * @author Harry Chen
 * @since 2.2
 *
 */
public class LinkTag implements Serializable, TagLabel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7871011373344014420L;

    private int id;

    private Link link;

    private Tag tag;

    private int count;

    /**
	 * Creates a new instance of this class.
	 */
    public LinkTag() {
        count = 0;
    }

    /**
	 * Creates a new instance of this class with a defined ID
	 * @param id
	 */
    public LinkTag(int id) {
        this();
        this.id = id;
    }

    /**
	 * Creates a new instance of this class with a defined <code>Link</code>
	 * and <code>Tag</code>
	 * 
	 * @param link
	 * @param tag
	 */
    public LinkTag(Link link, Tag tag) {
        this();
        this.link = new Link(link);
        this.tag = new Tag(tag);
    }

    /**
	 * Creates a new instance of this class with a defined <code>Link</code>
	 * and tag in it's <code>String</code> representation.
	 * @param url
	 * @param tag
	 */
    public LinkTag(String url, String tag) {
        this();
        this.link = new Link(url);
        this.tag = new Tag(tag);
    }

    /**
	 * Copy constructor for <code>LinkTag</code>. 
	 * 
	 * @param linkTag object to copy from.
	 */
    public LinkTag(LinkTag linkTag) {
        this.id = linkTag.id;
        this.link = new Link(linkTag.link);
        this.tag = new Tag(linkTag.tag);
        this.count = linkTag.count;
    }

    /**
	 * Returns the number of times <code>link</code> is
	 * tagged by <code>tag</code>.
	 */
    public int getCount() {
        return count;
    }

    /**
	 * Sets the number of the times <code>link</code> is 
	 * tagged by <code>tag</code>
	 * @param count number of times a link is tagged
	 */
    public void setCount(int count) {
        this.count = count;
    }

    /**
	 * Returns the ID of this <code>LinkTag</code>
	 * @return 
	 */
    public int getId() {
        return id;
    }

    /**
	 * Sets the ID of this <code>LinkTag</code>
	 * @param id
	 */
    public void setId(int id) {
        this.id = id;
    }

    /**
	 * Returns the link in this <code>LinkTag</code> relationship.
	 * @return a <code>Link</code> object
	 */
    public Link getLink() {
        return link;
    }

    /**
	 * Set the link in this <code>LinkTag</code> relationship.
	 * @param link
	 */
    public void setLink(Link link) {
        this.link = link;
    }

    /**
	 * Returns the tag in this <code>LinkTag</code> relationship.
	 * @return a <code>Tag</code> object
	 */
    public Tag getTag() {
        return tag;
    }

    /**
	 * Sets the tag in this <code>LinkTag</code> relationship.
	 * @param tag a <code>Tag</code> object
	 */
    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + count;
        result = PRIME * result + id;
        result = PRIME * result + ((link == null) ? 0 : link.hashCode());
        result = PRIME * result + ((tag == null) ? 0 : tag.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final LinkTag other = (LinkTag) obj;
        if (count != other.count) return false;
        if (id != other.id) return false;
        if (link == null) {
            if (other.link != null) return false;
        } else if (!link.equals(other.link)) return false;
        if (tag == null) {
            if (other.tag != null) return false;
        } else if (!tag.equals(other.tag)) return false;
        return true;
    }

    /**
	 * Returns the string representation of the tag in this 
	 * relationship. 
	 * @return a tag string
	 */
    public String getLabel() {
        if (tag != null) {
            return tag.getLabel();
        }
        return null;
    }
}
