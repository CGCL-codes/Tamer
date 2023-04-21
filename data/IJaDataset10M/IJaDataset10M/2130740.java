package org.dbe.composer.wfengine.bpel.def.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.dbe.composer.wfengine.bpel.def.ISdlMultipleActivityContainerDef;
import org.dbe.composer.wfengine.bpel.def.ISdlSingleActivityContainerDef;
import org.dbe.composer.wfengine.bpel.def.SdlActivityDef;
import org.dbe.composer.wfengine.bpel.def.SdlBaseDef;
import org.dbe.composer.wfengine.bpel.def.SdlEventHandlersDef;
import org.dbe.composer.wfengine.bpel.def.SdlFaultHandlerDef;
import org.dbe.composer.wfengine.bpel.def.SdlFaultHandlersContainerDef;
import org.dbe.composer.wfengine.bpel.def.SdlProcessDef;
import org.dbe.composer.wfengine.bpel.def.SdlScopeCompensatorContainerDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivityFlowDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivityPickDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivityScopeDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivitySwitchDef;
import org.dbe.composer.wfengine.bpel.def.activity.SdlActivityWhileDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlLinkDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlOnAlarmDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlOnMessageDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlOtherwiseDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlSourceDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlSwitchCaseDef;
import org.dbe.composer.wfengine.bpel.def.activity.support.SdlTargetDef;

/**
 * Validation helper for link collection and testing.
 */
public class SdlLinkValidator implements ISdlValidationDefs {

    /** for deployment logging purposes */
    protected static final Logger logger = Logger.getLogger(SdlLinkValidator.class.getName());

    /** Error reporting instance. */
    private ISdlBaseErrorReporter mReporter;

    /** Map holding link composites to test for validity. */
    private HashMap mLinkMap;

    /** List of source composites to test for cycles. */
    private ArrayList mLinkSources;

    /** Process Def. */
    private SdlProcessDef mProcessDef;

    /** Root scope locations for boundary cross checks. */
    private SdlBaseDef mActyScope;

    private SdlBaseDef mFaultScope;

    private SdlBaseDef mEventScope;

    private SdlBaseDef mCompScope;

    /**
     * Ctor to set the error reporter.
     *
     * @param aReporter The reporter to set.
     */
    public SdlLinkValidator(SdlProcessDef aProcessDef, ISdlBaseErrorReporter aReporter) {
        logger.debug("SdlLinkValidator() " + aProcessDef.getName());
        mProcessDef = aProcessDef;
        setReporter(aReporter);
    }

    /**
     * Get the error reporter for this instance.
     *
     * @return IAeErrorReporter
     */
    public ISdlBaseErrorReporter getReporter() {
        return mReporter;
    }

    /**
     * Set the error reporter for this instance.
     *
     * @param aReporter The reporter to set.
     */
    public void setReporter(ISdlBaseErrorReporter aReporter) {
        mReporter = aReporter;
    }

    /**
     * Find the element's enclosing flow, or null if there is no enclosing flow.
     *
     * @return SdlActivityFlowDef
     */
    private SdlActivityFlowDef getParentFlow(SdlBaseDef aChild) {
        SdlBaseDef parent = aChild.getParent();
        if (parent != null) {
            if (parent instanceof SdlActivityFlowDef) return (SdlActivityFlowDef) parent; else return getParentFlow(parent);
        }
        return null;
    }

    /**
     * Check for cycles and illegal boundary crosses in the graph's links.
     *
     * Note: this should only be called after the validation visitor has traversed
     * the entire def graph, to ensure that all links and their sources/targets have
     * been resolved.  Wherever this hasn't occurred, the composite will be incomplete,
     * which signifies an erroneously defined link.
     */
    public void checkLinks() {
        logger.debug("checkLinks() " + getLinkMap().keySet().size());
        for (Iterator iter = getLinkMap().keySet().iterator(); iter.hasNext(); ) {
            String key = (String) iter.next();
            SdlLinkComposite comp = (SdlLinkComposite) getLinkMap().get(key);
            if (comp.isComplete()) {
                checkBoundaryCrossings(comp);
                checkBadTarget(comp);
            } else {
                logger.error("Link definition error: " + comp.getLink().getLocationPath());
                getReporter().addError(ERROR_BAD_LINK, new String[] { comp.getLink().getLocationPath() }, comp.getLink());
            }
        }
        checkLinkCycles();
    }

