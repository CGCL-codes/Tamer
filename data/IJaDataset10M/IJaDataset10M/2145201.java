package org.hl7.types.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.UUID;
import org.hl7.types.ANY;
import org.hl7.types.BL;
import org.hl7.types.NullFlavor;
import org.hl7.types.enums.SetOperator;

/**
 * Root class for all types in this implementation.
 */
public abstract class ANYimpl implements ANY, Serializable {

    private NullFlavor _nullFlavor = null;

    protected ANYimpl(NullFlavor nf) {
        this._nullFlavor = (NullFlavorImpl) nf;
    }

    public NullFlavor nullFlavor() {
        if (_nullFlavor == null) _nullFlavor = NullFlavorImpl.NOT_A_NULL_FLAVOR;
        return _nullFlavor;
    }

    public boolean isNullJ() {
        return isNull().isTrue();
    }

    public boolean nonNullJ() {
        return nonNull().isTrue();
    }

    public boolean notApplicableJ() {
        return notApplicable().isTrue();
    }

    public boolean unknownJ() {
        return unknown().isTrue();
    }

    public boolean otherJ() {
        return other().isTrue();
    }

    public BL isNull() {
        return nullFlavor().implies(NullFlavorImpl.NI);
    }

    public BL nonNull() {
        return nullFlavor().isNull();
    }

    public BL notApplicable() {
        return nullFlavor().implies(NullFlavorImpl.NA);
    }

    public BL unknown() {
        return nullFlavor().implies(NullFlavorImpl.UNK);
    }

    public BL other() {
        return nullFlavor().implies(NullFlavorImpl.OTH);
    }

    /**
	 * We require that all inheritors of ANYimpl have their own equal function for all of its compatible types.
	 * 
	 * <p>
	 * The equals function is not a no-brainer. THINK!
	 * 
	 * <p>
	 * Equal must first test for type compatibility (not necessarily type-equality.) Be sure to test for the interface,
	 * not the implementation type, as the same type may have different implementations.
	 */
    public abstract BL equal(ANY x);

    /**
	 * Unique object identifier for persistence. This is an assigned UUID, so that it will always work.
	 */
    private String _internalId = null;

    public String getInternalId() {
        if (_internalId == null) _internalId = UUID.randomUUID().toString();
        return _internalId;
    }

    protected void setInternalId(String value) {
        _internalId = value;
    }

    /**
	 * Returns true if this.equal(that) is true or if the two are of identical (NULL value). Do NOT use this to save
	 * yourself the work of typing x.equal().isTrue() or !x.equal().isFalse(), because these are not equivalent!
	 * 
	 * This here is used mostly for anything builtin, java, Hibernate, etc.
	 * 
	 */
    public boolean equals(Object that) {
        return (that instanceof ANY) && this.equal((ANY) that).isTrue();
    }

    private SetOperator _operator = null;

    /** Get the operator for QSET term components. This is only relevant during parsing. */
    public SetOperator getOperator() {
        return _operator;
    }

    /** Set the operator for QSET term components. This is only relevant during parsing. */
    public void setOperator(SetOperator operator) {
        _operator = operator;
    }

    /**
     * An eclipse code generator is used to add an Externalizable 
     * implementation to the org.hl7 types.  Normally this implementation is
     * not checked into SVN.  However, the code generated for some of this
     * class's sub-classes did not work and had to be replaced with a 
     * hand-coded implementation that called super().  This method will be
     * replaced by the code generator due to the @generated tag below.
     * 
     * @throws UnsupportedOperationException - we want to fail fast if the
     * code generator has not been run on the classes in this package or
     * if this method is called in error. 
     *
     * @author jmoore
     * 
     * @generated
     */
    public void readExternal(ObjectInput oi) throws IOException {
        throw new UnsupportedOperationException("The Implement Externalizable Eclipse plugin has not been applied to this class.");
    }

    /**
     * An eclipse code generator is used to add an Externalizable 
     * implementation to the org.hl7 types.  Normally this implementation is
     * not checked into SVN.  However, the code generated for some of this
     * class's sub-classes did not work and had to be replaced with a 
     * hand-coded implementation that called super().  This method will be
     * replaced by the code generator due to the @generated tag below.
     * 
     * @throws UnsupportedOperationException - we want to fail fast if the
     * code generator has not been run on the classes in this package or
     * if this method is called in error. 
     *
     * @author jmoore
     * 
     * @generated
     */
    public void writeExternal(ObjectOutput oo) throws IOException {
        throw new UnsupportedOperationException("The Implement Externalizable Eclipse plugin has not been applied to this class.");
    }
}
