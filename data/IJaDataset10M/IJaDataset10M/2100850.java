package sipinspector.Threads;

import sipinspector.*;
import java.io.IOException;
import sipinspector.ScenarioEntries.ScenarioException;
import sipinspector.Socket.MySocketAbstract;
import sipinspector.Utils.SIPParser;

/**
 *
 * @author Zarko Coklin
 */
public class ReceiverThread extends Thread {

    public ReceiverThread(ScenarioProgressDialog scenarioProgressDialog, MySocketAbstract socket) {
        progressDialog = scenarioProgressDialog;
        callDB = scenarioProgressDialog.getCallDB();
        this.socket = socket;
        threadStopped = false;
    }

    @Override
    public void run() {
        MyPacket packet;
        try {
            while (true) {
                if (threadStopped == true) {
                    return;
                }
                packet = socket.receive();
                if (packet != null) {
                    handleIncomingMsgs(packet);
                }
            }
        } catch (IOException exc) {
        }
    }

    private SIPCall handleIncomingMsgs(MyPacket packet) {
        boolean newCallFlag = false;
        SIPCall currCall = callDB.findCallForMsg(packet.getMessageTxt());
        if (currCall == null) {
            currCall = new SIPCall(progressDialog, packet);
            newCallFlag = true;
        }
        synchronized (currCall) {
            try {
                if (currCall.isEndReached() == true) {
                    return null;
                }
                if (currCall.processIncomingMsg(packet.getMessageTxt()) == true) {
                    if (currCall.isEndReached() == true) {
                        callDB.deleteCall(currCall);
                    }
                    if (SIPParser.isMsgRequest(packet.getMessageTxt()) == true) {
                        currCall.setRemoteAddress(packet.getAddress());
                        currCall.setRemotePort(packet.getPort());
                    }
                    progressDialog.getCallsInProgress().add(currCall);
                    if (newCallFlag == true) {
                        callDB.addCall(currCall, currCall.getCallID(), true);
                    }
                }
            } catch (ScenarioException ex) {
                currCall.nextEntry();
            }
            return currCall;
        }
    }

    public void stopThread() {
        threadStopped = true;
    }

    private ScenarioProgressDialog progressDialog;

    private CallDB callDB;

    private MySocketAbstract socket;

    private boolean threadStopped;
}
