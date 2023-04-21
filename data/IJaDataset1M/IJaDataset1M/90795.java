package ti.swing.console;

/**
 * An {@link InputHandler} adapter, provides default methods that just fwd the
 * request to the parent.  A subclass can just override the methods it cares
 * about.
 * 
 * @author Rob Clark
 * @version 0.0
 */
public abstract class InputAdapter implements InputHandler {

    private InputHandler parent;

    /**
   * Class Constructor.
   * 
   * @param parent       the parent of this input adapter
   */
    public InputAdapter(InputHandler parent) {
        this.parent = parent;
    }

    /**
   * Append characters to the end of the character stream.
   * 
   * @param cbuf         the character buffer
   * @param off          the offset into cbuf to first character to append
   * @param len          the number of characters to append
   */
    public void append(char[] cbuf, int off, int len) {
        parent.append(cbuf, off, len);
    }

    /**
   * Delete characters from end of character stream.
   * 
   * @param num          the number of characters to delete
   */
    public void zap(int num) {
        parent.zap(num);
    }

    /**
   * Get the current offset of the last character in the character stream.
   * 
   * @return an offset
   */
    public int getOffset() {
        return parent.getOffset();
    }

    /**
   * Get the data within the specified region.  If the requested region has
   * scrolled past the top of the buffer, the returned data may be truncated.
   * 
   * @param offset       the begining of the range
   * @param len          the length of the range in characters
   * @return the data
   */
    public char[] getData(int offset, int len) {
        return parent.getData(offset, len);
    }

    /**
   * Add a region mapped over a section of character stream.  If the section
   * of the character stream over which the region is mapped has scrolled off
   * the top of the fixed size row buffer, the region will be automatically
   * removed.
   * 
   * @param r            region to add
   * @see #removeRegion
   */
    public void addRegion(Region r) {
        parent.addRegion(r);
    }

    /**
   * Remove a region.
   * 
   * @param r            region to remove
   * @see #addRegion
   */
    public void removeRegion(Region r) {
        parent.removeRegion(r);
    }

    /**
   * Get an iterator of the regions containing the specified range.  Access
   * to the iterator must be synchronized on the buffer-lock, to* prevent 
   * concurrent modification problems.  For example:
   * <pre>
   *   synchronized( ih.getBufferLock() )
   *   {
   *     for( Iterator itr=ih.getRegions( off, len ); itr.hasNext(); )
   *     {
   *       ...
   *     }
   *   }
   * </pre>
   * 
   * @param offset       the begining of the range
   * @param len          the length of the range in characters
   * @return an iterator of {@link Region}
   * @see #getBufferLock
   */
    public java.util.Iterator getRegions(int offset, int len) {
        return parent.getRegions(offset, len);
    }

    /**
   * Get an object on which to synchronize access to a region iterator.
   * 
   * @return an object suitable for synchronizing region iterator access
   * @see #getRegions
   */
    public Object getBufferLock() {
        return parent.getBufferLock();
    }

    /**
   * Lock the console from repaints.  This can be used to batch multiple
   * updates ({@link #append}, {@link #zap}, {@link #addRegion}, 
   * {@link #removeRegion}) and only trigger a single repaint at the end.
   * 
   * @see #unlock
   */
    public void lock() {
        parent.lock();
    }

    /**
   * Unlock the console, rerendering if needed.
   * 
   * @see #lock
   */
    public void unlock() {
        parent.unlock();
    }

    /**
   * Close method for doing any cleanup.
   *
   */
    public void close() {
        parent.close();
    }
}
