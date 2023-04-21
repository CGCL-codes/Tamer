package net.sourceforge.javacavemaps.core.view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import net.sourceforge.javacavemaps.utilities.configurations.GlobalProperties;

public class CalculatorMenuBar extends JMenuBar {

    public final JMenuBar menuBar = new JMenuBar();

    public CalculatorMenuBar() {
        super();
        buildMenus();
    }

    private JMenuItem workingDirItem;

    private void buildMenus() {
        menuBar.removeAll();
        JMenu menu;
        {
            menu = new JMenu("File");
            menuBar.add(menu);
            JMenuItem item = new JMenuItem("Exit");
            menu.add(item);
            menuBar.add(menu);
        }
        if (GlobalProperties.isWebStart()) {
            menu = new JMenu("Preferences");
            menuBar.add(menu);
            workingDirItem = new JMenuItem("Working Directory: " + GlobalProperties.getWorkingDirectory().getPath());
            menu.add(workingDirItem);
            menuBar.add(menu);
        }
        {
            menu = new JMenu("View");
            JMenuItem item = new JMenuItem("Isometrics");
            menu.add(item);
            menuBar.add(menu);
            item = new JMenuItem("Command Panel");
            menu.add(item);
            menuBar.add(menu);
            item = new JMenuItem("Command2 Panel");
            menu.add(item);
            item = new JMenuItem("Command3 Panel");
            menu.add(item);
            menuBar.add(menu);
        }
    }

    public void updatePreferences() {
        workingDirItem.setText("Working Directory: " + GlobalProperties.getWorkingDirectory().getPath());
    }

    /** 
     * Return the string describing the object state  
     */
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        final String NEW_LINE = System.getProperty("line.separator");
        strBuilder.append(super.toString());
        strBuilder.append("menuBar:" + this.menuBar.toString() + NEW_LINE);
        strBuilder.append("workingDirItem:" + this.workingDirItem.toString() + NEW_LINE);
        return strBuilder.toString();
    }
}
