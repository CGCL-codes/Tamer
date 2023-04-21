package sunw.demo.idedemo;

import java.util.*;
import java.net.URL;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.lang.reflect.*;
import javax.help.*;
import javax.swing.*;
import javax.swing.text.*;
import sunw.demo.classviewer.ClassViewerNavigator;
import java.beans.PropertyVetoException;
import javax.help.Map.ID;

/**
 * This class is the main class of the IdeHelp demo
 *
 * @author Roger D. Brinkley
 * @author Eduardo Pelegri-Llopart
 * @version	1.80	09/15/04
 */
public class ApiDemo extends JPanel implements ActionListener {

    private static boolean hasCustomCursor;

    static String swingPkg = "javax.swing.plaf.";

    static JFrame frame;

    static final String helpsetName = "IdeHelp";

    static final String helpsetLabel = "Demo JDE - Help";

    HelpSet mainHS = null;

    HelpBroker mainHB;

    HelpSet apiHS = null;

    HelpBroker apiHB;

    JRootPane rootpane;

    JSplitPane split;

    JMenuItem menuItem, menu_help, menu_open, menu_apihelp;

    JTabbedPane messages;

    int miscTabIndex;

    JMenuItem item1, item2;

    public static int WIDTH = 845;

    public static int HEIGHT = 495;

    public static int JH_WIDTH = 645;

    public static int JH_HEIGHT = 495;

    JDesktopPane desktop;

    JInternalFrame sourceIFrame;

    JInternalFrame classViewerIFrame;

    JButton helpbutton;

    private final boolean showTimes = false;

    ApiDemo() {
        if (showTimes) System.err.print("creating mainHB...");
        try {
            ClassLoader cl = ApiDemo.class.getClassLoader();
            URL url = HelpSet.findHelpSet(cl, helpsetName);
            mainHS = new HelpSet(cl, url);
        } catch (Exception ee) {
            System.out.println("Help Set " + helpsetName + " not found");
            return;
        } catch (ExceptionInInitializerError ex) {
            System.err.println("initialization error:");
            ex.getException().printStackTrace();
        }
        mainHB = mainHS.createHelpBroker();
        if (showTimes) System.err.println(" done!");
        if (showTimes) System.err.print("creating apiHB...");
        try {
            ClassLoader cl = ApiDemo.class.getClassLoader();
            URL url = HelpSet.findHelpSet(cl, "api");
            apiHS = new HelpSet(cl, url);
        } catch (Exception ee) {
            System.out.println("API Help Set not found");
            return;
        }
        apiHB = apiHS.createHelpBroker("api");
        if (showTimes) System.err.println(" done!");
        resources = ResourceBundle.getBundle("sunw.demo.idedemo.IdeDemo");
        if (resources == null) {
            System.err.println("Resources for application IdeDemo not found");
        }
        WindowListener l = new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                setVisible(false);
                System.exit(0);
            }

            public void windowClosed(WindowEvent e) {
                setVisible(false);
                System.exit(0);
            }

