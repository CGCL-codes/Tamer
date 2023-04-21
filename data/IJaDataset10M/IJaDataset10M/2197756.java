package sun.awt.qt;

import java.awt.*;
import sun.awt.peer.*;
import java.awt.event.*;

/**
 *
 *
 * @author Nicholas Allen
 */
class QtTextFieldPeer extends QtTextComponentPeer implements TextFieldPeer {

    private static native void initIDs();

    static {
        initIDs();
    }

    private static final int padding = 10;

    /** Creates a new QtTextFieldPeer. */
    QtTextFieldPeer(QtToolkit toolkit, TextField target) {
        super(toolkit, target);
        setEchoChar(target.getEchoChar());
    }

    protected native void create(QtComponentPeer parentPeer);

    public native void setEchoChar(char echoChar);

    public Dimension getPreferredSize(int columns) {
        FontMetrics fm = getFontMetrics(target.getFont());
        return new Dimension(fm.charWidth('0') * columns + 20, fm.getMaxDescent() + fm.getMaxAscent() + padding);
    }

    public Dimension getMinimumSize(int columns) {
        return getPreferredSize(columns);
    }

    /**
     * DEPRECATED:  Replaced by setEchoChar(char echoChar).
     */
    public void setEchoCharacter(char c) {
        setEchoChar(c);
    }

    /**
     * DEPRECATED:  Replaced by getPreferredSize(int).
     */
    public Dimension preferredSize(int cols) {
        return getPreferredSize(cols);
    }

    /**
     * DEPRECATED:  Replaced by getMinimumSize(int).
     */
    public Dimension minimumSize(int cols) {
        return getMinimumSize(cols);
    }

    private void postActionEvent() {
        QtToolkit.postEvent(new ActionEvent(target, ActionEvent.ACTION_PERFORMED, getText()));
    }

    public boolean isFocusable() {
        return true;
    }
}
