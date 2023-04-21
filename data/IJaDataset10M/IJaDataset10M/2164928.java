package org.newsml.toolkit;

import java.io.IOException;

/**
 * A collection of metadata.
 *
 * @author Reuters PLC
 * @version 2.0
 */
public interface TopicSet extends CatalogNode, CommentNode, FormalName {

    /**
     * Count the topic-set references.
     *
     * @return The number of topic-set references present.
     */
    public int getTopicSetRefCount();

    /**
     * Get a reference to another topic set.
     *
     * @param index The index of the topic set reference, zero-based,
     * numbered sequentially in document order.
     * @return The reference, or null if none was provided at the
     * specified index.
     */
    public TopicSetRef getTopicSetRef(int index);

    /**
     * Get all TopicSetRef children in an array.
     *
     * @return A (possibly-empty) array of TopicSetRef children.
     */
    public TopicSetRef[] getTopicSetRef();

    /**
     * Count the topics.
     *
     * @return The number of topics present.
     */
    public int getTopicCount();

    /**
     * Get a topic in the topic set.
     *
     * @param index The index of the topic, zero-based, numbered
     * sequentially in document order.
     * @return The topic, or null if none was provided at the specified
     * index.
     */
    public Topic getTopic(int index);

    /**
     * Get all Topic children in an array.
     *
     * @return A (possibly-empty) array of Topic children.
     */
    public Topic[] getTopic();

    /**
     * Find a topic given a formal name and scheme.
     *
     * @param name The single-part formal name to search for.
     * @param scheme The scheme to search for, or null if none is used.
     * @param useExternal True if the function should look outside
     * the current document.
     * @return The first matching Topic, or null if none is found
     * in the TopicSet or any external TopicSets it refers to.
     * @exception IOException If there is an error processing an
     * external TopicSet linked by an HTTP URL.
     */
    public Topic findTopic(String name, String scheme, boolean useExternal) throws IOException;
}