    /**
     * Recursively look for a cycle in the graph.
     *
     * @param aStartNode The node from which to start checking.
     * @param aNextNode The next node in the chain we're testing.
     * @param aCycle The array of links that form the cycle, otherwise null.
     * @param aNotCycle The array of links that aren't part of a cycle.
     * @param aChecked The array of nodes checked for this target.
     *
     * @return boolean True if the links eventually point back to the target.
     *
     * TODO This should be revisited and implemented with a visitor pattern
     *
     * TODO 'Automatic' (non-model) links can be part of a link cycle. Right now we detect most container
     * type cycles, but we do miss the Sequence activity when we link to an activity within the sequence.
     */
    private boolean findCycleInGraph(SdlActivityDef aStartNode, SdlActivityDef aNextNode, ArrayList aCycle, ArrayList aNotCycle, ArrayList aChecked) {
        logger.debug("findCycleInGraph()");
        boolean foundCycle = false;
        if (aNextNode != null && getLinkSource(aNextNode) != null) {
            for (Iterator iter = getLinkSource(aNextNode).getSourceConnections(); iter.hasNext(); ) {
                SdlLinkComposite link = (SdlLinkComposite) iter.next();
                if (link.isComplete()) {
                    if (link.getTarget().equals(aStartNode)) {
                        aCycle.add(link);
                        foundCycle = true;
                    } else if (!link.getTarget().equals(aNextNode) && !aChecked.contains(link.getTarget())) {
                        aChecked.add(link.getTarget());
                        if (findCycleInGraph(aStartNode, link.getTarget(), aCycle, aNotCycle, aChecked)) {
                            aCycle.add(link);
                            foundCycle = true;
                        }
                        aChecked.remove(link.getTarget());
                    } else {
                        aNotCycle.add(link);
                    }
                }
            }
        }
        if (!foundCycle) {
            foundCycle = findCycleInContainer(aStartNode, aNextNode, aCycle, aNotCycle, aChecked);
        }
        return foundCycle;
    }

