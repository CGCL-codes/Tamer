package com.peterhi.client.ui;

import static com.peterhi.client.ui.Property.*;
import static com.peterhi.client.ui.PropertyEditorType.Int;
import static com.peterhi.client.ui.PropertyEditorType.RGB;
import static com.peterhi.client.ui.PropertyEditorType.ReadOnly;
import static com.peterhi.client.ui.PropertyEditorType.String;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Transform;
import com.peterhi.DuplicateIDException;
import com.peterhi.client.ui.events.ElementEvent;
import com.peterhi.client.ui.events.ElementListener;

/**
 * Root class for all <c>Whiteboard</c> <c>Elements</c>.
 * <br><br>
 * To subclass, note the following:
 * <ul>
 * 	<li>Create a constructor that has solely a <c>Whiteboard</c> parameter.</li>
 * 	<li>In your constructor, add the Common Properties you want to expose by
 *  invoking ids.add(), where ids is a <c>List</c> of <c>Property</c> types.</li>
 *  <li>Override the get(), getEditorType() and set() method. When overriding
 *  the get() and getEditorType() method, you should call the super class version
 *  before your own implementation. When overriding the set() method, however,
 *  you should always call the super version in right before the method returns
 *  (i.e. the very last of your method body). It is advised so because there are
 *  some verifications and error checking in each and every method.</li>
 * </ul>
 * @author YUN TAO HAI (hytparadisee)
 *
 */
public abstract class AbstractElement implements PropertyBound {

    /**
	 * The whiteboard this <c>Element</c> belongs to.
	 */
    private Whiteboard wboard;

    /**
	 * A switch such that, if set to <c>true</c> will
	 * cause the <c>Element</c> to be redrawn automatically when a
	 * property changes. If set to <c>false</c> it will
	 * not redraw automatically unless you refresh the <c>Whiteboard</c>.
	 */
    private boolean autoUpdate;

    /**
	 * <c>Element</c> listeners.
	 */
    private Set<ElementListener> listeners = new HashSet<ElementListener>();

    /**
	 * A list of <c>Property</c>s that will be exposed.
	 */
    protected List<Property> ids = new ArrayList<Property>();

    private short id = -1;

    /**
	 * <c>Element</c> name.
	 */
    private String name;

    /**
	 * <c>Element</c> fill color.
	 */
    private RGB fill = new RGB(255, 255, 255);

    /**
	 * <c>Element</c> stroke color.
	 */
    private RGB stroke = new RGB(0, 0, 0);

    /**
	 * <c>Element</c> stroke width.
	 */
    private int strokeWidth = 1;

    /**
	 * <c>Element</c> alpha value (transparency).
	 */
    private int alpha = 255;

    /**
	 * <c>Element</c> rotation (in degrees).
	 */
    private int rot;

    /**
	 * <c>Element</c> x-translation.
	 */
    private int tx;

    /**
	 * <c>Element</c> y-translation.
	 */
    private int ty;

    /**
	 * Constructor.
	 * @param whiteboard The <c>Whiteboard</c> this
	 * <c>Element</c> belongs to.
	 */
    public AbstractElement(Whiteboard whiteboard) {
        wboard = whiteboard;
        ids.add(ID);
        ids.add(Name);
        ids.add(Type);
        ids.add(Fill);
        ids.add(Stroke);
        ids.add(StrokeWidth);
        ids.add(Alpha);
        ids.add(Rotation);
        ids.add(XTranslate);
        ids.add(YTranslate);
    }

    protected Whiteboard whiteboard() {
        return wboard;
    }

    public boolean hasChanged(Property id, Object value) {
        Object old = get(id);
        if (old == null && value == null) {
            return false;
        }
        if (old != null && old.equals(value)) {
            return false;
        }
        return true;
    }

    public Object get(Property id) {
        switch(id) {
            case ID:
                return this.id;
            case Name:
                return name;
            case Type:
                return getType();
            case Fill:
                return fill;
            case Stroke:
                return stroke;
            case StrokeWidth:
                return strokeWidth;
            case Alpha:
                return alpha;
            case Rotation:
                return rot;
            case XTranslate:
                return tx;
            case YTranslate:
                return ty;
            default:
                return null;
        }
    }

    public PropertyEditorType getEditorType(Property id) {
        switch(id) {
            case ID:
                return ReadOnly;
            case Name:
                return String;
            case Type:
                return ReadOnly;
            case Fill:
                return RGB;
            case Stroke:
                return RGB;
            case StrokeWidth:
                return Int;
            case Alpha:
                return Int;
            case Rotation:
                return Int;
            case XTranslate:
                return Int;
            case YTranslate:
                return Int;
            default:
                return ReadOnly;
        }
    }

