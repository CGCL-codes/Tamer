package edu.univalle.lingweb.agent.agents;

import jade.content.ContentManager;
import jade.content.abs.AbsAggregate;
import jade.content.abs.AbsPredicate;
import jade.content.abs.AbsTerm;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.introspection.AMSSubscriber;
import jade.domain.introspection.DeadAgent;
import jade.domain.introspection.Event;
import jade.domain.introspection.IntrospectionOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import edu.univalle.lingweb.agent.ontology.ChatOntology;

/**
   This agent maintains knowledge of agents currently attending the 
   chat and inform them when someone joins/leaves the chat.
   @author Giovanni Caire - TILAB
 */
public class ChatManagerAgent extends Agent implements SubscriptionManager {

    private static final long serialVersionUID = 1L;

    /**
	 * Manejador de mensajes Log's
	 */
    protected Logger log = Logger.getLogger(ChatManagerAgent.class);

    private Map participants = new HashMap();

    private Codec codec = new SLCodec();

    private Ontology chatOntology = ChatOntology.getInstance();

    private ContentManager cm = (ContentManager) getContentManager();

    private AMSSubscriber myAMSSubscriber;

    protected void setup() {
        cm.registerLanguage(codec);
        cm.registerOntology(chatOntology);
        MessageTemplate sTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE), MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()), MessageTemplate.MatchOntology(chatOntology.getName())));
        addBehaviour(new SubscriptionResponder(this, sTemplate, this));
        myAMSSubscriber = new AMSSubscriber() {

            private static final long serialVersionUID = 1L;

            protected void installHandlers(Map handlersTable) {
                handlersTable.put(IntrospectionOntology.DEADAGENT, new EventHandler() {

                    private static final long serialVersionUID = 1L;

                    public void handle(Event ev) {
                        DeadAgent da = (DeadAgent) ev;
                        AID id = da.getAgent();
                        if (participants.containsKey(id)) {
                            try {
                                deregister((Subscription) participants.get(id));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        };
        addBehaviour(myAMSSubscriber);
        DOMConfigurator.configure(ChatManagerAgent.class.getResource("/log4j.xml"));
    }

    protected void takeDown() {
        send(myAMSSubscriber.getCancel());
    }

    public boolean register(Subscription s) throws RefuseException, NotUnderstoodException {
        try {
            AID newId = s.getMessage().getSender();
            if (!participants.isEmpty()) {
                ACLMessage notif1 = s.getMessage().createReply();
                notif1.setPerformative(ACLMessage.INFORM);
                ACLMessage notif2 = (ACLMessage) notif1.clone();
                notif2.clearAllReceiver();
                AbsPredicate p = new AbsPredicate(ChatOntology.JOINED);
                AbsAggregate agg = new AbsAggregate(BasicOntology.SEQUENCE);
                agg.add((AbsTerm) BasicOntology.getInstance().fromObject(newId));
                p.set(ChatOntology.JOINED_WHO, agg);
                cm.fillContent(notif2, p);
                agg.clear();
                Iterator it = participants.keySet().iterator();
                while (it.hasNext()) {
                    AID oldId = (AID) it.next();
                    agg.add((AbsTerm) BasicOntology.getInstance().fromObject(oldId));
                    Subscription oldS = (Subscription) participants.get(oldId);
                    oldS.notify(notif2);
                }
                cm.fillContent(notif1, p);
                s.notify(notif1);
            }
            log.info("Adicionando el nuevo suscriptor: " + newId.getLocalName());
            participants.put(newId, s);
            log.info("longitud de la lista de particiantes: " + participants.size());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RefuseException("Subscription error");
        }
    }

    public boolean deregister(Subscription s) throws FailureException {
        AID oldId = s.getMessage().getSender();
        if (participants.remove(oldId) != null) {
            if (!participants.isEmpty()) {
                try {
                    ACLMessage notif = s.getMessage().createReply();
                    notif.setPerformative(ACLMessage.INFORM);
                    notif.clearAllReceiver();
                    AbsPredicate p = new AbsPredicate(ChatOntology.LEFT);
                    AbsAggregate agg = new AbsAggregate(BasicOntology.SEQUENCE);
                    agg.add((AbsTerm) BasicOntology.getInstance().fromObject(oldId));
                    p.set(ChatOntology.LEFT_WHO, agg);
                    cm.fillContent(notif, p);
                    Iterator it = participants.values().iterator();
                    while (it.hasNext()) {
                        Subscription s1 = (Subscription) it.next();
                        s1.notify(notif);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