    /**
     * Recursively look for a cycle in container activities of the graph.
     *
     * @param aStartNode The node from which to start checking.
     * @param aNextNode The next node in the chain we're testing.
     * @param aCycle The array of links that form the cycle, otherwise null.
     * @param aNotCycle The array of links that aren't part of a cycle.
     * @param aChecked The array of nodes checked for this target.
     *
     * @return boolean True if the links eventually point back to the target.
     */
    private boolean findCycleInContainer(SdlActivityDef aStartNode, SdlActivityDef aNextNode, ArrayList aCycle, ArrayList aNotCycle, ArrayList aChecked) {
        logger.debug("findCycleInContainer()");
        boolean foundCycle = false;
        if (aNextNode instanceof ISdlSingleActivityContainerDef) {
            SdlActivityDef def = ((ISdlSingleActivityContainerDef) aNextNode).getActivity();
            if (def != null) foundCycle = findCycleInGraph(aStartNode, def, aCycle, aNotCycle, aChecked);
            if (!foundCycle && aNextNode instanceof SdlActivityScopeDef) {
                SdlActivityScopeDef scope = (SdlActivityScopeDef) aNextNode;
                for (Iterator iter = scope.getFaultHandlerContainer().getFaultHandlers(); iter.hasNext() && !foundCycle; ) {
                    SdlFaultHandlerDef faultHandler = (SdlFaultHandlerDef) iter.next();
                    def = (SdlActivityDef) faultHandler.getActivity();
                    if (def != null) foundCycle = findCycleInGraph(aStartNode, def, aCycle, aNotCycle, aChecked);
                }
                if (!foundCycle && scope.getFaultHandlerContainer().getDefaultFaultHandler() != null) {
                    def = scope.getFaultHandlerContainer().getDefaultFaultHandler().getActivity();
                    if (def != null) foundCycle = findCycleInGraph(aStartNode, def, aCycle, aNotCycle, aChecked);
                }
                SdlEventHandlersDef evtDef = scope.getScope().getEventHandlers();
                if (!foundCycle && evtDef != null) {
                    for (Iterator iter = evtDef.getAlarmList(); iter.hasNext() && !foundCycle; ) {
                        SdlOnAlarmDef alarm = (SdlOnAlarmDef) iter.next();
                        if (alarm.getActivity() != null) foundCycle = findCycleInGraph(aStartNode, alarm.getActivity(), aCycle, aNotCycle, aChecked);
                    }
                    for (Iterator iter = evtDef.getMessageList(); iter.hasNext() && !foundCycle; ) {
                        SdlOnMessageDef msg = (SdlOnMessageDef) iter.next();
                        if (msg.getActivity() != null) foundCycle = findCycleInGraph(aStartNode, msg.getActivity(), aCycle, aNotCycle, aChecked);
                    }
                }
            }
        } else if (aNextNode instanceof ISdlMultipleActivityContainerDef) {
            Iterator iter = ((ISdlMultipleActivityContainerDef) aNextNode).getActivityList();
            while (iter.hasNext() && !foundCycle) foundCycle = findCycleInGraph(aStartNode, (SdlActivityDef) iter.next(), aCycle, aNotCycle, aChecked);
        } else if (aNextNode instanceof SdlActivityPickDef) {
            SdlActivityPickDef pick = (SdlActivityPickDef) aNextNode;
            for (Iterator iter = pick.getAlarmList(); iter.hasNext() && !foundCycle; ) {
                SdlOnAlarmDef alarm = (SdlOnAlarmDef) iter.next();
                if (alarm.getActivity() != null) foundCycle = findCycleInGraph(aStartNode, alarm.getActivity(), aCycle, aNotCycle, aChecked);
            }
            for (Iterator iter = pick.getMessageList(); iter.hasNext() && !foundCycle; ) {
                SdlOnMessageDef msg = (SdlOnMessageDef) iter.next();
                if (msg.getActivity() != null) foundCycle = findCycleInGraph(aStartNode, msg.getActivity(), aCycle, aNotCycle, aChecked);
            }
        } else if (aNextNode instanceof SdlActivitySwitchDef) {
            SdlActivitySwitchDef swtch = (SdlActivitySwitchDef) aNextNode;
            for (Iterator iter = swtch.getCaseList(); iter.hasNext() && !foundCycle; ) {
                SdlSwitchCaseDef switchCase = (SdlSwitchCaseDef) iter.next();
                if (switchCase.getActivity() != null) {
                    foundCycle = findCycleInGraph(aStartNode, switchCase.getActivity(), aCycle, aNotCycle, aChecked);
                }
            }
            if (swtch.getOtherwise() != null) {
                SdlOtherwiseDef otherwise = (SdlOtherwiseDef) swtch.getOtherwise();
                if (otherwise.getActivity() != null) {
                    foundCycle = findCycleInGraph(aStartNode, otherwise.getActivity(), aCycle, aNotCycle, aChecked);
                }
            }
        }
        return foundCycle;
    }

    /**
     * Check for cycles in the graph's links.
     */
    private void checkLinkCycles() {
        logger.debug("checkLinkCycles() " + getLinkSources().size());
        ArrayList flagged = new ArrayList();
        ArrayList checked = new ArrayList();
        if (getLinkSources() != null && getLinkSources().size() > 0) {
            Iterator iter = getLinkSources().iterator();
            while (iter.hasNext()) {
                ArrayList cycle = new ArrayList();
                ArrayList notCycle = new ArrayList();
                SdlActivityDef targetNode = ((SdlLinkSource) iter.next()).getSourceDef();
                if (findCycleInGraph(targetNode, targetNode, cycle, notCycle, checked)) {
                    Iterator iter2 = cycle.iterator();
                    while (iter2.hasNext()) {
                        SdlLinkDef link = ((SdlLinkComposite) iter2.next()).getLink();
                        if (!flagged.contains(link)) {
                            logger.error("Link " + link.getLocationPath() + " is part of a cycle.");
                            getReporter().addError(ERROR_LINK_CYCLE, new String[] { link.getLocationPath() }, link);
                            flagged.add(link);
                        }
                    }
                }
                checked.add(targetNode);
            }
        }
    }

