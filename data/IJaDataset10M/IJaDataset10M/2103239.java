package uk.ac.lkl.migen.system.expresser.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import uk.ac.lkl.common.util.expression.Expression;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BlockShape;
import uk.ac.lkl.migen.system.expresser.model.tiednumber.TiedNumberExpression;

/**
 * Walker that implements replaceUnlockedTiedNumbersWithNewValues()
 * 
 * @author Ken Kahn
 *
 */
public class ReplaceUnlockedTiedNumbersWithNewValuesWalker extends Walker {

    private HashMap<TiedNumberExpression<?>, TiedNumberExpression<?>> mappingOldVariablesToNew = new HashMap<TiedNumberExpression<?>, TiedNumberExpression<?>>();

    private ArrayList<Integer> offsetsInUse = new ArrayList<Integer>();

    private static int randomOffsets[] = { 2, -3, -5, 5, 4, -2, 3, -4 };

    private static int emergencyRandomOffsets[] = { 6, 12, 8, -8, 10, 7, 11, -6, 9, -9, -12, -10, -7, -11 };

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean tiedNumberFound(TiedNumberExpression<?> oldTiedNumber, BlockShape shape, AttributeHandle<IntegerValue> handle, ExpresserModel model) {
        if (!oldTiedNumber.isLocked()) {
            TiedNumberExpression<?> newTiedNumber = mappingOldVariablesToNew.get(oldTiedNumber);
            if (newTiedNumber == null) {
                TiedNumberExpression<?> trueOldTiedNumber = getKeyWithValue(oldTiedNumber, mappingOldVariablesToNew);
                if (trueOldTiedNumber == null) {
                    newTiedNumber = oldTiedNumber.createFreshCopy(true);
                    newOffset((TiedNumberExpression<IntegerValue>) oldTiedNumber, (TiedNumberExpression<IntegerValue>) newTiedNumber);
                    mappingOldVariablesToNew.put(oldTiedNumber, newTiedNumber);
                } else {
                    newTiedNumber = oldTiedNumber;
                    oldTiedNumber = trueOldTiedNumber;
                }
            } else {
                newTiedNumber.setName(oldTiedNumber.getName());
                newTiedNumber.setNamed(oldTiedNumber.isNamed());
                if (oldTiedNumber.getValue().equals(newTiedNumber.getValue())) {
                    newOffset((TiedNumberExpression<IntegerValue>) oldTiedNumber, (TiedNumberExpression<IntegerValue>) newTiedNumber);
                }
            }
            if (handle != null) {
                if (shape != null) {
                    Attribute<?> attribute = shape.getAttribute(handle);
                    if (attribute != null) {
                        Expression<?> attributeValueExpression = attribute.getValueSource().getExpression();
                        if (attributeValueExpression == oldTiedNumber) {
                            attribute.setValueSource(new ExpressionValueSource(newTiedNumber));
                        } else {
                            attributeValueExpression.replaceAll(oldTiedNumber, newTiedNumber);
                        }
                    }
                }
            } else if (shape == null) {
                Expression<IntegerValue> totalAllocationExpression = model.getTotalAllocationExpression();
                if (totalAllocationExpression == oldTiedNumber) {
                    model.setTotalAllocationExpression((Expression<IntegerValue>) newTiedNumber);
                } else {
                    totalAllocationExpression.replaceAll(oldTiedNumber, newTiedNumber);
                }
            }
        }
        return true;
    }

    private static TiedNumberExpression<?> getKeyWithValue(TiedNumberExpression<?> value, HashMap<TiedNumberExpression<?>, TiedNumberExpression<?>> map) {
        Set<Entry<TiedNumberExpression<?>, TiedNumberExpression<?>>> entrySet = map.entrySet();
        for (Entry<TiedNumberExpression<?>, TiedNumberExpression<?>> entry : entrySet) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    protected void newOffset(TiedNumberExpression<IntegerValue> oldTiedNumber, TiedNumberExpression<IntegerValue> newTiedNumber) {
        int intValue = oldTiedNumber.getValue().getInt();
        int offset = computeValueOffsetForSlave(oldTiedNumber.hashCode(), intValue);
        newTiedNumber.setValue(new IntegerValue(intValue + offset));
    }

    public int computeValueOffsetForSlave(int hashCode, int intValue) {
        int hashCodeRemaining = hashCode;
        int key = hashCode + intValue * 7;
        int offset = randomOffsets[key % randomOffsets.length];
        if (intValue + offset <= 1) {
            offset = Math.abs(offset);
        }
        int offsetThatMayBeInUse = offset;
        while (isOffsetInUse(offset) && hashCodeRemaining > 0) {
            hashCodeRemaining = hashCodeRemaining / randomOffsets.length;
            offset = randomOffsets[hashCodeRemaining % randomOffsets.length];
            if (intValue + offset <= 1) {
                offset = Math.abs(offset);
            }
        }
        if (hashCodeRemaining == 0) {
            hashCodeRemaining = hashCode;
            key = hashCode + intValue * 7;
            offset = emergencyRandomOffsets[key % emergencyRandomOffsets.length];
            if (intValue + offset <= 1) {
                offset = Math.abs(offset);
            }
            while (isOffsetInUse(offset) && hashCodeRemaining > 0) {
                hashCodeRemaining = hashCodeRemaining / emergencyRandomOffsets.length;
                offset = emergencyRandomOffsets[hashCodeRemaining % emergencyRandomOffsets.length];
                if (intValue + offset <= 1) {
                    offset = Math.abs(offset);
                }
            }
        }
        if (hashCodeRemaining == 0) {
            offset = firstUnusedOffset(2 - intValue);
            if (offset == 0) {
                return offsetThatMayBeInUse;
            }
        }
        usingOffset(offset);
        return offset;
    }

    public boolean isOffsetInUse(Integer offset) {
        return offsetsInUse.contains(offset);
    }

    public void usingOffset(Integer offset) {
        offsetsInUse.add(offset);
    }

    public int firstUnusedOffset(int minimum) {
        for (int offset : randomOffsets) {
            if (offset >= minimum && !isOffsetInUse(offset)) {
                return offset;
            }
        }
        for (int offset : emergencyRandomOffsets) {
            if (offset >= minimum && !isOffsetInUse(offset)) {
                return offset;
            }
        }
        return 0;
    }
}
