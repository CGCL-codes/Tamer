package de.fuhrmeister.browserchooser.controller;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.swing.UIManager;
import de.fuhrmeister.browserchooser.model.ModelFactory;
import de.fuhrmeister.lm4j.LogMonitor;
import de.fuhrmeister.util.ConfigManager;
import de.fuhrmeister.util.FileChooser;
import de.fuhrmeister.util.FileManager;
import de.fuhrmeister.util.MessageBox;
import de.fuhrmeister.util.OSValidator;
import de.fuhrmeister.util.Tools;

public class Controller {

    public static final int APP_WINDOW_MAIN = 1;

    public static final int SYSTEM_SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();

    public static final int SYSTEM_SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    private static Controller controller;

    private static boolean isStartingApp = true;

    public static synchronized Controller getInstance() {
        if (controller == null) {
            Controller.controller = new Controller();
            return Controller.controller;
        }
        return Controller.controller;
    }

    public Controller() {
        super();
    }

    public void load() {
        Tools.init();
        initLogger();
        initModel();
        setLockAndFeel();
        loadViews();
        Controller.isStartingApp = false;
    }

    private void initLogger() {
        final LogMonitor logHandler = LogMonitor.getInstance();
        logHandler.setMonitor(LogMonitor.FILE);
        Tools.logManager.setLevel(Level.ALL);
        Tools.logManager.addHandler(logHandler);
    }

