package iax.protocol.peer.state;

import iax.protocol.frame.Frame;
import iax.protocol.frame.ProtocolControlFrame;
import iax.protocol.peer.Peer;
import iax.protocol.peer.command.recv.PeerCommandRecvFacade;

/**
 * Peer's state registered. It's a singleton
 */
public class Registered extends PeerState {

    private static Registered instance;

    private Registered() {
        instance = this;
    }

    /**
     * Gets an instance of this state
     * @return the instance of this state
     */
    public static Registered getInstance() {
        if (instance != null) {
            return instance;
        } else return new Registered();
    }

    public void handleRecvFrame(Peer peer, Frame frame) {
        try {
            if (frame.getType() == Frame.PROTOCOLCONTROLFRAME_T) {
                ProtocolControlFrame protocolControlFrame = (ProtocolControlFrame) frame;
                switch(protocolControlFrame.getSubclass()) {
                    case ProtocolControlFrame.NEW_SC:
                        peer.recvCall(protocolControlFrame);
                        break;
                    default:
                        super.handleRecvFrame(peer, frame);
                        break;
                }
            } else {
                super.handleRecvFrame(peer, frame);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSendFrame(Peer peer, Frame frame) {
        try {
            if (frame.getType() == Frame.PROTOCOLCONTROLFRAME_T) {
                ProtocolControlFrame protocolControlFrame = (ProtocolControlFrame) frame;
                switch(protocolControlFrame.getSubclass()) {
                    case ProtocolControlFrame.REGREL_SC:
                        peer.sendFullFrameAndWaitForRep(protocolControlFrame);
                        peer.setState(Waiting.getInstance());
                        break;
                    case ProtocolControlFrame.REGREQ_SC:
                        peer.sendFullFrameAndWaitForRep(protocolControlFrame);
                        peer.setState(Waiting.getInstance());
                        break;
                    default:
                        super.handleSendFrame(peer, frame);
                        break;
                }
            } else {
                super.handleSendFrame(peer, frame);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
