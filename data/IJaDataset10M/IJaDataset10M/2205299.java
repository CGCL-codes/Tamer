package serene.validation.handlers.structure.impl;

import java.util.Arrays;
import serene.util.IntList;
import serene.validation.schema.active.components.APattern;
import serene.validation.schema.active.components.ActiveTypeItem;
import serene.validation.schema.active.components.AGroup;
import serene.validation.schema.active.components.AElement;
import serene.validation.handlers.structure.StructureHandler;
import serene.validation.handlers.structure.CardinalityHandler;
import serene.validation.handlers.structure.RuleHandlerVisitor;
import serene.validation.handlers.stack.StackHandler;
import serene.validation.handlers.stack.impl.MaximalReduceStackHandler;
import serene.validation.handlers.conflict.InternalConflictResolver;
import serene.validation.handlers.error.ErrorCatcher;
import serene.util.IntList;

public class GroupMaximalReduceCountHandler extends MaximalReduceCountHandler {

    final int GOOD_ORDER = -1;

    final int CURRENT_MISPLACED = 0;

    final int PREVIOUS_MISPLACED = 1;

    APattern[] childDefinition;

    int[][] childInputRecordIndex;

    APattern[][] childSourceDefinition;

    int childRecordIndex;

    int childRecordIncreaseSizeAmount;

    int childRecordInitialSize;

    /**
    * To keep track of the current index in the childInputRecordIndex and  
    * childSourceDefinition so that it's not necessary to increment size, but
    * it can grow with bigger steps.
    */
    int[] childDetailsCurrentIndex;

    int childDetailsInitialSize;

    int childDetailsIncreaseSizeAmount;

    IntList startedCountList;

    GroupMaximalReduceCountHandler original;

    GroupMaximalReduceCountHandler() {
        super();
        childRecordIndex = -1;
        childRecordInitialSize = 10;
        childRecordIncreaseSizeAmount = 10;
        childDefinition = new APattern[childRecordInitialSize];
        childInputRecordIndex = new int[childRecordInitialSize][];
        childSourceDefinition = new APattern[childRecordInitialSize][];
        childDetailsCurrentIndex = new int[childRecordInitialSize];
        Arrays.fill(childDetailsCurrentIndex, -1);
        childDetailsIncreaseSizeAmount = 10;
        childDetailsInitialSize = 10;
    }

    void init(IntList reduceCountList, IntList startedCountList, AGroup group, ErrorCatcher errorCatcher, MaximalReduceStackHandler stackHandler) {
        this.reduceCountList = reduceCountList;
        this.startedCountList = startedCountList;
        this.rule = group;
        satisfactionIndicator = group.getSatisfactionIndicator();
        saturationIndicator = group.getSaturationIndicator();
        this.errorCatcher = errorCatcher;
        this.stackHandler = stackHandler;
        size = group.getChildrenCount();
        if (size > childParticleHandlers.length) {
            childParticleHandlers = new ParticleHandler[size];
            childStructureHandlers = new StructureHandler[size];
        }
    }

    public void recycle() {
        original = null;
        setEmptyState();
        recycler.recycle(this);
    }

    public void handleValidatingReduce() {
        int started = -1;
        int reduced = -1;
        for (int i = 0; i < size; i++) {
            if (childStructureHandlers[i] != null) {
                childStructureHandlers[i].handleValidatingReduce();
            }
            started = startedCountList.get(i);
            reduced = reduceCountList.get(i);
            if (started > reduced) {
                currentChildParticleHandler = childParticleHandlers[i];
                if (isCurrentChildReduceAllowed()) handleCurrentChildReduce();
            }
            if (started > reduceCountList.get(i)) {
                throw new IllegalStateException();
            }
            if (childParticleHandlers[i] != null) {
                childParticleHandlers[i].recycle();
                childParticleHandlers[i] = null;
            }
        }
    }

    public boolean handleContentOrder(int expectedOrderHandlingCount, APattern child, APattern sourceDefinition) {
        int newChildIndex = child.getChildIndex();
        int prevCorrectChildIndex = childRecordIndex < 0 ? -1 : childDefinition[childRecordIndex].getChildIndex();
        int orderIndex = GOOD_ORDER;
        if (newChildIndex < prevCorrectChildIndex) {
            setAsCurrentChildParticleHandler(child);
            if (currentChildParticleHandler == null) {
                return true;
            }
            if (handleCurrentChildOrderReduce(child, sourceDefinition)) {
                return false;
            }
        } else if (newChildIndex == prevCorrectChildIndex) {
            addLastCorrectChildIndex(sourceDefinition);
        } else {
            addLastCorrectChildIndex(child, sourceDefinition);
        }
        return true;
    }

