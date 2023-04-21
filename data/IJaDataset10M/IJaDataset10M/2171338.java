package org.eucontract.agents;

import com.sun.xml.ws.developer.Stateful;
import com.sun.xml.ws.developer.StatefulWebServiceManager;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.jws.WebService;
import javax.xml.ws.soap.Addressing;
import javax.xml.bind.annotation.XmlSeeAlso;
import org.eucontract.agents.communication.AgentTransportLocal;
import org.eucontract.agents.engine.ControlLoop;
import org.eucontract.agents.engine.AgentKernel;
import org.eucontract.agents.io.InputProviderListener;
import org.eucontract.agents.io.Percept;
import org.eucontract.agents.io.SessionId;
import org.eucontract.agents.knowledge.ontology.OntologyConcept;
import org.eucontract.agents.reasoning.behaviour.Behaviour;
import org.eucontract.agents.utils.AgentLogger;
import org.eucontract.agents.communication.AgentTransport;
import org.eucontract.agents.communication.performative.AcceptProposal;
import org.eucontract.agents.communication.performative.Agree;
import org.eucontract.agents.communication.performative.Confirm;
import org.eucontract.agents.communication.performative.ConsentSuggestion;
import org.eucontract.agents.communication.performative.DismissSuggestion;
import org.eucontract.agents.communication.performative.Failure;
import org.eucontract.agents.communication.performative.Inform;
import org.eucontract.agents.communication.performative.InformDone;
import org.eucontract.agents.communication.performative.Propose;
import org.eucontract.agents.communication.performative.Request;
import org.eucontract.agents.communication.performative.Suggest;
import org.eucontract.agents.communication.protocol.Protocol;
import org.eucontract.agents.knowledge.bdi.BeliefBase;

@Stateful
@WebService
@Addressing
@XmlSeeAlso({ OntologyConcept.class })
public class AgentEndpoint implements InputProviderListener {

    public static StatefulWebServiceManager<AgentEndpoint> manager;

    protected String agentId;

    protected Set<Protocol> ps;

    private Set<Percept> bdPercepts;

    private SortedSet<Behaviour> tsBehaviour;

    private AgentKernel mp;

    private AgentTransport at;

    private BeliefBase bb;

    public AgentEndpoint() {
        ControlLoop cl;
        AgentLogger.log("Initiating new agent...");
        agentId = "AgentEndpoint";
        at = new AgentTransportLocal();
        ps = new LinkedHashSet<Protocol>();
        tsBehaviour = new TreeSet<Behaviour>();
        bb = new BeliefBase();
        mp = new AgentKernel(this, at, tsBehaviour, ps, bb);
        bdPercepts = new TreeSet<Percept>();
        cl = new ControlLoop(mp, bdPercepts);
        new Thread(cl).start();
    }

    protected void setTransport(AgentTransport at) {
        this.at = at;
    }

    public void addProtocol(Protocol p) {
        AgentLogger.log("Asignando protocolo: " + p.getName());
        ps.add(p);
    }

    public void addProtocols(Set<Protocol> sp) {
        AgentLogger.log("Asignando protocolos: " + sp);
        ps.addAll(sp);
    }

    public String getAgentType() {
        return "Agent";
    }

    public String getXml() throws java.rmi.RemoteException {
        return "<AgentEndpoint>" + getAgentType() + "</AgentEndpoint>";
    }

    /**
	 * Getter of the property <tt>agentId</tt>
	 * 
	 * @return Returns the agentId.
	 * @uml.property name="agentId"
	 */
    public String getAgentId() {
        return agentId;
    }

    public synchronized boolean receiveInputs(SessionId sid, Set<Percept> p) {
        if (p != null) {
            bdPercepts.addAll(p);
        }
        return true;
    }

    public synchronized boolean receiveSimpleInput(String stInput) {
        SimplePercept sp;
        sp = new SimplePercept(stInput);
        bdPercepts.add(sp);
        return true;
    }

    public synchronized String obtainSimpleOutput(String stKey) {
        return bb.get(stKey);
    }

    protected void setName(String name) {
        this.agentId = name;
    }

    protected void addBehaviourPatterns(Set<Behaviour> tsBehaviour) {
        this.tsBehaviour.addAll(tsBehaviour);
    }

    public Object dismissSuggestion(String stIdDialog, String stInReplyTo, String sender, String receiver) {
        AgentLogger.log("DismissSuggestion!");
        return mp.putPerformative(new DismissSuggestion(stIdDialog, stInReplyTo, sender, receiver));
    }

    /**
	 * @throws InterruptedException
	 */
    public Object informDone(String idDialog, String inReplyTo, String sender, String receiver) throws java.rmi.RemoteException {
        AgentLogger.log("InformDone!");
        return mp.putPerformative(new InformDone(idDialog, inReplyTo, sender, receiver));
    }

    /**
	 */
    public Object inform(String idDialog, String inReplyTo, String sender, String receiver, Object content) {
        AgentLogger.log("Inform!");
        return mp.putPerformative(new Inform(idDialog, inReplyTo, sender, receiver, (OntologyConcept) content));
    }

    /**
	 */
    public Object consentSuggestion(String idDialog, String inReplyTo, String sender, String receiver) throws java.rmi.RemoteException {
        AgentLogger.log("ConsentSuggestion!");
        return mp.putPerformative(new ConsentSuggestion(idDialog, inReplyTo, sender, receiver));
    }

    /**
	 */
    public Object confirm(String idDialog, String inReplyTo, String sender, String receiver, Object content) throws java.rmi.RemoteException {
        AgentLogger.log("Confirm!");
        return mp.putPerformative(new Confirm(idDialog, inReplyTo, sender, receiver, (OntologyConcept) content));
    }

    /**
	 */
    public Object agree(String idDialog, String inReplyTo, String sender, String receiver) throws java.rmi.RemoteException {
        AgentLogger.log("Agree!");
        return mp.putPerformative(new Agree(idDialog, inReplyTo, sender, receiver));
    }

    /**
	 */
    public Object acceptProposal(String idDialog, String inReplyTo, String sender, String receiver) throws java.rmi.RemoteException {
        AgentLogger.log("AcceptProposal!");
        return mp.putPerformative(new AcceptProposal(idDialog, inReplyTo, sender, receiver));
    }

    public Object suggest(String idDialog, String inReplyTo, String sender, String receiver, Object content) {
        AgentLogger.log("Suggest!");
        return mp.putPerformative(new Suggest(idDialog, inReplyTo, sender, receiver, (OntologyConcept) content));
    }

    public Object request(String idDialog, String inReplyTo, String sender, String receiver, Object content) {
        AgentLogger.log("Request!");
        return mp.putPerformative(new Request(idDialog, inReplyTo, sender, receiver, (OntologyConcept) content));
    }

    public Object propose(String idDialog, String stInReplyTo, String sender, String receiver, Object content) {
        AgentLogger.log("Propose!");
        return mp.putPerformative(new Propose(idDialog, stInReplyTo, sender, receiver, (OntologyConcept) content));
    }

    public void failure(String stMessage) throws java.rmi.RemoteException {
        mp.putPerformative(new Failure(stMessage));
    }

    public void refuse(String stMessage) {
    }
}
