package serene.validation.handlers.structure.impl;

import java.util.Arrays;
import serene.validation.schema.active.components.APattern;
import serene.validation.schema.active.components.ActiveTypeItem;
import serene.validation.schema.active.components.AGroup;
import serene.validation.schema.active.components.AElement;
import serene.validation.schema.active.components.ARef;
import serene.validation.handlers.structure.StructureHandler;
import serene.validation.handlers.structure.RuleHandlerVisitor;
import serene.validation.handlers.conflict.InternalConflictResolver;
import serene.validation.handlers.stack.StackHandler;
import serene.validation.handlers.error.ErrorCatcher;

public class GroupHandler extends MultipleChildrenPatternHandler {

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

    GroupHandler original;

    GroupHandler() {
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

    void init(AGroup group, ErrorCatcher errorCatcher, StructureHandler parent, StackHandler stackHandler) {
        this.rule = group;
        satisfactionIndicator = group.getSatisfactionIndicator();
        saturationIndicator = group.getSaturationIndicator();
        this.errorCatcher = errorCatcher;
        this.parent = parent;
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

    public boolean handleContentOrder(int expectedOrderHandlingCount, APattern child, APattern sourceDefinition) {
        int newChildIndex = child.getChildIndex();
        int prevCorrectChildIndex = childRecordIndex < 0 ? -1 : childDefinition[childRecordIndex].getChildIndex();
        int oldCorrectChildIndex = childRecordIndex - 1 < 0 ? -1 : childDefinition[childRecordIndex - 1].getChildIndex();
        int orderIndex = GOOD_ORDER;
        APattern reper = null;
        APattern prevDefinition = null;
        int[] prevChildInputRecordIndex = null;
        APattern[] prevSourceDefinition = null;
        int prevDetailsCurrentIndex = -1;
        if (newChildIndex < prevCorrectChildIndex) {
            if (newChildIndex < oldCorrectChildIndex) {
                if (handleOrderUncheckedReduce(sourceDefinition)) {
                    return false;
                }
                orderIndex = CURRENT_MISPLACED;
                reper = childDefinition[childRecordIndex];
            } else if (newChildIndex == oldCorrectChildIndex) {
                if (handleOrderCheckedReduce(sourceDefinition)) {
                    return false;
                }
                orderIndex = PREVIOUS_MISPLACED;
                reper = child;
                prevDefinition = childDefinition[childRecordIndex];
                prevChildInputRecordIndex = childInputRecordIndex[childRecordIndex];
                prevSourceDefinition = childSourceDefinition[childRecordIndex];
                prevDetailsCurrentIndex = childDetailsCurrentIndex[childRecordIndex];
                activeInputDescriptor.registerClientForRecord(prevChildInputRecordIndex, 0, prevDetailsCurrentIndex + 1, this);
                removeLastCorrectChildIndex();
                addLastCorrectChildIndex(sourceDefinition);
            } else {
                if (handleOrderCheckedReduce(sourceDefinition)) {
                    return false;
                }
                orderIndex = PREVIOUS_MISPLACED;
                reper = child;
                prevDefinition = childDefinition[childRecordIndex];
                prevChildInputRecordIndex = childInputRecordIndex[childRecordIndex];
                prevSourceDefinition = childSourceDefinition[childRecordIndex];
                prevDetailsCurrentIndex = childDetailsCurrentIndex[childRecordIndex];
                activeInputDescriptor.registerClientForRecord(prevChildInputRecordIndex, 0, prevDetailsCurrentIndex + 1, this);
                removeLastCorrectChildIndex();
                addLastCorrectChildIndex(child, sourceDefinition);
            }
        } else if (newChildIndex == prevCorrectChildIndex) {
            addLastCorrectChildIndex(sourceDefinition);
        } else {
            addLastCorrectChildIndex(child, sourceDefinition);
        }
        if (--expectedOrderHandlingCount > 0) {
            if (parent.handleContentOrder(expectedOrderHandlingCount, rule, sourceDefinition)) {
                int contextInputRecordIndex;
                if (!isStartSet) {
                    contextInputRecordIndex = inputStackDescriptor.getCurrentItemInputRecordIndex();
                } else {
                    contextInputRecordIndex = startInputRecordIndex;
                }
                if (orderIndex == CURRENT_MISPLACED) {
                    errorCatcher.misplacedContent(rule, contextInputRecordIndex, child, inputStackDescriptor.getCurrentItemInputRecordIndex(), sourceDefinition, reper);
                } else if (orderIndex == PREVIOUS_MISPLACED) {
                    errorCatcher.misplacedContent(rule, contextInputRecordIndex, prevDefinition, Arrays.copyOfRange(prevChildInputRecordIndex, 0, prevDetailsCurrentIndex + 1), prevSourceDefinition, reper);
                    activeInputDescriptor.unregisterClientForRecord(prevChildInputRecordIndex, 0, prevDetailsCurrentIndex + 1, this);
                }
                return true;
            } else {
                if (prevChildInputRecordIndex != null) activeInputDescriptor.unregisterClientForRecord(prevChildInputRecordIndex, 0, prevDetailsCurrentIndex + 1, this);
                return false;
            }
        } else {
            int contextInputRecordIndex;
            if (!isStartSet) {
                contextInputRecordIndex = inputStackDescriptor.getCurrentItemInputRecordIndex();
            } else {
                contextInputRecordIndex = startInputRecordIndex;
            }
            if (orderIndex == CURRENT_MISPLACED) {
                errorCatcher.misplacedContent(rule, contextInputRecordIndex, child, inputStackDescriptor.getCurrentItemInputRecordIndex(), sourceDefinition, reper);
            } else if (orderIndex == PREVIOUS_MISPLACED) {
                errorCatcher.misplacedContent(rule, contextInputRecordIndex, prevDefinition, Arrays.copyOfRange(prevChildInputRecordIndex, 0, prevDetailsCurrentIndex + 1), prevSourceDefinition, reper);
                activeInputDescriptor.unregisterClientForRecord(prevChildInputRecordIndex, 0, prevDetailsCurrentIndex + 1, this);
            }
        }
        return true;
    }

    public boolean handleContentOrder(int expectedOrderHandlingCount, APattern child, APattern sourceDefinition, InternalConflictResolver resolver) {
        int newChildIndex = child.getChildIndex();
        int prevCorrectChildIndex = childRecordIndex < 0 ? -1 : childDefinition[childRecordIndex].getChildIndex();
        int oldCorrectChildIndex = childRecordIndex - 1 < 0 ? -1 : childDefinition[childRecordIndex - 1].getChildIndex();
        int orderIndex = GOOD_ORDER;
        APattern reper = null;
        APattern prevDefinition = null;
        int[] prevChildInputRecordIndex = null;
        APattern[] prevSourceDefinition = null;
        int prevDetailsCurrentIndex = -1;
        if (stackConflictsHandler != null && stackConflictsHandler.isConflictRule(child)) {
            stackConflictsHandler.record(child, (AGroup) rule, resolver);
        }
        if (newChildIndex < prevCorrectChildIndex) {
            if (newChildIndex < oldCorrectChildIndex) {
                if (handleOrderUncheckedReduce(sourceDefinition)) {
                    return false;
                }
                orderIndex = CURRENT_MISPLACED;
                reper = childDefinition[childRecordIndex];
            } else if (newChildIndex == oldCorrectChildIndex) {
                if (handleOrderCheckedReduce(sourceDefinition)) {
                    return false;
                }
                orderIndex = PREVIOUS_MISPLACED;
                reper = child;
                prevDefinition = childDefinition[childRecordIndex];
                prevChildInputRecordIndex = childInputRecordIndex[childRecordIndex];
                prevSourceDefinition = childSourceDefinition[childRecordIndex];
                prevDetailsCurrentIndex = childDetailsCurrentIndex[childRecordIndex];
                activeInputDescriptor.registerClientForRecord(prevChildInputRecordIndex, 0, prevDetailsCurrentIndex + 1, this);
                removeLastCorrectChildIndex();
                addLastCorrectChildIndex(sourceDefinition);
            } else {
                if (handleOrderCheckedReduce(sourceDefinition)) {
                    return false;
                }
                orderIndex = PREVIOUS_MISPLACED;
                reper = child;
                prevDefinition = childDefinition[childRecordIndex];
                prevChildInputRecordIndex = childInputRecordIndex[childRecordIndex];
                prevSourceDefinition = childSourceDefinition[childRecordIndex];
                prevDetailsCurrentIndex = childDetailsCurrentIndex[childRecordIndex];
                activeInputDescriptor.registerClientForRecord(prevChildInputRecordIndex, 0, prevDetailsCurrentIndex + 1, this);
                removeLastCorrectChildIndex();
                addLastCorrectChildIndex(child, sourceDefinition);
            }
        } else if (newChildIndex == prevCorrectChildIndex) {
            addLastCorrectChildIndex(sourceDefinition);
        } else {
            addLastCorrectChildIndex(child, sourceDefinition);
        }
        if (--expectedOrderHandlingCount > 0) {
            if (parent.handleContentOrder(expectedOrderHandlingCount, rule, sourceDefinition, resolver)) {
                int contextInputRecordIndex;
                if (!isStartSet) {
                    contextInputRecordIndex = inputStackDescriptor.getCurrentItemInputRecordIndex();
                } else {
                    contextInputRecordIndex = startInputRecordIndex;
                }
                if (orderIndex == CURRENT_MISPLACED) {
                    errorCatcher.misplacedContent(rule, contextInputRecordIndex, child, inputStackDescriptor.getCurrentItemInputRecordIndex(), sourceDefinition, reper);
                } else if (orderIndex == PREVIOUS_MISPLACED) {
                    errorCatcher.misplacedContent(rule, contextInputRecordIndex, prevDefinition, Arrays.copyOfRange(prevChildInputRecordIndex, 0, prevDetailsCurrentIndex + 1), prevSourceDefinition, reper);
                    activeInputDescriptor.unregisterClientForRecord(prevChildInputRecordIndex, 0, prevDetailsCurrentIndex + 1, this);
                }
                return true;
            } else {
                if (prevChildInputRecordIndex != null) activeInputDescriptor.unregisterClientForRecord(prevChildInputRecordIndex, 0, prevDetailsCurrentIndex + 1, this);
                return false;
            }
        } else {
            int contextInputRecordIndex;
            if (!isStartSet) {
                contextInputRecordIndex = inputStackDescriptor.getCurrentItemInputRecordIndex();
            } else {
                contextInputRecordIndex = startInputRecordIndex;
            }
            if (orderIndex == CURRENT_MISPLACED) {
                errorCatcher.misplacedContent(rule, contextInputRecordIndex, child, inputStackDescriptor.getCurrentItemInputRecordIndex(), sourceDefinition, reper);
            } else if (orderIndex == PREVIOUS_MISPLACED) {
                errorCatcher.misplacedContent(rule, contextInputRecordIndex, prevDefinition, Arrays.copyOfRange(prevChildInputRecordIndex, 0, prevDetailsCurrentIndex + 1), prevSourceDefinition, reper);
                activeInputDescriptor.unregisterClientForRecord(prevChildInputRecordIndex, 0, prevDetailsCurrentIndex + 1, this);
            }
        }
        return true;
    }

    public int functionalEquivalenceCode() {
        int orderCode = 0;
        for (int i = 0; i <= childRecordIndex; i++) {
            orderCode += childDefinition[i].hashCode();
        }
        return super.functionalEquivalenceCode() + orderCode;
    }

    public GroupHandler getCopy(StackHandler stackHandler, ErrorCatcher errorCatcher) {
        GroupHandler copy = (GroupHandler) ((AGroup) rule).getStructureHandler(errorCatcher, parent, stackHandler);
        copy.setState(stackHandler, errorCatcher, childParticleHandlers, childStructureHandlers, size, satisfactionLevel, saturationLevel, contentIndex, startInputRecordIndex, isStartSet, childDefinition, childInputRecordIndex, childSourceDefinition, childRecordIndex, childDetailsCurrentIndex);
        copy.setOriginal(this);
        return copy;
    }

    public GroupHandler getCopy(StructureHandler parent, StackHandler stackHandler, ErrorCatcher errorCatcher) {
        GroupHandler copy = (GroupHandler) ((AGroup) rule).getStructureHandler(errorCatcher, (StructureValidationHandler) parent, stackHandler);
        copy.setState(stackHandler, errorCatcher, childParticleHandlers, childStructureHandlers, size, satisfactionLevel, saturationLevel, contentIndex, startInputRecordIndex, isStartSet, childDefinition, childInputRecordIndex, childSourceDefinition, childRecordIndex, childDetailsCurrentIndex);
        copy.setOriginal(this);
        return copy;
    }

    private void setOriginal(GroupHandler original) {
        this.original = original;
    }

    public GroupHandler getOriginal() {
        return original;
    }

    void setEmptyState() {
        if (stackConflictsHandler != null) {
            stackConflictsHandler.close((AGroup) rule);
        }
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
        if (isReduceAllowed() && isReduceAcceptable()) {
            handleReshift(sourceDefinition);
            return true;
        }
        return false;
    }

    boolean handleOrderUncheckedReduce(APattern sourceDefinition) {
        if (isReduceAcceptable()) {
            handleValidatingReshift(sourceDefinition);
            return true;
        }
        return false;
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
        return "GroupHandler  " + rule.toString() + " " + satisfactionLevel + "/" + satisfactionIndicator + " contentIndex=" + contentIndex;
    }
}
