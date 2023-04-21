package uk.co.fortunecookie.timesheet.data.entities.comparators;

import java.util.Comparator;
import uk.co.fortunecookie.timesheet.data.entities.SecurityRole;

public class SecurityRoleNameComparator implements Comparator<SecurityRole> {

    @Override
    public int compare(SecurityRole thisObj, SecurityRole thatObj) {
        int cmp = thisObj.getName().compareTo(thatObj.getName());
        if (cmp != 0) return cmp;
        return thisObj.getSecurityRoleId().compareTo(thatObj.getSecurityRoleId());
    }
}