    /**
     * Returns a string array containing the appropriate error text.
     *
     * @param aParent Node type involved in the error.
     *
     * return String
     */
    private String getCrossingError(SdlBaseDef aParent) {
        if (aParent instanceof SdlActivityWhileDef) {
            return " a While activity ";
        } else if (aParent instanceof SdlActivityScopeDef && ((SdlActivityScopeDef) aParent).getVariableAccessSerializable()) {
            return " a serializable Scope activity ";
        } else if (aParent instanceof SdlFaultHandlersContainerDef) {
            return " a Fault Handler ";
        } else if (aParent instanceof SdlEventHandlersDef) {
            return " a Event Handler ";
        } else if (aParent instanceof SdlScopeCompensatorContainerDef) {
            return " a Compensation Handler ";
        }
        logger.warn("UNKNOWN BOUNDARY CROSSING PROBLEM FOR " + aParent.getClass().toString());
        return "UNKNOWN BOUNDARY CROSSING PROBLEM FOR " + aParent.getClass().toString();
    }

    /**
     * Determine whether or not this node resides in an uncrossable container.
     *
     * @param aNode The node to check.
     *
     * @return SdlBaseDef A While, etc., activity that has uncrossable boundary.
     */
    private SdlBaseDef getInnermostUncrossableContainer(SdlBaseDef aNode) {
        logger.debug("getInnermostUncrossableContainer()");
        if (mActyScope == null) {
            mActyScope = getProcessDefNode();
            mFaultScope = getProcessDefNode().getFaultHandlerContainer();
            mEventScope = getProcessDefNode().getEventHandlers();
            mCompScope = getProcessDefNode().getCompensatorContainer();
        }
        SdlBaseDef parent = aNode.getParent();
        if (parent == null) {
            return null;
        } else if (parent.equals(mActyScope) || parent.equals(mFaultScope) || parent.equals(mEventScope) || parent.equals(mCompScope)) {
            return null;
        } else if (parent instanceof SdlActivityWhileDef || parent instanceof SdlFaultHandlersContainerDef || parent instanceof SdlScopeCompensatorContainerDef || parent instanceof SdlEventHandlersDef || (parent instanceof SdlActivityScopeDef && ((SdlActivityScopeDef) parent).getVariableAccessSerializable())) {
            return parent;
        } else {
            return getInnermostUncrossableContainer(parent);
        }
    }

    /**
     * Verify that a link doesn't cross an uncrossable boundary.  Flag an error
     * if so.
     *
     * @param aLink The link to check.
     */
    private void checkBoundaryCrossings(SdlLinkComposite aLink) {
        logger.debug("checkBoundaryCrossings()");
        SdlActivityDef src = aLink.getSource();
        SdlActivityDef dst = aLink.getTarget();
        SdlBaseDef srcParent = getInnermostUncrossableContainer(src);
        SdlBaseDef dstParent = getInnermostUncrossableContainer(dst);
        if (srcParent != null) {
            if (srcParent instanceof SdlFaultHandlersContainerDef) return;
            if (dstParent == null || (dstParent == null && !dstParent.equals(srcParent))) {
                logger.error("Link " + aLink.getLink().getLocationPath() + " illegally crosses " + getCrossingError(dstParent) + " boundary.");
                getReporter().addError(ERROR_LINK_CROSSING, new String[] { aLink.getLink().getLocationPath(), getCrossingError(srcParent) }, aLink.getLink());
            }
        } else if (dstParent != null) {
            if ((dstParent instanceof SdlFaultHandlersContainerDef) || (srcParent == null || (srcParent == null && !srcParent.equals(dstParent)))) {
                logger.error("Link " + aLink.getLink().getLocationPath() + " illegally crosses " + getCrossingError(dstParent) + " boundary.");
                getReporter().addError(ERROR_LINK_CROSSING, new String[] { aLink.getLink().getLocationPath(), getCrossingError(dstParent) }, aLink.getLink());
            }
        }
    }

