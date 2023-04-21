package org.sourceforge.espro.elicitation;

import org.sourceforge.espro.model.ModelInterface;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;

/**
 * The ElicitationMethod is the superclass of all elicitation methods. If
 * you write a new one you should extend it from this class, in order to make
 * it working. At the moment there is no tutorial available on creating new
 * elicitation methods, so simply ask me at martin.kaffanke@gmx.at
 *
 * @author Martin Kaffanke
 * @version 1.1
 */
public abstract class ElicitationMethod extends JComponent implements ModelInterface {

    /**  */
    private static final long serialVersionUID = 7460269361873636050L;

    private static final Logger logger = Logger.getLogger(ElicitationMethod.class.getName());

    /** A Graphics2D object which all below should use. */
    protected Graphics2D g2;

    /** The property change support for elicitation methods. */
    protected PropertyChangeSupportProxy pcs = new PropertyChangeSupportProxy(this);

    protected boolean frozen = false;

    private String category = null;

    private String name = null;

    private boolean edit = false;

    /**
    * We create an elicitation method by telling it the name and the
    * category where the method belongs to. Normally we do this in the
    * constructor of child classes.
    *
    * @param name The name of the method.
    * @param category The category where this method belongs to.
    */
    protected ElicitationMethod(final String name, final String category) {
        super();
        this.name = name;
        this.category = category;
        setPreferredSize(new Dimension(200, 200));
        initialize();
        addSelfListeners();
    }

    /**
    * Adds a generic property change listener.
    *
    * @param listener The listener.
    */
    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
    * Adds a specific property change listener for the given property
    * name.
    *
    * @param propertyName The name of the property.
    * @param listener The listener.
    */
    @Override
    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    /**
    * Returns the category of the method.
    *
    * @return The category.
    */
    public String getCategory() {
        return category;
    }

    /**
    * Returns the name of the method.
    *
    * @return The name.
    */
    @Override
    public String getName() {
        return name;
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public boolean isEdit() {
        return edit;
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public boolean isFrozen() {
        return frozen;
    }

    /**
    * Removes a property change listener which was registered for all
    * properties.
    *
    * @param listener The listener to remove.
    */
    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
    * DOCUMENT ME!
    *
    * @param property DOCUMENT ME!
    * @param listener DOCUMENT ME!
    */
    @Override
    public void removePropertyChangeListener(final String property, final PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(property, listener);
    }

    /**
    * DOCUMENT ME!
    *
    * @param edit DOCUMENT ME!
    */
    public void setEdit(boolean edit) {
        boolean old = this.edit;
        this.edit = edit;
        pcs.firePropertyChange("edit", old, this.edit);
    }

    /**
    * Sets if the Method is frozen.  Frozen Methods cannot change any
    * settings.
    *
    * @param frozen true if we want it to be frozen.
    */
    public void setFrozen(boolean frozen) {
        boolean old = this.frozen;
        this.frozen = frozen;
        pcs.firePropertyChange("frozen", old, this.frozen);
        setEnabled(!frozen);
    }

    /**
    * An initialize function, which can be used within several
    * constructors.
    */
    protected abstract void initialize();

    /**
    * Frees the objects memory.
    */
    public void free() {
    }

    /**
    * DOCUMENT ME!
    */
    protected void addSelfListeners() {
        pcs.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent event) {
                repaint();
            }
        });
        logger.log(Level.FINEST, "Adding repaint listener.");
        addPropertyChangeListener("frozen", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                setEnabled(!(Boolean) evt.getNewValue());
            }
        });
    }

    /**
    * DOCUMENT ME!
    *
    * @param g DOCUMENT ME!
    */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        smothed(g2);
    }

    /**
    * DOCUMENT ME!
    *
    * @param g2 DOCUMENT ME!
    */
    protected void smothed(Graphics2D g2) {
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        hints.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.addRenderingHints(hints);
    }
}
