package simis.appLayer.slaAgents.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import repast.simphony.essentials.RepastEssentials;
import simis.appLayer.slaAgents.SLANegAgent;
import simis.appLayer.slaAgents.lib.EnumerationSLOConstraint;
import simis.appLayer.slaAgents.lib.SLOConstraint;
import simis.appLayer.slaAgents.lib.SLONeg;
import simis.appLayer.slaAgents.lib.enums.RoleNames;
import simis.appLayer.slaAgents.lib.serviceDescriptionDocuments.EventActionType;
import simis.appLayer.slaAgents.lib.serviceDescriptionDocuments.ExtendedSLATemplate;
import simis.appLayer.slaAgents.lib.serviceDescriptionDocuments.ProtocolStep;
import simis.appLayer.slaAgents.lib.serviceDescriptionDocuments.SLO;
import simis.appLayer.slaAgents.lib.serviceDescriptionDocuments.ServiceIdentificator;
import simis.appLayer.slaAgents.lib.serviceDescriptionDocuments.ServiceType;
import simis.messaging.Element;
import simis.messaging.HeaderElements;
import simis.messaging.Message;
import simis.messaging.MessageTypes;
import simis.messaging.StringContent;

public class GenericNegotiator {

    private Logger logger;

    private String serviceID;

    private ServiceType serviceType;

    private ExtendedSLATemplate extendedSLATemplate;

    private SLANegAgent myAgent;

    private String myRole;

    private String negotiationCoordinator;

    private String serviceProvider;

    private List<ProtocolStep> protocolSteps = new Vector<ProtocolStep>();

    private HashMap<String, SLO> fixedSLOs = new HashMap<String, SLO>();

    private Integer negotiationTimeout;

    private Integer negotiationTimeoutDefault = 500;

    private HashMap<String, SLOConstraint> sloConstraints = new HashMap<String, SLOConstraint>();

    private String negotiationID;

    private HashMap<String, SLONeg> receivedSLOs = new HashMap<String, SLONeg>();

    private HashMap<String, SLONeg> suggestedSLOs = new HashMap<String, SLONeg>();

    private ProtocolStep currentProtocolStep = null;

