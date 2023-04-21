package iwallet.client.requester;

import java.util.Deque;
import java.util.Iterator;
import iwallet.client.transport.TransportClient;
import iwallet.common.request.RevertableRequest;

public class UndoManager {

    private TransportClient transport;

    private Deque<RevertableRequest> opSeq;

    private Iterator<RevertableRequest> iter;

    /**
     * capacity=0时表示容量无限
     */
    private final int capacity;

    private iwallet.client.requester.ServiceRequester undoer;

    public UndoManager(TransportClient transport) {
        throw new UnsupportedOperationException();
    }

    public UndoManager(TransportClient transport, int cap) {
        throw new UnsupportedOperationException();
    }

    public void pushOperation(RevertableRequest op) {
        throw new UnsupportedOperationException();
    }

    public boolean canUndo() {
        throw new UnsupportedOperationException();
    }

    public boolean canRedo() {
        throw new UnsupportedOperationException();
    }

    public boolean undo() {
        throw new UnsupportedOperationException();
    }

    public boolean redo() {
        throw new UnsupportedOperationException();
    }
}
