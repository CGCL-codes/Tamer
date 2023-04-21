package ch.intertec.storybook;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import ch.intertec.storybook.SbConstants.PreferenceKey;
import ch.intertec.storybook.controller.PreferenceController;
import ch.intertec.storybook.model.DbFile;
import ch.intertec.storybook.model.PreferenceModel;
import ch.intertec.storybook.model.hbn.entity.Preference;
import ch.intertec.storybook.model.legacy.PersistenceManager;
import ch.intertec.storybook.toolkit.DocumentUtil;
import ch.intertec.storybook.toolkit.I18N;
import ch.intertec.storybook.toolkit.PrefUtil;
import ch.intertec.storybook.toolkit.swing.SwingUtil;
import ch.intertec.storybook.toolkit.swing.splash.HourglassSplash;
import ch.intertec.storybook.view.MainFrame;
import ch.intertec.storybook.view.dialog.ExceptionDialog;
import ch.intertec.storybook.view.dialog.file.NewFileDialog;

/**
 * @author martin
 */
@SuppressWarnings("serial")
public class StorybookApp extends Component {

    private static StorybookApp instance;

    private PreferenceModel preferenceModel;

    private PreferenceController preferenceController;

    private List<MainFrame> mainFrames;

    private StorybookApp() {
        mainFrames = new ArrayList<MainFrame>();
    }

    private void init() {
        try {
            preferenceController = new PreferenceController();
            preferenceModel = new PreferenceModel();
            preferenceController.attachModel(preferenceModel);
            preferenceController.attachView(this);
            Preference pref = PrefUtil.get(PreferenceKey.OPEN_LAST_FILE, false);
            if (pref.getBooleanValue()) {
                Preference pref2 = PrefUtil.get(PreferenceKey.LAST_OPEN_FILE, "");
                DbFile dbFile = new DbFile(pref2.getStringValue());
                System.out.println("StorybookApp.init(): loading... " + dbFile);
                if (dbFile.getFile().exists()) {
                    openFile(dbFile);
                    return;
                } else {
                    System.err.println("StorybookApp.init(): DB file does not exists:" + dbFile);
                }
            }
            MainFrame mainFrame = new MainFrame();
            mainFrame.init();
            mainFrame.initBlankUi();
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionDialog dlg = new ExceptionDialog(e);
            SwingUtil.showModalDialog(dlg, null);
        }
    }

    public PreferenceModel getPreferenceModel() {
        return preferenceModel;
    }

    public PreferenceController getPreferenceController() {
        return preferenceController;
    }

    public List<MainFrame> getMainFrames() {
        return mainFrames;
    }

    public void addMainFrame(MainFrame mainFrame) {
        mainFrames.add(mainFrame);
    }

    public void removeMainFrame(MainFrame mainFrame) {
        mainFrames.remove(mainFrame);
    }

    public void closeBlank() {
        for (MainFrame mainFrame : mainFrames) {
            if (mainFrame.isBlank()) {
                mainFrames.remove(mainFrame);
                mainFrame.dispose();
            }
        }
    }

    public static StorybookApp getInstance() {
        if (instance == null) {
            instance = new StorybookApp();
        }
        return instance;
    }

