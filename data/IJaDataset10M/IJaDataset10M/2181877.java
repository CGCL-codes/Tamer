package serene.validation.handlers.structure.impl;

import serene.util.IntList;
import serene.validation.schema.active.components.APattern;
import serene.validation.schema.active.components.AExceptPattern;
import serene.validation.schema.active.components.MultipleChildrenAPattern;
import serene.validation.schema.active.components.AElement;
import serene.validation.schema.active.components.AAttribute;
import serene.validation.schema.active.components.AChoicePattern;
import serene.validation.schema.active.components.AGroup;
import serene.validation.schema.active.components.AInterleave;
import serene.validation.schema.active.components.AGrammar;
import serene.validation.schema.active.components.ARef;
import serene.validation.schema.active.components.AListPattern;
import serene.validation.handlers.structure.StructureHandler;
import serene.validation.handlers.structure.ChildEventHandler;
import serene.validation.handlers.stack.StackHandler;
import serene.validation.handlers.stack.impl.ActiveModelStackHandlerPool;
import serene.validation.handlers.stack.impl.MinimalReduceStackHandler;
import serene.validation.handlers.stack.impl.MaximalReduceStackHandler;
import serene.validation.handlers.error.ErrorCatcher;
import serene.validation.handlers.content.util.ActiveInputDescriptor;
import serene.validation.handlers.content.util.InputStackDescriptor;
import serene.Reusable;

public class ActiveModelRuleHandlerPool implements Reusable, RuleHandlerRecycler {

    RuleHandlerPool pool;

    int particleHandlerCreated;

    int particleHandlerRequested;

    int particleHandlerRecycled;

    int particleHandlerMaxSize;

    int particleHandlerFree;

    int particleHandlerMinFree;

    ParticleHandler[] particleHandler;

    int choiceHandlerCreated;

    int choiceHandlerRequested;

    int choiceHandlerRecycled;

    int choiceHandlerMaxSize;

    int choiceHandlerFree;

    int choiceHandlerMinFree;

    ChoiceHandler[] choiceHandler;

    int groupHandlerCreated;

    int groupHandlerRequested;

    int groupHandlerRecycled;

    int groupHandlerMaxSize;

    int groupHandlerFree;

    int groupHandlerMinFree;

    GroupHandler[] groupHandler;

    int grammarHandlerMaxSize;

    int grammarHandlerFree;

    int grammarHandlerMinFree;

    GrammarHandler[] grammarHandler;

    int refHandlerMaxSize;

    int refHandlerFree;

    int refHandlerMinFree;

    RefHandler[] refHandler;

    int uinterleaveHandlerMaxSize;

    int uinterleaveHandlerFree;

    int uinterleaveHandlerMinFree;

    UInterleaveHandler[] uinterleaveHandler;

    int minterleaveHandlerMaxSize;

    int minterleaveHandlerFree;

    int minterleaveHandlerMinFree;

    MInterleaveHandler[] minterleaveHandler;

    int sinterleaveHandlerMaxSize;

    int sinterleaveHandlerFree;

    int sinterleaveHandlerMinFree;

    SInterleaveHandler[] sinterleaveHandler;

    int elementHandlerCreated;

    int elementHandlerRequested;

    int elementHandlerRecycled;

    int elementHandlerMaxSize;

    int elementHandlerFree;

    int elementHandlerMinFree;

    ElementHandler[] elementHandler;

    int attributeHandlerMaxSize;

    int attributeHandlerFree;

    int attributeHandlerMinFree;

    AttributeHandler[] attributeHandler;

    int exceptPatternHandlerMaxSize;

    int exceptPatternHandlerFree;

    int exceptPatternHandlerMinFree;

    ExceptPatternHandler[] exceptPatternHandler;

    int listPatternHandlerMaxSize;

    int listPatternHandlerFree;

    int listPatternHandlerMinFree;

    ListPatternHandler[] listPatternHandler;

    int groupDoubleHandlerMaxSize;

    int groupDoubleHandlerFree;

    int groupDoubleHandlerMinFree;

    GroupDoubleHandler[] groupDoubleHandler;

    int interleaveDoubleHandlerMaxSize;

    int interleaveDoubleHandlerFree;

    int interleaveDoubleHandlerMinFree;

    InterleaveDoubleHandler[] interleaveDoubleHandler;

    int groupMinimalReduceCountHandlerMaxSize;

    int groupMinimalReduceCountHandlerFree;

    int groupMinimalReduceCountHandlerMinFree;

    GroupMinimalReduceCountHandler[] groupMinimalReduceCountHandler;

    int groupMaximalReduceCountHandlerMaxSize;

    int groupMaximalReduceCountHandlerFree;

    int groupMaximalReduceCountHandlerMinFree;

    GroupMaximalReduceCountHandler[] groupMaximalReduceCountHandler;

    int interleaveMinimalReduceCountHandlerMaxSize;

    int interleaveMinimalReduceCountHandlerFree;

    int interleaveMinimalReduceCountHandlerMinFree;

    InterleaveMinimalReduceCountHandler[] interleaveMinimalReduceCountHandler;

    int interleaveMaximalReduceCountHandlerMaxSize;

    int interleaveMaximalReduceCountHandlerFree;

    int interleaveMaximalReduceCountHandlerMinFree;

    InterleaveMaximalReduceCountHandler[] interleaveMaximalReduceCountHandler;

    int grammarMinimalReduceHandlerMaxSize;

    int grammarMinimalReduceHandlerFree;

    int grammarMinimalReduceHandlerMinFree;

    GrammarMinimalReduceHandler[] grammarMinimalReduceHandler;

    int grammarMaximalReduceHandlerMaxSize;

    int grammarMaximalReduceHandlerFree;

    int grammarMaximalReduceHandlerMinFree;

    GrammarMaximalReduceHandler[] grammarMaximalReduceHandler;

    int refMinimalReduceHandlerMaxSize;

    int refMinimalReduceHandlerFree;

    int refMinimalReduceHandlerMinFree;

    RefMinimalReduceHandler[] refMinimalReduceHandler;

    int refMaximalReduceHandlerMaxSize;

    int refMaximalReduceHandlerFree;

    int refMaximalReduceHandlerMinFree;

    RefMaximalReduceHandler[] refMaximalReduceHandler;

    int choiceMinimalReduceHandlerMaxSize;

    int choiceMinimalReduceHandlerFree;

    int choiceMinimalReduceHandlerMinFree;

    ChoiceMinimalReduceHandler[] choiceMinimalReduceHandler;

    int choiceMaximalReduceHandlerMaxSize;

    int choiceMaximalReduceHandlerFree;

    int choiceMaximalReduceHandlerMinFree;

    ChoiceMaximalReduceHandler[] choiceMaximalReduceHandler;

    int groupMinimalReduceHandlerMaxSize;

    int groupMinimalReduceHandlerFree;

    int groupMinimalReduceHandlerMinFree;

    GroupMinimalReduceHandler[] groupMinimalReduceHandler;

    int groupMaximalReduceHandlerMaxSize;

    int groupMaximalReduceHandlerFree;

    int groupMaximalReduceHandlerMinFree;

    GroupMaximalReduceHandler[] groupMaximalReduceHandler;

    int interleaveMinimalReduceHandlerMaxSize;

    int interleaveMinimalReduceHandlerFree;

    int interleaveMinimalReduceHandlerMinFree;

    InterleaveMinimalReduceHandler[] interleaveMinimalReduceHandler;

    int interleaveMaximalReduceHandlerMaxSize;

    int interleaveMaximalReduceHandlerFree;

    int interleaveMaximalReduceHandlerMinFree;

    InterleaveMaximalReduceHandler[] interleaveMaximalReduceHandler;

    ActiveInputDescriptor activeInputDescriptor;

    InputStackDescriptor inputStackDescriptor;

    boolean full;

