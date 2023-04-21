package net.sf.tinyPayroll.views;

import net.sf.tinyPayroll.event.DataFileLoadedEvent;
import net.sf.tinyPayroll.event.EntriesChangedEvent;
import net.sf.tinyPayroll.event.NormalExitEvent;
import net.sf.tinyPayroll.event.UsersChangedEvent;
import net.sf.tinyPayroll.utils.AppController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Observer;

/**
 * TODO: Document this class.
 * TODO: Separate the button bars into left and right instead of center bars.
 *
 * @author kev
 */
public class MainView {

    /**
     * The Swing parent window.
     */
    private JFrame frame = null;

    /**
     * Returns the Swing parent window.
     *
     * @return the Swing parent window.
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Event which fires when the application is unloading.
     */
    NormalExitEvent exitEvent = null;

    /**
     * Event which fires when the data file is loaded.
     */
    DataFileLoadedEvent dataFileLoadedEvent = null;

    /**
     * Event which fires when onre or more entries has been changed.
     */
    EntriesChangedEvent entriesChangedEvent = null;

    /**
     * Event which fires when one or more users has been changed.
     */
    UsersChangedEvent usersChangedEvent = null;

    /**
     * Controls communication between the GUI and the data file.
     */
    private AppController controller = null;

    /**
     * Tab view which contains the view for all the users.
     */
    PeopleView peopleView = null;

    /**
     * Default height of the main window.
     */
    private static int DEFAULT_HEIGHT = 550;

    /**
     * Default width of the main window.
     */
    private static int DEFAULT_WIDTH = 750;

    /**
     * Default X coordinate of the main window.
     */
    private static int DEFAULT_X = 250;

    /**
     * Default Y coordinate of the main window.
     */
    private static int DEFAULT_Y = 100;

    /**
     * Main constructor which constructs the main parent window and puts it up on the screen.
     */
    public MainView() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("We've got problems setting look and feel.");
        }
        frame = new JFrame("tinyPayroll");
        exitEvent = new NormalExitEvent();
        dataFileLoadedEvent = new DataFileLoadedEvent();
        entriesChangedEvent = new EntriesChangedEvent();
        usersChangedEvent = new UsersChangedEvent();
        JTabbedPane tabPane = new JTabbedPane();
        peopleView = new PeopleView(this);
        tabPane.addTab("Clock in/out", null, peopleView, "Clock in/out");
        tabPane.setMnemonicAt(0, KeyEvent.VK_C);
        EntryView entryView = new EntryView(this);
        tabPane.addTab("View Entries", null, entryView, "View entries that have already been made.");
        tabPane.setMnemonicAt(1, KeyEvent.VK_E);
        LogView logView = new LogView();
        tabPane.addTab("Logs", null, logView, "View application logs.");
        tabPane.setMnemonicAt(2, KeyEvent.VK_L);
        PreferencesView preferencesView = new PreferencesView();
        tabPane.addTab("Preferences", null, preferencesView, "View and change application preferences.");
        tabPane.setMnemonicAt(3, KeyEvent.VK_P);
        ReportsView reportsView = new ReportsView();
        reportsView.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        tabPane.addTab("Reports", null, reportsView, "View and print reports.");
        tabPane.setMnemonicAt(4, KeyEvent.VK_R);
        frame.getContentPane().add(tabPane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setBounds(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        frame.pack();
        MainViewWindowListener windowListener = new MainViewWindowListener(exitEvent);
        frame.addWindowListener(windowListener);
    }

    /**
     * Hides the window.
     */
    public void hide() {
        frame.setVisible(false);
    }

    /**
     * Quick and dirty way to display an exception to the screen.
     *
     * @param e The exception to be displayed.
     */
    public void showWarning(Exception e) {
        JOptionPane.showMessageDialog(this.frame, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Destroys the window.
     */
    public void dispose() {
        frame.setVisible(false);
        frame.dispose();
        frame = null;
    }

    /**
     * Registers an observer to be notified when the window exits.
     *
     * @param observer The observer that wants to watch the exit event.
     */
    public void registerForExitEvent(Observer observer) {
        exitEvent.addObserver(observer);
    }

    /**
     * Registers an observer to be notified when the data file is loaded.
     *
     * @param observer The observer that wants to watch this event.
     */
    public void registerForDataFileLoadedEvent(Observer observer) {
        dataFileLoadedEvent.addObserver(observer);
    }

    /**
     * Registers an observer to be notified when an entry has changed.
     *
     * @param observer The observer that wants to watch this event.
     */
    public void registerForEntriesChangedEvent(Observer observer) {
        entriesChangedEvent.addObserver(observer);
    }

    /**
     * Registers an observer to be notified when the data file is loaded.
     *
     * @param observer The observer that wants to watch this event.
     */
    public void registerForUsersChangedEvent(Observer observer) {
        usersChangedEvent.addObserver(observer);
    }

    /**
     * Called when the data file is loaded, and fires the event.
     * TODO: Move to the data file?
     */
    public void dataFileLoaded() {
        dataFileLoadedEvent.notifyEvent();
    }

    /**
     * Called when the list of users have been changed.
     * TODO: Move to the data file?
     */
    public void usersChanged() {
        usersChangedEvent.notifyEvent();
    }

    /**
     * Called when the list of entries has been changed.
     * TODO: Move to the data file?
     */
    public void entriesChanged() {
        entriesChangedEvent.notifyEvent();
    }

    /**
     * Gives the view a pointer to the controller.
     *
     * @param controller The controller object that we're to use to talk to the data file.
     */
    public void setController(AppController controller) {
        this.controller = controller;
    }

    /**
     * Gets a pointer to the controller.
     *
     * @return A handle to the controller.
     */
    public AppController getController() {
        return controller;
    }
}
