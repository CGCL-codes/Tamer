package nu.lazy8.util.help;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JTextField;
import nu.lazy8.ledger.main.Lazy8Ledger;

/**
 *  Description of the Class
 *
 * @author     Administrator
 * @created    den 8 mars 2002
 */
public class HelpedTextField extends JTextField {

    private String helpField;

    private String helpName;

    private JFrame view;

    /**
   *  Constructor for the HelpedTextField object
   *
   * @param  helpFieldin   Description of the Parameter
   * @param  helpNamein    Description of the Parameter
   * @param  view          Description of the Parameter
   */
    public HelpedTextField(String helpFieldin, String helpNamein, JFrame view) {
        helpName = helpNamein;
        this.view = view;
        helpField = helpFieldin;
        MouseListener l = new MouseListener() {

            public void mouseClicked(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
                showHelp();
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }
        };
        addMouseListener(l);
        KeyListener keylst = new KeyListener() {

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    gotEnter();
                }
            }

            public void keyTyped(KeyEvent e) {
            }
        };
        addKeyListener(keylst);
    }

    /**
   *  Description of the Method
   */
    public void gotEnter() {
    }

    /**
   *  Constructor for the showHelp object
   */
    public void showHelp() {
        Lazy8Ledger.ShowContextHelp(view, helpName, helpField);
    }
}
