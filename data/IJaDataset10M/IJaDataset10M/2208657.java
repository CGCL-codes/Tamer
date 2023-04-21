package org.hamcrestcollections;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of the Python {@code map()} function. Applies a {@link Function} to a collection of items,
 * returning the results.
 * </p>
 * Example:
 * </p>
 * <pre>
 * import static org.hamcrestcollections.Functions.asString
 * import static org.hamcrestcollections.Function.map
 * ...
 * List<Employee> employees = ...
 * List<String> stringFormOfEmployees = map(employees, asString);
 * </pre>
 *
 * @author Sam Newman (sam-newman@magpiebrain.com)
 * @see Function
 * @see Functions
 */
public class FunctionMapper {

    /**
     * Applies a {@link Function} on each item in the list, collecting the results and returning them.
     * <p/>
     * For an example use, see the class documentation
     *
     * @param items The items to run the function on
     * @param function The function to apply
     * @return The result of running the function on each item in the list
     */
    public static <T, U> Iterable<U> map(Iterable<T> items, Function<T, U> function) {
        List<U> results = new ArrayList<U>();
        for (T item : items) {
            results.add(function.apply(item));
        }
        return results;
    }
}
