package org.ascape.view.vis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.TooManyListenersException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.ascape.explorer.ViewFrameBridge;
import org.ascape.model.Scape;
import org.ascape.model.event.ScapeEvent;
import org.ascape.movie.MovieRecorder;

/**
 * A base class for a panel that acts as an observer of scapes. Provides
 * notification of scape updates.
 * 
 * @author Miles Parker
 * @version 2.9
 * @history 2.9 5/9/02 updated for new movie refactorings
 * @history 2.9 3/31/02 delegted scape management
 * @history 1.9.1 0?/00 added support for graphics recorder
 * @history 1.2.6 10/25/99 added support for named listeners
 * @history 1.0.1 added support for removing scapes
 * @history 1.0.2 3/6/1999 numerous update changes, made aware of view frame
 * @history 1.0.3 4/16/1999 fixed updateScapeGraphics so no need to call super
 *          calss for more straightforward subclassing
 * @history 1.1.1 5/1/1999 support for different kinds of frames, small fixes
 * @since 1.0
 */
public class PanelView extends JPanel implements ComponentView, Externalizable {

    /**
     * The scape being viewed.
     */
    protected Scape scape;

    /**
     * The delegate.
     */
    protected ComponentViewDelegate delegate;

    /**
     * The frame this view is displayed in. Null if this view isn't in a frame.
     */
    private ViewFrameBridge frame;

    /**
     * Specifies the number of updates that occur between each draw. Default is
     * 1, so that every update is drawn.
     */
    protected int iterationsPerRedraw = 1;

    /**
     * The name of the view.
     */
    protected String name = "Panel View";

    /**
     * Constructs a panel view.
     */
    public PanelView() {
        this("");
    }

    /**
     * Constructs a panel view.
     * 
     * @param name
     *            a user relevant name for this view
     */
    public PanelView(String name) {
        this.name = name;
        delegate = new ComponentViewDelegate(this);
        delegate.setNotifyScapeAutomatically(false);
        setDoubleBuffered(false);
    }

    /**
     * Called to create and layout the components of the component view, once
     * the view's scape has been created.
     */
    public void build() {
    }

    /**
     * Notifies the listener that the scape has added it.
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
        this.scape = ((Scape) scapeEvent.getSource());
        delegate.scapeAdded(scapeEvent);
    }

    /**
     * Notifies the listener that the scape has removed it.
     * 
     * @param scapeEvent
     *            the scape removed notification event
     */
    public void scapeRemoved(ScapeEvent scapeEvent) {
        delegate.scapeRemoved(scapeEvent);
    }

    /**
     * Notifies this view that something has happened on the scape. This view
     * then has a chance to update itself, and this super method then notifies
     * the scape that the view itself has been updated. By default, calls the
     * onStart, updateScapeGraphics, or onStop method as appropriate, and then
     * notifies scape.
     * 
     * @param scapeEvent
     *            a scape event update
     */
    public void scapeNotification(ScapeEvent scapeEvent) {
        delegate.scapeNotification(scapeEvent);
    }

    /**
     * Called when scape is iterated.
     * 
     * @param scapeEvent
     *            the scape event
     */
    public void scapeIterated(ScapeEvent scapeEvent) {
    }

    /**
     * Method called when the scape is ready for setup.
     * 
     * @param scapeEvent
     *            the scape event
     */
    public void scapeSetup(ScapeEvent scapeEvent) {
    }

    /**
     * Called when scape reports an update event. (No need to call this method
     * after updating panel.)
     */
    public void updateScapeGraphics() {
    }

    /**
     * Method called when the scape is initialized.
     * 
     * @param scapeEvent
     *            the scape event
     */
    public void scapeInitialized(ScapeEvent scapeEvent) {
    }

    /**
     * Method called when the scape is started.
     * 
     * @param scapeEvent
     *            the scape event
     */
    public void scapeStarted(ScapeEvent scapeEvent) {
    }

    /**
     * Method called when the scape is stopped.
     * 
     * @param scapeEvent
     *            the scape event
     */
    public void scapeStopped(ScapeEvent scapeEvent) {
    }

    /**
     * Method called when the scape is closed.
     * 
     * @param scapeEvent
     *            the scape event
     */
    public void scapeClosing(ScapeEvent scapeEvent) {
    }

    /**
     * Method called when the environment quits.
     * 
     * @param scapeEvent
     *            the scape event
     */
    public void environmentQuiting(ScapeEvent scapeEvent) {
    }

