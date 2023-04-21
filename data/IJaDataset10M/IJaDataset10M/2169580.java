package sun.awt.pocketpc;

import java.awt.*;
import sun.awt.peer.*;
import java.awt.event.*;

class PPCTextFieldPeer extends PPCTextComponentPeer implements TextFieldPeer {

    private static native void initIDs();

    static {
        initIDs();
    }

    /** Creates a new PPCTextFieldPeer. */
    PPCTextFieldPeer(TextField target) {
        super(target);
    }

    public Dimension getMinimumSize() {
        FontMetrics fm = getFontMetrics(((TextField) target).getFont());
        return new Dimension(fm.stringWidth(getText()) + 24, fm.getHeight() + 8);
    }

    public void setEchoChar(char c) {
        setEchoCharacter(c);
    }

    public Dimension getPreferredSize(int cols) {
        return getMinimumSize(cols);
    }

    public Dimension getMinimumSize(int cols) {
        FontMetrics fm = getFontMetrics(((TextField) target).getFont());
        return new Dimension(fm.charWidth('0') * cols + 24, fm.getHeight() + 8);
    }

    native void create(PPCComponentPeer parent);

    void initialize() {
        TextField tf = (TextField) target;
        if (tf.echoCharIsSet()) {
            setEchoChar(tf.getEchoChar());
        }
        super.initialize();
    }

    void handleAction() {
        PPCToolkit.postEvent(new ActionEvent(target, ActionEvent.ACTION_PERFORMED, getText()));
    }

    /**
     * DEPRECATED but, for now, called by setEchoChar(char).
     */
    public native void setEchoCharacter(char c);

    /**
     * DEPRECATED
     */
    public Dimension minimumSize() {
        return getMinimumSize();
    }

    /**
     * DEPRECATED
     */
    public Dimension minimumSize(int cols) {
        return getMinimumSize(cols);
    }

    /**
     * DEPRECATED
     */
    public Dimension preferredSize(int cols) {
        return getPreferredSize(cols);
    }
}
