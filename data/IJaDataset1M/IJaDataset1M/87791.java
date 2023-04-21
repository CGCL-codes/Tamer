package org.tzi.use.uml.ocl.value;

import java.util.Collection;
import java.util.Iterator;
import org.tzi.use.uml.ocl.type.Type;
import org.tzi.use.util.CollectionComparator;
import org.tzi.use.uml.ocl.expr.ExpInvalidException;
import java.util.HashSet;
import java.util.Set;
import org.tzi.use.uml.ocl.type.TypeFactory;

/**
 * Base class for collection values.
 *
 * @version     $ProjectVersion: 0.393 $
 * @author  Mark Richters
 * @see     SetValue
 * @see     SequenceValue
 * @see     BagValue
 */
public abstract class CollectionValue extends Value {

    private Type fElemType;

    CollectionValue(Type t, Type elemType) {
        super(t);
        fElemType = elemType;
    }

    public Type elemType() {
        return fElemType;
    }

    public void setElemType(Type t) {
        fElemType = t;
        if (this instanceof SetValue) {
            setType(TypeFactory.mkSet(fElemType));
        } else if (this instanceof BagValue) {
            setType(TypeFactory.mkBag(fElemType));
        } else if (this instanceof SequenceValue) {
            setType(TypeFactory.mkSequence(fElemType));
        } else {
            setType(TypeFactory.mkCollection(fElemType));
        }
    }

    public abstract Iterator iterator();

    public abstract int size();

    public abstract boolean isEmpty();

    public abstract boolean includes(Value v);

    public abstract boolean includesAll(CollectionValue v);

    public abstract boolean excludesAll(CollectionValue v);

    public abstract int count(Value v);

    public abstract Collection collection();

    public int compareTo(Object o) {
        if (o == this) return 0;
        if (o instanceof UndefinedValue) return +1;
        if (o instanceof CollectionValue) {
            CollectionValue c = (CollectionValue) o;
            int res = new CollectionComparator().compare(collection(), c.collection());
            if (c.getClass().equals(getClass())) {
                return res;
            } else {
                return (res != 0) ? res : +1;
            }
        }
        throw new ClassCastException();
    }

    /**
     * Returns the value for the type parameter of this collection.
     */
    protected Type inferElementType() throws ExpInvalidException {
        if (collection().size() == 0) {
            return fElemType;
        }
        Value[] values = (Value[]) collection().toArray(new Value[] {});
        Type t0 = values[0].type();
        boolean sameTypes = true;
        for (int i = 1; i < values.length; i++) if (!t0.equals(values[i].type())) {
            sameTypes = false;
            break;
        }
        if (sameTypes) return t0;
        Set cs = new HashSet();
        cs.addAll(values[0].type().allSupertypes());
        for (int i = 1; i < values.length; i++) {
            cs.retainAll(values[i].type().allSupertypes());
            if (cs.isEmpty()) throw new ExpInvalidException("Type mismatch, " + this.getClass().toString() + " element " + (i + 1) + " does not have a common supertype " + "with previous elements.");
        }
        if (cs.size() == 1) return (Type) cs.iterator().next();
        t0 = null;
        Iterator it1 = cs.iterator();
        outerLoop: while (it1.hasNext()) {
            Type t1 = (Type) it1.next();
            Iterator it2 = cs.iterator();
            while (it2.hasNext()) {
                Type t2 = (Type) it2.next();
                if (!t1.isSubtypeOf(t2)) continue outerLoop;
            }
            t0 = t1;
            break;
        }
        if (t0 != null) return t0;
        return fElemType;
    }

    protected void deriveRuntimeType() {
        try {
            setElemType(inferElementType());
        } catch (ExpInvalidException e) {
            throw new RuntimeException(e);
        }
    }
}
