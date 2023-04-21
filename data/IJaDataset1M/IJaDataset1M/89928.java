package org.insight.view.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.table.TableColumnModel;
import org.insight.common.util.TextResources;
import org.insight.control.core.MainController;
import org.insight.model.log.LogManager;
import org.insight.view.config.ConfigDialog;

/**
 * The main application window.
 *
 * <pre>
 * Version History:
 * 
 * $Log: MainWindow.java,v $
 * Revision 1.5  2006/02/03 14:18:10  cjn
 * Fixed checkstyle violations.
 *
 * Revision 1.4  2006/01/11 12:15:47  cjn
 * Increased initial width of window
 *
 * Revision 1.3  2006/01/09 14:04:02  cjn
 * Extracted out open and save dialog control code, and added filters and clearing of filename between uses.
 *
 * Revision 1.2  2003/03/31 08:57:53  cjn
 * Added configuration window
 *
 * Revision 1.1  2003/03/19 10:01:07  cjn
 * Refactored packages
 *
 * Revision 1.5  2002/10/07 08:52:19  cjn
 * Simplified progress bar code
 *
 * Revision 1.4  2002/09/20 15:02:08  cjn
 * Removed debug
 *
 * Revision 1.3  2002/07/23 08:00:48  cjn
 * Added progress bar
 *
 * Revision 1.2  2002/07/05 09:11:51  cjn
 * Added CVS keywords
 * </pre>
 *
 * @author Chris Nappin
 * @version $Revision: 1.5 $
 */
public class MainWindow extends JFrame {

    /** width of window. */
    private static final int WIDTH = 500;

    /** height of window. */
    private static final int HEIGHT = 300;

    /** relative widths of table columns. */
    private static final int FILENAME_COL = 50;

    private static final int HITS_COL = 10;

    private static final int START_COL = 20;

    private static final int END_COL = 20;

    /** amount of time between updating the progress bar, in milliseconds. */
    private static final int PROGRESS_BAR_DELAY = 100;

    /** the main controller. */
    private MainController mainController = MainController.getController();

    /** used to load logfiles and save reports. */
    private JFileChooser chooser = new JFileChooser();

    /** our main table. */
    private JTable table;

    /** the progress bar. */
    private JProgressBar progressBar;

    /** the progress bar's label. */
    private JLabel progressBarLabel;

    /** determines ownership of the progress bar. */
    private String progressMessage;

    /** swing thread that updates the progress bar. */
    private Timer progressTimer;

    /** the current progress bar amount. */
    private int progressPercentage;

    /** indicates if the progress bar is active. */
    private boolean progressBarActive;

