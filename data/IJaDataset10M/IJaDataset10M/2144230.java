package edu.upc.lsi.kemlg.aws;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import edu.upc.lsi.kemlg.aws.communication.AgentTransportLocal;
import edu.upc.lsi.kemlg.aws.communication.AgentTransportWS;
import edu.upc.lsi.kemlg.aws.input.domain.AgentInfo;
import edu.upc.lsi.kemlg.aws.input.domain.AgentList;
import edu.upc.lsi.kemlg.aws.input.domain.AgentScenario;
import edu.upc.lsi.kemlg.aws.input.domain.AgentTypeInfo;
import edu.upc.lsi.kemlg.aws.input.domain.AgentTypes;
import edu.upc.lsi.kemlg.aws.io.InputProviderImpl;
import edu.upc.lsi.kemlg.aws.reasoning.behaviour.Behaviour;
import edu.upc.lsi.kemlg.aws.communication.protocol.Protocol;
import edu.upc.lsi.kemlg.aws.stubs.AgentEndpoint.AgentEndpointService;
import edu.upc.lsi.kemlg.aws.utils.AgentLogger;

/**
 *
 * @author sergio
 */
@WebService()
public class AgentFactory {

    protected static HashMap<String, W3CEndpointReference> singleton;

    static {
        singleton = new HashMap<String, W3CEndpointReference>();
    }

    /**
	 *
	 * the createAgent method without complex type parameters is going to repalce the old one that had complex types. 
	 *
	 * Here an agent is created with 3 protocols and 3 behaviours
	 * each protocol represents a subset of all the Simple Contract Creation without Notary
	 * each behaviour handles the different steps of the GUI, such as (still to be connected):
	 * - the activation of the accept and rejects buttons of UPC GUI (UpcPhdStudentBehaviour)
	 * - the rejection contract status of the CTU GUI (DismissBehaviuor)
	 * - the execution of the contract and the passing of the DownloadClientResults WS
	 * data/ status for the GUI Are thought to be got through the getOutputFromAgent(W3CEndpointReference ref,  
	 * String stKey) new methos
	 */
    @SuppressWarnings("deprecation")
    @WebMethod
    public synchronized W3CEndpointReference createAgent(String name) {
        AgentEndpoint ae;
        AgentEndpointService aes;
        edu.upc.lsi.kemlg.aws.stubs.AgentEndpoint.AgentEndpoint aeStub;
        aes = new AgentEndpointService();
        if (AgentFactory.singleton.get(name) != null) {
            AgentLogger.log("Previous instance detected! ");
            try {
                aeStub = aes.getPort(singleton.get(name), edu.upc.lsi.kemlg.aws.stubs.AgentEndpoint.AgentEndpoint.class);
                aeStub.getAgentId();
                return AgentFactory.singleton.get(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            aeStub = aes.getAgentEndpointPort();
            aeStub.getAgentId();
        } catch (Exception e) {
        }
        ae = createAgentEndpoint(name);
        ae.setTransport(new AgentTransportWS());
        AgentFactory.singleton.put(name, AgentEndpoint.manager.export(W3CEndpointReference.class, "http://localhost:8080/AgentWebServiceLib/AgentEndpointService", ae));
        return AgentFactory.singleton.get(name);
    }

    public AgentEndpoint createLocalAgent(String name, Set<Protocol> sp, Set<Behaviour> sb, Set<InputProviderImpl> ips) {
        AgentEndpoint ae;
        ae = createAgentEndpoint(name);
        ae.setTransport(new AgentTransportLocal());
        return ae;
    }

    private AgentEndpoint createAgentEndpoint(String name) {
        AgentEndpoint ae;
        Set<Protocol> sp;
        Set<Behaviour> sb;
        InputStream is;
        AgentScenario as;
        AgentList al;
        AgentTypes atl;
        Iterator<AgentInfo> itAi;
        Iterator<AgentTypeInfo> itAti;
        AgentInfo aii;
        AgentTypeInfo ati;
        boolean bFound;
        Iterator<edu.upc.lsi.kemlg.aws.input.domain.Behaviour> itB;
        Iterator<edu.upc.lsi.kemlg.aws.input.domain.Protocol> itP;
        sp = null;
        sb = null;
        AgentLogger.log("createAgent: " + name);
        sp = new TreeSet<Protocol>();
        sb = new TreeSet<Behaviour>();
        is = AgentParser.class.getResourceAsStream("agents.xml");
        try {
            AgentLogger.log("Parsing agents.xml...");
            AgentLogger.log("agents.xml is " + is.available() + " bytes long.");
            as = AgentParser.getAgentScenarioFromXml(is);
            al = as.getAgentList();
            atl = as.getAgentTypes();
            itAi = al.getAgentInfo().iterator();
            itAti = atl.getAgentTypeInfo().iterator();
            bFound = false;
            aii = null;
            while (itAi.hasNext() && !bFound) {
                aii = itAi.next();
                bFound = aii.getAgentId().equals(name);
            }
            bFound = false;
            ati = null;
            while (itAti.hasNext() && !bFound) {
                ati = itAti.next();
                bFound = ati.getType().equals(aii.getType());
            }
            itB = ati.getBehaviourList().getBehaviour().iterator();
            itP = ati.getProtocolList().getProtocol().iterator();
            while (itB.hasNext()) {
                sb.add((Behaviour) this.getClass().getClassLoader().loadClass(itB.next().getClassname()).newInstance());
            }
            while (itP.hasNext()) {
                sp.add((Protocol) this.getClass().getClassLoader().loadClass(itP.next().getClassname()).newInstance());
            }
            AgentLogger.log("Parsing finished.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ae = new AgentEndpoint();
        ae.agentId = name;
        ae.addBehaviourPatterns(sb);
        ae.addProtocols(sp);
        return ae;
    }
}
