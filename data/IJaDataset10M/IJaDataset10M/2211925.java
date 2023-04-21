package de.waldheinz.dependencies;

/**
 * A <code>Modifier</code> is a functor which takes a {@link Range} from
 * it's <code>Domain</code> and transforms it into another <code>Range</code>
 * from the same <code>Domain</code>.
 * <p>
 * A <code>Modifier</code> <i>must</i> be a pure function (w.r.t what's visible
 * to the outside world by the means of this interface). This means that for
 * multiple invocations of a <code>Modifier</code> on some {@link Range}
 * <i>x</i> giving new <code>Range</code>s <i>{y_1 .. y_n}</i>,
 * <code>y_1.equals(y_i)</code> must be true for every <i>i</i>. If this rule
 * is violated, unexpected behavior has to be expected.
 * <p>
 * A <code>Modifier</code> should be a light-weight object, as instances may
 * be cached by the rest of the system.
 * 
 * @author Matthias Treydte <waldheinz at gmail.com>
 */
public interface Modifier {

    /**
     * Called if the <code>Range</code> resulting from this functor applied
     * to a <code>Range</code> is needed.
     * <p>
     * It is safe to assume that the <code>Range</code> passed to this method
     * is from the same {@link Domain} as this <code>Modifier</code>.
     * 
     * @param r The <code>Range</code> to apply this functor to.
     * @return the result of this functor applied to the given
     *      <code>Range</code>.
     */
    public Range apply(Range r);

    /**
     * Marks a <code>Modifier</code> as implementing an intersection.
     * A <code>Modifier</code> implementing this interface
     * is restricted in the type of operations it may perform on a given range
     * so that the semantics for an intersection of sets are met.
     * <p>
     * Working with modifier exhibiting this semantics allows some
     * optimizations, which can not be applied to the basic
     * <code>Modifier</code> interface. However, it is always safe to use the
     * plain <code>Modifier</code> interface if unsure.
     * <p>
     * TODO: more doc.
     */
    public interface Intersection extends Modifier {
    }

    /**
     * A marker interface.
     * 
     * @see Modifier.Intersection
     */
    public interface Union extends Modifier {
    }
}
