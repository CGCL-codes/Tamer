package gumbo.core.struct;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A set whose backing store is not created until it is actually needed. Until
 * then, this set acts as an empty set. The default backing store is HashSet. In
 * the spirit of laziness, also provides a convenient array constructor and a
 * singleton unmodifiable view.
 * 
 * @author Jon Barrilleaux (jonb@jmbaai.com) of JMB and Associates Inc.
 * @version $Revision: 1.1 $
 */
public class LazySet<E> extends SetWrapper.Impl<E> {

    /**
	 * Creates an instance, that is empty.
	 */
    public LazySet() {
        super(Collections.<E>emptySet());
    }

    /**
	 * Creates an instance, initialized with the specified contents.
	 * @param init The value of the initial contents.  Never null.
	 */
    public LazySet(E... init) {
        super(Collections.<E>emptySet());
        addAll(Arrays.asList(init));
    }

    /**
	 * Creates an instance, initialized with the specified contents.
	 * @param init The value of the initial contents (Object).  Never null.
	 */
    public LazySet(Collection<? extends E> init) {
        super(Collections.<E>emptySet());
        addAll(init);
    }

    /**
	 * Called by the system when it needs to lazily build the
	 * backing store; implemented by subclasses.  Only called once.
	 * <P>
	 * Default impl: Returns a new empty HashSet. 
	 * @return A new empty store.  Never null.
	 * */
    protected Set<E> newStore() {
        return new HashSet<E>();
    }

    public boolean add(E member) {
        buildStore();
        return super.add(member);
    }

    public boolean addAll(Collection<? extends E> members) {
        if (members.isEmpty()) return false;
        buildStore();
        return super.addAll(members);
    }

    /** Target store.  Lazy build. */
    private Set<E> _store = null;

    /**
	 * If not yet built, builds the backing store using newStore().
	 * @exception IllegalStateException New group store is null or not empty.
	 * */
    private void buildStore() {
        if (_store == null) {
            Set<E> store = newStore();
            if (store == null) throw new IllegalStateException("newStore() returned null.");
            if (!store.isEmpty()) throw new IllegalStateException("newStore() must return an empty store.");
            _store = store;
            setWrapperTarget(_store);
        }
    }

    private static final long serialVersionUID = 1;
}
