package polr.client.ui.base;

import org.newdawn.slick.Input;
import org.newdawn.slick.gui.GUIContext;
import polr.client.ui.base.event.*;
import polr.client.ui.base.skin.ComponentAppearance;
import polr.client.ui.base.skin.TextComponentAppearance;

/**
 * 
 * @author davedes
 */
public abstract class TextComponent extends Container {

    private String text = null;

    private boolean editable = true;

    private int caretPos = 0;

    protected KeyListener keyListener = new TextKeyListener();

    protected MouseListener mouseListener = new TextMouseListener();

    private int maxChars = Integer.MAX_VALUE;

    public static final int STANDARD_INITIAL_REPEAT_DELAY = 400;

    public static final int STANDARD_REPEAT_DELAY = 50;

    private static boolean defaultRepeatEnabled = true;

    private static int defaultRepeatDelay = STANDARD_REPEAT_DELAY;

    private static int defaultInitialRepeatDelay = STANDARD_INITIAL_REPEAT_DELAY;

    private Timer keyRepeats = new Timer(defaultRepeatDelay);

    private boolean keyRepeating = defaultRepeatEnabled;

    protected static final String COL_CHAR = "w";

    /**
	 * Creates a new instance of TextComponent. By default, no call to
	 * updateAppearance is made in the construction of this component.
	 */
    public TextComponent() {
        super(false);
        addKeyListener(keyListener);
        addMouseListener(mouseListener);
        setFocusable(true);
        keyRepeats.setInitialDelay(defaultInitialRepeatDelay);
        keyRepeats.setRepeats(true);
    }

    /**
	 * Sets the appearance for this text component. If <code>appearance</code>
	 * is not an instance of TextComponentAppearance, an
	 * <code>IllegalArgumentException</code> is thrown.
	 * 
	 * @param appearance
	 *            the new appearance to set
	 */
    public void setAppearance(ComponentAppearance appearance) {
        if (!(appearance instanceof TextComponentAppearance)) throw new IllegalArgumentException("must pass instance of text component appearance");
        super.setAppearance(appearance);
    }

    public int viewToModel(float x, float y) {
        TextComponentAppearance appearance = (TextComponentAppearance) getAppearance();
        if (appearance != null) {
            return appearance.viewToModel(this, x, y);
        } else return -1;
    }

    public Point modelToView(int pos) {
        TextComponentAppearance appearance = (TextComponentAppearance) getAppearance();
        if (appearance != null) {
            return appearance.modelToView(this, pos);
        } else return null;
    }

    public int getCaretPosition() {
        return caretPos;
    }

    public void setCaretPosition(int caretPos) {
        int old = this.caretPos;
        this.caretPos = caretPos;
        if (old != caretPos) caretPositionChanged(old);
    }

    public String getText() {
        if (text == null) text = "";
        return text;
    }

    public void setText(String text) {
        String old = this.text;
        this.text = text;
        if (this.text == null) this.text = "";
        caretPos = this.text.length();
        if (old != text) {
            fireStateChanged();
            textChanged(old);
        }
    }

    /**
	 * Allows subclasses to tap into changed events directly without the need
	 * for listeners.
	 * 
	 * @param oldText
	 *            the previous value of the text, before it was changed
	 */
    protected void textChanged(String oldText) {
    }

    protected void caretPositionChanged(int old) {
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        setFocusable(editable);
    }

    public boolean isEditable() {
        return editable;
    }

    public int getMaxChars() {
        return maxChars;
    }

    public void setMaxChars(int maxChars) {
        this.maxChars = maxChars;
    }

    public void updateComponent(GUIContext ctx, int delta) {
        super.updateComponent(ctx, delta);
        if (keyRepeating) {
            keyRepeats.update(ctx, delta);
            if (keyRepeats.isAction()) {
                doRepeat();
            }
        }
    }

    /**
	 * Provides a hint for the creation of new text components to enable key
	 * repeat with the specified delays. This does not affect any existing
	 * instances of TextComponent. If individual text component timing is
	 * desired, use the instance methods.
	 * 
	 * @param initialDelay
	 *            the default initial delay for new text components to use
	 * @param delay
	 *            the default delay for new text components to use
	 */
    public static void enableDefaultKeyRepeat(int initialDelay, int delay) {
        defaultRepeatEnabled = true;
        defaultInitialRepeatDelay = initialDelay;
        defaultRepeatDelay = delay;
    }

    /**
	 * Provides a hint for the creation of new text components to enable key
	 * repeat with the last used delays. This does not affect any existing
	 * instances of TextComponent. If individual text component timing is
	 * desired, use the instance methods. If no delays were last specified in
	 * enableDefaultKeyRepeat, the delays will be STANDARD_INITIAL_REPEAT_DELAY
	 * and STANDARD_REPEAT_DELAY.
	 */
    public static void enableDefaultKeyRepeat() {
        defaultRepeatEnabled = true;
    }

    /**
	 * Provides a hint for the creation of new text components to disable key
	 * repeat. This does not affect any existing instances of TextComponent. If
	 * individual text component timing is desired, use the instance methods.
	 */
    public static void disableDefaultKeyRepeat() {
        defaultRepeatEnabled = false;
    }

