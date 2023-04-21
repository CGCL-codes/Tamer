package jaxlib.tcol.tint;

import java.io.Serializable;
import jaxlib.closure.TClosure;
import jaxlib.closure.TFilter;
import jaxlib.closure.IntCondition;

/**
 * TODO: comment
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: IntCollectionTAlgo.java 1044 2004-04-06 16:37:29Z joerg_wassmer $
 */
final class IntCollectionTAlgo extends IntCondition implements TFilter, TClosure, Serializable {

    private static final long serialVersionUID = 1L;

    static final int CONTAINS = 1;

    static final int REMOVE = 2;

    static final int REMOVE_EACH = 3;

    private final IntCollection delegate;

    private final int id;

    IntCollectionTAlgo(int id, IntCollection delegate) {
        super();
        if (delegate == null) throw new NullPointerException("delegate");
        this.delegate = delegate;
        this.id = id;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof IntCollectionTAlgo)) return false;
        IntCollectionTAlgo b = (IntCollectionTAlgo) o;
        return (this.id == b.id) && (this.delegate == b.delegate);
    }

    public int hashCode() {
        return System.identityHashCode(this.delegate) ^ this.id;
    }

    public boolean accept(int e) {
        switch(this.id) {
            case CONTAINS:
                return this.delegate.contains(e);
            case REMOVE:
                return this.delegate.remove(e);
            case REMOVE_EACH:
                return this.delegate.removeEach(e) > 0;
            default:
                throw new AssertionError(this.id);
        }
    }

    public boolean proceed(int e) {
        switch(this.id) {
            case CONTAINS:
                this.delegate.contains(e);
                return true;
            case REMOVE:
                this.delegate.remove(e);
                return true;
            case REMOVE_EACH:
                this.delegate.removeEach(e);
                return true;
            default:
                throw new AssertionError(this.id);
        }
    }

    public boolean accept(double e) {
        int f = (int) e;
        return (f == e) ? accept(f) : false;
    }

    public boolean accept(float e) {
        int f = (int) e;
        return (f == e) ? accept(f) : false;
    }

    public boolean accept(long e) {
        return accept((int) e);
    }

    public boolean proceed(boolean e) {
        return proceed(e ? 1L : 0L);
    }

    public boolean proceed(byte e) {
        return proceed((int) e);
    }

    public boolean proceed(char e) {
        return proceed((int) e);
    }

    public boolean proceed(double e) {
        int f = (int) e;
        return (f == e) ? proceed(f) : true;
    }

    public boolean proceed(float e) {
        int f = (int) e;
        return (f == e) ? proceed(f) : true;
    }

    public boolean proceed(long e) {
        return proceed((int) e);
    }

    public boolean proceed(short e) {
        return proceed((int) e);
    }
}
