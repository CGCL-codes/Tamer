package GUI.utils;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

/**
 * This is a window which always appears on top of the other windows.
 * <p/>
 * Project rule-editor <br/>
 * AllwaysOnTop.java created 5 april 2007, 16:44
 * <p/>
 * Copyright &copy 2006 SemLab
 * @author <a href="mailto:info@jborsje.nl">Jethro Borsje</a>
 * @version $$Revision:$$, $$Date:$$
 */
public class AllwaysOnTop extends JFrame implements WindowListener {

    public AllwaysOnTop(String p_title) {
        super(p_title);
        addWindowListener(this);
    }

    public void windowOpened(WindowEvent event) {
    }

    ;

    public void windowActivated(WindowEvent event) {
    }

    ;

    public void windowDeactivated(WindowEvent event) {
        toFront();
    }

    public void windowIconified(WindowEvent event) {
    }

    ;

    public void windowDeiconified(WindowEvent event) {
    }

    ;

    public void windowClosed(WindowEvent event) {
    }

    ;

    public void windowClosing(WindowEvent event) {
    }

    ;
}
