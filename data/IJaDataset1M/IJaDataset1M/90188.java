package org.mockftpserver.fake.filesystem;

import org.mockftpserver.core.util.StringUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Windows-specific implementation of the DirectoryListingFormatter interface.
 *
 * @author Chris Mair
 * @version $Revision: 182 $ - $Date: 2008-11-30 21:37:49 -0500 (Sun, 30 Nov 2008) $
 */
public class WindowsDirectoryListingFormatter implements DirectoryListingFormatter {

    private static final String DATE_FORMAT = "MM-dd-yy hh:mmaa";

    private static final int SIZE_WIDTH = 15;

    /**
     * Format the directory listing for a single file/directory entry.
     *
     * @param fileSystemEntry - the FileSystemEntry for a single file system entry
     * @return the formatted directory listing
     */
    public String format(FileSystemEntry fileSystemEntry) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateStr = dateFormat.format(fileSystemEntry.getLastModified());
        String dirOrSize = fileSystemEntry.isDirectory() ? StringUtil.padRight("<DIR>", SIZE_WIDTH) : StringUtil.padLeft(Long.toString(fileSystemEntry.getSize()), SIZE_WIDTH);
        return dateStr + "  " + dirOrSize + "  " + fileSystemEntry.getName();
    }
}