    public ActiveModelRuleHandlerPool(RuleHandlerPool pool) {
        this.pool = pool;
        particleHandlerMaxSize = 100;
        choiceHandlerMaxSize = 50;
        groupHandlerMaxSize = 50;
        grammarHandlerMaxSize = 50;
        refHandlerMaxSize = 50;
        uinterleaveHandlerMaxSize = 50;
        minterleaveHandlerMaxSize = 50;
        sinterleaveHandlerMaxSize = 50;
        elementHandlerMaxSize = 100;
        attributeHandlerMaxSize = 50;
        exceptPatternHandlerMaxSize = 50;
        listPatternHandlerMaxSize = 50;
        groupDoubleHandlerMaxSize = 50;
        interleaveDoubleHandlerMaxSize = 50;
        groupMinimalReduceCountHandlerMaxSize = 50;
        groupMaximalReduceCountHandlerMaxSize = 50;
        interleaveMinimalReduceCountHandlerMaxSize = 50;
        interleaveMaximalReduceCountHandlerMaxSize = 50;
        grammarMinimalReduceHandlerMaxSize = 50;
        grammarMaximalReduceHandlerMaxSize = 50;
        refMinimalReduceHandlerMaxSize = 50;
        refMaximalReduceHandlerMaxSize = 50;
        choiceMinimalReduceHandlerMaxSize = 50;
        choiceMaximalReduceHandlerMaxSize = 50;
        groupMinimalReduceHandlerMaxSize = 50;
        groupMaximalReduceHandlerMaxSize = 50;
        interleaveMinimalReduceHandlerMaxSize = 50;
        interleaveMaximalReduceHandlerMaxSize = 50;
        full = false;
    }

    public int getParticleHandlerRequested() {
        return particleHandlerRequested;
    }

    public void recycle() {
        if (full) releaseHandlers();
        pool.recycle(this);
    }

    public void fill(ActiveInputDescriptor activeInputDescriptor, InputStackDescriptor inputStackDescriptor) {
        this.activeInputDescriptor = activeInputDescriptor;
        this.inputStackDescriptor = inputStackDescriptor;
        if (pool != null) {
            pool.fill(this, particleHandler, choiceHandler, groupHandler, grammarHandler, refHandler, uinterleaveHandler, minterleaveHandler, sinterleaveHandler, elementHandler, attributeHandler, exceptPatternHandler, listPatternHandler, groupDoubleHandler, interleaveDoubleHandler, groupMinimalReduceCountHandler, groupMaximalReduceCountHandler, interleaveMinimalReduceCountHandler, interleaveMaximalReduceCountHandler, grammarMinimalReduceHandler, grammarMaximalReduceHandler, refMinimalReduceHandler, refMaximalReduceHandler, choiceMinimalReduceHandler, choiceMaximalReduceHandler, groupMinimalReduceHandler, groupMaximalReduceHandler, interleaveMinimalReduceHandler, interleaveMaximalReduceHandler);
        } else {
            particleHandler = new ParticleHandler[30];
            choiceHandler = new ChoiceHandler[10];
            groupHandler = new GroupHandler[10];
            grammarHandler = new GrammarHandler[10];
            refHandler = new RefHandler[10];
            uinterleaveHandler = new UInterleaveHandler[10];
            minterleaveHandler = new MInterleaveHandler[10];
            sinterleaveHandler = new SInterleaveHandler[10];
            elementHandler = new ElementHandler[10];
            attributeHandler = new AttributeHandler[10];
            exceptPatternHandler = new ExceptPatternHandler[10];
            listPatternHandler = new ListPatternHandler[10];
            groupDoubleHandler = new GroupDoubleHandler[10];
            interleaveDoubleHandler = new InterleaveDoubleHandler[10];
            groupMinimalReduceCountHandler = new GroupMinimalReduceCountHandler[5];
            groupMaximalReduceCountHandler = new GroupMaximalReduceCountHandler[5];
            interleaveMinimalReduceCountHandler = new InterleaveMinimalReduceCountHandler[5];
            interleaveMaximalReduceCountHandler = new InterleaveMaximalReduceCountHandler[5];
            grammarMinimalReduceHandler = new GrammarMinimalReduceHandler[5];
            grammarMaximalReduceHandler = new GrammarMaximalReduceHandler[5];
            refMinimalReduceHandler = new RefMinimalReduceHandler[5];
            refMaximalReduceHandler = new RefMaximalReduceHandler[5];
            choiceMinimalReduceHandler = new ChoiceMinimalReduceHandler[5];
            choiceMaximalReduceHandler = new ChoiceMaximalReduceHandler[5];
            groupMinimalReduceHandler = new GroupMinimalReduceHandler[5];
            groupMaximalReduceHandler = new GroupMaximalReduceHandler[5];
            interleaveMinimalReduceHandler = new InterleaveMinimalReduceHandler[5];
            interleaveMaximalReduceHandler = new InterleaveMaximalReduceHandler[5];
        }
        full = true;
    }

