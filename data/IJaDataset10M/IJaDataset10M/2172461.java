package de.fuh.xpairtise.plugin.ui.xpviews.whiteboard;

import de.fuh.xpairtise.common.LogConstants;
import de.fuh.xpairtise.common.XPLog;
import de.fuh.xpairtise.common.network.IServerCommandInterface;
import de.fuh.xpairtise.common.network.NetworkException;
import de.fuh.xpairtise.common.replication.IReplicatedListEventListener;
import de.fuh.xpairtise.common.replication.IReplicatedListReceiver;
import de.fuh.xpairtise.common.replication.IReplicatedListReceiverSynchronizeListener;
import de.fuh.xpairtise.common.replication.UnexpectedReplicationState;
import de.fuh.xpairtise.common.replication.elements.ReplicatedWhiteboardElement;
import de.fuh.xpairtise.common.replication.elements.ReplicatedWhiteboardEraserElement;
import de.fuh.xpairtise.common.replication.elements.ReplicatedWhiteboardPencilUpdate;
import de.fuh.xpairtise.plugin.network.ClientSideCommunicationFactory;

/**
 * This class represents the controller part of the MVC pattern based whiteboard
 * handling.
 */
public class WhiteboardController implements IWhiteboardController {

    private IServerCommandInterface commandInterface;

    private IReplicatedListReceiver<ReplicatedWhiteboardElement> receiver;

    private String xpSessionId;

    private IWhiteboardView view;

    /**
   * Creates a new whiteboard
   * 
   * @param channelId 
   *          whiteboard channel
   * @param view 
   *          the associated view interface
   * @throws Exception
   */
    public WhiteboardController(String channelId, IWhiteboardView view) throws Exception {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_WHITEBOARD_CONTROLLER + "starting.");
        }
        this.view = view;
        ClientSideCommunicationFactory factory = ClientSideCommunicationFactory.getInstance();
        receiver = factory.attachToList(channelId, new ReplicationListener(), new SynchronizeListener());
        commandInterface = factory.getServerCommandInterface();
        this.xpSessionId = channelId;
    }

    public void newPencilPoint(int[] points, byte[] color, int lineThickness) throws Exception {
        commandInterface.sendWhiteboardPencilUpdate(xpSessionId, points, color, lineThickness);
    }

    public void clearWhiteboard() throws Exception {
        commandInterface.sendWhiteboardClear(xpSessionId);
    }

    public void detach() throws NetworkException {
        receiver.detach();
    }

    public void newEraseCommand(int[] points) throws Exception {
        commandInterface.sendWhiteboardEraseCommand(xpSessionId, points);
    }

    private class SynchronizeListener implements IReplicatedListReceiverSynchronizeListener {

        public void listSynchronized() {
            view.onListSynchronized();
        }

        public void synchronizationStarted() {
            view.onStartSynchronization();
        }
    }

    private class ReplicationListener implements IReplicatedListEventListener<ReplicatedWhiteboardElement> {

        public void add(ReplicatedWhiteboardElement element) throws UnexpectedReplicationState {
            if (element instanceof ReplicatedWhiteboardPencilUpdate) {
                view.onAddPencilUpdateElement((ReplicatedWhiteboardPencilUpdate) element);
            } else if (element instanceof ReplicatedWhiteboardEraserElement) {
                view.onAddEraserElement((ReplicatedWhiteboardEraserElement) element);
            }
        }

        public void remove(ReplicatedWhiteboardElement element) throws UnexpectedReplicationState {
        }

        public void update(ReplicatedWhiteboardElement element) throws UnexpectedReplicationState {
        }

        public void removeAll() throws UnexpectedReplicationState {
            view.onRemoveAll();
        }
    }
}
