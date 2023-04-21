package br.unb.bioagents.behaviours.conflictresolution;

import jade.content.onto.basic.Action;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.WrapperException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.builder.ResourceType;
import org.drools.runtime.ClassObjectFilter;
import org.drools.runtime.StatefulKnowledgeSession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.unb.bioagents.agents.AgentGroup;
import br.unb.bioagents.agents.ConflictResolutionAgent;
import br.unb.bioagents.agents.ServiceConstants;
import br.unb.bioagents.behaviours.ResponderBehaviour;
import br.unb.bioagents.exceptions.PropertyNotFoundException;
import br.unb.bioagents.hibernate.HibernateUtil;
import br.unb.bioagents.ontology.AnaliseRequests;
import br.unb.bioagents.ontology.PortraitInform;
import br.unb.bioagents.ontology.RequestPortrait;
import br.unb.bioagents.ontology.RequestSugestion;
import br.unb.bioagents.ontology.RequestSugestionGroup;
import br.unb.bioagents.ontology.Sugestion;
import br.unb.bioagents.ontology.Sugests;
import br.unb.bioagents.util.BioAgentsProperties;
import br.unb.bioagents.util.KnowledgeBuilderUtil;
import br.unb.bioagents.util.ResourceTypePair;
import br.unb.bioagents.util.ResourcesUtil;
import br.unb.bioagents.util.UrlResourceTypePair;

public class CRARequestResponder extends ResponderBehaviour<ConflictResolutionAgent> {

    private enum Step {

        DONE, FINISHING, INFORMS_AND_FAILURES, PROPOSALS_AND_REFUSES, RUN_PORTRAIT, WAIT_PORTRAIT
    }

    /**
	 * Empty class used as a fact to indicate that portrait should be executed
	 * 
	 * @author hugowschneider
	 * 
	 */
    public static class RunPortrait {
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = -8453825835560553014L;

    private Vector<ACLMessage> acceptedProposals;

    private Step currentStep;

    private int expectedAnswers;

    private Vector<ACLMessage> failures;

    private Vector<ACLMessage> informs;

    private Vector<ACLMessage> notUnderstoods;

    private Vector<ACLMessage> refuses;

    private Vector<ACLMessage> rejectedProposals;

    public CRARequestResponder(ConflictResolutionAgent a, ConflictResolutionAgent.WorkingGroup workingGroup, int expectedAnswers, RequestSugestion requestSugestion, MessageTemplate messageTemplate) {
        super(a, workingGroup, requestSugestion, messageTemplate);
        setInforms(new Vector<ACLMessage>());
        setAcceptedProposals(new Vector<ACLMessage>());
        setRefuses(new Vector<ACLMessage>());
        setFailures(new Vector<ACLMessage>());
        setNotUnderstoods(new Vector<ACLMessage>());
        setRejectedProposals(new Vector<ACLMessage>());
        setWorkingGroup(workingGroup);
        setCurrentStep(Step.PROPOSALS_AND_REFUSES);
        setExpectedAnswers(expectedAnswers);
    }

    private void addAnswer(ACLMessage message) {
        switch(message.getPerformative()) {
            case ACLMessage.INFORM:
                getInforms().add(message);
                break;
            case ACLMessage.FAILURE:
                getFailures().add(message);
                break;
            default:
                break;
        }
    }

