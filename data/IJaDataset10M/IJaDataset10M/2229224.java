package com.google.code.appengine.awt.dnd;

import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import org.apache.harmony.awt.internal.nls.Messages;
import com.google.code.appengine.awt.Component;
import com.google.code.appengine.awt.Cursor;
import com.google.code.appengine.awt.Image;
import com.google.code.appengine.awt.Point;
import com.google.code.appengine.awt.datatransfer.Transferable;
import com.google.code.appengine.awt.dnd.DnDConstants;
import com.google.code.appengine.awt.dnd.DragGestureRecognizer;
import com.google.code.appengine.awt.dnd.DragSource;
import com.google.code.appengine.awt.dnd.DragSourceListener;
import com.google.code.appengine.awt.dnd.InvalidDnDOperationException;
import com.google.code.appengine.awt.event.InputEvent;

public class DragGestureEvent extends EventObject {

    private static final long serialVersionUID = 9080172649166731306L;

    private final DragGestureRecognizer recognizer;

    private final Point origin;

    private final List<InputEvent> eventList;

    private final int action;

    @SuppressWarnings("unchecked")
    public DragGestureEvent(DragGestureRecognizer dgr, int act, Point ori, List<? extends InputEvent> evs) {
        super(dgr);
        if (dgr.getComponent() == null) {
            throw new IllegalArgumentException(Messages.getString("awt.185"));
        }
        if (dgr.getDragSource() == null) {
            throw new IllegalArgumentException(Messages.getString("awt.186"));
        }
        if (!DnDConstants.isValidAction(act)) {
            throw new IllegalArgumentException(Messages.getString("awt.184"));
        }
        if (ori == null) {
            throw new IllegalArgumentException(Messages.getString("awt.187"));
        }
        if (evs == null) {
            throw new IllegalArgumentException(Messages.getString("awt.188"));
        }
        if (evs.isEmpty()) {
            throw new IllegalArgumentException(Messages.getString("awt.189"));
        }
        recognizer = dgr;
        action = act;
        origin = ori;
        eventList = (List<InputEvent>) evs;
    }

    public DragSource getDragSource() {
        return recognizer.getDragSource();
    }

    public DragGestureRecognizer getSourceAsDragGestureRecognizer() {
        return recognizer;
    }

    public Point getDragOrigin() {
        return new Point(origin);
    }

    public Component getComponent() {
        return recognizer.getComponent();
    }

    public int getDragAction() {
        return action;
    }

    public Object[] toArray(Object[] array) {
        return eventList.toArray(array);
    }

    public Object[] toArray() {
        return eventList.toArray();
    }

    public Iterator<InputEvent> iterator() {
        return eventList.iterator();
    }

    public InputEvent getTriggerEvent() {
        return recognizer.getTriggerEvent();
    }

    public void startDrag(Cursor dragCursor, Transferable transferable) throws InvalidDnDOperationException {
        DragSourceListener[] listeners = recognizer.dragSource.getDragSourceListeners();
        DragSourceListener dsl = listeners.length > 0 ? new DragSourceMulticaster(listeners) : null;
        startDrag(dragCursor, transferable, dsl);
    }

    public void startDrag(Cursor dragCursor, Image dragImage, Point imageOffset, Transferable transferable, DragSourceListener dsl) throws InvalidDnDOperationException {
        recognizer.getDragSource().startDrag(this, dragCursor, dragImage, imageOffset, transferable, dsl);
    }

    public void startDrag(Cursor dragCursor, Transferable transferable, DragSourceListener dsl) throws InvalidDnDOperationException {
        recognizer.getDragSource().startDrag(this, dragCursor, transferable, dsl);
    }
}
