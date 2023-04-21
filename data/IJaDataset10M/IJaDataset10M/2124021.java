package org.ztest.filter;

import org.ztest.classinfo.ZIClassFilter;

public class ZClassFilterAcceptAll implements ZIClassFilter {

    public boolean accept(String name) {
        return true;
    }
}
