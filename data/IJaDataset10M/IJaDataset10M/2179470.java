package org.tockit.relations.operations;

import java.util.Iterator;
import org.tockit.relations.model.Relation;
import org.tockit.relations.model.RelationImplementation;
import org.tockit.relations.model.Tuple;
import org.tockit.relations.operations.util.AbstractUnaryRelationOperation;

public class DropColumnsOperation<D> extends AbstractUnaryRelationOperation<D> {

    public static <R> Relation<R> drop(Relation<R> input, int[] columnsToDrop) {
        DropColumnsOperation<R> op = new DropColumnsOperation<R>(columnsToDrop);
        return op.doApply(input);
    }

    public static <R> Relation<R> drop(Relation<R> input, int columnToDrop) {
        DropColumnsOperation<R> op = new DropColumnsOperation<R>(new int[] { columnToDrop });
        return op.doApply(input);
    }

    private int[] columnsToDrop;

    private String name;

    /**
	 * @todo add sanity checking: no duplicates allowed
	 */
    public DropColumnsOperation(int[] columnsToDrop) {
        this.columnsToDrop = columnsToDrop;
        this.name = "Drop columns (";
        for (int i = 0; i < columnsToDrop.length; i++) {
            if (i != 0) {
                this.name += ",";
            }
            this.name += columnsToDrop[i];
        }
        this.name += ")";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String[] getParameterNames() {
        return new String[] { "input" };
    }

    /**
	 * @todo add check for dropping things that aren't there
	 */
    @Override
    public Relation<D> doApply(Relation<D> input) {
        String[] dimensionNames = new String[input.getArity() - this.columnsToDrop.length];
        int columnsDropped = 0;
        outerLoop: for (int i = 0; i < input.getDimensionNames().length; i++) {
            for (int j = 0; j < this.columnsToDrop.length; j++) {
                if (i == this.columnsToDrop[j]) {
                    columnsDropped++;
                    continue outerLoop;
                }
            }
            dimensionNames[i - columnsDropped] = input.getDimensionNames()[i];
        }
        RelationImplementation<D> result = new RelationImplementation<D>(dimensionNames);
        for (Iterator<Tuple<? extends D>> iter = input.getTuples().iterator(); iter.hasNext(); ) {
            Tuple<? extends D> tuple = iter.next();
            result.addTuple(project(tuple.getData()));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private D[] project(D[] input) {
        D[] result = (D[]) new Object[input.length - this.columnsToDrop.length];
        int columnsDropped = 0;
        outerLoop: for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < this.columnsToDrop.length; j++) {
                if (i == this.columnsToDrop[j]) {
                    columnsDropped++;
                    continue outerLoop;
                }
            }
            result[i - columnsDropped] = input[i];
        }
        return result;
    }
}