    public void createNewFile() {
        try {
            NewFileDialog dlg = new NewFileDialog();
            SwingUtil.showModalDialog(dlg, null);
            if (dlg.isCanceled()) {
                return;
            }
            DbFile dbFile = new DbFile(dlg.getFile());
            String dbName = dbFile.getDbName();
            if (dbName == null) {
                return;
            }
            final MainFrame newMainFrame = new MainFrame();
            newMainFrame.init(dbName);
            newMainFrame.getDocumentModel().initEntites();
            newMainFrame.initUi();
            newMainFrame.getDocumentController().fireAgain();
            addMainFrame(newMainFrame);
            closeBlank();
            updateFilePref(dbFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openFile() {
        final DbFile dbFile = DocumentUtil.openDocumentDialog();
        if (dbFile == null) {
            return;
        }
        openFile(dbFile);
    }

    @SuppressWarnings("deprecation")
    public void openFile(final DbFile dbFile) {
        try {
            if (!dbFile.getFile().exists()) {
                String txt = I18N.getMsg("msg.dlg.project.not.exits.text", dbFile.getFile().getPath());
                JOptionPane.showMessageDialog(null, txt, I18N.getMsg("msg.dlg.project.not.exits.title"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            PersistenceManager oldPersMngr = PersistenceManager.getInstance();
            oldPersMngr.open(dbFile);
            try {
                oldPersMngr.checkAndAlterModel();
            } catch (Exception e) {
                oldPersMngr.closeConnection();
                System.err.println("StorybookApp.openFile(): DB update failed");
                e.printStackTrace();
                ExceptionDialog dlg = new ExceptionDialog(e);
                SwingUtil.showModalDialog(dlg, null);
                return;
            }
            oldPersMngr.closeConnection();
            setWaitCursor();
            String text = I18N.getMsg("msg.common.loading", dbFile.getDbName());
            final HourglassSplash dlg = new HourglassSplash(text);
            Timer timer = new Timer(10, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    MainFrame newMainFrame = new MainFrame();
                    newMainFrame.init(dbFile.getDbName());
                    newMainFrame.initUi();
                    addMainFrame(newMainFrame);
                    closeBlank();
                    dlg.dispose();
                    updateFilePref(dbFile);
                    reloadMenuBars();
                    setDefaultCursor();
                }
            });
            timer.setRepeats(false);
            timer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateFilePref(DbFile dbFile) {
        File file = dbFile.getFile();
        PrefUtil.set(PreferenceKey.LAST_OPEN_DIR, file.getParent());
        PrefUtil.set(PreferenceKey.LAST_OPEN_FILE, file.getPath());
        List<DbFile> list = PrefUtil.getDbFileList();
        if (!list.contains(dbFile)) {
            list.add(dbFile);
        }
        PrefUtil.setDbFileList(list);
        reloadMenuBars();
    }

    public void clearRecentFiles() {
        PrefUtil.setDbFileList(new ArrayList<DbFile>());
        reloadMenuBars();
    }

    private void reloadMenuBars() {
        for (MainFrame mainFrame : mainFrames) {
            mainFrame.getSbActionManager().reloadMenuToolbar();
        }
    }

    public void setWaitCursor() {
        for (MainFrame mainFrame : mainFrames) {
            SwingUtil.setWaitingCursor(mainFrame);
        }
    }

    public void setDefaultCursor() {
        for (MainFrame mainFrame : mainFrames) {
            SwingUtil.setDefaultCursor(mainFrame);
        }
    }

    public void modelPropertyChange(PropertyChangeEvent evt) {
    }

    public static void main(String[] args) {
        if (!lockInstance("/tmp/storybook-lock")) {
            JOptionPane.showMessageDialog(null, "Another instance of Storybook is already running.\n" + "If you don't see a Storybook window," + "kill the process or reboot your system.", "Already running", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                StorybookApp app = StorybookApp.getInstance();
                app.init();
            }
        });
    }

    private static boolean lockInstance(final String lockFile) {
        try {
            final File file = new File(lockFile);
            final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            final FileLock fileLock = randomAccessFile.getChannel().tryLock();
            if (fileLock != null) {
                Runtime.getRuntime().addShutdownHook(new Thread() {

                    public void run() {
                        try {
                            fileLock.release();
                            randomAccessFile.close();
                            file.delete();
                        } catch (Exception e) {
                            System.err.println("Unable to remove lock file: " + lockFile);
                            e.printStackTrace();
                        }
                    }
                });
                return true;
            }
        } catch (Exception e) {
            System.err.println("Unable to create and/or lock file: " + lockFile);
            e.printStackTrace();
        }
        return false;
    }
}
