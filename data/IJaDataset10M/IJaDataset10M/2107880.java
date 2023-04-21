package net.sourceforge.hatbox.ui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

public class HatboxUI extends JFrame {

    private HatboxUI(String title) {
        super(title);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
        }
        Controller controller = new Controller();
        HatboxUI ui = new HatboxUI("Hatbox UI");
        ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ui.setSize(800, 600);
        JTabbedPane tabs = new JTabbedPane();
        controller.setTabs(tabs);
        tabs.add("Connection", new ConnPanel(controller));
        tabs.add("Tables", new TablesSplitPane(controller));
        tabs.add("Table", new TablePanel(controller));
        tabs.add("Index", new IndexSplitPane(controller));
        LogPanel logPanel = new LogPanel();
        controller.setLogPanel(logPanel);
        tabs.add("Log", logPanel);
        tabs.addChangeListener(controller);
        ui.add(tabs);
        ui.setVisible(true);
    }
}