    private List<Sugestion> analise(List<Sugestion> inputSugestions) throws PropertyNotFoundException, IOException {
        List<Sugestion> outputSugestions = new Vector<Sugestion>();
        String[] rulesDefinitions = BioAgentsProperties.getPropertyValue(BioAgentsProperties.CONFLICT_RESOLUTION_RULE_DEFITITIONS).split(",");
        List<ResourceTypePair> resources = new Vector<ResourceTypePair>();
        for (String ruleDefinition : rulesDefinitions) {
            URL url = ResourcesUtil.getResourceURL(BioAgentsProperties.getPropertyValue(String.format(BioAgentsProperties.CONFLICT_RESOLUTION_RULE_PATH_FORMAT_STRING, ruleDefinition)));
            resources.add(new UrlResourceTypePair(ResourceType.DRL, url));
        }
        String[] ruleflowDefinitions = BioAgentsProperties.getPropertyValue(BioAgentsProperties.CONFLICT_RESOLUTION_RULEFLOW_DEFITITIONS).split(",");
        for (String ruleflowDefinition : ruleflowDefinitions) {
            URL url = ResourcesUtil.getResourceURL(BioAgentsProperties.getPropertyValue(String.format(BioAgentsProperties.CONFLICT_RESOLUTION_RULEFLOW_PATH_FORMAT_STRING, ruleflowDefinition)));
            resources.add(new UrlResourceTypePair(ResourceType.DRF, url));
        }
        KnowledgeBase knowledgeBase = KnowledgeBuilderUtil.getInstance().getKnowledgeBase(resources);
        StatefulKnowledgeSession statefulKnowledgeSession = knowledgeBase.newStatefulKnowledgeSession();
        for (Sugestion sugestion : inputSugestions) {
            statefulKnowledgeSession.insert(sugestion);
        }
        for (String ruleflowDefinition : ruleflowDefinitions) {
            statefulKnowledgeSession.startProcess(ruleflowDefinition);
        }
        statefulKnowledgeSession.fireAllRules();
        Collection<?> facts = statefulKnowledgeSession.getObjects(new ClassObjectFilter(RunPortrait.class));
        outputSugestions = new Vector<Sugestion>();
        facts = statefulKnowledgeSession.getObjects(new ClassObjectFilter(Sugestion.class));
        for (Object obj : facts) {
            Sugestion sugestion = (Sugestion) obj;
            outputSugestions.add(sugestion);
        }
        return outputSugestions;
    }

    /**
	 * FIXME - Colocar logica de resolucao de conflitos
	 */
    private void analiseAnswers() {
        ACLMessage reply = getWorkingGroup().getInitialMessage().createReply();
        Vector<Sugestion> sugestions = new Vector<Sugestion>();
        if (getInforms().size() > 0) {
            for (ACLMessage message : getInforms()) {
                try {
                    Sugests s = (Sugests) this.myAgent.getContentManager().extractContent(message);
                    for (int i = 0; i < s.getSugestions().size(); i++) {
                        sugestions.add((Sugestion) s.getSugestions().get(i));
                    }
                } catch (WrapperException e) {
                    Logger.getLogger(this.getClass()).error(e.getMessage(), e);
                }
            }
        }
        try {
            List<Sugestion> outputSugestions = analise(sugestions);
            if (outputSugestions.size() > 0) {
                Sugests sugests = new Sugests();
                sugests.setAgentGroupType(AgentGroup.CONFLICT_RESOLUTION);
                List<Sugestion> s = new ArrayList<Sugestion>();
                for (Sugestion sugestion : outputSugestions) {
                    s.add(sugestion);
                }
                sugests.setSugestionsWrapper(s);
                sugests.setAgent(this.myAgent.getAID());
                sugests.setCodingSequenceProbability(s.isEmpty() ? 0 : 1);
                reply.setPerformative(ACLMessage.INFORM);
                Session session = HibernateUtil.getSession();
                Transaction transaction = session.beginTransaction();
                saveSugests(sugests, session);
                transaction.commit();
                myAgent.getContentManager().fillContent(reply, sugests);
                requestSugestionAnalysis(getRequestSugestion().getRequestGroup());
            } else {
                setCurrentStep(Step.RUN_PORTRAIT);
                return;
            }
        } catch (WrapperException e) {
            Logger.getLogger(this.getClass()).error(e.getMessage(), e);
            reply.setPerformative(ACLMessage.FAILURE);
            reply.setContent(e.getMessage());
        } catch (PropertyNotFoundException e) {
            Logger.getLogger(this.getClass()).error(e.getMessage(), e);
            reply.setPerformative(ACLMessage.FAILURE);
            reply.setContent(e.getMessage());
        } catch (IOException e) {
            Logger.getLogger(this.getClass()).error(e.getMessage(), e);
            reply.setPerformative(ACLMessage.FAILURE);
            reply.setContent(e.getMessage());
        }
        setCurrentStep(Step.FINISHING);
        this.myAgent.send(reply);
    }

    private Vector<ACLMessage> getAcceptedProposals() {
        return acceptedProposals;
    }

    private Step getCurrentStep() {
        return currentStep;
    }

    private int getExpectedAnswers() {
        return expectedAnswers;
    }

    private Vector<ACLMessage> getInforms() {
        return informs;
    }

    private Vector<ACLMessage> getNotUnderstoods() {
        return notUnderstoods;
    }

    private Vector<ACLMessage> getRejectedProposals() {
        return rejectedProposals;
    }

