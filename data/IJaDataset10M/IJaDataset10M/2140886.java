package util;

/**
  * A comparator that compares Objects using their natural order.
  *
  * <p>This is a convenience facade for the {@link java.lang.Comparable} interface.</p>
  *
  * @author Steffen Zschaler
  * @version 2.0 27/07/1999
  * @since v2.0
  */
public final class NaturalComparator extends Object implements SerializableComparator {

    /**
    * Compare the two objects assuming they are both {@link java.lang.Comparable comparable}.
    *
    * @override Never
    */
    public final int compare(Object o1, Object o2) {
        return ((Comparable) o1).compareTo(o2);
    }
}