    /**
	 * Constructor
	 */
    public GenericNegotiator(double acquisitiveness, HashMap<String, SLOConstraint> sloConstraints, ServiceIdentificator identificator, ServiceType serviceType, ExtendedSLATemplate template, SLANegAgent agent, String role, String negotiationID) {
        this.logger = Logger.getLogger(this.getClass().toString());
        logger.setLevel(Level.INFO);
        this.negotiationTimeout = null;
        this.negotiationCoordinator = identificator.getNegotiationCoordinator();
        this.serviceProvider = identificator.getServiceProvider();
        this.serviceID = identificator.getServiceID();
        this.serviceType = serviceType.clone();
        this.extendedSLATemplate = template.clone();
        this.myAgent = agent;
        this.myRole = role;
        this.negotiationID = negotiationID;
        logger.info("SLANeg - " + this.myAgent.getAgentId() + ": Extracting the fixed and negotiable SLOs and negotiation constraints.");
        HashMap<String, SLO> tempSLOMap = this.extendedSLATemplate.getSlaTemplate().getSloSet();
        for (String y : this.serviceType.getProperties().keySet()) {
            if (!tempSLOMap.containsKey(y)) {
                tempSLOMap.put(y, new SLO(y, null));
            }
        }
        for (String x : tempSLOMap.keySet()) {
            if (!this.extendedSLATemplate.getNegotiationProtocol().getNegotiationObject().getNegotiableSLOs().containsKey(x)) {
                fixedSLOs.put(tempSLOMap.get(x).getPropertyID(), tempSLOMap.get(x).clone());
            } else {
                SLO tempSLO = tempSLOMap.get(x);
                boolean tempMultipleValuesPossible = false;
                String tempDomain = null;
                SLONeg tempSLONeg = null;
                try {
                    tempMultipleValuesPossible = this.extendedSLATemplate.getNegotiationProtocol().getNegotiationObject().getNegotiableSLOs().get(tempSLO.getPropertyID()).multipleValuesPossible();
                } catch (Exception e) {
                    throw new RuntimeException("SLANeg - " + this.myAgent.getAgentId() + ": no assertion on multiple values for this SLO " + "found -> set it to single values only. " + e.getStackTrace());
                }
                try {
                    tempDomain = this.serviceType.getProperties().get(tempSLO.getPropertyID()).getDomain();
                } catch (Exception e) {
                    tempDomain = "String";
                    throw new RuntimeException("SLANeg - " + this.myAgent.getAgentId() + ": domain not found in the service type document -> set to String." + e.getStackTrace());
                }
                tempSLONeg = new SLONeg(tempSLO.getPropertyID(), tempSLO.getValue(), tempMultipleValuesPossible, tempDomain);
                tempSLONeg.setAttributeRestrictions(this.extendedSLATemplate.getNegotiationProtocol().getOfferRestrictions().getAttributeRestrictionForProperty(tempSLO.getPropertyID()));
                this.receivedSLOs.put(tempSLONeg.getPropertyID(), tempSLONeg);
            }
        }
        if (this.myRole.equals(RoleNames.SC)) {
            for (int j = 0; j < this.extendedSLATemplate.getNegotiationProtocol().getProcess().getServiceConsumerProcess().size(); j++) {
                this.protocolSteps.add(this.extendedSLATemplate.getNegotiationProtocol().getProcess().getServiceConsumerProcess().get(j).clone());
            }
        } else if (this.myRole.equals(RoleNames.SP)) {
            throw new RuntimeException("SLANeg - " + this.myAgent.getAgentId() + ": reverse auction - not supported!");
        }
        this.sloConstraints = sloConstraints;
        for (String y : sloConstraints.keySet()) {
            if (this.sloConstraints.get(y) instanceof EnumerationSLOConstraint) {
                if (this.fixedSLOs.get(y) != null && (this.sloConstraints.get(y).isFulfilled((this.fixedSLOs.get(y).getValue())))) {
                    this.sloConstraints.get(y).setFulfilledByFixedSLOs(true);
                }
            } else {
                if (this.fixedSLOs.get(y) != null) {
                    sloConstraints.get(y).setFulfilledByFixedSLOs(this.sloConstraints.get(y).isFulfilled(this.fixedSLOs.get(y).getValue()));
                } else {
                    sloConstraints.get(y).setFulfilledByFixedSLOs(false);
                }
            }
        }
        logger.info("SLANeg - " + this.myAgent.getAgentId() + ": service description documents fully extracted " + "-> check if this agent must start the negotiation process." + RepastEssentials.GetTickCount());
        Vector<String> tempRoles = new Vector<String>();
        tempRoles.add(RoleNames.NC);
        currentProtocolStep = getProtocolStep(MessageTypes.ADMISSION, tempRoles);
        if (currentProtocolStep != null) {
            logger.info("SLANeg - " + this.myAgent.getAgentId() + ": this agent must start the negotiation process." + RepastEssentials.GetTickCount());
            boolean temp2 = false;
            for (int i = 0; i < currentProtocolStep.getPossibleActions().size(); i++) {
                if (currentProtocolStep.getPossibleActions().get(i).getMessageType().equals(MessageTypes.OFFER)) {
                    this.createOffer();
                    temp2 = true;
                    break;
                } else if (currentProtocolStep.getPossibleActions().get(i).getMessageType().equals(MessageTypes.NOTIFICATIONACCEPT)) {
                    Message msg2 = new Message();
                    String receipient = null;
                    for (int j = 0; j < currentProtocolStep.getPossibleActions().size(); j++) {
                        if (currentProtocolStep.getPossibleActions().get(j).getMessageType().equals(MessageTypes.NOTIFICATIONACCEPT)) {
                            String temp3 = currentProtocolStep.getPossibleActions().get(j).getTo();
                            if (temp3.equals(RoleNames.SP)) {
                                receipient = this.serviceProvider;
                            } else {
                                receipient = this.negotiationCoordinator;
                            }
                        }
                    }
                    msg2.getHeader().setChild(new Element(HeaderElements.SENDERAGENT, new StringContent(this.myAgent.getAgentId())));
                    msg2.getHeader().setChild(new Element(HeaderElements.MESSAGETYPE, new StringContent(MessageTypes.NOTIFICATIONACCEPT)));
                    msg2.getHeader().setContent(new StringContent(new Long(System.currentTimeMillis()).toString()));
                    msg2.getHeader().setChild(new Element(HeaderElements.NEGOTIATIONID, new StringContent(this.negotiationID)));
                    Vector<Element> children2 = new Vector<Element>();
                    for (String l : this.receivedSLOs.keySet()) {
                        Element elem = new Element(l, new StringContent(this.receivedSLOs.get(l).getValue()));
                        children2.add(elem);
                    }
                    for (String l : this.fixedSLOs.keySet()) {
                        Element elem = new Element(l, new StringContent(this.fixedSLOs.get(l).getValue()));
                        children2.add(elem);
                    }
                    msg2.getBody().setChildren(children2);
                    this.myAgent.relayMessage(msg2, receipient);
                    temp2 = true;
                    this.negotiationTimeout = this.negotiationTimeoutDefault;
                    break;
                }
            }
            if (!temp2) {
                throw new RuntimeException("SLANeg - " + this.myAgent.getAgentId() + ": this agent must start the negotiation process " + "but there was no proactive offer or accept defined! Error in protocol description.");
            }
        } else {
            logger.info("SLANeg - " + this.myAgent.getAgentId() + ": this agent doesn't have to start the negotiation process." + RepastEssentials.GetTickCount());
            this.negotiationTimeout = this.negotiationTimeoutDefault;
        }
    }

