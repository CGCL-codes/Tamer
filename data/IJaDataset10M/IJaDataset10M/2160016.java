package java.awt.event;

/**
 * This class implements <code>HierarchyBoundsListener</code> and implements
 * all methods with empty bodies.  This allows a listener interested in
 * implementing only a subset of the <code>HierarchyBoundsListener</code>
 * interface to extend this class and override only the desired methods.
 *
 * @author Bryce McKinlay
 * @see HierarchyBoundsListener
 * @see HierarchyEvent
 * @since 1.3
 * @status updated to 1.4
 */
public abstract class HierarchyBoundsAdapter implements HierarchyBoundsListener {

    /**
   * Do nothing default constructor for subclasses.
   */
    public HierarchyBoundsAdapter() {
    }

    /**
   * Implements this method from the interface with an empty body.
   *
   * @param event the event, ignored in this implementation
   */
    public void ancestorMoved(HierarchyEvent event) {
    }

    /**
   * Implements this method from the interface with an empty body.
   *
   * @param event the event, ignored in this implementation
   */
    public void ancestorResized(HierarchyEvent event) {
    }
}
