package net.sf.staccatocommons.missinginterfaces;

import java.io.Serializable;
import java.util.NoSuchElementException;
import net.sf.staccatocommons.defs.Applicable;
import net.sf.staccatocommons.defs.Evaluable;
import net.sf.staccatocommons.defs.Executable;
import net.sf.staccatocommons.defs.ProtoMonad;
import net.sf.staccatocommons.defs.Thunk;
import net.sf.staccatocommons.defs.partial.ContainsAware;
import net.sf.staccatocommons.defs.partial.SizeAware;
import net.sf.staccatocommons.restrictions.check.NonNull;

/**
 * @author flbulgarelli
 * 
 */
public interface Optional<A> extends Thunk<A>, Iterable<A>, SizeAware, ContainsAware<A>, Serializable, ProtoMonad<Optional, A> {

    /**
   * Gets the Optionalal value, if defined, or throws an
   * {@link NoSuchElementException}, otherwise.
   * 
   * @return The Optionalal value. This value is nullable, if client code
   *         considers null as possible, valid values. Non null otherwise.
   *         Please prefer the second approach, as normally, null values are
   *         there in code to represent Optionalal data, so nullable values in
   *         Optionalal values is in most scenarios completely redundant,
   *         unnecessary an error prone.
   * @throws NoSuchElementException
   *           if this Optional is undefined, and thus there is no value.
   */
    A value() throws NoSuchElementException;

    /**
   * Returns if the value has been defined or not.
   * 
   * @return true is the value is defined. False otherwise
   */
    boolean isDefined();

    /**
   * 
   * @return !{@link #isDefined()}
   */
    boolean isUndefined();

    /**
   * Applies the given <code>function</code> to the Optional's value and wraps
   * it into an Optional, if defined. Returns an undefined value, otherwise
   * 
   * @param <T2>
   * @param function
   * @return the mapped {@link Optional}
   */
    <T2> Optional<T2> map(Applicable<? super A, ? extends T2> function);

    /**
   * Answers this Optional if defined and the given <code>predicate</code>
   * evaluates to <code>true</code>. Answers an undefined value, otherwise
   * 
   * @param predicate
   * @return the filtered Optional
   */
    Optional<A> filter(Evaluable<? super A> predicate);

    /**
   * Returns the value of this {@link Optional}, or the provided object if
   * undefined
   * 
   * @param other
   *          the return value in case this {@link Optional} is undefined
   * @return <code>this.value()</code> if defined, other <code>otherwise</code>
   */
    A valueOrElse(A other);

    /**
   * Returns the value of this {@link Optional}, or the provided object if
   * undefined
   * 
   * @param other
   *          the thunk of the return value in case this {@link Optional} is
   *          undefined
   * @return <code>this.value()</code> if defined, other.value()
   *         <code>otherwise</code>
   */
    A valueOrElse(Thunk<? extends A> other);

    /**
   * Returns the value of this {@link Optional}, or <code>null</code>, if
   * undefined.
   * 
   * @return <code>this.value()</code> if defined, or <code>null</code>,
   *         otherwise
   */
    A valueOrNull();

    /**
   * Executed the given block if this Optional is defined
   * 
   * @param block
   */
    void ifDefined(@NonNull Executable<A> block);
}
