package net.wotonomy.test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import net.wotonomy.control.EOEditingContext;
import net.wotonomy.control.EOObjectStore;

/**
* A simple test-bed for wotonomy.
* Shows a JFrame containing the TestPanel
* which is controlled by the TestController.
*/
public class Test {

    static EOObjectStore objectStore;

    static EOEditingContext editingContext;

    public static void main(String[] argv) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
        }
        for (int i = 0; i < argv.length; i++) {
            if (argv[i].indexOf("monitor") != -1) {
                new net.wotonomy.ui.swing.NotificationInspector();
            }
        }
        new net.wotonomy.ui.swing.NotificationInspector();
        objectStore = new DataObjectStore("data");
        editingContext = new EOEditingContext(objectStore);
        TestPanel testPanel = new TestPanel();
        final TestController controller = new TestController(testPanel);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JMenu menu;
        JMenuItem menuItem;
        JMenuBar menuBar = new JMenuBar();
        menu = new JMenu("File");
        menu.add("New");
        menuItem = new JMenuItem("Save");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                controller.displayGroup.dataSource().editingContext().saveChanges();
            }
        });
        menu.add(menuItem);
        menu.add("Close");
        menuBar.add(menu);
        menu = new JMenu("Edit");
        menu.add("Cut");
        menu.add("Copy");
        menu.add("Paste");
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(testPanel, BorderLayout.CENTER);
        frame.setTitle("Test Frame");
        frame.setBounds(50, 50, 750, 500);
        frame.show();
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
