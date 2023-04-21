package jresearchtool.common;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Topic is the top of the datamodel and contains all items and subtopics.
 * 
 * @author Anton Wolf
 */
public class Topic implements TopicChangeListener {

    /** Logger for this class */
    private static final Logger logger = Logger.getLogger(Topic.class.getName());

    /** items connected with this topic */
    protected ArrayList<Item> items;

    /** name of the topic */
    protected String name;

    protected ArrayList<TopicChangeListener> topicChangeListeners;

    /** topics related with this project */
    protected ArrayList<SubTopic> topics;

    /**
	 * @param name name of the topic
	 */
    public Topic(String name) {
        this.name = name;
        this.items = new ArrayList<Item>();
        this.topics = new ArrayList<SubTopic>();
        this.topicChangeListeners = new ArrayList<TopicChangeListener>();
    }

    /**
	 * @param item item to be added
	 * @return true if successful
	 */
    public boolean addItem(Item item) {
        logger.finer("Adding item " + item.getTitle() + " to project: " + name);
        boolean successful = items.add(item);
        if (successful) {
            item.setTopic(this);
            for (TopicChangeListener topicChangeListener : topicChangeListeners) {
                topicChangeListener.itemAdded(item);
            }
            item.addItemChangeListener(this);
        }
        return successful;
    }

    /**
	 * @param topic topic to be added
	 * @return true if successful
	 */
    public boolean addTopic(SubTopic topic) {
        logger.finer("Adding topic " + topic.getName() + " to project: " + name);
        boolean successful = topics.add(topic);
        if (successful) {
            topic.setParentTopic(this);
            for (TopicChangeListener topicChangeListener : topicChangeListeners) {
                topicChangeListener.subTopicAdded(topic);
            }
            topic.addTopicChangeListener(this);
        }
        return successful;
    }

    public boolean addTopicChangeListener(TopicChangeListener topicChangeListener) {
        return topicChangeListeners.add(topicChangeListener);
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public ArrayList<SubTopic> getTopics() {
        return topics;
    }

    /**
	 * @param item item to be removed
	 * @return true if successful
	 */
    public boolean removeItem(Item item) {
        logger.finer("Removing item " + item.getTitle() + " from project: " + name);
        boolean successful = items.remove(item);
        if (successful) {
            item.setTopic(null);
            for (TopicChangeListener topicChangeListener : topicChangeListeners) {
                topicChangeListener.itemRemoved(this);
            }
            item.removeItemChangeListener(this);
        }
        return successful;
    }

    /**
	 * @param topic topic to be removed
	 * @return true if successful
	 */
    public boolean removeTopic(SubTopic topic) {
        logger.finer("Removing topic " + topic.getName() + " from project: " + name);
        boolean successful = topics.remove(topic);
        if (successful) {
            topic.setParentTopic(null);
            for (TopicChangeListener topicChangeListener : topicChangeListeners) {
                topicChangeListener.subTopicRemoved(this);
            }
            topic.removeTopicChangeListener(this);
        }
        return successful;
    }

    public boolean removeTopicChangeListener(TopicChangeListener topicChangeListener) {
        return topicChangeListeners.remove(topicChangeListener);
    }

    public void setName(String name) {
        this.name = name;
        for (TopicChangeListener topicChangeListener : topicChangeListeners) {
            topicChangeListener.topicChanged(this);
        }
    }

    @Override
    public String toString() {
        return name;
    }

    /** 
	 * @see jresearchtool.common.ItemChangeListener#itemChanged(Item)
	 */
    public void itemChanged(Item changedItem) {
        for (TopicChangeListener topicChangeListener : topicChangeListeners) {
            topicChangeListener.itemChanged(changedItem);
        }
    }

    /**
	 * @see jresearchtool.common.TopicChangeListener#itemAdded(Item)
	 */
    public void itemAdded(Item addedItem) {
        for (TopicChangeListener topicChangeListener : topicChangeListeners) {
            topicChangeListener.itemAdded(addedItem);
        }
    }

    /**
	 * @see jresearchtool.common.TopicChangeListener#itemRemoved(Topic)
	 */
    public void itemRemoved(Topic parentTopic) {
        for (TopicChangeListener topicChangeListener : topicChangeListeners) {
            topicChangeListener.itemRemoved(parentTopic);
        }
    }

    /**
	 * @see jresearchtool.common.TopicChangeListener#subTopicAdded(SubTopic)
	 */
    public void subTopicAdded(SubTopic addedSubTopic) {
        for (TopicChangeListener topicChangeListener : topicChangeListeners) {
            topicChangeListener.subTopicAdded(addedSubTopic);
        }
    }

    /**
	 * @see jresearchtool.common.TopicChangeListener#subTopicRemoved(Topic)
	 */
    public void subTopicRemoved(Topic parentTopic) {
        for (TopicChangeListener topicChangeListener : topicChangeListeners) {
            topicChangeListener.subTopicRemoved(parentTopic);
        }
    }

    /**
	 * @see jresearchtool.common.TopicChangeListener#topicChanged(Topic)
	 */
    public void topicChanged(Topic changedTopic) {
        for (TopicChangeListener topicChangeListener : topicChangeListeners) {
            topicChangeListener.topicChanged(changedTopic);
        }
    }
}
