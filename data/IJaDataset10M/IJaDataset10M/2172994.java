package org.charvolant.tmsnet.ui;

import org.charvolant.properties.BeanDescription;
import org.charvolant.properties.annotations.Property;
import org.charvolant.tmsnet.AbstractModel;
import org.charvolant.tmsnet.util.ResourceManager;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Composite;

/**
 * An information panel for a class of objects which constructs a suitable
 * display for the class using a list of properties.
 * <p>
 * The properties that are displayed are supplied by the 
 * {@link #getProperties()} method. These refer to named properties
 * in the model object that have been annotated as properties by the 
 * {@link Property} annotation.
 * <p>
 * Properties may be either on the model or on the panel itself.
 * Panel properties override model properties.
 * <p>
 * Generally, an information panel is editable or read-only as a whole.
 * This state can be overridden on individual properties.
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 * @param <Model>
 */
public abstract class AbstractInformationPanel<Model extends AbstractModel> extends AbstractModelPanel<Model> implements ControlListener {

    /** The read-only property indicator. */
    public static final String READ_ONLY_INDICATOR = "-";

    /** The editable property indicator. */
    public static final String EDITABLE_INDICATOR = "+";

    /** The panel information */
    protected BeanDescription panelDescription;

    /** The bean information */
    protected BeanDescription beanDescription;

    /** Is this a read-only panel? */
    protected boolean readOnly;

    /**
   * Construct an information panel with an initial model.
   * <p>
   * The readOnly flag can be overridden by individual property names
   * in {@link #getProperties()}.
   *
   * @param model The bean to model
   * @param readOnly Make the data display only
   * @param resources The resource manager to use
   * @param parent The parent widget
   * @param style Style flags
   * 
   * @throws Exception if unable to describe the model
   */
    public AbstractInformationPanel(Model model, boolean readOnly, ResourceManager resources, Composite parent, int style) throws Exception {
        super(model, resources, parent, style);
        this.readOnly = readOnly;
        this.panelDescription = new BeanDescription(this.getClass());
        this.beanDescription = new BeanDescription(model.getClass());
        this.buildUI();
        this.addControlListener(this);
    }

    /**
   * Construct an information panel with on an initial model class.
   *
   * @param modelClass The class of bean to eventually model
   * @param readOnly Make the data display only
   * @param resources The resource manager to use
   * @param parent The parent widget
   * @param style Style flags
   * 
   * @throws Exception if unable to describe the model
   */
    public AbstractInformationPanel(Class<Model> modelClass, boolean readOnly, ResourceManager resources, Composite parent, int style) throws Exception {
        super(null, resources, parent, style);
        this.readOnly = readOnly;
        this.panelDescription = new BeanDescription(this.getClass());
        this.beanDescription = new BeanDescription(modelClass);
        this.buildUI();
        this.addControlListener(this);
    }

    /**
   * Build the user interface
   * 
   * @throws Exception if unable to build the UI
   */
    protected abstract void buildUI() throws Exception;

    /**
   * Respond to a control moved event.
   * <p>
   * Currently ignored
   *
   * @param e The event
   * 
   * @see org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.ControlEvent)
   */
    @Override
    public void controlMoved(ControlEvent e) {
    }

    /**
   * Respond to a control resized event
   * <p>
   * Ignored. Subclasses may need to re-implement this to ensure that elements of the information panel
   * update correctly.
   *
   * @param e The event
   * 
   * @see org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt.events.ControlEvent)
   */
    @Override
    public void controlResized(ControlEvent e) {
    }

    /**
   * Get the properties that this panel will display.
   * <p>
   * If a property name is prefixed with a '-' the property will be
   * read-only. If a property name is prefixed with a '+' the property will
   * be editable.
   * 
   * @return The properties
   */
    protected abstract String[] getProperties();
}
