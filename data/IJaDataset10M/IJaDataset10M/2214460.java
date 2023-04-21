package edu.gmu.xacml.lock.wsimpl;

import edu.gmu.xacml.lock.Lock;
import edu.gmu.xacml.lock.client.impl.LockRequest;

public class LockRequestWrapper implements Lock {

    private LockRequest lock;

    public LockRequestWrapper() {
        lock = new LockRequest();
    }

    public String getOwnerId() {
        return lock.getActorId();
    }

    public String getResourceId() {
        return lock.getResourceId();
    }

    public void setOwnerId(String ownerId) {
        lock.setActorId(ownerId);
    }

    public void setResourceId(String resourceId) {
        lock.setResourceId(resourceId);
    }

    protected LockRequest getLock() {
        return lock;
    }

    protected void setLock(LockRequest lock) {
        this.lock = lock;
    }
}
