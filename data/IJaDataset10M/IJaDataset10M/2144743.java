package jresearchtool.common;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @see jresearchtool.common.Item
 * 
 * @author Anton Wolf
 */
public abstract class AbstractItem implements Item {

    /** Logger for this class */
    private static final Logger logger = Logger.getLogger(AbstractItem.class.getName());

    /** authors of this item */
    protected ArrayList<Author> authors;

    protected ArrayList<ItemChangeListener> itemChangeListeners;

    /** tags describing the item */
    protected ArrayList<String> tags;

    /** title of the item */
    protected String title;

    /** topic of the titem */
    protected Topic topic;

    public AbstractItem(String title) {
        this.title = title;
        this.authors = new ArrayList<Author>();
        this.tags = new ArrayList<String>();
        this.itemChangeListeners = new ArrayList<ItemChangeListener>();
    }

    public boolean addAuthor(Author author) {
        logger.finer("Adding author: " + author + " to item: " + title);
        return authors.add(author);
    }

    public boolean addItemChangeListener(ItemChangeListener itemChangeListener) {
        return itemChangeListeners.add(itemChangeListener);
    }

    /**
	 * @see jresearchtool.common.Item#addTag(java.lang.String)
	 */
    public boolean addTag(String tag) {
        logger.finer("Adding tag: " + tag + " to item: " + title);
        return tags.add(tag);
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getTitle() {
        return title;
    }

    public boolean removeAuthor(Author author) {
        logger.finer("Removing author: " + author + " from item: " + title);
        return authors.remove(author);
    }

    public boolean removeItemChangeListener(ItemChangeListener itemChangeListener) {
        return itemChangeListeners.remove(itemChangeListener);
    }

    /**
	 * @see jresearchtool.common.Item#removeTag(java.lang.String)
	 */
    public boolean removeTag(String tag) {
        logger.finer("Removing tag: " + tag + " from item: " + title);
        return tags.remove(tag);
    }

    public void setTitle(String title) {
        this.title = title;
        for (ItemChangeListener itemChangeListener : itemChangeListeners) {
            itemChangeListener.itemChanged(this);
        }
    }

    @Override
    public String toString() {
        return title;
    }

    public Topic getTopic() {
        return this.topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
