package services;

import net.jxta.endpoint.Message;
import net.jxta.share.MessageProcessor;
import Topology.AbstractTopology;
import java.io.IOException;
import java.util.Vector;
import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.EndpointAddress;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageFilterListener;
import net.jxta.endpoint.Messenger;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.id.ID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.share.CMS;

/**
 *
 * @author wassim
 */
public class FSSRdvMessageProcessor extends MessageProcessor {

    private FSM fss;

    private PeerGroup group;

    /**
     * Constructor for the FSSListMessageProcessor object
     *
     * @param fss 
     */
    public FSSRdvMessageProcessor(FSM fss, PeerGroup group) {
        super(fss.getCMS());
        this.fss = fss;
        this.group = group;
    }

    protected void process(Message message) {
        Vector<ID> ids = group.getRendezVousService().getConnectedPeerIDs();
        Messenger messenger = null;
        try {
            String addrStr = CMS.popString(message, CMS.RETURN_ADDRESS);
            EndpointAddress endpointAddress = new EndpointAddress(addrStr);
            messenger = fss.getCMS().getEndpointService().getMessenger(endpointAddress);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Message resultMsg = new Message();
        resultMsg.addMessageElement(null, new StringMessageElement(FSM.Message_Service, FSM.serviceName, null));
        resultMsg.addMessageElement(new StringMessageElement(CMS.MESSAGE_TYPE, FSM.GETRDV_Result, null));
        int count = 0;
        String tag;
        for (ID id : ids) {
            tag = FSM.PeerIdTag + (count++);
            resultMsg.addMessageElement(new StringMessageElement(tag, id.toString(), null));
        }
        resultMsg.addMessageElement(new StringMessageElement(FSM.PeerIdCountTag, Integer.toString(count), null));
        String RID = "null";
        int hopCount = -1;
        try {
            RID = CMS.popString(message, CMS.REQUEST_ID);
            hopCount = Integer.parseInt(fss.getCMS().popString(message, FSM.HOP_COUNT).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            resultMsg.addMessageElement(new StringMessageElement(CMS.REQUEST_ID, RID, null));
            messenger.sendMessage(resultMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
