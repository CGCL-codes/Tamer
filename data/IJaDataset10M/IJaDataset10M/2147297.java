package gpsxml.gui;

import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.apache.log4j.Logger;

/**
 *  Base class for a number of dialogs: ServerAuthenticationDialog, 
 *  DBAuthenticationDialog, ServernameInputdialog and ExporterAuthenticationDialog.
 * @author kbt  Axiohelix Pvt. Ltd.
 */
public abstract class AbstractDialog extends JDialog implements ActionListener, WindowListener {

    static Logger logger = Logger.getLogger(gpsxml.gui.AbstractDialog.class.getName());

    protected final String HTMLSTARTTAG = "<html>";

    protected final String HTMLENDTAG = "</html>";

    protected final String BASESTARTFONT = "<font face=\"Verdana\" size=\"3\">";

    protected final String BASEENDFONT = "</font>";

    protected final String COMMENTSTARTFONT = "<font size=\"2\"><i>";

    protected final String COMMENTENDFONT = "</i></font>";

    protected final String REDFONTSTART = "<font color=\"red\"><b>";

    protected final String REDFONTEND = "</b></font>";

    protected final String BLUEFONTSTART = "<font color=\"blue\"><b>";

    protected final String BLUEFONTEND = "</b></font>";

    protected final String GREENFONTSTART = "<font color=\"green\"><b>";

    protected final String GREENFONTEND = "</b></font>";

    private final int MINPOSX = 5;

    private final int MINPOSY = 5;

    protected JFrame parentFrame;

    /** Creates a new instance of AbstractDialog */
    public AbstractDialog() {
        this(new JFrame(), "Empty Dialog");
    }

    /** Creates a new instance of AbstractDialog */
    public AbstractDialog(JFrame parentFrame, String title) {
        super(parentFrame, title, true);
        addWindowListener(this);
        this.parentFrame = parentFrame;
    }

    protected void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            logger.debug("Couldn't initialize Look and Feel");
        }
    }

    public void publish() {
        this.pack();
        int x = parentFrame.getLocationOnScreen().x + parentFrame.getSize().width / 2 - this.getSize().width / 2;
        x = x < MINPOSX ? MINPOSX : x;
        int y = parentFrame.getLocationOnScreen().y + parentFrame.getSize().height / 2 - this.getSize().height / 2;
        y = y < MINPOSY ? MINPOSY : x;
        this.setLocation(x, y);
        this.setVisible(true);
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
}
