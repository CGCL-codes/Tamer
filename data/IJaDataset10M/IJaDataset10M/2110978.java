package org.wsmostudio.bpmo.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.Graphics;

/**
 * A connection between two distinct shapes.
 * @author Elias Volanakis
 */
public class GraphConnector implements Serializable {

    /** 
     * Used for indicating that a Connection with solid line style should be created.
     * @see org.eclipse.gef.examples.shapes.parts.ShapeEditPart#createEditPolicies() 
     */
    public static final Integer SOLID_CONNECTION = new Integer(Graphics.LINE_SOLID);

    /**
     * Used for indicating that a Connection with dashed line style should be created.
     * @see org.eclipse.gef.examples.shapes.parts.ShapeEditPart#createEditPolicies()
     */
    public static final Integer DASHED_CONNECTION = new Integer(Graphics.LINE_DASHDOT);

    /** Property ID to use when the line style of this connection is modified. */
    public static final String LINESTYLE_PROP = "LineStyle";

    private static final IPropertyDescriptor[] descriptors = new IPropertyDescriptor[1];

    private static final String SOLID_STR = "Solid";

    private static final String DASHED_STR = "Dashed";

    private static final long serialVersionUID = 1;

    private transient PropertyChangeSupport pcsDelegate = new PropertyChangeSupport(this);

    /** True, if the connection is attached to its endpoints. */
    private boolean isConnected;

    /** Line drawing style for this connection. */
    private int lineStyle = Graphics.LINE_SOLID;

    /** Connection's source endpoint. */
    private WorkflowEntity source;

    /** Connection's target endpoint. */
    private WorkflowEntity target;

    private Color foreground = null;

    private String label = null;

    static {
        descriptors[0] = new ComboBoxPropertyDescriptor(LINESTYLE_PROP, LINESTYLE_PROP, new String[] { SOLID_STR, DASHED_STR });
    }

    protected String m_sourceTerminal = "";

    protected String m_targetTerminal = "";

    public void setSourceTerminal(String s) {
        m_sourceTerminal = s;
    }

    public void setTargetTerminal(String s) {
        m_targetTerminal = s;
    }

    public String getSourceTerminal() {
        return m_sourceTerminal;
    }

    public String getTargetTerminal() {
        return m_targetTerminal;
    }

    /** 
     * Create a (solid) connection between two distinct shapes.
     * @param source a source endpoint for this connection (non null)
     * @param target a target endpoint for this connection (non null)
     * @throws IllegalArgumentException if any of the parameters are null or source == target
     * @see #setLineStyle(int) 
     */
    public GraphConnector(WorkflowEntity source, String sourceTerminal, WorkflowEntity target, String targetTerminal) {
        if (sourceTerminal != null) {
            setSourceTerminal(sourceTerminal);
        }
        if (targetTerminal != null) {
            setTargetTerminal(targetTerminal);
        }
        reconnect(source, target);
    }

    public void setLabel(String lab) {
        String oldLab = this.label;
        this.label = lab;
        firePropertyChange("name", oldLab, lab);
    }

    public String getLabel() {
        return this.label;
    }

    /** 
     * Disconnect this connection from the shapes it is attached to.
     */
    public void disconnect() {
        if (isConnected) {
            source.removeOutConnection(this);
            target.removeInConnection(this);
            isConnected = false;
            firePropertyChange("", null, null);
        }
    }

    /** 
     * Attach a non-null PropertyChangeListener to this object.
     * @param l a non-null PropertyChangeListener instance
     * @throws IllegalArgumentException if the parameter is null
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        if (l == null) {
            throw new IllegalArgumentException();
        }
        pcsDelegate.addPropertyChangeListener(l);
    }

    /** 
     * Report a property change to registered listeners (for example edit parts).
     * @param property the programmatic name of the property that changed
     * @param oldValue the old value of this property
     * @param newValue the new value of this property
     */
    protected void firePropertyChange(String property, Object oldValue, Object newValue) {
        if (pcsDelegate.hasListeners(property)) {
            pcsDelegate.firePropertyChange(property, oldValue, newValue);
        }
    }

