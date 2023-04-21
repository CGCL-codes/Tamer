package ch.intertec.storybook.toolkit;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import ch.intertec.storybook.action.ActionManager;
import ch.intertec.storybook.action.ActionManager.SbAction;
import ch.intertec.storybook.action.ActionRegistry;
import ch.intertec.storybook.main.MainFrame;
import ch.intertec.storybook.main.MainSplitPane.ContentPanelType;
import ch.intertec.storybook.model.Internal;
import ch.intertec.storybook.model.InternalPeer;
import ch.intertec.storybook.model.PCSDispatcher;
import ch.intertec.storybook.model.PartPeer;
import ch.intertec.storybook.model.PersistenceManager;
import ch.intertec.storybook.testng.database.GenerateDemoProject;
import ch.intertec.storybook.toolkit.Constants.Preference;
import ch.intertec.storybook.toolkit.Constants.ProjectSetting;
import ch.intertec.storybook.toolkit.filefilter.H2FileFilter;
import ch.intertec.storybook.toolkit.swing.SwingTools;
import ch.intertec.storybook.view.dialog.file.NewFileDialog;
import ch.intertec.storybook.view.dialog.file.RenameFileDialog;
import ch.intertec.storybook.view.dialog.file.SaveAsFileDialog;

public class ProjectTools {

    private static Logger logger = Logger.getLogger(ProjectTools.class);

    private static File projectDir = null;

    private static File currentFile = null;

    public static final String DB_FILE_ENDING = "h2.db";

    public static boolean renameFile() {
        File oldFile = ProjectTools.getCurrentFile();
        JFrame mainFrame = MainFrame.getInstance();
        RenameFileDialog dlg = new RenameFileDialog(mainFrame);
        SwingTools.showModalDialog(dlg, mainFrame);
        if (dlg.isCanceled()) {
            return false;
        }
        File file = dlg.getFile();
        if (!copy(file)) {
            return false;
        }
        updateRecentFiles(file);
        openFile(file.toString());
        return oldFile.delete();
    }

    public static boolean saveFileAs() {
        JFrame mainFrame = MainFrame.getInstance();
        SaveAsFileDialog dlg = new SaveAsFileDialog(mainFrame);
        SwingTools.showModalDialog(dlg, mainFrame);
        if (dlg.isCanceled()) {
            return false;
        }
        File file = dlg.getFile();
        if (!copy(file)) {
            return false;
        }
        updateRecentFiles(file);
        openFile(file.toString());
        return true;
    }

    private static boolean copy(File file) {
        String srcName = PersistenceManager.getInstance().getDatabaseName();
        String srcFileName = srcName + "." + DB_FILE_ENDING;
        File srcFile = new File(srcFileName);
        try {
            FileUtils.copyFile(srcFile, file);
        } catch (IOException e) {
            logger.error(e.getMessage());
            SwingTools.showException(e);
            return false;
        }
        return true;
    }

    public static boolean createNewFile() {
        return createNewFile(false);
    }

    public static boolean createNewFile(boolean isDemo) {
        NewFileDialog dlg = new NewFileDialog(MainFrame.getInstance());
        if (isDemo) {
            dlg.setTfName(I18N.getMsg("msg.file.demo.name"));
        }
        SwingTools.showModalDialog(dlg, MainFrame.getInstance());
        if (dlg.isCanceled()) {
            return false;
        }
        File file = dlg.getFile();
        updateRecentFiles(file);
        return createFile(file, isDemo);
    }

