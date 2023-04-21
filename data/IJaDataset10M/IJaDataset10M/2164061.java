package frost.components;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.logging.*;
import javax.swing.JFrame;

public class BrowserFrame extends JFrame {

    private static final Logger logger = Logger.getLogger(BrowserFrame.class.getName());

    boolean plugin;

    Browser browser = new Browser(this);

    private void Init() throws Exception {
        this.setTitle("Experimental Freenet Browser");
        this.setSize(new Dimension(780, 550));
        this.setResizable(true);
        browser.setPreferredSize(new Dimension(780, 550));
        this.getContentPane().add(browser);
    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            dispose();
            if (!plugin) System.exit(0);
        }
        super.processWindowEvent(e);
    }

    /**Constructor*/
    public BrowserFrame(boolean plugin) {
        this.plugin = plugin;
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            Init();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception thrown in constructor", e);
        }
        pack();
    }
}
