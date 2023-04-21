package au.gov.naa.digipres.xena.util;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JComponent;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

/**
 * This is the glass pane class that intercepts screen interactions during
 * system busy states.
 * 
 */
public class GlassPane extends JComponent implements AWTEventListener {

    private Window theWindow;

    private Component activeComponent;

    /**
	 * GlassPane constructor comment.
	 * 
	 * @param Container
	 *            a
	 */
    protected GlassPane(Component activeComponent) {
        addMouseListener(new MouseAdapter() {
        });
        addKeyListener(new KeyAdapter() {
        });
        setActiveComponent(activeComponent);
    }

    /**
	 * Receives all key events in the AWT and processes the ones that originated
	 * from the current window with the glass pane.
	 * 
	 * @param event
	 *            the AWTEvent that was fired
	 */
    public void eventDispatched(AWTEvent event) {
        Object source = event.getSource();
        boolean sourceIsComponent = (event.getSource() instanceof Component);
        if ((event instanceof KeyEvent) && sourceIsComponent) {
            if ((SwingUtilities.windowForComponent((Component) source) == theWindow)) {
                ((KeyEvent) event).consume();
            }
        }
    }

    /**
	 * Finds the glass pane that is related to the specified component.
	 * 
	 * @param startComponent
	 *            the component used to start the search for the glass pane
	 * @param create
	 *            a flag whether to create a glass pane if one does not exist
	 * @return GlassPane
	 */
    public static synchronized GlassPane mount(Component startComponent, boolean create) {
        RootPaneContainer aContainer = null;
        Component aComponent = startComponent;
        while ((aComponent.getParent() != null) && !(aComponent instanceof RootPaneContainer)) {
            aComponent = aComponent.getParent();
        }
        if (aComponent instanceof RootPaneContainer) {
            aContainer = (RootPaneContainer) aComponent;
        }
        if (aContainer != null) {
            if ((aContainer.getGlassPane() != null) && (aContainer.getGlassPane() instanceof GlassPane)) {
                return (GlassPane) aContainer.getGlassPane();
            } else if (create) {
                GlassPane aGlassPane = new GlassPane(startComponent);
                aContainer.setGlassPane(aGlassPane);
                return aGlassPane;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
	 * Set the component that ordered-up the glass pane.
	 * 
	 * @param aComponent
	 *            the UI component that asked for the glass pane
	 */
    private void setActiveComponent(Component aComponent) {
        activeComponent = aComponent;
    }

    /**
	 * Sets the glass pane as visible or invisible. The mouse cursor will be set
	 * accordingly.
	 */
    @Override
    public void setVisible(boolean value) {
        if (value) {
            if (theWindow == null) {
                theWindow = SwingUtilities.windowForComponent(activeComponent);
                if (theWindow == null) {
                    if (activeComponent instanceof Window) {
                        theWindow = (Window) activeComponent;
                    }
                }
            }
            getTopLevelAncestor().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            activeComponent = theWindow.getFocusOwner();
            Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
            this.requestFocus();
            super.setVisible(value);
        } else {
            Toolkit.getDefaultToolkit().removeAWTEventListener(this);
            super.setVisible(value);
            if (getTopLevelAncestor() != null) {
                getTopLevelAncestor().setCursor(null);
            }
        }
    }
}
