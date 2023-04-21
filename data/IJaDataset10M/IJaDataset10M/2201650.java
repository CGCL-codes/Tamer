package frame;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MyMenu extends JMenuBar {

    private JMenu fileMenu = new JMenu("File");

    private JMenuItem newItem = new JMenuItem("New");

    private JMenuItem openItem = new JMenuItem("Open");

    private JMenuItem saveItem = new JMenuItem("Save");

    private JMenuItem exitItem = new JMenuItem("Exit");

    /**
     * @param args
     */
    public MyMenu() {
        newItem = new JMenuItem("New");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        this.add(fileMenu);
    }
}
