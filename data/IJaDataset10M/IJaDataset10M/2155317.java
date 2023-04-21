package org.aspentools.dormouse.bookmarks;

import java.io.IOException;
import java.util.List;

/**
 * Implementers of this interface read a bookmark file, RSS feed, or other URL
 * and create a bookmark.
 * 
 * @author mfortner
 */
public interface BookmarkReader {

    /**
     * This method reads a bookmark from a file or valid URL.
     * @param url
     * @return
     * @throws java.io.IOException
     */
    public Bookmark readBookmark(String url) throws IOException;

    /**
     * This method reads a folder of bookmarks and returns a list of them.
     * @param folderUrl
     * @return
     * @throws java.io.IOException
     */
    public List<Bookmark> readBookmarks(String folderUrl) throws IOException;
}
