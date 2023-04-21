package org.jsresources.apps.jmvp.view.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.jsresources.apps.jmvp.MVPDebug;
import org.jsresources.apps.jmvp.model.ModelEvent;
import org.jsresources.apps.jmvp.model.ModelListener;
import org.jsresources.apps.jmvp.presenter.Presenter;
import org.jsresources.apps.jmvp.view.swing.JInternalFrameView;

/** InternalFrameView with some default behaviour.
 *
 * This class has the following properties: <ol>
 *
 * <li>The JInternalFrame is resizable, closable, maximizable and
 * minimizable.</li>
 *
 * <li>The title bar of the JInternalFrame displays a name obtained
 * from the model.</li>
 *
 * <li>The title bar is updated on model change events (from the
 * model) and model property events (from the presenter).</li>
 *
 * </ol>
 *
 * @author Matthias Pfisterer
 */
public class DefaultInternalFrameView extends JInternalFrameView implements PropertyChangeListener, ModelListener {

    public DefaultInternalFrameView(Presenter presenter) {
        super(presenter);
        presenter.addPropertyChangeListener(this);
        setTitle("---");
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        display();
    }

    /** Display the content of the frame.
	 *
	 * This class' implementation only sets the title bar. Subclasses
	 * may choose to override this method to implement additional
	 * actions. If they do so, they should call the superclass'
	 * version in their overridden implementation.
	 */
    protected void display() {
        displayTitle();
    }

    /** Adapts the display to changes in the model.
	 *
	 * This class' implementation only sets the title bar. Subclasses
	 * may choose to override this method to implement additional
	 * actions. If they do so, they should call the superclass'
	 * version in their overridden implementation.
	 */
    public void modelChanged(ModelEvent event) {
        if (MVPDebug.TraceModelNotification) MVPDebug.out("DefaultInternalFrameView.modelChanged(): called with " + event);
        displayTitle();
    }

    /** Adapts the display to a newly set model.
	 *
	 * This class' implementation only sets the title bar. Subclasses
	 * may choose to override this method to implement additional
	 * actions. If they do so, they should call the superclass'
	 * version in their overridden implementation.
	 */
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(Presenter.MODEL_PROPERTY)) {
            displayTitle();
        }
    }
}
