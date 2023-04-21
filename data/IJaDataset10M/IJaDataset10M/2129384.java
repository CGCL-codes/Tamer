package vivace.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import vivace.exception.AppException;

/**
 * Models the application state and keeps tracks of the currently opened projects.
 * This class creates a singleton instance of itself the first time a method is called.
 * The singleton instance is not accessible outside this class, but are used by the static
 * methods indirectly.
 */
public class App extends Observable implements Observer {

    private static App theInstance = new App();

    /**
	 * Returns whether any of the observables in the model is 
	 * currently notifying it's observers 
	 * @return
	 */
    public static boolean isNotifying() {
        return isNotifying;
    }

    private static Integer activeProjectKey;

    private static HashMap<Integer, Tuple<Project, UI>> projects;

    /** Public references to the current project model and it's GUI */
    public static Project Project;

    public static UI UI;

    /** Returns whether there exists any opened projects or not **/
    public static boolean hasProjects() {
        return !projects.isEmpty();
    }

    /** Returns an iterator over all opened projects **/
    public static Iterator<Map.Entry<Integer, Tuple<Project, UI>>> getProjectsIterator() {
        return projects.entrySet().iterator();
    }

    /** Returns the key of the active project **/
    public static Integer getActiveProjectKey() {
        return activeProjectKey;
    }

    private App() {
        activeProjectKey = 0;
        projects = new HashMap<Integer, Tuple<Project, UI>>();
        Project = null;
        UI = null;
    }

    /**
	 * @deprecated  Use addAppObserver() or addProjectObserver() instead
	 */
    @Deprecated
    public synchronized void addObserver(Observer o) {
        ;
    }

    private void addObserver(Observer o, int dummy) {
        super.addObserver(o);
    }

    /** Adds an AppObserver to the model. An AppObservers will be notified about all updates. **/
    public static void addAppObserver(Observer o) {
        theInstance.addObserver(o, 0);
    }

    /** Adds a ProjectObserver to the model. A ProjectObserver will be notified when something is changed 
	 * for the specific project. The parameter listenTo can be used to specify which kind of updates that
	 * the ProjectObserver wants to get. **/
    public static void addProjectObserver(Observer o, Source listenTo) {
        if (listenTo == Source.MODEL) {
            Project.addObserver(o);
        } else if (listenTo == Source.UI) {
            UI.addObserver(o);
        } else {
            Project.addObserver(o);
            UI.addObserver(o);
        }
    }

    /**
	 * Removes an observer from the active project
	 * @param o
	 */
    public static void removeProjectObserver(Observer o) {
        Project.deleteObserver(o);
        UI.deleteObserver(o);
    }

    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    private static boolean isNotifying = false;

    private static boolean timerStarted = false;

    static Timer t;

    protected static void setNotifying(boolean value) {
        isNotifying = value;
        theInstance.handleNotification();
    }

    private void handleNotification() {
        if (slowTaskListener == null) return;
        if (t != null) {
            t.cancel();
        }
        if (isNotifying) {
            t = new Timer();
            t.schedule(new SlowTaskTimer(), slowTaskListener.getDelay());
        } else if (timerStarted) {
            timerStarted = false;
            theInstance.slowTaskListener.slowTaskFinished();
        }
    }

    private class SlowTaskTimer extends TimerTask {

        public void run() {
            timerStarted = true;
            slowTaskListener.slowTaskStarted();
            this.cancel();
        }
    }

    private SlowTaskListener slowTaskListener;

    /**
	 * Sets the listener for slow model performance
	 */
    public static void setSlowTaskListener(SlowTaskListener s) {
        theInstance.slowTaskListener = s;
    }

    /**
	 * Sets the active project
	 * @param key The key of the project that should be set to active
	 * @param removeUnusedListeners Set to true if you want to remove listeners to the previously active project (if any).
	 */
    public static void setActiveProject(Integer key, boolean removeUnusedListeners) {
        if (removeUnusedListeners) {
            removeListeners();
        }
        activeProjectKey = key;
        updateProjectReferences();
        theInstance.setChanged();
        theInstance.notifyObservers(Action.PROJECT_SWITCHED);
    }

    /**
	 * Adds a new, empty project to the application
	 * @param removeUnusedListeners Set to true if you want to remove listeners to the previously active project (if any).
	 */
    public static void addProject(boolean removeUnusedListeners) throws AppException {
        Integer newKey = new Integer(projects.size() + 1);
        try {
            Project p = new Project();
            p.setName("Untitled " + newKey.toString());
            UI g = new UI();
            addProject(newKey, p, g, removeUnusedListeners);
        } catch (MidiUnavailableException e) {
            throw new AppException("E_MIDI_DEVICE_UNAVAILABLE");
        }
    }

