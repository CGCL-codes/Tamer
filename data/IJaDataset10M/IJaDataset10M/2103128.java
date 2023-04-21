package org.mushroomdb.engine.operation.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.mushroomdb.catalog.Column;
import org.mushroomdb.catalog.Register;
import org.mushroomdb.catalog.impl.MultipleRegister;
import org.mushroomdb.engine.operation.AbstractBinaryRelationOperation;
import org.mushroomdb.engine.operation.RelationOperation;
import org.mushroomdb.engine.optimizer.RelationOperationVisitor;
import org.mushroomdb.engine.optimizer.impl.OutputColumnScanningVisitor;
import org.mushroomdb.exception.RDBMSRuntimeException;
import org.mushroomdb.executionplan.ExecutionPlanElement;
import org.mushroomdb.executionplan.ExecutionPlanElementImpl;
import org.mushroomdb.query.impl.condition.Condition;
import org.mushroomdb.query.impl.condition.EqualsToCondition;
import org.mushroomdb.query.impl.condition.EvaluationCondition;

/**
 * @author Matu
 *
 */
public class SortMergeJoinOperation extends AbstractBinaryRelationOperation {

    private Set<EvaluationCondition> conditions = new HashSet<EvaluationCondition>();

    /**
	 * @param leftRelationOperation
	 * @param rightRelationOperation
	 */
    public SortMergeJoinOperation(RelationOperation leftRelationOperation, RelationOperation rightRelationOperation, EvaluationCondition evaluationCondition) {
        super(leftRelationOperation, rightRelationOperation);
        OutputColumnScanningVisitor leftScanner = new OutputColumnScanningVisitor();
        OutputColumnScanningVisitor rightScanner = new OutputColumnScanningVisitor();
        leftRelationOperation.accept(leftScanner);
        rightRelationOperation.accept(rightScanner);
        Set leftColumns = leftScanner.getColumns();
        Set rightColumns = rightScanner.getColumns();
        ExternalMergeSortOperation leftExternalMergeSortOperation = null;
        ExternalMergeSortOperation rightExternalMergeSortOperation = null;
        if (!(evaluationCondition instanceof EqualsToCondition)) {
            throw new RDBMSRuntimeException("La condici�n de evaluaci�n no es propicia para un join.");
        }
        if (leftColumns.contains(evaluationCondition.getColumn())) {
            leftExternalMergeSortOperation = new ExternalMergeSortOperation(leftRelationOperation, evaluationCondition.getColumn());
            if (evaluationCondition.getValue() instanceof Column) {
                rightExternalMergeSortOperation = new ExternalMergeSortOperation(rightRelationOperation, (Column) evaluationCondition.getValue());
            } else throw new RDBMSRuntimeException("La condici�n de evaluaci�n no es propicia para un join.");
        } else {
            rightExternalMergeSortOperation = new ExternalMergeSortOperation(rightRelationOperation, evaluationCondition.getColumn());
            if (evaluationCondition.getValue() instanceof Column) {
                leftExternalMergeSortOperation = new ExternalMergeSortOperation(leftRelationOperation, (Column) evaluationCondition.getValue());
            } else throw new RDBMSRuntimeException("La condici�n de evaluaci�n no es propicia para un join.");
        }
        this.setLeftRelationOperation(leftExternalMergeSortOperation);
        this.setRightRelationOperation(rightExternalMergeSortOperation);
        this.conditions.add(evaluationCondition);
    }

