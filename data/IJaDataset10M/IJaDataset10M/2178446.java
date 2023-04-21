package edu.psu.its.lionshare.peerserver;

import com.limegroup.gnutella.settings.ConnectionSettings;
import edu.psu.its.lionshare.share.gnutella.LionShareMessageRouter;
import edu.psu.its.lionshare.peerserver.settings.PeerserverProperties;
import java.net.InetAddress;

public class RouterService {

    private static RouterService instance = null;

    private byte[] byte_address = null;

    private String ip_address = null;

    private ShareFileManager filemanager = null;

    private LionShareMessageRouter router = null;

    public RouterService() {
        ConnectionSettings.DISABLE_UPNP.setValue(true);
        int port = PeerserverProperties.getInstance().getNonSecurePeerserverPort();
        int secure_port = PeerserverProperties.getInstance().getPeerserverSecurePort();
        router = new LionShareMessageRouter(port, secure_port);
        filemanager = new ShareFileManager();
        try {
            byte_address = InetAddress.getLocalHost().getAddress();
            ip_address = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
        }
    }

    public void start() {
        filemanager.start();
    }

    public static RouterService getInstance() {
        if (instance == null) {
            instance = new RouterService();
        }
        return instance;
    }

    public ShareFileManager getFileManager() {
        return filemanager;
    }

    public byte[] getAddress() {
        return byte_address;
    }

    public String getIPAddress() {
        return ip_address;
    }

    public int getPort() {
        return 6346;
    }

    public LionShareMessageRouter getMessageRouter() {
        return router;
    }
}
