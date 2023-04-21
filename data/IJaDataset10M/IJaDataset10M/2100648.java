package de.tuberlin.cs.cis.ocl.eval.instance;

import java.util.Collection;
import java.util.Vector;
import de.tuberlin.cs.cis.ocl.type.Type;
import de.tuberlin.cs.cis.ocl.type.reflect.OclBag;
import de.tuberlin.cs.cis.ocl.type.reflect.OclBoolean;
import de.tuberlin.cs.cis.ocl.type.reflect.OclExpression;
import de.tuberlin.cs.cis.ocl.type.reflect.OclInteger;
import de.tuberlin.cs.cis.ocl.type.reflect.OclSequence;
import de.tuberlin.cs.cis.ocl.type.reflect.OclSet;

/**
 * Represents an instance of the OCL type Bag.
 * 
 * @author fchabar
 *
 */
public class BagInstance extends CollectionInstance implements OclBag {

    /**
	 * Constructs an instance of the OCL type Bag via a Java 
	 * <code>Bag</code>. 
	 * @param elemType the OCL runtime type of the elements contained in
	 * the Bag. 
	 * @param representation the Java Bag representation of this 
	 * OCL Set.
	 */
    public BagInstance(Type elemType, Vector representation) {
        super(elemType, representation);
    }

    public OclSequence asSequence() {
        return _asSequence();
    }

    public OclSet asSet() {
        return _asSet();
    }

    public OclBag collect(OclExpression expr) {
        return (OclBag) _collect(expr);
    }

    public OclInteger count(Object object) {
        return _count(object);
    }

    public OclBoolean eq(OclBag bag2) {
        return new BooleanInstance(this.equals(bag2));
    }

    public OclBag intersection(OclBag bag2) {
        return (OclBag) _intersection(bag2);
    }

    public OclSet intersection(OclSet set) {
        return (OclSet) _intersection(set);
    }

    public OclBag reject(OclExpression expr) {
        return (OclBag) _reject(expr);
    }

    public OclBag select(OclExpression expr) {
        return (OclBag) _select(expr);
    }

    public OclBag union(OclBag bag2) {
        return (OclBag) _union(bag2);
    }

    public OclBag union(OclSet set) {
        return (OclBag) _union(set);
    }

    public OclBag excluding(Object object) {
        return (OclBag) _excluding(object);
    }

    public OclBag including(Object object) {
        return (OclBag) _including(object);
    }

    public Object clone() {
        Collection thisBag = collectionValue(this);
        Vector newBag = new Vector(thisBag.size());
        newBag.addAll(thisBag);
        return new BagInstance(this.getElementType(), newBag);
    }
}
