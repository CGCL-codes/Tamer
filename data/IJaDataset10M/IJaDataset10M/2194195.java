package be.vds.jtbdive.view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Action;
import javax.swing.JFrame;
import org.apache.log4j.Logger;
import be.vds.jtbdive.actions.EditLogBookAction;
import be.vds.jtbdive.actions.ExitAction;
import be.vds.jtbdive.actions.NewLogBookAction;
import be.vds.jtbdive.actions.OpenAction;
import be.vds.jtbdive.actions.ShowAboutWindowAction;
import be.vds.jtbdive.actions.ShowConsoleAction;
import be.vds.jtbdive.actions.ShowDiveLocationDetailAction;
import be.vds.jtbdive.actions.ShowDiveLocationManagerAction;
import be.vds.jtbdive.actions.ShowDiveProfileGraphicAction;
import be.vds.jtbdive.actions.ShowDiverDetailAction;
import be.vds.jtbdive.actions.ShowDiverManagerAction;
import be.vds.jtbdive.actions.ShowToolBarDiveAction;
import be.vds.jtbdive.actions.ShowToolBarDiveLocationAction;
import be.vds.jtbdive.actions.ShowToolBarDiverAction;
import be.vds.jtbdive.actions.ShowToolBarImportAction;
import be.vds.jtbdive.logging.ContinuousAppender;
import be.vds.jtbdive.model.LogBookApplicationFacade;
import be.vds.jtbdive.utils.ResourceManager;
import be.vds.jtbdive.view.util.WindowManager;

public class LogBookApplicationJFrame extends JFrame implements WindowListener, Observer {

    private static final Logger logger = Logger.getLogger(LogBookApplicationJFrame.class);

    public static final int ACTION_EXIT_APPLICATION = 1;

    public static final int ACTION_OPEN_LOGBOOK = 2;

    public static final int ACTION_SHOW_DIVERMANAGER = 3;

    public static final int ACTION_SHOW_DIVEPROFILE = 4;

    public static final int ACTION_SHOW_CONSOLE = 5;

    public static final int ACTION_SHOW_DIVER_DETAIL = 6;

    public static final int ACTION_NEW_LOGBOOK = 7;

    public static final int ACTION_SHOW_ABOUT = 8;

    public static final int ACTION_SHOW_DIVELOCATION_MANAGER = 9;

    public static final int ACTION_SHOW_DIVELOCATION_DETAIL = 10;

    public static final int ACTION_EDIT_LOGBOOK = 11;

    public static final int ACTION_SHOW_TOOLBAR_DIVELOCATION = 12;

    public static final int ACTION_SHOW_TOOLBAR_DIVER = 13;

    public static final int ACTION_SHOW_TOOLBAR_DIVE = 14;

    public static final int ACTION_SHOW_TOOLBAR_IMPORT = 15;

    private LogBookApplicationMainPanel logBookApplicationMainPanel;

    private LogBookApplicationFacade logBookApplicationFacade;

    private Map<Integer, Action> actions = new HashMap<Integer, Action>();

    private LogBookApplicationJMenuBar menuBar;

    public LogBookApplicationJFrame(LogBookApplicationFacade logBookApplicationFacade) {
        this.logBookApplicationFacade = logBookApplicationFacade;
        this.logBookApplicationFacade.addObserver(this);
        this.addWindowListener(this);
        init();
    }

