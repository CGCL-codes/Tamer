package jir.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * A default application layout for jClassifier.
 * @author Paul Lammertsma
 */
public class ApplicationFrame extends SystemFrame {

    private static final long serialVersionUID = -2197345154706296742L;

    public static ApplicationFrame mainWindow = null;

    public static Application mainApplication = null;

    private LinkedList<JButton> tbButtons;

    private JSplitPane splitPane;

    private JPanel pngLog;

    private JButton showLogger, hideLogger, clearLogger;

    private JLabel statusMessage;

    private JProgressBar progress;

    private JTextArea log;

    public ApplicationFrame() {
        super("icon32");
        mainWindow = this;
    }

    public void initialize(String windowTitle, final Application app) {
        mainApplication = app;
        buildUI(windowTitle, app);
    }

    private void buildUI(String windowTitle, final Application app) {
        this.setTitle(windowTitle);
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagConstraints;
        Insets insets = new Insets(5, 5, 0, 0);
        Insets insetsBR = new Insets(5, 5, 5, 5);
        Border borderLine = BorderFactory.createLineBorder(Color.black);
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        tbButtons = new LinkedList<JButton>();
        tbButtons.add(createButton("open", "OPEN", "Open a sampling image", "Open image", true));
        tbButtons.add(createButton("add", "ADD", "Add an image to the dataset", "Add image to set", false));
        tbButtons.add(createButton("refresh", "REFRESH", "Reprocess entire data set", "Refresh data set", false));
        tbButtons.add(createButton("settings", "SETTINGS", "Change the algorithm settings", "Settings", false));
        tbButtons.add(createButton("info", "ABOUT", "About the authors", "About", false));
        for (JButton b : tbButtons) {
            toolbar.add(b);
            b.setEnabled(b.getActionCommand().equals("OPEN") || b.getActionCommand().equals("REFRESH") || b.getActionCommand().equals("SETTINGS") || b.getActionCommand().equals("ABOUT"));
            if (b.getActionCommand().equals("OPEN") || b.getActionCommand().equals("REFRESH")) {
                toolbar.addSeparator();
            }
        }
        JPanel mainPane = new JPanel();
        mainPane.setLayout(gridBag);
        Application.pnlInput.setPreferredSize(new Dimension(400, 280));
        Application.pnlInput.setBorder(borderLine);
        gridBagConstraints = new GridBagConstraints(0, 2, 5, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
        mainPane.add(Application.pnlInput, gridBagConstraints);
        JScrollPane scrollableDataset = new JScrollPane(Application.pnlDatasetImages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollableDataset.setPreferredSize(new Dimension(330, 400));
        scrollableDataset.setBackground(Color.WHITE);
        gridBagConstraints = new GridBagConstraints(5, 2, 1, 2, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insetsBR, 0, 0);
        mainPane.add(scrollableDataset, gridBagConstraints);
        JButton btnEval = createButton("evaluate", "EVALUATE", "Evaluate sample image", "Evaluate", true);
        btnEval.setEnabled(false);
        gridBagConstraints = new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
        mainPane.add(btnEval, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints(0, 4, 2, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, -5);
        mainPane.add(Application.pnlOptions, gridBagConstraints);
        JPanel statusbar = new JPanel();
        statusbar.setLayout(gridBag);
        showLogger = createButton("console_open", "CONSOLE", "Expand detailed information field", "Show details", false);
        hideLogger = createButton("console_close", "CONSOLE", "Collapse detailed information field", "Hide details", false);
        clearLogger = createButton("console_clear", "CONSOLE_CLEAR", "Clear console", "Clear console", false);
        showLogger.setPreferredSize(new Dimension(24, 24));
        hideLogger.setPreferredSize(new Dimension(24, 24));
        clearLogger.setPreferredSize(new Dimension(24, 24));
        hideLogger.setVisible(false);
        clearLogger.setVisible(false);
        gridBagConstraints = new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 2, 2), 0, 0);
        gridBagConstraints.insets = new Insets(0, 2, 2, 0);
        statusbar.add(showLogger, gridBagConstraints);
        statusbar.add(hideLogger, gridBagConstraints);
        gridBagConstraints.gridx++;
        gridBagConstraints.insets = new Insets(0, 2, 2, 5);
        statusbar.add(clearLogger, gridBagConstraints);
        statusbar.add(Box.createRigidArea(new Dimension(5, 0)));
        statusMessage = new JLabel("Loading...");
        Logger.out.setLabelListener(statusMessage);
        gridBagConstraints.gridx++;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 5);
        statusbar.add(statusMessage, gridBagConstraints);
        gridBagConstraints.gridx++;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 4);
        progress = new JProgressBar();
        progress.setStringPainted(true);
        progress.setMaximumSize(new Dimension(350, 24));
        progress.setMinimumSize(new Dimension(50, 24));
        statusbar.add(progress, gridBagConstraints);
        log = new JTextArea("", 5, 25);
        log.setEditable(false);
        log.setLineWrap(true);
        Logger.out.addLogListener(log);
        Logger.out.setProgressListener(progress);
        Logger.out.progressSetVisible(false);
        JScrollPane scrollableLog = new JScrollPane(log, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pngLog = new JPanel();
        pngLog.setLayout(new BorderLayout());
        pngLog.add(scrollableLog, BorderLayout.CENTER);
        pngLog.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainPane, pngLog);
        splitPane.setBorder(null);
        splitPane.setResizeWeight(0.9);
        pngLog.setVisible(false);
        splitPane.setDividerSize(0);
        this.add(toolbar, BorderLayout.NORTH);
        this.add(splitPane, BorderLayout.CENTER);
        this.add(statusbar, BorderLayout.SOUTH);
    }

    /**
     * Simplifies creation of toolbar buttons
     */
    private JButton createButton(String imgName, String action, String toolTipText, String altText, Boolean showText) {
        String filename = "resources/" + imgName + ".png";
        JButton button = new JButton();
        ImageIcon icon = new ImageIcon(filename, altText);
        button.setIcon(icon);
        if (showText || icon.getImageLoadStatus() == MediaTracker.ERRORED) {
            if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
                try {
                    System.out.println("ERROR loading image: " + (new File(".")).getCanonicalPath() + "/" + filename);
                } catch (Exception e) {
                    System.out.println("ERROR loading image: " + filename);
                }
            }
            button.setText(altText);
            button.setMargin(new Insets(3, 3, 3, 8));
        } else {
            button.setMargin(new Insets(3, 3, 3, 3));
        }
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setActionCommand(action);
        button.setToolTipText(toolTipText);
        button.addActionListener(this);
        return button;
    }

    public void finalize() {
        super.finalize(true);
        Logger.out.progressSetVisible(true);
        new Thread(new DatasetLocator()).start();
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.length() == 0 && e.getSource() instanceof JButton) {
            JButton b = (JButton) (e.getSource());
            action = b.getText();
        }
        if (action.equals("EVALUATE")) {
            mainApplication.evaluate(new DatasetEntry());
        } else if (action.equals("CONSOLE")) {
            if (pngLog.isVisible()) {
                pngLog.setVisible(false);
                showLogger.setVisible(true);
                clearLogger.setVisible(false);
                hideLogger.setVisible(false);
                splitPane.setDividerLocation(splitPane.getMaximumDividerLocation());
            } else {
                pngLog.setVisible(true);
                showLogger.setVisible(false);
                clearLogger.setVisible(true);
                hideLogger.setVisible(true);
                splitPane.resetToPreferredSizes();
            }
        } else if (action.equals("CONSOLE_CLEAR")) {
            mainApplication.clear();
        } else if (action.equals("REFRESH")) {
            new Thread(new DatasetLocator()).start();
        } else if (action.equals("OPEN")) {
            openImage();
        } else if (action.equals("ADD")) {
            System.err.println("Adding images to datasets is currently unimplemented.");
        } else if (action.equals("SETTINGS")) {
            System.err.println("The settings screen is currently unimplemented.");
        } else if (action.equals("ABOUT")) {
            System.err.println("The about screen is currently unimplemented.");
        } else {
            System.err.println("Unknown command: " + action);
        }
    }

    private void openImage() {
        String[] fileExtensions = { "jpg", "png", "bmp", "gif" };
        WindowsFileDialog openDial = new WindowsFileDialog(this, "Open image", FileDialog.LOAD, fileExtensions);
        openDial.display();
        if (openDial.getFile() == null) return;
        String filename = openDial.getDirectory() + openDial.getFile();
        System.out.println(filename);
    }

    public void addDatasetEntry(DatasetEntry entry) {
        Application.pnlDatasetImages.setLayout(new BoxLayout(Application.pnlDatasetImages, BoxLayout.PAGE_AXIS));
        DatasetPanel panel = new DatasetPanel(entry);
        Application.pnlDatasetImages.add(panel);
    }
}
