package wotlas.libs.graphics2d;

/**
 * An DrawableIterator iterates over a chained list of Drawables. You can use the provided
 * methods to iterate over the drawables : the resetIterator() method resets the iterator.
 *
 * Concurrent accesses ARE NOT supported. Only one thread should use this iterator at the
 * same time.
 *
 * There are also three methods : resetIteratorToEnd(), hasPrev(), prev() to iterate over
 * the drawables backward... BUT the remove(), replace() and insert() methods only work as
 * they do when you iterate with the hasNext(), next() methods.
 *
 * @author aldiss
 */
public class DrawableIterator {

    private Element first;

    private Element current;

    private Element last;

    /** Inserts the new drawable just before the last drawable you retrieved with the next() call.
     *
     * @param drawable drawable to insert
     */
    public void insert(Drawable drawable) {
        Element toAdd = new Element();
        toAdd.drawable = drawable;
        if (this.first == null) {
            this.first = toAdd;
            this.last = this.first;
            return;
        }
        Element justAfterAdd = null;
        if (this.current == null || this.current.prev == null) justAfterAdd = this.last; else justAfterAdd = this.current.prev;
        if (justAfterAdd.prev != null) {
            justAfterAdd.prev.next = toAdd;
            toAdd.prev = justAfterAdd.prev;
            toAdd.next = justAfterAdd;
            justAfterAdd.prev = toAdd;
        } else {
            this.first = toAdd;
            toAdd.next = justAfterAdd;
            justAfterAdd.prev = toAdd;
        }
    }

    /** Adds the given drawable to the end of our list. The Iterator is not updated.
     *
     * @param drawable drawable to add
     */
    public void add(Drawable drawable) {
        Element toAdd = new Element();
        toAdd.drawable = drawable;
        if (this.last == null) {
            this.first = toAdd;
            this.last = this.first;
        } else {
            this.last.next = toAdd;
            toAdd.prev = this.last;
            this.last = toAdd;
        }
    }

    /** Replace the last drawable you retrieved with the next() call by the specified drawable.
     *  
     * @param drawable drawable to set
     */
    public void replace(Drawable drawable) {
        if (this.current == null) {
            if (this.last != null) this.last.drawable = drawable;
        } else if (this.current.prev != null) this.current.prev.drawable = drawable;
    }

    /** We remove the drawable returned by the last next() call.
     */
    public void remove() {
        Element toRemove = null;
        if (this.current == null) {
            if (this.last != null) toRemove = this.last; else return;
        } else toRemove = this.current.prev;
        if (toRemove.prev != null) toRemove.prev.next = toRemove.next; else this.first = toRemove.next;
        if (toRemove.next != null) toRemove.next.prev = toRemove.prev; else this.last = toRemove.prev;
        toRemove.prev = null;
        toRemove.next = null;
        toRemove.drawable = null;
    }

    /** We remove all the drawables.
     */
    public void clear() {
        this.current = this.first;
        while (this.current != null) {
            this.current.drawable = null;
            this.current.prev = null;
            this.current = this.current.next;
        }
        if (this.last != null) this.last.next = null;
        this.first = null;
        this.last = null;
    }

    /** Resets this iterator ( use hasNext() & next() methods ).
     */
    public void resetIterator() {
        this.current = this.first;
    }

    /** Resets this iterator to the end of the drawable list ( use hasPrev() & prev()
     *  methods ). 
     */
    public void resetIteratorToEnd() {
        this.current = this.last;
    }

    /** Returns true if there is a next element.
     *  @return true if there is a next element.
     */
    public boolean hasNext() {
        return this.current != null;
    }

    /** Returns true if there is a previous element.
     *  @return true if there is a previous element.
     */
    public boolean hasPrev() {
        return this.current != null;
    }

    /** Returns the next drawable.
     *  @return next drawable
     */
    public Drawable next() {
        if (this.current == null) return null;
        Drawable drawable = this.current.drawable;
        this.current = this.current.next;
        return drawable;
    }

    /** Returns the previous drawable.
     *  @return previous drawable
     */
    public Drawable prev() {
        if (this.current == null) return null;
        Drawable drawable = this.current.drawable;
        this.current = this.current.prev;
        return drawable;
    }

    /** Element class
     */
    static class Element {

        public Drawable drawable;

        public Element prev;

        public Element next;
    }
}
