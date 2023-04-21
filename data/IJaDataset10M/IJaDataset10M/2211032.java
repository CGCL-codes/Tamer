package de.tudresden.inf.lat.jcel.core.axiom.complex;

import java.util.Collections;
import java.util.Set;

/**
 * An object of this class is an axiom that declares a named individual.
 * 
 * @author Julian Mendez
 */
public class IntegerNamedIndividualDeclarationAxiom implements IntegerDeclarationAxiom {

    private Integer entity = null;

    /**
	 * Constructs a new named individual declaration axiom.
	 * 
	 * @param declaredEntity
	 *            named individual
	 */
    public IntegerNamedIndividualDeclarationAxiom(Integer declaredEntity) {
        if (declaredEntity == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        this.entity = declaredEntity;
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
        boolean ret = false;
        if (o instanceof IntegerNamedIndividualDeclarationAxiom) {
            IntegerNamedIndividualDeclarationAxiom other = (IntegerNamedIndividualDeclarationAxiom) o;
            ret = getEntity().equals(other.getEntity());
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

    @Override
    public Integer getEntity() {
        return this.entity;
    }

    @Override
    public Set<Integer> getIndividualsInSignature() {
        return Collections.singleton(this.entity);
    }

    @Override
    public Set<Integer> getObjectPropertiesInSignature() {
        return Collections.emptySet();
    }

    @Override
    public int hashCode() {
        return getEntity().hashCode();
    }

    @Override
    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(ComplexIntegerAxiomConstant.Declaration);
        sbuf.append(ComplexIntegerAxiomConstant.openPar);
        sbuf.append(ComplexIntegerAxiomConstant.NamedIndividualDeclaration);
        sbuf.append(ComplexIntegerAxiomConstant.openPar);
        sbuf.append(getEntity().toString());
        sbuf.append(ComplexIntegerAxiomConstant.closePar);
        sbuf.append(ComplexIntegerAxiomConstant.closePar);
        return sbuf.toString();
    }
}
