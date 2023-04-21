package eu.soa4all.wp6.composer.agents;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import eu.soa4all.wp6.common.utils.thread.ThreadPool;
import eu.soa4all.wp6.composer.ComposerOperations;
import eu.soa4all.wp6.composer.DTComposerImpl;
import eu.soa4all.wp6.composer.agents.events.CleanupBlackBoardEvent;
import eu.soa4all.wp6.composer.agents.events.NewDesignModelEvent;
import eu.soa4all.wp6.composer.agents.events.TaggedModelEvent;
import eu.soa4all.wp6.composer.blackboard.BlackBoard;
import eu.soa4all.wp6.composer.designmodel.DesignModel;
import eu.soa4all.wp6.composer.designmodel.ModelSolution;
import eu.soa4all.wp6.composer.exception.DTComposerException;
import eu.soa4all.wp6.composer.utils.ModelIOUtils;

public class BlackBoardControlAgent extends Agent implements ApplicationContextAware {

    private Collection<DesignModel> solutions;

    private Collection<DesignModel> incompleteSolutions;

    private ApplicationContext applicationContext;

    private BlackBoard blackBoard;

    private boolean foundRequiredSolutions = false;

    private boolean findAllSolutions;

    private int maxNumberSolutions;

    private int numberRequestedSolutions;

    private int sizeDMAInvocationPool;

    private int numberFoundSolutions = 0;

    private URI goalTargetURI;

    private ComposerOperations operation;

    private boolean SLODMAActivated = true;

    static Logger logger = Logger.getLogger(BlackBoardControlAgent.class);

    public int getNumberRequestedSolutions() {
        return numberRequestedSolutions;
    }

    public void setNumberRequestedSolutions(int numberRequestedSolutions) {
        this.numberRequestedSolutions = numberRequestedSolutions;
    }

    public int getMaxNumberSolutions() {
        return maxNumberSolutions;
    }

    public void setMaxNumberSolutions(int maxNumberSolutions) {
        this.maxNumberSolutions = maxNumberSolutions;
    }

    public BlackBoardControlAgent() {
        this.solutions = new ArrayList<DesignModel>();
        this.incompleteSolutions = new ArrayList<DesignModel>();
    }

    public BlackBoard getBlackBoard() {
        return blackBoard;
    }

    public void setBlackBoard(BlackBoard blackBoard) {
        this.blackBoard = blackBoard;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        if (applicationContext.getBeansOfType(SemanticLinkOperatorAgent.class).isEmpty()) {
            SLODMAActivated = false;
        }
    }

    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    public String toString() {
        return this.getIdentifier().toString();
    }

    private void notifySolution(DesignModel solutionDesignModel, URI designAnalysisAgentURI) throws IOException {
        if (solutionDesignModel.getDesignStructure().getID() == blackBoard.getSeed().getDesignStructure().getID()) {
            return;
        }
        numberFoundSolutions++;
        URL url = ModelIOUtils.saveSolutionModel(solutionDesignModel);
        solutionDesignModel.setUrl(url);
        if (!solutions.contains(solutionDesignModel)) {
            solutions.add(solutionDesignModel);
        }
        if (!findAllSolutions && numberFoundSolutions >= numberRequestedSolutions) {
            foundRequiredSolutions = true;
            if (DTComposerImpl.getInstance().getConfiguration().isThread()) {
                ThreadPool.getInstance().shutdown();
            }
        }
    }

    public void updateDesignModel(DesignModel oldDesignModel, DesignModel newDesignModel, URI designOperatorURI, URI agentURI, String adapter, String goal, String ruleName) {
        if (foundRequiredSolutions || numberFoundSolutions >= maxNumberSolutions) {
            return;
        }
        System.out.println("*********************** New Model: " + newDesignModel.getIdentifier());
        boolean updateResult = getBlackBoard().updateDesignModel(oldDesignModel, newDesignModel, designOperatorURI, agentURI, adapter, goal, ruleName);
        if (updateResult == true) {
            notifyNewDesignModel(newDesignModel);
        }
    }

    private Collection<DesignModel> getKnownSolutions() {
        return this.solutions;
    }

