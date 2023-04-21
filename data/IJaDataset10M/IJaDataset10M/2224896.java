package net.jadoth.lang.functional;

/**
 * @author Thomas M�nz
 *
 */
public interface Predicate<T> extends BasePredicate<T> {

    @Override
    public boolean apply(T e) throws RuntimeException;
}