    /**
	 * If the strategy contains a pro-active part, this method has to be
	 * invoked.
	 */
    public void stepTimeouts() {
        if (this.negotiationTimeout != null) {
            if (this.negotiationTimeout > 1) {
                this.negotiationTimeout--;
            } else {
                this.negotiationTimeout = null;
                Message msg = new Message();
                if (this.negotiationCoordinator.equals(this.serviceProvider)) {
                    msg.getHeader().setChild(new Element(HeaderElements.SENDERAGENT, new StringContent(this.myAgent.getAgentId())));
                    msg.getHeader().setChild(new Element(HeaderElements.MESSAGETYPE, new StringContent(MessageTypes.NOTIFICATIONREJECT)));
                    msg.getHeader().setContent(new StringContent(new Long(System.currentTimeMillis()).toString()));
                    msg.getHeader().setChild(new Element(HeaderElements.NEGOTIATIONID, new StringContent(this.negotiationID)));
                    logger.warning("negotiationTimeout reached - sending reject message to SP " + this.serviceProvider + " in tick " + RepastEssentials.GetTickCount());
                    this.myAgent.relayMessage(msg, serviceProvider);
                } else {
                    msg.getHeader().setChild(new Element(HeaderElements.SENDERAGENT, new StringContent(this.myAgent.getAgentId())));
                    msg.getHeader().setChild(new Element(HeaderElements.MESSAGETYPE, new StringContent(MessageTypes.NOTIFICATIONREJECT)));
                    msg.getHeader().setContent(new StringContent(new Long(System.currentTimeMillis()).toString()));
                    msg.getHeader().setChild(new Element(HeaderElements.NEGOTIATIONID, new StringContent(this.negotiationID)));
                    logger.warning("negotiationTimeout reached - sending reject message to NC " + this.negotiationCoordinator + " in tick " + RepastEssentials.GetTickCount());
                    this.myAgent.relayMessage(msg, negotiationCoordinator);
                }
                this.processLostNegotiation(true);
            }
        }
    }

