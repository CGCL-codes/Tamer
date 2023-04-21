package eu.soa4all.wp6.composer.agents;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.soa4all.lpml.Activity;
import org.soa4all.lpml.AnnotationType;
import org.soa4all.lpml.Parameter;
import org.soa4all.lpml.Process;
import org.soa4all.lpml.SemanticAnnotation;
import org.soa4all.lpml.Binding;
import org.soa4all.lpml.impl.ProcessImpl;
import org.soa4all.lpml.impl.SemanticAnnotationImpl;
import org.soa4all.lpml.impl.BindingImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import eu.soa4all.construction.reasoner.wsl.types.WSLElement;
import eu.soa4all.construction.reasoner.wsl.types.WSLService;
import eu.soa4all.construction.reasoner.wsl.types.WSLTemplate;
import eu.soa4all.wp6.common.utils.thread.ThreadPool;
import eu.soa4all.wp6.composer.ComposerOperations;
import eu.soa4all.wp6.composer.DTComposerImpl;
import eu.soa4all.wp6.composer.agents.adapter.RuleAdapter;
import eu.soa4all.wp6.composer.agents.adapter.GoalTemplateAdapter;
import eu.soa4all.wp6.composer.agents.adapter.GoalWSAdapter;
import eu.soa4all.wp6.composer.agents.events.NewDesignModelEvent;
import eu.soa4all.wp6.composer.agents.events.TaggedModelEvent;
import eu.soa4all.wp6.composer.agents.events.CleanupBlackBoardEvent;
import eu.soa4all.wp6.composer.designmodel.DesignModel;
import eu.soa4all.wp6.composer.lpml.LPMLProxy;
import eu.soa4all.wp6.composer.utils.ModelLoader;
import eu.soa4all.construction.reasoner.client.ReasonerAPI;

public class DesignModificationWSMLAgent extends Agent implements ApplicationListener, DesignModificationAgent, InitializingBean, Runnable {

    private static final int MAX_NUMBER_BINDINGS = 3;

    private static final int MAX_NUMBER_FETCHED_RESULTS = 3;

    static Logger logger = Logger.getLogger(DesignModificationWSMLAgent.class);

    NewDesignModelEvent newDesignModelEvent;

