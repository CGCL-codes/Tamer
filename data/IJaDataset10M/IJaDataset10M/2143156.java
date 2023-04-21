package jaxlib.tcol.tlong;

import jaxlib.closure.LongFilter;
import jaxlib.jaxlib_private.CheckArg;

/**
 * Abstract implementation of the <tt>jaxlib.tcol.tlong.LongSet</tt> interface.
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: AbstractLongSet.java 1044 2004-04-06 16:37:29Z joerg_wassmer $
 */
public abstract class AbstractLongSet extends AbstractLongCollection implements LongSet {

    protected AbstractLongSet() {
        super();
    }

    public abstract LongIterator iterator();

    public abstract int size();

    @Overrides
    public final int addCount(long e, int count) {
        CheckArg.count(count);
        return (count == 0) ? 0 : add(e) ? 1 : 0;
    }

    @Overrides
    public final boolean addIfAbsent(long e) {
        return add(e);
    }

    @Overrides
    final int addSelf() {
        return 0;
    }

    @Overrides
    public boolean contains(long e) {
        return super.countUp(e, 1) > 0;
    }

    @Overrides
    public final int count(long e) {
        return contains(e) ? 1 : 0;
    }

    @Overrides
    public final int countUp(long e, int maxCount) {
        CheckArg.maxCount(maxCount);
        return (maxCount == 0) ? 0 : (contains(e) ? 1 : 0);
    }

    @Overrides
    public boolean equals(Object o) {
        if (o == this) return true; else if (!(o instanceof jaxlib.tcol.tlong.LongSet)) return false; else {
            LongSet b = (LongSet) o;
            return (size() == b.size()) && containsAll(b);
        }
    }

    @Overrides
    public boolean isFastSearching() {
        return true;
    }

    @Overrides
    public int hashCode() {
        int remaining = size();
        if (remaining == 0) return 0;
        int h = 0;
        for (LongIterator it = iterator(); remaining-- > 0; ) h += it.next();
        return h;
    }

    @Overrides
    public boolean remove(long e) {
        return super.removeCount(e, 1) > 0;
    }

    @Overrides
    public final int removeEach(long e) {
        return remove(e) ? 1 : 0;
    }

    @Overrides
    public final int removeCount(long e, int maxCount) {
        CheckArg.maxCount(maxCount);
        return maxCount == 0 ? 0 : (remove(e) ? 1 : 0);
    }
}