    /**
	 * This method offers the SC agent to dispatch negotiation-related messages
	 * to this negotiation sub-component.
	 * 
	 * @param msg
	 *            received message
	 */
    public void dispatchMessage(Message msg) {
        logger.info("SLANeg - " + this.myAgent.getAgentId() + ": message dispatched to negotiation subcomponent.");
        String messageType = null;
        try {
            messageType = ((StringContent) (msg.getHeaderElement(HeaderElements.MESSAGETYPE)).getContent()).getValue();
        } catch (Exception e) {
            throw new RuntimeException("SLANeg - " + this.myAgent.getAgentId() + "could not retrieve the messageType from the received message." + e.getStackTrace());
        }
        String tempNegotiationID = null;
        try {
            tempNegotiationID = ((StringContent) (msg.getHeaderElement(HeaderElements.NEGOTIATIONID)).getContent()).getValue();
        } catch (Exception e) {
            throw new RuntimeException("SLANeg - " + this.myAgent.getAgentId() + "could not retrieve the negotiationID from the received message." + e.getStackTrace());
        }
        if (!tempNegotiationID.equals(this.negotiationID)) {
            logger.warning("SLANeg - " + this.myAgent.getAgentId() + "received a message not fitting its negotiation ID." + RepastEssentials.GetTickCount());
            return;
        }
        String sender = null;
        Vector<String> senderRole = new Vector<String>();
        try {
            sender = ((StringContent) (msg.getHeaderElement(HeaderElements.SENDERAGENT)).getContent()).getValue();
            if (sender.equals(this.serviceProvider)) {
                senderRole.add(RoleNames.SP);
            } else if (sender.equals(this.negotiationCoordinator)) {
                senderRole.add(RoleNames.NC);
            }
        } catch (ClassCastException e) {
            throw new RuntimeException("SLANeg - " + this.myAgent.getAgentId() + "could not retrieve the sender id from the received message." + e.getStackTrace());
        }
        if (messageType.equals(MessageTypes.NOTIFICATIONACCEPT)) {
            logger.warning("SLANeg - " + this.myAgent.getAgentId() + ": end of negotiation reached (this agent won the" + " negotiation) --> process the result." + RepastEssentials.GetTickCount());
            this.currentProtocolStep = null;
            if (!(this.serviceProvider.equals(this.negotiationCoordinator))) {
                String ispa = null;
                try {
                    ispa = ((StringContent) (msg.getBodyElement("seller")).getContent()).getValue();
                } catch (ClassCastException e) {
                    throw new RuntimeException("SLANeg - " + this.myAgent.getAgentId() + "could not retrieve the seller id from the received message." + e.getStackTrace());
                }
                this.processWonNegotiation(false, ispa);
            } else {
                this.processWonNegotiation(false, null);
            }
        } else if (messageType.equals(MessageTypes.NOTIFICATIONREJECT)) {
            logger.warning("SLANeg - " + this.myAgent.getAgentId() + ": end of negotiation reached (this agent lost the" + " negotiation) --> process the result." + RepastEssentials.GetTickCount());
            this.processLostNegotiation(false);
            this.currentProtocolStep = null;
        }
        if (messageType.equals(MessageTypes.CALLFORBIDS)) {
            this.createOffer();
        } else if (messageType.equals(MessageTypes.OFFER)) {
            logger.info("SLANeg - " + this.myAgent.getAgentId() + ": looking for protocolStep fitting to messageType: " + messageType + " and role: " + senderRole);
            currentProtocolStep = this.getProtocolStep(messageType, senderRole);
            boolean[] temp = this.getAllowedActions(currentProtocolStep);
            this.interpretOffer(msg, temp[0], temp[1], temp[2], temp[3]);
        } else if (messageType.equals(MessageTypes.STILLINTERESTED)) {
            logger.warning("SLANeg - " + this.myAgent.getAgentId() + ": still_interested message received by a SC agent - not supported!");
        }
        this.negotiationTimeout = this.negotiationTimeoutDefault;
    }

