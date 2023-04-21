package org.labrad.browser.client;

import java.util.List;
import org.labrad.browser.client.event.NodeRequestFailedException;
import org.labrad.browser.client.event.NodeStatusEvent;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("node")
public interface NodeService extends RemoteService {

    public List<NodeStatusEvent> getNodeInfo();

    public String refreshServers(String node);

    public String startServer(String node, String server) throws NodeRequestFailedException;

    public String stopServer(String node, String server) throws NodeRequestFailedException;

    public String restartServer(String node, String server) throws NodeRequestFailedException;
}