    /**
	 * TODO: Improve ranking algorithm
	 * Ranking algorithm:
	 * - selected firstly: admissible and suitable incomplete solutions
	 * - selected secondly: admissible but not suitable incomplete solutions
	 * TODO: Improvement: under same classification, select first that model with higher number of modifications
	 * @param incompleteSolutions
	 * @return
	 */
    private Collection<DesignModel> rank(Collection<DesignModel> incompleteSolutions) {
        Collection<DesignModel> result1 = new ArrayList<DesignModel>();
        for (DesignModel solution : incompleteSolutions) {
            if (solution.getStatus().isAdmissible() && solution.getStatus().isSuitable()) {
                result1.add(solution);
            }
        }
        Object[] r1 = result1.toArray();
        Arrays.sort(r1);
        Collection<DesignModel> result2 = new ArrayList<DesignModel>();
        for (DesignModel solution : incompleteSolutions) {
            if (solution.getStatus().isAdmissible() && solution.getStatus().isNotSuitable()) {
                result2.add(solution);
            }
        }
        Object[] r2 = result2.toArray();
        Arrays.sort(r2);
        Collection<DesignModel> result = new ArrayList<DesignModel>();
        for (int i = 0; i < r1.length; i++) {
            result.add((DesignModel) r1[i]);
        }
        for (int i = 0; i < r2.length; i++) {
            result.add((DesignModel) r2[i]);
        }
        return result;
    }

    public boolean isFindAllSolutions() {
        return findAllSolutions;
    }

    public void setFindAllSolutions(boolean findAllSolutions) {
        this.findAllSolutions = findAllSolutions;
    }

    private void exit() throws MalformedURLException {
        pressAnyKey("Press the enter key to exit");
        cleanup();
        System.exit(0);
    }

    public void cleanup() throws MalformedURLException {
        String tmpdir = DTComposerImpl.getInstance().getConfiguration().getTEMP_URL();
        File tmp;
        if (tmpdir.startsWith("file:")) {
            tmp = new File(new URL(DTComposerImpl.getInstance().getConfiguration().getTEMP_URL()).getFile());
        } else {
            tmp = new File(DTComposerImpl.getInstance().getConfiguration().getTEMP_URL());
        }
        File[] tmpfiles = tmp.listFiles();
        for (int i = 0; i < tmpfiles.length; i++) {
            tmpfiles[i].delete();
        }
        numberFoundSolutions = 0;
        foundRequiredSolutions = false;
        this.solutions = new ArrayList<DesignModel>();
        this.incompleteSolutions = new ArrayList<DesignModel>();
        resetSizeDMAInvocationPool();
        if (DTComposerImpl.getInstance().getConfiguration().isThread()) {
            ThreadPool.getInstance().reset();
        }
        blackBoard.cleanup();
        cleanupDMA();
    }

    private void cleanupDMA() {
        CleanupBlackBoardEvent ce = new CleanupBlackBoardEvent(this);
        getApplicationContext().publishEvent(ce);
    }

    private void pressAnyKey(String message) {
        Scanner keyIn = new Scanner(System.in);
        System.out.print(message);
        keyIn.nextLine();
    }

    public Collection<ModelSolution> getSolutions() throws IOException, URISyntaxException, DTComposerException, InterruptedException {
        if (DTComposerImpl.getInstance().getConfiguration().isThread()) {
            waitForBlackboardComplete();
        }
        int index = 0;
        ArrayList<ModelSolution> solutions = new ArrayList<ModelSolution>();
        for (DesignModel solution : getKnownSolutions()) {
            if (index < numberFoundSolutions) {
                ModelSolution ms = new ModelSolution();
                ms.setComplete(true);
                ms.setModel(ModelIOUtils.loadModelAsString(solution.getUrl()));
                solutions.add(ms);
                index++;
            }
        }
        if (getKnownSolutions().isEmpty()) {
            incompleteSolutions = rank(blackBoard.findIncompleteSolutions());
            for (DesignModel incompleteSolution : incompleteSolutions) {
                if (incompleteSolution.getDesignStructure().getID() == blackBoard.getSeed().getDesignStructure().getID()) {
                    continue;
                }
                if (index >= numberRequestedSolutions) {
                    break;
                }
                URL url = ModelIOUtils.saveSolutionModel(incompleteSolution);
                ModelSolution ms = new ModelSolution();
                ms.setComplete(false);
                ms.setModel(ModelIOUtils.loadModelAsString(url));
                solutions.add(ms);
                index++;
            }
        }
        return solutions;
    }

    private void waitForBlackboardComplete() throws InterruptedException {
        while (!foundRequiredSolutions && getSizeDMAInvocationPool() != 0) {
            Thread.currentThread();
            Thread.sleep(500);
        }
        logger.debug("Found solutions or blackboard complete processed");
    }

    public void setInitialModel(DesignModel initialModel) {
        resetSizeDMAInvocationPool();
        blackBoard.setSeed(initialModel);
        notifyNewDesignModel(initialModel);
    }