    /**
	 * Returns <tt>true</tt> if new text component instances use key repeating.
	 * 
	 * @return <tt>true</tt> if new instances have key repeat enabled,
	 *         <tt>false</tt> otherwise
	 */
    public static boolean isDefaultKeyRepeatEnabled() {
        return defaultRepeatEnabled;
    }

    /**
	 * Enables key repeating on this text component instance with the specified
	 * delays. If a "global" or default setting is desired, use the static
	 * methods instead.
	 * <p>
	 * If the key repeats timer is currently running, it will be restarted to
	 * reflect the new delays.
	 * 
	 * @param initialDelay
	 *            the initial delay before repeating keys
	 * @param delay
	 *            the delay between each key repeat
	 */
    public void enableKeyRepeat(int initialDelay, int delay) {
        keyRepeating = true;
        keyRepeats.setDelay(initialDelay);
        keyRepeats.setInitialDelay(delay);
        if (keyRepeats.isRunning()) keyRepeats.restart();
    }

    /**
	 * Enables key repeating on this text component instance with the last used
	 * delays. If a "global" or default setting is desired, use the static
	 * methods instead. If no delays were last specified in enableKeyRepeat, the
	 * delays will be STANDARD_INITIAL_REPEAT_DELAY and STANDARD_REPEAT_DELAY.
	 * <p>
	 * If the key repeats timer is already running, it will <i>not</i> be
	 * restarted.
	 */
    public void enableKeyRepeat() {
        keyRepeating = true;
    }

    /**
	 * Disables key repeating on this text component instance. If a "global" or
	 * default setting is desired, use the static methods instead.
	 * <p>
	 * If the key repeats timer is currently running, it will be stopped.
	 */
    public void disableKeyRepeat() {
        keyRepeating = false;
        keyRepeats.stop();
    }

    /**
	 * Returns <tt>true</tt> if this text component instance has key repeating
	 * enabled.
	 * 
	 * @return <tt>true</tt> if key repeating is enabled on this component,
	 *         <tt>false</tt> otherwise
	 */
    public boolean isKeyRepeatEnabled() {
        return keyRepeating;
    }

    /**
	 * Adds the specified listener to the list.
	 * 
	 * @param s
	 *            the listener to receive events
	 */
    public synchronized void addChangeListener(ChangeListener s) {
        listenerList.add(ChangeListener.class, s);
    }

    /**
	 * Removes the specified listener from the list.
	 * 
	 * @param s
	 *            the listener to remove
	 */
    public synchronized void removeChangeListener(ChangeListener s) {
        listenerList.remove(ChangeListener.class, s);
    }

    /**
	 * Fires a change event to all action listeners in this component.
	 * 
	 * 
	 * @see polr.client.ui.base.event.ChangeEvent
	 */
    protected void fireStateChanged() {
        ChangeEvent evt = null;
        final ChangeListener[] listeners = (ChangeListener[]) listenerList.getListeners(ChangeListener.class);
        for (int i = 0; i < listeners.length; i++) {
            if (evt == null) {
                evt = new ChangeEvent(this);
            }
            listeners[i].stateChanged(evt);
        }
    }

    private int key;

    private char c;

    private void doRepeat() {
        if (!isEditable()) return;
        if (text == null) text = "";
        String oldText = text;
        int oldCaret = caretPos;
        if ((c < 127) && (c > 31) && (text.length() < getMaxChars())) {
            if (caretPos < text.length()) {
                text = text.substring(0, caretPos) + c + text.substring(caretPos);
            } else {
                text = text.substring(0, caretPos) + c;
            }
            caretPos++;
        } else if (key == Input.KEY_LEFT) {
            if (caretPos > 0) caretPos--;
        } else if (key == Input.KEY_RIGHT) {
            if (caretPos < text.length()) caretPos++;
        } else if (key == Input.KEY_BACK) {
            if ((caretPos > 0) && (text.length() > 0)) {
                if (caretPos < text.length()) text = text.substring(0, caretPos - 1) + text.substring(caretPos); else text = text.substring(0, caretPos - 1);
                caretPos--;
            }
        } else if (key == Input.KEY_DELETE) {
            if (caretPos < text.length()) {
                text = text.substring(0, caretPos) + text.substring(caretPos + 1);
            }
        }
        doRepeatImpl(key, c);
        if (oldText != text) {
            textChanged(oldText);
            fireStateChanged();
        }
        if (oldCaret != caretPos) {
            caretPositionChanged(oldCaret);
        }
    }

    protected void doRepeatImpl(int key, char c) {
    }

    protected class TextKeyListener extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            key = e.getKeyCode();
            c = e.getKeyChar();
            doRepeat();
            if (keyRepeating) {
                keyRepeats.restart();
            }
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == key) {
                keyRepeats.stop();
            }
        }
    }

    protected class TextMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            if (!isEditable()) return;
            int pos = viewToModel(e.getX(), e.getY());
            if (pos >= 0) {
                setCaretPosition(pos);
            }
        }
    }
}
