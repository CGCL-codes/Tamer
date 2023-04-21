package org.simpleframework.http.session;

import org.simpleframework.util.lease.Cleaner;
import org.simpleframework.util.lease.Lease;
import org.simpleframework.util.lease.LeaseException;
import org.simpleframework.util.lease.LeaseManager;
import org.simpleframework.util.lease.LeaseMap;

class Invalidator {

    private LeaseManager manager;

    private LeaseMap map;

    public Invalidator(Cleaner cleaner) {
        this.manager = new LeaseManager(cleaner);
        this.map = new LeaseMap();
    }

    public void lease(Object key, long duration) {
        Lease lease = manager.lease(key, duration);
        if (lease != null) {
            map.put(key, lease);
        }
    }

    public boolean renew(Object key) {
        return renew(key, 60000);
    }

    public boolean renew(Object key, long duration) {
        Lease lease = map.get(key);
        try {
            if (lease != null) {
                lease.renew(duration);
                return true;
            }
        } catch (LeaseException e) {
        }
        return false;
    }

    public boolean isInvalid(Object key) {
        Lease lease = map.get(key);
        try {
            if (lease != null) {
                return lease.getExpiry() <= 0;
            }
        } catch (LeaseException e) {
        }
        return true;
    }

    public void invalidate(Object key) {
        Lease lease = map.remove(key);
        try {
            if (lease != null) {
                lease.cancel();
            }
        } catch (LeaseException e) {
            return;
        }
    }
}
