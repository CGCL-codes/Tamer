package weborbs;

import org.python.core.*;
import org.python.util.*;
import java.util.Vector;
import java.io.*;

/**
 *The overall application, containing all open projects, the Jython interpretor, and the list of widgets
 * @author Ben
 */
public class Application {

    /**
     * The Jython interpretor that executes the widgets' Jython code
     */
    PythonInterpreter interp;

    /**
     * A list of all currently open projects.
     */
    Vector projects;

    /**
     * All the currently imported widgets available to be added to web pages.
     */
    Vector widgets;

    /**
     * The directory that the application is stored in. Contains a preferences file, along with a directory
     * containing all the widgets' code
     */
    File directory;

    /**
     * Makes new Application
     * @param _projects A vector of all currently open projects
     * @param _widgets A vector of all imported and available widgets to be added to pages.
     * @param _directory The <b>base</b> directory, where the application is installed. The preferences are in a subdirectory from here.
     */
    public Application(Vector _projects, Vector _widgets, File _directory) {
        interp = new PythonInterpreter();
        projects = _projects;
        widgets = _widgets;
        directory = _directory;
    }

    /**
     * Loads the application preferences from the given base directory, then instantiates a new Application from them.
     * @param _directory The <b>base</b> directory, where the application is installed. The preferences are in a subdirectory from here.
     * @return
     */
    public static Application loadApplication(File _directory) {
        String tempPath = _directory.getPath();
        if (!tempPath.endsWith("/")) {
            tempPath += "/";
        }
        tempPath += "preferences/preferences.txt";
        File prefDir = new File(tempPath);
        Vector _projects = new Vector();
        Vector _widgets = new Vector();
        if (prefDir.exists()) {
            try {
                FileInputStream ins = new FileInputStream(prefDir);
                ObjectInputStream obs = new ObjectInputStream(ins);
                _projects = (Vector) obs.readObject();
                _widgets = (Vector) obs.readObject();
                obs.close();
                ins.close();
            } catch (Exception e) {
                System.err.println("Preferences File Error: Loading failed.");
            }
        } else {
            System.err.println("Startup Notice: No existing preferences file. Creating new one...");
            try {
                prefDir.createNewFile();
            } catch (Exception e) {
                System.err.println("Startup Error: Failed to create new preferences file.");
            }
        }
        return new Application(_projects, _widgets, _directory);
    }

    /**
     * Initializes the application
     */
    public void init() {
    }
}
