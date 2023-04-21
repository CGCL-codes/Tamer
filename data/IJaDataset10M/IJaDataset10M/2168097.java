package net.sf.javarisk.test;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;

/**
 * <p>
 * <code>CustomMatchers</code> provides access to some convenient {@link Matcher}s not provided by hamcrest.
 * </p>
 * 
 * @author <a href='mailto:sebastiankirsch@users.sourceforge.net'>Sebastian Kirsch</a>
 * @version 0.1; $Rev: 144 $
 * @since 0.1
 */
public class CustomMatchers {

    @Factory
    public static <T> Matcher<? super Collection<T>> containsInAnyOrder(Iterable<T> items) {
        List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>();
        for (T item : items) {
            matchers.add(equalTo(item));
        }
        return new IsIterableContainingInAnyOrder<T>(matchers);
    }

    @Factory
    public static <E> Matcher<? super Collection<? extends E>> hasSameSize(Collection<?> collection) {
        return hasSize(collection.size());
    }

    @Factory
    public static <T> Matcher<? super Collection<T>> sameCollection(Collection<T> items) {
        return both(containsInAnyOrder(items)).and(hasSameSize(items));
    }
}
