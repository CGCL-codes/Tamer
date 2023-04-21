package edu.upc.lsi.kemlg.aws.agentshell.messaging;

import edu.upc.lsi.kemlg.aws.agentshell.components.ContractTemplate;
import edu.upc.lsi.kemlg.aws.agentshell.domain.ContractList;
import edu.upc.lsi.kemlg.aws.agentshell.domain.Event;
import edu.upc.lsi.kemlg.aws.agentshell.domain.Fact;
import edu.upc.lsi.kemlg.aws.agentshell.interfaces.IMessageManager;
import edu.upc.lsi.kemlg.aws.communication.message.Message;

public class AsyncMessageManager extends AgentShellComponent implements IMessageManager {

    public AsyncMessageManager(IMessageManager mm) {
        implementation = mm;
    }

    public Message decodeMessage(Message message, String ontologyName) {
        AsyncHandler ah;
        ah = addMessage("decodeMessage", message, ontologyName);
        try {
            synchronized (ah) {
                ah.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (Message) ah.getResult();
    }

    public Message encodeMessage(Message message, String ontologyName) {
        AsyncHandler ah;
        ah = addMessage("encodeMessage", message, ontologyName);
        try {
            synchronized (ah) {
                ah.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (Message) ah.getResult();
    }

    public void getContractOntology(ContractTemplate ct) {
        addMessage("getContractOntology", ct);
    }

    public void initMessageManager(ContractList cl) {
        addMessage("initMessageManager", cl);
    }

    public void notifyExternalEvent(Event event) {
        addMessage("notifyExternalEvent", event);
    }

    public Fact queryRDF(String rdfQuery, String OntologyRef) {
        AsyncHandler ah;
        ah = addMessage("queryRDF", rdfQuery, OntologyRef);
        try {
            synchronized (ah) {
                ah.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (Fact) ah.getResult();
    }

    public void subscribe(String subscriberModuleId, Event event) {
        addMessage("subscribe", subscriberModuleId, event);
    }
}