    public boolean handleContentOrder(int expectedOrderHandlingCount, APattern child, APattern sourceDefinition, InternalConflictResolver resolver) {
        throw new IllegalStateException();
    }

    public int functionalEquivalenceCode() {
        int orderCode = 0;
        for (int i = 0; i <= childRecordIndex; i++) {
            orderCode += childDefinition[i].getChildIndex();
        }
        orderCode *= (childRecordIndex + 1);
        orderCode *= 1000;
        return super.functionalEquivalenceCode() + orderCode;
    }

    public GroupMaximalReduceCountHandler getCopy(StackHandler stackHandler, ErrorCatcher errorCatcher) {
        throw new IllegalStateException();
    }

    public GroupMaximalReduceCountHandler getCopy(StructureHandler parent, StackHandler stackHandler, ErrorCatcher errorCatcher) {
        throw new IllegalStateException();
    }

    public GroupMaximalReduceCountHandler getCopy(IntList reduceCountList, IntList startedCountList, StackHandler stackHandler, ErrorCatcher errorCatcher) {
        GroupMaximalReduceCountHandler copy = ((AGroup) rule).getStructureHandler(reduceCountList, startedCountList, errorCatcher, (MaximalReduceStackHandler) stackHandler);
        copy.setState(stackHandler, errorCatcher, childParticleHandlers, childStructureHandlers, size, satisfactionLevel, saturationLevel, contentIndex, startInputRecordIndex, isStartSet, childDefinition, childInputRecordIndex, childSourceDefinition, childRecordIndex, childDetailsCurrentIndex);
        copy.setOriginal(this);
        return copy;
    }

    private void setOriginal(GroupMaximalReduceCountHandler original) {
        this.original = original;
    }

    public GroupMaximalReduceCountHandler getOriginal() {
        return original;
    }

    /**
	* It asseses the state of the handler and triggers reduce due to the saturation 
	* state of a handler(isReduceRequired() and isReduceAcceptable() are used).
	*/
    boolean handleStateSaturationReduce() {
        if (isCurrentChildReduceRequired() && isCurrentChildReduceAcceptable()) {
            handleCurrentChildReduce();
            return true;
        }
        return false;
    }

    void setAsCurrentChildParticleHandler(APattern childPattern) {
        if (!childPattern.getParent().equals(rule)) throw new IllegalArgumentException();
        int childIndex = childPattern.getChildIndex();
        currentChildParticleHandler = childParticleHandlers[childIndex];
    }

    void setCurrentChildParticleHandler(APattern childPattern) {
        if (!childPattern.getParent().equals(rule)) throw new IllegalArgumentException();
        int childIndex = childPattern.getChildIndex();
        int started = startedCountList.get(childIndex);
        if (childParticleHandlers[childIndex] == null) {
            childParticleHandlers[childIndex] = childPattern.getParticleHandler(this, errorCatcher);
            startedCountList.set(childIndex, ++started);
        } else {
            int reduced = reduceCountList.get(childIndex);
            if (started == reduced) {
                if (isChildStartAcceptable(childIndex, started)) {
                    childParticleHandlers[childIndex].reset();
                    startedCountList.set(childIndex, ++started);
                }
            }
        }
        currentChildParticleHandler = childParticleHandlers[childIndex];
    }

    void setEmptyState() {
        super.setEmptyState();
        for (int j = 0; j <= childRecordIndex; j++) {
            for (int i = 0; i <= childDetailsCurrentIndex[j]; i++) {
                activeInputDescriptor.unregisterClientForRecord(childInputRecordIndex[j][i], this);
                childSourceDefinition[j][i] = null;
            }
            childDetailsCurrentIndex[j] = -1;
            childDefinition[j] = null;
        }
        childRecordIndex = -1;
    }

    boolean handleOrderCheckedReduce(APattern sourceDefinition) {
        throw new IllegalStateException();
    }

