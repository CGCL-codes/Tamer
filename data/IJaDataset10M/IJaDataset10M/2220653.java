package org.tockit.relations.operations;

import java.util.Iterator;
import org.tockit.relations.model.Relation;
import org.tockit.relations.model.RelationImplementation;
import org.tockit.relations.model.Tuple;
import org.tockit.relations.operations.util.AbstractBinaryRelationOperation;

public class JoinOperation<D> extends AbstractBinaryRelationOperation<D> {

    public static <R> Relation<R> join(Relation<R> left, int[] leftColumns, Relation<R> right, int[] rightColumns) {
        JoinOperation<R> op = new JoinOperation<R>(leftColumns, false, rightColumns, true);
        return op.doApply(left, right);
    }

    public static <R> Relation<R> join(Relation<R> left, int leftColumn, Relation<R> right, int rightColumn) {
        JoinOperation<R> op = new JoinOperation<R>(new int[] { leftColumn }, false, new int[] { rightColumn }, true);
        return op.doApply(left, right);
    }

    public static <R> Relation<R> join(Relation<R> left, int[] leftColumns, Relation<R> right, int[] rightColumns, boolean dropColumns) {
        JoinOperation<R> op = new JoinOperation<R>(leftColumns, dropColumns, rightColumns, true);
        return op.doApply(left, right);
    }

    public static <R> Relation<R> join(Relation<R> left, int leftColumn, Relation<R> right, int rightColumn, boolean dropColumn) {
        JoinOperation<R> op = new JoinOperation<R>(new int[] { leftColumn }, dropColumn, new int[] { rightColumn }, true);
        return op.doApply(left, right);
    }

    private int[] leftHandColumns;

    private boolean dropLeftHandColumns;

    private int[] rightHandColumns;

    private boolean dropRightHandColumns;

    public JoinOperation(int[] leftHandColumns, boolean dropLeftHandColumns, int[] rightHandColumns, boolean dropRightHandColumns) {
        if (leftHandColumns.length != rightHandColumns.length) {
            throw new IllegalArgumentException("Equal number of columns needed for join");
        }
        if (leftHandColumns.length == 0) {
            throw new IllegalArgumentException("Columns must be specified for join");
        }
        this.leftHandColumns = leftHandColumns;
        this.dropLeftHandColumns = dropLeftHandColumns;
        this.rightHandColumns = rightHandColumns;
        this.dropRightHandColumns = dropRightHandColumns;
    }

    @Override
    public Relation<D> doApply(Relation<D> leftHandInput, Relation<D> rightHandInput) {
        int arity = leftHandInput.getArity() + rightHandInput.getArity();
        if (this.dropLeftHandColumns) {
            arity -= this.leftHandColumns.length;
        }
        if (this.dropRightHandColumns) {
            arity -= this.rightHandColumns.length;
        }
        String[] dimensionNames = getDimensionNames(leftHandInput, rightHandInput, arity);
        RelationImplementation<D> result = new RelationImplementation<D>(dimensionNames);
        for (Iterator<Tuple<? extends D>> iterLeft = leftHandInput.getTuples().iterator(); iterLeft.hasNext(); ) {
            Tuple<? extends D> leftTuple = iterLeft.next();
            for (Iterator<Tuple<? extends D>> iterRight = rightHandInput.getTuples().iterator(); iterRight.hasNext(); ) {
                Tuple<? extends D> rightTuple = iterRight.next();
                if (joinPossible(leftTuple, rightTuple)) {
                    result.addTuple(join(leftTuple, rightTuple, arity));
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private D[] join(Tuple<? extends D> leftTuple, Tuple<? extends D> rightTuple, int arity) {
        D[] result = (D[]) new Object[arity];
        int skippedDimensions = 0;
        tupleLoop: for (int i = 0; i < leftTuple.getLength(); i++) {
            if (this.dropLeftHandColumns) {
                for (int j = 0; j < this.leftHandColumns.length; j++) {
                    if (this.leftHandColumns[j] == i) {
                        skippedDimensions++;
                        continue tupleLoop;
                    }
                }
            }
            result[i - skippedDimensions] = leftTuple.getElement(i);
        }
        tupleLoop: for (int i = 0; i < rightTuple.getLength(); i++) {
            if (this.dropRightHandColumns) {
                for (int j = 0; j < this.rightHandColumns.length; j++) {
                    if (this.rightHandColumns[j] == i) {
                        skippedDimensions++;
                        continue tupleLoop;
                    }
                }
            }
            result[leftTuple.getLength() + i - skippedDimensions] = rightTuple.getElement(i);
        }
        return result;
    }

    private boolean joinPossible(Tuple<? extends D> leftTuple, Tuple<? extends D> rightTuple) {
        for (int i = 0; i < this.leftHandColumns.length; i++) {
            int colLeft = this.leftHandColumns[i];
            int colRight = this.rightHandColumns[i];
            if (!leftTuple.getElement(colLeft).equals(rightTuple.getElement(colRight))) {
                return false;
            }
        }
        return true;
    }

    private String[] getDimensionNames(Relation<D> leftHandInput, Relation<D> rightHandInput, int arity) {
        String[] dimensionNames = new String[arity];
        int skippedDimensions = 0;
        for (int i = 0; i < leftHandInput.getDimensionNames().length; i++) {
            if (this.dropLeftHandColumns) {
                for (int j = 0; j < this.leftHandColumns.length; j++) {
                    if (this.leftHandColumns[j] == i) {
                        skippedDimensions++;
                        continue;
                    }
                }
            }
            dimensionNames[i - skippedDimensions] = leftHandInput.getDimensionNames()[i];
        }
        for (int i = 0; i < rightHandInput.getDimensionNames().length; i++) {
            if (this.dropRightHandColumns) {
                for (int j = 0; j < this.rightHandColumns.length; j++) {
                    if (this.rightHandColumns[j] == i) {
                        skippedDimensions++;
                        continue;
                    }
                }
            }
            dimensionNames[leftHandInput.getDimensionNames().length + i - skippedDimensions] = rightHandInput.getDimensionNames()[i];
        }
        return dimensionNames;
    }

    /**
	 * @todo give better naming scheme
	 */
    public String getName() {
        return "Join";
    }
}