    /**
     * Verify that link target is not nested in src.
     * @param aLink The link to check.
     */
    private void checkBadTarget(SdlLinkComposite aLink) {
        logger.debug("checkBadTarget()");
        SdlActivityDef src = aLink.getSource();
        SdlActivityDef dst = aLink.getTarget();
        for (SdlBaseDef dstModel = dst; dstModel != null; dstModel = dstModel.getParent()) {
            if (dstModel == src) {
                logger.error("Error: " + ERROR_SCOPE_LINK + " " + aLink.getLink().getLocationPath());
                getReporter().addError(ERROR_SCOPE_LINK, new String[] { aLink.getLink().getLocationPath() }, aLink.getLink());
                break;
            }
        }
    }

    /**
     * Find an SdlLinkDef by name within an enclosing Flow.
     *
     * @param aLinkName The name of the link to search for.
     * @param aDef The def object 'above' which the search starts.
     *
     * @return SdlLinkDef
     */
    public SdlLinkDef findLink(String aLinkName, SdlBaseDef aDef) {
        logger.debug("findLink() " + aLinkName);
        SdlActivityFlowDef parent = getParentFlow(aDef);
        while (parent != null) {
            for (Iterator iter = parent.getLinkList(); iter.hasNext(); ) {
                SdlLinkDef link = (SdlLinkDef) iter.next();
                if (link.getName().equals(aLinkName)) {
                    return (link);
                }
            }
            parent = getParentFlow(parent);
        }
        return null;
    }

    /**
     * Get the map used to manage the SdlLinkComposite instances.
     *
     * @return HashMap
     */
    public HashMap getLinkMap() {
        if (mLinkMap == null) {
            mLinkMap = new HashMap();
        }
        return mLinkMap;
    }

    /**
     * Get the list used to manage the SdlLinkSource instances.
     *
     * @return ArrayList
     */
    public ArrayList getLinkSources() {
        if (mLinkSources == null) {
            mLinkSources = new ArrayList();
        }
        return mLinkSources;
    }

    /**
     * Create a link map key from the parent flow's location path and link name.
     *
     * @param aDef The object whose parent flow is used.
     * @param aLinkName The link name used.
     *
     * @return String
     */
    protected String makeLinkMapKey(SdlBaseDef aDef, String aLinkName) {
        SdlLinkDef link = findLink(aLinkName, aDef);
        if (link != null) {
            return link.getLocationPath();
        } else {
            return null;
        }
    }

    /**
     * Get the link composite from the map, if there, otherwise create it.
     *
     * @param aKey Key to search the map.
     *
     * @return SdlLinkComposite
     */
    protected SdlLinkComposite getLinkComposite(String aKey) {
        SdlLinkComposite comp = (SdlLinkComposite) getLinkMap().get(aKey);
        if (comp == null) {
            comp = new SdlLinkComposite(aKey);
        }
        return comp;
    }

    /**
     * Add an SdlLinkDef to the map.
     *
     * @param aLink The link to add.
     */
    public void addLink(SdlLinkDef aLink) {
        SdlLinkComposite comp = getLinkComposite(aLink.getLocationPath());
        comp.mLink = aLink;
    }

    /**
     * Add a source activity to the map.
     *
     * @param aSource The source to add.
     * @param aDef The activity def that owns the source.
     */
    public void addSource(SdlSourceDef aSource, SdlActivityDef aDef) {
        logger.debug("addSource() " + aSource.getLocationPath());
        String key = makeLinkMapKey(aDef, aSource.getLinkName());
        if (key != null) {
            SdlLinkComposite comp = getLinkComposite(key);
            if (comp.mSource != null) {
                logger.error("Link " + comp.getLink().getLocationPath() + " has more than one source activity.");
                getReporter().addError(ERROR_MULTI_SRC_LINK, new String[] { comp.getLink().getLocationPath() }, comp.getLink());
                return;
            }
            comp.mSource = aDef;
            SdlLinkSource src = getLinkSource(aDef);
            if (src == null) {
                src = new SdlLinkSource(aDef);
                addLinkSource(src);
            }
            src.addConnection(comp);
        }
    }