    boolean handleOrderUncheckedReduce(APattern sourceDefinition) {
        throw new IllegalStateException();
    }

    boolean handleExcessiveChildReduce() {
        throw new IllegalStateException();
    }

    boolean isChildStartAcceptable(int childIndex, int started) {
        for (int i = 0; i < childIndex; i++) {
            if (startedCountList.get(i) <= started) return false;
        }
        return true;
    }

    void handleCurrentChildReduce() {
        APattern pattern = currentChildParticleHandler.getRule();
        int childIndex = pattern.getChildIndex();
        int started = startedCountList.get(childIndex);
        int reduced = reduceCountList.get(childIndex);
        if (started == reduced) {
            return;
        }
        reduceCountList.set(childIndex, 1 + reduced);
        if (childStructureHandlers[childIndex] != null) {
            childStructureHandlers[childIndex].recycle();
            childStructureHandlers[childIndex] = null;
        }
        if (childParticleHandlers[childIndex].getIndex() == CardinalityHandler.SATURATED) {
            currentChildParticleHandler.recycle();
            childParticleHandlers[childIndex] = null;
        }
    }

    private void setState(StackHandler stackHandler, ErrorCatcher errorCatcher, ParticleHandler[] cph, StructureHandler[] csh, int size, int satisfactionLevel, int saturationLevel, int contentIndex, int startInputRecordIndex, boolean isStartSet, APattern[] childDefinition, int[][] childInputRecordIndex, APattern[][] childSourceDefinition, int childRecordIndex, int[] childDetailsCurrentIndex) {
        if (this.childDefinition.length < childDefinition.length) {
            this.childDefinition = new APattern[childDefinition.length];
            this.childInputRecordIndex = new int[childDefinition.length][];
            this.childSourceDefinition = new APattern[childDefinition.length][];
            this.childDetailsCurrentIndex = new int[childDefinition.length];
        }
        for (int i = 0; i <= childRecordIndex; i++) {
            this.childDefinition[i] = childDefinition[i];
            this.childDetailsCurrentIndex[i] = childDetailsCurrentIndex[i];
            this.childInputRecordIndex[i] = new int[childInputRecordIndex[i].length];
            this.childSourceDefinition[i] = new APattern[childSourceDefinition[i].length];
            for (int j = 0; j <= childDetailsCurrentIndex[i]; j++) {
                this.childInputRecordIndex[i][j] = childInputRecordIndex[i][j];
                this.childSourceDefinition[i][j] = childSourceDefinition[i][j];
                activeInputDescriptor.registerClientForRecord(childInputRecordIndex[i][j], this);
            }
        }
        this.childRecordIndex = childRecordIndex;
        if (this.size < size) {
            childParticleHandlers = new ParticleHandler[size];
            childStructureHandlers = new StructureHandler[size];
        }
        this.size = size;
        for (int i = 0; i < size; i++) {
            if (cph[i] != null) childParticleHandlers[i] = cph[i].getCopy(this, errorCatcher);
            if (csh[i] != null) childStructureHandlers[i] = csh[i].getCopy(this, stackHandler, errorCatcher);
        }
        this.contentIndex = contentIndex;
        this.satisfactionLevel = satisfactionLevel;
        this.saturationLevel = saturationLevel;
        if (this.isStartSet) {
            activeInputDescriptor.unregisterClientForRecord(this.startInputRecordIndex, this);
        }
        this.startInputRecordIndex = startInputRecordIndex;
        this.isStartSet = isStartSet;
        if (isStartSet) {
            activeInputDescriptor.registerClientForRecord(startInputRecordIndex, this);
        }
    }

    boolean isCurrentChildReduceAcceptable() {
        if (currentChildParticleHandler.getIndex() == CardinalityHandler.SATURATED) return true;
        APattern currentChildPattern = currentChildParticleHandler.getRule();
        int currentChildIndex = currentChildPattern.getChildIndex();
        int potentialReduceCount = reduceCountList.get(currentChildIndex) + 1;
        for (int i = 0; i < currentChildIndex; i++) {
            if (reduceCountList.get(i) < potentialReduceCount) return false;
        }
        return true;
    }

