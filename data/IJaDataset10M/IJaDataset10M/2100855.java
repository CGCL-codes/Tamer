package net.sf.archimede.security;

import java.util.HashSet;
import java.util.Set;

class SecurityCaches {

    private Set publicItemsChache = new HashSet(10000);

    private static SecurityCaches INSTANCE;

    static {
        INSTANCE = new SecurityCaches();
    }

    private SecurityCaches() {
    }

    public static SecurityCaches singleton() {
        return INSTANCE;
    }

    public Set getPublicItemsCache() {
        return this.publicItemsChache;
    }
}
