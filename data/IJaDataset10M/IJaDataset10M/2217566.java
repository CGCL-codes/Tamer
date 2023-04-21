package org.digitall.lib.components;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;
import org.digitall.lib.data.listeners.intListen;

public class JIntEntry extends JEntry {

    public JIntEntry() {
        super(new DecimalFormat("#,##0", new DecimalFormatSymbols(new Locale("es", "AR"))));
        super.setValue(0);
        try {
            NumberFormatter nfl = (NumberFormatter) getFormatter();
            nfl.setOverwriteMode(true);
            nfl.setAllowsInvalid(false);
            addKeyListener(new KeyListener() {

                public void keyTyped(KeyEvent keyEvent) {
                    typeKey(keyEvent);
                }

                public void keyPressed(KeyEvent keyEvent) {
                    pressKey(keyEvent);
                }

                public void keyReleased(KeyEvent keyEvent) {
                    releaseKey(keyEvent);
                }
            });
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        addKeyListener(new intListen());
        super.setHorizontalAlignment(SwingConstants.RIGHT);
    }

    private void typeKey(KeyEvent _keyEvent) {
    }

    private void pressKey(KeyEvent _keyEvent) {
        if (isEditable()) {
            switch(_keyEvent.getKeyCode()) {
                case KeyEvent.VK_HOME:
                    _keyEvent.consume();
                    setCaretPosition(0);
                    break;
                case KeyEvent.VK_BACK_SPACE:
                case KeyEvent.VK_DELETE:
                    _keyEvent.consume();
                    setValue(0);
                    break;
            }
        }
    }

    private void releaseKey(KeyEvent _keyEvent) {
        switch(_keyEvent.getKeyCode()) {
            case KeyEvent.VK_HOME:
                _keyEvent.consume();
                setCaretPosition(0);
                break;
        }
    }

    public void setTextColor(Color _color) {
        this.setForeground(_color);
    }

    public int getAmount() {
        return new Integer(getValue().toString());
    }
}
