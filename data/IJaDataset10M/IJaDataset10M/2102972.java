package com.mtp.pounder;

import java.awt.Frame;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.AWTEvent;
import java.awt.Window;
import java.awt.KeyboardFocusManager;
import java.awt.KeyEventDispatcher;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.AWTEventListener;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import javax.swing.SwingUtilities;
import com.mtp.gui.WindowWatcher;

/**

Records all AWT events sent.

@author Matthew Pekar

**/
public class VerbatimRecording implements AWTEventListener, Recording, KeyEventDispatcher {

    /** After idle for this long we terminate. **/
    public static final long DEFAULT_MAX_IDLE_TIME = 100000;

    protected WindowWatcher windowWatcher;

    protected long mask;

    protected boolean finished, paused;

    protected RecordingRecord record;

    protected long lastTimeItemRecorded;

    protected Filter filter;

    protected long maxIdleTime;

    protected PounderPrefs prefs;

    protected ComponentIdentifierFactory componentIdentifierFactory;

    public VerbatimRecording(PounderPrefs prefs) {
        this(prefs, DEFAULT_MAX_IDLE_TIME);
    }

    public VerbatimRecording(PounderPrefs prefs, long maxIdleTime) {
        this.windowWatcher = null;
        this.mask = AWTEvent.ACTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK | AWTEvent.TEXT_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK | AWTEvent.WINDOW_FOCUS_EVENT_MASK | AWTEvent.WINDOW_STATE_EVENT_MASK | AWTEvent.COMPONENT_EVENT_MASK | AWTEvent.INPUT_METHOD_EVENT_MASK;
        this.finished = false;
        this.paused = false;
        this.record = null;
        this.lastTimeItemRecorded = 0;
        this.prefs = prefs;
        this.filter = null;
        this.maxIdleTime = maxIdleTime;
        this.componentIdentifierFactory = new ComponentIdentifierFactory();
    }

    public synchronized void begin(RecordingRecord record, WindowWatcher ww) {
        this.windowWatcher = ww;
        this.windowWatcher.setFilterAllWindows(paused);
        this.record = record;
        this.record.clear();
        this.filter = new DefaultFilter(ww);
        lastTimeItemRecorded = System.currentTimeMillis();
        addListeners();
    }

