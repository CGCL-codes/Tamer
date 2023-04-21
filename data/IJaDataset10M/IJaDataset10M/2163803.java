package up2p.peer.gnutella.test;

import com.echomine.gnutella.GnutellaConnection;
import com.echomine.gnutella.GnutellaStatistics;
import com.echomine.gnutella.GnutellaStatisticsListener;

public class DefaultStatListener implements GnutellaStatisticsListener {

    public void connectionStatsUpdated(GnutellaConnection conn) {
        if (conn == null) return;
        System.out.println("[Connection " + conn.getConnectionModel() + "]Msgs: " + conn.getMessages() + ", hosts: " + conn.getHosts() + ", Files: " + conn.getFiles() + ", Size: " + conn.getSize() + "\r");
    }

    public void globalStatsUpdated(GnutellaStatistics stats) {
    }
}
