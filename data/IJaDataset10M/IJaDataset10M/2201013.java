package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

public class SimpleSwing extends JFrame {

    public static final String DEFAULT_PATH = "file://./latin_text.txt";

    private JButton readRequestsButton;

    private JMenuItem newMenuItem, openMenuItem, exitMenuItem;

    private JTextField pathField;

    private JTextArea contentArea;

    private Thread loadFileActionThread;

    /**
	 * Default constructor.
	 */
    public SimpleSwing() {
        setNativeLookUI();
        initTextAreaLayout();
        initMenus();
        initActions();
    }

    private void initTextAreaLayout() {
        pathField = new JTextField(DEFAULT_PATH, 40);
        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(pathField);
        final JPanel buttonPanel = new JPanel();
        this.readRequestsButton = new JButton("Run Action");
        buttonPanel.add(this.readRequestsButton);
        topPanel.add(buttonPanel);
        contentArea = new JTextArea(25, 60);
        JScrollPane scrollPane = new JScrollPane(contentArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private void initMenus() {
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        newMenuItem = new JMenuItem("New");
        openMenuItem = new JMenuItem("Open");
        exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(exitMenuItem);
        bar.add(fileMenu);
        this.setJMenuBar(bar);
    }

    private void initActions() {
        this.exitMenuItem.addActionListener(new QuitAction());
        LoadFileAction loadFileAction = new LoadFileAction();
        readRequestsButton.addActionListener(loadFileAction);
    }

    private class QuitAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            System.out.println("INFO: exiting application");
            System.exit(0);
        }
    }

    private class LoadFileAction extends AbstractAction implements Runnable {

        public void actionPerformed(ActionEvent e) {
            loadFileActionThread = new Thread((Runnable) this);
            loadFileActionThread.start();
        }

        public void run() {
            ExampleReaderWithUI reader = new ExampleReaderWithUI(SimpleSwing.this.contentArea, true);
            ExampleReaderWithUI.runFilterStackTrace(reader, "latin_text.txt", "swing.log", false);
            loadFileActionThread = null;
        }
    }

    private static void setNativeLookUI() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SimpleSwing createReaderFrame() {
        SimpleSwing simpleFrame = new SimpleSwing();
        simpleFrame.pack();
        simpleFrame.setVisible(true);
        return simpleFrame;
    }

    public static void main(String[] args) {
        createReaderFrame();
    }
}