    private void initModel() {
        if (ModelFactory.modelFileExists()) {
            try {
                ModelFactory.load();
            } catch (final Exception e) {
                Tools.messageBox.inform(null, Tools.configMng.getStringFor(ConfigManager.MESSAGE_TITLE_INFORMATION), Tools.configMng.getStringFor(ConfigManager.MESSAGE_VALUE_ERROR_MODELLOADING));
                Tools.logManager.finest(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void setLockAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final Exception e) {
            Tools.logManager.finest(e.getMessage());
        }
    }

    private void loadViews() {
        DockletController.getInstance().show();
    }

    public void update() {
        ModelFactory.save();
        this.initModel();
        this.updateGUI();
    }

    public void updateGUI() {
        Tools.configMng.updateLanguage();
        DockletController.getInstance().updateGUI();
    }

    public void exit() {
        Tools.configMng.savePreferences();
        DockletController.getInstance().exit();
        ModelFactory.save();
        System.gc();
        System.exit(0);
    }

    public void updateLanguage(final String text) {
        if (Tools.configMng.languageExists(text)) {
            Tools.configMng.setPreference(ConfigManager.LANGUAGE, text);
            Tools.configMng.updateLanguage();
            DockletPopupMenuController.getInstance().updateLanguage();
        } else {
            Tools.messageBox.error(DockletController.getInstance().getWindow(), Tools.configMng.getStringFor(ConfigManager.MESSAGE_TITLE_ERROR), Tools.configMng.getStringFor(ConfigManager.MESSAGE_VALUE_ERROR_FILENOTEXISTS_LANGUAGE));
        }
    }

    public void loadLanguage() {
        final FileChooser chooser = new FileChooser(false);
        chooser.setLocale(Tools.configMng.getLocale());
        chooser.setFileFilter(Tools.configMng.getConfiguration(ConfigManager.STRINGS_FILEEXT), Tools.configMng.getStringFor(ConfigManager.STRING_FILEEXT_DESC));
        if (chooser.showOpenDialog(DockletController.getInstance().getWindow())) {
            if (copyLanguageFile(chooser.getDirectory(), chooser.getFileName())) {
                updateLanguage(chooser.getFileName().substring(0, chooser.getFileName().indexOf(".")));
            }
        }
    }

    private boolean copyLanguageFile(final String dir, final String file) {
        try {
            Tools.fileManager.copy(dir, file, false, Tools.configMng.getConfiguration(ConfigManager.STRINGS_FOLDER), true);
        } catch (final IOException e) {
            Tools.messageBox.error(DockletController.getInstance().getWindow(), Tools.configMng.getStringFor(ConfigManager.MESSAGE_TITLE_ERROR), Tools.configMng.getStringFor(ConfigManager.MESSAGE_VALUE_ERROR_COPY_LANGUAGE));
            return false;
        }
        return true;
    }

    public void focus(final int object) {
        switch(object) {
            case Controller.APP_WINDOW_MAIN:
                {
                    DockletController.getInstance().focus();
                }
        }
    }

    public Component getWindow(final int object) {
        switch(object) {
            case Controller.APP_WINDOW_MAIN:
                {
                    return DockletController.getInstance().getWindow();
                }
            default:
                {
                    return DockletController.getInstance().getWindow();
                }
        }
    }

    public static String[] getExecutables() {
        if (OSValidator.isWindows()) {
            return new String[] { "exe", "bat", "jar", "lnk" };
        } else if (OSValidator.isMac()) {
            return new String[] { "app", "dmg", "jar" };
        } else if (OSValidator.isUnix()) {
            return new String[] { "bin", "sh", "jar" };
        } else {
            return new String[] { "jar" };
        }
    }

    public static String getExecutablesDesc() {
        if (OSValidator.isWindows()) {
            return "exe, bat, jar, lnk";
        } else if (OSValidator.isMac()) {
            return "app, dmg, jar";
        } else if (OSValidator.isUnix()) {
            return "bin, sh, jar";
        } else {
            return "jar";
        }
    }

    public boolean isStartingApp() {
        return Controller.isStartingApp;
    }

    public void dropBrowser(final DropTargetDropEvent dtde) {
        final Iterator<String> i = this.getDroppedFiles(dtde).iterator();
        while (i.hasNext()) {
            final String filePath = i.next();
            final File file = new File(filePath);
            if (!file.isFile()) {
                Tools.messageBox.alert(DockletController.getInstance().getWindow(), Tools.configMng.getStringFor(ConfigManager.MESSAGE_TITLE_INFORMATION), Tools.configMng.getStringFor(ConfigManager.MESSAGE_VALUE_ERROR_NODRAGABLEFILE), MessageBox.INFORMATION_MESSAGE);
                return;
            }
            SettingsController.getInstance().show(file.getName(), file.getAbsolutePath());
        }
    }

    public void dropVersion(final DropTargetDropEvent dtde, final int id) {
        final Iterator<String> i = this.getDroppedFiles(dtde).iterator();
        while (i.hasNext()) {
            final String filePath = i.next();
            final File file = new File(filePath);
            if (!file.isFile()) {
                Tools.messageBox.alert(DockletController.getInstance().getWindow(), Tools.configMng.getStringFor(ConfigManager.MESSAGE_TITLE_INFORMATION), Tools.configMng.getStringFor(ConfigManager.MESSAGE_VALUE_ERROR_NODRAGABLEFILE), MessageBox.INFORMATION_MESSAGE);
                return;
            }
            SettingsController.getInstance().show(id, file.getAbsolutePath());
        }
    }

    /**
	 * Get called when the Drop was successfull. Extracts the file path and calls
	 * <code>SettingsController.getInstance().show()</code>.
	 * 
	 * @param files
	 */
    private static final void dropFiles(final List<String> files) {
        final Iterator<String> i = files.iterator();
        while (i.hasNext()) {
            final String filePath = i.next();
            final File file = new File(filePath);
            if (!file.isFile()) {
                Tools.messageBox.alert(DockletController.getInstance().getWindow(), Tools.configMng.getStringFor(ConfigManager.MESSAGE_TITLE_INFORMATION), Tools.configMng.getStringFor(ConfigManager.MESSAGE_VALUE_ERROR_NODRAGABLEFILE), MessageBox.INFORMATION_MESSAGE);
                return;
            }
            SettingsController.getInstance().show(file.getName(), file.getAbsolutePath());
        }
    }

    private List<String> getDroppedFiles(DropTargetDropEvent dtde) {
        dtde.getLocation();
        boolean success = false;
        if ((dtde.getSourceActions() & DnDConstants.ACTION_COPY) == 0) {
            dtde.rejectDrop();
        } else {
            dtde.acceptDrop(DnDConstants.ACTION_LINK);
            final Transferable trans = dtde.getTransferable();
            final DataFlavor[] currentFlavors = dtde.getCurrentDataFlavors();
            DataFlavor selectedFlavor = null;
            for (int i = 0; i < currentFlavors.length; i++) {
                if (DataFlavor.javaFileListFlavor.equals(currentFlavors[i])) {
                    selectedFlavor = currentFlavors[i];
                    break;
                }
            }
            final List<String> vFiles = new ArrayList<String>();
            if (selectedFlavor != null) {
                try {
                    final List<File> list = (List<File>) trans.getTransferData(selectedFlavor);
                    final Iterator<File> files = list.iterator();
                    while (files.hasNext()) {
                        File file = files.next();
                        try {
                            if (OSValidator.isWindows()) {
                                final String fileSource = FileManager.getInstance().parseLink(file);
                                if (!fileSource.isEmpty()) file = new File(fileSource);
                            }
                        } catch (NullPointerException npe) {
                        }
                        vFiles.add(file.getPath());
                        success = true;
                    }
                } catch (final ClassCastException e) {
                } catch (final Exception ex) {
                }
            }
            dtde.getDropTargetContext().dropComplete(success);
            if (vFiles.size() > 0) {
                return vFiles;
            }
        }
        return null;
    }
}
