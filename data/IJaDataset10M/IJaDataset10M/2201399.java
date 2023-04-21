package net.sf.istcontract.aws.reasoning.behaviour;

import net.sf.istcontract.aws.SimplePercept;
import net.sf.istcontract.aws.communication.message.MessageTemplate;
import net.sf.istcontract.aws.communication.performative.*;
import net.sf.istcontract.aws.communication.protocol.IConversationListener;
import net.sf.istcontract.aws.communication.protocol.MalformedMessageException;
import net.sf.istcontract.aws.communication.protocol.NoSuchConversationException;
import net.sf.istcontract.aws.communication.protocol.NoSuchProtocolException;
import net.sf.istcontract.aws.communication.protocol.ProtocolNode;
import net.sf.istcontract.aws.communication.protocol.ProtocolViolationException;
import net.sf.istcontract.aws.communication.protocol.Protocol_Interaction;
import net.sf.istcontract.aws.communication.protocol.Protocol_InteractionFailure_InteractionUnit;
import net.sf.istcontract.aws.communication.protocol.Protocol_InteractionListener;
import net.sf.istcontract.aws.communication.protocol.Protocol_InteractionObserver;
import net.sf.istcontract.aws.communication.protocol.VerySimpleProtocol;
import net.sf.istcontract.aws.communication.protocol.VerySimpleProtocolParticipant;
import net.sf.istcontract.aws.configuration.InvalidAgentConfiguration;
import net.sf.istcontract.aws.exception.AgentException;
import net.sf.istcontract.aws.input.domain.EBookContract;
import net.sf.istcontract.aws.knowledge.ontology.OntologyConcept;
import net.sf.istcontract.aws.utils.AgentLogger;
import net.sf.istcontract.aws.communication.protocol.*;
import net.sf.istcontract.aws.communication.performative.*;
import net.sf.istcontract.aws.communication.message.MessageTemplate;
import net.sf.istcontract.aws.communication.message.MessageFactory;
import net.sf.istcontract.aws.communication.message.Message;

;

public class IngeniasTestBehaviourObs extends Behaviour {

    @Override
    protected void runSimplePercept(IConversationListener comm) throws AgentException {
        SimplePercept sp = (SimplePercept) percept;
        AgentLogger.log("This is Observer receiving a simple percept: " + sp.getStInput());
        AgentLogger.log("But it is too lazy to do anything about it, try using the other agent");
    }

    @Override
    protected void runSubscribe(IConversationListener comm) throws AgentException {
        Performative receivedPerf = (Performative) percept;
        String currNode = "";
        try {
            comm.UpdateStateOfConversationReceivedMessage(receivedPerf, Subscribe.class, comm.buildIDAsParticipant(receivedPerf));
            try {
                currNode = comm.GetCurrentNodeName(comm.buildIDAsInitiator(receivedPerf));
            } catch (NoSuchProtocolException NSPE) {
                currNode = "Protocol_InteractionSuscribe_InteractionUnit";
            }
        } catch (Exception E) {
            String dummy = getStackTrace(E);
            AgentLogger.log(dummy);
        }
        if (currNode.equalsIgnoreCase("Protocol_InteractionSuscribe_InteractionUnit")) {
            follow_SubscriptionAgreeInformDesubsPath(receivedPerf, comm);
        }
    }

