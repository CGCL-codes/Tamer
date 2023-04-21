package org.monet.kernel.listeners;

import org.monet.kernel.bpi.BPIEventManager;
import org.monet.kernel.bpi.BPIEventManagerFactory;
import org.monet.kernel.model.MonetEvent;
import org.monet.kernel.model.Node;
import org.monet.kernel.model.Task;
import org.monet.kernel.workmap.lock.Worklock;

public class ListenerBPI extends Listener {

    private BPIEventManager bpi;

    public ListenerBPI() {
        this.bpi = BPIEventManagerFactory.getInstance().get();
    }

    @Override
    public void nodeCreated(MonetEvent event) {
        Node node = (Node) event.getSender();
        this.bpi.onNodeCreated(node);
    }

    @Override
    public void beforeModifyNode(MonetEvent event) {
        Node node = (Node) event.getSender();
        this.bpi.onNodeSaving(node);
    }

    @Override
    public void nodeModified(MonetEvent event) {
        Node node = (Node) event.getSender();
        this.bpi.onNodeSaved(node);
    }

    @Override
    public void nodeRemoved(MonetEvent event) {
        Node node = (Node) event.getSender();
        this.bpi.onNodeRemoved(node);
    }

    @Override
    public void nodeExecuteCommand(MonetEvent event) {
        Node node = (Node) event.getSender();
        this.bpi.onNodeExecuteCommand(node, (String) event.getParameter(MonetEvent.PARAMETER_COMMAND));
    }

    @Override
    public void nodeAddedToCollection(MonetEvent event) {
        Node node = (Node) event.getSender();
        Node parent = (Node) event.getParameter(MonetEvent.PARAMETER_COLLECTION);
        this.bpi.onNodeItemAdded(parent, node);
    }

    @Override
    public void nodeRemovedFromCollection(MonetEvent event) {
        Node node = (Node) event.getSender();
        Node parent = (Node) event.getParameter(MonetEvent.PARAMETER_COLLECTION);
        this.bpi.onNodeItemRemoved(parent, node);
    }

    @Override
    public void taskCreated(MonetEvent event) {
        Task task = (Task) event.getSender();
        this.bpi.onCreateTask(task);
    }

    @Override
    public void taskWorkMapInitialized(MonetEvent event) {
        String idTask = (String) event.getSender();
        this.bpi.onInitializeTaskWorkMap(idTask);
    }

    @Override
    public void taskWorkMapTerminated(MonetEvent event) {
        String idTask = (String) event.getSender();
        this.bpi.onTerminateTaskWorkMap(idTask);
    }

    @Override
    public void taskWorkMapAborted(MonetEvent event) {
        String idTask = (String) event.getSender();
        this.bpi.onAbortTaskWorkMap(idTask);
    }

    @Override
    public void taskWorkPlaceArrival(MonetEvent event) {
        String idTask = (String) event.getSender();
        this.bpi.onArriveTaskWorkPlace(idTask, (String) event.getParameter(MonetEvent.PARAMETER_WORKSTOP), (String) event.getParameter(MonetEvent.PARAMETER_WORKPLACE));
    }

    @Override
    public void taskWorkPlaceArrivalByException(MonetEvent event) {
        String idTask = (String) event.getSender();
        this.bpi.onArriveTaskWorkPlaceByException(idTask, (String) event.getParameter(MonetEvent.PARAMETER_WORKPLACE));
    }

    @Override
    public void taskWorkPlaceLeaved(MonetEvent event) {
        String idTask = (String) event.getSender();
        this.bpi.onLeaveTaskWorkPlace(idTask, (String) event.getParameter(MonetEvent.PARAMETER_WORKPLACE));
    }

    @Override
    public void taskWorkLineStarted(MonetEvent event) {
        String idTask = (String) event.getSender();
        this.bpi.onStartTaskWorkLine(idTask, (String) event.getParameter(MonetEvent.PARAMETER_WORKLINE));
    }

    @Override
    public void taskWorkStopTaken(MonetEvent event) {
        String idTask = (String) event.getSender();
        this.bpi.onTakeTaskWorkStop(idTask, (String) event.getParameter(MonetEvent.PARAMETER_WORKSTOP));
    }

    @Override
    public void taskLockSolved(MonetEvent event) {
        String idTask = (String) event.getSender();
        this.bpi.onSolveTaskLock(idTask, (Worklock<?>) event.getParameter(MonetEvent.PARAMETER_LOCK), (String) event.getParameter(MonetEvent.PARAMETER_WORKLINE));
    }

    @Override
    public void taskServiceUseLockCreated(MonetEvent event) {
        String idTask = (String) event.getSender();
        this.bpi.onServiceUseLockCreated(idTask, (Worklock<?>) event.getParameter(MonetEvent.PARAMETER_LOCK), (Node) event.getParameter(MonetEvent.PARAMETER_DOCUMENT));
    }

    @Override
    public void taskFormLockCreated(MonetEvent event) {
        String idTask = (String) event.getSender();
        this.bpi.onFormLockCreated(idTask, (Worklock<?>) event.getParameter(MonetEvent.PARAMETER_LOCK), (Node) event.getParameter(MonetEvent.PARAMETER_FORM));
    }
}
