package wsdir.security;

import atomik.core.AConcept;

public class DefaultStoreSecurityManager implements StoreSecurity {

    public boolean register(Object concept) {
        return true;
    }

    public boolean deregister(Object concept) {
        return true;
    }

    public boolean modify(Object concept) {
        return true;
    }
}
