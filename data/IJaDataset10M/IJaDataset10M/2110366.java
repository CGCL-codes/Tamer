package info.monitorenter.gui.chart.events;

import info.monitorenter.gui.chart.ITrace2D;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

/**
 * <p>
 * <code>Action</code> that increases the <code>zIndex</code> of the
 * constructor-given <code>ITrace2D</code> by a constructor-given integer.
 * </p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 * @version $Revision: 1.4 $
 */
public final class Trace2DActionZindexIncrease extends ATrace2DAction {

    /**
   * Generated <code>serialVersionUID</code>.
   */
    private static final long serialVersionUID = 3978986583057707570L;

    /**
   * The increment to the trace's zIndex.
   */
    private int m_increase;

    /**
   * Create an <code>Action</code> that accesses the trace and identifies
   * itself with the given action String.
   * <p>
   * 
   * @param trace
   *          the target the action will work on.
   * 
   * @param description
   *          the descriptive <code>String</code> that will be displayed by
   *          {@link javax.swing.AbstractButton} subclasses that get this
   *          <code>Action</code> assigned (
   *          {@link javax.swing.AbstractButton#setAction(javax.swing.Action)}).
   * 
   * @param increase
   *          the increment to the trace's zIndex (see
   *          {@link ITrace2D#setZIndex(Integer)}).
   */
    public Trace2DActionZindexIncrease(final ITrace2D trace, final String description, final int increase) {
        super(trace, description);
        this.m_increase = increase;
    }

    /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
    public void actionPerformed(final ActionEvent e) {
        int value = this.m_trace.getZIndex().intValue();
        this.m_trace.setZIndex(new Integer(value + this.m_increase));
    }

    /**
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
    public void propertyChange(final PropertyChangeEvent evt) {
    }
}
