package jaxlib.col.filtered;

import java.io.Serializable;
import jaxlib.closure.Filter;
import jaxlib.col.XCollection;
import jaxlib.col.XSet;
import jaxlib.jaxlib_private.CheckArg;

/**
 * A set which delegates to another, allowing only such elements to be added which are accepted by 
 * a filter.
 *
 * @see #getFilter()
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: FilteredXSet.java 1215 2004-08-02 20:10:09Z joerg_wassmer $
 */
public class FilteredXSet<E> extends FilteredXCollection<E> implements XSet<E>, Serializable {

    /**
   * @since 1.0
   */
    private static final long serialVersionUID = 1L;

    /**
   * @serial
   * @since JaXLib 1.0
   */
    private XSet<E> delegate;

    /**
   * Creates a new <tt>FilteredXSet</tt> using the specified delegate and filter.
   * <p>
   * This constructor does not validate elements already contained in the specified set.
   * </p>
   *
   * @param delegate the set to delegate to.
   * @param filter   the filter used to decide whether to allow adding an object to the set.
   *
   * @throws NullPointerException if <code>(delegate == null) || (filter == null)</code>.
   *
   * @since JaXLib 1.0
   */
    public FilteredXSet(XSet<E> delegate, Filter<? super E> filter) {
        super(delegate, filter);
        this.delegate = delegate;
    }

    /**
   * Sets the set to delegate to.
   *
   * @throws IllegalArgumentException if <code>delegate == this</code>.
   * @throws NullPointerException     if <code>delegate == this</code>.
   * @throws ClassCastException       if delegate is not instance of {@link XSet}.
   *
   * @since JaLib 1.0
   */
    @Override
    protected void setDelegate(XCollection<E> delegate) {
        if (delegate == null) throw new NullPointerException("delegate");
        if (delegate == this) throw new IllegalArgumentException("can not delegate to my self");
        this.delegate = (XSet<E>) delegate;
        super.setDelegate(delegate);
    }

    @Override
    public int addCount(E e, int count) {
        CheckArg.count(count);
        return (count == 0) ? 0 : add(e) ? 1 : 0;
    }

    @Override
    public boolean addIfAbsent(E e) {
        return add(e);
    }
}
