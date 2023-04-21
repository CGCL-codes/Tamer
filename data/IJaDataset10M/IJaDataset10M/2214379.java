package xxl.core.predicates;

import java.util.List;

/**
 * This class provides a logical XOR predicate. The XOR predicate represents
 * the exclusive disjunction of two predicates to a new predicate that returns
 * <code>true</code> if and only if exactly one of the underlying predicates
 * returns <code>true</code>.
 *
 * @param <P> the type of the predicate's parameters.
 */
public class Xor<P> extends BinaryPredicate<P> {

    /**
	 * Creates a new XOR predicate that represents the exclusive disjunction of
	 * the specified predicates.
	 *
	 * @param predicate0 the first predicate of the exclusive disjunction.
	 * @param predicate1 the second predicate of the exclusive disjunction.
	 */
    public Xor(Predicate<? super P> predicate0, Predicate<? super P> predicate1) {
        super(predicate0, predicate1);
    }

    /**
	 * Returns <code>true</code> if and only if exactly one of the underlying
	 * predicates returns <code>true</code>, otherwise <code>false</code> is
	 * returned.
	 *
	 * @param arguments the arguments to the underlying predicates.
	 * @return <code>true</code> if and only if one of the underlying
	 *         predicates returns <code>true</code>, otherwise
	 *         <code>false</code>.
	 */
    @Override
    public boolean invoke(List<? extends P> arguments) {
        return predicate0.invoke(arguments) ^ predicate1.invoke(arguments);
    }

    /**
	 * Returns <code>true</code> if and only if exactly one of the underlying
	 * predicates returns <code>true</code>, otherwise <code>false</code> is
	 * returned.
	 *
	 * @return <code>true</code> if and only if one of the underlying
	 *         predicates returns <code>true</code>, otherwise
	 *         <code>false</code>.
	 */
    @Override
    public boolean invoke() {
        return predicate0.invoke() ^ predicate1.invoke();
    }

    /**
	 * Returns <code>true</code> if and only if exactly one of the underlying
	 * predicates returns <code>true</code>, otherwise <code>false</code> is
	 * returned.
	 *
	 * @param argument the argument to the underlying predicates.
	 * @return <code>true</code> if and only if one of the underlying
	 *         predicates returns <code>true</code>, otherwise
	 *         <code>false</code>.
	 */
    @Override
    public boolean invoke(P argument) {
        return predicate0.invoke(argument) ^ predicate1.invoke(argument);
    }

    /**
	 * Returns <code>true</code> if and only if exactly one of the underlying
	 * predicates returns <code>true</code>, otherwise <code>false</code> is
	 * returned.
	 *
	 * @param argument0 the first arguments to the underlying predicates.
	 * @param argument1 the second arguments to the underlying predicates.
	 * @return <code>true</code> if and only if one of the underlying
	 *         predicates returns <code>true</code>, otherwise
	 *         <code>false</code>.
	 */
    @Override
    public boolean invoke(P argument0, P argument1) {
        return predicate0.invoke(argument0, argument1) ^ predicate1.invoke(argument0, argument1);
    }
}
