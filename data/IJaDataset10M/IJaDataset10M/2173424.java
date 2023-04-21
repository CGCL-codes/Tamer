package com.google.gdata.data.extensions;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;

/**
 * Customizes the base feed class to define a feed of events.
 *
 * @param <F> event feed class
 * @param <E> event entry class
 */
public abstract class BaseEventFeed<F extends BaseEventFeed<F, E>, E extends BaseEventEntry<E>> extends BaseFeed<F, E> {

    /**
   * Constructs a new event feed instance that is parameterized to contain event
   * entry instances.
   *
   * @param entryClass event entry class
   */
    public BaseEventFeed(Class<E> entryClass) {
        super(entryClass);
        getCategories().add(EventEntry.EVENT_CATEGORY);
    }

    /**
   * Constructs a new event feed instance that is initialized using data from
   * another base feed instance.
   *
   * @param entryClass event entry class
   * @param sourceFeed source feed
   */
    public BaseEventFeed(Class<E> entryClass, BaseFeed<?, ?> sourceFeed) {
        super(entryClass, sourceFeed);
        getCategories().add(EventEntry.EVENT_CATEGORY);
    }

    @Override
    public void declareExtensions(ExtensionProfile extProfile) {
        extProfile.declare(BaseEventFeed.class, Where.getDefaultDescription());
        super.declareExtensions(extProfile);
    }
}
