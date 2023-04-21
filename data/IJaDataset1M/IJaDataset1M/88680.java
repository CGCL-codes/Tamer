package net.sourceforge.timetrack.utils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;

/**
 * Override the method <tt>isValidCharacter</tt> to tell the filter to accept
 * or not the character
 * 
 * @author sfbouchard
 * @version $Revision: 1.1 $, $Date: 2006/03/27 01:58:17 $
 */
public abstract class KeyFilter extends KeyAdapter {

    JComponent comp;

    public KeyFilter(JComponent comp) {
        this.comp = comp;
    }

    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (((c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_ENTER)) && isControlAccepted()) return;
        if (!isValidCharacter(c)) {
            comp.getToolkit().beep();
            e.consume();
        }
    }

    /**
     * Overrite to accept or return the character in the JComponement.
     * 
     * @param ch the char to test
     * @return true if the character can be accepted
     */
    public abstract boolean isValidCharacter(char ch);

    /**
     * tell to accept backspace,enter,delete
     * 
     * @return defaults return true
     */
    public boolean isControlAccepted() {
        return true;
    }
}
