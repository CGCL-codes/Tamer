package org.sourceforge.jemm.client;

import java.util.ArrayList;
import java.util.List;
import org.sourceforge.jemm.database.ClassId;
import org.sourceforge.jemm.database.ClassInfo;
import org.sourceforge.jemm.database.EnumId;
import org.sourceforge.jemm.database.EnumInfo;
import org.sourceforge.jemm.database.StructureModifiedException;
import org.sourceforge.jemm.lifecycle.ObjectRequest;
import org.sourceforge.jemm.lifecycle.ObjectResponse;
import org.sourceforge.jemm.types.ID;
import org.sourceforge.jemm.util.JEMMObject;
import org.sourceforge.jemm.util.JEMMType;

/**
 * A Database implementation that simulates the asynchronous locking
 * behaviour of a real database with a single listener which
 * ignores the threadId.
 * 
 * @author Paul Keeble
 *
 */
public class LockSimulationDatabase implements ObjectDatabase {

    ClientLockAcquiredListener listener;

    List<LockEvent> events;

    List<LockEvent> sent;

    public LockSimulationDatabase() {
        events = new ArrayList<LockEvent>(2);
        sent = new ArrayList<LockEvent>(2);
    }

    @Override
    public synchronized void acquireLock(String threadId, ID jemmId) {
        events.add(new LockEvent(threadId, jemmId));
    }

    public synchronized void sendEvents() {
        for (LockEvent le : events) {
            sent.add(le);
            listener.lockAcquired(le.getThreadId(), le.getJemmId());
        }
        events.clear();
    }

    @Override
    public void setClientLockAcquiredListener(ClientLockAcquiredListener listener) {
        this.listener = listener;
    }

    public List<LockEvent> getSent() {
        return sent;
    }

    public static class LockEvent {

        String threadId;

        ID jemmId;

        public LockEvent(String threadId, ID jemmId) {
            this.threadId = threadId;
            this.jemmId = jemmId;
        }

        public String getThreadId() {
            return threadId;
        }

        public ID getJemmId() {
            return jemmId;
        }
    }

    @Override
    public void releaseLock(String threadId, ID jemmId) {
    }

    @Override
    public void removeLockAcquiredListener() {
    }

    @Override
    public ClassInfo getClassInfo(ClassId classId) {
        return null;
    }

    @Override
    public EnumInfo getEnumInfo(EnumId enumId) {
        return null;
    }

    @Override
    public JEMMObject getObject(ID jemmId) {
        return null;
    }

    @Override
    public JEMMObject getRoot(String rootName) {
        return null;
    }

    @Override
    public ID newObject(ClassId classId, JEMMObject obj) {
        return null;
    }

    @Override
    public ID newType(ClassId classId, JEMMType typeRef, Object[] initArgs) {
        return null;
    }

    @Override
    public ClassId registerClass(ClassInfo classInfo) throws StructureModifiedException {
        return null;
    }

    @Override
    public EnumId registerEnum(EnumInfo enumInfo) throws StructureModifiedException {
        return null;
    }

    @Override
    public void setRoot(String rootName, JEMMObject newValue) {
    }

    @Override
    public JEMMObject setRootIfNull(String rootName, JEMMObject newValue) {
        return null;
    }

    @Override
    public void synchroniseObject(JEMMObject syncData) {
    }

    @Override
    public void refreshObject(JEMMObject obj) {
    }

    @Override
    public ObjectResponse<?> processTypeRequest(JEMMObject jemmObject, ObjectRequest<?> request) {
        return null;
    }

    @Override
    public JEMMObject getRefreshedObject(ID jemmId) {
        return null;
    }
}
