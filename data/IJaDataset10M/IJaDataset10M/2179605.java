package net.sourceforge.obexftpfrontend.gui.action;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;

/**
 * Action that is responsible to quit the application.
 * @author Daniel F. Martins
 */
public class QuitApplicationAction extends DefaultAction {

    /** Logger. */
    private Logger log = Logger.getLogger(QuitApplicationAction.class);

    /**
     * Create a new instance of ExitApplicationAction.
     * @param mainFrame Application's main frame.
     */
    public QuitApplicationAction(Window mainFrame) {
        super(mainFrame);
        putValue(NAME, "Quit");
        putValue(SHORT_DESCRIPTION, "Close this application");
        putValue(MNEMONIC_KEY, KeyEvent.VK_X);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/icon/exit.png")));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        log.debug("Sending a window closing event to the registered window listeners");
        WindowEvent windowEvent = new WindowEvent(getParentWindow(), WindowEvent.WINDOW_CLOSING);
        for (WindowListener listener : getParentWindow().getWindowListeners()) {
            listener.windowClosing(windowEvent);
        }
    }
}
