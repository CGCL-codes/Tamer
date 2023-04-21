package com.liferay.portlet.bookmarks.util.comparator;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;

/**
 * <a href="BookmarksURLComparator.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class BookmarksURLComparator extends OrderByComparator {

    public static String ORDER_BY_ASC = "url ASC";

    public static String ORDER_BY_DESC = "url DESC";

    public BookmarksURLComparator() {
        this(false);
    }

    public BookmarksURLComparator(boolean asc) {
        _asc = asc;
    }

    public int compare(Object obj1, Object obj2) {
        BookmarksEntry entry1 = (BookmarksEntry) obj1;
        BookmarksEntry entry2 = (BookmarksEntry) obj2;
        int value = entry1.getUrl().toLowerCase().compareTo(entry2.getUrl().toLowerCase());
        if (_asc) {
            return value;
        } else {
            return -value;
        }
    }

    public String getOrderBy() {
        if (_asc) {
            return ORDER_BY_ASC;
        } else {
            return ORDER_BY_DESC;
        }
    }

    private boolean _asc;
}
