package net.sf.istcontract.aws.communication;

import javax.xml.bind.JAXBException;
import net.sf.istcontract.aws.AgentEndpoint;
import net.sf.istcontract.aws.AgentFactory;
import net.sf.istcontract.aws.communication.message.Message;
import net.sf.istcontract.aws.communication.performative.Agree;
import net.sf.istcontract.aws.communication.performative.ConsentSuggestion;
import net.sf.istcontract.aws.communication.performative.DismissSuggestion;
import net.sf.istcontract.aws.communication.performative.Failure;
import net.sf.istcontract.aws.communication.performative.Inform;
import net.sf.istcontract.aws.communication.performative.InformDone;
import net.sf.istcontract.aws.communication.performative.Propose;
import net.sf.istcontract.aws.communication.performative.Query;
import net.sf.istcontract.aws.communication.performative.Refuse;
import net.sf.istcontract.aws.communication.performative.Request;
import net.sf.istcontract.aws.communication.performative.Subscribe;
import net.sf.istcontract.aws.communication.performative.Suggest;
import net.sf.istcontract.aws.communication.performative.UnSubscribe;
import net.sf.istcontract.aws.communication.performative.NotUnderstood;
import net.sf.istcontract.aws.configuration.InvalidAgentConfiguration;
import net.sf.istcontract.aws.exception.AgentTransportException;
import net.sf.istcontract.aws.knowledge.ontology.OntologyConcept;
import net.sf.istcontract.aws.utils.AgentLogger;
import net.sf.istcontract.aws.utils.MessageIDGenerator;

/**
 * 
 * 
 * @author sergio
 *
 * @since 0.2a
 */
public class AgentTransportLocal implements AgentTransport {

