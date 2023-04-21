package org.dvb.si;

/**
 * Objects implementing SIIterator interface allow to browse through a set of SI
 * objects. In order to maintain consistency within the set of SI objects, this
 * browsing does NOT initiate an actual access to the stream.
 */
public interface SIIterator extends java.util.Enumeration {

    /**
	 * Get the number of remaining objects in the iterator.
	 * @return The number of remaining objects.
	 */
    public int numberOfRemainingObjects();
}
