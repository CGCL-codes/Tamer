package src.main;

import src.gui.AboutWindow;
import src.gui.AnalyseWindow;
import src.gui.BikeInfoWindow;
import src.gui.ConfigWindow;
import src.gui.DiagramDialogWindow;
import src.gui.DiagramDrawPad;
import src.gui.ExerciseWindow;
import src.gui.GetOverviewWindow;
import src.gui.ReminderWindow;
import src.gui.UserInfoWindow;
import src.gui.WatchInfoWindow;
import src.gui.WorkoutWindow;
import src.parser.Workout;
import src.util.Zs710Exception;
import src.util.Zs710Message;
import ewe.filechooser.FileChooser;
import ewe.fx.Color;
import ewe.fx.mImage;
import ewe.sys.TaskObject;
import ewe.sys.Vm;
import ewe.sys.mThread;
import ewe.ui.ControlEvent;
import ewe.ui.Event;
import ewe.ui.EventListener;
import ewe.ui.Form;
import ewe.ui.MenuEvent;
import ewe.ui.MessageBox;
import ewe.ui.ScrollBarPanel;

/**
 * Controller - MVC-Pattern
 * 
 * @author Karl Neuhold
 */
public class Zs710Controller implements EventListener {

    /** Data Model - MVC Design */
    private Zs710Model dataModel;

    /** GUI - MVC Design */
    private Zs710View view;

    /**
     * Parameter Beschreibung
     * 
     * @param view
     *            Viewer - MVC-Design
     * @param dataModel
     *            Model for MVC-Design
     */
    public Zs710Controller(Zs710View view, Zs710Model dataModel) {
        this.view = view;
        this.dataModel = dataModel;
        dataModel.openParseConfigFile();
        dataModel.initVectors();
    }

    /**
     * @see ewe.ui.EventListener#onEvent(ewe.ui.Event)
     */
    public void onEvent(Event event) {
        if (event.type == ControlEvent.PRESSED) {
            onGetFiles(event);
            onGetOverviewButton(event);
            onExerciseButton(event);
            onReminderButton(event);
            onUserInfoButton(event);
            onWatchInfoButton(event);
            onSyncTimeButton(event);
            onLogoButton(event);
            onBikeInfoButton(event);
            onAboutButton(event);
            onHardResetbutton(event);
            onConfigButton(event);
            onDiagramDialogButton(event);
            onDiagramAnalyseButton(event);
            onShowWorkoutButton(event);
        }
        onMenuEvent(event);
    }

    /**
     * @param event Event (Controll Event) 
     */
    private void onAboutButton(Event event) {
        if ((((ControlEvent) event).target == view.aboutButton)) {
            new AboutWindow().execute();
        }
    }

    /**
     * @param event Event (Controll Event) 
     */
    private void onBikeInfoButton(Event event) {
        if ((((ControlEvent) event).target == view.bikeInfoButton)) {
            new BikeInfoWindow(dataModel).execute();
        }
    }

    /**
     * @param event Event (Controll Event) 
     */
    private void onLogoButton(Event event) {
        if ((((ControlEvent) event).target == view.logoButton)) {
            new Zs710Message("System Message ...", "Not implemented!");
        }
    }

    /**
     * @param event Event (Controll Event) 
     */
    private void onSyncTimeButton(Event event) {
        if ((((ControlEvent) event).target == view.syncTimeButton)) {
            dataModel.syncTime();
        }
    }

    /**
     * @param event Event (Controll Event) 
     */
    private void onWatchInfoButton(Event event) {
        if ((((ControlEvent) event).target == view.watchInfoButton)) {
            new WatchInfoWindow(dataModel).execute();
        }
    }

    /**
     * @param event Event (Controll Event) 
     */
    private void onUserInfoButton(Event event) {
        if ((((ControlEvent) event).target == view.userInfoButton)) {
            new UserInfoWindow(dataModel).execute();
        }
    }

    /**
     * @param event Event (Controll Event) 
     */
    private void onReminderButton(Event event) {
        if ((((ControlEvent) event).target == view.reminderButton)) {
            new ReminderWindow(dataModel).execute();
        }
    }

    /**
     * @param event Event (Controll Event) 
     */
    private void onExerciseButton(Event event) {
        if ((((ControlEvent) event).target == view.exerciseButton)) {
            new ExerciseWindow(dataModel).execute();
        }
    }

    /**
     * @param event Event (Controll Event) 
     */
    private void onGetOverviewButton(Event event) {
        if ((((ControlEvent) event).target == view.getOverviewButton)) {
            new GetOverviewWindow(dataModel).execute();
        }
    }

    /**
     * @param event Event (Controll Event) 
     */
    private void onMenuEvent(Event event) {
        if (event.type == MenuEvent.SELECTED) {
            if (((MenuEvent) event).selectedItem.equals("Quit")) {
                view.closeAll(0);
            }
            if (((MenuEvent) event).selectedItem.equals("Open")) {
                fileChooserDialog();
            }
        }
    }

    /**
     * @param event Event (Controll Event) 
     */
    private void onShowWorkoutButton(Event event) {
        if ((((ControlEvent) event).target == view.showWorkoutButton)) {
            if (dataModel.getWorkouts().getCount() > 0) {
                WorkoutWindow showWorkout = new WorkoutWindow(dataModel);
                showWorkout.execute();
            } else {
                new Zs710Exception("No workout available!\nOpen or download workout first");
            }
        }
    }

