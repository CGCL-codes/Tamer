package dryven.request;

import dryven.request.controller.ControllerDescription;
import dryven.request.controller.result.ActionResult;
import dryven.request.http.Request;

public class CompositeRequestListener implements RequestLifeCycleListener {

    private Iterable<RequestLifeCycleListener> _listeners;

    public CompositeRequestListener(Iterable<RequestLifeCycleListener> listeners) {
        super();
        _listeners = listeners;
    }

    @Override
    public void endRequest(Request request) {
        for (RequestLifeCycleListener l : _listeners) {
            l.endRequest(request);
        }
    }

    @Override
    public void startRequest(Request request, ControllerDescription cd) {
        for (RequestLifeCycleListener l : _listeners) {
            l.startRequest(request, cd);
        }
    }

    @Override
    public void startResult(Request request, ActionResult result) {
        for (RequestLifeCycleListener l : _listeners) {
            l.startResult(request, result);
        }
    }

    @Override
    public void endResult(Request request, ActionResult result) {
        for (RequestLifeCycleListener l : _listeners) {
            l.endResult(request, result);
        }
    }

    @Override
    public void beforeRouting(Request request) {
        for (RequestLifeCycleListener l : _listeners) {
            l.beforeRouting(request);
        }
    }

    @Override
    public void shutdown() {
        for (RequestLifeCycleListener l : _listeners) {
            l.shutdown();
        }
    }
}
