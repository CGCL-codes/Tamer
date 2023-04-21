package uk.ac.warwick.dcs.cokefolk.server.operations.types;

import uk.ac.warwick.dcs.cokefolk.server.databaseconnectivity.DatabaseException;
import java.util.*;

public class DTypeComparator implements Comparator<DType<?>> {

    @SuppressWarnings(value = { "unchecked" })
    public int compare(DType<?> a, DType<?> b) throws NullPointerException {
        if (a == null || b == null) throw new NullPointerException();
        Class<? extends DType> aClass = a.getClass();
        Class<? extends DType> bClass = b.getClass();
        try {
            aClass.getDeclaredMethod("compareTo", DType.class);
            return a.compareTo(b);
        } catch (Exception e) {
        }
        try {
            bClass.getDeclaredMethod("compareTo", DType.class);
            return -b.compareTo(a);
        } catch (Exception e) {
        }
        int result = -1;
        Object aValue = a.actualRep(), bValue = b.actualRep();
        if (aValue.getClass().isAssignableFrom(bValue.getClass())) {
            try {
                if (a.equalTo((DType) b).asBoolean()) return 0;
            } catch (DatabaseException e) {
                throw new IllegalStateException(e.getLocalizedMessage());
            } catch (TypeMismatchException e) {
            }
            if (aValue instanceof Comparable) {
                result = ((Comparable) aValue).compareTo(bValue);
            } else if (bValue instanceof Comparable) {
                result = ((Comparable) bValue).compareTo(aValue);
            }
        } else {
            result = a.compareTo(b);
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DTypeComparator) {
            return true;
        } else {
            return super.equals(obj);
        }
    }
}
