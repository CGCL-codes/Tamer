package org.variance.version;

/**
 * Source for a revision resource such as a file, object, etc.
 * 
 * @author Matthew Purland
 */
public interface RevisionResource {

    /**
	 * Get data that will be revisioned.
	 */
    byte[] getData();
}
