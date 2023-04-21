package org.nakedobjects.system.console;

import org.nakedobjects.nof.core.context.NakedObjectsContext;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.log4j.Category;

public class AWTConsole extends Frame implements ServerConsole {

    private static final Category LOG = Category.getInstance(AWTConsole.class);

    public static final String WIDTH = "nakedobjects.awt-console.width";

    public static final String HEIGHT = "nakedobjects.awt-console.height";

    public static final int DEFAULT_WIDTH = 600;

    public static final int DEFAULT_HEIGHT = 350;

    private Server server;

    private TextArea log;

    private Button quit;

    private ActionListener buttonAction;

    public AWTConsole() {
        super("Object Server Console");
        buildGUI();
        show();
    }

    /**
     * 
     */
    private void addButtons() {
        Panel p = new Panel();
        p.setLayout(new java.awt.GridLayout(1, 0, 10, 0));
        add(p, BorderLayout.SOUTH);
        Button b;
        p.add(b = new Button("Blank"));
        b.addActionListener(buttonAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                clearLog();
            }
        });
        p.add(b = new Button("Classes"));
        b.addActionListener(buttonAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                listClasses();
            }
        });
        p.add(b = new Button("Cache"));
        b.addActionListener(buttonAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                listCachedObjects();
            }
        });
        p.add(b = new Button("C/Cache"));
        b.addActionListener(buttonAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                clearCache();
            }
        });
        p.add(quit = new Button("Quit"));
        quit.addActionListener(buttonAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });
    }

    /**
     * LogWindow constructor comment.
     */
    private void buildGUI() {
        add(log = new TextArea());
        addButtons();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension();
        Insets insets = getInsets();
        frameSize.width = NakedObjectsContext.getConfiguration().getInteger(WIDTH, DEFAULT_WIDTH);
        frameSize.height = NakedObjectsContext.getConfiguration().getInteger(HEIGHT, DEFAULT_HEIGHT);
        Rectangle bounds = new Rectangle(frameSize);
        bounds.x = screenSize.width - frameSize.width - insets.right;
        bounds.y = 0 + insets.top;
        setBounds(bounds);
    }

    private void clearCache() {
        NakedObjectsContext.getObjectLoader().reset();
    }

    /**
     * 
     * @param message java.lang.String
     */
    private void clearLog() {
        log.setText("");
    }

    /**
     * 
     */
    public void close() {
        dispose();
    }

    public void init(Server server) {
        this.server = server;
        log("Console in control of " + server);
    }

    private void listCachedObjects() {
    }

    private void listClasses() {
    }

    /**
     * 
     * @param message java.lang.String
     */
    public void log() {
        log.append("\n");
    }

    public void log(String message) {
        log.append(message + '\n');
        LOG.info(message);
    }

    /**
     * 
     */
    public void quit() {
        server.shutdown();
        close();
        System.exit(0);
    }
}
