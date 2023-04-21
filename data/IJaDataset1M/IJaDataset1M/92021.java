package org.vesuf.util.collection;

/**
 *	CollectionCreationException occurs, when it was not possible to
 *	build	up a suiting collection wrapper for the object.
 */
public class CollectionCreationException extends RuntimeException {

    /**
 	 *	Construct a new CollectionCreationException.
	 */
    public CollectionCreationException(String v) {
        super(v);
    }
}
