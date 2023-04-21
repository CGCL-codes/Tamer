package net.sf.mzmine.main.mzmineviewer;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import net.sf.mzmine.desktop.impl.MainWindow;
import net.sf.mzmine.main.MZmineCore;
import net.sf.mzmine.main.MZmineModule;
import net.sf.mzmine.main.MZminePreferences;
import net.sf.mzmine.modules.peaklistmethods.io.xmlimport.XMLImporter;
import net.sf.mzmine.project.impl.ProjectManagerImpl;
import net.sf.mzmine.taskcontrol.impl.TaskControllerImpl;

public class MZviewer extends MZmineCore implements Runnable {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private static MZviewer client = new MZviewer();

    private Vector<MZmineModule> moduleSet;

    private String args[];

    private MZviewer() {
    }

    public static MZviewer getInstance() {
        return client;
    }

    /**
     * Main method
     */
    public static void main(String args[]) {
        client.args = args;
        SwingUtilities.invokeLater(client);
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        MainWindow desktop = null;
        try {
            logger.info("MZviewer starting");
            MZmineCore.preferences = new MZminePreferences();
            TaskControllerImpl taskController = new TaskControllerImpl();
            desktop = new MainWindow();
            ProjectManagerImpl projectManager = new ProjectManagerImpl();
            MZmineCore.taskController = taskController;
            MZmineCore.desktop = desktop;
            MZmineCore.projectManager = projectManager;
            logger.finer("Initializing core classes");
            projectManager.initModule();
            desktop.initModule();
            taskController.initModule();
            logger.finer("Loading modules");
            moduleSet = new Vector<MZmineModule>();
            for (MZviewerModuleName module : MZviewerModuleName.values()) {
                loadModule(module.getClassName());
            }
            MZmineCore.initializedModules = moduleSet.toArray(new MZmineModule[0]);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not initialize MZviewer ", e);
            System.exit(1);
        }
        logger.finest("Showing main window");
        desktop.setVisible(true);
        desktop.setStatusBarText("Welcome to MZviewer!");
        if (args.length > 0) {
            logger.finer("Loading peak lists from arguments");
            XMLImporter.myInstance.loadPeakLists(args);
        }
    }

    public MZmineModule loadModule(String moduleClassName) {
        try {
            logger.finest("Loading module " + moduleClassName);
            Class moduleClass = Class.forName(moduleClassName);
            MZmineModule moduleInstance = (MZmineModule) moduleClass.newInstance();
            moduleInstance.initModule();
            moduleSet.add(moduleInstance);
            return moduleInstance;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not load module " + moduleClassName, e);
            return null;
        }
    }
}