    private void addLastCorrectChildIndex(APattern definition, APattern sourceDefinition) {
        if (++childRecordIndex == childDefinition.length) {
            int size = childDefinition.length + childRecordIncreaseSizeAmount;
            APattern increasedDefinition[] = new APattern[size];
            System.arraycopy(childDefinition, 0, increasedDefinition, 0, childRecordIndex);
            childDefinition = increasedDefinition;
            int increasedCDCI[] = new int[size];
            System.arraycopy(childDetailsCurrentIndex, 0, increasedCDCI, 0, childRecordIndex);
            childDetailsCurrentIndex = increasedCDCI;
            Arrays.fill(childDetailsCurrentIndex, childRecordIndex, childDetailsCurrentIndex.length, -1);
            int[][] increasedCII = new int[size][];
            for (int i = 0; i < childRecordIndex; i++) {
                if (childInputRecordIndex[i] != null) {
                    increasedCII[i] = new int[childInputRecordIndex[i].length];
                    System.arraycopy(childInputRecordIndex[i], 0, increasedCII[i], 0, childDetailsCurrentIndex[i] + 1);
                }
            }
            childInputRecordIndex = increasedCII;
            APattern increasedSourceDefinition[][] = new APattern[size][];
            for (int i = 0; i < childRecordIndex; i++) {
                if (childSourceDefinition[i] != null) {
                    increasedSourceDefinition[i] = new APattern[childSourceDefinition[i].length];
                    System.arraycopy(childSourceDefinition[i], 0, increasedSourceDefinition[i], 0, childDetailsCurrentIndex[i] + 1);
                }
            }
            childSourceDefinition = increasedSourceDefinition;
        }
        childDefinition[childRecordIndex] = definition;
        childDetailsCurrentIndex[childRecordIndex] = 0;
        childInputRecordIndex[childRecordIndex] = new int[childDetailsInitialSize];
        childInputRecordIndex[childRecordIndex][childDetailsCurrentIndex[childRecordIndex]] = inputStackDescriptor.getCurrentItemInputRecordIndex();
        childSourceDefinition[childRecordIndex] = new ActiveTypeItem[childDetailsInitialSize];
        childSourceDefinition[childRecordIndex][childDetailsCurrentIndex[childRecordIndex]] = sourceDefinition;
        activeInputDescriptor.registerClientForRecord(childInputRecordIndex[childRecordIndex][childDetailsCurrentIndex[childRecordIndex]], this);
    }

    private void addLastCorrectChildIndex(APattern sourceDefinition) {
        int oldLength = childInputRecordIndex[childRecordIndex].length;
        childDetailsCurrentIndex[childRecordIndex] += 1;
        if (childDetailsCurrentIndex[childRecordIndex] == oldLength) {
            int newLength = oldLength + childDetailsIncreaseSizeAmount;
            int increasedCII[] = new int[newLength];
            System.arraycopy(childInputRecordIndex[childRecordIndex], 0, increasedCII, 0, oldLength);
            childInputRecordIndex[childRecordIndex] = increasedCII;
            APattern increasedSourceDefinition[] = new APattern[newLength];
            System.arraycopy(childSourceDefinition[childRecordIndex], 0, increasedSourceDefinition, 0, oldLength);
            childSourceDefinition[childRecordIndex] = increasedSourceDefinition;
        }
        childInputRecordIndex[childRecordIndex][childDetailsCurrentIndex[childRecordIndex]] = inputStackDescriptor.getCurrentItemInputRecordIndex();
        childSourceDefinition[childRecordIndex][childDetailsCurrentIndex[childRecordIndex]] = sourceDefinition;
        activeInputDescriptor.registerClientForRecord(childInputRecordIndex[childRecordIndex][childDetailsCurrentIndex[childRecordIndex]], this);
    }

    private void removeLastCorrectChildIndex() {
        for (int i = 0; i <= childDetailsCurrentIndex[childRecordIndex]; i++) {
            activeInputDescriptor.unregisterClientForRecord(childInputRecordIndex[childRecordIndex][i], this);
            childSourceDefinition[childRecordIndex][i] = null;
        }
        childDefinition[childRecordIndex] = null;
        childDetailsCurrentIndex[childRecordIndex] = -1;
        childRecordIndex--;
    }

    public void accept(RuleHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return "GroupMaximalReduceCountHandler  " + rule.toString() + " " + satisfactionLevel + "/" + satisfactionIndicator + " contentIndex=" + contentIndex;
    }
}
