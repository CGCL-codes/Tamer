package client.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import client.Client;
import core.GameObject;

/**
 * AbstractView is the superclass of all Views in the Model-View-Controller
 * pattern.  It ties a GameObject (model) to a JComponent (view).  It also 
 * plays the role of Observer in the Observer pattern (GoF), listening to
 * UpdateActionEvents from the model.
 */
public class AbstractView {

    /**
	 * Create a new viewless AbstractView, given a 'Model' and 'Controller'.
	 * @param m the GameObject from which this view is derived
	 * @param c the Client connection over which commands will be passed
	 */
    public AbstractView(GameObject m, Client c) {
        this(m, null, c);
    }

    /**
	 * Create a new AbstractView, given a 'Model', 'View', and 'Controller'.
	 * @param m the GameObject from which this view is derived
	 * @param c a JComponent that visualizes the GameObject
	 * @param cl the Client connection over which commands will be passed
	 */
    public AbstractView(GameObject m, JComponent c, Client cl) {
        model = m;
        component = c;
        client = cl;
        popupMenu = new AttributesPopupMenu(this);
        setComponent(c);
        if (c == null) return;
    }

    /**
	 * Returns the JComponent associated with this View.
	 */
    public JComponent getComponent() {
        return component;
    }

    /**
	 * Sets the JComponent associated with this View.  This method does
	 * not refresh the display.
	 */
    public void setComponent(final JComponent component) {
        this.component = component;
        if (component == null) return;
        component.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent me) {
                if (me.getButton() != MouseEvent.BUTTON1) {
                    popupMenu.refresh();
                    popupMenu.show(component, me.getX(), me.getY());
                }
            }
        });
    }

    /**
	 * When an update message is received by the AbstractView, this method
	 * is fired. Subclasses should override this method to receive notification
	 * of view update requests.
	 */
    public void updateView() {
    }

    /**
	 * Returns the GameObject associated with this View.
	 */
    public GameObject getModel() {
        return model;
    }

    /**
	 * Returns the Client associated with this View.
	 */
    public Client getClient() {
        return client;
    }

    protected JComponent component;

    protected final GameObject model;

    /** All AbstractViews display a popup menu when the view is right-clicked */
    protected final AttributesPopupMenu popupMenu;

    protected Client client;
}
