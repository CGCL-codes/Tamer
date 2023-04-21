package org.tigris.subversion.javahl;

import java.util.Date;

/**
 * This interface is used to receive every single line for a file on a
 * the SVNClientInterface.blame call.
 */
public interface BlameCallback {

    /**
     * the method will be called for every line in a file.
     * @param changed   the date of the last change.
     * @param revision  the revision of the last change.
     * @param author    the author of the last change.
     * @param line      the line in the file
     */
    public void singleLine(Date changed, long revision, String author, String line);
}
