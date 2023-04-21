package org.nees.tivo;

/**
 * A subclass to identify Exceptions specific to the Archive.
 * @author Terry E Weymouth
 * @version $Revision: 153 $ (CVS Revision number)
 */
public class ArchiveException extends Exception {

    ArchiveException() {
        super();
    }

    ArchiveException(String message) {
        super(message);
    }
}
