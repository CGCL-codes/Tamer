package pubweb.supernode;

import java.net.MalformedURLException;
import java.net.URL;
import pubweb.IntegrityException;
import pubweb.util.GuidGenerator;

public class StaticSupernodeConnector implements SupernodeConnector {

    private URL supernodeUrl;

    public void connect() throws IntegrityException {
        try {
            supernodeUrl = new URL("pp://" + System.getProperty("pubweb.username") + ":" + System.getProperty("pubweb.password") + "@" + System.getProperty("pubweb.supernode.host") + ":" + System.getProperty("pubweb.supernode.port") + "/" + GuidGenerator.getGuid(System.getProperty("pubweb.supernode.host"), System.getProperty("pubweb.supernode.port"), GuidGenerator.PeerType.SUPERNODE));
        } catch (MalformedURLException e) {
            throw new IntegrityException("error creating supernode URL: invalid invalid supernode host, port, username, or password", e);
        }
    }

    public URL getSupernodeUrl() {
        return supernodeUrl;
    }
}
