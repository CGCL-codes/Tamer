package net.sf.istcontract.aws.utils;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import net.sf.istcontract.aws.stubs.AgentEndpoint.AgentEndpoint;
import net.sf.istcontract.aws.stubs.AgentEndpoint.AgentEndpointService;
import net.sf.istcontract.aws.stubs.AgentFactory.AgentFactory;
import net.sf.istcontract.aws.stubs.AgentFactory.AgentFactoryService;

/**
 * @deprecated
 * @author sergio
 *
 */
public class RemoteAgentFactory {

    public static synchronized AgentEndpoint createRemoteAgent(String ip, String port, String agentId) throws MalformedURLException {
        AgentFactory af;
        AgentEndpoint ae;
        AgentFactoryService afs;
        AgentEndpointService aes;
        afs = new AgentFactoryService(new URL("http://" + ip + ":" + port + "/AgentWebServiceLib/" + "AgentFactoryService?wsdl"), new QName("http://aws.istcontract.sf.net/", "AgentFactoryService"));
        af = afs.getAgentFactoryPort();
        aes = new AgentEndpointService(new URL("http://" + ip + ":" + port + "/AgentWebServiceLib/" + "AgentEndpointService?wsdl"), new QName("http://aws.istcontract.sf.net/", "AgentEndpointService"));
        ae = aes.getPort(af.createAgent(agentId), AgentEndpoint.class);
        ae = aes.getPort(af.createAgent(agentId), AgentEndpoint.class);
        return ae;
    }
}