    void initFilled(int particleHandlerFillCount, int choiceHandlerFillCount, int groupHandlerFillCount, int grammarHandlerFillCount, int refHandlerFillCount, int uinterleaveHandlerFillCount, int minterleaveHandlerFillCount, int sinterleaveHandlerFillCount, int elementHandlerFillCount, int attributeHandlerFillCount, int exceptPatternHandlerFillCount, int listPatternHandlerFillCount, int groupDoubleHandlerFillCount, int interleaveDoubleHandlerFillCount, int groupMinimalReduceCountHandlerFillCount, int groupMaximalReduceCountHandlerFillCount, int interleaveMinimalReduceCountHandlerFillCount, int interleaveMaximalReduceCountHandlerFillCount, int grammarMinimalReduceHandlerFillCount, int grammarMaximalReduceHandlerFillCount, int refMinimalReduceHandlerFillCount, int refMaximalReduceHandlerFillCount, int choiceMinimalReduceHandlerFillCount, int choiceMaximalReduceHandlerFillCount, int groupMinimalReduceHandlerFillCount, int groupMaximalReduceHandlerFillCount, int interleaveMinimalReduceHandlerFillCount, int interleaveMaximalReduceHandlerFillCount) {
        particleHandlerFree = particleHandlerFillCount;
        particleHandlerMinFree = particleHandlerFree;
        for (int i = 0; i < particleHandlerFree; i++) {
            particleHandler[i].init(activeInputDescriptor, this);
        }
        choiceHandlerFree = choiceHandlerFillCount;
        choiceHandlerMinFree = choiceHandlerFree;
        for (int i = 0; i < choiceHandlerFree; i++) {
            choiceHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        groupHandlerFree = groupHandlerFillCount;
        groupHandlerMinFree = groupHandlerFree;
        for (int i = 0; i < groupHandlerFree; i++) {
            groupHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        grammarHandlerFree = grammarHandlerFillCount;
        grammarHandlerMinFree = grammarHandlerFree;
        for (int i = 0; i < grammarHandlerFree; i++) {
            grammarHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        uinterleaveHandlerFree = uinterleaveHandlerFillCount;
        uinterleaveHandlerMinFree = uinterleaveHandlerFree;
        for (int i = 0; i < uinterleaveHandlerFree; i++) {
            uinterleaveHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        minterleaveHandlerFree = minterleaveHandlerFillCount;
        minterleaveHandlerMinFree = minterleaveHandlerFree;
        for (int i = 0; i < minterleaveHandlerFree; i++) {
            minterleaveHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        sinterleaveHandlerFree = sinterleaveHandlerFillCount;
        sinterleaveHandlerMinFree = sinterleaveHandlerFree;
        for (int i = 0; i < sinterleaveHandlerFree; i++) {
            sinterleaveHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        refHandlerFree = refHandlerFillCount;
        refHandlerMinFree = refHandlerFree;
        for (int i = 0; i < refHandlerFree; i++) {
            refHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        elementHandlerFree = elementHandlerFillCount;
        elementHandlerMinFree = elementHandlerFree;
        for (int i = 0; i < elementHandlerFree; i++) {
            elementHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        attributeHandlerFree = attributeHandlerFillCount;
        attributeHandlerMinFree = attributeHandlerFree;
        for (int i = 0; i < attributeHandlerFree; i++) {
            attributeHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        exceptPatternHandlerFree = exceptPatternHandlerFillCount;
        exceptPatternHandlerMinFree = exceptPatternHandlerFree;
        for (int i = 0; i < exceptPatternHandlerFree; i++) {
            exceptPatternHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        listPatternHandlerFree = listPatternHandlerFillCount;
        listPatternHandlerMinFree = listPatternHandlerFree;
        for (int i = 0; i < listPatternHandlerFree; i++) {
            listPatternHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        groupDoubleHandlerFree = groupDoubleHandlerFillCount;
        groupDoubleHandlerMinFree = groupDoubleHandlerFree;
        for (int i = 0; i < groupDoubleHandlerFree; i++) {
            groupDoubleHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        interleaveDoubleHandlerFree = interleaveDoubleHandlerFillCount;
        interleaveDoubleHandlerMinFree = interleaveDoubleHandlerFree;
        for (int i = 0; i < interleaveDoubleHandlerFree; i++) {
            interleaveDoubleHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        groupMinimalReduceCountHandlerFree = groupMinimalReduceCountHandlerFillCount;
        groupMinimalReduceCountHandlerMinFree = groupMinimalReduceCountHandlerFree;
        for (int i = 0; i < groupMinimalReduceCountHandlerFree; i++) {
            groupMinimalReduceCountHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        groupMaximalReduceCountHandlerFree = groupMaximalReduceCountHandlerFillCount;
        groupMaximalReduceCountHandlerMinFree = groupMaximalReduceCountHandlerFree;
        for (int i = 0; i < groupMaximalReduceCountHandlerFree; i++) {
            groupMaximalReduceCountHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        interleaveMinimalReduceCountHandlerFree = interleaveMinimalReduceCountHandlerFillCount;
        interleaveMinimalReduceCountHandlerMinFree = interleaveMinimalReduceCountHandlerFree;
        for (int i = 0; i < interleaveMinimalReduceCountHandlerFree; i++) {
            interleaveMinimalReduceCountHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        interleaveMaximalReduceCountHandlerFree = interleaveMaximalReduceCountHandlerFillCount;
        interleaveMaximalReduceCountHandlerMinFree = interleaveMaximalReduceCountHandlerFree;
        for (int i = 0; i < interleaveMaximalReduceCountHandlerFree; i++) {
            interleaveMaximalReduceCountHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        grammarMinimalReduceHandlerFree = grammarMinimalReduceHandlerFillCount;
        grammarMinimalReduceHandlerMinFree = grammarMinimalReduceHandlerFree;
        for (int i = 0; i < grammarMinimalReduceHandlerFree; i++) {
            grammarMinimalReduceHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        grammarMaximalReduceHandlerFree = grammarMaximalReduceHandlerFillCount;
        grammarMaximalReduceHandlerMinFree = grammarMaximalReduceHandlerFree;
        for (int i = 0; i < grammarMaximalReduceHandlerFree; i++) {
            grammarMaximalReduceHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        refMinimalReduceHandlerFree = refMinimalReduceHandlerFillCount;
        refMinimalReduceHandlerMinFree = refMinimalReduceHandlerFree;
        for (int i = 0; i < refMinimalReduceHandlerFree; i++) {
            refMinimalReduceHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        refMaximalReduceHandlerFree = refMaximalReduceHandlerFillCount;
        refMaximalReduceHandlerMinFree = refMaximalReduceHandlerFree;
        for (int i = 0; i < refMaximalReduceHandlerFree; i++) {
            refMaximalReduceHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        choiceMinimalReduceHandlerFree = choiceMinimalReduceHandlerFillCount;
        choiceMinimalReduceHandlerMinFree = choiceMinimalReduceHandlerFree;
        for (int i = 0; i < choiceMinimalReduceHandlerFree; i++) {
            choiceMinimalReduceHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        choiceMaximalReduceHandlerFree = choiceMaximalReduceHandlerFillCount;
        choiceMaximalReduceHandlerMinFree = choiceMaximalReduceHandlerFree;
        for (int i = 0; i < choiceMaximalReduceHandlerFree; i++) {
            choiceMaximalReduceHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        groupMinimalReduceHandlerFree = groupMinimalReduceHandlerFillCount;
        groupMinimalReduceHandlerMinFree = groupMinimalReduceHandlerFree;
        for (int i = 0; i < groupMinimalReduceHandlerFree; i++) {
            groupMinimalReduceHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        groupMaximalReduceHandlerFree = groupMaximalReduceHandlerFillCount;
        groupMaximalReduceHandlerMinFree = groupMaximalReduceHandlerFree;
        for (int i = 0; i < groupMaximalReduceHandlerFree; i++) {
            groupMaximalReduceHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        interleaveMinimalReduceHandlerFree = interleaveMinimalReduceHandlerFillCount;
        interleaveMinimalReduceHandlerMinFree = interleaveMinimalReduceHandlerFree;
        for (int i = 0; i < interleaveMinimalReduceHandlerFree; i++) {
            interleaveMinimalReduceHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
        interleaveMaximalReduceHandlerFree = interleaveMaximalReduceHandlerFillCount;
        interleaveMaximalReduceHandlerMinFree = interleaveMaximalReduceHandlerFree;
        for (int i = 0; i < interleaveMaximalReduceHandlerFree; i++) {
            interleaveMaximalReduceHandler[i].init(this, activeInputDescriptor, inputStackDescriptor);
        }
    }

    public void releaseHandlers() {
        pool.recycle(particleHandlerFree, particleHandlerFree - particleHandlerMinFree, particleHandler, choiceHandlerFree, choiceHandlerFree - choiceHandlerMinFree, choiceHandler, groupHandlerFree, groupHandlerFree - groupHandlerMinFree, groupHandler, grammarHandlerFree, grammarHandlerFree - grammarHandlerMinFree, grammarHandler, refHandlerFree, refHandlerFree - refHandlerMinFree, refHandler, uinterleaveHandlerFree, uinterleaveHandlerFree - uinterleaveHandlerMinFree, uinterleaveHandler, minterleaveHandlerFree, minterleaveHandlerFree - minterleaveHandlerMinFree, minterleaveHandler, sinterleaveHandlerFree, sinterleaveHandlerFree - sinterleaveHandlerMinFree, sinterleaveHandler, elementHandlerFree, elementHandlerFree - elementHandlerMinFree, elementHandler, attributeHandlerFree, attributeHandlerFree - attributeHandlerMinFree, attributeHandler, exceptPatternHandlerFree, exceptPatternHandlerFree - exceptPatternHandlerMinFree, exceptPatternHandler, listPatternHandlerFree, listPatternHandlerFree - listPatternHandlerMinFree, listPatternHandler, groupDoubleHandlerFree, groupDoubleHandlerFree - groupDoubleHandlerMinFree, groupDoubleHandler, interleaveDoubleHandlerFree, interleaveDoubleHandlerFree - interleaveDoubleHandlerMinFree, interleaveDoubleHandler, groupMinimalReduceCountHandlerFree, groupMinimalReduceCountHandlerFree - groupMinimalReduceCountHandlerMinFree, groupMinimalReduceCountHandler, groupMaximalReduceCountHandlerFree, groupMaximalReduceCountHandlerFree - groupMaximalReduceCountHandlerMinFree, groupMaximalReduceCountHandler, interleaveMinimalReduceCountHandlerFree, interleaveMinimalReduceCountHandlerFree - interleaveMinimalReduceCountHandlerMinFree, interleaveMinimalReduceCountHandler, interleaveMaximalReduceCountHandlerFree, interleaveMaximalReduceCountHandlerFree - interleaveMaximalReduceCountHandlerMinFree, interleaveMaximalReduceCountHandler, grammarMinimalReduceHandlerFree, grammarMinimalReduceHandlerFree - grammarMinimalReduceHandlerMinFree, grammarMinimalReduceHandler, grammarMaximalReduceHandlerFree, grammarMaximalReduceHandlerFree - grammarMaximalReduceHandlerMinFree, grammarMaximalReduceHandler, refMinimalReduceHandlerFree, refMinimalReduceHandlerFree - refMinimalReduceHandlerMinFree, refMinimalReduceHandler, refMaximalReduceHandlerFree, refMaximalReduceHandlerFree - refMaximalReduceHandlerMinFree, refMaximalReduceHandler, choiceMinimalReduceHandlerFree, choiceMinimalReduceHandlerFree - choiceMinimalReduceHandlerMinFree, choiceMinimalReduceHandler, choiceMaximalReduceHandlerFree, choiceMaximalReduceHandlerFree - choiceMaximalReduceHandlerMinFree, choiceMaximalReduceHandler, groupMinimalReduceHandlerFree, groupMinimalReduceHandlerFree - groupMinimalReduceHandlerMinFree, groupMinimalReduceHandler, groupMaximalReduceHandlerFree, groupMaximalReduceHandlerFree - groupMaximalReduceHandlerMinFree, groupMaximalReduceHandler, interleaveMinimalReduceHandlerFree, interleaveMinimalReduceHandlerFree - interleaveMinimalReduceHandlerMinFree, interleaveMinimalReduceHandler, interleaveMaximalReduceHandlerFree, interleaveMaximalReduceHandlerFree - interleaveMaximalReduceHandlerMinFree, interleaveMaximalReduceHandler);
        particleHandlerFree = 0;
        choiceHandlerFree = 0;
        groupHandlerFree = 0;
        grammarHandlerFree = 0;
        refHandlerFree = 0;
        uinterleaveHandlerFree = 0;
        minterleaveHandlerFree = 0;
        sinterleaveHandlerFree = 0;
        elementHandlerFree = 0;
        attributeHandlerFree = 0;
        exceptPatternHandlerFree = 0;
        listPatternHandlerFree = 0;
        groupDoubleHandlerFree = 0;
        interleaveDoubleHandlerFree = 0;
        groupMinimalReduceCountHandlerFree = 0;
        groupMaximalReduceCountHandlerFree = 0;
        interleaveMinimalReduceCountHandlerFree = 0;
        interleaveMaximalReduceCountHandlerFree = 0;
        grammarMinimalReduceHandlerFree = 0;
        grammarMaximalReduceHandlerFree = 0;
        refMinimalReduceHandlerFree = 0;
        refMaximalReduceHandlerFree = 0;
        choiceMinimalReduceHandlerFree = 0;
        choiceMaximalReduceHandlerFree = 0;
        groupMinimalReduceHandlerFree = 0;
        groupMaximalReduceHandlerFree = 0;
        interleaveMinimalReduceHandlerFree = 0;
        interleaveMaximalReduceHandlerFree = 0;
        full = false;
    }

    public ParticleHandler getParticleHandler(ChildEventHandler childEventHandler, APattern p, ErrorCatcher errorCatcher) {
        particleHandlerRequested++;
        if (particleHandlerFree == 0) {
            particleHandlerCreated++;
            ParticleHandler ph = new ParticleHandler();
            ph.init(activeInputDescriptor, this);
            ph.init(childEventHandler, p, errorCatcher);
            return ph;
        } else {
            ParticleHandler ph = particleHandler[--particleHandlerFree];
            ph.init(childEventHandler, p, errorCatcher);
            if (particleHandlerFree < particleHandlerMinFree) particleHandlerMinFree = particleHandlerFree;
            return ph;
        }
    }

    public void recycle(ParticleHandler psh) {
        particleHandlerRecycled++;
        if (particleHandlerFree == particleHandler.length) {
            if (particleHandler.length == particleHandlerMaxSize) return;
            ParticleHandler[] increased = new ParticleHandler[10 + particleHandler.length];
            System.arraycopy(particleHandler, 0, increased, 0, particleHandlerFree);
            particleHandler = increased;
        }
        particleHandler[particleHandlerFree++] = psh;
    }

    public ChoiceHandler getStructureValidationHandler(AChoicePattern cp, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        choiceHandlerRequested++;
        if (choiceHandlerFree == 0) {
            choiceHandlerCreated++;
            ChoiceHandler ch = new ChoiceHandler();
            ch.init(this, activeInputDescriptor, inputStackDescriptor);
            ch.init(cp, errorCatcher, parent, stackHandler);
            return ch;
        } else {
            ChoiceHandler ch = choiceHandler[--choiceHandlerFree];
            ch.init(cp, errorCatcher, parent, stackHandler);
            if (choiceHandlerFree < choiceHandlerMinFree) choiceHandlerMinFree = choiceHandlerFree;
            return ch;
        }
    }

    public void recycle(ChoiceHandler ch) {
        choiceHandlerRecycled++;
        if (choiceHandlerFree == choiceHandler.length) {
            if (choiceHandler.length == choiceHandlerMaxSize) return;
            ChoiceHandler[] increased = new ChoiceHandler[10 + choiceHandler.length];
            System.arraycopy(choiceHandler, 0, increased, 0, choiceHandlerFree);
            choiceHandler = increased;
        }
        choiceHandler[choiceHandlerFree++] = ch;
    }

    public GroupHandler getStructureValidationHandler(AGroup g, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        groupHandlerRequested++;
        if (groupHandlerFree == 0) {
            groupHandlerCreated++;
            GroupHandler gh = new GroupHandler();
            gh.init(this, activeInputDescriptor, inputStackDescriptor);
            gh.init(g, errorCatcher, parent, stackHandler);
            return gh;
        } else {
            GroupHandler gh = groupHandler[--groupHandlerFree];
            gh.init(g, errorCatcher, parent, stackHandler);
            if (groupHandlerFree < groupHandlerMinFree) groupHandlerMinFree = groupHandlerFree;
            return gh;
        }
    }

    public void recycle(GroupHandler gh) {
        groupHandlerRecycled++;
        if (groupHandlerFree == groupHandler.length) {
            if (groupHandler.length == groupHandlerMaxSize) return;
            GroupHandler[] increased = new GroupHandler[10 + groupHandler.length];
            System.arraycopy(groupHandler, 0, increased, 0, groupHandlerFree);
            groupHandler = increased;
        }
        groupHandler[groupHandlerFree++] = gh;
    }

    public RefHandler getStructureValidationHandler(ARef r, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        if (refHandlerFree == 0) {
            RefHandler pih = new RefHandler();
            pih.init(this, activeInputDescriptor, inputStackDescriptor);
            pih.init(r, errorCatcher, parent, stackHandler);
            return pih;
        } else {
            RefHandler pih = refHandler[--refHandlerFree];
            pih.init(r, errorCatcher, parent, stackHandler);
            if (refHandlerFree < refHandlerMinFree) refHandlerMinFree = refHandlerFree;
            return pih;
        }
    }

    public void recycle(RefHandler pih) {
        if (refHandlerFree == refHandler.length) {
            if (refHandler.length == refHandlerMaxSize) return;
            RefHandler[] increased = new RefHandler[10 + refHandler.length];
            System.arraycopy(refHandler, 0, increased, 0, refHandlerFree);
            refHandler = increased;
        }
        refHandler[refHandlerFree++] = pih;
    }

    public GrammarHandler getStructureValidationHandler(AGrammar g, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        if (grammarHandlerFree == 0) {
            GrammarHandler pih = new GrammarHandler();
            pih.init(this, activeInputDescriptor, inputStackDescriptor);
            pih.init(g, errorCatcher, parent, stackHandler);
            return pih;
        } else {
            GrammarHandler pih = grammarHandler[--grammarHandlerFree];
            pih.init(g, errorCatcher, parent, stackHandler);
            if (grammarHandlerFree < grammarHandlerMinFree) grammarHandlerMinFree = grammarHandlerFree;
            return pih;
        }
    }

    public void recycle(GrammarHandler pih) {
        if (grammarHandlerFree == grammarHandler.length) {
            if (grammarHandler.length == grammarHandlerMaxSize) return;
            GrammarHandler[] increased = new GrammarHandler[10 + grammarHandler.length];
            System.arraycopy(grammarHandler, 0, increased, 0, grammarHandlerFree);
            grammarHandler = increased;
        }
        grammarHandler[grammarHandlerFree++] = pih;
    }

    public UInterleaveHandler getUInterleaveHandler(AInterleave i, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        if (uinterleaveHandlerFree == 0) {
            UInterleaveHandler ih = new UInterleaveHandler();
            ih.init(this, activeInputDescriptor, inputStackDescriptor);
            ih.init(i, errorCatcher, parent, stackHandler);
            return ih;
        } else {
            UInterleaveHandler ih = uinterleaveHandler[--uinterleaveHandlerFree];
            ih.init(i, errorCatcher, parent, stackHandler);
            if (uinterleaveHandlerFree < uinterleaveHandlerMinFree) uinterleaveHandlerMinFree = uinterleaveHandlerFree;
            return ih;
        }
    }

    public void recycle(UInterleaveHandler ih) {
        if (uinterleaveHandlerFree == uinterleaveHandler.length) {
            if (uinterleaveHandler.length == uinterleaveHandlerMaxSize) return;
            UInterleaveHandler[] increased = new UInterleaveHandler[10 + uinterleaveHandler.length];
            System.arraycopy(uinterleaveHandler, 0, increased, 0, uinterleaveHandlerFree);
            uinterleaveHandler = increased;
        }
        uinterleaveHandler[uinterleaveHandlerFree++] = ih;
    }

    public MInterleaveHandler getMInterleaveHandler(AInterleave i, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        if (minterleaveHandlerFree == 0) {
            MInterleaveHandler ih = new MInterleaveHandler();
            ih.init(this, activeInputDescriptor, inputStackDescriptor);
            ih.init(i, errorCatcher, parent, stackHandler, this);
            return ih;
        } else {
            MInterleaveHandler ih = minterleaveHandler[--minterleaveHandlerFree];
            ih.init(i, errorCatcher, parent, stackHandler, this);
            if (minterleaveHandlerFree < minterleaveHandlerMinFree) minterleaveHandlerMinFree = minterleaveHandlerFree;
            return ih;
        }
    }

    public void recycle(MInterleaveHandler ih) {
        if (minterleaveHandlerFree == minterleaveHandler.length) {
            if (minterleaveHandler.length == minterleaveHandlerMaxSize) return;
            MInterleaveHandler[] increased = new MInterleaveHandler[10 + minterleaveHandler.length];
            System.arraycopy(minterleaveHandler, 0, increased, 0, minterleaveHandlerFree);
            minterleaveHandler = increased;
        }
        minterleaveHandler[minterleaveHandlerFree++] = ih;
    }

    public SInterleaveHandler getSInterleaveHandler(AInterleave i, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler, MInterleaveHandler primaryHandler) {
        if (sinterleaveHandlerFree == 0) {
            SInterleaveHandler ih = new SInterleaveHandler();
            ih.init(this, activeInputDescriptor, inputStackDescriptor);
            ih.init(i, errorCatcher, parent, stackHandler, primaryHandler);
            return ih;
        } else {
            SInterleaveHandler ih = sinterleaveHandler[--sinterleaveHandlerFree];
            ih.init(i, errorCatcher, parent, stackHandler, primaryHandler);
            if (sinterleaveHandlerFree < sinterleaveHandlerMinFree) sinterleaveHandlerMinFree = sinterleaveHandlerFree;
            return ih;
        }
    }

    public void recycle(SInterleaveHandler ih) {
        if (sinterleaveHandlerFree == sinterleaveHandler.length) {
            if (sinterleaveHandler.length == sinterleaveHandlerMaxSize) return;
            SInterleaveHandler[] increased = new SInterleaveHandler[10 + sinterleaveHandler.length];
            System.arraycopy(sinterleaveHandler, 0, increased, 0, sinterleaveHandlerFree);
            sinterleaveHandler = increased;
        }
        sinterleaveHandler[sinterleaveHandlerFree++] = ih;
    }

    public ElementHandler getStructureValidationHandler(AElement e, ErrorCatcher errorCatcher, StackHandler stackHandler) {
        elementHandlerRequested++;
        if (elementHandlerFree == 0) {
            elementHandlerCreated++;
            ElementHandler eh = new ElementHandler();
            eh.init(this, activeInputDescriptor, inputStackDescriptor);
            eh.init(e, errorCatcher, stackHandler);
            return eh;
        } else {
            ElementHandler eh = elementHandler[--elementHandlerFree];
            eh.init(e, errorCatcher, stackHandler);
            if (elementHandlerFree < elementHandlerMinFree) elementHandlerMinFree = elementHandlerFree;
            return eh;
        }
    }

    public void recycle(ElementHandler eh) {
        elementHandlerRecycled++;
        if (elementHandlerFree == elementHandler.length) {
            if (elementHandler.length == elementHandlerMaxSize) return;
            ElementHandler[] increased = new ElementHandler[10 + elementHandler.length];
            System.arraycopy(elementHandler, 0, increased, 0, elementHandlerFree);
            elementHandler = increased;
        }
        elementHandler[elementHandlerFree++] = eh;
    }

    public AttributeHandler getStructureValidationHandler(AAttribute a, ErrorCatcher errorCatcher, StackHandler stackHandler) {
        if (attributeHandlerFree == 0) {
            AttributeHandler ah = new AttributeHandler();
            ah.init(this, activeInputDescriptor, inputStackDescriptor);
            ah.init(a, errorCatcher, stackHandler);
            return ah;
        } else {
            AttributeHandler ah = attributeHandler[--attributeHandlerFree];
            ah.init(a, errorCatcher, stackHandler);
            if (attributeHandlerFree < attributeHandlerMinFree) attributeHandlerMinFree = attributeHandlerFree;
            return ah;
        }
    }

    public void recycle(AttributeHandler ah) {
        if (attributeHandlerFree == attributeHandler.length) {
            if (attributeHandler.length == attributeHandlerMaxSize) return;
            AttributeHandler[] increased = new AttributeHandler[10 + attributeHandler.length];
            System.arraycopy(attributeHandler, 0, increased, 0, attributeHandlerFree);
            attributeHandler = increased;
        }
        attributeHandler[attributeHandlerFree++] = ah;
    }

    public ExceptPatternHandler getStructureValidationHandler(AExceptPattern a, ErrorCatcher errorCatcher, StackHandler stackHandler) {
        if (exceptPatternHandlerFree == 0) {
            ExceptPatternHandler ah = new ExceptPatternHandler();
            ah.init(this, activeInputDescriptor, inputStackDescriptor);
            ah.init(a, errorCatcher, stackHandler);
            return ah;
        } else {
            ExceptPatternHandler ah = exceptPatternHandler[--exceptPatternHandlerFree];
            ah.init(a, errorCatcher, stackHandler);
            if (exceptPatternHandlerFree < exceptPatternHandlerMinFree) exceptPatternHandlerMinFree = exceptPatternHandlerFree;
            return ah;
        }
    }

    public void recycle(ExceptPatternHandler ah) {
        if (exceptPatternHandlerFree == exceptPatternHandler.length) {
            if (exceptPatternHandler.length == exceptPatternHandlerMaxSize) return;
            ExceptPatternHandler[] increased = new ExceptPatternHandler[10 + exceptPatternHandler.length];
            System.arraycopy(exceptPatternHandler, 0, increased, 0, exceptPatternHandlerFree);
            exceptPatternHandler = increased;
        }
        exceptPatternHandler[exceptPatternHandlerFree++] = ah;
    }

    public ListPatternHandler getStructureValidationHandler(AListPattern a, ErrorCatcher errorCatcher, StackHandler stackHandler) {
        if (listPatternHandlerFree == 0) {
            ListPatternHandler ah = new ListPatternHandler();
            ah.init(this, activeInputDescriptor, inputStackDescriptor);
            ah.init(a, errorCatcher, stackHandler);
            return ah;
        } else {
            ListPatternHandler ah = listPatternHandler[--listPatternHandlerFree];
            ah.init(a, errorCatcher, stackHandler);
            if (listPatternHandlerFree < listPatternHandlerMinFree) listPatternHandlerMinFree = listPatternHandlerFree;
            return ah;
        }
    }

    public void recycle(ListPatternHandler ah) {
        if (listPatternHandlerFree == listPatternHandler.length) {
            if (listPatternHandler.length == listPatternHandlerMaxSize) return;
            ListPatternHandler[] increased = new ListPatternHandler[10 + listPatternHandler.length];
            System.arraycopy(listPatternHandler, 0, increased, 0, listPatternHandlerFree);
            listPatternHandler = increased;
        }
        listPatternHandler[listPatternHandlerFree++] = ah;
    }

    public GroupDoubleHandler getStructureDoubleHandler(AGroup pattern, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler, ActiveModelStackHandlerPool stackHandlerPool) {
        if (groupDoubleHandlerFree == 0) {
            GroupDoubleHandler sih = new GroupDoubleHandler();
            sih.init(this, activeInputDescriptor, inputStackDescriptor);
            sih.init(pattern, errorCatcher, parent, stackHandler, stackHandlerPool);
            return sih;
        } else {
            GroupDoubleHandler sih = groupDoubleHandler[--groupDoubleHandlerFree];
            sih.init(pattern, errorCatcher, parent, stackHandler, stackHandlerPool);
            if (groupDoubleHandlerFree < groupDoubleHandlerMinFree) groupDoubleHandlerMinFree = groupDoubleHandlerFree;
            return sih;
        }
    }

    public void recycle(GroupDoubleHandler sih) {
        if (groupDoubleHandlerFree == groupDoubleHandler.length) {
            if (groupDoubleHandler.length == groupDoubleHandlerMaxSize) return;
            GroupDoubleHandler[] increased = new GroupDoubleHandler[10 + groupDoubleHandler.length];
            System.arraycopy(groupDoubleHandler, 0, increased, 0, groupDoubleHandlerFree);
            groupDoubleHandler = increased;
        }
        groupDoubleHandler[groupDoubleHandlerFree++] = sih;
    }

    public InterleaveDoubleHandler getStructureDoubleHandler(AInterleave pattern, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler, ActiveModelStackHandlerPool stackHandlerPool) {
        if (interleaveDoubleHandlerFree == 0) {
            InterleaveDoubleHandler sih = new InterleaveDoubleHandler();
            sih.init(this, activeInputDescriptor, inputStackDescriptor);
            sih.init(pattern, errorCatcher, parent, stackHandler, stackHandlerPool);
            return sih;
        } else {
            InterleaveDoubleHandler sih = interleaveDoubleHandler[--interleaveDoubleHandlerFree];
            sih.init(pattern, errorCatcher, parent, stackHandler, stackHandlerPool);
            if (interleaveDoubleHandlerFree < interleaveDoubleHandlerMinFree) interleaveDoubleHandlerMinFree = interleaveDoubleHandlerFree;
            return sih;
        }
    }

    public void recycle(InterleaveDoubleHandler sih) {
        if (interleaveDoubleHandlerFree == interleaveDoubleHandler.length) {
            if (interleaveDoubleHandler.length == interleaveDoubleHandlerMaxSize) return;
            InterleaveDoubleHandler[] increased = new InterleaveDoubleHandler[10 + interleaveDoubleHandler.length];
            System.arraycopy(interleaveDoubleHandler, 0, increased, 0, interleaveDoubleHandlerFree);
            interleaveDoubleHandler = increased;
        }
        interleaveDoubleHandler[interleaveDoubleHandlerFree++] = sih;
    }

    public GroupMinimalReduceCountHandler getMinimalReduceCountHandler(IntList reduceCountList, IntList startedCountList, AGroup g, ErrorCatcher errorCatcher, MinimalReduceStackHandler stackHandler) {
        if (groupMinimalReduceCountHandlerFree == 0) {
            GroupMinimalReduceCountHandler pih = new GroupMinimalReduceCountHandler();
            pih.init(this, activeInputDescriptor, inputStackDescriptor);
            pih.init(reduceCountList, startedCountList, g, errorCatcher, stackHandler);
            return pih;
        } else {
            GroupMinimalReduceCountHandler pih = groupMinimalReduceCountHandler[--groupMinimalReduceCountHandlerFree];
            pih.init(reduceCountList, startedCountList, g, errorCatcher, stackHandler);
            if (groupMinimalReduceCountHandlerFree < groupMinimalReduceCountHandlerMinFree) groupMinimalReduceCountHandlerMinFree = groupMinimalReduceCountHandlerFree;
            return pih;
        }
    }

    public void recycle(GroupMinimalReduceCountHandler gh) {
        if (groupMinimalReduceCountHandlerFree == groupMinimalReduceCountHandler.length) {
            if (groupMinimalReduceCountHandler.length == groupMinimalReduceCountHandlerMaxSize) return;
            GroupMinimalReduceCountHandler[] increased = new GroupMinimalReduceCountHandler[10 + groupMinimalReduceCountHandler.length];
            System.arraycopy(groupMinimalReduceCountHandler, 0, increased, 0, groupMinimalReduceCountHandlerFree);
            groupMinimalReduceCountHandler = increased;
        }
        groupMinimalReduceCountHandler[groupMinimalReduceCountHandlerFree++] = gh;
    }

    public GroupMaximalReduceCountHandler getMaximalReduceCountHandler(IntList reduceCountList, IntList startedCountList, AGroup g, ErrorCatcher errorCatcher, MaximalReduceStackHandler stackHandler) {
        if (groupMaximalReduceCountHandlerFree == 0) {
            GroupMaximalReduceCountHandler pih = new GroupMaximalReduceCountHandler();
            pih.init(this, activeInputDescriptor, inputStackDescriptor);
            pih.init(reduceCountList, startedCountList, g, errorCatcher, stackHandler);
            return pih;
        } else {
            GroupMaximalReduceCountHandler pih = groupMaximalReduceCountHandler[--groupMaximalReduceCountHandlerFree];
            pih.init(reduceCountList, startedCountList, g, errorCatcher, stackHandler);
            if (groupMaximalReduceCountHandlerFree < groupMaximalReduceCountHandlerMinFree) groupMaximalReduceCountHandlerMinFree = groupMaximalReduceCountHandlerFree;
            return pih;
        }
    }

    public void recycle(GroupMaximalReduceCountHandler gh) {
        if (groupMaximalReduceCountHandlerFree == groupMaximalReduceCountHandler.length) {
            if (groupMaximalReduceCountHandler.length == groupMaximalReduceCountHandlerMaxSize) return;
            GroupMaximalReduceCountHandler[] increased = new GroupMaximalReduceCountHandler[10 + groupMaximalReduceCountHandler.length];
            System.arraycopy(groupMaximalReduceCountHandler, 0, increased, 0, groupMaximalReduceCountHandlerFree);
            groupMaximalReduceCountHandler = increased;
        }
        groupMaximalReduceCountHandler[groupMaximalReduceCountHandlerFree++] = gh;
    }

    public InterleaveMinimalReduceCountHandler getMinimalReduceCountHandler(IntList reduceCountList, AInterleave i, ErrorCatcher errorCatcher, MinimalReduceStackHandler stackHandler) {
        if (interleaveMinimalReduceCountHandlerFree == 0) {
            InterleaveMinimalReduceCountHandler pih = new InterleaveMinimalReduceCountHandler();
            pih.init(this, activeInputDescriptor, inputStackDescriptor);
            pih.init(reduceCountList, i, errorCatcher, stackHandler);
            return pih;
        } else {
            InterleaveMinimalReduceCountHandler pih = interleaveMinimalReduceCountHandler[--interleaveMinimalReduceCountHandlerFree];
            pih.init(reduceCountList, i, errorCatcher, stackHandler);
            if (interleaveMinimalReduceCountHandlerFree < interleaveMinimalReduceCountHandlerMinFree) interleaveMinimalReduceCountHandlerMinFree = interleaveMinimalReduceCountHandlerFree;
            return pih;
        }
    }

    public void recycle(InterleaveMinimalReduceCountHandler ih) {
        if (interleaveMinimalReduceCountHandlerFree == interleaveMinimalReduceCountHandler.length) {
            if (interleaveMinimalReduceCountHandler.length == interleaveMinimalReduceCountHandlerMaxSize) return;
            InterleaveMinimalReduceCountHandler[] increased = new InterleaveMinimalReduceCountHandler[10 + interleaveMinimalReduceCountHandler.length];
            System.arraycopy(interleaveMinimalReduceCountHandler, 0, increased, 0, interleaveMinimalReduceCountHandlerFree);
            interleaveMinimalReduceCountHandler = increased;
        }
        interleaveMinimalReduceCountHandler[interleaveMinimalReduceCountHandlerFree++] = ih;
    }

    public InterleaveMaximalReduceCountHandler getMaximalReduceCountHandler(IntList reduceCountList, AInterleave i, ErrorCatcher errorCatcher, MaximalReduceStackHandler stackHandler) {
        if (interleaveMaximalReduceCountHandlerFree == 0) {
            InterleaveMaximalReduceCountHandler pih = new InterleaveMaximalReduceCountHandler();
            pih.init(this, activeInputDescriptor, inputStackDescriptor);
            pih.init(reduceCountList, i, errorCatcher, stackHandler);
            return pih;
        } else {
            InterleaveMaximalReduceCountHandler pih = interleaveMaximalReduceCountHandler[--interleaveMaximalReduceCountHandlerFree];
            pih.init(reduceCountList, i, errorCatcher, stackHandler);
            if (interleaveMaximalReduceCountHandlerFree < interleaveMaximalReduceCountHandlerMinFree) interleaveMaximalReduceCountHandlerMinFree = interleaveMaximalReduceCountHandlerFree;
            return pih;
        }
    }

    public void recycle(InterleaveMaximalReduceCountHandler ih) {
        if (interleaveMaximalReduceCountHandlerFree == interleaveMaximalReduceCountHandler.length) {
            if (interleaveMaximalReduceCountHandler.length == interleaveMaximalReduceCountHandlerMaxSize) return;
            InterleaveMaximalReduceCountHandler[] increased = new InterleaveMaximalReduceCountHandler[10 + interleaveMaximalReduceCountHandler.length];
            System.arraycopy(interleaveMaximalReduceCountHandler, 0, increased, 0, interleaveMaximalReduceCountHandlerFree);
            interleaveMaximalReduceCountHandler = increased;
        }
        interleaveMaximalReduceCountHandler[interleaveMaximalReduceCountHandlerFree++] = ih;
    }

    public GrammarMinimalReduceHandler getMinimalReduceHandler(AGrammar g, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        if (grammarMinimalReduceHandlerFree == 0) {
            GrammarMinimalReduceHandler pih = new GrammarMinimalReduceHandler();
            pih.init(this, activeInputDescriptor, inputStackDescriptor);
            pih.init(g, errorCatcher, parent, stackHandler);
            return pih;
        } else {
            GrammarMinimalReduceHandler pih = grammarMinimalReduceHandler[--grammarMinimalReduceHandlerFree];
            pih.init(g, errorCatcher, parent, stackHandler);
            if (grammarMinimalReduceHandlerFree < grammarMinimalReduceHandlerMinFree) grammarMinimalReduceHandlerMinFree = grammarMinimalReduceHandlerFree;
            return pih;
        }
    }

    public void recycle(GrammarMinimalReduceHandler gh) {
        if (grammarMinimalReduceHandlerFree == grammarMinimalReduceHandler.length) {
            if (grammarMinimalReduceHandler.length == grammarMinimalReduceHandlerMaxSize) return;
            GrammarMinimalReduceHandler[] increased = new GrammarMinimalReduceHandler[10 + grammarMinimalReduceHandler.length];
            System.arraycopy(grammarMinimalReduceHandler, 0, increased, 0, grammarMinimalReduceHandlerFree);
            grammarMinimalReduceHandler = increased;
        }
        grammarMinimalReduceHandler[grammarMinimalReduceHandlerFree++] = gh;
    }

    public GrammarMaximalReduceHandler getMaximalReduceHandler(AGrammar g, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        if (grammarMaximalReduceHandlerFree == 0) {
            GrammarMaximalReduceHandler pih = new GrammarMaximalReduceHandler();
            pih.init(this, activeInputDescriptor, inputStackDescriptor);
            pih.init(g, errorCatcher, parent, stackHandler);
            return pih;
        } else {
            GrammarMaximalReduceHandler pih = grammarMaximalReduceHandler[--grammarMaximalReduceHandlerFree];
            pih.init(g, errorCatcher, parent, stackHandler);
            if (grammarMaximalReduceHandlerFree < grammarMaximalReduceHandlerMinFree) grammarMaximalReduceHandlerMinFree = grammarMaximalReduceHandlerFree;
            return pih;
        }
    }

    public void recycle(GrammarMaximalReduceHandler gh) {
        if (grammarMaximalReduceHandlerFree == grammarMaximalReduceHandler.length) {
            if (grammarMaximalReduceHandler.length == grammarMaximalReduceHandlerMaxSize) return;
            GrammarMaximalReduceHandler[] increased = new GrammarMaximalReduceHandler[10 + grammarMaximalReduceHandler.length];
            System.arraycopy(grammarMaximalReduceHandler, 0, increased, 0, grammarMaximalReduceHandlerFree);
            grammarMaximalReduceHandler = increased;
        }
        grammarMaximalReduceHandler[grammarMaximalReduceHandlerFree++] = gh;
    }

    public RefMinimalReduceHandler getMinimalReduceHandler(ARef r, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        if (refMinimalReduceHandlerFree == 0) {
            RefMinimalReduceHandler rmrh = new RefMinimalReduceHandler();
            rmrh.init(this, activeInputDescriptor, inputStackDescriptor);
            rmrh.init(r, errorCatcher, parent, stackHandler);
            return rmrh;
        } else {
            RefMinimalReduceHandler rmrh = refMinimalReduceHandler[--refMinimalReduceHandlerFree];
            rmrh.init(r, errorCatcher, parent, stackHandler);
            if (refMinimalReduceHandlerFree < refMinimalReduceHandlerMinFree) refMinimalReduceHandlerMinFree = refMinimalReduceHandlerFree;
            return rmrh;
        }
    }

    public void recycle(RefMinimalReduceHandler rmrh) {
        if (refMinimalReduceHandlerFree == refMinimalReduceHandler.length) {
            if (refMinimalReduceHandler.length == refMinimalReduceHandlerMaxSize) return;
            RefMinimalReduceHandler[] increased = new RefMinimalReduceHandler[10 + refMinimalReduceHandler.length];
            System.arraycopy(refMinimalReduceHandler, 0, increased, 0, refMinimalReduceHandlerFree);
            refMinimalReduceHandler = increased;
        }
        refMinimalReduceHandler[refMinimalReduceHandlerFree++] = rmrh;
    }

    public RefMaximalReduceHandler getMaximalReduceHandler(ARef r, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        if (refMaximalReduceHandlerFree == 0) {
            RefMaximalReduceHandler pih = new RefMaximalReduceHandler();
            pih.init(this, activeInputDescriptor, inputStackDescriptor);
            pih.init(r, errorCatcher, parent, stackHandler);
            return pih;
        } else {
            RefMaximalReduceHandler pih = refMaximalReduceHandler[--refMaximalReduceHandlerFree];
            pih.init(r, errorCatcher, parent, stackHandler);
            if (refMaximalReduceHandlerFree < refMaximalReduceHandlerMinFree) refMaximalReduceHandlerMinFree = refMaximalReduceHandlerFree;
            return pih;
        }
    }

    public void recycle(RefMaximalReduceHandler rmrh) {
        if (refMaximalReduceHandlerFree == refMaximalReduceHandler.length) {
            if (refMaximalReduceHandler.length == refMaximalReduceHandlerMaxSize) return;
            RefMaximalReduceHandler[] increased = new RefMaximalReduceHandler[10 + refMaximalReduceHandler.length];
            System.arraycopy(refMaximalReduceHandler, 0, increased, 0, refMaximalReduceHandlerFree);
            refMaximalReduceHandler = increased;
        }
        refMaximalReduceHandler[refMaximalReduceHandlerFree++] = rmrh;
    }

    public ChoiceMinimalReduceHandler getMinimalReduceHandler(AChoicePattern c, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        if (choiceMinimalReduceHandlerFree == 0) {
            ChoiceMinimalReduceHandler pih = new ChoiceMinimalReduceHandler();
            pih.init(this, activeInputDescriptor, inputStackDescriptor);
            pih.init(c, errorCatcher, parent, stackHandler);
            return pih;
        } else {
            ChoiceMinimalReduceHandler pih = choiceMinimalReduceHandler[--choiceMinimalReduceHandlerFree];
            pih.init(c, errorCatcher, parent, stackHandler);
            if (choiceMinimalReduceHandlerFree < choiceMinimalReduceHandlerMinFree) choiceMinimalReduceHandlerMinFree = choiceMinimalReduceHandlerFree;
            return pih;
        }
    }

    public void recycle(ChoiceMinimalReduceHandler ch) {
        if (choiceMinimalReduceHandlerFree == choiceMinimalReduceHandler.length) {
            if (choiceMinimalReduceHandler.length == choiceMinimalReduceHandlerMaxSize) return;
            ChoiceMinimalReduceHandler[] increased = new ChoiceMinimalReduceHandler[10 + choiceMinimalReduceHandler.length];
            System.arraycopy(choiceMinimalReduceHandler, 0, increased, 0, choiceMinimalReduceHandlerFree);
            choiceMinimalReduceHandler = increased;
        }
        choiceMinimalReduceHandler[choiceMinimalReduceHandlerFree++] = ch;
    }

    public ChoiceMaximalReduceHandler getMaximalReduceHandler(AChoicePattern c, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        if (choiceMaximalReduceHandlerFree == 0) {
            ChoiceMaximalReduceHandler pih = new ChoiceMaximalReduceHandler();
            pih.init(this, activeInputDescriptor, inputStackDescriptor);
            pih.init(c, errorCatcher, parent, stackHandler);
            return pih;
        } else {
            ChoiceMaximalReduceHandler pih = choiceMaximalReduceHandler[--choiceMaximalReduceHandlerFree];
            pih.init(c, errorCatcher, parent, stackHandler);
            if (choiceMaximalReduceHandlerFree < choiceMaximalReduceHandlerMinFree) choiceMaximalReduceHandlerMinFree = choiceMaximalReduceHandlerFree;
            return pih;
        }
    }

    public void recycle(ChoiceMaximalReduceHandler ch) {
        if (choiceMaximalReduceHandlerFree == choiceMaximalReduceHandler.length) {
            if (choiceMaximalReduceHandler.length == choiceMaximalReduceHandlerMaxSize) return;
            ChoiceMaximalReduceHandler[] increased = new ChoiceMaximalReduceHandler[10 + choiceMaximalReduceHandler.length];
            System.arraycopy(choiceMaximalReduceHandler, 0, increased, 0, choiceMaximalReduceHandlerFree);
            choiceMaximalReduceHandler = increased;
        }
        choiceMaximalReduceHandler[choiceMaximalReduceHandlerFree++] = ch;
    }

    public GroupMinimalReduceHandler getMinimalReduceHandler(AGroup g, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        if (groupMinimalReduceHandlerFree == 0) {
            GroupMinimalReduceHandler pih = new GroupMinimalReduceHandler();
            pih.init(this, activeInputDescriptor, inputStackDescriptor);
            pih.init(g, errorCatcher, parent, stackHandler);
            return pih;
        } else {
            GroupMinimalReduceHandler pih = groupMinimalReduceHandler[--groupMinimalReduceHandlerFree];
            pih.init(g, errorCatcher, parent, stackHandler);
            if (groupMinimalReduceHandlerFree < groupMinimalReduceHandlerMinFree) groupMinimalReduceHandlerMinFree = groupMinimalReduceHandlerFree;
            return pih;
        }
    }

    public void recycle(GroupMinimalReduceHandler gh) {
        if (groupMinimalReduceHandlerFree == groupMinimalReduceHandler.length) {
            if (groupMinimalReduceHandler.length == groupMinimalReduceHandlerMaxSize) return;
            GroupMinimalReduceHandler[] increased = new GroupMinimalReduceHandler[10 + groupMinimalReduceHandler.length];
            System.arraycopy(groupMinimalReduceHandler, 0, increased, 0, groupMinimalReduceHandlerFree);
            groupMinimalReduceHandler = increased;
        }
        groupMinimalReduceHandler[groupMinimalReduceHandlerFree++] = gh;
    }

    public GroupMaximalReduceHandler getMaximalReduceHandler(AGroup g, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        if (groupMaximalReduceHandlerFree == 0) {
            GroupMaximalReduceHandler pih = new GroupMaximalReduceHandler();
            pih.init(this, activeInputDescriptor, inputStackDescriptor);
            pih.init(g, errorCatcher, parent, stackHandler);
            return pih;
        } else {
            GroupMaximalReduceHandler pih = groupMaximalReduceHandler[--groupMaximalReduceHandlerFree];
            pih.init(g, errorCatcher, parent, stackHandler);
            if (groupMaximalReduceHandlerFree < groupMaximalReduceHandlerMinFree) groupMaximalReduceHandlerMinFree = groupMaximalReduceHandlerFree;
            return pih;
        }
    }

    public void recycle(GroupMaximalReduceHandler gh) {
        if (groupMaximalReduceHandlerFree == groupMaximalReduceHandler.length) {
            if (groupMaximalReduceHandler.length == groupMaximalReduceHandlerMaxSize) return;
            GroupMaximalReduceHandler[] increased = new GroupMaximalReduceHandler[10 + groupMaximalReduceHandler.length];
            System.arraycopy(groupMaximalReduceHandler, 0, increased, 0, groupMaximalReduceHandlerFree);
            groupMaximalReduceHandler = increased;
        }
        groupMaximalReduceHandler[groupMaximalReduceHandlerFree++] = gh;
    }

    public InterleaveMinimalReduceHandler getMinimalReduceHandler(AInterleave i, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        if (interleaveMinimalReduceHandlerFree == 0) {
            InterleaveMinimalReduceHandler pih = new InterleaveMinimalReduceHandler();
            pih.init(this, activeInputDescriptor, inputStackDescriptor);
            pih.init(i, errorCatcher, parent, stackHandler);
            return pih;
        } else {
            InterleaveMinimalReduceHandler pih = interleaveMinimalReduceHandler[--interleaveMinimalReduceHandlerFree];
            pih.init(i, errorCatcher, parent, stackHandler);
            if (interleaveMinimalReduceHandlerFree < interleaveMinimalReduceHandlerMinFree) interleaveMinimalReduceHandlerMinFree = interleaveMinimalReduceHandlerFree;
            return pih;
        }
    }

    public void recycle(InterleaveMinimalReduceHandler ih) {
        if (interleaveMinimalReduceHandlerFree == interleaveMinimalReduceHandler.length) {
            if (interleaveMinimalReduceHandler.length == interleaveMinimalReduceHandlerMaxSize) return;
            InterleaveMinimalReduceHandler[] increased = new InterleaveMinimalReduceHandler[10 + interleaveMinimalReduceHandler.length];
            System.arraycopy(interleaveMinimalReduceHandler, 0, increased, 0, interleaveMinimalReduceHandlerFree);
            interleaveMinimalReduceHandler = increased;
        }
        interleaveMinimalReduceHandler[interleaveMinimalReduceHandlerFree++] = ih;
    }

    public InterleaveMaximalReduceHandler getMaximalReduceHandler(AInterleave i, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        if (interleaveMaximalReduceHandlerFree == 0) {
            InterleaveMaximalReduceHandler pih = new InterleaveMaximalReduceHandler();
            pih.init(this, activeInputDescriptor, inputStackDescriptor);
            pih.init(i, errorCatcher, parent, stackHandler);
            return pih;
        } else {
            InterleaveMaximalReduceHandler pih = interleaveMaximalReduceHandler[--interleaveMaximalReduceHandlerFree];
            pih.init(i, errorCatcher, parent, stackHandler);
            if (interleaveMaximalReduceHandlerFree < interleaveMaximalReduceHandlerMinFree) interleaveMaximalReduceHandlerMinFree = interleaveMaximalReduceHandlerFree;
            return pih;
        }
    }

    public void recycle(InterleaveMaximalReduceHandler ih) {
        if (interleaveMaximalReduceHandlerFree == interleaveMaximalReduceHandler.length) {
            if (interleaveMaximalReduceHandler.length == interleaveMaximalReduceHandlerMaxSize) return;
            InterleaveMaximalReduceHandler[] increased = new InterleaveMaximalReduceHandler[10 + interleaveMaximalReduceHandler.length];
            System.arraycopy(interleaveMaximalReduceHandler, 0, increased, 0, interleaveMaximalReduceHandlerFree);
            interleaveMaximalReduceHandler = increased;
        }
        interleaveMaximalReduceHandler[interleaveMaximalReduceHandlerFree++] = ih;
    }
}