    @Override
    protected void runInform(IConversationListener comm) throws AgentException {
        try {
            Performative receivedPerf = (Performative) this.percept;
            comm.UpdateStateOfConversationReceivedMessage(receivedPerf, Inform.class, comm.buildIDAsParticipant(receivedPerf));
            String currNode = comm.GetCurrentNodeName(comm.buildIDAsParticipant(receivedPerf));
            if (currNode.equalsIgnoreCase("Protocol_InteractionUnSuscribe_InteractionUnit")) {
                AgentLogger.log("-------------> I am at node: '" + comm.GetCurrentNodeName(comm.buildIDAsParticipant(receivedPerf)) + "'");
                AgentLogger.log("-------------> Farewell Mr Desuscriptor, hope to see you soon!!");
            }
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public String getStackTrace(Throwable t) {
        java.io.StringWriter stringWritter = new java.io.StringWriter();
        java.io.PrintWriter printWritter = new java.io.PrintWriter(stringWritter, true);
        t.printStackTrace(printWritter);
        printWritter.flush();
        stringWritter.flush();
        return stringWritter.toString();
    }

    private void follow_SubscriptionAgreeInformFailurePath(Performative receivedPerf, IConversationListener comm) {
        MessageTemplate[] NextMsg;
        MessageTemplate choose;
        try {
            AgentLogger.log("Responding to subscription request");
            NextMsg = comm.GetMessagesStartNewConversationParticipant(Protocol_Interaction.class, Protocol_InteractionObserver.class, receivedPerf);
            choose = this.getBehaivoralChoice("Protocol_InteractionAgree_InteractionUnit", NextMsg);
            Agree newAgree = new Agree(receivedPerf.getIdMessage(), receivedPerf.getIdDialog(), receivedPerf.getInReplyTo(), receivedPerf.getSender(), receivedPerf.getReceiver(), null, receivedPerf.getProtocolName(), receivedPerf.getReplyWith());
            comm.sendMessage(newAgree, choose, null, comm.buildIDAsParticipant(receivedPerf));
            NextMsg = comm.GetNextMessages(comm.buildIDAsParticipant(receivedPerf));
            choose = this.getBehaivoralChoice("Protocol_InteractionInform_InteractionUnit", NextMsg);
            EBookContract ebc0 = new EBookContract();
            ebc0.setName("Marciaaaal Ruiz Escribano");
            ebc0.setBankAccount("666");
            ebc0.setCustomerId("ElGanyan");
            ebc0.setEBookTitle("El senyor de los paletillos: La hermandad del garrote");
            ebc0.setEmail("ganyan@paletos.com");
            ebc0.setPrice(1000);
            ebc0.setRetrieveFrom("http://paletos.com");
            ebc0.setW3cEndpointReference(null);
            OntologyConcept onto0 = new OntologyConcept(ebc0);
            Inform newInform0 = new Inform(receivedPerf.getIdMessage(), receivedPerf.getIdDialog(), receivedPerf.getInReplyTo(), receivedPerf.getSender(), receivedPerf.getReceiver(), onto0, receivedPerf.getProtocolName(), receivedPerf.getReplyWith());
            comm.sendMessage(newInform0, choose, onto0, comm.buildIDAsParticipant(receivedPerf));
            for (int i = 0; i < 200000; i++) {
                System.out.println("CANTAR UNA NANA");
            }
            NextMsg = comm.GetNextMessages(comm.buildIDAsParticipant(receivedPerf));
            choose = this.getBehaivoralChoice("Protocol_InteractionFailure_InteractionUnit", NextMsg);
            Failure newFailure0 = new Failure(receivedPerf.getIdMessage(), receivedPerf.getIdDialog(), receivedPerf.getInReplyTo(), receivedPerf.getSender(), receivedPerf.getReceiver(), null, receivedPerf.getProtocolName(), receivedPerf.getReplyWith());
            comm.sendMessage(newFailure0, choose, null, comm.buildIDAsParticipant(receivedPerf));
        } catch (Exception E) {
            String dummy = getStackTrace(E);
            AgentLogger.log(dummy);
        }
    }

    private void follow_SubscriptionAgreeInformDesubsPath(Performative receivedPerf, IConversationListener comm) {
        MessageTemplate[] NextMsg;
        MessageTemplate choose;
        try {
            AgentLogger.log("Responding to subscription request");
            NextMsg = comm.GetMessagesStartNewConversationParticipant(Protocol_Interaction.class, Protocol_InteractionObserver.class, receivedPerf);
            choose = this.getBehaivoralChoice("Protocol_InteractionAgree_InteractionUnit", NextMsg);
            Agree newAgree = new Agree(receivedPerf.getIdMessage(), receivedPerf.getIdDialog(), receivedPerf.getInReplyTo(), receivedPerf.getSender(), receivedPerf.getReceiver(), null, receivedPerf.getProtocolName(), receivedPerf.getReplyWith());
            comm.sendMessage(newAgree, choose, null, comm.buildIDAsParticipant(receivedPerf));
            NextMsg = comm.GetNextMessages(comm.buildIDAsParticipant(receivedPerf));
            choose = this.getBehaivoralChoice("Protocol_InteractionInform_InteractionUnit", NextMsg);
            EBookContract ebc0 = new EBookContract();
            ebc0.setName("Marciaaaal Ruiz Escribano");
            ebc0.setBankAccount("666");
            ebc0.setCustomerId("ElGanyan");
            ebc0.setEBookTitle("El senyor de los paletillos: La hermandad del garrote");
            ebc0.setEmail("ganyan@paletos.com");
            ebc0.setPrice(1000);
            ebc0.setRetrieveFrom("http://paletos.com");
            ebc0.setW3cEndpointReference(null);
            OntologyConcept onto0 = new OntologyConcept(ebc0);
            Inform newInform0 = new Inform(receivedPerf.getIdMessage(), receivedPerf.getIdDialog(), receivedPerf.getInReplyTo(), receivedPerf.getSender(), receivedPerf.getReceiver(), onto0, receivedPerf.getProtocolName(), receivedPerf.getReplyWith());
            comm.sendMessage(newInform0, choose, onto0, comm.buildIDAsParticipant(receivedPerf));
            for (int i = 0; i < 200000; i++) {
                System.out.println("CANTAR UNA NANA");
            }
            NextMsg = comm.GetNextMessages(comm.buildIDAsParticipant(receivedPerf));
            choose = this.getBehaivoralChoice("Protocol_InteractionInform_InteractionUnit", NextMsg);
            EBookContract ebc1 = new EBookContract();
            ebc1.setName("Marciaaaal Ruiz Escribano");
            ebc1.setBankAccount("666");
            ebc1.setCustomerId("ElGanyan");
            ebc1.setEBookTitle("El libro de los records de Gines");
            ebc1.setEmail("ganyan@paletos.com");
            ebc1.setPrice(1000);
            ebc1.setRetrieveFrom("http://paletos.com");
            ebc1.setW3cEndpointReference(null);
            OntologyConcept onto1 = new OntologyConcept(ebc1);
            Inform newInform1 = new Inform(receivedPerf.getIdMessage(), receivedPerf.getIdDialog(), receivedPerf.getInReplyTo(), receivedPerf.getSender(), receivedPerf.getReceiver(), onto1, receivedPerf.getProtocolName(), receivedPerf.getReplyWith());
            comm.sendMessage(newInform1, choose, onto1, comm.buildIDAsParticipant(receivedPerf));
        } catch (Exception E) {
            String dummy = getStackTrace(E);
            AgentLogger.log(dummy);
        }
    }

    private void follow_SubscriptionRefusePath(Performative receivedPerf, IConversationListener comm) {
        MessageTemplate[] NextMsg;
        MessageTemplate choose;
        try {
            AgentLogger.log("Responding to subscription request");
            NextMsg = comm.GetMessagesStartNewConversationParticipant(Protocol_Interaction.class, Protocol_InteractionObserver.class, receivedPerf);
            choose = this.getBehaivoralChoice("Protocol_InteractionRefuse_InteractionUnit", NextMsg);
            Refuse newRefuse = new Refuse(receivedPerf.getIdMessage(), receivedPerf.getIdDialog(), receivedPerf.getInReplyTo(), receivedPerf.getSender(), receivedPerf.getReceiver(), null, receivedPerf.getProtocolName(), receivedPerf.getReplyWith());
            comm.sendMessage(newRefuse, choose, null, comm.buildIDAsParticipant(receivedPerf));
        } catch (Exception E) {
            String dummy = getStackTrace(E);
            AgentLogger.log(dummy);
        }
    }

    private void follow_SubscriptionFailurePath(Performative receivedPerf, IConversationListener comm) {
        MessageTemplate[] NextMsg;
        MessageTemplate choose;
        try {
            AgentLogger.log("Responding to subscription request");
            NextMsg = comm.GetMessagesStartNewConversationParticipant(Protocol_Interaction.class, Protocol_InteractionObserver.class, receivedPerf);
            choose = this.getBehaivoralChoice("Protocol_InteractionFailure_InteractionUnit", NextMsg);
            Failure newFailure = new Failure(receivedPerf.getIdMessage(), receivedPerf.getIdDialog(), receivedPerf.getInReplyTo(), receivedPerf.getSender(), receivedPerf.getReceiver(), null, receivedPerf.getProtocolName(), receivedPerf.getReplyWith());
            comm.sendMessage(newFailure, choose, null, comm.buildIDAsParticipant(receivedPerf));
        } catch (Exception E) {
            String dummy = getStackTrace(E);
            AgentLogger.log(dummy);
        }
    }

    private void follow_protocolBreakTroughIncorrectPerformative(Performative receivedPerf, IConversationListener comm) {
        MessageTemplate[] NextMsg;
        MessageTemplate choose;
        try {
            AgentLogger.log("Responding to subscription request");
            NextMsg = comm.GetMessagesStartNewConversationParticipant(Protocol_Interaction.class, Protocol_InteractionObserver.class, receivedPerf);
            choose = this.getBehaivoralChoice("Protocol_InteractionAgree_InteractionUnit", NextMsg);
            Inform newAgree = new Inform(receivedPerf.getIdMessage(), receivedPerf.getIdDialog(), receivedPerf.getInReplyTo(), receivedPerf.getSender(), receivedPerf.getReceiver(), null, receivedPerf.getProtocolName(), receivedPerf.getReplyWith());
            comm.sendMessage(newAgree, choose, null, comm.buildIDAsParticipant(receivedPerf));
        } catch (Exception E) {
            String dummy = getStackTrace(E);
            AgentLogger.log(dummy);
        }
    }

    private void follow_protocolBreakTroughPrecedences(Performative receivedPerf, IConversationListener comm) {
        MessageTemplate[] NextMsg;
        try {
            AgentLogger.log("Responding to subscription request");
            NextMsg = comm.GetMessagesStartNewConversationParticipant(Protocol_Interaction.class, Protocol_InteractionObserver.class, receivedPerf);
            MessageTemplate choose0 = this.getBehaivoralChoice("Protocol_InteractionAgree_InteractionUnit", NextMsg);
            Agree newAgree = new Agree(receivedPerf.getIdMessage(), receivedPerf.getIdDialog(), receivedPerf.getInReplyTo(), receivedPerf.getSender(), receivedPerf.getReceiver(), null, receivedPerf.getProtocolName(), receivedPerf.getReplyWith());
            comm.sendMessage(newAgree, choose0, null, comm.buildIDAsParticipant(receivedPerf));
            NextMsg = comm.GetNextMessages(comm.buildIDAsParticipant(receivedPerf));
            MessageTemplate choose1 = this.getBehaivoralChoice("Protocol_InteractionInform_InteractionUnit", NextMsg);
            EBookContract ebc0 = new EBookContract();
            ebc0.setName("Marciaaaal Ruiz Escribano");
            ebc0.setBankAccount("666");
            ebc0.setCustomerId("ElGanyan");
            ebc0.setEBookTitle("El senyor de los paletillos: La hermandad del garrote");
            ebc0.setEmail("ganyan@paletos.com");
            ebc0.setPrice(1000);
            ebc0.setRetrieveFrom("http://paletos.com");
            ebc0.setW3cEndpointReference(null);
            OntologyConcept onto0 = new OntologyConcept(ebc0);
            Inform newInform0 = new Inform(receivedPerf.getIdMessage(), receivedPerf.getIdDialog(), receivedPerf.getInReplyTo(), receivedPerf.getSender(), receivedPerf.getReceiver(), onto0, receivedPerf.getProtocolName(), receivedPerf.getReplyWith());
            comm.sendMessage(newAgree, choose0, onto0, comm.buildIDAsParticipant(receivedPerf));
        } catch (Exception E) {
            String dummy = getStackTrace(E);
            AgentLogger.log(dummy);
        }
    }
}