    private void handlePortraitInform(ACLMessage message) {
        PortraitInform inform;
        try {
            if (message.getPerformative() == ACLMessage.INFORM) {
                inform = ((PortraitInform) this.myAgent.getContentManager().extractContent(message));
                ACLMessage reply = getWorkingGroup().getInitialMessage().createReply();
                reply.setPerformative(ACLMessage.INFORM);
                Sugests sugests = new Sugests();
                sugests.setAgentGroupType(AgentGroup.CONFLICT_RESOLUTION);
                sugests.setAgent(this.myAgent.getAID());
                sugests.setCodingSequenceProbability(inform.getResult().getCodingSequenceProbability());
                reply.setPerformative(ACLMessage.INFORM);
                myAgent.getContentManager().fillContent(reply, sugests);
                this.myAgent.send(reply);
            } else {
                ACLMessage reply = getWorkingGroup().getInitialMessage().createReply();
                reply.setPerformative(ACLMessage.INFORM);
                Sugests sugests = new Sugests();
                sugests.setAgentGroupType(AgentGroup.CONFLICT_RESOLUTION);
                sugests.setCodingSequenceProbability(-1);
                sugests.setAgent(this.myAgent.getAID());
                reply.setPerformative(ACLMessage.INFORM);
                Session session = HibernateUtil.getSession();
                Transaction transaction = session.beginTransaction();
                session.save(sugests);
                transaction.commit();
                myAgent.getContentManager().fillContent(reply, sugests);
                this.myAgent.send(reply);
            }
        } catch (WrapperException e) {
            Logger.getLogger(this.getClass()).error(e.getMessage(), e);
        }
        setCurrentStep(Step.FINISHING);
    }

    private void sendPortraitRequest() {
        RequestSugestion requestSugestion;
        RequestPortrait requestPortrait;
        String conversationId;
        try {
            requestSugestion = (RequestSugestion) ((Action) this.myAgent.getContentManager().extractContent(getWorkingGroup().getInitialMessage())).getAction();
            requestPortrait = new RequestPortrait();
            requestPortrait.setAgent(this.myAgent.getAID());
            requestSugestion.getGroupId().setIdentifier(requestSugestion.getGroupId().getIdentifier().replace("\r", "").replace("\n", ""));
            requestPortrait.setGroupId(requestSugestion.getGroupId());
            requestPortrait.setSequence(requestSugestion.getSequence());
            Action action = new Action();
            action.setAction(requestPortrait);
            action.setActor(this.myAgent.getAID());
            conversationId = this.getBioAgent().generateConversationId();
            ACLMessage message = getBioAgent().createACLMessage(ACLMessage.REQUEST, FIPANames.InteractionProtocol.FIPA_REQUEST);
            message.setConversationId(conversationId);
            this.getBioAgent().getReceiverBehaviour().addConversationIdToIgnoreList(conversationId);
            this.addConversationIdToTemplate(conversationId);
            DFAgentDescription[] agents = this.getBioAgent().searchAgent(ServiceConstants.TYPE_PORTRAIT_ANALYSIS);
            if (agents.length > 0) {
                message.addReceiver(agents[0].getName());
                this.myAgent.getContentManager().fillContent(message, action);
                this.myAgent.send(message);
                setCurrentStep(Step.WAIT_PORTRAIT);
                return;
            } else {
            }
        } catch (WrapperException e) {
            Logger.getLogger(this.getClass()).error(e.getMessage(), e);
        } catch (FIPAException e) {
            Logger.getLogger(this.getClass()).error(e.getMessage(), e);
        }
    }

    private void setAcceptedProposals(Vector<ACLMessage> proposals) {
        this.acceptedProposals = proposals;
    }

    private void setCurrentStep(Step currentStep) {
        if (currentStep == Step.FINISHING) restart();
        this.currentStep = currentStep;
    }

    private void setExpectedAnswers(int expectedAnswers) {
        this.expectedAnswers = expectedAnswers;
    }

    private void setFailures(Vector<ACLMessage> failures) {
        this.failures = failures;
    }

    private void setInforms(Vector<ACLMessage> informs) {
        this.informs = informs;
    }

    private void setNotUnderstoods(Vector<ACLMessage> notUnderstoods) {
        this.notUnderstoods = notUnderstoods;
    }

    private void setRefuses(Vector<ACLMessage> refuses) {
        this.refuses = refuses;
    }

    private void setRejectedProposals(Vector<ACLMessage> rejectedProposals) {
        this.rejectedProposals = rejectedProposals;
    }