    /**
	 * Interprets a received offer message
	 * 
	 * @param msg
	 *            received offer message
	 * @param offerAllowed
	 *            flag indicating whether counter offers are allowed in the
	 *            respective protocol step
	 * @param stillInterestedAllowed
	 *            flag indicating whether a stillInterested message is allowed
	 *            as an action in the respective protocol step
	 * @param acceptAllowed
	 *            flag indicating whether an accept message is allowed as an
	 *            action in the respective protocol step
	 */
    private void interpretOffer(Message msg, boolean offerAllowed, boolean stillInterestedAllowed, boolean acceptAllowed, boolean rejectAllowed) {
        Vector<Element> temp = msg.getBody().getChildren();
        String temp2 = null;
        logger.info("SLANeg - " + this.myAgent.getAgentId() + ": updating the received SLO values.");
        for (int i = 0; i < temp.size(); i++) {
            if (this.receivedSLOs.containsKey(temp.get(i).getName())) {
                try {
                    temp2 = ((StringContent) temp.get(i).getContent()).getValue();
                } catch (ClassCastException e) {
                    throw new RuntimeException("SLANeg - " + this.myAgent.getAgentId() + "could not retrieve the SLO value from the received message." + e.getStackTrace());
                }
                this.receivedSLOs.get(temp.get(i).getName()).setValue(temp2);
                temp2 = null;
            } else {
                logger.warning("SLANeg - " + this.myAgent.getAgentId() + ": offer concerning an attribute not yet " + "negotiated received - unsupported!");
            }
        }
        if (!this.isOfferRejectable() && offerAllowed) {
            logger.info("SLANeg - " + this.myAgent.getAgentId() + ": offer is not rejectable and " + "counterOffer message is allowed -> send counter offer." + RepastEssentials.GetTickCount());
            this.createOffer();
            this.currentProtocolStep = null;
            return;
        } else if (this.isOfferAcceptable() && stillInterestedAllowed) {
            logger.info("SLANeg - " + this.myAgent.getAgentId() + ": offer is not rejectable and stillInterested message is allowed " + "-> send stillInterested message." + RepastEssentials.GetTickCount());
            Message msg2 = new Message();
            String receipient = null;
            for (int j = 0; j < currentProtocolStep.getPossibleActions().size(); j++) {
                if (currentProtocolStep.getPossibleActions().get(j).getMessageType().equals(MessageTypes.STILLINTERESTED)) {
                    String temp3 = currentProtocolStep.getPossibleActions().get(j).getTo();
                    if (temp3.equals(RoleNames.SP)) {
                        receipient = this.serviceProvider;
                    } else {
                        receipient = this.negotiationCoordinator;
                    }
                }
            }
            msg2.getHeader().setChild(new Element(HeaderElements.SENDERAGENT, new StringContent(this.myAgent.getAgentId())));
            msg2.getHeader().setChild(new Element(HeaderElements.MESSAGETYPE, new StringContent(MessageTypes.STILLINTERESTED)));
            msg2.getHeader().setContent(new StringContent(new Long(System.currentTimeMillis()).toString()));
            msg2.getHeader().setChild(new Element(HeaderElements.NEGOTIATIONID, new StringContent(this.negotiationID)));
            this.myAgent.relayMessage(msg2, receipient);
            this.currentProtocolStep = null;
            this.negotiationTimeout = this.negotiationTimeoutDefault;
            return;
        } else if (this.isOfferAcceptable() && acceptAllowed) {
            logger.info("SLANeg - " + this.myAgent.getAgentId() + ": offer is aceptable and accept message is allowed -> send accept message." + RepastEssentials.GetTickCount());
            Message msg2 = new Message();
            String receipient = null;
            for (int j = 0; j < currentProtocolStep.getPossibleActions().size(); j++) {
                if (currentProtocolStep.getPossibleActions().get(j).getMessageType().equals(MessageTypes.NOTIFICATIONACCEPT)) {
                    String temp3 = currentProtocolStep.getPossibleActions().get(j).getTo();
                    if (temp3.equals(RoleNames.SP)) {
                        receipient = this.serviceProvider;
                    } else {
                        receipient = this.negotiationCoordinator;
                    }
                }
            }
            msg2.getHeader().setChild(new Element(HeaderElements.SENDERAGENT, new StringContent(this.myAgent.getAgentId())));
            msg2.getHeader().setChild(new Element(HeaderElements.MESSAGETYPE, new StringContent(MessageTypes.NOTIFICATIONACCEPT)));
            msg2.getHeader().setContent(new StringContent(new Long(System.currentTimeMillis()).toString()));
            msg2.getHeader().setChild(new Element(HeaderElements.NEGOTIATIONID, new StringContent(this.negotiationID)));
            Vector<Element> children2 = new Vector<Element>();
            for (String l : this.receivedSLOs.keySet()) {
                Element elem = new Element(l, new StringContent(this.receivedSLOs.get(l).getValue()));
                children2.add(elem);
            }
            for (String l : this.fixedSLOs.keySet()) {
                Element elem = new Element(l, new StringContent(this.fixedSLOs.get(l).getValue()));
                children2.add(elem);
            }
            msg2.getBody().setChildren(children2);
            this.myAgent.relayMessage(msg2, receipient);
            this.processWonNegotiation(true, null);
            this.currentProtocolStep = null;
            return;
        } else if (this.isOfferRejectable() && rejectAllowed) {
            logger.info("SLANeg - " + this.myAgent.getAgentId() + ": offer is rejectable and reject message is allowed -> send lost message." + RepastEssentials.GetTickCount());
            Message msg2 = new Message();
            String receipient = null;
            for (int j = 0; j < currentProtocolStep.getPossibleActions().size(); j++) {
                String temp3 = currentProtocolStep.getPossibleActions().get(j).getTo();
                if (temp3.equals(RoleNames.SP)) {
                    receipient = this.serviceProvider;
                } else {
                    receipient = this.negotiationCoordinator;
                }
            }
            msg2.getHeader().setChild(new Element(HeaderElements.SENDERAGENT, new StringContent(this.myAgent.getAgentId())));
            msg2.getHeader().setChild(new Element(HeaderElements.MESSAGETYPE, new StringContent(MessageTypes.NOTIFICATIONREJECT)));
            msg2.getHeader().setContent(new StringContent(new Long(System.currentTimeMillis()).toString()));
            msg2.getHeader().setChild(new Element(HeaderElements.NEGOTIATIONID, new StringContent(this.negotiationID)));
            this.myAgent.relayMessage(msg2, receipient);
            this.processLostNegotiation(true);
            this.currentProtocolStep = null;
            return;
        }
    }

