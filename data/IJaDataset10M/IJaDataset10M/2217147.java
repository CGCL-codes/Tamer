package com.liferay.portlet.documentlibrary.util.comparator;

import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;

/**
 * <a href="FileEntryModifiedDateComparator.java.html"><b><i>View Source</i></b>
 * </a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class FileEntryModifiedDateComparator extends OrderByComparator {

    public static String ORDER_BY_ASC = "modifiedDate ASC";

    public static String ORDER_BY_DESC = "modifiedDate DESC";

    public FileEntryModifiedDateComparator() {
        this(false);
    }

    public FileEntryModifiedDateComparator(boolean asc) {
        _asc = asc;
    }

    public int compare(Object obj1, Object obj2) {
        DLFileEntry fileEntry1 = (DLFileEntry) obj1;
        DLFileEntry fileEntry2 = (DLFileEntry) obj2;
        int value = DateUtil.compareTo(fileEntry1.getModifiedDate(), fileEntry2.getModifiedDate());
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
