package sywico.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.apache.log4j.Logger;
import sywico.core.FeedBack;
import sywico.core.Util;
import sywico.gui.BusinessRunner;
import sywico.gui.Options;
import sywico.gui.SywicoBusinessRunner;
import sywico.gui.component.MainOutputPanel.Status;

public class Window implements ActionListener {

    static final long serialVersionUID = 1;

    static Logger logger = Logger.getLogger(Window.class);

    private JFrame frame;

    private MainComponent mainComponent;

    private Options options;

    private BusinessRunner businessRunner;

    AboutDialog aboutDialog;

    ProcessDialog processDialog;

    OptionDialog optionDialog;

    ResplitDialog resplitDialog;

    public MainComponent getMainComponent() {
        return mainComponent;
    }

    public static final String ICON_YES = "icons/tick.png";

    public static final String ICON_NO = "icons/cross.png";

    public static final String ICON_CANCEL = "icons/cancel.png";

    public static final String ICON_RUN = "icons/resultset_next.png";

    public static final String ICON_CHANGES_ACCEPTED = "icons/flag_green.png";

    public static final String ICON_CHANGES_REFUSED = "icons/flag_yellow.png";

    public static final String ICON_ERROR = "icons/exclamation.png";

    public static final String ICON_MERGED_WITH_CONFLICTS = "icons/weather_lightning.png";

    public static final String ICON_MERGED = "icons/weather_sun.png";

    public static final String ICON_SYNCHRONIZED = "icons/flag_green.png";

    public static final String ICON_REVIEW_CHANGES = "icons/magnifier.png";

    public static final String ICON_SEND_FILES = "icons/email.png";

    public static final String ICON_SAVE = "icons/disk.png";

    public static final String ICON_DOCUMENT = "icons/page_white.png";

    public static ImageIcon iconError;

    public static ImageIcon iconYes;

