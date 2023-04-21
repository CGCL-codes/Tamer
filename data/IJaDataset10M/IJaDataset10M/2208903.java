package org.jcvi.vics.compute.engine.def;

import org.jcvi.vics.compute.engine.launcher.ProcessLauncher;
import java.util.List;

/**
 * This class represents the definition of a process.  A process is executed by an
 * ILauncher implementation at runtime ... ultimately by ProcessLauncher
 *
 * @author Tareq Nabeel
 */
public class ProcessDef extends SeriesDef {

    private static final ProcessorType DEFAULT_PROCESSOR_TYPE = ProcessorType.POJO;

    private static final String DEFAULT_LOCAL_MDB_PROCESSOR = "queue/processLauncher";

    private static final String DEFAULT_POJO_PROCESSOR = ProcessLauncher.class.getName();

    private static final String DEFAULT_LOCAL_SLSB_PROCESSOR = "local/ProcessLauncherSLSB";

    /**
     * Logic for initializing Process defintiion should be contained within a DefLoader
     *
     * @param parentProcessDef parent process definition
     */
    protected ProcessDef(ProcessDef parentProcessDef) {
        setParentDef(parentProcessDef);
    }

    /**
     * Used by DefLoader
     */
    protected void validateChildActionDef(ActionDef actionDef) {
        if (actionDef == null) {
            throw new IllegalArgumentException("Child action def cannot be null");
        }
        if (!actionDef.isSeriesDef()) {
            throw new IllegalArgumentException("Process can only contain sequences and other processes");
        }
    }

    /**
     * Returns action type represented by processes
     *
     * @return action type represented by processes
     */
    public ActionType getActionType() {
        return ActionType.PROCESS;
    }

    /**
     * Looks for the action whose queueToLinkFrom equals <code>queueToLinkTo</code>
     *
     * @param queueToLinkTo the jndi queue name
     * @return the linked action
     */
    public ActionDef getLinkedAction(String queueToLinkTo) {
        return getLinkedAction(getChildActionDefs(), queueToLinkTo);
    }

    /**
     * Looks for the action whose queueToLinkFrom equals <code>queueToLinkTo</code>
     *
     * @param childDefs     list of child action definitions
     * @param queueToLinkTo JMS queue to link messaging to
     * @return returns an ActionDef object related to this pipeline
     */
    private ActionDef getLinkedAction(List<ActionDef> childDefs, String queueToLinkTo) {
        for (ActionDef actionDef : childDefs) {
            if (queueToLinkTo.equals(actionDef.getQueueToLinkFrom())) {
                return actionDef;
            }
            if (actionDef.isSeriesDef()) {
                SeriesDef seriesDef = (SeriesDef) actionDef;
                ActionDef def = getLinkedAction(seriesDef.getChildActionDefs(), queueToLinkTo);
                if (def != null) {
                    return def;
                }
            }
        }
        return null;
    }

    /**
     * Returns the default processor type for processes
     */
    protected ProcessorType getDefaultProcessorType() {
        return DEFAULT_PROCESSOR_TYPE;
    }

    /**
     * Returns the default mdb processor for processes
     */
    protected String getDefaultMdbProcessor() {
        return DEFAULT_LOCAL_MDB_PROCESSOR;
    }

    /**
     * Returns the default pojo processor for processes
     */
    protected String getDefaultPojoProcessor() {
        return DEFAULT_POJO_PROCESSOR;
    }

    /**
     * Returns the default stateless session bean processor for processes
     */
    protected String getDefaultLocalSlsbProcessor() {
        return DEFAULT_LOCAL_SLSB_PROCESSOR;
    }
}
