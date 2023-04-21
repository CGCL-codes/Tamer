package com.liferay.portal.util.comparator;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.Role;

/**
 * <a href="RoleNameComparator.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class RoleNameComparator extends OrderByComparator {

    public static String ORDER_BY_ASC = "Role_.name ASC";

    public static String ORDER_BY_DESC = "Role_.name DESC";

    public RoleNameComparator() {
        this(false);
    }

    public RoleNameComparator(boolean asc) {
        _asc = asc;
    }

    public int compare(Object obj1, Object obj2) {
        Role role1 = (Role) obj1;
        Role role2 = (Role) obj2;
        int value = role1.getName().compareTo(role2.getName());
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