    public void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } catch (Exception e2) {
            }
        }
    }

    /**
     * Create the GUI and show it.  
     */
    public void create() throws Exception {
        options = new Options();
        options.load();
        businessRunner = createBusinessRunner();
        setLookAndFeel();
        frame = new JFrame("SyWiCo") {

            static final long serialVersionUID = 1;

            public void dispose() {
                if (logger.isInfoEnabled()) logger.info("closing...");
                options.save();
                super.dispose();
            }
        };
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setJMenuBar(createMenuBar());
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);
        mainComponent = new MainComponent(this, options);
        mainComponent.setBusinessRunner(businessRunner);
        panel.add(mainComponent);
        optionDialog = new OptionDialog(frame, options, this);
        resplitDialog = new ResplitDialog(frame, mainComponent, businessRunner);
        processDialog = new ProcessDialog(frame, this);
        aboutDialog = new AboutDialog(frame);
        iconError = Window.createImageIcon(this, Window.ICON_ERROR);
        iconYes = Window.createImageIcon(this, Window.ICON_YES);
    }

    public void setGUIStarted(final FeedBack feedBack) {
        setGUIStarted(feedBack, false);
    }

    public void setGUIStarted(final FeedBack feedBack, boolean preservePreviousResults) {
        if (!preservePreviousResults) mainComponent.setGUIRunning(true);
        mainComponent.inputPanel.buttonProcess.setEnabled(false);
        if (feedBack != null) {
            Runnable feedBackPrinter = new Runnable() {

                public void run() {
                    while (!feedBack.isDone()) {
                        processDialog.processPanel.update(feedBack);
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                        }
                        ;
                    }
                    if (logger.isInfoEnabled()) logger.info("feedback thread is done");
                }
            };
            new Thread(feedBackPrinter).start();
        } else {
            processDialog.processPanel.update((FeedBack) null);
        }
        processDialog.setVisible(true);
    }

    public void setGUIStoppedWithoutErasingPreviousResult(FeedBack feedBack) {
        if (feedBack != null) {
            feedBack.done("");
        }
        processDialog.setVisible(false);
        mainComponent.inputPanel.buttonProcess.setEnabled(true);
    }

    public void setGUIStopped(Status status, String text, List<String> files, FeedBack feedBack) {
        setGUIStoppedWithoutErasingPreviousResult(feedBack);
        mainComponent.setGUIRunning(false);
        mainComponent.outputPanel.setOutputFileAsStringList(files);
        mainComponent.outputPanel.update(status, text);
    }

    protected BusinessRunner createBusinessRunner() throws Exception {
        SywicoBusinessRunner sywicoBusinessRunner = new SywicoBusinessRunner(this);
        return sywicoBusinessRunner;
    }

    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);
        JMenuItem menuItem = new JMenuItem("Settings");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuItem = new JMenuItem("Resplit a file");
        menuItem.setActionCommand("ResplitFile");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuItem = new JMenuItem("About");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuItem = new JMenuItem("Bye");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuItem.addActionListener(this);
        return menuBar;
    }

    protected void showGUI() {
        frame.pack();
        frame.setVisible(true);
    }

    protected void saveResultCopy() {
        File aFile = new File(mainComponent.outputPanel.getOutputFileAsStringList().get(0));
        File dir = aFile.getParentFile();
        mainComponent.outputPanel.fileChooserSaveACopy.setSelectedFile(dir);
        int returnVal = mainComponent.outputPanel.fileChooserSaveACopy.showDialog(mainComponent, "Save in directory");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File newDir = mainComponent.outputPanel.fileChooserSaveACopy.getSelectedFile();
            for (String copyFrom : mainComponent.outputPanel.getOutputFileAsStringList()) {
                String actualName = new File(copyFrom).getName();
                String copyTo = newDir.getPath() + File.separator + actualName;
                Util.copy(copyFrom, copyTo, null);
                if (logger.isInfoEnabled()) logger.info("copy " + copyFrom + " -> " + copyTo);
            }
        } else {
            if (logger.isDebugEnabled()) logger.debug("Open command cancelled by user.");
        }
    }

    /**
     * Helper function that tell if we are on company side or developer side according to Options
     * @return
     */
    public boolean isCompany() {
        return "company".equals(options.getValue(Options.SIDE));
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (logger.isInfoEnabled()) logger.info("action=" + e.getActionCommand());
            if ("Bye".equals(e.getActionCommand())) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            } else if ("Settings".equals(e.getActionCommand())) {
                optionDialog.setVisible(true);
            } else if ("ResplitFile".equals(e.getActionCommand())) {
                resplitDialog.setVisible(true);
            } else if ("Options".equals(e.getActionCommand())) {
                mainComponent.inputPanel.update(isCompany());
            } else if ("Process".equals(e.getActionCommand())) {
                String validation = optionDialog.optionsComponent.validateFields();
                if (validation != null) {
                    validation = "Some  of the settings are not valid (To change them go to Menu>Settings):\n " + validation;
                } else {
                    if (options.getValue(Options.SIDE).equals("company") || mainComponent.inputPanel.radioProcess.isSelected()) {
                        String fileName = mainComponent.inputPanel.fileOrDirChooseComponent.getValue();
                        if (fileName.trim().length() == 0) validation = "Please specify a input message file to process"; else if (!new File(fileName).isFile()) validation = "can not find any file or slices from a file named " + fileName;
                    }
                }
                if (validation == null) {
                    mainComponent.clearLog();
                    if (options.getValue(Options.SIDE).equals("company")) mainComponent.businessRunner.start(options, BusinessRunner.Operation.PROCESS_SUBMIT_MSG, mainComponent.inputPanel.fileOrDirChooseComponent.getValue()); else if (mainComponent.inputPanel.radioSubmit.isSelected()) mainComponent.businessRunner.start(options, BusinessRunner.Operation.SUBMIT); else if (mainComponent.inputPanel.radioRequestUpdate.isSelected()) mainComponent.businessRunner.start(options, BusinessRunner.Operation.REQUEST_UPDATE); else mainComponent.businessRunner.start(options, BusinessRunner.Operation.PROCESS_UPDATE_MSG, mainComponent.inputPanel.fileOrDirChooseComponent.getValue());
                } else {
                    JOptionPane.showMessageDialog(frame, validation, "Invalid settings", JOptionPane.ERROR_MESSAGE, Window.iconError);
                }
            } else if ("Cancel".equals(e.getActionCommand())) {
                businessRunner.getFeedBack().cancel();
            } else if ("SaveResultCopy".equals(e.getActionCommand())) {
                saveResultCopy();
            } else if ("About".equals(e.getActionCommand())) {
                aboutDialog.setVisible(true);
            } else throw new RuntimeException("unknown command '" + e.getActionCommand() + "'");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            JOptionPane.showMessageDialog(frame, "a problem occured:\n" + ex.getMessage(), "error", JOptionPane.ERROR_MESSAGE, Window.iconError);
        }
    }

    /**
     * launch the GUI
     *
     */
    public void startUp() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    create();
                    showGUI();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * java entry point
     * 
     */
    public static void main(String[] args) {
        Window window = new Window();
        window.startUp();
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    public static ImageIcon createImageIcon(Object caller, String path) {
        java.net.URL imgURL = caller.getClass().getClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, "");
        } else {
            logger.error("Couldn't load icon: " + path);
            assert (false) : "Couldn't load icon: " + path;
            return null;
        }
    }
}