    public void set(Property id, Object value) throws Exception {
        ElementEvent e;
        switch(id) {
            case ID:
                short tempID = (Short) value;
                if (wboard.contains(tempID)) {
                    throw new DuplicateIDException();
                }
                e = new ElementEvent(this, tempID);
                dispatchChangeElementID(e);
                if (e.doit) {
                    this.id = tempID;
                }
                break;
            case Name:
                String temp = (String) value;
                if (temp == null || temp.length() <= 0) {
                    throw new NullPointerException("Element name cannot be null");
                }
                e = new ElementEvent(this, temp);
                dispatchRenameElement(e);
                if (e.doit) {
                    name = temp;
                }
                break;
            case Fill:
                fill = (RGB) value;
                break;
            case Stroke:
                stroke = (RGB) value;
                break;
            case StrokeWidth:
                strokeWidth = (Integer) value;
                break;
            case Alpha:
                alpha = (Integer) value;
                break;
            case Rotation:
                rot = (Integer) value;
                break;
            case XTranslate:
                tx = (Integer) value;
                break;
            case YTranslate:
                ty = (Integer) value;
                break;
            default:
                break;
        }
        update();
    }

    public List<Property> ids() {
        return ids;
    }

    /**
	 * Subscribe to <c>Whiteboard</c> <c>Element</c> events.
	 * @param l the <c>ElementListener</c>.
	 */
    public void addElementListener(ElementListener l) {
        if (l == null) {
            throw new NullPointerException();
        }
        listeners.add(l);
    }

    /**
	 * Unsubscribe to <c>Whiteboard</c> <c>Element</c> events.
	 * @param l the <c>ElementListener</c>.
	 */
    public void removeListener(ElementListener l) {
        if (l == null) {
            throw new NullPointerException();
        }
        listeners.remove(l);
    }

    /**
	 * Dispatch name changed event.
	 * @param old Old name.
	 * @param _new New name.
	 */
    private void dispatchRenameElement(ElementEvent e) {
        for (ElementListener l : listeners) {
            l.renameElement(e);
        }
    }

    private void dispatchChangeElementID(ElementEvent e) {
        for (ElementListener l : listeners) {
            l.changeElementID(e);
        }
    }

    /**
	 * Whether the current <c>Element</c> has auto-update enabled.
	 * @return <c>true</c> if auto-update is enabled, otherwise
	 * <c>false</c>.
	 * 
	 * @see #autoUpdate
	 */
    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    /**
	 * Mark/Unmark the current <c>Element</c> as auto-update
	 * to enable or disable auto-update.
	 * @param value Whether auto-update is enabled for
	 * this <c>Element</c>.
	 */
    public void setAutoUpdate(boolean value) {
        autoUpdate = value;
    }

    /**
	 * Update this element.
	 */
    public void update() {
        if (autoUpdate) {
            wboard.redraw();
        }
    }

    /**
	 * The actual drawing instructions on this
	 * <c>Element</c>.
	 * @param e The <c>PaintEvent</c>.
	 * 
	 * @see org.eclipse.swt.events.PaintEvent
	 */
    public void draw(PaintEvent e) {
        RGB fill = (RGB) get(Fill);
        RGB stroke = (RGB) get(Stroke);
        int strokeWidth = (Integer) get(StrokeWidth);
        int alpha = (Integer) get(Alpha);
        int tx = (Integer) get(XTranslate);
        int ty = (Integer) get(YTranslate);
        int rot = (Integer) get(Rotation);
        GC g = e.gc;
        int oldWidth = g.getLineWidth();
        int oldAlpha = g.getAlpha();
        Color oldBack = g.getBackground();
        Color oldFore = g.getForeground();
        Color newBack = null;
        if (fill != null) {
            newBack = new Color(e.display, fill);
            g.setBackground(newBack);
        }
        Color newFore = null;
        if (stroke != null) {
            newFore = new Color(e.display, stroke);
            g.setForeground(newFore);
        }
        g.setAlpha(alpha);
        g.setLineWidth(strokeWidth);
        Transform t = new Transform(e.display);
        t.translate(tx, ty);
        t.rotate(rot);
        g.setTransform(t);
        drawShape(e);
        t.dispose();
        g.setTransform(new Transform(e.display));
        g.setForeground(oldFore);
        g.setBackground(oldBack);
        g.setLineWidth(oldWidth);
        g.setAlpha(oldAlpha);
        if (newBack != null) {
            newBack.dispose();
        }
        if (newFore != null) {
            newFore.dispose();
        }
    }

    /**
	 * Gets the default image resource name. This image is used
	 * by tool bars or other system components to show this
	 * <c>Element</c>. In addition, the image must be located
	 * under the <c>ROOT</c> directory for reference.
	 * @return The name of the image of the <c>Element</c>.
	 * 
	 * @see com.peterhi.client.ui.Grabber#ROOT
	 */
    public abstract Image getIcon();

    public abstract ElementType getType();

    /**
	 * Subclass method to draw the <c>Element</c>.
	 * @param e event.
	 */
    protected abstract void drawShape(PaintEvent e);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final AbstractElement other = (AbstractElement) obj;
        if (id != other.id) return false;
        return true;
    }

    public String toString() {
        return (String) get(Property.Name);
    }
}