            public void windowOpened(WindowEvent e) {
                split.setDividerLocation(0.80);
            }
        };
        frame.addWindowListener(l);
        rootpane = frame.getRootPane();
        mainHB.enableHelpKey(rootpane, "top", mainHS, "javax.help.SecondaryWindow", null);
        setLayout(new BorderLayout());
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.add(createMenus(), "North");
        header.add(createToolbar(), "South");
        add(header, "North");
        desktop = new JDesktopPane();
        desktop.setOpaque(true);
        desktop.setDoubleBuffered(true);
        createSourceIFrame();
        desktop.add(sourceIFrame, JLayeredPane.PALETTE_LAYER);
        sourceIFrame.show();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        messages = new JTabbedPane();
        JTextArea newtext = new JTextArea();
        newtext.setBackground(Color.white);
        CSH.setHelpIDString(newtext, "build.build");
        messages.addTab("Build", newtext);
        newtext = new JTextArea();
        CSH.setHelpIDString(newtext, "debug.overview");
        messages.addTab("Debug", newtext);
        newtext = new JTextArea();
        CSH.setHelpIDString(newtext, "browse.strings");
        messages.addTab("String Search", newtext);
        miscTabIndex = messages.getTabCount();
        messages.insertTab("Misc", null, new JTextArea(), null, miscTabIndex);
        messages.setSelectedIndex(0);
        messages.setDoubleBuffered(true);
        panel.add(messages, "Center");
        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, desktop, panel);
        split.setOneTouchExpandable(true);
        add(split, "Center");
    }

    /**
     * Create a JButton out of a resource name
     */
    private JButton createButton(String name) {
        java.net.URL url = this.getClass().getResource(name);
        ImageIcon icon = new ImageIcon(url);
        return new JButton(icon);
    }

    private void createClassViewer() {
        if (classViewerIFrame != null) {
            return;
        }
        classViewerIFrame = new JInternalFrame("Classes", true, true, true, true);
        JComponent content = new JPanel();
        content.setLayout(new BorderLayout());
        content.setDoubleBuffered(true);
        classViewerIFrame.setDoubleBuffered(true);
        classViewerIFrame.setBounds(570, 10, 200, 310);
        JHelpNavigator xnav;
        try {
            JHelpContentViewer viewer1 = new JHelpContentViewer(apiHS);
            messages.setComponentAt(miscTabIndex, viewer1);
            messages.setSelectedIndex(miscTabIndex);
            xnav = (JHelpNavigator) apiHS.getNavigatorView("TOC").createNavigator(viewer1.getModel());
            content.add(xnav, "Center");
            classViewerIFrame.setContentPane(content);
        } catch (Exception ee) {
            System.err.println("Caught excpetion while creating ClassViewer: " + ee);
            ee.printStackTrace();
            JTextArea sourceText = new JTextArea("Trouble adding Navigator");
            content.add(sourceText, "Center");
        }
    }

    private void createSourceIFrame() {
        if (sourceIFrame != null) {
            return;
        }
        sourceIFrame = new JInternalFrame("Source", true, true, true, true);
        CSH.setHelpIDString(sourceIFrame, "edit.editsource");
        JComponent c = (JComponent) sourceIFrame.getContentPane();
        c.setLayout(new BorderLayout());
        c.setDoubleBuffered(true);
        sourceIFrame.setDoubleBuffered(true);
        sourceIFrame.setBounds(10, 10, 550, 310);
        JTextArea sourceText = new JTextArea("");
        sourceText.setFont(new Font("Courier", Font.PLAIN, 12));
        sourceText.setBackground(Color.white);
        sourceText.append("/* To view JavaHelp click, Help, Java API Help */" + "\n\nimport java.applet.Applet;" + "\nimport java.awt.Graphics;" + "\n\npublic class HelloWorld extends Applet {" + "\n\n    public void paint(Graphics g) {" + "\n        g.drawString(\"Hello world!\", 50, 25);" + "\n    }\n}\n");
        c.add(sourceText, "Center");
    }

    ResourceBundle resources;

    private JButton addButton(JToolBar toolbar, String img, String tipKey) {
        JButton button = createButton(img);
        if (tipKey != null) {
            try {
                String tipText = resources.getString("toolbar." + tipKey + ".tip");
                button.setToolTipText(tipText);
            } catch (Exception ex) {
                System.err.println("Could not find a resource for " + tipKey);
            }
        }
        toolbar.add(button);
        return button;
    }

    public JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();
        CSH.setHelpIDString(toolbar, "toolbar.main");
        addButton(toolbar, "images/open.gif", "open");
        addButton(toolbar, "images/save.gif", "save");
        toolbar.addSeparator();
        addButton(toolbar, "images/start.gif", "start");
        addButton(toolbar, "images/break.gif", "stop");
        addButton(toolbar, "images/setbreak.gif", "setbreak");
        addButton(toolbar, "images/resume.gif", "resume");
        addButton(toolbar, "images/goto.gif", "goto");
        addButton(toolbar, "images/goend.gif", "goend");
        addButton(toolbar, "images/skip.gif", "skip");
        toolbar.addSeparator();
        addButton(toolbar, "images/down.gif", "down");
        addButton(toolbar, "images/up.gif", "up");
        toolbar.addSeparator();
        helpbutton = addButton(toolbar, "images/help.gif", "help");
        helpbutton.addActionListener(new CSH.DisplayHelpAfterTracking(mainHB));
        helpbutton.setEnabled(hasCustomCursor);
        return toolbar;
    }

    private JMenuItem addMenuItem(JMenu menu, String label, String tipKey) {
        JMenuItem item = new JMenuItem(label);
        if (tipKey != null) {
            try {
                String tipText = resources.getString("menu." + tipKey + ".tip");
                item.setToolTipText(tipText);
            } catch (Exception ex) {
                System.err.println("Could not find a resource for " + tipKey);
            }
        }
        menu.add(item);
        return item;
    }

    private void showDialog1() {
        Object options[] = { "OK", "CANCEL", "HELP" };
        int index = JOptionPane.showOptionDialog(null, "Exit?", "Quit", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        switch(index) {
            case 0:
                System.exit(0);
            case 1:
                break;
            case 2:
                System.err.println("will ask for help");
                break;
        }
    }

    public JMenuBar createMenus() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(getBackground());
        JMenu menu = new JMenu("File");
        CSH.setHelpIDString(menu, "menus.file");
        menu.setToolTipText("File operations");
        menuBar.add(menu);
        addMenuItem(menu, "New", "file.new");
        addMenuItem(menu, "Open...", "file.open");
        menu.addSeparator();
        addMenuItem(menu, "Save", "file.save");
        addMenuItem(menu, "Save As...", "file.saveas");
        menu.addSeparator();
        menuItem = addMenuItem(menu, "Exit", "file.exit");
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object options[] = { "OK", "CANCEL", "HELP" };
                int index = JOptionPane.showOptionDialog(null, "Exit IdeDemo?", "Exit Confirmation", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                switch(index) {
                    case 0:
                        System.exit(0);
                    case 1:
                        break;
                    case 2:
                        try {
                            mainHB.setCurrentID("menus.file");
                        } catch (Exception be) {
                        }
                        break;
                }
            }
        });
        menu = new JMenu("Edit");
        CSH.setHelpIDString(menu, "menus.edit");
        menuBar.add(menu);
        addMenuItem(menu, "Undo", null);
        addMenuItem(menu, "Redo", null);
        addMenuItem(menu, "Cut", null);
        addMenuItem(menu, "Copy", null);
        addMenuItem(menu, "Paste", null);
        menu.addSeparator();
        menuItem = addMenuItem(menu, "Find", null);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new FindDialog(ApiDemo.this, null);
                dialog.show();
            }
        });
        menu.addSeparator();
        addMenuItem(menu, "Go to...", null);
        menu = new JMenu("Build");
        CSH.setHelpIDString(menu, "menus.build");
        menuBar.add(menu);
        menuItem = addMenuItem(menu, "Build", null);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mainHB.showID("build.build", "javax.help.SecondaryWindow", "main");
            }
        });
        menuItem = addMenuItem(menu, "Build All", null);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                javax.help.Popup popup = (javax.help.Popup) javax.help.Popup.getPresentation(mainHS, null);
                popup.setInvoker((Component) e.getSource());
                popup.setCurrentID("build.compilefile");
                popup.setDisplayed(true);
            }
        });
        menuItem = addMenuItem(menu, "Compile File", null);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mainHB.showID("build.compilefile", "javax.help.MainWindow", "main");
            }
        });
        menu = new JMenu("Debug");
        CSH.setHelpIDString(menu, "menus.debug");
        menuBar.add(menu);
        addMenuItem(menu, "Start/Restart", null);
        addMenuItem(menu, "Stop", null);
        menu = new JMenu("Window");
        CSH.setHelpIDString(menu, "menus.windows");
        menuBar.add(menu);
        item1 = menuItem = addMenuItem(menu, "Source", null);
        item1.addActionListener(this);
        item2 = menuItem = addMenuItem(menu, "Class Inspector", null);
        item2.addActionListener(this);
        JMenu help = new JMenu("Help");
        CSH.setHelpIDString(help, "menus.help");
        if ((UIManager.getLookAndFeel().getName()).equals("CDE/Motif")) {
            menuBar.add(Box.createGlue());
        }
        menuBar.add(help);
        menu_help = new JMenuItem(helpsetLabel);
        menu_help.addActionListener(new CSH.DisplayHelpFromSource(mainHB));
        help.add(menu_help);
        help.addSeparator();
        menu_apihelp = new JMenuItem("Java API Reference");
        CSH.setHelpIDString(menu_apihelp, "intro");
        menu_apihelp.addActionListener(new CSH.DisplayHelpFromSource(apiHB));
        help.add(menu_apihelp);
        menu_apihelp = new JMenuItem("Java API Reference - Secondary Window");
        CSH.setHelpIDString(menu_apihelp, "intro");
        menu_apihelp.addActionListener(new CSH.DisplayHelpFromSource(apiHS, "javax.help.SecondaryWindow", null));
        help.add(menu_apihelp);
        menu_apihelp = new JMenuItem("Java API Reference - Popup");
        apiHB.enableHelpOnButton(menu_apihelp, "intro", apiHS, "javax.help.Popup", null);
        help.add(menu_apihelp);
        help.addSeparator();
        if (hasCustomCursor) {
            JMenuItem menu_cshHelp = new JMenuItem("Help OnItem - Main Window");
            menu_cshHelp.addActionListener(new CSH.DisplayHelpAfterTracking(mainHB));
            help.add(menu_cshHelp);
            menu_cshHelp = new JMenuItem("Help OnItem - Secondary Window");
            menu_cshHelp.addActionListener(new CSH.DisplayHelpAfterTracking(mainHS, "javax.help.SecondaryWindow", "main"));
            help.add(menu_cshHelp);
            menu_cshHelp = new JMenuItem("Help OnItem - Popup");
            menu_cshHelp.addActionListener(new CSH.DisplayHelpAfterTracking(mainHS, "javax.help.Popup", null));
            help.add(menu_cshHelp);
            help.addSeparator();
        }
        return menuBar;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == item1) {
            try {
                if (sourceIFrame.isClosed()) {
                    createSourceIFrame();
                    desktop.add(sourceIFrame, JLayeredPane.PALETTE_LAYER);
                    sourceIFrame.setIcon(false);
                    sourceIFrame.show();
                }
            } catch (PropertyVetoException ex) {
            }
        } else if (source == item2) {
            try {
                if (classViewerIFrame == null) {
                    createClassViewer();
                    desktop.add(classViewerIFrame, JLayeredPane.PALETTE_LAYER);
                    classViewerIFrame.setIcon(false);
                    classViewerIFrame.show();
                } else if (classViewerIFrame.isClosed()) {
                    desktop.add(classViewerIFrame, JLayeredPane.PALETTE_LAYER);
                    classViewerIFrame.setIcon(false);
                    classViewerIFrame.show();
                }
            } catch (PropertyVetoException ex) {
            }
        }
        if (e.getID() == Event.WINDOW_DESTROY) {
            frame.dispose();
            System.exit(0);
        }
    }

    private static String[] shiftArgs(String args[], int step) {
        int count = args.length;
        String back[] = new String[count - step];
        for (int i = 0; i < count - step; i++) {
            back[i] = args[i + step];
        }
        return back;
    }

    public static void main(String args[]) throws Exception {
        if (args.length >= 1) {
            String laf = args[0];
            UIManager.setLookAndFeel(swingPkg + laf);
            args = shiftArgs(args, 1);
        }
        frame = new JFrame("Java Development Environment");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(new ApiDemo(), "Center");
        frame.setBounds(100, 100, WIDTH, HEIGHT);
        frame.show();
    }

    private static void usage() {
        System.err.println("Usage: [laf] [-helpset label name]");
        System.exit(1);
    }

    private static void printData(Container container) {
        Component k[] = container.getComponents();
        for (int i = 0; i < k.length; i++) {
            System.err.println(k[i]);
        }
        System.err.println(container);
    }

    static {
        Method m = null;
        hasCustomCursor = false;
        try {
            Class types[] = { Image.class, Point.class, String.class };
            m = Toolkit.class.getMethod("createCustomCursor", types);
            if (m != null) {
                hasCustomCursor = true;
            }
        } catch (NoSuchMethodError ex) {
        } catch (NoSuchMethodException ex) {
        }
    }
}

