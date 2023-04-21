package com.sun.jndi.toolkit.dir;

import javax.naming.*;
import javax.naming.directory.*;

public class ContainmentFilter implements AttrFilter {

    private Attributes matchingAttrs;

    public ContainmentFilter(Attributes match) {
        matchingAttrs = match;
    }

    public boolean check(Attributes attrs) throws NamingException {
        return matchingAttrs == null || matchingAttrs.size() == 0 || contains(attrs, matchingAttrs);
    }

    public static boolean contains(Attributes superset, Attributes subset) throws NamingException {
        if (subset == null) return true;
        NamingEnumeration m = subset.getAll();
        while (m.hasMore()) {
            if (superset == null) {
                return false;
            }
            Attribute target = (Attribute) m.next();
            Attribute fromSuper = superset.get(target.getID());
            if (fromSuper == null) {
                return false;
            } else {
                if (target.size() > 0) {
                    NamingEnumeration vals = target.getAll();
                    while (vals.hasMore()) {
                        if (!fromSuper.contains(vals.next())) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
