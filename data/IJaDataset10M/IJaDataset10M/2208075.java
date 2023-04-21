package pl.msystems.mqprobe.gui;

import java.awt.Component;
import javax.swing.JTabbedPane;

/**
 * Main Tabbed Pane of the application
 * @author Marek Godlewski
 *
 */
public class AppTabbedPane extends JTabbedPane {

    /**
	 * Active instance of AppTabbedPane.
	 */
    private static AppTabbedPane activeInstance;

    /**
	 * Constructor is private. Use <code>getActiveInstance()</code> to obtain 
	 * active instance of AppTabbedPane.
	 */
    private AppTabbedPane() {
        super(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
        activeInstance = this;
        add("New Connection", new ConnectionPanel());
    }

    /**
	 * Method implementation.
	 * @param title
	 * @param component
	 * @return
	 * @see javax.swing.JTabbedPane#add(java.lang.String, java.awt.Component)
	 */
    @Override
    public Component add(String title, Component component) {
        super.add(title, component);
        setSelectedComponent(component);
        return component;
    }

    /**
	 * Returns active instance of AppTabbedPane.
	 * @return active instance of AppTabbedPane
	 */
    public static AppTabbedPane getActiveInstance() {
        return (activeInstance != null) ? activeInstance : new AppTabbedPane();
    }
}
