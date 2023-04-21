package org.ztest.filter;

import java.util.Set;
import org.ztest.classinfo.ZIClassFilter;

public class ZClassFilterByClasses implements ZIClassFilter {

    private ZIClassFilter next = null;

    final Set<String> classes;

    public ZClassFilterByClasses(Set<String> classes, ZIClassFilter next) {
        this(classes);
        this.next = next;
    }

    public ZClassFilterByClasses(Set<String> classes) {
        this.classes = classes;
    }

    public boolean accept(String name) throws Exception {
        if (!classes.contains(name)) {
            return false;
        }
        return next == null || next.accept(name);
    }
}
