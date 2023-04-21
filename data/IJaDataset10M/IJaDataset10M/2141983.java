package sun.awt.qt;

import java.security.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import sun.awt.peer.*;

/**
 *
 *
 * @author Nicholas Allen
 */
abstract class QtTextComponentPeer extends QtComponentPeer implements TextComponentPeer {

    private static native void initIDs();

    private QtClipboard systemClipboard;

    private int pasteStart, pasteEnd;

    static {
        initIDs();
    }

    /** Creates a new QtTextComponentPeer. */
    QtTextComponentPeer(QtToolkit toolkit, TextComponent target) {
        super(toolkit, target);
        setText(target.getText());
        setEditable(target.isEditable());
        target.enableInputMethods(true);
        final QtToolkit tmpToolkit = toolkit;
        AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                systemClipboard = (QtClipboard) (tmpToolkit.getSystemClipboard());
                return null;
            }
        });
    }

    public boolean isFocusTraversable() {
        return true;
    }

    public native void setEditable(boolean editable);

    protected native String getTextNative();

    public String getText() {
        return getTextNative();
    }

    protected native void setTextNative(String text);

    public void setText(String text) {
        if (text != null) if ((text.length() > 0) || (getText().length() > 0)) setTextNative(text);
    }

    public Dimension getPreferredSize(int rows, int columns) {
        return getMinimumSize(rows, columns);
    }

    public Dimension getMinimumSize(int rows, int columns) {
        FontMetrics fm = getFontMetrics(target.getFont());
        int colWidth = fm.charWidth('0');
        int rowHeight = fm.getHeight();
        return new Dimension((columns + 1) * colWidth, (rows + 1) * rowHeight);
    }

    public native int getSelectionStart();

    public native int getSelectionEnd();

    public native void select(int selStart, int selEnd);

    public native void setCaretPosition(int pos);

    public native int getCaretPosition();

    private void postTextEvent() {
        QtToolkit.postEvent(new TextEvent(target, TextEvent.TEXT_VALUE_CHANGED));
    }

    void pasteData(byte[] data) {
        String text = getText();
        int textLen = text.length();
        if (pasteEnd > textLen) pasteEnd = textLen;
        if (pasteStart > pasteEnd) pasteStart = pasteEnd;
        String preText = text.substring(0, pasteStart);
        String postText = text.substring(pasteEnd, textLen);
        text = preText + new String(data) + postText;
        setText(text);
        setCaretPosition(text.length() - postText.length());
    }
}