class FindDialog extends JDialog {

    private JButton helpButton;

    private JButton closeButton;

    private JFrame frame;

    private ApiDemo demo;

    private void initComponents() {
        Box topBox = Box.createVerticalBox();
        Box box1 = Box.createHorizontalBox();
        JLabel findLabel = new JLabel("Find: ");
        JTextField textField = new JTextField(20);
        box1.add(Box.createHorizontalStrut(5));
        box1.add(findLabel);
        box1.add(textField);
        box1.add(Box.createHorizontalStrut(5));
        topBox.add(Box.createVerticalStrut(5));
        topBox.add(box1);
        Box box3 = Box.createHorizontalBox();
        box3.add(Box.createHorizontalGlue());
        JButton findButton = new JButton("Find Next");
        JButton prevButton = new JButton("Find Previous");
        box3.add(findButton);
        box3.add(Box.createHorizontalStrut(10));
        box3.add(prevButton);
        box3.add(Box.createHorizontalGlue());
        topBox.add(box3);
        Box box4 = Box.createHorizontalBox();
        Box box5 = Box.createHorizontalBox();
        JCheckBox backwardsCheck = new JCheckBox("Find Backward");
        JCheckBox ignoreCaseCheck = new JCheckBox("Ignore Case");
        box4.add(Box.createHorizontalGlue());
        box4.add(backwardsCheck);
        box4.add(Box.createHorizontalGlue());
        box5.add(Box.createHorizontalGlue());
        box5.add(ignoreCaseCheck);
        box5.add(Box.createHorizontalGlue());
        topBox.add(box4);
        topBox.add(box5);
        Box box2 = Box.createHorizontalBox();
        closeButton = new JButton("Close");
        helpButton = new JButton("Help");
        box2.add(closeButton);
        box2.add(helpButton);
        Box box6 = Box.createHorizontalBox();
        box6.add(Box.createHorizontalStrut(5));
        box6.add(new JSeparator());
        box6.add(Box.createHorizontalStrut(5));
        topBox.add(Box.createVerticalStrut(10));
        topBox.add(box6);
        topBox.add(box2);
        getContentPane().add(topBox);
    }

    public FindDialog(ApiDemo demo, JFrame f) {
        super(f, "Find", false);
        this.frame = f;
        this.demo = demo;
        initComponents();
        pack();
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
        demo.mainHB.enableHelpOnButton(helpButton, "browse.strings", null);
        placeDialog();
        show();
    }

    protected void placeDialog() {
        if (frame != null) {
            int x = frame.getLocation().x + 30;
            int y = frame.getLocation().y + 100;
            setLocation(x, y);
        }
    }
}
