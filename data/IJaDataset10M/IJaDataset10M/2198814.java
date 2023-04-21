package ru.ispu.gemini.core.io;

import java.util.LinkedList;
import java.util.List;
import ru.ispu.gemini.core.session.SessionController;
import ru.ispu.gemini.core.io.requests.AbstractRequest;

public abstract class PoolIOSystem extends IOSystem {

    private Boolean isProcessing = false;

    protected List<AbstractRequest> requests;

    public PoolIOSystem(SessionController session) {
        super(session);
        requests = new LinkedList<AbstractRequest>();
    }

    @Override
    public void serveRequest(AbstractRequest request) {
        requests.add(request);
        synchronized (isProcessing) {
            if (!isProcessing) {
                processRequest(request);
                isProcessing = Boolean.TRUE;
            }
        }
    }

    @Override
    public void shutdown() {
    }

    protected void processRequest(AbstractRequest request) {
    }
}