    public static void createDemoFile() {
        try {
            if (!ProjectTools.createNewFile(true)) {
                return;
            }
            GenerateDemoProject demo = new GenerateDemoProject();
            demo.createObjects();
            ActionRegistry.getInstance().getAction(SbAction.REFRESH).actionPerformed(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkIfFileExists(File file) {
        return checkIfFileExists(file, true);
    }

    public static boolean checkIfFileExists(File file, boolean showWarning) {
        File fileName = new File(file + DB_FILE_ENDING);
        File fileNameOld = new File(file + ".data.db");
        if (fileName.exists() || fileNameOld.exists()) {
            if (showWarning) {
                JOptionPane.showMessageDialog(MainFrame.getInstance(), I18N.getMsg("msg.prjtools.project.exits.text", file), I18N.getMsg("msg.prjtools.project.exits.title"), JOptionPane.ERROR_MESSAGE);
            }
            return true;
        }
        return false;
    }

    public static boolean openFile() {
        return openFile(null);
    }

    public static boolean openFile(String filename) {
        File file;
        if (filename == null) {
            final JFileChooser fc = new JFileChooser();
            File dir = new File(PrefManager.getInstance().getStringValue(Preference.LAST_OPENED_DIRECTORY));
            fc.setCurrentDirectory(dir);
            H2FileFilter filter = new H2FileFilter();
            fc.addChoosableFileFilter(filter);
            fc.setFileFilter(filter);
            int ret = fc.showOpenDialog(MainFrame.getInstance());
            if (ret == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(MainFrame.getInstance(), I18N.getMsg("msg.dlg.project.not.exits.text", file), I18N.getMsg("msg.dlg.project.not.exits.title"), JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                return false;
            }
        } else {
            file = new File(filename);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(MainFrame.getInstance(), I18N.getMsg("msg.dlg.project.not.exits.text", file), I18N.getMsg("msg.dlg.project.not.exits.title"), JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        updateRecentFiles(file);
        try {
            if (filename != null) {
                PrefManager.getInstance().setValue(Constants.Preference.LAST_OPENED_FILE, filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return openProject(file);
    }

    public static void updateRecentFiles() {
        updateRecentFiles(null);
    }

    private static void updateRecentFiles(File file) {
        try {
            String csFiles = PrefManager.getInstance().getStringValue(Preference.RECENT_FILES);
            ArrayList<String> files = PrefManager.toStringList(csFiles);
            if (file != null) {
                if (files.contains(file.toString())) {
                    files.remove(file.toString());
                }
                files.add(file.toString());
            }
            Object a[] = files.toArray();
            for (int i = 0; i < a.length; ++i) {
                File f = new File(a[i].toString());
                if (file != null && f.toString().equals(file.toString())) {
                    continue;
                }
                if (!f.exists()) {
                    files.remove(i);
                }
            }
            if (files.size() > 10) {
                files.remove(0);
            }
            PrefManager.getInstance().setValue(Preference.RECENT_FILES, PrefManager.toCsString(files));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public static void saveFile() {
        saveFile(500);
    }

    public static void saveFile(int showClockDelay) {
        PersistenceManager.getInstance().saveTables(showClockDelay);
    }

    public static String getDatabaseName(File file) {
        String path = FilenameUtils.getFullPath(file.getPath());
        String baseName = FilenameUtils.getBaseName(file.getName());
        int i = baseName.lastIndexOf(".");
        if (i == -1) {
            return path + baseName;
        }
        String dbName = baseName.substring(0, i);
        return path + dbName;
    }

    public static boolean isProjectOpen() {
        return PersistenceManager.getInstance().isConnectionOpen();
    }

    @Deprecated
    public static File initProjectDir() {
        File dir = new File(System.getProperty("user.home"));
        return initProjectDir(dir);
    }

    @Deprecated
    public static File initProjectDir(File dir) {
        if (projectDir == null) {
            projectDir = new File(dir + File.separator + ".storybook" + File.separator + Constants.ProjectDirectory.PROJECTS);
            projectDir.mkdir();
        }
        return projectDir;
    }

    public static File getProjectDir() {
        initProjectDir();
        return projectDir;
    }

    public static String getProjectName() {
        String name = PersistenceManager.getInstance().getDatabaseName();
        return getName(name);
    }

    public static String getName(String filename) {
        if (filename != null) {
            File file = new File(filename);
            String name = file.getName();
            return name;
        }
        return "";
    }

    public static boolean isFileOpen(String name) {
        if (!PersistenceManager.getInstance().isConnectionOpen()) {
            return false;
        }
        String openDb = PersistenceManager.getInstance().getDatabaseName();
        return getName(openDb).equals(name);
    }

    public class PrjFilenameFilter implements FilenameFilter {

        private String path;

        public PrjFilenameFilter(String path) {
            this.path = path;
        }

        public boolean accept(File dir, String name) {
            return name.startsWith(path);
        }
    }

    private static boolean createFile(File file, boolean isDemo) {
        MainFrame mainFrame = MainFrame.getInstance();
        SwingTools.setWaitCursor(mainFrame);
        try {
            if (ProjectTools.isProjectOpen()) {
                ActionManager.performAction(SbAction.FILE_CLOSE);
            }
            PersistenceManager.getInstance().create(file);
            PersistenceManager.getInstance().initDbModel(isDemo);
            PersistenceManager.getInstance().getConnection();
            setup(file);
        } catch (Exception e) {
            SwingTools.setDefaultCursor(mainFrame);
            e.printStackTrace();
        }
        SwingTools.setDefaultCursor(mainFrame);
        return true;
    }

    public static File getCurrentFile() {
        return currentFile;
    }

    public static boolean openProject(File file) {
        currentFile = file;
        if (ProjectTools.isProjectOpen()) {
            ActionManager.performAction(SbAction.FILE_CLOSE);
        }
        MainFrame mainFrame = MainFrame.getInstance();
        try {
            SwingTools.setWaitCursor(mainFrame);
            PersistenceManager.getInstance().open(file);
            if (!DbTools.checkAndAlterModel()) {
                closeFile();
                SwingTools.setDefaultCursor(mainFrame);
                return false;
            }
            PersistenceManager.getInstance().getConnection();
        } catch (Exception e) {
            SwingTools.setDefaultCursor(mainFrame);
            e.printStackTrace();
        }
        setup(file);
        SwingTools.setDefaultCursor(mainFrame);
        return true;
    }

    private static void setup(File file) {
        try {
            PrefManager.getInstance().setValue(Preference.LAST_OPENED_DIRECTORY, FilenameUtils.getFullPath(file.toString()));
            PrefManager.getInstance().setValue(Preference.LAST_OPENED_FILE, file.getAbsoluteFile().toString());
        } catch (Exception e) {
            try {
                PrefManager.getInstance().setValue(Preference.LAST_OPENED_DIRECTORY, System.getProperty("user.home"));
            } catch (Exception e1) {
                logger.error(e.getMessage());
            }
        }
        SwingTools.disposeOpenedDialogs();
        restorePartId();
        restoreScaleSettings();
        restoreViewSettings();
        MainFrame.getInstance().getSplitPane().setSmartDividerLocation();
        PCSDispatcher.getInstance().firePropertyChange(PCSDispatcher.Property.PROJECT.toString(), null, true);
    }

    public static void saveView() {
        try {
            Internal internal = InternalPeer.doSelectByKey(Constants.ProjectSetting.VIEW);
            if (internal == null) {
                internal = new Internal();
            }
            internal.setKey(Constants.ProjectSetting.VIEW);
            if (MainFrame.getInstance().isBookPanelActive()) {
                internal.setStringValue(Constants.ProjectSetting.VIEW_BOOK);
            } else if (MainFrame.getInstance().isManagePanelActive()) {
                internal.setStringValue(Constants.ProjectSetting.VIEW_MANAGE);
            } else {
                internal.setStringValue(Constants.ProjectSetting.VIEW_CHRONO);
            }
            internal.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void restoreViewSettings() {
        try {
            Internal internal = InternalPeer.doSelectByKey(Constants.ProjectSetting.VIEW);
            if (internal == null) {
                MainFrame.getInstance().setChronoPanel();
                return;
            }
            ProjectSetting ps = internal.getProjectSetting();
            switch(ps) {
                case VIEW_BOOK:
                    MainFrame.getInstance().setBookPanel();
                    break;
                case VIEW_MANAGE:
                    MainFrame.getInstance().setManagePanel();
                    break;
                case VIEW_CHRONO:
                    MainFrame.getInstance().setChronoPanel();
                    break;
                default:
                    MainFrame.getInstance().setChronoPanel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveAllScales() {
        for (ContentPanelType cpt : ContentPanelType.values()) {
            saveScale(cpt);
        }
    }

    public static void saveScale(ContentPanelType cpt) {
        try {
            Internal internal = null;
            ProjectSetting ps;
            switch(cpt) {
                case BOOK:
                    ps = Constants.ProjectSetting.SCALE_BOOK;
                    break;
                case CHRONO:
                    ps = Constants.ProjectSetting.SCALE_CHRONO;
                    break;
                case MANAGE:
                    ps = Constants.ProjectSetting.SCALE_MANAGE;
                    break;
                default:
                    return;
            }
            internal = InternalPeer.doSelectByKey(ps);
            if (internal == null) {
                internal = new Internal();
                internal.setKey(ps);
            }
            internal.setIntegerValue(cpt.getScale());
            internal.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void restoreScaleSettings() {
        try {
            for (ContentPanelType cpt : ContentPanelType.values()) {
                ProjectSetting ps;
                switch(cpt) {
                    case BOOK:
                        ps = Constants.ProjectSetting.SCALE_BOOK;
                        break;
                    case CHRONO:
                        ps = Constants.ProjectSetting.SCALE_CHRONO;
                        break;
                    case MANAGE:
                        ps = Constants.ProjectSetting.SCALE_MANAGE;
                        break;
                    default:
                        continue;
                }
                Internal internal = InternalPeer.doSelectByKey(ps);
                if (internal == null) {
                    continue;
                }
                int scale = internal.getIntegerValue();
                cpt.setScale(scale);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void savePartId() {
        try {
            if (!isProjectOpen()) {
                return;
            }
            Internal internal = InternalPeer.doSelectByKey(Constants.ProjectSetting.PART_ID);
            if (internal == null) {
                internal = new Internal();
            }
            internal.setKey(Constants.ProjectSetting.PART_ID);
            internal.setIntegerValue(MainFrame.getInstance().getActivePartId());
            internal.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void restorePartId() {
        try {
            Internal internal = InternalPeer.doSelectByKey(Constants.ProjectSetting.PART_ID);
            MainFrame mainFrame = MainFrame.getInstance();
            if (internal == null) {
                mainFrame.setActivePartId(PartPeer.getFirstPart().getId());
                return;
            }
            int partId = internal.getIntegerValue();
            mainFrame.setActivePartId(partId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeFile() {
        if (!isProjectOpen()) {
            return;
        }
        ActionManager.performAction(SbAction.FILE_SAVE);
        saveView();
        saveAllScales();
        savePartId();
        PersistenceManager.getInstance().closeConnection();
        SwingTools.disposeOpenedDialogs();
        PCSDispatcher.getInstance().firePropertyChange(PCSDispatcher.Property.PROJECT.toString(), true, null);
        MainFrame.getInstance().setBlankPanel();
    }
}
