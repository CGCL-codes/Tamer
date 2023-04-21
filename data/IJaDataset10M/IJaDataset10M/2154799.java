package org.apache.commons.compress.changes;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the results of an performed ChangeSet operation.
 */
public class ChangeSetResults {

    private final List addedFromChangeSet = new ArrayList();

    private final List addedFromStream = new ArrayList();

    private final List deleted = new ArrayList();

    /**
     * Adds the filename of a recently deleted file to the result list.
     * @param fileName the file which has been deleted
     */
    void deleted(String fileName) {
        deleted.add(fileName);
    }

    /**
     * Adds the name of a file to the result list which has been 
     * copied from the source stream to the target stream.
     * @param fileName the file name which has been added from the original stream
     */
    void addedFromStream(String fileName) {
        addedFromStream.add(fileName);
    }

    /**
     * Adds the name of a file to the result list which has been
     * copied from the changeset to the target stream
     * @param fileName the name of the file
     */
    void addedFromChangeSet(String fileName) {
        addedFromChangeSet.add(fileName);
    }

    /**
     * Returns a list of filenames which has been added from the changeset
     * @return the list of filenames
     */
    public List getAddedFromChangeSet() {
        return addedFromChangeSet;
    }

    /**
     * Returns a list of filenames which has been added from the original stream
     * @return the list of filenames
     */
    public List getAddedFromStream() {
        return addedFromStream;
    }

    /**
     * Returns a list of filenames which has been deleted
     * @return the list of filenames
     */
    public List getDeleted() {
        return deleted;
    }

    /**
     * Checks if an filename already has been added to the result list
     * @param filename the filename to check
     * @return true, if this filename already has been added
     */
    boolean hasBeenAdded(String filename) {
        if (addedFromChangeSet.contains(filename) || addedFromStream.contains(filename)) {
            return true;
        }
        return false;
    }
}
