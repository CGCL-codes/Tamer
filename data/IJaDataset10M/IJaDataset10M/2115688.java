package net.sourceforge.jcoupling.peer.destination;

import java.net.URI;

/**
 * Represents a logical, shared, message destination in the middleware.  Many
 * applications may share the channel.  The message will go to the lucky first
 * receiver.  More formally - there is a race condition to obtain the message between
 * all receivers of any Channel. It is given a URI (not URL) to uniquely define it.
 * @author Lachlan Aldred
 */
public class Channel extends Destination {

    /**
     * Creates a reference to a logical channel.
     * @param uri the unique id of the address.
     * @param metadata [optional] metadata of any format that describes the
     */
    public Channel(URI uri, String metadata) {
        super(uri, metadata);
    }
}
