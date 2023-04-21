package com.liferay.portlet.shopping.util.comparator;

import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portlet.shopping.model.ShoppingOrder;

/**
 * <a href="OrderDateComparator.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class OrderDateComparator extends OrderByComparator {

    public static String ORDER_BY_ASC = "createDate ASC";

    public static String ORDER_BY_DESC = "createDate DESC";

    public OrderDateComparator() {
        this(false);
    }

    public OrderDateComparator(boolean asc) {
        _asc = asc;
    }

    public int compare(Object obj1, Object obj2) {
        ShoppingOrder order1 = (ShoppingOrder) obj1;
        ShoppingOrder order2 = (ShoppingOrder) obj2;
        int value = DateUtil.compareTo(order1.getCreateDate(), order2.getCreateDate());
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
