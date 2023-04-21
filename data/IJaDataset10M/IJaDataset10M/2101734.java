package org.sourceforge.jemm.database.memory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.sourceforge.jemm.database.ClientId;
import org.sourceforge.jemm.database.ClientThreadId;
import org.sourceforge.jemm.database.components.ClientThreadIdRef;
import org.sourceforge.jemm.database.components.UserLockInfo;
import org.sourceforge.jemm.database.components.se.StorageEngineUserLockIF;
import org.sourceforge.jemm.types.ID;
import org.sourceforge.jemm.util.LockManager;

public class MemDbStorageEngineUserLockIFImpl implements StorageEngineUserLockIF {

    ConcurrentHashMap<ID, UserLockInfo> locks = new ConcurrentHashMap<ID, UserLockInfo>();

    ConcurrentHashMap<ClientId, Set<ClientThreadIdRef>> clientLockRefs = new ConcurrentHashMap<ClientId, Set<ClientThreadIdRef>>();

    LockManager<ClientId> clientLocks = new LockManager<ClientId>();

    @Override
    public UserLockInfo getLockInfo(ID id) {
        UserLockInfo lockInfo = locks.get(id);
        if (lockInfo == null) {
            lockInfo = new UserLockInfo(id);
            locks.put(id, lockInfo);
        }
        return lockInfo;
    }

    @Override
    public void saveLockInfo(UserLockInfo info) {
        if (info.isUnused()) locks.remove(info.getId());
    }

    @Override
    public void clearAll() {
        locks.clear();
        clientLockRefs.clear();
    }

    @Override
    public void addClientLockReference(ClientThreadId clientThreadId, ID objectId) {
        ClientId clientId = clientThreadId.getClientId();
        try {
            clientLocks.acquire(clientId);
            Set<ClientThreadIdRef> set = clientLockRefs.get(clientId);
            if (set == null) {
                set = new HashSet<ClientThreadIdRef>();
                clientLockRefs.put(clientId, set);
            }
            set.add(new ClientThreadIdRef(clientThreadId, objectId));
        } finally {
            clientLocks.release(clientId);
        }
    }

    @Override
    public Set<ClientThreadIdRef> getClientLockSet(ClientId clientId) {
        try {
            clientLocks.acquire(clientId);
            Set<ClientThreadIdRef> set = clientLockRefs.get(clientId);
            if (set == null) return Collections.emptySet(); else return Collections.unmodifiableSet(set);
        } finally {
            clientLocks.release(clientId);
        }
    }

    @Override
    public void removeClientLockReference(ClientThreadId clientThreadId, ID objectId) {
        ClientId clientId = clientThreadId.getClientId();
        try {
            clientLocks.acquire(clientId);
            Set<ClientThreadIdRef> set = clientLockRefs.get(clientId);
            if (set != null) {
                set.remove(new ClientThreadIdRef(clientThreadId, objectId));
                if (set.size() == 0) clientLockRefs.remove(clientId);
            }
        } finally {
            clientLocks.release(clientId);
        }
    }
}