    private void verifyInformAndFailures() {
        if (getInforms().size() + getFailures().size() == getExpectedAnswers()) {
            analiseAnswers();
            getInforms().clear();
            getFailures().clear();
        }
    }

    private void verifyProposalsAndRefuses() {
        boolean done = true;
        for (Behaviour b : this.getWorkingGroup().getBehaviours()) {
            if (b instanceof CRAContractNet) {
                CRAContractNet contractNet = (CRAContractNet) b;
                done &= contractNet.isReceivedAllProposals();
            }
        }
        if (done) {
            ACLMessage response = getWorkingGroup().getInitialMessage().createReply();
            if (getAcceptedProposals().size() > 0) {
                response.setPerformative(ACLMessage.AGREE);
                ACLMessage reply;
                for (ACLMessage message : getAcceptedProposals()) {
                    reply = message.createReply();
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    reply.setConversationId(message.getConversationId());
                    this.myAgent.send(reply);
                }
                setCurrentStep(Step.INFORMS_AND_FAILURES);
            } else {
                response.setPerformative(ACLMessage.REFUSE);
                response.setContent("");
                for (int i = 0; i < getRefuses().size(); i++) {
                    response.setContent(response.getContent() + getRefuses().get(i).getContent() + "\n");
                }
                setCurrentStep(Step.FINISHING);
            }
            this.myAgent.send(response);
            getAcceptedProposals().clear();
            getRejectedProposals().clear();
            getRefuses().clear();
            getNotUnderstoods().clear();
        }
    }

    private void requestSugestionAnalysis(RequestSugestionGroup group) {
        ACLMessage message = getBioAgent().createACLMessage(ACLMessage.REQUEST, FIPANames.InteractionProtocol.FIPA_REQUEST);
        message.setSender(this.myAgent.getAID());
        try {
            DFAgentDescription[] agents = getBioAgent().searchAgent(ServiceConstants.TYPE_LEARNING_MANAGEMENT);
            if (agents.length > 0) {
                for (int i = 0; i < agents.length; i++) {
                    message.addReceiver(agents[i].getName());
                }
            }
            AnaliseRequests analiseRequests = new AnaliseRequests();
            analiseRequests.setGroup(group);
            Action action = new Action();
            action.setAction(analiseRequests);
            action.setActor(this.myAgent.getAID());
            this.myAgent.getContentManager().fillContent(message, action);
            this.myAgent.send(message);
        } catch (FIPAException e) {
            Logger.getLogger(this.getClass()).error(e.getMessage(), e);
        } catch (WrapperException e) {
            Logger.getLogger(this.getClass()).error(e.getMessage(), e);
        }
    }

    @Override
    public void action() {
        ACLMessage message;
        switch(getCurrentStep()) {
            case PROPOSALS_AND_REFUSES:
                block();
                verifyProposalsAndRefuses();
                break;
            case INFORMS_AND_FAILURES:
                message = receive();
                if (message != null) {
                    addAnswer(message);
                } else {
                    block();
                }
                verifyInformAndFailures();
                break;
            case RUN_PORTRAIT:
                sendPortraitRequest();
                block();
                break;
            case WAIT_PORTRAIT:
                message = receive();
                if (message != null) {
                    handlePortraitInform(message);
                } else {
                    block();
                }
                break;
            case FINISHING:
                cleanWorkgroup();
                setCurrentStep(Step.DONE);
                break;
            default:
                break;
        }
    }

    public void addAcceptedProposal(ACLMessage proposal) {
        getAcceptedProposals().add(proposal);
    }

    public void addAgree(ACLMessage m) {
        getAcceptedProposals().add(m);
    }

    public void addFailure(ACLMessage m) {
        getFailures().add(m);
    }

    public void addInform(ACLMessage m) {
        getInforms().add(m);
    }

    public void addNotUnderstood(ACLMessage message) {
        getNotUnderstoods().add(message);
    }

    public void addRefuse(ACLMessage m) {
        getRefuses().add(m);
    }

    public void addRejectedProposal(ACLMessage rejectedProposal) {
        getRejectedProposals().add(rejectedProposal);
    }

    @Override
    public boolean done() {
        return getCurrentStep() == Step.DONE;
    }

    public Vector<ACLMessage> getFailures() {
        return failures;
    }

    public Vector<ACLMessage> getRefuses() {
        return refuses;
    }
}
