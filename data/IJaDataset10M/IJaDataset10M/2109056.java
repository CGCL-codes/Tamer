package com.ibm.tuningfork.infra.typespace;

import com.ibm.tuningfork.infra.feed.Feed;

/**
 * An EventTypeSpaceFactory is responsible for determining, given a Feed
 * and an EventTypeSpaceVersion
 */
public abstract class EventTypeSpaceFactory {

    /**
     * Can this EventTypeSpaceFactory create an EventTypeSpace that applies to
     * the EventTypeSpace version ets?
     *
     * @param ets the EventTypeSpaceVersion that is being considered.
     * @return true if the factory can create an EventTypeSpace that is compatible with ets,
     *         and false otherwise.
     */
    public abstract boolean isApplicable(EventTypeSpaceVersion etsv);

    /**
     * Create an EventTypeSpace instance for the argument Feed and EventTypeSpaceVersion.
     *
     * @param f the feed to which to bind the EventTypeSpace instance.
     * @param ets the EventTypeSpaceVersion instance of the feed that resulted in isApplicable returning true.
     * @return The newly constructed EventTypeSpace instance.
     */
    public abstract EventTypeSpace makeInstance(Feed f, EventTypeSpaceVersion etsv);
}
