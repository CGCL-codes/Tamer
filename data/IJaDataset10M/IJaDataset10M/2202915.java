package gridrm.agents.master;

import static gridrm.core.GridAgentConstants.MASTER_VERIFY;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

/**
 *
 * @author noxis
 */
public class VerifyMasterHandler extends CyclicBehaviour {

    private Logger log = Logger.getMyLogger(getClass().getName());

    private MasterAgent myAgent;

    private MessageTemplate tmpl = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchConversationId(MASTER_VERIFY));

    public VerifyMasterHandler(Agent a) {
        super(a);
        myAgent = (MasterAgent) a;
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(tmpl);
        if (msg == null) {
            block();
            return;
        }
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        myAgent.send(reply);
    }
}
