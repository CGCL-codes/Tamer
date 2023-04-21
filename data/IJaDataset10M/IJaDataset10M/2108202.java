package jaxlib.unit.transformer;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import jaxlib.unit.TransformedUnit;
import jaxlib.unit.Unit;
import jaxlib.unit.UnitTransformer;
import jaxlib.unit.Unity;

/**
 * The {@code UnitTransformer} to be used by implementations of the
 * {@link UnitTransformer#concat(UnitTransformer)} method.
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: ConcatenatedUnitTransformer.java 3040 2012-01-23 12:45:40Z joerg_wassmer $
 */
@SuppressWarnings("unchecked")
public final class ConcatenatedUnitTransformer extends Object implements UnitTransformer {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    private static final int LINEAR = 0;

    private static final int LEFT_NOT_LINEAR = 1;

    private static final int RIGHT_NOT_LINEAR = 2;

    private static final int BOTH_NOT_LINEAR = 3;

    /**
   * Concat the specified transformers.
   * <p>
   * <h3>Note</h3><br/>
   *  This method should be called only by implementations of
   *  {@link UnitTransformer#concat(UnitTransformer) UnitTransformer.concat(UnitTransformer)}.
   * </p>
   *
   * @param left
   *  the converter to apply first.
   * @param right
   *  the converter to apply second.
   *
   * @return
   *  One of the arguments if at least one is the {@link UnitTransformer#IDENTITY identity transformer},
   *  otherwise a new instance of {@code ConcatenatedUnitTransformer}.
   *
   * @throws NullPointerException
   *  for any null argument.
   *
   * @since JaXLib 1.0
   */
    public static UnitTransformer create(UnitTransformer left, UnitTransformer right) {
        if (left == null) throw new NullPointerException("left");
        if (right == null) throw new NullPointerException("right");
        if (left == UnitTransformer.IDENTITY) return right;
        if (right == UnitTransformer.IDENTITY) return left;
        if ((left == right) && (left instanceof ConcatenatedUnitTransformer)) {
            final ConcatenatedUnitTransformer cleft = (ConcatenatedUnitTransformer) left;
            left = new ConcatenatedUnitTransformer(cleft.left, cleft.right);
        }
        return new ConcatenatedUnitTransformer(left, right);
    }

    /**
   * The converter applied first.
   *
   * @serial
   * @since JaXLib 1.0
   */
    public final UnitTransformer left;

    /**
   * The converter applied second.
   *
   * @serial
   * @since JaXLib 1.0
   */
    public final UnitTransformer right;

    /**
   * @serial
   * @since JaXLib 1.0
   */
    private final int linear;

    /**
   * Also serialized to avoid unnecessary creation of dupe objects at deserializing side.
   *
   * @serial
   * @since JaXLib 1.0
   */
    private UnitTransformer inverse;

    private ConcatenatedUnitTransformer(final UnitTransformer left, final UnitTransformer right) {
        super();
        this.left = left;
        this.right = right;
        this.linear = left.isLinear() ? (right.isLinear() ? LINEAR : RIGHT_NOT_LINEAR) : (right.isLinear() ? LEFT_NOT_LINEAR : BOTH_NOT_LINEAR);
    }

    /**
   * @serialData
   * @since JaXLib 1.0
   */
    private void readObject(final ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        if ((this.linear == LINEAR) != (this.left.isLinear() && this.right.isLinear())) {
            throw new InvalidObjectException("Serialized transformer was" + ((this.linear == 0) ? "" : " not") + " linear, actual object is" + ((this.linear == 0) ? " not" : "") + ":\n" + this);
        }
    }

    @Override
    public final boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ConcatenatedUnitTransformer)) return false;
        final ConcatenatedUnitTransformer b = (ConcatenatedUnitTransformer) o;
        return (this.linear == b.linear) && this.left.equals(b.left) && this.right.equals(b.right);
    }

    @Override
    public final int hashCode() {
        return (this.left.hashCode() * 31) + this.right.hashCode();
    }

    @Override
    public final UnitTransformer concat(final UnitTransformer b) {
        return ConcatenatedUnitTransformer.create(this, b);
    }

    @Override
    public final Class<? extends UnitTransformer> getDeclaringClass() {
        return ConcatenatedUnitTransformer.class;
    }

    @Override
    public final UnitTransformer inv() {
        if (this.inverse == null) {
            final UnitTransformer inverse = ConcatenatedUnitTransformer.create(this.right.inv(), this.left.inv());
            if (inverse instanceof ConcatenatedUnitTransformer) ((ConcatenatedUnitTransformer) inverse).inverse = this;
            this.inverse = inverse;
        }
        return this.inverse;
    }

    @Override
    public final boolean isLinear() {
        return this.linear == 0;
    }

    @Override
    public final String toAscii() {
        return (this.linear <= LEFT_NOT_LINEAR) ? (this.left.toAscii() + this.right.toAscii()) : ("(" + this.left.toAscii() + this.right.toAscii() + ")");
    }

    @Override
    public final String toAscii(final Unit<?> u) {
        return (this.linear <= LEFT_NOT_LINEAR) ? (this.left.toAscii(u) + this.right.toAscii()) : ("(" + this.left.toAscii(u) + this.right.toAscii() + ")");
    }

    @Override
    public final String toCommonString() {
        return (this.linear <= LEFT_NOT_LINEAR) ? (this.left.toCommonString() + this.right.toCommonString()) : ("(" + this.left.toCommonString() + this.right.toCommonString() + ")");
    }

    @Override
    public final String toCommonString(final Unit<?> u) {
        return (this.linear <= LEFT_NOT_LINEAR) ? (this.left.toCommonString(u) + this.right.toCommonString()) : ("(" + this.left.toCommonString(u) + this.right.toCommonString() + ")");
    }

    @Override
    public final String toSymbol(final Unit<?> u) {
        return (this.linear <= LEFT_NOT_LINEAR) ? (this.left.toSymbol(u) + this.right.toString()) : ("(" + this.left.toSymbol(u) + this.right.toString() + ")");
    }

    @Override
    public final String toString() {
        return (this.linear <= LEFT_NOT_LINEAR) ? (this.left.toString() + this.right.toString()) : ("(" + this.left.toString() + this.right.toString() + ")");
    }

    @Override
    public final BigDecimal transform(final BigDecimal v) {
        return this.right.transform(this.left.transform(v));
    }

    @Override
    public final BigDecimal transform(final BigDecimal v, final MathContext mc) {
        return this.right.transform(this.left.transform(v, mc), mc);
    }

    @Override
    public final double transform(final double v) {
        return this.right.transform(this.left.transform(v));
    }

    @Override
    public final long transform(final long v) {
        return this.right.transform(this.left.transform(v));
    }

    @Override
    public final Unit transform(Unity<?> sourceUnity) {
        Unit sourceUnit = sourceUnity.getUnit();
        if (!(sourceUnit instanceof TransformedUnit)) return new TransformedUnit(sourceUnit, this);
        TransformedUnit tu = (TransformedUnit) sourceUnit;
        UnitTransformer a = tu.getTransformer();
        final UnitTransformer b = a.concat(this);
        if (a == b) return tu;
        sourceUnit = tu.getSourceUnit();
        sourceUnity = null;
        tu = null;
        a = null;
        return b.transform(sourceUnit);
    }
}
