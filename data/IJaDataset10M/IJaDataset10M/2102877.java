package net.sf.secretsvault.store;

/**
 *
 * @author quarck
 */
public class SynchronizationDelegate {

    public void localIsNewer(StoreItem local, StoreItem remote) {
    }

    public void remoteIsNewer(StoreItem local, StoreItem remote) {
    }

    public void newRemoteEntry(Directory local, StoreItem rch) {
    }

    public void newLocalEntry(Directory local, StoreItem rch) {
    }

    public void modificationConflict(StoreItem local, StoreItem remote) {
    }

    public void typeConflict(Directory parent, StoreItem local, StoreItem remote) {
    }
}
