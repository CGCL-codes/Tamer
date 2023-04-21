package slash.dsm.client;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.io.Serializable;
import slash.dsm.tuple.Tuple;

public class DsmClient implements Serializable {

    private Agent agent;

    private AID dsm;

    public DsmClient(Agent agent) {
        this.agent = agent;
        dsm = new AID("dsm", AID.ISLOCALNAME);
    }

    public int out(String index, String type, Object value) {
        try {
            if (!index.matches("\\D*")) index = index.replaceAll("\\D*", "");
            Tuple tuple = new Tuple(Tuple.OUT, type, index, value);
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(dsm);
            msg.setLanguage("English");
            msg.setConversationId("dsm");
            msg.setContentObject(tuple);
            agent.send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int update(String index, String type, Object value) {
        try {
            if (!index.matches("\\D*")) index = index.replaceAll("\\D*", "");
            Tuple tuple = new Tuple(Tuple.UPDATE, type, index, value);
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(dsm);
            msg.setLanguage("English");
            msg.setConversationId("dsm");
            msg.setContentObject(tuple);
            agent.send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Tuple read(String index, String type) {
        try {
            if (!index.matches("\\D*")) index = index.replaceAll("\\D*", "");
            Tuple tuple = new Tuple(Tuple.READ, type, index, null);
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(dsm);
            msg.setLanguage("English");
            msg.setConversationId("dsm");
            msg.setContentObject(tuple);
            agent.send(msg);
            ACLMessage recvMsg = agent.blockingReceive();
            if (recvMsg != null) {
                Object obj = recvMsg.getContentObject();
                if (obj != null) {
                    return ((Tuple) obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Tuple in(String index, String type) {
        try {
            if (!index.matches("\\D*")) index = index.replaceAll("\\D*", "");
            Tuple tuple = new Tuple(Tuple.IN, type, index, null);
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(dsm);
            msg.setLanguage("English");
            msg.setConversationId("dsm");
            msg.setContentObject(tuple);
            agent.send(msg);
            ACLMessage recvMsg = agent.blockingReceive();
            if (recvMsg != null) {
                Object obj = recvMsg.getContentObject();
                if (obj != null) {
                    return ((Tuple) obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