    public DesignModificationWSMLAgent(ReasonerAPI reasoner) {
        this.reasoner = reasoner;
        this.modelLoader = ModelLoader.getInstance();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof TaggedModelEvent) {
            newDesignModelEvent = (NewDesignModelEvent) applicationEvent;
            if (DTComposerImpl.getInstance().getConfiguration().isThread()) {
                ThreadPool.getInstance().execute(this);
            } else {
                run();
            }
        } else if (applicationEvent instanceof CleanupBlackBoardEvent) {
            this.reasoner.cleanUp();
            this.modelLoader.cleanUp();
        }
    }

    @Override
    public void run() {
        if (!newDesignModelEvent.getBlackBoardControlAgent().isFoundRequiredSolutions()) {
            runWSMLAgent();
        }
        newDesignModelEvent.getBlackBoardControlAgent().decreaseSizeDMAInvocationPool();
    }

    private void runWSMLAgent() {
        try {
            if (newDesignModelEvent.getDesignModel().getStatus().isInadmissible()) {
                return;
            }
            if (newDesignModelEvent.getDesignModel().getStatus().isIOUncheckedSolution()) {
                return;
            }
            ComposerOperations operation = newDesignModelEvent.getBlackBoardControlAgent().getOperation();
            List<RuleAdapter> adapters = new ArrayList<RuleAdapter>();
            if (newDesignModelEvent.getBlackBoardControlAgent().isProcessTarget() == false) {
                Activity goalActivity = LPMLProxy.getActivityById(newDesignModelEvent.getDesignModel().getDesignStructure(), newDesignModelEvent.getBlackBoardControlAgent().getGoalTarget());
                if (goalActivity != null) {
                    adapters = resolveGoal(newDesignModelEvent, goalActivity, operation);
                }
            } else {
                adapters = resolveProcess(newDesignModelEvent, operation);
            }
            if (adapters != null && !adapters.isEmpty()) {
                for (RuleAdapter adapter : adapters) {
                    adapter.applyAdapter();
                }
            }
            (newDesignModelEvent.getBlackBoardControlAgent()).notifyAgentResponse(newDesignModelEvent);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
	 * Method for resolving whole process. It searches for unresolved goals,
	 * takes first and applies resolveGoal method.
	 * 
	 * @param newDesignModelEvent
	 * @param operation
	 * @return adapter for converting resolved goal into either service or
	 *         template
	 */
    private List<RuleAdapter> resolveProcess(NewDesignModelEvent newDesignModelEvent, ComposerOperations operation) {
        List<RuleAdapter> results = new LinkedList<RuleAdapter>();
        Process process = newDesignModelEvent.getDesignModel().getDesignStructure();
        try {
            Iterator<Activity> goalsIterator = LPMLProxy.findAllGoals(process).iterator();
            while (goalsIterator.hasNext()) {
                final Activity currGoal = (Activity) goalsIterator.next();
                logger.debug("trying to resolve goal: " + currGoal.getName() + " using WSML-based agent " + this.identifier);
                List<RuleAdapter> adapters = resolveGoal(newDesignModelEvent, currGoal, operation);
                if (currGoal.getBindings() != null && !currGoal.getBindings().isEmpty() && adapters != null && !adapters.isEmpty()) {
                    for (Iterator<RuleAdapter> iter = adapters.iterator(); iter.hasNext(); ) {
                        RuleAdapter ra = iter.next();
                        if (ra instanceof GoalWSAdapter) {
                            GoalWSAdapter swsa = (GoalWSAdapter) ra;
                            Binding service = swsa.getService();
                            for (Binding b : currGoal.getBindings()) {
                                if (b.getServiceReference().toString().equals(service.getServiceReference().toString())) {
                                    iter.remove();
                                }
                            }
                        }
                    }
                }
                if (adapters != null && !adapters.isEmpty()) {
                    for (RuleAdapter adapter : adapters) {
                        results.add(adapter);
                    }
                    break;
                }
            }
        } catch (Throwable t) {
            logger.error(t + " resolveGoals() Fail");
            t.printStackTrace();
            return null;
        }
        return results;
    }

    /**
	 * Method for resolving single goal. It tries to find matches that are
	 * compatible with selected goal's annotations. It takes into account
	 * current operation requested from DTC, and only performs according to
	 * that.
	 * 
	 * @param dme
	 * @param goalTarget
	 * @param operation
	 * @return adapter to transform goal into Service or Template
	 * @throws MalformedURLException 
	 */
    private List<RuleAdapter> resolveGoal(NewDesignModelEvent dme, Activity goalTarget, ComposerOperations operation) throws MalformedURLException {
        List<RuleAdapter> results = new ArrayList<RuleAdapter>();
        List<WSLElement> ws = new ArrayList<WSLElement>();
        if (isAnnotated(goalTarget) == false) {
            return null;
        }
        if (operation == ComposerOperations.RESOLVE_GOAL_WITH_WS || operation == ComposerOperations.RESOLVE_PROCESS_WITH_WS) {
            List<WSLService> matches = reasoner.matchGoalMSM_WS(dme.getDesignModel().getDesignStructure(), goalTarget, MAX_NUMBER_FETCHED_RESULTS);
            if (matches != null) {
                ws.addAll(matches);
            } else {
                logger.debug("no match found");
            }
        }
        if (operation == ComposerOperations.RESOLVE_GOAL_WITH_TEMPLATE || operation == ComposerOperations.RESOLVE_PROCESS_WITH_TEMPLATE) {
            List<WSLTemplate> matches = reasoner.matchGoalMSM_Template(dme.getDesignModel().getDesignStructure(), goalTarget, MAX_NUMBER_FETCHED_RESULTS);
            if (matches != null) {
                ws.addAll(matches);
            } else {
                logger.debug("no match found");
            }
        }
        if (operation == ComposerOperations.RESOLVE_GOAL || operation == ComposerOperations.RESOLVE_PROCESS) {
            List<WSLService> serviceMatches = null;
            if (goalTarget.getBindings().size() < MAX_NUMBER_BINDINGS) {
                serviceMatches = reasoner.matchGoalMSM_WS(dme.getDesignModel().getDesignStructure(), goalTarget, MAX_NUMBER_FETCHED_RESULTS);
            }
            List<WSLTemplate> templateMatches = null;
            if (goalTarget.getBindings().size() == 0) {
                templateMatches = reasoner.matchGoalMSM_Template(dme.getDesignModel().getDesignStructure(), goalTarget, MAX_NUMBER_FETCHED_RESULTS);
            }
            if (serviceMatches != null) {
                ws.addAll(serviceMatches);
            } else {
                logger.debug("no service matches found...");
            }
            if ((templateMatches != null) && (goalTarget.getBindings().size() == 0)) {
                ws.addAll(templateMatches);
            } else {
                logger.debug("no template matches found...");
            }
        }
        if (ws == null || (ws != null && ws.size() == 0)) {
            return results;
        } else {
            RuleAdapter adapter = null;
            for (WSLElement element : ws) {
                if (element instanceof WSLService) {
                    adapter = createGoalWSAdapter(dme, this, goalTarget, "http://www.soa4all.eu#Adapter", (WSLService) element);
                }
                if (element instanceof WSLTemplate) {
                    adapter = createGoalTemplateAdapter(dme, this, goalTarget, "http://www.soa4all.eu#Adapter", ((WSLTemplate) element));
                }
                results.add(adapter);
            }
        }
        return results;
    }

    private boolean isAnnotated(Activity goalTarget) {
        boolean inputParam = true;
        boolean outputParam = true;
        boolean FC = true;
        if (goalTarget.getInputParameters() == null || goalTarget.getInputParameters().size() == 0) {
            inputParam = false;
        } else {
            for (Parameter p : goalTarget.getInputParameters()) {
                if (p.getSemanticAnnotations() == null || p.getSemanticAnnotations().size() == 0) {
                    inputParam = false;
                }
            }
        }
        if (goalTarget.getOutputParameters() == null || goalTarget.getInputParameters().size() == 0) {
            outputParam = false;
        } else {
            for (Parameter p : goalTarget.getOutputParameters()) {
                if (p.getSemanticAnnotations() == null || p.getSemanticAnnotations().size() == 0) {
                    outputParam = false;
                }
            }
        }
        if (goalTarget.getSemanticAnnotations() == null || goalTarget.getSemanticAnnotations().size() == 0) {
            FC = false;
        }
        if (inputParam == false && outputParam == false && FC == false) {
            return false;
        } else {
            return true;
        }
    }

    private URL getTemplateURL(URI templateURI) throws MalformedURLException {
        String templateName = templateURI.getFragment();
        URL url = null;
        try {
            url = new URL(DTComposerImpl.getInstance().getConfiguration().getTEMPLATES_URL() + templateName + ".xml");
        } catch (Exception e) {
            logger.error("not a URL, try loading from classpath.");
        }
        return url;
    }

    private String getTemplateLocation(String templateName) throws MalformedURLException {
        return DTComposerImpl.getInstance().getConfiguration().getTEMPLATES_URL() + templateName + ".xml";
    }

    private boolean checkApplicabilityConditions(Process process, Activity goalActivity) {
        return true;
    }

    public RuleAdapter createGoalTemplateAdapter(NewDesignModelEvent dme, Agent agent, Activity g, String a, WSLTemplate wsltemplate) {
        Process process = new ProcessImpl(wsltemplate.getTemplateReference());
        GoalTemplateAdapter adapter = new GoalTemplateAdapter(dme, agent);
        try {
            if (wsltemplate.getNFP() != null && !wsltemplate.getNFP().isEmpty()) {
                for (String nfp : wsltemplate.getNFP()) {
                    SemanticAnnotation sa = new SemanticAnnotationImpl();
                    sa.setType(AnnotationType.META_DATA);
                    sa.setReferenceURI(nfp);
                    adapter.getAssumptions().add(sa);
                }
            }
            adapter.setIdentifier(new URI(a));
            adapter.setSource(g);
            adapter.setTarget(process);
        } catch (URISyntaxException e) {
            logger.error("createAdapter fail");
            e.printStackTrace();
        }
        return adapter;
    }

    public GoalWSAdapter createGoalWSAdapter(NewDesignModelEvent dme, Agent agent, Activity g, String a, WSLService wslservice) throws MalformedURLException {
        String serviceRef = wslservice.getServiceReference();
        String operation = wslservice.getOperations().get(0).getOperationName();
        String serviceName = wslservice.getServiceName();
        Binding service = new BindingImpl();
        GoalWSAdapter adapter = new GoalWSAdapter(dme, agent);
        try {
            adapter.setIdentifier(new URI(a));
            adapter.setSource(g);
            adapter.setTarget(service);
            adapter.setRuleName("Semantic Matching");
            if (wslservice.getNFP() != null && !wslservice.getNFP().isEmpty()) {
                for (String nfp : wslservice.getNFP()) {
                    SemanticAnnotation sa = new SemanticAnnotationImpl();
                    sa.setType(AnnotationType.META_DATA);
                    sa.setReferenceURI(nfp);
                    adapter.getAssumptions().add(sa);
                }
            }
            operation = operation.substring(operation.lastIndexOf("#") + 1, operation.length());
            adapter.setServiceOperation(operation);
            adapter.setServiceDefinitionURL(serviceRef);
            adapter.setServiceName(serviceName);
        } catch (URISyntaxException e) {
            logger.error("createAdapter fail");
            e.printStackTrace();
        }
        return adapter;
    }

    public void addAssumption(RuleAdapter adapter, String reference_uri, String expression) {
        try {
            SemanticAnnotation sa = new SemanticAnnotationImpl();
            sa.setReferenceURI(reference_uri);
            sa.setExpression(expression);
            adapter.addAssumption(sa);
        } catch (Throwable t) {
            logger.error("add assumption fail");
            t.printStackTrace();
        }
    }

    public void applyConstraints(DesignModel ndm, String g, String a, String p) {
        try {
        } catch (Throwable t) {
        }
    }

    @Override
    public String toString() {
        return this.getIdentifier().toString();
    }
}
