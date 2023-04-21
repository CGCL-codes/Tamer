package org.sourceforge.jemm.database.persistent.berkeley;

import java.util.LinkedList;
import java.util.List;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import org.sourceforge.jemm.database.ClientId;
import org.sourceforge.jemm.database.ClientThreadId;
import org.sourceforge.jemm.database.components.UserLockInfo;
import org.sourceforge.jemm.types.ID;

@Entity
public class UserLockEntry {

    @PrimaryKey
    long id;

    String clientId;

    String threadId;

    String[][] lockQueue;

    public UserLockEntry() {
    }

    public UserLockEntry(UserLockInfo info) {
        this.id = info.getId().getIDValue();
        ClientThreadId clientThreadId = info.getHolder();
        if (clientThreadId == null) {
            clientId = null;
            threadId = null;
        } else {
            clientId = clientThreadId.getClientId().getInternalRep();
            threadId = clientThreadId.getThreadId();
        }
        List<ClientThreadId> waitingThreads = info.getLockQueue();
        if (waitingThreads == null) lockQueue = null; else {
            lockQueue = new String[waitingThreads.size()][2];
            int idx = 0;
            for (ClientThreadId ctId : waitingThreads) {
                lockQueue[idx][0] = ctId.getClientId().getInternalRep();
                lockQueue[idx][1] = ctId.getThreadId();
                idx++;
            }
        }
    }

    public UserLockInfo convert() {
        ClientThreadId ctId;
        if (clientId == null) ctId = null; else ctId = new ClientThreadId(new ClientId(clientId), threadId);
        if (lockQueue == null) return new UserLockInfo(new ID(id), ctId, null); else {
            LinkedList<ClientThreadId> waitingThreads = new LinkedList<ClientThreadId>();
            for (String[] row : lockQueue) waitingThreads.add(new ClientThreadId(new ClientId(row[0]), row[1]));
            return new UserLockInfo(new ID(id), ctId, waitingThreads);
        }
    }
}
