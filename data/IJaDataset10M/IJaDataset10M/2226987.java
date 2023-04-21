package org.dancres.blitz.txn.batch;

import org.prevayler.Command;
import org.prevayler.PrevalentSystem;
import org.prevayler.implementation.PrevaylerCore;
import org.prevayler.implementation.SnapshotPrevayler;
import org.prevayler.implementation.Snapshotter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class OptimisticBatcher implements SnapshotPrevayler {

    private PrevaylerCore _prevayler;

    private boolean _writing = false;

    private ArrayList<WriteRequest> _writes = new ArrayList<WriteRequest>();

    public OptimisticBatcher(PrevaylerCore aPrevayler) {
        _prevayler = aPrevayler;
    }

    public PrevalentSystem system() {
        return _prevayler.system();
    }

    public Serializable executeCommand(Command aCommand) throws Exception {
        return write(aCommand, true);
    }

    public Serializable executeCommand(Command aCommand, boolean sync) throws Exception {
        return write(aCommand, sync);
    }

    private Serializable write(Command aComm, boolean sync) throws Exception {
        boolean someoneWriting = false;
        WriteRequest myReq = null;
        synchronized (this) {
            someoneWriting = _writing;
            if (_writing) {
                myReq = new WriteRequest(aComm);
                _writes.add(myReq);
            } else {
                _writing = true;
            }
        }
        if (someoneWriting) {
            if (sync) myReq.await();
            return aComm.execute(_prevayler.system());
        } else {
            _prevayler.logCommand(aComm, false);
            ArrayList<WriteRequest> myAllWrites = new ArrayList<WriteRequest>();
            ArrayList<WriteRequest> myBuffer = new ArrayList<WriteRequest>();
            while (haveWrites()) {
                synchronized (this) {
                    myBuffer.clear();
                    myBuffer.addAll(_writes);
                    _writes.clear();
                }
                Iterator<WriteRequest> myWrites = myBuffer.iterator();
                while (myWrites.hasNext()) _prevayler.logCommand(myWrites.next().getCommand(), false);
                myAllWrites.addAll(myBuffer);
            }
            try {
                return aComm.execute(_prevayler.system());
            } finally {
                Iterator<WriteRequest> myTargets = myAllWrites.iterator();
                while (myTargets.hasNext()) {
                    myTargets.next().dispatch();
                }
            }
        }
    }

    private boolean haveWrites() throws Exception {
        synchronized (this) {
            if (_writes.size() > 0) return true; else {
                _writing = false;
                _prevayler.flush();
                return false;
            }
        }
    }

    public Snapshotter takeSnapshot() throws IOException {
        return _prevayler.takeSnapshot();
    }

    private class WriteRequest {

        private Command _comm;

        private Object _lock = new Object();

        private boolean _exit = false;

        WriteRequest(Command aComm) {
            _comm = aComm;
        }

        Command getCommand() {
            return _comm;
        }

        void dispatch() {
            synchronized (_lock) {
                _exit = true;
                _lock.notify();
            }
        }

        void await() {
            synchronized (_lock) {
                while (!_exit) {
                    try {
                        _lock.wait();
                    } catch (InterruptedException anIE) {
                    }
                }
            }
        }
    }
}