    protected void addListeners() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, mask);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }

    public synchronized boolean isFinished() {
        if (finished) return finished;
        return ((System.currentTimeMillis() - lastTimeItemRecorded) > maxIdleTime);
    }

    /** Terminate recording. **/
    public synchronized void terminate() {
        if (finished) return;
        finished = true;
        if (record != null) {
            record.addItem(new DummyRecordingItem(System.currentTimeMillis() - lastTimeItemRecorded));
        }
        removeListeners();
    }

    protected void removeListeners() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
    }

    public boolean dispatchKeyEvent(KeyEvent e) {
        eventDispatched(e);
        return false;
    }

    protected int getWindowID(Component c) {
        if (c instanceof Window) return windowWatcher.getWindowID((Window) c);
        Window w = SwingUtilities.windowForComponent(c);
        return windowWatcher.getWindowID(SwingUtilities.windowForComponent(c));
    }

    protected boolean eventShouldBeIgnored(AWTEvent e) {
        if (!prefs.getIgnoreUnnamed()) return false;
        if (e instanceof ComponentEvent) {
            ComponentEvent ce = (ComponentEvent) e;
            return ce.getComponent().getName() == null;
        }
        return false;
    }

    public synchronized void setPaused(boolean b) {
        paused = b;
        if (windowWatcher != null) windowWatcher.setFilterAllWindows(b);
        if (!b) lastTimeItemRecorded = System.currentTimeMillis();
    }

    public synchronized boolean isPaused() {
        return paused;
    }

    public synchronized void eventDispatched(AWTEvent e) {
        if (prefs.getEventDetector().isEndEvent(e)) {
            terminate();
            return;
        }
        if (paused) return;
        if (eventShouldBeIgnored(e)) return;
        if (!filter.accepts(e)) return;
        try {
            matchEvent(e);
        } catch (Exception exc) {
            exc.printStackTrace();
            terminate();
        }
    }

    protected boolean mouseWheelEventMatched(AWTEvent e, long delay) {
        if (!prefs.getVerbatimRecordingOptions().doMouseInputEvents) return false;
        if (!(e instanceof MouseWheelEvent)) return false;
        MouseWheelEvent mwe = (MouseWheelEvent) e;
        record.addItem(new MouseWheelItem(mwe, getWindowID(mwe.getComponent()), delay, componentIdentifierFactory));
        return true;
    }

    protected boolean mouseEventMatched(AWTEvent e, long delay) {
        if (!(e instanceof MouseEvent)) return false;
        MouseEvent me = (MouseEvent) e;
        int id = me.getID();
        switch(id) {
            case MouseEvent.MOUSE_PRESSED:
            case MouseEvent.MOUSE_RELEASED:
            case MouseEvent.MOUSE_CLICKED:
                record.addItem(new MouseClickItem(me, getWindowID(me.getComponent()), delay, componentIdentifierFactory));
        }
        return true;
    }

    protected boolean mouseMotionEventMatched(AWTEvent e, long delay) {
        if (!(e instanceof MouseEvent)) return false;
        MouseEvent me = (MouseEvent) e;
        int id = me.getID();
        switch(id) {
            case MouseEvent.MOUSE_ENTERED:
            case MouseEvent.MOUSE_EXITED:
            case MouseEvent.MOUSE_MOVED:
                if (dragging) checkIsDragEnd(me, delay);
                record.addItem(new MouseMotionItem(me, getWindowID(me.getComponent()), delay, componentIdentifierFactory));
                break;
            case MouseEvent.MOUSE_DRAGGED:
                {
                    if (!dragging) startDrag(me);
                    record.addItem(new MouseMotionItem(me, getWindowID(me.getComponent()), delay, componentIdentifierFactory));
                    break;
                }
        }
        return true;
    }

    protected boolean windowEventMatched(AWTEvent e, long delay) {
        int id = e.getID();
        if (e instanceof WindowEvent) {
            switch(id) {
                case WindowEvent.WINDOW_STATE_CHANGED:
                case WindowEvent.WINDOW_GAINED_FOCUS:
                    record.addItem(WindowItems.getItemForEvent(windowWatcher, (WindowEvent) e, delay));
                    return true;
            }
        } else if (e instanceof ComponentEvent) {
            if (e.getSource() instanceof Window) {
                switch(id) {
                    case ComponentEvent.COMPONENT_MOVED:
                    case ComponentEvent.COMPONENT_RESIZED:
                        record.addItem(WindowItems.getItemForEvent(windowWatcher, (ComponentEvent) e, delay));
                        return true;
                }
            }
        }
        return false;
    }

    protected boolean keyEventMatched(AWTEvent e, long delay) {
        if (!(e instanceof KeyEvent)) return false;
        KeyEvent ke = (KeyEvent) e;
        Component c = ke.getComponent();
        record.addItem(new KeyItem(ke, getWindowID(c), delay, componentIdentifierFactory));
        return true;
    }

    protected boolean inputMethodEventMatched(AWTEvent e, long delay) {
        if (!(e instanceof InputMethodEvent)) return false;
        InputMethodEvent ime = (InputMethodEvent) e;
        if (ime.getCommittedCharacterCount() <= 0) return false;
        AttributedCharacterIterator text = ime.getText();
        Component comp = (Component) ime.getSource();
        boolean delayAdded = false;
        for (char c = text.first(); c != CharacterIterator.DONE; c = text.next()) {
            long keyDelay = delayAdded ? 0 : delay;
            KeyEvent ke = new KeyEvent(comp, KeyEvent.KEY_TYPED, ime.getWhen(), 0, KeyEvent.VK_UNDEFINED, c);
            record.addItem(new KeyItem(ke, getWindowID(comp), keyDelay, componentIdentifierFactory));
            delayAdded = true;
        }
        return true;
    }

    /** See if this event is one we'd like to record, and do so if it
	 * is. **/
    protected void matchEvent(AWTEvent e) {
        VerbatimRecordingOptions options = prefs.getVerbatimRecordingOptions();
        long currentTime = System.currentTimeMillis();
        long delay = currentTime - lastTimeItemRecorded;
        if (options.doMouseInputEvents) {
            if (mouseWheelEventMatched(e, delay) || mouseEventMatched(e, delay)) lastTimeItemRecorded = currentTime;
        }
        if (options.doMouseMotionEvents) {
            if (mouseMotionEventMatched(e, delay)) lastTimeItemRecorded = currentTime;
        }
        if (options.doWindowEvents) {
            if (windowEventMatched(e, delay)) lastTimeItemRecorded = currentTime;
        }
        if (options.doKeyEvents) {
            if (keyEventMatched(e, delay) || inputMethodEventMatched(e, delay)) lastTimeItemRecorded = currentTime;
        }
    }

    /** Whether or not a drag is currently occuring. **/
    protected boolean dragging = false;

    /** Mask of buttons that were pressed when the drag started. **/
    protected int dragStartButtons = 0;

    /** Store data necessary to recover from a drag.  Swing ignores a
	 * very important mouse release. **/
    protected void startDrag(MouseEvent me) {
        dragging = true;
        dragStartButtons = me.getModifiersEx();
    }

    /** Release any mouse buttons that were not pressed at the start. **/
    protected void endDrag(MouseEvent me, long delay) {
        int modifiers = me.getModifiersEx();
        int windowID = getWindowID(me.getComponent());
        if ((dragStartButtons & MouseEvent.BUTTON1_DOWN_MASK) > 0) modifiers = addMouseReleaseEvent(MouseEvent.BUTTON1, me.getComponent(), windowID, modifiers, me.getX(), me.getY(), delay);
        if ((dragStartButtons & MouseEvent.BUTTON2_DOWN_MASK) > 0) modifiers = addMouseReleaseEvent(MouseEvent.BUTTON2, me.getComponent(), windowID, modifiers, me.getX(), me.getY(), delay);
        if ((dragStartButtons & MouseEvent.BUTTON3_DOWN_MASK) > 0) addMouseReleaseEvent(MouseEvent.BUTTON3, me.getComponent(), windowID, modifiers, me.getX(), me.getY(), delay);
        dragging = false;
        dragStartButtons = 0;
    }

    protected int addMouseReleaseEvent(int button, Component c, int windowID, int startModifiers, int x, int y, long delay) {
        int modifiers = 0;
        if (button == MouseEvent.BUTTON1) {
            modifiers = startModifiers & (~MouseEvent.BUTTON1_DOWN_MASK);
        } else if (button == MouseEvent.BUTTON2) {
            modifiers = startModifiers & (~MouseEvent.BUTTON2_DOWN_MASK);
        } else if (button == MouseEvent.BUTTON3) {
            modifiers = startModifiers & (~MouseEvent.BUTTON3_DOWN_MASK);
        }
        MouseEvent releaseEvent = new MouseEvent(c, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), modifiers, x, y, 0, false, button);
        record.addItem(new MouseClickItem(releaseEvent, windowID, delay, componentIdentifierFactory));
        return modifiers;
    }

    /** Check whether the given MouseEvent is the end of a drag.  If
	 * so call endDrag(). **/
    protected void checkIsDragEnd(MouseEvent me, long delay) {
        if (me.getID() == MouseEvent.MOUSE_MOVED) endDrag(me, delay);
    }

    /** Check whether the mouse release event should end the drag.
	 * Set 'dragging' to false if it does. **/
    protected void checkMouseReleaseEndsDrag(MouseEvent me) {
        if ((me.getButton() == MouseEvent.BUTTON1) && (me.getID() == MouseEvent.MOUSE_RELEASED)) dragging = false;
    }
}