    private void init() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("JtB Dive LogBook");
        this.setIconImage(ResourceManager.getImageIcon("logo.gif").getImage());
        createActions();
        createLogBookApplicationMainPanel();
        this.getContentPane().add(logBookApplicationMainPanel);
        menuBar = new LogBookApplicationJMenuBar(this);
        this.setJMenuBar(menuBar);
        this.pack();
        WindowManager.centerWindow(this);
        logBookApplicationMainPanel.setDefaultDividerLocation();
    }

    private void createActions() {
        ExitAction exitAction = new ExitAction();
        actions.put(ACTION_EXIT_APPLICATION, exitAction);
        OpenAction openAction = new OpenAction(this);
        actions.put(ACTION_OPEN_LOGBOOK, openAction);
        NewLogBookAction newLogBookAction = new NewLogBookAction(this);
        actions.put(ACTION_NEW_LOGBOOK, newLogBookAction);
        ShowDiverManagerAction showDiverManagerAction = new ShowDiverManagerAction(this);
        actions.put(ACTION_SHOW_DIVERMANAGER, showDiverManagerAction);
        ShowDiveProfileGraphicAction showDiveProfileGraphicAction = new ShowDiveProfileGraphicAction(this);
        actions.put(ACTION_SHOW_DIVEPROFILE, showDiveProfileGraphicAction);
        ShowConsoleAction showConsoleAction = new ShowConsoleAction(this);
        actions.put(ACTION_SHOW_CONSOLE, showConsoleAction);
        ShowDiverDetailAction showDiverDetailAction = new ShowDiverDetailAction(this);
        actions.put(ACTION_SHOW_DIVER_DETAIL, showDiverDetailAction);
        ShowAboutWindowAction showAboutWindowAction = new ShowAboutWindowAction();
        actions.put(ACTION_SHOW_ABOUT, showAboutWindowAction);
        ShowDiveLocationManagerAction showDiveLocationManagerAction = new ShowDiveLocationManagerAction(this);
        actions.put(ACTION_SHOW_DIVELOCATION_MANAGER, showDiveLocationManagerAction);
        ShowDiveLocationDetailAction showDiveLocationDetailAction = new ShowDiveLocationDetailAction(this);
        actions.put(ACTION_SHOW_DIVELOCATION_DETAIL, showDiveLocationDetailAction);
        EditLogBookAction editLogBookAction = new EditLogBookAction(this);
        editLogBookAction.setEnabled(false);
        actions.put(ACTION_EDIT_LOGBOOK, editLogBookAction);
        ShowToolBarDiveLocationAction showToolBarDiveLocationAction = new ShowToolBarDiveLocationAction(this);
        actions.put(ACTION_SHOW_TOOLBAR_DIVELOCATION, showToolBarDiveLocationAction);
        ShowToolBarDiverAction showToolBarDiverAction = new ShowToolBarDiverAction(this);
        actions.put(ACTION_SHOW_TOOLBAR_DIVER, showToolBarDiverAction);
        ShowToolBarDiveAction showToolBarDiveAction = new ShowToolBarDiveAction(this);
        actions.put(ACTION_SHOW_TOOLBAR_DIVE, showToolBarDiveAction);
        ShowToolBarImportAction showToolBarImportAction = new ShowToolBarImportAction(this);
        actions.put(ACTION_SHOW_TOOLBAR_IMPORT, showToolBarImportAction);
    }

    private void createLogBookApplicationMainPanel() {
        logBookApplicationMainPanel = new LogBookApplicationMainPanel(this, logBookApplicationFacade);
    }

    public Action getAction(int action) {
        return actions.get(action);
    }

    public void shutDown() {
        logger.info("application shutting down");
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        saveAllDives();
        shutDown();
    }

    private void saveAllDives() {
        logBookApplicationMainPanel.closeAllDives();
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    public void openLogBook(long id) {
        logBookApplicationFacade.openLogBook(id);
    }

    public LogBookApplicationFacade getLogBookApplicationFacade() {
        return logBookApplicationFacade;
    }

    public LogBookApplicationMainPanel getLogBookApplicationMainPanel() {
        return logBookApplicationMainPanel;
    }

    public void setContinuousAppender(ContinuousAppender continuousAppender) {
        logBookApplicationMainPanel.setContinuousAppender(continuousAppender);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg.equals("logbook opened")) {
            getAction(ACTION_EDIT_LOGBOOK).setEnabled(true);
        }
    }

    public void enableDiveToolBar(boolean enable) {
        menuBar.enableDiveToolBar(enable);
        logBookApplicationMainPanel.toggleDiveToolBar();
    }
}
