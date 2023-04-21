package jaxlib.col;

import java.util.Comparator;

/**
 * A collection which stores elements in sorted order.
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: SortedCollection.java 2705 2009-01-29 13:02:32Z joerg_wassmer $
 */
public interface SortedCollection<E> extends OrderedCollection<E> {

    /**
   * Returns the comparator which defines the order how elements are stored in this collection,
   * or <tt>null</tt> to indicate that this collection sorts elements by their natural order induced by their
   * {@link Comparable} interface implementation.
   *
   * @since JaXLib 1.0
   */
    public Comparator<? super E> comparator();
}
