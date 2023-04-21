package amoeba.net;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import amoeba.data.Consts;
import amoeba.data.DataFinder;
import amoeba.data.StorageGraph;
import amoeba.util.AmoebaExceptionHandler;

/**
 * @author Shawn Lassiter
 * 
 */
public class AmoebaMessageListener implements MessageListener, PacketListener {

    @Override
    public void processMessage(Chat chat, Message message) {
        try {
            chat.sendMessage(message.getBody());
        } catch (XMPPException e) {
            AmoebaExceptionHandler.handle(e);
        }
    }

    @Override
    public void processPacket(Packet packet) {
        GraphExtension graphPacket = (GraphExtension) packet.getExtension(Consts.GRAPH_TAG, Consts.AMOEBA_NS);
        if (graphPacket != null) {
            StorageGraph graph = graphPacket.getStorageGraph();
            if (graph != null) {
                DataFinder.get_ConnectionSource().incomingGraph(graph, packet.getFrom());
            }
        }
    }
}
