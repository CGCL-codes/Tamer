package gnu.trove.queue;

import gnu.trove.TShortCollection;
import java.io.Serializable;

/**
 * Interface for Trove queue implementations.
 *
 * @see java.util.Queue
 */
public interface TShortQueue extends TShortCollection {

    /**
	 * Retrieves and removes the head of this queue. This method differs from
	 * {@link #poll} only in that it throws an exception if this queue is empty.
	 */
    public short element();

    /**
	 * Inserts the specified element into this queue if it is possible to do so
	 * immediately without violating capacity restrictions. When using a
	 * capacity-restricted queue, this method is generally preferable to
	 * {@link #add}, which can fail to insert an element only by throwing an exception.
	 *
	 * @param e		The element to add.
	 *
	 * @return	<tt>true</tt> if the element was added to this queue, else <tt>false</tt>
	 */
    public boolean offer(short e);

    /**
	 * Retrieves, but does not remove, the head of this queue, or returns
	 * {@link #getNoEntryValue} if this queue is empty.
	 *
	 * @return	the head of this queue, or {@link #getNoEntryValue} if this queue is empty 
	 */
    public short peek();

    /**
	 * Retrieves and removes the head of this queue, or returns {@link #getNoEntryValue}
	 * if this queue is empty.
	 *
	 * @return	the head of this queue, or {@link #getNoEntryValue} if this queue is empty
	 */
    public short poll();
}