    public void onChangeIterationsPerRedraw() {
        setIterationsPerRedraw(scape.getIterationsPerRedraw());
    }

    /**
     * Method called once a model is deserialized.
     * 
     * @param scapeEvent
     *            the scape event
     */
    public void scapeDeserialized(ScapeEvent scapeEvent) {
        delegate.scapeDeserialized(scapeEvent);
    }

    /**
     * Returns a name for the view as defined by set name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this view.
     * 
     * @param name
     *            a user relevant name for this view
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the recorder that can be used to record the graphics of this view.
     * If null, do not record.
     * 
     * @param recorder
     *            the recorder
     */
    public void setMovieRecorder(MovieRecorder recorder) {
        delegate.setGraphicsRecorder(recorder);
    }

    /**
     * Returns true; this is a GUI view.
     * 
     * @return true, if is graphic
     */
    public final boolean isGraphic() {
        return true;
    }

    /**
     * Returns true (default) if the listener is intended to be used only for
     * the current scape; typical of all but control related listeners.
     * 
     * @return true, if is life of scape
     */
    public boolean isLifeOfScape() {
        return true;
    }

    /**
     * Returns the scape this canvas views.
     * 
     * @return the scape
     */
    public Scape getScape() {
        return scape;
    }

    /**
     * Returns the number of iterations that occur before the view is updated.
     * 
     * @return the iterations per redraw
     */
    public int getIterationsPerRedraw() {
        return iterationsPerRedraw;
    }

    /**
     * Sets the number of iterations that occur before the view is updated.
     * 
     * @param iterationsPerRedraw
     *            the number of iterations to wait between paints
     */
    public void setIterationsPerRedraw(int iterationsPerRedraw) {
        this.iterationsPerRedraw = iterationsPerRedraw;
    }

    /**
     * Returns the frame this view occupies, null if none.
     * 
     * @return the view frame
     */
    public ViewFrameBridge getViewFrame() {
        return frame;
    }

    /**
     * Sets the frame this view appears in.
     * 
     * @param frame
     *            the frame
     */
    public void setViewFrame(ViewFrameBridge frame) {
        this.frame = frame;
    }

    /**
     * Paints the panel. If view is awaiting update, paint calls updated so that
     * the scape will be notified when we are done painting. This method should
     * rarely need to be overridden; use scapeNotification to update component
     * state.
     * 
     * @param g
     *            the g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        delegate.viewPainted();
    }

    /**
     * Called to notify the parent scape that the view has been updated.
     */
    public void notifyScapeUpdated() {
        delegate.notifyScapeUpdated();
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
        out.writeObject(scape);
        out.writeObject(name);
        out.writeObject(delegate);
        out.writeObject(frame);
        out.writeInt(iterationsPerRedraw);
        out.writeObject(getForeground());
        out.writeObject(getBackground());
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
        scape = (Scape) in.readObject();
        name = (String) in.readObject();
        delegate = (ComponentViewDelegate) in.readObject();
        frame = (ViewFrameBridge) in.readObject();
        iterationsPerRedraw = in.readInt();
        setForeground((Color) in.readObject());
        setBackground((Color) in.readObject());
    }

    /**
     * Clones this object.
     * 
     * @return the object
     */
    public Object clone() {
        try {
            PanelView clone = (PanelView) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    /**
     * Gets the delegate.
     * 
     * @return the delegate
     */
    public ComponentViewDelegate getDelegate() {
        return delegate;
    }

    /**
     * Sets the delegate.
     * 
     * @param delegate
     *            the new delegate
     */
    public void setDelegate(ComponentViewDelegate delegate) {
        this.delegate = delegate;
    }

    public void forceScapeNotify() {
        delegate.forceScapeNotify();
    }

    /**
     * Default behavior for a component. Simply returns same size.
     * 
     * @param d
     *            the d
     * @return the preferred size within
     */
    public Dimension getPreferredSizeWithin(Dimension d) {
        return d;
    }

    /**
     * Return an icon that can be used to represent this frame. Returns null in
     * this case, use default. Implementors should specify an icon that makes
     * sense for the view.
     * 
     * @return the icon
     */
    public ImageIcon getIcon() {
        return null;
    }

    /**
     * Should be called when the view has updated itself in a way that changes
     * icon.
     */
    public void iconUpdated() {
        if (getViewFrame() != null) {
            getViewFrame().iconUpdated();
        }
    }

    /**
     * Returns a short description of this view. Sames as name unless
     * overridden.
     * 
     * @return the string
     */
    public String toString() {
        return name;
    }
}
