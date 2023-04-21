package org.tockit.relations;

import java.util.Iterator;
import java.util.Set;
import org.tockit.relations.model.Relation;
import org.tockit.relations.model.RelationImplementation;
import org.tockit.relations.operations.CrossproductOperation;
import org.tockit.relations.operations.IntersectionOperation;
import org.tockit.relations.operations.PermutationOperation;
import org.tockit.relations.operations.PickColumnsOperation;

public class BasicRelationalAlgebra {

    @SuppressWarnings("unchecked")
    public static <D> Relation<D> getDelta(Set<D> domain) {
        RelationImplementation<D> result = new RelationImplementation<D>(2);
        for (Iterator<D> iter = domain.iterator(); iter.hasNext(); ) {
            D cur = iter.next();
            result.addTuple((D[]) new Object[] { cur, cur });
        }
        return result;
    }

    public static <D> Relation<D> intersect(Relation<D> left, Relation<D> right) {
        return IntersectionOperation.intersect(left, right);
    }

    public static <D> Relation<D> crossproduct(Relation<D> left, Relation<D> right) {
        return CrossproductOperation.crossproduct(left, right);
    }

    public static <D> Relation<D> project(Relation<D> input, int[] columns) {
        int lastCol = -1;
        for (int i = 0; i < columns.length; i++) {
            int col = columns[i];
            if (col < 0 || col >= input.getArity()) {
                throw new IllegalArgumentException("Column out of range");
            }
            if (col <= lastCol) {
                throw new IllegalArgumentException("Columns have to be given in increasing order");
            }
            lastCol = col;
        }
        return PickColumnsOperation.pickColumns(input, columns);
    }

    public static <D> Relation<D> permute(Relation<D> input, int firstColumn, int secondColumn) {
        return PermutationOperation.exchange(input, firstColumn, secondColumn);
    }
}
