package org.unitils.easymock.util;

import org.easymock.internal.matchers.Equals;
import org.unitils.reflectionassert.ReflectionComparator;
import org.unitils.reflectionassert.ReflectionComparatorFactory;
import org.unitils.reflectionassert.ReflectionComparatorMode;

/**
 * An easy mock argument matcher to check whether 2 objects are equal by comparing all fields of the objects using
 * reflection.
 * <p/>
 * The (combination of) comparator modes specify how strict the comparison must be:<ul>
 * <li>ignore defaults: compare only arguments (and inner values) that have a non default value (eg null) as exepected value</li>
 * <li>lenient dates: do not compare actual date values, just that they both have a value or not</li>
 * <li>lenient order: order is not important when comparing collections or arrays</li>
 * </ul>
 *
 * @author Tim Ducheyne
 * @author Filip Neven
 * @see ReflectionComparator
 * @see ReflectionComparatorMode
 */
public class ReflectionArgumentMatcher<T> extends Equals {

    private ReflectionComparator reflectionComparator;

    /**
     * Creates a matcher for the expected argument value.
     * The modes specify how to compare the expected value with the actual value.
     *
     * @param expected the argument value, not null
     * @param modes    the comparator modes
     */
    public ReflectionArgumentMatcher(T expected, ReflectionComparatorMode... modes) {
        super(expected);
        this.reflectionComparator = ReflectionComparatorFactory.createRefectionComparator(modes);
    }

    /**
     * Checks whether the given actual value is equal to the expected value.
     * A reflection comparator is used to compare both values. The modes determine how
     * strict this comparison will be.
     * <p/>
     * An assertion error is thrown if expected and actual did not match. This way, a more
     * detailed message can be provided.
     *
     * @param actual the actual argument value
     * @return true
     * @throws AssertionError in case expected and actual did not match
     */
    @Override
    public boolean matches(Object actual) {
        return reflectionComparator.isEqual(getExpected(), actual);
    }
}
