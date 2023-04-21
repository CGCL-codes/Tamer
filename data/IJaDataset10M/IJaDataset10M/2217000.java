package org.mockftpserver.fake.filesystem;

/**
 * Interface for an object that can format a file system directory listing.
 *
 * @author Chris Mair
 * @version $Revision: 182 $ - $Date: 2008-11-30 21:37:49 -0500 (Sun, 30 Nov 2008) $
 */
public interface DirectoryListingFormatter {

    /**
     * Format the directory listing for a single file/directory entry.
     *
     * @param fileSystemEntry - the FileSystemEntry for a single file system entry
     * @return the formatted directory listing
     */
    String format(FileSystemEntry fileSystemEntry);
}
