package edu.upc.lsi.kemlg.aws.agentshell.messaging;

import edu.upc.lsi.kemlg.aws.AgentEndpoint;
import edu.upc.lsi.kemlg.aws.agentshell.interfaces.IDialogueManager;
import edu.upc.lsi.kemlg.aws.communication.message.Message;
import edu.upc.lsi.kemlg.aws.communication.message.MessageTemplate;
import edu.upc.lsi.kemlg.aws.communication.performative.Performative;
import edu.upc.lsi.kemlg.aws.communication.protocol.ConversationInitiator;
import edu.upc.lsi.kemlg.aws.communication.protocol.Protocol;
import edu.upc.lsi.kemlg.aws.communication.protocol.ProtocolInstance;
import edu.upc.lsi.kemlg.aws.knowledge.ontology.OntologyConcept;

public class AsyncDialogueManager extends AgentShellComponent implements IDialogueManager {

    public AsyncDialogueManager(IDialogueManager dm) {
        implementation = dm;
    }

    public void executeProtocol(Protocol p) {
        addMessage("executeProtocol", p);
    }

    public void initDialogueManager() {
        addMessage("initDialogueManager");
    }

    public void addProtocol(Protocol p) {
        addMessage("addProtocol", p);
    }

    public void executeProtocol(Protocol p, AgentEndpoint initiator, AgentEndpoint participant) {
        addMessage("executeProtocol", p, initiator, participant);
    }

    public Protocol[] getCompliantProtocols(MessageTemplate mt) {
        return ((IDialogueManager) implementation).getCompliantProtocols(mt);
    }

    public MessageTemplate[] getNextMessages() {
        return ((IDialogueManager) implementation).getNextMessages();
    }

    public Protocol[] getProtocols() {
        return ((IDialogueManager) implementation).getProtocols();
    }

    public ProtocolInstance[] getRunningDialogues() {
        return ((IDialogueManager) implementation).getRunningDialogues();
    }

    public void removeProtocol(Protocol p) {
        addMessage("removeProtocol", p);
    }

    public void update(Performative p) {
        addMessage("update", p);
    }

    public void executeProtocol(Protocol p, Class<? extends ConversationInitiator> initiator, String addreseeAgentID, OntologyConcept content) {
        addMessage("executeProtocol", p, initiator, addreseeAgentID, content);
    }

    public boolean existsConversation(Performative p) {
        return ((IDialogueManager) implementation).existsConversation(p);
    }

    public void addProtocol(Class<? extends Protocol> p) {
        addMessage("addProtocol", p);
    }

    public void removeProtocol(Class<? extends Protocol> p) {
        addMessage("removeProtocol", p);
    }
}