    public void sendMessage(Message m) throws AgentTransportException {
        AgentEndpoint ae;
        AgentFactory af;
        String stIdDialog, stInReplyTo, sender, receiver;
        sender = m.getSender();
        receiver = m.getReceiver();
        stIdDialog = m.getIdDialog();
        stInReplyTo = m.getInReplyTo();
        AgentLogger.log("sender is " + sender);
        AgentLogger.log("receiver is " + receiver);
        ae = AgentRegistry.getAgent(receiver);
        if (ae == null) {
            af = new AgentFactory();
            try {
                ae = af.createLocalAgent(receiver);
            } catch (InvalidAgentConfiguration e) {
                throw new AgentTransportException(e.getMessage(), e);
            }
        }
        AgentLogger.log("AgentEndpoint of receiver: " + ae + " (" + ae.getAgentId() + ")");
        try {
            if (m.getPerformative().equals(ConsentSuggestion.class) || (m.getPerformative().getGenericSuperclass()).equals(ConsentSuggestion.class)) {
                String content = null;
                if (m.getContent() != null) {
                    content = ((OntologyConcept) m.getContent()).marshall();
                }
                ae.consentSuggestion(MessageIDGenerator.getMessageID(), stIdDialog, stInReplyTo, sender, receiver, content, m.getProtocolName(), m.getReplyWith());
            } else if (m.getPerformative().equals(InformDone.class) || (m.getPerformative().getGenericSuperclass()).equals(InformDone.class)) {
                String content = null;
                if (m.getContent() != null) {
                    content = ((OntologyConcept) m.getContent()).marshall();
                }
                ae.informDone(MessageIDGenerator.getMessageID(), stIdDialog, stInReplyTo, sender, receiver, content, m.getProtocolName(), m.getReplyWith());
            } else if (m.getPerformative().equals(Inform.class) || (m.getPerformative().getGenericSuperclass()).equals(Inform.class)) {
                String content = null;
                if (m.getContent() != null) {
                    content = ((OntologyConcept) m.getContent()).marshall();
                }
                ae.inform(MessageIDGenerator.getMessageID(), stIdDialog, stInReplyTo, sender, receiver, content, m.getProtocolName(), m.getReplyWith());
            } else if (m.getPerformative().equals(Suggest.class) || (m.getPerformative().getGenericSuperclass()).equals(Suggest.class)) {
                String content = null;
                if (m.getContent() != null) {
                    content = ((OntologyConcept) m.getContent()).marshall();
                }
                ae.suggest(MessageIDGenerator.getMessageID(), stIdDialog, stInReplyTo, sender, receiver, content, m.getProtocolName(), m.getReplyWith());
            } else if (m.getPerformative().equals(DismissSuggestion.class) || (m.getPerformative().getGenericSuperclass()).equals(DismissSuggestion.class)) {
                String content = null;
                if (m.getContent() != null) {
                    content = ((OntologyConcept) m.getContent()).marshall();
                }
                ae.dismissSuggestion(MessageIDGenerator.getMessageID(), stIdDialog, stInReplyTo, sender, receiver, content, m.getProtocolName(), m.getReplyWith());
            } else if (m.getPerformative().equals(Agree.class) || (m.getPerformative().getGenericSuperclass()).equals(Agree.class)) {
                String content = null;
                if (m.getContent() != null) {
                    content = ((OntologyConcept) m.getContent()).marshall();
                }
                ae.agree(MessageIDGenerator.getMessageID(), stIdDialog, stInReplyTo, sender, receiver, content, m.getProtocolName(), m.getReplyWith());
            } else if (m.getPerformative().equals(Request.class) || (m.getPerformative().getGenericSuperclass()).equals(Request.class)) {
                String content = null;
                if (m.getContent() != null) {
                    content = ((OntologyConcept) m.getContent()).marshall();
                }
                ae.request(MessageIDGenerator.getMessageID(), stIdDialog, stInReplyTo, sender, receiver, content, m.getProtocolName(), m.getReplyWith());
            } else if (m.getPerformative().equals(Propose.class) || (m.getPerformative().getGenericSuperclass()).equals(Propose.class)) {
                String content = null;
                if (m.getContent() != null) {
                    content = ((OntologyConcept) m.getContent()).marshall();
                }
                ae.propose(MessageIDGenerator.getMessageID(), stIdDialog, stInReplyTo, sender, receiver, content, m.getProtocolName(), m.getReplyWith());
            } else if (m.getPerformative().equals(Refuse.class) || (m.getPerformative().getGenericSuperclass()).equals(Refuse.class)) {
                String content = null;
                if (m.getContent() != null) {
                    content = ((OntologyConcept) m.getContent()).marshall();
                }
                ae.refuse(MessageIDGenerator.getMessageID(), stIdDialog, stInReplyTo, sender, receiver, content, m.getProtocolName(), m.getReplyWith());
            } else if (m.getPerformative().equals(Subscribe.class) || (m.getPerformative().getGenericSuperclass()).equals(Subscribe.class)) {
                String content = null;
                if (m.getContent() != null) {
                    content = ((OntologyConcept) m.getContent()).marshall();
                }
                ae.subscribe(MessageIDGenerator.getMessageID(), stIdDialog, stInReplyTo, sender, receiver, content, m.getProtocolName(), m.getReplyWith());
            } else if (m.getPerformative().equals(UnSubscribe.class) || (m.getPerformative().getGenericSuperclass()).equals(UnSubscribe.class)) {
                String content = null;
                if (m.getContent() != null) {
                    content = ((OntologyConcept) m.getContent()).marshall();
                }
                ae.unsubscribe(MessageIDGenerator.getMessageID(), stIdDialog, stInReplyTo, sender, receiver, content, m.getProtocolName(), m.getReplyWith());
            } else if (m.getPerformative().equals(Failure.class) || (m.getPerformative().getGenericSuperclass()).equals(Failure.class)) {
                String content = null;
                if (m.getContent() != null) {
                    content = ((OntologyConcept) m.getContent()).marshall();
                }
                ae.failure(MessageIDGenerator.getMessageID(), stIdDialog, stInReplyTo, sender, receiver, content, m.getProtocolName(), m.getReplyWith());
            } else if (m.getPerformative().equals(Query.class) || (m.getPerformative().getGenericSuperclass()).equals(Query.class)) {
                String content = null;
                if (m.getContent() != null) {
                    content = ((OntologyConcept) m.getContent()).marshall();
                }
                ae.query(MessageIDGenerator.getMessageID(), stIdDialog, stInReplyTo, sender, receiver, content, m.getProtocolName(), m.getReplyWith());
            } else {
                AgentLogger.log("Unexcpected performative - cannot send locally: " + m.getPerformative().getName());
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