    /**
     * @param event Event (Controll Event) 
     */
    private void onDiagramAnalyseButton(Event event) {
        if ((((ControlEvent) event).target == view.diagramAnalyseButton)) {
            AnalyseWindow showAnalyse = new AnalyseWindow(dataModel);
            showAnalyse.execute();
        }
    }

    /**
     * @param event Event (Controll Event) 
     */
    private void onConfigButton(Event event) {
        if ((((ControlEvent) event).target == view.configButton)) {
            ConfigWindow config = new ConfigWindow();
            config.execute();
            dataModel.openParseConfigFile();
        }
    }

    /**
     * @param event Event (Controll Event) 
     */
    private void onDiagramDialogButton(Event event) {
        if ((((ControlEvent) event).target == view.diagramDialogButton)) {
            DiagramDialogWindow diagramDialog = new DiagramDialogWindow(dataModel);
            Form curvesWindow = new Form();
            Workout workoutToDraw = diagramDialog.getWorkoutToDraw();
            DiagramDrawPad draw = new DiagramDrawPad(workoutToDraw, diagramDialog.getCurvesToPlot(), diagramDialog.isTimeForBottomAxis());
            ScrollBarPanel scrollBar = new ScrollBarPanel(draw);
            scrollBar.setOptions(ScrollBarPanel.Permanent);
            if (workoutToDraw == null) {
                curvesWindow.close(0);
            } else {
                curvesWindow.setTitle("" + workoutToDraw.getDate().format("yyyy'-'MM'-'dd' 'HH':'mm"));
                curvesWindow.addLast(scrollBar);
                curvesWindow.execute();
            }
        }
    }

    /**
     * @param event
     *            Event (Controll Event)
     */
    private void onHardResetbutton(Event event) {
        if ((((ControlEvent) event).target == view.hardResetButton)) {
            MessageBox warning = new MessageBox("Hard Reset: ", "Reset watch to factory defaults?", MessageBox.MBOKCANCEL);
            warning.icon = new mImage("ewe/Stop.bmp", Color.White);
            int returnValue = warning.execute();
            if (returnValue != -1) {
                dataModel.hardReset();
                new Zs710Message("System Message ...", "Ok, you asked for it ...\nHard reset sendet!");
            }
        }
    }

    /**
     * @param event
     *            Event (Controll Event)
     */
    private void onGetFiles(Event event) {
        if ((((ControlEvent) event).target == view.getFiles)) {
            new mThread() {

                public void run() {
                    downloadWorkouts();
                }

                ;
            }.start();
        }
    }

    /**
     * 
     * File open (file chooser) dialog.
     */
    private void fileChooserDialog() {
        FileChooser fileChooser = new FileChooser(FileChooser.OPEN, dataModel.getPathToWorkouts());
        iniFileChooser(fileChooser);
        if (fileChooser.execute() == FileChooser.IDCANCEL) {
            fileChooser.close(0);
            view.textOuput.clear(true);
            view.initConsole();
        } else {
            final String filename = fileChooser.getChosen();
            TaskObject task = new TaskObject() {

                public void doRun() {
                    yield(100);
                    Vm.setCursor(Vm.BUSY_CURSOR);
                    dataModel.parseWorkout(filename);
                    view.textOuput.clear(true);
                    showWorkoutsAtConsole();
                    Vm.setCursor(Vm.DEFAULT_CURSOR);
                }
            };
            task.startTask();
        }
    }

    /**
     * @param fileChooser Initialize FileChooser 
     */
    private void iniFileChooser(FileChooser fileChooser) {
        fileChooser.title = "Open saved workout ...";
        fileChooser.addMask("*.srd,*.hrm - Workout files.");
        fileChooser.addMask("*.* - all Files");
    }

    /**
     * 
     * If download button is clicked, download all saved workouts from s710.
     */
    private void downloadWorkouts() {
        dataModel.getFiles();
        if (dataModel.handle != null) {
            view.textOuput.clear(true);
            view.textOuput.refresh();
            view.setCursor(Vm.BUSY_CURSOR);
            view.progressBar.execute(dataModel.handle, "downloading");
            showDownloadedWorkout();
        }
    }

    /**
     *  Show workout information in console. 
     */
    private void showDownloadedWorkout() {
        view.textOuput.append(dataModel.getConsoleOutput(), true);
        view.textOuput.append("=======================================\n\n", true);
        showWorkoutsAtConsole();
        view.setCursor(Vm.DEFAULT_CURSOR);
    }

    /**
     * Writes all workout data to the console. 
     */
    private void showWorkoutsAtConsole() {
        for (int i = 0; i < dataModel.getWorkouts().getCount(); i++) {
            view.textOuput.append(dataModel.getWorkouts().get(i).toString(), true);
            view.textOuput.append("=======================================\n\n", true);
        }
    }

    /**
     * toString methode: creates a String representation of the object
     * @return the String representation
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Zs710Controller[");
        buffer.append("dataModel = ").append(dataModel);
        buffer.append(", view = ").append(view);
        buffer.append("]");
        return buffer.toString();
    }
}
