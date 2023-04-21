package uk.ac.warwick.dcs.cokefolk.server.operations.types;

import java.util.*;
import uk.ac.warwick.dcs.cokefolk.NotImplementedException;
import uk.ac.warwick.dcs.cokefolk.parser.syntaxtree.ScalarExp;
import uk.ac.warwick.dcs.cokefolk.server.operations.types.TupleComparator.SortDirection;
import uk.ac.warwick.dcs.cokefolk.server.typechecker.*;
import uk.ac.warwick.dcs.cokefolk.util.datastructures.Pair;

/**
 * @author Adrian
 */
public class DArray<T extends DType<?>> extends DType<List<T>> implements RandomAccess {

    private final DKind componentType;

    public DArray(DKind componentType) {
        super(new ArrayList<T>());
        this.componentType = componentType;
    }

    private DArray(DKind componentType, List<T> elements) {
        super(elements);
        this.componentType = componentType;
    }

    public void load(DRelation relation, List<Pair<ScalarExp, SortDirection>> ordering) {
        throw new NotImplementedException();
    }

    @Override
    public DArray<T> factory(Object init) {
        throw new NotImplementedException();
    }

    public T get(int index) throws DIndexOutOfBoundsException {
        throw new NotImplementedException();
    }

    public void set(int index, T value) throws DIndexOutOfBoundsException, TypeMismatchException {
        throw new NotImplementedException();
    }

    public DInteger count() {
        return new DInteger(actualRep().size());
    }

    @Override
    public DBoolean equalTo(DType<List<T>> y) throws TypeMismatchException, NullPointerException {
        if (y == null) throw new NullPointerException();
        DKind thisKind = new DKindArray(this.componentType);
        DKind yKind = y.getKind();
        if (thisKind.unify(yKind) == null) {
            throw new TypeMismatchException("Array equality", thisKind, yKind);
        }
        List<T> elements = this.actualRep();
        List<T> thoseElements = y.actualRep();
        if (elements.size() != thoseElements.size()) return DBoolean.FALSE;
        HashSet<T> theseElements = new HashSet<T>(elements);
        for (T element : thoseElements) {
            if (!theseElements.contains(element)) {
                return DBoolean.FALSE;
            }
        }
        return DBoolean.TRUE;
    }

    @Override
    public boolean isOfType(DKind type) {
        if (type.syntacticallyEquals(DKindArray.UNKNOWN_ARRAY)) {
            return true;
        } else {
            return this.getKind().subtype(type);
        }
    }

    @Override
    public DKind getKind() {
        return new DKindArray(componentType);
    }

    @Override
    public DArray<T> clone() {
        List<T> actualRep = this.actualRep();
        ArrayList<T> newArray = new ArrayList<T>(actualRep.size());
        for (T element : actualRep) {
            @SuppressWarnings("unchecked") T clonedElement = (T) element.clone();
            newArray.add(clonedElement);
        }
        return new DArray<T>(this.componentType, newArray);
    }

    public int compareTo(DType<?> x) {
        if (x == null) throw new NullPointerException();
        DType xRaw = x;
        if (xRaw instanceof DArray) {
            throw new NotImplementedException();
        } else {
            return this.compareHashCode(x);
        }
    }

    public String toStringBrief() {
        List<T> actualRep = this.actualRep();
        StringBuilder result = new StringBuilder();
        result.append("[");
        Iterator<T> elementsiter = actualRep.iterator();
        for (int i = 0; i < 5 && elementsiter.hasNext(); i++) {
            result.append(elementsiter.next().toString());
            result.append(", ");
        }
        int strlen = result.length();
        result.delete(strlen - 2, strlen - 1);
        if (elementsiter.hasNext()) {
            result.append("... ] (");
            result.append(actualRep.size());
            result.append("  elements)");
        } else {
            result.append(" ]");
        }
        return result.toString();
    }
}
