package frameworks.urbiflock.ui;

import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.BoxLayout;

/**
 * The Application Launcher UI - a grid from where the installed applications
 * can be launched.
 */
public class ApplicationLauncher extends Frame implements ActionListener {

    Application[] applications_;

    Panel applicationGridPanel_;

    public ApplicationLauncher(Application[] applications) throws Exception {
        applications_ = applications;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        GridLayout gridLayout = new GridLayout();
        gridLayout.setColumns(3);
        applicationGridPanel_ = new Panel(gridLayout);
        add(applicationGridPanel_);
        for (int i = 0; i < applications_.length; i++) {
            Application app = applications_[i];
            String appName = app.name();
            Button button = new Button(appName);
            button.setActionCommand(appName);
            button.addActionListener(this);
            applicationGridPanel_.add(button);
        }
        pack();
        setVisible(true);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
    }

    public Application findApplication(String name) throws Exception {
        Application result = null;
        for (int i = 0; i < applications_.length; i++) {
            Application app = applications_[i];
            if (app.name().equals(name)) {
                result = app;
            }
        }
        return result;
    }

    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();
        try {
            (findApplication(command)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application app1 = new Application._EmptyApp() {

            public String name() throws Exception {
                return "app1";
            }

            public void start() throws Exception {
                System.out.println("app1 started");
            }
        };
        Application app2 = new Application._EmptyApp() {

            public String name() throws Exception {
                return "app2";
            }

            public void start() throws Exception {
                System.out.println("app2 started");
            }
        };
        Application app3 = new Application._EmptyApp() {

            public String name() throws Exception {
                return "jkfkjsfbbskf dfdfj,n defn f";
            }

            public void start() throws Exception {
                System.out.println("jkfkjsfbbskf dfdfj,n defn f started");
            }
        };
        Application app4 = new Application._EmptyApp() {

            public String name() throws Exception {
                return "app4";
            }

            public void start() throws Exception {
                System.out.println("app4 started");
            }
        };
        Application app5 = new Application._EmptyApp() {

            public String name() throws Exception {
                return "app5";
            }

            public void start() throws Exception {
                System.out.println("app5 started");
            }
        };
        Vector apps = new Vector();
        apps.add(app1);
        apps.add(app2);
        apps.add(app3);
        apps.add(app4);
        apps.add(app5);
        try {
            ApplicationLauncher launcher = new ApplicationLauncher((Application[]) apps.toArray(new Application[apps.size()]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