    /**
	 * Processes a successful negotiation
	 */
    private void processWonNegotiation(boolean thisAgentAccepted, String doubleAuctionWinner) {
        logger.info("SLANeg - " + this.myAgent.getAgentId() + ": this agent won the negotiation -> process the results." + RepastEssentials.GetTickCount());
        if (thisAgentAccepted) {
            this.myAgent.finishNegotiation(true, this.serviceID, null, this.receivedSLOs);
        } else {
            if (this.extendedSLATemplate.getSlaTemplateID().contains("doubleAuction")) {
                this.myAgent.finishNegotiation(true, this.serviceID, doubleAuctionWinner, this.suggestedSLOs);
            } else {
                this.myAgent.finishNegotiation(true, this.serviceID, null, this.suggestedSLOs);
            }
        }
    }

    /**
	 * Processes an unsuccessful negotiation
	 */
    private void processLostNegotiation(boolean thisAgentRejected) {
        logger.info("SLANeg - " + this.myAgent.getAgentId() + ": this agent lost the negotiation -> process the results." + RepastEssentials.GetTickCount());
        if (thisAgentRejected) {
            this.myAgent.finishNegotiation(false, this.serviceID, null, this.receivedSLOs);
        } else {
            this.myAgent.finishNegotiation(false, this.serviceID, null, this.suggestedSLOs);
        }
    }

    /**
	 * Helper method, retrieving the currently applicable ProtocolStep
	 * 
	 * @param receivedMessage
	 *            message event
	 * @param senderRole
	 *            role of the sending agent
	 * @return ProtocolStep applicable in case of such an event
	 */
    private ProtocolStep getProtocolStep(String receivedMessageType, List<String> senderRole) {
        ProtocolStep foundProtocolStep = null;
        for (int i = 0; i < this.protocolSteps.size(); i++) {
            if (protocolSteps.get(i).getEvent().getMessageType().equals(receivedMessageType) && senderRole.contains(protocolSteps.get(i).getEvent().getFrom())) {
                foundProtocolStep = protocolSteps.get(i);
                break;
            }
        }
        if (foundProtocolStep == null) {
            logger.warning("SLANeg - " + this.myAgent.getAgentId() + ": no fitting protocol step found for messageType " + receivedMessageType + " in tick " + RepastEssentials.GetTickCount());
        }
        return foundProtocolStep;
    }

