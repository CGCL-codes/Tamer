package com.rapidminer.gui.look.fc;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 * The model for the list containing the bookmarks.
 *
 * @author Ingo Mierswa
 */
public class BookmarkListModel extends AbstractListModel {

    private static final long serialVersionUID = 9145792845942034849L;

    private List<Bookmark> bookmarks = new ArrayList<Bookmark>();

    public void removeAllBookmarks() {
        int oldSize = bookmarks.size();
        bookmarks.clear();
        fireIntervalRemoved(this, 0, oldSize);
    }

    public void addBookmark(Bookmark bookmark) {
        bookmarks.add(bookmark);
        fireIntervalAdded(this, bookmarks.size(), bookmarks.size());
    }

    public Object getElementAt(int index) {
        return bookmarks.get(index);
    }

    public int getSize() {
        return bookmarks.size();
    }
}
