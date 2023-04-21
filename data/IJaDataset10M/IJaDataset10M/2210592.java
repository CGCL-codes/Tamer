package nomads.agents.leader;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import nomads.agents.AbstractNomadAgent;
import nomads.agents.BehaviourPlugin;
import nomads.onto.action.LetsGo;
import nomads.onto.action.StartWalking;
import nomads.onto.concept.Road;
import nomads.util.ConditionalSeqBehaviour;

/**
 * //TODO: O scurta descriere a clasei
 * 
 * @author Victor
 * 
 */
public class LeaderProposeWalkB extends BehaviourPlugin {

    public static final String confirm_protocol = "CONFIRM_WALK";

    public LeaderProposeWalkB(AbstractNomadAgent myAgent) {
        super(myAgent);
    }

    public Behaviour load() {
        final int totalAgents = myNomad.getTotalAgentsCount();
        SequentialBehaviour seq = new ConditionalSeqBehaviour(myNomad);
        seq.addSubBehaviour(new OneShotBehaviour(myNomad) {

            public void action() {
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                myNomad.setContent(msg, new LetsGo());
                myNomad.addReceiversAllMeToo(msg);
                msg.setProtocol(confirm_protocol);
                myNomad.send(msg);
            }
        });
        seq.addSubBehaviour(new SimpleBehaviour(myNomad) {

            int receivedResponses = 0;

            boolean ok = true;

            public boolean done() {
                return receivedResponses == totalAgents;
            }

            public void action() {
                ACLMessage msg = myNomad.receive(MessageTemplate.and(MessageTemplate.MatchProtocol(confirm_protocol), MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchPerformative(ACLMessage.FAILURE))));
                if (msg == null) {
                    block();
                } else {
                    receivedResponses++;
                    ok &= msg.getPerformative() == ACLMessage.INFORM;
                    if (msg.getPerformative() != ACLMessage.INFORM) {
                    }
                }
            }

            public int onEnd() {
                return ok ? 0 : LeaderWalkB.STOP_REFUSE_START;
            }
        });
        seq.addSubBehaviour(new OneShotBehaviour(myNomad) {

            public void action() {
                myNomad.say("Ce bine! toti sunteti de acord sa incepem sa ne deplasam!");
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                myNomad.setContent(msg, new StartWalking(new Road(((Leader) myNomad).roadToWalk.getId())));
                myNomad.addReceiversAllMeToo(msg);
                myNomad.send(msg);
            }
        });
        return seq;
    }
}
