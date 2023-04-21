package edu.xtec.adapter.test.tester;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import javax.swing.JFrame;
import edu.xtec.adapter.Adapter;
import edu.xtec.adapter.impl.PropertiesUtils;

/**
 * Stand alone application used to thest the applets and the interaction with
 * the {@link edu.xtec.adapter.Adapter}.
 */
public class AppletTester extends JFrame implements ActionListener {

    /**
   * The applet that we are testing.
   */
    Applet applet;

    /**
   * Parameters of the applet.
   */
    Properties appletProps = new Properties();

    /**
   * Adapter properties.
   */
    Properties adapterProps = new Properties();

    Class mainClass = edu.xtec.adapter.test.drawactivity.MainApplet.class;

    /**
   * Static main method
   * @param args
   */
    public static void main(String args[]) {
        new AppletTester().run();
    }

    /**
   * Start of the application.
   */
    public void run() {
        init();
        setSize(800, 600);
        setVisible(true);
        reloadApplet();
    }

    /**
   * Reloads the applet and ensures that the state is kept.
   */
    public void reloadApplet() {
        if (applet != null) this.remove(applet);
        try {
            applet = (Applet) mainClass.newInstance();
        } catch (Exception e) {
            throw new Error(e);
        }
        applet.setStub(new MyAppletStub());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.add(applet, BorderLayout.CENTER);
        validate();
        applet.init();
        applet.start();
        validate();
    }

    /**
   * Create user interface items.
   */
    public void init() {
        MenuBar mb = new MenuBar();
        Menu m = new Menu("Tools");
        MenuItem mi;
        m.addActionListener(this);
        m.add("Design");
        m.add("No design");
        m.add("Reload");
        mb.add(m);
        this.setMenuBar(mb);
    }

    /**
   * Event handler.
   * @param e
   */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Design")) {
            saveState();
            adapterProps.setProperty("userMode", "design");
            reloadApplet();
        }
        if (cmd.equals("No design")) {
            saveState();
            adapterProps.setProperty("userMode", "user");
            reloadApplet();
        }
        if (cmd.equals("Reload")) {
            saveState();
            reloadApplet();
        }
    }

    /**
   * Saves the state of the applet into <code>adapterProps</code>.
   */
    public void saveState() {
        Adapter adapter = getAdapter(applet);
        String str = adapter.doExit();
        System.out.println("doExit=" + str);
        Properties prop = PropertiesUtils.decode(str);
        Enumeration e = prop.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = prop.getProperty(key);
            System.out.println(key + "=" + value);
        }
        String state = prop.getProperty("state");
        System.out.println(state);
        adapterProps.put("state", state);
    }

    /**
   * Calls the <code>getAdapter</code> method of the applet.
   * @param applet
   * @return
   */
    protected Adapter getAdapter(Applet applet) {
        Method method;
        try {
            method = applet.getClass().getMethod("getAdapter", new Class[] {});
            return (Adapter) method.invoke(applet, new Object[] {});
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /**
   * {@link java.applet.AppletStub} implementation.
   */
    class MyAppletStub implements AppletStub {

        public boolean isActive() {
            return false;
        }

        public URL getDocumentBase() {
            return null;
        }

        public URL getCodeBase() {
            return null;
        }

        public String getParameter(String name) {
            if (name.equals("edu.xtec.adapter.parameters")) {
                return PropertiesUtils.encode(adapterProps);
            }
            return appletProps.getProperty(name);
        }

        public AppletContext getAppletContext() {
            return null;
        }

        public void appletResize(int width, int height) {
        }
    }
}
