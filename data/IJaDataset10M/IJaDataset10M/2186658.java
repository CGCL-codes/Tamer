package pubweb.worker;

import java.io.IOException;
import java.net.Socket;
import net.jxta.peergroup.PeerGroup;
import pubweb.IntegrityException;
import pubweb.util.JxtaProtocolLayer;
import aeolus.edge.AdvancedService;
import aeolus.edge.AdvancedServiceConnectionHandler;
import aeolus.edge.Service;
import aeolus.edge.ServiceImpl;
import aeolus.edge.Settings;

public class ComputingService extends ServiceImpl implements AdvancedServiceConnectionHandler {

    private AdvancedService service;

    private static PeerGroup netPeerGroup = null;

    public static PeerGroup getNetPeerGroup() {
        return netPeerGroup;
    }

    public ComputingService(PeerGroup netPeerGroup, Settings settings) throws IOException, IntegrityException {
        super(netPeerGroup, settings);
        ComputingService.netPeerGroup = netPeerGroup;
        new Worker("configs/pubweb-worker.conf");
        service = new AdvancedService("PUBWebW", "PUB-Web Computing Service", Integer.toString(JxtaProtocolLayer.VERSION), 10, this);
    }

    public Service getService() {
        return service;
    }

    public void newConnection(Socket socket) {
    }
}
