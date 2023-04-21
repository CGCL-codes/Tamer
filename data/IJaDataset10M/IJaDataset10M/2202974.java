package org.ascape.view.vis;

import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.TooManyListenersException;
import org.ascape.model.event.ScapeEvent;

/**
 * A generic base class for views that draw some kind of spatial view of a group
 * of cells. Cell views have a default draw feature that draws a background for
 * the cell, using the cell color feature.
 * 
 * @author Miles Parker
 * @version 3.0
 * @history 3.0 extracted from CellView
 * @since 3.0
 */
public abstract class AgentSizedView extends AgentView {

    /**
     * Size of each individual cell, typically in pixels. 6 by default.
     */
    protected int agentSize = 3;

    /**
     * Instantiates a new agent sized view.
     */
    public AgentSizedView() {
    }

    /**
     * Instantiates a new agent sized view.
     * 
     * @param name
     *            the name
     */
    public AgentSizedView(String name) {
        super(name);
    }

    /**
     * Override addNotify to build buffer.
     */
    public void addNotify() {
        super.addNotify();
        setAgentSize(agentSize);
    }

    public void build() {
        super.build();
        setAgentSize(agentSize);
    }

    /**
     * Notifies the listener that the scape has added it. Override to set draw
     * network on if the scape is a small world.
     * 
     * @param scapeEvent
     *            the scape added notification event
     * @throws TooManyListenersException
     *             the too many listeners exception
     * @exception java.util.TooManyListenersException
     *                on attempt to add this listener to another scape when one
     *                has already been assigned
     */
    public void scapeAdded(ScapeEvent scapeEvent) throws TooManyListenersException {
        super.scapeAdded(scapeEvent);
        setPreferredSize(calculateViewSizeForAgentSize(agentSize));
    }

    /**
     * Calculate view size for agent size.
     * 
     * @param agentSize
     *            the agent size
     * @return the dimension
     */
    public Dimension calculateViewSizeForAgentSize(int agentSize) {
        return getSize();
    }

    /**
     * Calculate agent size for view size.
     * 
     * @param d
     *            the d
     * @return the int
     */
    public int calculateAgentSizeForViewSize(Dimension d) {
        return agentSize;
    }

    public Dimension getPreferredSizeWithin(Dimension d) {
        int tempCellSize = calculateAgentSizeForViewSize(d);
        Dimension internalSize = calculateViewSizeForAgentSize(tempCellSize);
        return internalSize;
    }

    public synchronized void setBounds(int x, int y, int width, int height) {
        if ((getScape() != null) && (scape.isInitialized())) {
            scape.requestUpdate();
        }
        super.setBounds(x, y, width, height);
    }

    /**
     * Returns a one-dimension size of pixels used to represent each cell.
     * 
     * @return the agent size
     */
    public int getAgentSize() {
        return agentSize;
    }

    /**
     * Sets the number of pixels used to represent each cell.
     * 
     * @param cellSize
     *            number of pixels per edge
     */
    public void setAgentSize(int cellSize) {
        this.agentSize = cellSize;
        if (scape != null) {
            setSize(calculateViewSizeForAgentSize(cellSize));
            setPreferredSize(getSize());
        }
    }

    /**
     * The object implements the writeExternal method to save its contents by
     * calling the methods of DataOutput for its primitive values or calling the
     * writeObject method of ObjectOutput for objects, strings, and arrays.
     * 
     * @param out
     *            the stream to write the object to
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @serialData Overriding methods should use this tag to describe the data
     *             layout of this Externalizable object. List the sequence of
     *             element types and, if possible, relate the element to a
     *             public/protected field and/or method of this Externalizable
     *             class.
     * @exception java.io.IOException
     *                Includes any I/O exceptions that may occur
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(new Integer(agentSize));
    }

    /**
     * The object implements the readExternal method to restore its contents by
     * calling the methods of DataInput for primitive types and readObject for
     * objects, strings and arrays. The readExternal method must read the values
     * in the same sequence and with the same types as were written by
     * writeExternal.
     * 
     * @param in
     *            the stream to read data from in order to restore the object
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException
     *             the class not found exception
     * @exception java.io.IOException
     *                if I/O errors occur
     * @exception java.lang.ClassNotFoundException
     *                If the class for an object being restored cannot be found.
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        agentSize = ((Integer) in.readObject()).intValue();
    }
}