    private void notifyNewDesignModel(DesignModel initialModel) {
        NewDesignModelEvent newDesignModelEvent = new NewDesignModelEvent(this);
        newDesignModelEvent.setDesignModel(initialModel);
        newDesignModelEvent.setBlackBoardControlAgent(this);
        increaseSizeDMAInvocationPool(getNumberNotifiedAgents());
        getApplicationContext().publishEvent(newDesignModelEvent);
    }

    private void notifyTaggedModel(DesignModel initialModel) {
        TaggedModelEvent taggedModelEvent = new TaggedModelEvent(this);
        taggedModelEvent.setDesignModel(initialModel);
        taggedModelEvent.setBlackBoardControlAgent(this);
        increaseSizeDMAInvocationPool(getNumberNotifiedAgents());
        getApplicationContext().publishEvent(taggedModelEvent);
    }

    public void notifyAgentResponse(NewDesignModelEvent event) throws IOException, URISyntaxException {
        if (event.getAgent() instanceof DesignAnalysisAgent) {
            processDesignAnalysisAgentResponse(event);
        }
    }

    private void processDesignModificationAgentResponse(NewDesignModelEvent event) {
        DesignModel oldDesignModel = event.getDesignModel();
        Iterator<DesignModel> iter = event.getNewDesignModels();
        if (!iter.hasNext()) {
            return;
        }
        while (iter.hasNext()) {
            DesignModel newDesignModel = iter.next();
            updateDesignModel(oldDesignModel, newDesignModel, newDesignModel.getDesignOperatorAppliedURI(), newDesignModel.getAgentInvokedURI(), newDesignModel.getAdapterUsed(), newDesignModel.getGoalReplaced(), newDesignModel.getAppliedRule());
        }
    }

    private void processDesignAnalysisAgentResponse(NewDesignModelEvent event) throws IOException, URISyntaxException {
        DesignModel oldDesignModel = event.getDesignModel();
        if (oldDesignModel.getStatus().isSolution()) {
            notifySolution(oldDesignModel, event.getAgent().getIdentifier());
        } else if (oldDesignModel.getStatus().isIOUncheckedSolution() && !isSLODMAActivated()) {
            notifySolution(oldDesignModel, event.getAgent().getIdentifier());
        } else if (oldDesignModel.getStatus().isIOUncheckedSolution() && !isProcessTarget()) {
            notifySolution(oldDesignModel, event.getAgent().getIdentifier());
        }
        if (oldDesignModel.getStatus().isInadmissible()) {
            blackBoard.tagModelBranchAsInadmissible(oldDesignModel);
        }
        blackBoard.refreshModelInViewers(oldDesignModel);
        this.notifyTaggedModel(event.getDesignModel());
    }

    private boolean isSLODMAActivated() {
        return SLODMAActivated;
    }

    private int getNumberNotifiedAgents() {
        return getApplicationContext().getBeanNamesForType(ApplicationListener.class).length;
    }

    public void setGoalTarget(URI goalTargetURI) {
        this.goalTargetURI = goalTargetURI;
    }

    public URI getGoalTarget() {
        return this.goalTargetURI;
    }

    public void setOperation(ComposerOperations operation) {
        this.operation = operation;
    }

    public ComposerOperations getOperation() {
        return this.operation;
    }

    public boolean isProcessTarget() throws URISyntaxException {
        boolean result = false;
        ComposerOperations operation = getOperation();
        URI goalTarget = getGoalTarget();
        if (goalTarget == null && (operation == ComposerOperations.RESOLVE_PROCESS || operation == ComposerOperations.RESOLVE_PROCESS_WITH_TEMPLATE || operation == ComposerOperations.RESOLVE_PROCESS_WITH_WS || operation == ComposerOperations.RESOLVE_DATAFLOW)) {
            result = true;
        }
        return result;
    }

    public synchronized void increaseSizeDMAInvocationPool(int size) {
        sizeDMAInvocationPool += size;
        logger.debug("Increased sizeDMAInvocationPool to value: " + sizeDMAInvocationPool);
    }

    public synchronized void decreaseSizeDMAInvocationPool() {
        sizeDMAInvocationPool--;
        logger.debug("Decreased sizeDMAInvocationPool to value: " + sizeDMAInvocationPool);
    }

    public synchronized int getSizeDMAInvocationPool() {
        return sizeDMAInvocationPool;
    }

    public synchronized void resetSizeDMAInvocationPool() {
        sizeDMAInvocationPool = 0;
        logger.debug("Reset sizeDMAInvocationPool to value: " + sizeDMAInvocationPool);
    }

    public boolean isFoundRequiredSolutions() {
        return foundRequiredSolutions;
    }
}
