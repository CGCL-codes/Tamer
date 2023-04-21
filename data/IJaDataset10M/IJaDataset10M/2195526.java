package im.agents.slachecker;

import im.agents.utility.Utility;
import im.domain.Context;
import im.domain.SLA;
import im.domain.XMLSerializer;
import java.util.Vector;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetInitiator;

/**
 * Negozia la stipula di un SLA in base a parametri specificati dall'agente subscriber.
 * Implementa il protocollo <tt>FIPA CONTRACT NET</tt>. 
 * 
 * @author Savino
 * @see im.agents.service.NegotiateSLAResponder
 *
 */
public class NegotiateSLAInitiator extends ContractNetInitiator {

    private SLA sla;

    int accepted;

    AID user;

    /**
	 * 
	 * @param a myAgent
	 * @param cfp Messaggio da inviare come <tt>CALL FOR PROPOSALS</tt>
	 * @param sla Istanza di SLA inviata da agente che vuole stipulare agreement, contiene livelli da rispettare
	 * @param user AID del subscriber
	 */
    public NegotiateSLAInitiator(Agent a, ACLMessage cfp, SLA sla, AID user) {
        super(a, cfp);
        this.sla = sla;
        this.user = user;
    }

    /**
	 * Prima cerca sul DF gli agenti che forniscono il servizio passato al costruttore in <tt>sla</tt>.
	 * Poi prepara i messaggi "CALL FOR PROPOSALS" da inviare a tali agenti.
	 * @return Un <tt>Vector</tt> contenente un solo messaggio (destinatari: gli agenti trovati).
	 */
    public Vector prepareCfps(ACLMessage cfp) {
        Vector<ACLMessage> cfps = new Vector<ACLMessage>();
        Vector<AID> ids = discoverServiceProviders();
        cfp.setContent(XMLSerializer.doSerialize(sla));
        if (ids == null) return null;
        for (AID a : ids) {
            cfp.addReceiver(a);
        }
        cfps.add(cfp);
        return cfps;
    }

    /**
	 * Ad ogni <tt>PROPOSAL</tt> ricevuto, prepara la risposta, con performative 
	 * <tt>ACCEPT_PROPOSAL</tt> o <tt>REJECT_PROPOSAL</tt>, e la mette in 
	 * <tt>acceptances</tt>.
	 * <br/>
	 * <b>Non accetta pi� di una proposta.</b>
	 * @param propose messaggio di proposta
	 * @param acceptances <tt>Vector</tt> di messaggi di risposta
	 */
    @SuppressWarnings("unchecked")
    public void handlePropose(ACLMessage propose, Vector acceptances) {
        ACLMessage accept = propose.createReply();
        if (accepted == 0) {
            accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            accepted++;
            sla.setSubscriberB(propose.getSender());
        } else {
            accept.setPerformative(ACLMessage.REJECT_PROPOSAL);
        }
        acceptances.add(accept);
    }

    /**
	 * interroga il DF per trovare agenti che forniscano servizi indicati in sla
	 * @return
	 */
    private Vector<AID> discoverServiceProviders() {
        Vector<AID> providers = new Vector<AID>();
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription s = new ServiceDescription();
        s.setName(sla.getServizio());
        s.setType(sla.getServizio());
        dfd.addServices(s);
        try {
            DFAgentDescription[] dfds = DFService.search(myAgent, dfd);
            if (dfds != null) {
                for (DFAgentDescription d : dfds) {
                    providers.add(d.getName());
                }
            }
        } catch (FIPAException e) {
            e.printStackTrace();
            return null;
        }
        return providers;
    }

    /**
	 * Dopo aver risposto <tt>ACCEPT_PROPOSAL</tt>, il proponente risponde con <tt>INFORM</tt>.
	 * <br/>Questo metodo inoltra il messaggio all'agente che ha chiesto di stipulare l'SLA, 
	 * informandolo del successo della contrattazione, e dell'AID dell'agente con cui ha stipulato l'SLA.
	 * Inoltre si registra sui monitor di entrambe le parti e inizia a ricevere statistiche sulla conversazione,
	 * per verificare se l'SLA � rispettato.
	 */
    public void handleInform(ACLMessage inform) {
        inform.removeReceiver(myAgent.getAID());
        inform.addReceiver(user);
        sla.setSubscriberB(inform.getSender());
        String xmlSLA = XMLSerializer.doSerialize(sla);
        inform.setContent(xmlSLA);
        myAgent.send(inform);
        try {
            AID requesterMonitor = findSubscriberMonitor(sla.getSubscriberA());
            AID providerMonitor = findSubscriberMonitor(sla.getSubscriberB());
            ((SLAChecker) myAgent).getSubscribedSLAs().put(sla.toString(), providerMonitor);
            contactMonitor(requesterMonitor, SLAChecker.ROLE_REQUESTER);
            contactMonitor(providerMonitor, SLAChecker.ROLE_PROVIDER);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    private void contactMonitor(AID monitorAID, int role) throws FIPAException {
        String xmlSLA = XMLSerializer.doSerialize(sla);
        ACLMessage monitorReq = new ACLMessage(ACLMessage.QUERY_REF);
        monitorReq.addReceiver(monitorAID);
        monitorReq.setContent(xmlSLA);
        monitorReq.setSender(myAgent.getAID());
        myAgent.addBehaviour(new GetMonitorData(myAgent, sla, monitorAID, role));
        myAgent.addBehaviour(new ChangeMonitorAfterProviderMigration(myAgent, sla));
        myAgent.send(monitorReq);
    }

    public void reset() {
        super.reset();
        sla = null;
        accepted = 0;
        user = null;
    }

    public int onEnd() {
        reset();
        return 0;
    }

    private AID findSubscriberMonitor(AID subscriberAID) {
        AID monitorAID = null;
        Location subscriberLocation = null;
        DFAgentDescription monitor_ds = null;
        String convId = Utility.createConvId(myAgent);
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        request.setSender(myAgent.getAID());
        request.addReceiver(subscriberAID);
        request.setConversationId(convId);
        myAgent.send(request);
        boolean gotIt = false;
        while (!gotIt) {
            ACLMessage response = myAgent.receive(MessageTemplate.MatchConversationId(convId));
            if (response != null) {
                Context context = (Context) XMLSerializer.doDeserialize(response.getContent());
                subscriberLocation = context.getNodeId();
                gotIt = true;
            } else {
                block();
            }
        }
        monitor_ds = Utility.findAgent(myAgent, "monitor", subscriberLocation);
        monitorAID = monitor_ds.getName();
        return monitorAID;
    }
}
