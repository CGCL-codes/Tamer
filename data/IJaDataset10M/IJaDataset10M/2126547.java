package org.bs.sm;

import org.bs.sm.outputers.XMLContext;
import org.bs.sm.outputers.dot.SMDotContext;
import org.bs.sm.outputers.dot.SMDotHelper;
import org.w3c.dom.Element;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class Fork extends PseudoState {

    static final String DEFAULT_NAME = "F*";

    private TreeMap<SMStateVertex, SMTransition> mRegionToStateMap;

    Fork(SMConcurrentState parent, String sid) {
        super(parent, sid);
    }

    @Override
    final String getTypeName() {
        return "Fork";
    }

    @Override
    SMTransition addOutgoingTransition(SMBaseTrigger trigger, SMTransition t) {
        super.addOutgoingTransition(trigger, t);
        checkItIsUnary(t);
        setTarget(t);
        return t;
    }

    @Override
    void setIncomingTransition(SMStateVertex source) {
        SMConcurrentState parent = (SMConcurrentState) mParentState;
        SMStateVertex brother = parent.findSubStateContainsOrEq(source, true);
        if (brother != null) {
            throw new SMDefinitionError("In " + getTypeAndName() + ", incoming transition - " + source.getTypeAndName() + " - must come from outside concurrent state: " + parent);
        }
    }

    private void setTarget(SMTransition t) {
        SMStateVertex target = t.getBranchTarget(0);
        SMConcurrentState parent = (SMConcurrentState) mParentState;
        SMStateVertex region = parent.findSubStateContainsOrEq(target);
        if (region == null || !region.isRegion()) {
            throw new SMDefinitionError("In fork '" + this + "', " + "Target '" + target + "' is not region nor is region in concurrent state '" + parent + "'");
        }
        if (mRegionToStateMap == null) {
            mRegionToStateMap = new TreeMap<SMStateVertex, SMTransition>();
        }
        SMTransition already = mRegionToStateMap.put(region, t);
        if (already != null) {
            throw new SMDefinitionError("In fork '" + this + "', " + "Region '" + region + "' already has a target '" + already);
        }
    }

    void clear() {
        if (mRegionToStateMap != null) {
            mRegionToStateMap.clear();
        }
    }

    boolean isEmpty() {
        return mRegionToStateMap == null || mRegionToStateMap.isEmpty();
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void checkValid() {
        super.checkValid();
        if (mRegionToStateMap != null) {
            for (Map.Entry<SMStateVertex, SMTransition> e : mRegionToStateMap.entrySet()) {
                SMComplexState region = (SMComplexState) e.getKey();
                SMTransition t = e.getValue();
                SMStateVertex target = t.getBranchTarget(0);
                if (target != region) {
                    List<SMStateVertex> l = target.isAllPathTroughIsInContainer(region, false);
                    if (l != null) {
                        l.add(0, this);
                        throw new SMDefinitionError("In fork '" + this + "', " + "there is a possibility of a path ending outside or on region: " + pathToString(l));
                    }
                }
            }
        }
    }

    @Override
    public final boolean isFork() {
        return true;
    }

    @Override
    List<SMTransitionSegment> processTrigger(SMBaseTrigger trigger, SMStateVertex target, Object eventData) {
        if (target != this || trigger != SystemTrigger.FORK_EXIT) {
            return null;
        }
        super.processTrigger(trigger, target, eventData);
        List<SMTransitionSegment> path = null;
        SMConcurrentState parent = (SMConcurrentState) mParentState;
        HelpExitData fed = (HelpExitData) eventData;
        SMBaseTrigger realTrigger = fed.getRealTrigger();
        SMState beginOfPath = fed.getBeginOfPath();
        for (SMState region : parent.getSubStatesMap().values()) {
            SMTransition regionTransition = mRegionToStateMap == null ? null : mRegionToStateMap.get(region);
            LinkedList<SMTransitionSegment> tempPath = null;
            if (regionTransition != null) {
                tempPath = regionTransition.findPathThrough(tempPath, beginOfPath, this, realTrigger);
            }
            if (tempPath == null) {
                tempPath = new LinkedList<SMTransitionSegment>();
                SMTransitionSegment thisSegment = SMTransitionSegment.createSimple(beginOfPath, this, realTrigger, region);
                tempPath.addLast(thisSegment);
                LinkedList<SMTransitionSegment> tempPath2 = region.findPathThrough(tempPath, beginOfPath, this, realTrigger);
                if (tempPath2 == null) {
                    throw new SMDefinitionError("there is must some way to enter region");
                }
                tempPath = tempPath2;
            }
            path = SMTransitionSegment.add(path, tempPath);
        }
        return path;
    }

    @Override
    LinkedList<SMStateVertex> enterBegin(SMState beginOfPath, SMStateVertex innerTargetState, LinkedList<SMStateVertex> statesPath, SMStateVertex sourceState, SMBaseTrigger triggerTrigger) {
        statesPath = super.enterBegin(beginOfPath, innerTargetState, statesPath, sourceState, triggerTrigger);
        getWorld().addInternalSystemTrigger(SystemTrigger.FORK_EXIT, this, new HelpExitData(triggerTrigger, beginOfPath), true);
        return statesPath;
    }

    @Override
    LinkedList<SMTransitionSegment> findPathThrough(LinkedList<SMTransitionSegment> pathSoFar, SMState beginOfPath, SMStateVertex source, SMBaseTrigger trigger) {
        return pathSoFar;
    }

    @Override
    boolean possiblyImpassable() {
        return false;
    }

    @Override
    public void writeBody(XMLContext xmlContext, Element myNode) {
        if (mRegionToStateMap != null && !mRegionToStateMap.isEmpty()) {
            for (Map.Entry<SMStateVertex, SMTransition> e : mRegionToStateMap.entrySet()) {
                SMTransition transition = e.getValue();
                writeUnNamedTransition(xmlContext, myNode, transition);
            }
        }
        super.writeBody(xmlContext, myNode);
    }

    @Override
    public String getElementName() {
        return "Fork";
    }

    /**
     * DoingX_Fx[label="Fork|<1>1|<2>2" shape=record,width=.0];
     */
    @Override
    public String getDotLabel() {
        String label = super.getDotLabel();
        if (label.equals(DEFAULT_NAME)) {
            label = null;
        }
        SMConcurrentState parent = (SMConcurrentState) mParentState;
        return SMDotHelper.buildRecordLabelForConcurrent(label, parent);
    }

    /**
     * DoingX_Fx[label="Fork|<1>1|<2>2" shape=record,width=.0];
     * @param dotContext
     */
    @Override
    protected String getDotNoneComplexModeAttributes(SMDotContext dotContext) {
        String s1 = super.getDotNoneComplexModeAttributes(dotContext);
        String s2 = "shape=record, width=.0";
        return SMDotContext.concatenateAttributes(s1, s2);
    }

    /**
     * The syntax from record is
     *  node0:f0 -> node1:n;
     * where f0 and n are the ids we put in <>
     */
    @Override
    public String getDotNameAsSourceOfTransition(SMStateVertex target) {
        String name = super.getDotNameAsSourceOfTransition(target);
        return SMDotHelper.getRecordSubLabel(name, (SMConcurrentState) mParentState, target);
    }
}
