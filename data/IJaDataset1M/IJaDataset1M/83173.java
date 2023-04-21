package openfuture.bugbase.app.applet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ResourceBundle;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import openfuture.bugbase.app.view.MainAdminView;
import openfuture.bugbase.servlet.BugBaseServletClient;
import openfuture.util.error.I18NException;

/**
 * Applet for the administration of Bug Base. </ p>
 *
 *
 * Created: Tue Feb 08 06:35:56 2000
 *
 * @author Wolfgang Reissenberger
 * @version $Revision: 1.5 $
 */
public class BugBaseAdminApplet extends JApplet {

    private static String servletURL;

    private JLabel statusLabel;

    public BugBaseAdminApplet() {
    }

    public void init() {
        servletURL = getParameter("servletURL");
    }

    public void start() {
        try {
            BugBaseServletClient servletClient = new BugBaseServletClient(servletURL);
            getContentPane().setBackground(Color.white);
            MainAdminView view = new MainAdminView(servletClient);
            view.init();
            getContentPane().add(view);
        } catch (I18NException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("openfuture.bugbase.BugBaseRes_txt");
            String message;
            if (e.getDescription() != null) {
                message = e.getDescription().toString(bundle);
            } else {
                message = "Initialization failed. Can't contact servlet: " + servletURL;
            }
            JTextArea textArea = new JTextArea(message);
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(textArea, BorderLayout.CENTER);
        } catch (Exception e) {
            String text = "Initialization failed. Can't contact servlet: " + servletURL;
            text += "\n" + e.getMessage();
            JTextArea textArea = new JTextArea(text);
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(textArea, BorderLayout.CENTER);
        }
    }

    public static void main(String[] args) {
        servletURL = args[0];
        JFrame frame = new JFrame("BugBaseAdmin");
        WindowListener l = new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        frame.addWindowListener(l);
        BugBaseAdminApplet applet = new BugBaseAdminApplet();
        applet.start();
        frame.getContentPane().add("Center", applet);
        frame.setSize(700, 600);
        frame.setVisible(true);
    }
}
