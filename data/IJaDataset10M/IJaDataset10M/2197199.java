package abbot.swt.matcher;

import java.util.Collection;

/**
 * A {@link Matcher} implementation that composes other {@link Matcher}'s. That is,
 * {@link #matches(Object)} returns <code>true</code> if (and only if) all of the comprising
 * {@link Matcher}s' {@link Matcher#matches(Object)} methods return true.
 * <p>
 * <strong>Note:</strong> Class name <code>CompositeMatcher</code> notwithstanding, this class does
 * not have anything to do with {@link org.eclipse.swt.widgets.Composite}.
 * <p>
 * <strong>Suggestion:</strong> Use {@link Matchers} to conveniently create {@link CompositeMatcher}
 * s.
 * <p>
 * TODO Exploit performance considerations noted by Richard Birenheide with some [contextual
 * clarifications] by me (Gary Johnston): <blockquote>Tests on a first glance show (on my machine)
 * that combining Class type check and name [<code>Widget.getData()</code>] check is considerably
 * faster if combined in the way shown below [i.e., doing them both in a single
 * <code>Display.syncExec()</code> call]. For performance it is necessary to have control that the
 * least cost check is done first, the class check seems to be extremely cheap compared to the name
 * check. This is not necessarily obvious with CompositeMatcher.</blockquote>
 */
public class CompositeMatcher<T> implements Matcher<T> {

    /** Our {@link Matcher}s. */
    protected final Matcher<T>[] matchers;

    /**
	 * Constructs a new {@link CompositeMatcher} from zero or more {@link Matcher}s.
	 * 
	 * @param matchers
	 *            the {@link Matcher}s
	 */
    public CompositeMatcher(Matcher<T>... matchers) {
        this.matchers = matchers;
    }

    /**
	 * Constructs a new {@link CompositeMatcher} from a {@link Collection} of {@link Matcher}.
	 * 
	 * @param matchers
	 *            the {@link Matcher}s
	 */
    @SuppressWarnings("unchecked")
    public CompositeMatcher(Collection<Matcher<T>> matchers) {
        this.matchers = (Matcher<T>[]) matchers.toArray(new Object[matchers.size()]);
    }

    /**
	 * @return <code>true</code> if <code>node</code> is matched by all of the receiver's
	 *         {@link Matcher}s (or if the receiver has <i>no</i> {@link Matcher}s),
	 *         <code>false</code> otherwise.
	 * @see Matcher#matches(Object)
	 */
    public boolean matches(T node) {
        for (int i = 0; i < matchers.length; i++) {
            if (!matchers[i].matches(node)) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getName() + "(" + matchers + ")";
    }
}
