package im.agents.slachecker;

import im.domain.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class GetMonitorData extends CyclicBehaviour {

    private SLA sla;

    private AID monitor;

    private int role;

    public GetMonitorData(Agent a, SLA sla, AID monitor, int role) {
        super(a);
        this.sla = sla;
        this.monitor = monitor;
        this.role = role;
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(getMessageTemplate());
        if (msg != null) {
            MonitorData monitored = (MonitorData) XMLSerializer.doDeserialize(msg.getContent());
            if (!getMyAgent().checkRespectedSLA(sla, monitored, role)) {
                System.out.println("Violazione! " + sla.getSubscriberB());
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.addReceiver(sla.getSubscriberA());
                reply.addReceiver(sla.getSubscriberB());
                reply.setContent("violazione");
                reply.setConversationId(sla.toString());
                getMyAgent().send(reply);
            }
        } else {
            block();
        }
    }

    private MessageTemplate getMessageTemplate() {
        return MessageTemplate.and(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchConversationId(sla.toString())), MessageTemplate.MatchSender(monitor));
    }

    private SLAChecker getMyAgent() {
        return (SLAChecker) myAgent;
    }
}