    /**
	 * Adds a new project to the application.
	 * @param removeUnusedListeners Set to true if you want to remove listeners to the previously active project (if any).
	 */
    public static void addProject(File file, boolean removeUnusedListeners) throws AppException {
        Integer newKey = new Integer(projects.size() + 1);
        try {
            Project p = new Project(file);
            p.setName(file.getName());
            UI g = new UI();
            addProject(newKey, p, g, removeUnusedListeners);
        } catch (IOException e) {
            throw new AppException("E_COULD_NOT_OPEN_FILE");
        } catch (InvalidMidiDataException e) {
            throw new AppException("E_INVALID_MIDI_DATA");
        } catch (MidiUnavailableException e) {
            throw new AppException("E_MIDI_DEVICE_UNAVAILABLE");
        }
    }

    private static void addProject(Integer key, Project p, UI g, boolean removeUnusedListeners) {
        if (removeUnusedListeners) {
            removeListeners();
        }
        HashSet<Integer> selection = new HashSet<Integer>();
        selection.add(new Integer(0));
        g.setTrackSelection(selection);
        Tuple<Project, UI> t = new Tuple<Project, UI>(p, g);
        projects.put(key, t);
        activeProjectKey = key;
        updateProjectReferences();
        theInstance.setChanged();
        theInstance.notifyObservers(Action.PROJECT_ADDED);
    }

    /**
	 * Closes all open projects
	 */
    public static void closeAllProjects() {
        removeAllListeners();
        if (Project.isPlaying()) {
            Project.stop();
        }
        projects.clear();
        activeProjectKey = 0;
        updateProjectReferences();
        theInstance.setChanged();
        theInstance.notifyObservers(Action.ALL_CLOSED);
    }

    /**
	 * Closes the active project
	 */
    public static void closeProject() {
        removeListeners();
        if (Project.isPlaying()) {
            Project.stop();
        }
        projects.remove(activeProjectKey);
        activeProjectKey = getNewActive();
        updateProjectReferences();
        theInstance.setChanged();
        theInstance.notifyObservers(Action.PROJECT_CLOSED);
    }

    /**
	 * Returns a Vector containing all the unsaved projects
	 * @return
	 */
    public static Vector<Project> getUnsavedProjects() {
        Iterator<Map.Entry<Integer, Tuple<Project, UI>>> projects = getProjectsIterator();
        Map.Entry<Integer, Tuple<Project, UI>> e;
        Project p;
        Vector<Project> unsavedProjects = new Vector<Project>();
        while (projects.hasNext()) {
            e = projects.next();
            p = e.getValue().a();
            if (!p.getIsSaved()) {
                unsavedProjects.add(p);
            }
        }
        return unsavedProjects;
    }

    private static void removeListeners() {
        if (!projects.isEmpty()) {
            Project.deleteObservers();
            UI.deleteObservers();
        }
    }

    private static void removeAllListeners() {
        for (Tuple<Project, UI> t : projects.values()) {
            t.a().deleteObservers();
            t.b().deleteObservers();
        }
    }

    private static void updateProjectReferences() {
        if (activeProjectKey == 0) {
            Project = null;
            UI = null;
        } else {
            Project = projects.get(activeProjectKey).a();
            UI = projects.get(activeProjectKey).b();
            Project.addObserver(theInstance);
            UI.addObserver(theInstance);
        }
    }

    private static Integer getNewActive() {
        Integer i = 0;
        Iterator<Integer> it = projects.keySet().iterator();
        if (it.hasNext()) {
            i = it.next();
        }
        return i;
    }

    /**
	 * Saves all opened projects
	 */
    public static void saveAllProjects() {
    }

    /**
	 * Retrieves the available MIDI in devices (input ports) available on the system
	 * @return
	 */
    public static Vector<MidiDevice> getMidiInDevices() {
        Vector<MidiDevice> devices = new Vector<MidiDevice>();
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        devices.add(null);
        MidiDevice device;
        for (MidiDevice.Info i : infos) {
            try {
                device = MidiSystem.getMidiDevice(i);
                if (device.getMaxTransmitters() != 0) {
                    devices.add(device);
                }
            } catch (MidiUnavailableException e) {
            }
        }
        return devices;
    }

    /**
	 * Retrieves the available MIDI out devices (output ports) available on the system
	 * @return
	 */
    public static Vector<MidiDevice> getMidiOutDevices() {
        Vector<MidiDevice> devices = new Vector<MidiDevice>();
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        devices.add(null);
        MidiDevice device;
        for (MidiDevice.Info i : infos) {
            try {
                device = MidiSystem.getMidiDevice(i);
                if (device.getMaxReceivers() != 0) {
                    devices.add(device);
                }
            } catch (MidiUnavailableException e) {
            }
        }
        return devices;
    }

    /**
	 * Enumerator that explains the different sources from 
	 * where a notification can be sent.
	 */
    public static enum Source {

        UI, MODEL, ALL
    }

    /**
	 * Static class that stores constants and used by the application
	 * TODO: Beh�vs denna..borde egentligen ligga i GUIHelper kanske...
	 */
    public static class Constants {

        /** 
		 * The default zoom level
		 */
        public static final int DEFAULT_ZOOM_LEVEL = 100;

        /** 
		 * The number of octaves used
		 */
        public static final int NUMBER_OF_OCTAVES = 10;
    }
}
