package jaxlib.unit.transformer;

import java.io.InvalidObjectException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import jaxlib.unit.Dimension;
import jaxlib.unit.Quantifiable;
import jaxlib.unit.One;
import jaxlib.unit.Unit;
import jaxlib.unit.Unity;
import jaxlib.unit.UnitTransformer;
import jaxlib.unit.Value;
import jaxlib.util.CheckArg;

/**
 * <p>
 * <h3>Example</h3><br/>
 * <pre>
 *  UnitTransformer t = ItemUnitTransformer.create(bottle, Volume.dimension);
 *  Unit&lt;Volume>  volumeUnit = t.transform(Volume.L);
 *  Value&lt;Bottle> oldBottles = new DecimalValue&lt;Bottle>(10, bottle.getUnit());
 *  Value&lt;Volume> oldVolume  = oldBottles.to(volumeUnit);
 *  Value&lt;Volume> newVolume  = oldVolume.mul(10);
 *  Value&lt;Bottle> newBottles = newVolume.to(bottle.getUnit());
 * </pre>
 * </p>
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: ItemUnitTransformer.java 3002 2011-10-18 00:11:08Z joerg_wassmer $
 */
@SuppressWarnings("unchecked")
public final class ItemUnitTransformer<I extends Quantifiable<I>, Q> extends Object implements UnitTransformer {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    public static UnitTransformer create(final Quantifiable<?> itemQuantity, final Dimension<?> dimension) {
        final UnitTransformer t = tryCreate(itemQuantity, dimension);
        if (t == null) throw new ArithmeticException(itemQuantity + " provides no value of dimension " + dimension);
        return t;
    }

    public static UnitTransformer tryCreate(final Quantifiable<?> itemQuantity, final Dimension<?> dimension) {
        CheckArg.notNull(itemQuantity, "itemQuantity");
        CheckArg.notNull(dimension, "dimension");
        if (itemQuantity.getUnit().dimension.equals(dimension)) return UnitTransformer.IDENTITY;
        final Value one = itemQuantity.getQuantity(dimension).getValue();
        return (one == null) ? null : new ItemUnitTransformer(itemQuantity, one);
    }

    /**
   * @serial
   * @since JaXLib 1.0
   */
    private final boolean inverse;

    private transient ItemUnitTransformer inv;

    private transient I itemQuantity;

    private transient Value<Q> one;

    private ItemUnitTransformer(final I itemQuantity, final Value<Q> one) {
        super();
        this.inverse = false;
        this.itemQuantity = itemQuantity;
        this.one = one;
    }

    private ItemUnitTransformer(final ItemUnitTransformer<I, Q> inv) {
        super();
        this.inv = inv;
        this.inverse = true;
        this.itemQuantity = inv.itemQuantity;
        this.one = inv.one.inv();
    }

    /**
   * @serialData
   * @since JaXLib 1.0
   */
    private void readObject(final ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        if (this.inverse) this.inv = (ItemUnitTransformer) in.readObject(); else {
            this.itemQuantity = (I) in.readObject();
            this.one = (Value) in.readObject();
            final Value one = this.itemQuantity.getQuantity(this.one.unit.dimension).getValue();
            if ((one == null) || !this.one.to(one.unit).equals(one)) {
                throw new InvalidObjectException("ItemQuantity.getValue(Dimension) differs from serialized value:" + "\n  ItemQuantity class = " + this.itemQuantity.getClass() + "\n  serialized value   = " + this.one + "\n  actual value       = " + one);
            }
        }
    }

    /**
   * @serialData
   * @since JaXLib 1.0
   */
    private Object readResolve() {
        return this.inverse ? this.inv.inv() : this;
    }

    /**
   * @serialData
   * @since JaXLib 1.0
   */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (this.inverse) out.writeObject(this.inv); else {
            out.writeObject(this.itemQuantity);
            out.writeObject(this.one);
        }
    }

    @Override
    public final boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ItemUnitTransformer)) return false;
        final ItemUnitTransformer b = (ItemUnitTransformer) o;
        return (this.inverse == b.inverse) && this.itemQuantity.equals(b.itemQuantity) && this.one.equals(b.one);
    }

    @Override
    public final int hashCode() {
        return ((this.itemQuantity.hashCode() * 31) + this.one.hashCode()) * (this.inverse ? -1 : 1);
    }

    @Override
    public final UnitTransformer concat(final UnitTransformer b) {
        return ConcatenatedUnitTransformer.create(this, b);
    }

    @Override
    public final Class<? extends UnitTransformer> getDeclaringClass() {
        return ItemUnitTransformer.class;
    }

    public final Quantifiable<I> getItemQuantity() {
        return this.itemQuantity;
    }

    public final Value<Q> getOne() {
        return this.inverse ? this.inv.one : this.one;
    }

    @Override
    public final ItemUnitTransformer<I, Q> inv() {
        if (this.inv == null) this.inv = new ItemUnitTransformer<I, Q>(this);
        return this.inv;
    }

    public final boolean isInverse() {
        return this.inverse;
    }

    @Override
    public final boolean isLinear() {
        return true;
    }

    @Override
    public final String toAscii() {
        return transform(One.ONE).toAscii();
    }

    @Override
    public final String toAscii(final Unit<?> u) {
        return transform(u).toAscii();
    }

    @Override
    public final String toCommonString() {
        return transform(One.ONE).getCommonSymbol();
    }

    @Override
    public final String toCommonString(final Unit<?> u) {
        return transform(u).getCommonSymbol();
    }

    @Override
    public final String toSymbol(final Unit<?> u) {
        return transform(u).getSymbol();
    }

    @Override
    public final BigDecimal transform(final BigDecimal v) {
        return v.multiply(this.one.decimal());
    }

    @Override
    public final BigDecimal transform(final BigDecimal v, final MathContext mc) {
        return v.multiply(this.one.decimal());
    }

    @Override
    public final double transform(final double v) {
        return this.one.mul(v).doubleValue();
    }

    @Override
    public final long transform(final long v) {
        return this.one.mul(v).longValue();
    }

    @Override
    public final Unit transform(final Unity<?> unity) {
        final Unit unit = unity.getUnit();
        if (this.inverse) {
            unit.co(this.inv.one.unit);
            return unit.mul(this.one).mul(this.itemQuantity);
        } else {
            final Unit itemUnit = this.itemQuantity.getUnit();
            unit.co(itemUnit);
            return unit.mul(itemUnit.inv()).mul(this.one);
        }
    }
}