    /**
     * Default constructor.
     */
    public MainWindow() {
        setTitle(TextResources.getResources().getString("program.titlebar"));
        setSize(WIDTH, HEIGHT);
        addWindowListener(new WindowAdapter() {

            /**
             * Called when the window is closing
             * @param event The Window Event
             */
            public void windowClosing(WindowEvent event) {
                mainController.exit();
            }
        });
        ActionListener menuListener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                String command = event.getActionCommand();
                if (command.equals(TextResources.EXIT)) {
                    mainController.exit();
                } else if (command.equals(TextResources.ABOUT)) {
                    AboutDialog about = new AboutDialog(MainWindow.this);
                    Point point = MainWindow.this.getLocation();
                    about.setLocation(point.x + 30, point.y + 30);
                    about.show();
                } else if (command.equals(TextResources.OPEN)) {
                    File file = showOpenLogfileDialog();
                    if (file != null) {
                        mainController.loadLogfile(file);
                    }
                } else if (command.equals(TextResources.CREATE_REPORT)) {
                    File file = showSaveReportDialog();
                    if (file != null) {
                        mainController.createReport(file);
                    }
                } else if (command.equals(TextResources.CONFIGURATION)) {
                    ConfigDialog config = new ConfigDialog(MainWindow.this);
                    Point point = MainWindow.this.getLocation();
                    config.setLocation(point.x + 30, point.y + 30);
                    config.show();
                } else if (command.equals(TextResources.CLOSE)) {
                    int[] selectedRows = table.getSelectedRows();
                    if (selectedRows.length > 0) {
                        mainController.closeLogfiles(selectedRows);
                    }
                }
            }
        };
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = createMenu(TextResources.FILE, menuBar);
        createMenuItem(TextResources.OPEN, menuListener, fileMenu);
        createMenuItem(TextResources.CLOSE, menuListener, fileMenu);
        fileMenu.addSeparator();
        createMenuItem(TextResources.EXIT, menuListener, fileMenu);
        JMenu reportMenu = createMenu(TextResources.REPORT, menuBar);
        createMenuItem(TextResources.CREATE_REPORT, menuListener, reportMenu);
        createMenuItem(TextResources.CONFIGURATION, menuListener, reportMenu);
        JMenu helpMenu = createMenu(TextResources.HELP, menuBar);
        createMenuItem(TextResources.ABOUT, menuListener, helpMenu);
        table = new JTable(LogManager.getManager().getTableModel());
        TableColumnModel columns = table.getColumnModel();
        columns.getColumn(0).setPreferredWidth(FILENAME_COL);
        columns.getColumn(1).setPreferredWidth(HITS_COL);
        columns.getColumn(2).setPreferredWidth(START_COL);
        columns.getColumn(3).setPreferredWidth(END_COL);
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        progressBarLabel = new JLabel(" ");
        JPanel statusBar = new JPanel(new GridLayout(1, 2, 5, 5));
        statusBar.add(progressBarLabel);
        statusBar.add(progressBar);
        getContentPane().add(statusBar, BorderLayout.SOUTH);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - WIDTH) / 2, (screen.height - HEIGHT) / 2);
    }

    /**
     * Activate the progress bar, displays the specified message,
     * and starts the progress bar at zero (percent).
     * @param message shown on the status bar and used to manage updates
     */
    public synchronized void activateProgressBar(String message) {
        progressBarActive = true;
        progressMessage = message;
        progressPercentage = 0;
        if (progressTimer == null) {
            progressTimer = new Timer(PROGRESS_BAR_DELAY, new ActionListener() {

                /**
                     * timer event to update the progress bar
                     * @param event The timer event
                     */
                public void actionPerformed(ActionEvent event) {
                    if (progressBarActive) {
                        if (!progressBar.isVisible()) {
                            progressBar.setVisible(true);
                        }
                        progressBarLabel.setText(progressMessage);
                        progressBar.setValue(progressPercentage);
                    } else {
                        if (progressBar.isVisible()) {
                            progressBarLabel.setText(" ");
                            progressBar.setVisible(false);
                        }
                        progressTimer.stop();
                    }
                }
            });
        }
        progressTimer.start();
    }

    /**
     * Deactivate the progress bar.
     * @param message The message
     */
    public synchronized void deactivateProgressBar(String message) {
        progressBarActive = false;
    }

    /**
     * Update the progress bar, if still active.
     * @param percentage The percentage completion value (0..100)
     * @param message Progress bar message 
     */
    public synchronized void updateProgressBar(int percentage, String message) {
        if (progressBarActive) {
            progressMessage = message;
            if (percentage < 0) {
                progressPercentage = 0;
            } else if (percentage > 100) {
                progressPercentage = 100;
            } else {
                progressPercentage = percentage;
            }
        }
    }

    /**
     * Shows a modal error dialog box, with a single OK button.
     * @param message The error message
     */
    public void showOKErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a modal error dialog box, with Yes and No buttons.
     * @param message The error message
     * @return <code>true</code> if the Yes button was selected
     */
    public boolean showYesNoErrorDialog(String message) {
        int option = JOptionPane.showConfirmDialog(this, message, "Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
        return option == JOptionPane.YES_OPTION;
    }

    /**
     * Shows a modal question dialog box, with Yes and No buttons.
     * @param message The question message
     * @return <code>true</code> if the Yes button was selected
     */
    public boolean showYesNoQuestionDialog(String message) {
        int option = JOptionPane.showConfirmDialog(this, message, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return option == JOptionPane.YES_OPTION;
    }

    /**
     * Shows a file selection dialog box, to load a logfile.
     * 
     * Note: Uses a shared JFileChooser instance, so that the current directory
     * is retained between invications.
     * 
     * @return The file to load, or <code>null</code> if none was selected. 
     */
    private File showOpenLogfileDialog() {
        TextResources resources = TextResources.getResources();
        chooser.setDialogTitle(resources.getString("program.open.title"));
        chooser.setMultiSelectionEnabled(false);
        chooser.resetChoosableFileFilters();
        chooser.addChoosableFileFilter(new FileFilter() {

            /**
                 * Get the Filter's description.
                 * @return The description
                 */
            public String getDescription() {
                return TextResources.getResources().getString("program.open.description");
            }

            /**
                 * Determine if the file is accepted.
                 * @param file  The file
                 * @return <code>true</code> if the file is accepted
                 */
            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toLowerCase().endsWith(TextResources.getResources().getString("program.open.filter"));
            }
        });
        ((BasicFileChooserUI) chooser.getUI()).setFileName("");
        if (chooser.showOpenDialog(MainWindow.this) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }

    /**
     * Shows a file selection dialog box, to save a report.
     * 
     * Note: Uses a shared JFileChooser instance, so that the current directory
     * is retained between invications.
     * 
     * @return The file to load, or <code>null</code> if none was selected. 
     */
    private File showSaveReportDialog() {
        TextResources resources = TextResources.getResources();
        chooser.setDialogTitle(resources.getString("program.save.title"));
        chooser.setMultiSelectionEnabled(false);
        chooser.resetChoosableFileFilters();
        chooser.addChoosableFileFilter(new FileFilter() {

            /**
                 * Get the Filter's description.
                 * @return The description
                 */
            public String getDescription() {
                return TextResources.getResources().getString("program.save.description");
            }

            /**
                 * Determine if the file is accepted.
                 * @param file  The file
                 * @return <code>true</code> if the file is accepted
                 */
            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toLowerCase().endsWith(TextResources.getResources().getString("program.save.filter"));
            }
        });
        ((BasicFileChooserUI) chooser.getUI()).setFileName("");
        if (chooser.showSaveDialog(MainWindow.this) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }

    /**
     * Utility method for creating menus.
     * @param name The menu name
     * @param menuBar The parent menuBar
     * @return The new menu
     */
    private JMenu createMenu(String name, JMenuBar menuBar) {
        TextResources resources = TextResources.getResources();
        JMenu menu = new JMenu(resources.getName(name));
        menu.setMnemonic(resources.getMnemonic(name));
        menuBar.add(menu);
        return menu;
    }

    /**
     * Utility method for creating menu items.
     * @param name The item name
     * @param listener The event listener
     * @param menu The parent menu
     */
    private void createMenuItem(String name, ActionListener listener, JMenu menu) {
        TextResources resources = TextResources.getResources();
        JMenuItem item = new JMenuItem(resources.getName(name), resources.getMnemonic(name));
        item.setActionCommand(name);
        item.addActionListener(listener);
        menu.add(item);
    }
}