    /** 
     * Remove a PropertyChangeListener from this component.
     * @param l a PropertyChangeListener instance
     */
    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        if (l != null) {
            pcsDelegate.removePropertyChangeListener(l);
        }
    }

    public int getLineStyle() {
        return lineStyle;
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color col) {
        foreground = col;
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        return descriptors;
    }

    public Object getPropertyValue(Object id) {
        if (id.equals(LINESTYLE_PROP)) {
            if (getLineStyle() == Graphics.LINE_DASH) return new Integer(1);
            return new Integer(0);
        }
        return null;
    }

    /**
     * Returns the source endpoint of this connection.
     * @return a non-null Shape instance
     */
    public WorkflowEntity getSource() {
        return source;
    }

    /**
     * Returns the target endpoint of this connection.
     * @return a non-null Shape instance
     */
    public WorkflowEntity getTarget() {
        return target;
    }

    /** 
     * Reconnect this connection. 
     * The connection will reconnect with the shapes it was previously attached to.
     */
    public void reconnect() {
        if (!isConnected) {
            target.addInConnection(this);
            source.addOutConnection(this);
            isConnected = true;
            firePropertyChange("", null, null);
        }
    }

    /**
     * Reconnect to a different source and/or target shape.
     * The connection will disconnect from its current attachments and reconnect to 
     * the new source and target. 
     * @param newSource a new source endpoint for this connection (non null)
     * @param newTarget a new target endpoint for this connection (non null)
     * @throws IllegalArgumentException if any of the paramers are null or newSource == newTarget
     */
    public void reconnect(WorkflowEntity newSource, WorkflowEntity newTarget) {
        if (newSource == null || newTarget == null || newSource == newTarget) {
            throw new IllegalArgumentException();
        }
        disconnect();
        this.source = newSource;
        this.target = newTarget;
        reconnect();
    }

    /**
     * Set the line drawing style of this connection.
     * @param lineStyle one of following values: Graphics.LINE_DASH or Graphics.LINE_SOLID
     * @see Graphics#LINE_DASH
     * @see Graphics#LINE_SOLID
     * @throws IllegalArgumentException if lineStyle does not have one of the above values
     */
    public void setLineStyle(int lineStyle) {
        if (lineStyle != Graphics.LINE_DASH && lineStyle != Graphics.LINE_SOLID) {
            throw new IllegalArgumentException();
        }
        this.lineStyle = lineStyle;
        firePropertyChange(LINESTYLE_PROP, null, new Integer(this.lineStyle));
    }

    /**
     * Sets the lineStyle based on the String provided by the PropertySheet
     * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
     */
    public void setPropertyValue(Object id, Object value) {
        if (id.equals(LINESTYLE_PROP)) setLineStyle(new Integer(1).equals(value) ? Graphics.LINE_DASH : Graphics.LINE_SOLID);
    }

    /** List of bendpoint model object associated with current connection model object. */
    protected List<Bendpoint> bendpoints = new ArrayList<Bendpoint>();

    /** Returns the list of bendpoints model objects. */
    public List<Bendpoint> getBendpoints() {
        return bendpoints;
    }

    public void insertBendpoint(int index, Bendpoint point) {
        getBendpoints().add(index, point);
        firePropertyChange("bendpoint", null, null);
    }

    /**
     * Removes a bendpoint.
     **/
    public void removeBendpoint(int index) {
        getBendpoints().remove(index);
        firePropertyChange("bendpoint", null, null);
    }

    /**
     * Sets another location for an existing bendpoint.
     **/
    public void setBendpoint(int index, Bendpoint point) {
        getBendpoints().set(index, point);
        firePropertyChange("bendpoint", null, null);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.pcsDelegate = new PropertyChangeSupport(this);
    }
}
