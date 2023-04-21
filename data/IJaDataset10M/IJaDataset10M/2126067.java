package serene.validation.schema.active.components;

import serene.util.IntList;
import serene.validation.schema.active.components.APattern;
import serene.validation.schema.active.ActiveComponentVisitor;
import serene.validation.schema.active.RuleVisitor;
import serene.validation.handlers.structure.StructureHandler;
import serene.validation.handlers.structure.MinimalReduceHandler;
import serene.validation.handlers.structure.MaximalReduceHandler;
import serene.validation.handlers.structure.impl.InterleaveHandler;
import serene.validation.handlers.structure.impl.InterleaveMinimalReduceHandler;
import serene.validation.handlers.structure.impl.InterleaveMaximalReduceHandler;
import serene.validation.handlers.structure.impl.InterleaveMinimalReduceCountHandler;
import serene.validation.handlers.structure.impl.InterleaveMaximalReduceCountHandler;
import serene.validation.handlers.structure.impl.InterleaveDoubleHandler;
import serene.validation.handlers.stack.StackHandler;
import serene.validation.handlers.stack.impl.ActiveModelStackHandlerPool;
import serene.validation.handlers.stack.impl.MinimalReduceStackHandler;
import serene.validation.handlers.stack.impl.MaximalReduceStackHandler;
import serene.validation.handlers.error.ErrorCatcher;
import serene.validation.handlers.structure.impl.ActiveModelRuleHandlerPool;

public abstract class AInterleave extends MultipleChildrenAPattern implements ACompositor {

    int satisfactionIndicator;

    int saturationIndicator;

    ActiveModelStackHandlerPool stackHandlerPool;

    public AInterleave(APattern[] children, ActiveModelStackHandlerPool stackHandlerPool, ActiveModelRuleHandlerPool ruleHandlerPool) {
        super(children, ruleHandlerPool);
        this.stackHandlerPool = stackHandlerPool;
    }

    protected void asParent(APattern[] children) {
        this.children = children;
        satisfactionIndicator = 0;
        saturationIndicator = 0;
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                ((AbstractAPattern) children[i]).setParent(this);
                ((AbstractAPattern) children[i]).setChildIndex(i);
                if (children[i].getMinOccurs() > 0) ++satisfactionIndicator;
                if (children[i].getMaxOccurs() != UNBOUNDED) ++saturationIndicator;
            }
        }
    }

    public int getSatisfactionIndicator() {
        return satisfactionIndicator;
    }

    public int getSaturationIndicator() {
        return saturationIndicator;
    }

    public void accept(ActiveComponentVisitor v) {
        v.visit(this);
    }

    public void accept(RuleVisitor rv) {
        rv.visit(this);
    }

    boolean hasMultipleCardinality() {
        if (maxOccurs > 1 || maxOccurs == UNBOUNDED) return true;
        if (parent instanceof AbstractAPattern) return ((AbstractAPattern) parent).transmitsMultipleCardinality();
        return false;
    }

    boolean requiresDoubleHandler() {
        return hasMultipleCardinality() && parent instanceof AbstractAPattern && ((AbstractAPattern) parent).isInterleaved();
    }

    public StructureHandler getStructureHandler(ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        if (requiresDoubleHandler()) {
            return getDoubleHandler(errorCatcher, parent, stackHandler);
        }
        InterleaveHandler ih;
        if (hasMultipleCardinality()) {
            ih = ruleHandlerPool.getMInterleaveHandler(this, errorCatcher, parent, stackHandler);
        } else {
            ih = ruleHandlerPool.getUInterleaveHandler(this, errorCatcher, parent, stackHandler);
        }
        return ih;
    }

    public InterleaveMinimalReduceHandler getStructureHandler(ErrorCatcher errorCatcher, MinimalReduceHandler parent, StackHandler stackHandler) {
        return ruleHandlerPool.getMinimalReduceHandler(this, errorCatcher, parent, stackHandler);
    }

    public InterleaveMaximalReduceHandler getStructureHandler(ErrorCatcher errorCatcher, MaximalReduceHandler parent, StackHandler stackHandler) {
        return ruleHandlerPool.getMaximalReduceHandler(this, errorCatcher, parent, stackHandler);
    }

    InterleaveDoubleHandler getDoubleHandler(ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        InterleaveDoubleHandler sih = ruleHandlerPool.getStructureDoubleHandler(this, errorCatcher, parent, stackHandler, stackHandlerPool);
        return sih;
    }

    public InterleaveMinimalReduceCountHandler getStructureHandler(IntList minimalReduceCount, ErrorCatcher errorCatcher, MinimalReduceStackHandler stackHandler) {
        return ruleHandlerPool.getMinimalReduceCountHandler(minimalReduceCount, this, errorCatcher, stackHandler);
    }

    public InterleaveMaximalReduceCountHandler getStructureHandler(IntList maximalReduceCount, ErrorCatcher errorCatcher, MaximalReduceStackHandler stackHandler) {
        return ruleHandlerPool.getMaximalReduceCountHandler(maximalReduceCount, this, errorCatcher, stackHandler);
    }

    boolean isInterleaved() {
        return true;
    }

    public String toString() {
        String s = "AInterleave" + " min " + minOccurs + " max " + maxOccurs + " saturation " + saturationIndicator + " satisfaction " + satisfactionIndicator;
        return s;
    }
}
