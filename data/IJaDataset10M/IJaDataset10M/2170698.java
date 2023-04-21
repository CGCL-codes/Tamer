package net.kano.joustsim.oscar.oscar.service.icbm.ft;

import net.kano.joscar.rv.RvSession;
import net.kano.joscar.rvcmd.ConnectionRequestRvCmd;
import net.kano.joscar.rvcmd.InvitationMessage;
import net.kano.joscar.rvcmd.sendfile.FileSendBlock;
import net.kano.joscar.rvcmd.sendfile.FileSendReqRvCmd;
import net.kano.joustsim.Screenname;
import net.kano.joustsim.oscar.oscar.service.icbm.RendezvousSessionHandler;
import net.kano.joustsim.oscar.oscar.service.icbm.dim.MutableSessionConnectionInfo;
import net.kano.joustsim.oscar.oscar.service.icbm.ft.controllers.ReceiveFileController;
import net.kano.joustsim.oscar.oscar.service.icbm.ft.controllers.StateController;
import net.kano.joustsim.oscar.oscar.service.icbm.ft.controllers.ConnectedController;
import net.kano.joustsim.oscar.oscar.service.icbm.ft.events.ConnectionCompleteEvent;
import net.kano.joustsim.oscar.oscar.service.icbm.ft.events.RvConnectionEvent;
import net.kano.joustsim.oscar.oscar.service.icbm.ft.state.StateInfo;
import net.kano.joustsim.oscar.oscar.service.icbm.ft.state.StreamInfo;
import net.kano.joustsim.oscar.proxy.AimProxyInfo;
import java.util.logging.Logger;

public class IncomingFileTransferImpl extends IncomingRvConnectionImpl implements IncomingFileTransfer {

    private static final Logger LOGGER = Logger.getLogger(IncomingFileTransferImpl.class.getName());

    private FileMapper fileMapper;

    private FileTransferHelper helper = new FileTransferHelper(this);

    IncomingFileTransferImpl(AimProxyInfo proxy, Screenname screenname, RvSessionConnectionInfo rvsessioninfo) {
        super(proxy, screenname, rvsessioninfo);
        fileMapper = new DefaultFileMapper(getBuddyScreenname(), System.getProperty("user.dir"));
    }

    public IncomingFileTransferImpl(AimProxyInfo proxy, Screenname screenname, RvSession session) {
        this(proxy, screenname, new MutableSessionConnectionInfo(session));
        ((MutableSessionConnectionInfo) getRvSessionInfo()).setMaker(new FileTransferRequestMaker(this));
    }

    protected RendezvousSessionHandler createSessionHandler() {
        return new IncomingFtRvSessionHandler();
    }

    public synchronized void setFileMapper(FileMapper mapper) {
        fileMapper = mapper;
    }

    public synchronized FileMapper getFileMapper() {
        return fileMapper;
    }

    protected ConnectedController createConnectedController(StateInfo endState) {
        return new ReceiveFileController();
    }

    protected boolean isConnectedController(StateController controller) {
        return controller instanceof ReceiveFileController;
    }

    protected NextStateControllerInfo getNextControllerFromSuccess(StateController oldController, StateInfo oldStateInfo) {
        if (oldController instanceof ReceiveFileController) {
            LOGGER.fine("Changing from success of receive controller to " + "completed");
            return new NextStateControllerInfo(RvConnectionState.FINISHED, new ConnectionCompleteEvent());
        } else if (oldStateInfo instanceof StreamInfo) {
            throw new IllegalStateException("stream info here??");
        } else {
            throw new IllegalStateException("Unknown last controller " + oldController);
        }
    }

    protected NextStateControllerInfo getNextControllerFromUnknownError(StateController oldController, StateInfo oldState, RvConnectionEvent event) {
        if (oldController instanceof ReceiveFileController) {
            return new NextStateControllerInfo(RvConnectionState.FAILED, event);
        } else {
            throw new IllegalStateException("Unknown controller " + oldController);
        }
    }

    public RvRequestMaker getRvRequestMaker() {
        return helper.getRvRequestMaker();
    }

    public InvitationMessage getInvitationMessage() {
        return helper.getInvitationMessage();
    }

    private void setInvitationMessage(InvitationMessage msg) {
        helper.setInvitationMessage(msg);
    }

    public FileSendBlock getRequestFileInfo() {
        return helper.getFileInfo();
    }

    private void setFileInfo(FileSendBlock block) {
        helper.setFileInfo(block);
    }

    private class IncomingFtRvSessionHandler extends AbstractIncomingRvSessionHandler {

        public IncomingFtRvSessionHandler() {
            super(IncomingFileTransferImpl.this);
        }

        protected void handleFirstRequest(ConnectionRequestRvCmd reqCmd) {
            FileSendReqRvCmd ftcmd = (FileSendReqRvCmd) reqCmd;
            setFileInfo(ftcmd.getFileSendBlock());
            setInvitationMessage(ftcmd.getMessage());
        }
    }
}
