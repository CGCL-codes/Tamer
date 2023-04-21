package sourced;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventObject;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class SourcedApp extends SingleFrameApplication {

    SourcedView view;

    String appDir;

    PreferenceManager prefMan;

    String[] supportedFiles = { ".php", ".html", ".xml", ".java", ".rhtml", ".js", ".css", ".rb", ".txt" };

    String[] supportedImages = { ".jpg", ".png", ".jpeg", ".gif" };

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        TempCleaner cleaner = new TempCleaner();
        view = new SourcedView(this);
        String p = SourcedApp.class.getResource("").getPath();
        if (p.indexOf(":") > -1) {
            p = p.substring((p.indexOf(":") + 1));
        }
        if (p.indexOf(".jar!") > 0) {
            while (p.indexOf(".jar!") > -1) {
                System.out.println(p);
                p = p.substring(0, p.lastIndexOf("/"));
            }
            appDir = p;
        } else {
            appDir = System.getProperty("user.dir");
        }
        prefMan = new PreferenceManager(appDir);
        this.addExitListener(new ExitListener() {

            public boolean canExit(EventObject arg) {
                prefMan.savePreferences();
                if (!view.checkSaveStatus()) {
                    return true;
                }
                return false;
            }

            public void willExit(EventObject arg0) {
            }
        });
        loadPreferences();
        this.view.setIcon(getSystemIcon());
        show(view);
    }

    public void killApp() {
        this.end();
    }

    public boolean isSupportedImage(String img) {
        String temp = img.toLowerCase();
        for (int i = 0; i < supportedImages.length; i++) {
            if (temp.endsWith(supportedImages[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Opens new project dialog
     */
    public void newProject() {
        NewRemoteProject remoteProject = new NewRemoteProject();
        remoteProject.setVisible(true);
    }

    /**
     * Checks if file type is supported
     * @param file
     * @return
     */
    public boolean isSupportedFile(String file) {
        String temp = file.toLowerCase();
        for (int i = 0; i < supportedFiles.length; i++) {
            if (temp.endsWith(supportedFiles[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Loads preferences and, if neccessary, displays password entry tab
     */
    private void loadPreferences() {
        ArrayList<Project> passwordProjs = new ArrayList<Project>();
        try {
            Preferences prefs = prefMan.getPreferences();
            ArrayList<Project> projects = prefs.getProjects();
            for (int i = 0; i < projects.size(); i++) {
                Project proj = projects.get(i);
                if (proj.requiresPassword()) {
                    passwordProjs.add(proj);
                } else {
                    setupProject(proj);
                }
            }
        } catch (NullPointerException e) {
        }
        showPasswordRequestScreen(passwordProjs);
    }

    public void showPasswordRequestScreen(ArrayList<Project> projects) {
        if (projects.size() > 0) {
            view.addTab("Password Required", new PasswordRequiredPanel(projects));
        }
    }

    public ImageIcon decideIcon(String icon) {
        ClassLoader loader = getClass().getClassLoader();
        URL fileLocation = loader.getResource("icons/" + icon);
        Image img = Toolkit.getDefaultToolkit().getImage(fileLocation);
        return new ImageIcon(img);
    }

    public Image getSystemIcon() {
        String icon = "sourced_icon.png";
        ClassLoader loader = getClass().getClassLoader();
        URL fileLocation = loader.getResource("icons/" + icon);
        Image img = Toolkit.getDefaultToolkit().getImage(fileLocation);
        return img;
    }

    /**
     * Removes a project, from preferences and side listing.
     * 
     * @param project
     */
    public void removeProject(Project project) {
        prefMan.removeProject(project);
        view.removeProjectListing(project);
    }

    public void addProject(Project project) {
        System.out.println("SourcedApp " + project.getName());
        setupProject(project);
        prefMan.addProject(project);
    }

    public void setupProject(Project project) {
        System.out.println("Establishing project");
        if (project.establish()) {
            System.out.println("Project established");
            view.addProjectListing(project);
        } else {
            ResourceMap resourceMap = Application.getInstance(sourced.SourcedApp.class).getContext().getResourceMap(SourcedApp.class);
            this.flagError(resourceMap.getString("error.could_not_open_project") + project.getName());
            SourcedApp.getApplication().removeProject(project);
        }
    }

    /**
     * Calls the view to open a new tab containing an instance of JcTerm
     */
    public void openTerminal() {
    }

    public void flagError(String msg) {
        view.displayErrorMessage(msg);
    }

    public void updateStats(int lineNum, int caretPos) {
        view.updateStats(lineNum, caretPos);
    }

    public String getAppDirectory() {
        return appDir;
    }

    public void setFileEdited() {
        view.setEdited();
    }

    public void setFileSaved() {
        view.setSaved();
    }

    public void closeTab(JComponent comp) {
        view.closeTab(comp);
    }

    public boolean findText(String text, boolean matchCase) {
        return view.findText(text, matchCase);
    }

    public boolean gotoLine(int line) {
        return view.gotoLine(line);
    }

    public void replaceText(String find, String replace, boolean matchCase, boolean onlySelection) {
        view.replaceText(find, replace, matchCase, onlySelection);
    }

    public void launchQuickFind() {
        view.launchQuickFind();
    }

    public void launchGotoLine() {
        view.launchGotoLine();
    }

    public void clearUtility() {
        view.clearUtility();
    }

    public boolean checkActiveSaved() {
        return view.checkSavedActiveStatus();
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of SourcedApp
     */
    public static SourcedApp getApplication() {
        return Application.getInstance(SourcedApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(SourcedApp.class, args);
    }
}
