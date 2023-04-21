package org.bs.sm;

import org.bs.sm.outputers.XMLContext;
import org.bs.sm.outputers.dot.SMDotContext;
import org.w3c.dom.Element;
import java.util.LinkedList;

class InitialState extends PseudoState {

    private SMTransition mTransition;

    InitialState(SMCompositeState parent, String sid) {
        super(parent, sid);
    }

    @Override
    final String getTypeName() {
        return "Initial";
    }

    @Override
    protected void checkValid() {
        super.checkValid();
        checkHaveGuaranteedOutgoingTransition(mTransition);
        checkTargetIsInParent(mTransition);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    void setIncomingTransition(SMStateVertex source) {
        throw new SMDefinitionError("In " + getTypeAndName() + " no incoming transition are allowed");
    }

    @Override
    SMTransition addOutgoingTransition(SMBaseTrigger trigger, SMTransition t) {
        super.addOutgoingTransition(trigger, t);
        checkAlreadyHasOutgoingTransition(mTransition);
        checkItIsUnary(t);
        mTransition = t;
        return t;
    }

    /**
     * find a path to destination target
     * return a list of SMTransitionSegment
     * if return null then no path trough this state
     */
    @Override
    LinkedList<SMTransitionSegment> findPathThrough(LinkedList<SMTransitionSegment> pathSoFar, SMState beginOfPath, SMStateVertex source, SMBaseTrigger trigger) {
        return mTransition.findPathThrough(pathSoFar, beginOfPath, this, trigger);
    }

    @Override
    boolean possiblyImpassable() {
        return false;
    }

    @Override
    public void writeBody(XMLContext xmlContext, Element myNode) {
        if (xmlContext.getDotContext() != null) {
            xmlContext.getDotContext().addDotComment(myNode, "Initial State");
        }
        super.writeBody(xmlContext, myNode);
        writeDotBody(xmlContext, myNode);
    }

    protected void writeDotBody(XMLContext xmlContext, Element myNode) {
        writeUnNamedTransition(xmlContext, myNode, mTransition);
    }

    @Override
    public String getElementName() {
        return "Initial";
    }

    @Override
    public String getDotLabel() {
        return "";
    }

    @Override
    protected String getDotNoneComplexModeAttributes(SMDotContext dotContext) {
        String s1 = super.getDotNoneComplexModeAttributes(dotContext);
        String s2 = "shape=circle, margin=0, style=filled, color=black, width=0.1, height=0.1";
        return SMDotContext.concatenateAttributes(s1, s2);
    }

    SMStateVertex debugGetTarget() {
        if (mTransition != null) {
            return mTransition.getBranchTarget(0);
        } else {
            return null;
        }
    }
}