    /**
	 * @see org.mushroomdb.engine.operation.RelationOperation#execute()
	 */
    @SuppressWarnings("unchecked")
    public Iterator execute() {
        final Iterator leftIterator = this.leftRelationOperation.execute();
        final Iterator rightIterator = this.rightRelationOperation.execute();
        EvaluationCondition evaluationCondition = this.conditions.iterator().next();
        final Column rightColumn, leftColumn;
        OutputColumnScanningVisitor vis = new OutputColumnScanningVisitor();
        leftRelationOperation.accept(vis);
        Set<Column> leftColumns = (Set<Column>) vis.getColumns();
        if (leftColumns.contains(evaluationCondition.getColumn())) {
            leftColumn = evaluationCondition.getColumn();
            rightColumn = (Column) evaluationCondition.getValue();
        } else {
            rightColumn = evaluationCondition.getColumn();
            leftColumn = (Column) evaluationCondition.getValue();
        }
        return new Iterator() {

            private Register foundRegister;

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public boolean hasNext() {
                return this.findNext() != null;
            }

            public Object next() {
                Register register = this.findNext();
                this.foundRegister = null;
                return register;
            }

            /**
			 * Encuentra un registro para devolver.
			 * @return
			 */
            private Register findNext() {
                if (this.foundRegister != null) {
                    return this.foundRegister;
                }
                if (leftIterator.hasNext() && rightIterator.hasNext()) {
                    Register leftRegister = (Register) leftIterator.next();
                    Register rightRegister = (Register) rightIterator.next();
                    Comparable rightValue = (Comparable) rightRegister.getValue(rightRegister.getColumnIndex(rightColumn));
                    Comparable leftValue = (Comparable) leftRegister.getValue(leftRegister.getColumnIndex(leftColumn));
                    while ((rightValue.compareTo(leftValue) > 0 && leftIterator.hasNext()) || (rightValue.compareTo(leftValue) < 0 && rightIterator.hasNext())) {
                        if (rightValue.compareTo(leftValue) > 0 && leftIterator.hasNext()) {
                            leftRegister = (Register) leftIterator.next();
                            leftValue = (Comparable) leftRegister.getValue(leftRegister.getColumnIndex(leftColumn));
                        } else if (rightValue.compareTo(leftValue) < 0 && rightIterator.hasNext()) {
                            rightRegister = (Register) rightIterator.next();
                            rightValue = (Comparable) rightRegister.getValue(rightRegister.getColumnIndex(rightColumn));
                        }
                    }
                    if (rightValue.compareTo(leftValue) == 0) {
                        this.foundRegister = new MultipleRegister(leftRegister, rightRegister);
                    }
                }
                return this.foundRegister;
            }
        };
    }

    /**
	 * toString
	 */
    public String toString() {
        Iterator iterator = this.conditions.iterator();
        String conditions = "";
        while (iterator.hasNext()) {
            if (conditions.length() > 0) {
                conditions = conditions + ",";
            }
            conditions = conditions + ((Condition) iterator.next()).toString();
        }
        String rv = "(" + this.leftRelationOperation.toString() + " SMJ[" + conditions + "] " + this.rightRelationOperation.toString() + ")";
        return rv;
    }

    /**
	 * @see org.mushroomdb.engine.operation.RelationOperation#buildExecutionPlan()
	 */
    public ExecutionPlanElement buildExecutionPlan() {
        List<String> conditionsNames = new LinkedList<String>();
        Iterator<EvaluationCondition> iterator = this.conditions.iterator();
        while (iterator.hasNext()) {
            conditionsNames.add(iterator.next().toString());
        }
        ExecutionPlanElementImpl executionPlan = new ExecutionPlanElementImpl("SMJ", conditionsNames);
        executionPlan.addChild(this.leftRelationOperation.buildExecutionPlan());
        executionPlan.addChild(this.rightRelationOperation.buildExecutionPlan());
        return executionPlan;
    }

    public void accept(RelationOperationVisitor v) {
        v.visitSortMergeJoin(this);
        this.getLeftRelationOperation().accept(v);
        this.getRightRelationOperation().accept(v);
    }

    /**
	 * Agregado por Hache para optimizar
	 * @return
	 */
    public Iterator getConditions() {
        Iterator iterator = this.conditions.iterator();
        return iterator;
    }
}
