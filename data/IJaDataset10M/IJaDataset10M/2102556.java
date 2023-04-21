package de.tudresden.inf.lat.jcel.ontology.axiom.complex;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import de.tudresden.inf.lat.jcel.ontology.datatype.IntegerObjectPropertyExpression;

/**
 * This class models an axiom stating that one object property is the inverse of
 * another object property. <br />
 * This means r &equiv; s<sup>-</sup> .
 * 
 * @author Julian Mendez
 */
public class IntegerInverseObjectPropertiesAxiom implements ComplexIntegerAxiom {

    private final IntegerObjectPropertyExpression firstProperty;

    private final Set<Integer> objectPropertiesInSignature;

    private final IntegerObjectPropertyExpression secondProperty;

    /**
	 * Constructs a new inverse object property axiom, declaring that one object
	 * property is the inverse of another one.
	 * 
	 * @param first
	 *            object property
	 * @param second
	 *            object property
	 */
    protected IntegerInverseObjectPropertiesAxiom(IntegerObjectPropertyExpression first, IntegerObjectPropertyExpression second) {
        if (first == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        if (second == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        this.firstProperty = first;
        this.secondProperty = second;
        Set<Integer> objectPropertiesInSignature = new HashSet<Integer>();
        objectPropertiesInSignature.addAll(this.firstProperty.getObjectPropertiesInSignature());
        objectPropertiesInSignature.addAll(this.secondProperty.getObjectPropertiesInSignature());
        this.objectPropertiesInSignature = Collections.unmodifiableSet(objectPropertiesInSignature);
    }

    @Override
    public <T> T accept(ComplexIntegerAxiomVisitor<T> visitor) {
        if (visitor == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        boolean ret = (this == o);
        if (!ret && o instanceof IntegerInverseObjectPropertiesAxiom) {
            IntegerInverseObjectPropertiesAxiom other = (IntegerInverseObjectPropertiesAxiom) o;
            ret = getFirstProperty().equals(other.getFirstProperty()) && getSecondProperty().equals(other.getSecondProperty());
        }
        return ret;
    }

    @Override
    public Set<Integer> getClassesInSignature() {
        return Collections.emptySet();
    }

    @Override
    public Set<Integer> getDataPropertiesInSignature() {
        return Collections.emptySet();
    }

    @Override
    public Set<Integer> getDatatypesInSignature() {
        return Collections.emptySet();
    }

    /**
	 * Returns one of object properties (called first) in this axiom.
	 * 
	 * @return one of object properties in this axiom.
	 */
    public IntegerObjectPropertyExpression getFirstProperty() {
        return this.firstProperty;
    }

    @Override
    public Set<Integer> getIndividualsInSignature() {
        return Collections.emptySet();
    }

    @Override
    public Set<Integer> getObjectPropertiesInSignature() {
        return Collections.unmodifiableSet(this.objectPropertiesInSignature);
    }

    /**
	 * Returns one of object properties (called second) in this axiom.
	 * 
	 * @return one of object properties in this axiom.
	 */
    public IntegerObjectPropertyExpression getSecondProperty() {
        return this.secondProperty;
    }

    @Override
    public int hashCode() {
        return getFirstProperty().hashCode() + 31 * getSecondProperty().hashCode();
    }

    @Override
    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(ComplexIntegerAxiomConstant.InverseObjectProperties);
        sbuf.append(ComplexIntegerAxiomConstant.openPar);
        sbuf.append(getFirstProperty());
        sbuf.append(ComplexIntegerAxiomConstant.sp);
        sbuf.append(getSecondProperty());
        sbuf.append(ComplexIntegerAxiomConstant.closePar);
        return sbuf.toString();
    }
}