    /**
	 * Helper method, retrieving all possible actions from a given ProtocolStep
	 * 
	 * @param protocolStep
	 *            ProtocolStep to be investigated
	 * @return tuple of booleans representing the allowed actions (sequence:
	 *         offer, stillInterested, notification_accept, notification_reject)
	 */
    private boolean[] getAllowedActions(ProtocolStep protocolStep) {
        boolean[] toReturn = new boolean[4];
        logger.info("SLANeg - " + this.myAgent.getAgentId() + ": retrieving the allowed action from the current protocol step." + RepastEssentials.GetTickCount());
        List<EventActionType> temp = protocolStep.getPossibleActions();
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getMessageType().equals(MessageTypes.OFFER)) {
                toReturn[0] = true;
            } else if (temp.get(i).getMessageType().equals(MessageTypes.STILLINTERESTED)) {
                toReturn[1] = true;
            } else if (temp.get(i).getMessageType().equals(MessageTypes.NOTIFICATIONACCEPT)) {
                toReturn[2] = true;
            }
            toReturn[3] = true;
        }
        return toReturn;
    }

    /**
	 * Checks whether a received offer is acceptable
	 * 
	 * @return flag indicating whether this offer is acceptable
	 */
    private boolean isOfferAcceptable() {
        boolean result = true;
        for (String x : receivedSLOs.keySet()) {
            boolean temp;
            try {
                temp = !this.sloConstraints.get(this.receivedSLOs.get(x).getPropertyID()).isFulfilled(this.receivedSLOs.get(x).getValue());
            } catch (Exception e) {
                throw new RuntimeException("SLANeg - " + this.myAgent.getAgentId() + ": value of offered slo could not be extracted. " + e.getStackTrace());
            }
            if (temp) {
                logger.info("SLANeg - " + this.myAgent.getAgentId() + "one offered value that did not fulfill the " + "respective slo constraint was found " + "-> set whole offer as not acceptable.");
                result = false;
            }
        }
        return result;
    }

    /**
	 * Checks whether a received offer is rejectable
	 * 
	 * @return flag indicating whether this offer is rejectable
	 */
    private boolean isOfferRejectable() {
        boolean result = false;
        for (String x : receivedSLOs.keySet()) {
            boolean temp;
            try {
                temp = this.sloConstraints.get(this.receivedSLOs.get(x).getPropertyID()).isRejectable(this.receivedSLOs.get(x).getValue());
            } catch (Exception e) {
                throw new RuntimeException("SLANeg - " + this.myAgent.getAgentId() + ": value of offered slo could not be extracted. " + e.getStackTrace());
            }
            if (temp) {
                result = true;
            }
        }
        return result;
    }

    /**
	 * Creates an offer message based on the current negotiation situation
	 */
    private void createOffer() {
        String receipient = null;
        for (int j = 0; j < currentProtocolStep.getPossibleActions().size(); j++) {
            if (currentProtocolStep.getPossibleActions().get(j).getMessageType().equals(MessageTypes.OFFER)) {
                String temp3 = currentProtocolStep.getPossibleActions().get(j).getTo();
                if (temp3.equals(RoleNames.SP)) {
                    receipient = this.serviceProvider;
                    logger.info("SLANeg - " + this.myAgent.getAgentId() + ": creating a counter offer to be sent to the service provider." + RepastEssentials.GetTickCount());
                } else {
                    receipient = this.negotiationCoordinator;
                    logger.info("SLANeg - " + this.myAgent.getAgentId() + ": creating a counter offer to be sent to the negotiation coordinator." + RepastEssentials.GetTickCount());
                }
                break;
            }
        }
        Message msg = new Message();
        msg.getHeader().setChild(new Element(HeaderElements.SENDERAGENT, new StringContent(this.myAgent.getAgentId())));
        msg.getHeader().setChild(new Element(HeaderElements.MESSAGETYPE, new StringContent(MessageTypes.OFFER)));
        msg.getHeader().setContent(new StringContent(new Long(System.currentTimeMillis()).toString()));
        msg.getHeader().setChild(new Element(HeaderElements.NEGOTIATIONID, new StringContent(this.negotiationID)));
        Vector<Element> children = new Vector<Element>();
        Vector<String> counterOfferSLOs = new Vector<String>();
        logger.info("SLANeg - " + this.myAgent.getAgentId() + ": identify all sloConstraints that are not yet fulfilled.");
        for (String x : sloConstraints.keySet()) {
            if (!sloConstraints.get(x).isFulfilledByFixedSLOs()) {
                if (this.receivedSLOs.get(x).getValue() == null || !sloConstraints.get(x).isFulfilled(this.receivedSLOs.get(x).getValue())) {
                    counterOfferSLOs.add(x);
                } else {
                }
            } else {
            }
        }
        logger.info("SLANeg - " + this.myAgent.getAgentId() + ": create a counter offer for all not fulfilled sloConstraints." + RepastEssentials.GetTickCount());
        boolean isAcceptable = true;
        for (int i = 0; i < counterOfferSLOs.size(); i++) {
            if (this.suggestedSLOs.isEmpty()) {
                String temp = this.sloConstraints.get(counterOfferSLOs.get(i)).createCounterOffer(this.receivedSLOs.get(counterOfferSLOs.get(i)), null);
                SLONeg tempNeg = this.receivedSLOs.get(counterOfferSLOs.get(i));
                if ((tempNeg.getValue() == null) || (Double.valueOf(temp) != Double.valueOf(tempNeg.getValue()))) {
                    isAcceptable = false;
                }
                Element elem = new Element(counterOfferSLOs.get(i), new StringContent(temp));
                children.add(elem);
            } else {
                String temp2 = this.sloConstraints.get(counterOfferSLOs.get(i)).createCounterOffer(this.receivedSLOs.get(counterOfferSLOs.get(i)), this.suggestedSLOs.get(counterOfferSLOs.get(i)));
                if (Double.valueOf(temp2) != Double.valueOf(this.receivedSLOs.get(counterOfferSLOs.get(i)).getValue())) {
                    isAcceptable = false;
                }
                Element elem = new Element(counterOfferSLOs.get(i), new StringContent(temp2));
                children.add(elem);
            }
        }
        if (isAcceptable) {
            logger.info("SLANeg - " + this.myAgent.getAgentId() + ": offer is aceptable now (special case) -> send accept message." + RepastEssentials.GetTickCount());
            Message msg2 = new Message();
            msg2.getHeader().setChild(new Element(HeaderElements.SENDERAGENT, new StringContent(this.myAgent.getAgentId())));
            msg2.getHeader().setChild(new Element(HeaderElements.MESSAGETYPE, new StringContent(MessageTypes.NOTIFICATIONACCEPT)));
            msg2.getHeader().setContent(new StringContent(new Long(System.currentTimeMillis()).toString()));
            msg2.getHeader().setChild(new Element(HeaderElements.NEGOTIATIONID, new StringContent(this.negotiationID)));
            Vector<Element> children2 = new Vector<Element>();
            for (String l : this.receivedSLOs.keySet()) {
                Element elem = new Element(l, new StringContent(this.receivedSLOs.get(l).getValue()));
                children2.add(elem);
            }
            for (String l : this.fixedSLOs.keySet()) {
                Element elem = new Element(l, new StringContent(this.fixedSLOs.get(l).getValue()));
                children2.add(elem);
            }
            msg2.getBody().setChildren(children2);
            this.myAgent.relayMessage(msg2, receipient);
            this.processWonNegotiation(true, null);
        } else {
            logger.info("SLANeg - " + this.myAgent.getAgentId() + ": updating the suggestedSLOs." + RepastEssentials.GetTickCount());
            for (String y : receivedSLOs.keySet()) {
                this.suggestedSLOs.put(y, receivedSLOs.get(y).clone());
            }
            for (int j = 0; j < children.size(); j++) {
                this.suggestedSLOs.get(children.get(j).getName()).setValue(((StringContent) children.get(j).getContent()).getValue());
            }
            msg.getBody().setChildren(children);
            this.myAgent.relayMessage(msg, receipient);
            this.negotiationTimeout = this.negotiationTimeoutDefault;
        }
    }
}