    /**
     * Add a target activity to the map.
     *
     * @param aTarget The target to add.
     * @param aDef The activity def that owns the target.
     */
    public void addTarget(SdlTargetDef aTarget, SdlActivityDef aDef) {
        logger.debug("addTarget() " + aTarget.getLocationPath());
        String key = makeLinkMapKey(aDef, aTarget.getLinkName());
        if (key != null) {
            SdlLinkComposite comp = getLinkComposite(key);
            if (comp.mTarget != null) {
                logger.error("Link " + comp.getLink().getLocationPath() + " has more than one target activity.");
                getReporter().addError(ERROR_MULTI_TARGET_LINK, new String[] { comp.getLink().getLocationPath() }, comp.getLink());
                return;
            }
            comp.mTarget = aDef;
        }
    }

    /**
     * Search for the link source that corresponds to the def provided.
     *
     * @param aDef The source def to search for.
     *
     * @return SdlLinkSource
     */
    private SdlLinkSource getLinkSource(SdlActivityDef aDef) {
        for (Iterator iter = getLinkSources().iterator(); iter.hasNext(); ) {
            SdlLinkSource src = (SdlLinkSource) iter.next();
            if (aDef.equals(src.getSourceDef())) {
                return src;
            }
        }
        return null;
    }

    /**
     * Add a link source instance to the list.
     *
     * @param aSrc The link source to add.
     */
    private void addLinkSource(SdlLinkSource aSrc) {
        if (!getLinkSources().contains(aSrc)) {
            getLinkSources().add(aSrc);
        }
    }

    /**
     * Accessor for the root process def.
     */
    private SdlProcessDef getProcessDefNode() {
        return mProcessDef;
    }

    /**
     * Inner class to facilitate cycle checking.
     *
     */
    public class SdlLinkSource {

        /** List of link composites for which this instance is a source. */
        private ArrayList mSources = new ArrayList();

        /** Activity associated with this source. */
        private SdlActivityDef mSource;

        /**
         * Ctor to set the source def.
         *
         * @param aSource The source activity to set.
         */
        public SdlLinkSource(SdlActivityDef aSource) {
            mSource = aSource;
        }

        /**
         * Add a link / connection to this source's list of links.
         *
         * @param aConnection The link to add.
         */
        public void addConnection(SdlLinkComposite aConnection) {
            mSources.add(aConnection);
        }

        /**
         * Get the iterator over current list of link sources.
         *
         * @return Iterator
         */
        public Iterator getSourceConnections() {
            return mSources.iterator();
        }

        /**
         * Get the activity associated with this list of source links.
         *
         * @return SdlActivityDef
         */
        public SdlActivityDef getSourceDef() {
            return mSource;
        }
    }

    /**
     * Composite link inner class used to manage validation of link definitions.
     */
    public class SdlLinkComposite {

        private SdlActivityDef mSource;

        private SdlActivityDef mTarget;

        private SdlLinkDef mLink;

        /**
         * Create a composite with link name.
         *
         * @param aName The name of the link for this composite.
         */
        public SdlLinkComposite(String aName) {
            getLinkMap().put(aName, this);
        }

        /**
         * Indicate whether the composite has all its parts.
         *
         * @return boolean <code>true</code> if the source, target and link elements are defined.
         */
        public boolean isComplete() {
            if (mSource == null || mTarget == null || mLink == null) {
                return false;
            }
            return true;
        }

        /**
         * Get the link for this composite.
         *
         * @return SdlLinkDef
         */
        public SdlLinkDef getLink() {
            return mLink;
        }

        /**
         * Get the source node for this composite.
         *
         * @return SdlActivityDef
         */
        public SdlActivityDef getSource() {
            return mSource;
        }

        /**
         * Get the target node for this composite.
         *
         * @return SdlActivityDef
         */
        public SdlActivityDef getTarget() {
            return mTarget;
        }
    }
}
