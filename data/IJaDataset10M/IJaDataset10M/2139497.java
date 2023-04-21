package calclipse.core.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import calclipse.lib.lcd.display.Display;
import calclipse.lib.lcd.display.DisplayScrollPane;

public class GUITestApp {

    private final Doc testDoc = new TestDoc();

    public static class TestDoc implements Doc {

        private final Display display;

        private final DisplayScrollPane scrollPane;

        private final JMenuBar menuBar;

        public TestDoc() {
            display = new Display();
            scrollPane = new DisplayScrollPane(display);
            menuBar = new JMenuBar();
            final JMenu menu = new JMenu("Test");
            final JMenuItem item = new JMenuItem("Test 2");
            menu.add(item);
            menuBar.add(menu);
        }

        @Override
        public Component getComponent() {
            return scrollPane;
        }

        @Override
        public Component getFocusComponent() {
            return display;
        }

        @Override
        public void winCreated(final Win win) {
            win.setClosable(true);
            win.setMaximizable(true);
            win.setIconifiable(true);
            win.setResizable(true);
            final JToolBar toolBar = new JToolBar();
            toolBar.add(new JButton("Test"));
            win.setJToolBar(toolBar);
            win.setJMenuBar(menuBar);
            win.pack();
        }
    }

    public GUITestApp() {
        new ControlPanel();
    }

    public void run() {
        final WinManager manager = GUI.getGUI().getWinManager();
        final Win win = manager.getWin(testDoc);
        win.setVisible(true);
    }

    public static void main(final String[] args) {
        new GUITestApp();
    }

    private class ControlPanel extends JPanel implements ActionListener {

        private static final long serialVersionUID = 1L;

        private final JButton run = new JButton("Run");

        private final JButton uninstall = new JButton("Uninstall");

        public ControlPanel() {
            add(run);
            add(uninstall);
            run.addActionListener(this);
            uninstall.addActionListener(this);
            final JFrame f = new JFrame();
            f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            f.getContentPane().add(this);
            f.pack();
            f.setLocation(500, 0);
            f.setVisible(true);
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getSource() == run) {
                run();
            } else if (e.getSource() == uninstall) {
                GUI.setGUI(null);
            }
        }
    }
}
