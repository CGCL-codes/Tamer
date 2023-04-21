package net.ontopia.topicmaps.entry;

import java.util.Collection;

/**
 * PUBLIC: Topic map sources collect references to topic maps.<p>
 */
public interface TopicMapSourceIF {

    /**
   * PUBLIC: Gets the id of the source.
   *
   * @since 1.3.2
   */
    public String getId();

    /**
   * PUBLIC: Sets the id of the source. Note that this method is
   * intended for use when the source is used in a
   * TopicMapRepositoryIF. The source should throw an {@link
   * java.lang.UnsupportedOperationException} if it does not support
   * setting the id.<p>
   *
   * @since 1.3.2
   */
    public void setId(String id);

    /**
   * PUBLIC: Gets the title of the source.
   *
   * @since 3.0
   */
    public String getTitle();

    /**
   * PUBLIC: Sets the title of the source.<p>
   *
   * @since 3.0
   */
    public void setTitle(String title);

    /**
   * PUBLIC: Returns an unmodifiable collection of {@link
   * TopicMapReferenceIF}s found by the topic map source.
   */
    public Collection<TopicMapReferenceIF> getReferences();

    /**
   * PUBLIC: Refreshes the collection of references. This lets the
   * source look at its underlying data source to reflect any changes
   * made since the last refresh.
   *
   * @since 1.3.2
   */
    public void refresh();

    /**
   * PUBLIC: Returns true if the source supports creating new
   * topic maps with the createTopicMap.
   *
   * @since 3.0
   */
    public boolean supportsCreate();

    /**
   * PUBLIC: Returns true if the source supports deleting topic map
   * with the TopicMapReferenceIF.delete() method.
   *
   * @since 3.4.1
   */
    public boolean supportsDelete();

    /**
   * PUBLIC: Creates a new topic map in the underlying source
   * and returns a reference to the created topic map. The method
   * takes a name and the base address for the topic map to create.
   *
   * @throws java.lang.UnsupportedOperationException
   * @since 3.0
   */
    public TopicMapReferenceIF createTopicMap(String name, String baseAddressURI);
}
