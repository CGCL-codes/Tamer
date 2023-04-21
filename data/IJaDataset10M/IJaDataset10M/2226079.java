package org.modss.facilitator.port.control;

import org.modss.facilitator.model.util.ConversionException;
import org.modss.facilitator.model.v1.*;
import org.modss.facilitator.model.v1.xml.*;
import org.modss.facilitator.model.v1.convert.*;
import org.modss.facilitator.shared.resource.ResourceProvider;
import org.modss.facilitator.shared.singleton.Singleton;
import org.modss.facilitator.shared.pref.UserPreferences;
import org.modss.facilitator.shared.help.HelpManager;
import org.modss.facilitator.shared.browser.BrowserManager;
import org.modss.facilitator.shared.button.ButtonProvider;
import org.modss.facilitator.shared.window.*;
import org.modss.facilitator.shared.soup.SoupUtil;
import org.modss.facilitator.port.view.AppGUI;
import org.modss.facilitator.port.view.support.*;
import org.modss.facilitator.port.command.*;
import org.modss.facilitator.port.general.*;
import org.modss.facilitator.port.control.command.*;
import org.modss.facilitator.port.control.support.*;
import org.modss.facilitator.port.ui.option.*;
import org.modss.facilitator.port.ui.option.comp.OptionFactory;
import org.modss.facilitator.util.collection.list.*;
import org.modss.facilitator.util.collection.matrix.*;
import org.modss.facilitator.util.description.*;
import org.modss.facilitator.util.ui.SmarterWindowAdapter;
import org.modss.facilitator.util.ui.SmarterFrame;
import org.modss.facilitator.util.ui.SmarterDialog;
import org.modss.facilitator.util.xml.XmlException;
import org.modss.facilitator.util.xml.DomUtil;
import org.swzoo.nursery.browser.*;
import org.swzoo.nursery.queue.*;
import org.swzoo.log2.core.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.lang.InterruptedException;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.xml.parsers.ParserConfigurationException;

/**
 * The primary responsibility of this object is
 * to handle user <b>File</b> requests.
 * <p>
 * In doing so it needs to maintain a list of
 * AnalysisControl objects (just one for the prototype).
 * <p>
 */
public class AppController implements IFileNewCommand, IFileOpenCommand, IFileCloseCommand, IFileSaveCommand, IFileSaveAsCommand, IFileMatrixImportCommand, IFileMatrixExportCommand, IFileReportCommand, IFileExitCommand, IWindowOptionsCommand, IHelpDSSBrowserCommand, IHelpOnlineResources, IHelpOnlineUpdates, IHelpAboutCommand {

    /** Queue for performing operations. */
    private RunQueue runQ = RunQueueFactory.createQueue();

    /**
     * Variable to support Singleton pattern.
     */
    private boolean built = false;

    /**
     * Application main GUI screen.
     */
    private AppGUI _gui = null;

    /**
     * In the prototype we only have one Analysis, so
     * we only need one AnalysisController
     */
    private AnalysisController _controller = null;

    /**
     * In the prototype we only have one Analysis.
     */
    private Analysis _analysis = null;

    /**
     * Create an adapter so we can map Analysis specific
     * commands to a particular analysis (at the moment only
     * one).
     */
    private AnalysisCommandsAdapter _adapter = null;

    /**
     * Current Save file name.
     */
    File _saveFile = null;

    /**
     * Current Save directory.
     */
    File _saveDir = null;

    /**
     * Current Import directory.
     */
    private File _importDir;

    /**
     * Current Export directory.
     */
    private File _exportDir;

    /**
     * Options dialog;
     */
    JDialog optionsDialog;

    /** Options component. */
    MainOptionComponent optionsComp;

    /**
     * Options content node.
     */
    OptionNode options;

    /**
     * Overwrite option pane.
     */
    JOptionPane overwriteOptionPane = null;

    /**
     * The title.
     */
    String title;

    JDialog _helpAboutGUI = null;

    Window _helpDSSGUI = null;

    /**
     * Constructor.
     */
    public AppController() {
        configure();
        build();
        setTitleFilename(null);
    }

    /**
     * Perform configuration.
     */
    protected void configure() {
        _saveFile = null;
        _saveDir = new File(resources.getProperty("dss.save.loc"));
        _saveDir = checkForSavedLocation("dss.file.save.dir.location", _saveDir);
        _importDir = _saveDir;
        _exportDir = _saveDir;
        _importDir = checkForSavedLocation("dss.file.import.dir.location", _importDir);
        _exportDir = checkForSavedLocation("dss.file.export.dir.location", _exportDir);
        LogTools.trace(logger, 25, "AppController.configure() - _saveDir=" + _saveDir);
        LogTools.trace(logger, 25, "AppController.configure() - _importDir=" + _importDir);
        LogTools.trace(logger, 25, "AppController.configure() - _exportDir=" + _exportDir);
    }

    /**
     * Check whether a property exists which represents a previously
     * saved directory location.
     *
     * @param propName the property name for the directory.
     * @param fallback a fallback directory reference.
     * @return the directory specified by the property if the property
     * exists and the directory exists.
     */
    File checkForSavedLocation(String propName, File fallback) {
        LogTools.trace(logger, 25, "AppController.checkForSavedLocation() - propName=" + propName + ",fallback=" + fallback);
        String dirName = resources.getProperty(propName);
        if (dirName == null) return fallback;
        File dir = new File(dirName);
        if (!dir.exists()) return fallback;
        if (!dir.isDirectory()) return fallback;
        return dir;
    }

    protected void build() {
        _gui = new AppGUI();
        try {
            windowMgr.add(_gui, "MAIN APPLICATION WINDOW", null, true);
        } catch (WindowManagerException wmex) {
            LogTools.warn(logger, "AppController.build() - Failed to add MAIN APPLICATION WINDOW to window manager.  Reason: " + wmex.getMessage());
        }
        _gui.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        _gui.addWindowListener(new SmarterWindowAdapter(_gui) {

            public void windowClosing(WindowEvent ev) {
                LogTools.trace(logger, 25, "AppController.build().windowClosingAdapter() - Window closing.");
                fileExit();
                LogTools.trace(logger, 25, "AppController.build().windowClosingAdapter() - Window closing ... cancelled by user!");
            }
        });
        _adapter = new AnalysisCommandsAdapter();
        Command fileNewCommand = new FileNewCommand(this);
        Command fileOpenCommand = new FileOpenCommand(this);
        Command fileCloseCommand = new FileCloseCommand(this);
        Command fileSaveCommand = new FileSaveCommand(this);
        Command fileSaveAsCommand = new FileSaveAsCommand(this);
        Command fileMatrixImportCommand = new FileMatrixImportCommand(this);
        Command fileMatrixExportCommand = new FileMatrixExportCommand(this);
        Command fileReportCommand = new FileReportCommand(this);
        Command fileExitCommand = new FileExitCommand(this);
        Command windowIssueCommand = new WindowIssueCommand(_adapter);
        Command windowStakeholdersCommand = new WindowStakeholdersCommand(_adapter);
        Command windowCriteriaCommand = new WindowCriteriaCommand(_adapter);
        Command windowAlternativesCommand = new WindowAlternativesCommand(_adapter);
        Command windowRankingCommand = new WindowRankingCommand(_adapter);
        Command windowOptionsCommand = new WindowOptionsCommand(this);
        Command viewResultsCommand = new ViewResultsCommand(_adapter);
        Command runAnalyseCommand = new RunAnalyseCommand(_adapter);
        Command runSetCycleCommand = new RunSetCycleCommand(_adapter);
        Command helpDSSBrowserCommand = new HelpDSSBrowserCommand(this);
        Command helpOnlineResourcesCommand = new HelpOnlineResourcesCommand(this);
        Command helpOnlineUpdatesCommand = new HelpOnlineUpdatesCommand(this);
        Command helpAboutCommand = new HelpAboutCommand(this);
        _gui.setCommandFileNew(fileNewCommand);
        _gui.setCommandFileOpen(fileOpenCommand);
        _gui.setCommandFileClose(fileCloseCommand);
        _gui.setCommandFileSave(fileSaveCommand);
        _gui.setCommandFileSaveAs(fileSaveAsCommand);
        _gui.setCommandFileMatrixImport(fileMatrixImportCommand);
        _gui.setCommandFileMatrixExport(fileMatrixExportCommand);
        _gui.setCommandFileReport(fileReportCommand);
        _gui.setCommandFileExit(fileExitCommand);
        _gui.setCommandWindowIssue(windowIssueCommand);
        _gui.setCommandWindowStakeholders(windowStakeholdersCommand);
        _gui.setCommandWindowCriteria(windowCriteriaCommand);
        _gui.setCommandWindowAlternatives(windowAlternativesCommand);
        _gui.setCommandWindowRanking(windowRankingCommand);
        _gui.setCommandWindowOptions(windowOptionsCommand);
        _gui.setCommandViewResults(viewResultsCommand);
        _gui.setCommandRunAnalyse(runAnalyseCommand);
        _gui.setCommandRunSetCycle(runSetCycleCommand);
        _gui.setCommandHelpDSSBrowser(helpDSSBrowserCommand);
        _gui.setCommandHelpOnlineResources(helpOnlineResourcesCommand);
        _gui.setCommandHelpOnlineUpdates(helpOnlineUpdatesCommand);
        _gui.setCommandHelpAbout(helpAboutCommand);
        setMenus(false);
        _gui.getWindowOptionsEnabler().setEnabled(true);
        _gui.getHelpDSSBrowserEnabler().setEnabled(true);
        _gui.getHelpOnlineResourcesEnabler().setEnabled(true);
        _gui.getHelpOnlineUpdatesEnabler().setEnabled(true);
        _gui.getHelpAboutEnabler().setEnabled(true);
        initOptionsDialog();
        LogTools.trace(logger, 25, "AppController.build() - Start build of option pane.");
        overwriteOptionPane = new JOptionPane();
        overwriteOptionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
        overwriteOptionPane.setOptionType(JOptionPane.YES_NO_OPTION);
    }

    /**
     * Options dialog.
     */
    void initOptionsDialog() {
        LogTools.info(logger, "AppController.initOptionsDialog() - START");
        options = OptionFactory.createOption(getFrame(), OptionFactory.APP, true);
        optionsComp = new MainOptionComponent();
        optionsComp.setModel(options);
    }

    /**
     * Enable/Disable appropriate menus
     * @param open indicates whether an analysis currently open or not.
     */
    protected void setMenus(boolean open) {
        if (open) {
            LogTools.trace(logger, 25, "AppController.setMenus(OPEN)");
            _gui.getFileNewEnabler().setEnabled(false);
            _gui.getFileOpenEnabler().setEnabled(false);
            _gui.getFileCloseEnabler().setEnabled(true);
            _gui.getFileSaveEnabler().setEnabled(true);
            _gui.getFileSaveAsEnabler().setEnabled(true);
            _gui.getFileReportEnabler().setEnabled(true);
            _gui.getFileMatrixImportEnabler().setEnabled(true);
            _gui.getFileMatrixExportEnabler().setEnabled(true);
        } else {
            LogTools.trace(logger, 25, "AppController.setMenus(CLOSED)");
            _gui.getFileNewEnabler().setEnabled(true);
            _gui.getFileOpenEnabler().setEnabled(true);
            _gui.getFileCloseEnabler().setEnabled(false);
            _gui.getFileSaveEnabler().setEnabled(false);
            _gui.getFileSaveAsEnabler().setEnabled(false);
            _gui.getFileReportEnabler().setEnabled(false);
            _gui.getFileMatrixImportEnabler().setEnabled(false);
            _gui.getFileMatrixExportEnabler().setEnabled(false);
            _gui.getFileExitEnabler().setEnabled(true);
        }
    }

    /**
     * Return frame for GUI part of application.
     */
    public Frame getFrame() {
        return (Frame) _gui;
    }

    /**
     * New file.
     */
    public void fileNew() {
        LogTools.trace(logger, 25, "AppController.fileNew()");
        if (_analysis != null) {
            LogTools.warn(logger, "AppController.fileNew() - Analysis should be null but isn't.");
            return;
        }
        _analysis = createAnalysis();
        LogTools.warn(logger, "Re-employ dirty tracking...");
        initAnalysis();
        _saveFile = null;
    }

    /**
     * Open a file.  This method provides a user dialog for the file open.
     */
    public void fileOpen() {
        LogTools.trace(logger, 25, "AppController.fileOpen()");
        LogTools.trace(logger, 25, "AppController.fileOpen() - _saveFile=" + _saveFile);
        LogTools.trace(logger, 25, "AppController.fileOpen() - _saveDir=" + _saveDir);
        final JFileChooser chooser = FileChooserFactory.getInstance(FileChooserFactory.OPEN_SAVE);
        chooser.setDialogTitle(resources.getProperty("dss.gui.main.file.open.dialog.title", "* OPEN ANALYSIS *"));
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setApproveButtonText(resources.getProperty("dss.gui.main.file.open.dialog.approve.button", "OPEN"));
        chooser.setCurrentDirectory(_saveDir);
        chooser.rescanCurrentDirectory();
        int choice = chooser.showDialog(getFrame(), null);
        File chooserFile = chooser.getSelectedFile();
        File chooserDir = chooser.getCurrentDirectory();
        LogTools.trace(logger, 25, "AppController.fileOpen() - chooserFile=" + chooserFile);
        LogTools.trace(logger, 25, "AppController.fileOpen() - chooserDir=" + chooserDir);
        switch(choice) {
            case JFileChooser.APPROVE_OPTION:
                LogTools.trace(logger, 25, "AppController.fileOpen() - APPROVED.");
                break;
            case JFileChooser.CANCEL_OPTION:
                LogTools.trace(logger, 25, "AppController.fileOpen() - CANCELLED.");
                return;
            default:
                LogTools.warn(logger, "AppController.fileOpen() - Unexpected choice from JFileChooser.showDialog(); " + choice);
                return;
        }
        if (chooserFile == null) {
            LogTools.warn(logger, "AppController.fileOpen() - Selected file is null.");
            return;
        }
        chooserFile = FileChooserUtil.applyFilter(chooserFile, chooser.getFileFilter());
        if (chooserFile.isDirectory()) {
            LogTools.trace(logger, 25, "AppController.fileOpen() - choser.getSelectedFile() is a directory, not a file - BAIL.");
            return;
        }
        doOpen(new File(chooserDir, chooserFile.getName()));
    }

    /**
     * Open the specified file.
     *
     * @param file the file to open.
     */
    void doOpen(final File file) {
        LogTools.trace(logger, 25, "AppController.doOpen() - Opening file=" + file);
        if (_saveFile != null) {
            LogTools.warn(logger, "AppController.doOpen() - Refusing to open a file on the grounds that one may already be open!");
            return;
        }
        reconstitute(file);
    }

    /**
     * Initialise an analysis based on a file.
     *
     * @param file the file to open.
     */
    void reconstitute(File file) {
        LogTools.info(logger, "AppController.reconstitute() - File=" + file.getAbsolutePath());
        Analysis analysis = null;
        try {
            if (!file.exists()) throw new IOException(SoupUtil.populateWithArgs(resources.getProperty("dss.file.error.does.not.exist", "FILE DOES NOT EXIST {0}"), new String[] { file.getAbsolutePath() }));
            if (file.isDirectory()) throw new IOException(SoupUtil.populateWithArgs(resources.getProperty("dss.file.error.is.directory", "FILE {0} IS A DIRECTORY; NOT A FILE"), new String[] { file.getAbsolutePath() }));
            if (!file.canRead()) throw new IOException(SoupUtil.populateWithArgs(resources.getProperty("dss.file.error.cannot.read", "CANNOT READ FILE {0}"), new String[] { file.getAbsolutePath() }));
        } catch (IOException ex) {
            LogTools.info(logger, "AppController.reconstitute() - IOException.  Reason: " + ex.getMessage());
            String title = resources.getProperty("dss.gui.main.file.open.dialog.error.title", "* FILE OPEN ERROR *");
            String heading = resources.getProperty("dss.gui.main.file.open.dialog.error.io", "* FILE ACCESS ERROR *");
            String message = heading + "\n\n" + ex.getMessage();
            SoupUtil.showMessageDialog(getFrame(), message, title, JOptionPane.ERROR_MESSAGE);
            return;
        }
        LogTools.info(logger, "AppController.reconstitute() - Assuming we have a current (1.3) model...");
        try {
            InputStream in = new FileInputStream(file);
            analysis = ModelDOMFactory.createAnalysis(in);
        } catch (SAXException ex) {
            LogTools.info(logger, "SAX exception encountered processing file (file=" + file.getAbsolutePath() + ")", ex);
        } catch (XmlException ex) {
            LogTools.info(logger, "XML exception encountered processing file (file=" + file.getAbsolutePath() + ")", ex);
        } catch (ParserConfigurationException ex) {
            LogTools.info(logger, "XML exception encountered processing file (file=" + file.getAbsolutePath() + ")", ex);
        } catch (IOException ex) {
            LogTools.info(logger, "AppController.reconstitute() - IOException.  Reason: " + ex.getMessage());
            String title = resources.getProperty("dss.gui.main.file.open.dialog.error.title", "* FILE OPEN ERROR *");
            String heading = resources.getProperty("dss.gui.main.file.open.dialog.error.io", "* FILE ACCESS ERROR *");
            String message = heading + "\n\n" + ex.getMessage();
            SoupUtil.showMessageDialog(getFrame(), message, title, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (analysis == null) {
            LogTools.info(logger, "AppController.reconstitute() - Looks like we don't have a 1.3 model - let's try the old 1.2 serialised version with a yummy conversion :-)");
            try {
                InputStream in = new FileInputStream(file);
                analysis = ConverterFactory.getModelConverter().convert(ConverterFactory.buildAnalysis(in));
            } catch (ClassNotFoundException ex) {
                LogTools.info(logger, "AppController.reconstitute() - ClassNotFoundException.  Reason: " + ex.getMessage());
                String title = resources.getProperty("dss.gui.main.file.open.dialog.error.title", "* FILE OPEN ERROR *");
                String heading = resources.getProperty("dss.gui.main.file.open.dialog.error.classnotfound", "* CLASS COULD NOT BE FOUND TO RECONSTRUCT DSS FILE *");
                String message = heading + "\n\n" + ex.getMessage();
                SoupUtil.showMessageDialog(getFrame(), message, title, JOptionPane.ERROR_MESSAGE);
                return;
            } catch (IOException ex) {
                LogTools.info(logger, "AppController.reconstitute() - IOException.  Reason: " + ex.getMessage());
                String title = resources.getProperty("dss.gui.main.file.open.dialog.error.title", "* FILE OPEN ERROR *");
                String heading = resources.getProperty("dss.gui.main.file.open.dialog.error.io", "* FILE ACCESS ERROR *");
                String message = heading + "\n\n" + ex.getMessage();
                SoupUtil.showMessageDialog(getFrame(), message, title, JOptionPane.ERROR_MESSAGE);
                return;
            } catch (ConversionException ex) {
                LogTools.info(logger, "AppController.reconstitute() - IOException.  Reason: " + ex.getMessage());
                String title = resources.getProperty("dss.gui.main.file.open.dialog.error.title", "* FILE OPEN ERROR *");
                String heading = resources.getProperty("dss.gui.main.file.open.dialog.error.io", "* FILE ACCESS ERROR *");
                String message = heading + "\n\n" + ex.getMessage();
                SoupUtil.showMessageDialog(getFrame(), message, title, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        _analysis = analysis;
        LogTools.trace(logger, 25, "AppController.reconstitute() - Selected <" + file + "> - SUCCESSFULLY RECONSTITUTED.");
        initAnalysis();
        _saveFile = file;
        _saveDir = new File(file.getParent());
        LogTools.trace(logger, 25, "AppController.reconstitute() - _saveFile=" + _saveFile);
        LogTools.trace(logger, 25, "AppController.reconstitute() - _saveDir=" + _saveDir);
        setTitleFilename(file.getName());
        up.setProperty("dss.file.save.dir.location", _saveDir.getAbsolutePath(), getFrame());
    }

    /**
     * Close file.
     */
    public void fileClose() {
        LogTools.trace(logger, 25, "AppController.fileClose()");
        if (!okToAbandon()) {
            return;
        }
        if (_analysis == null) {
            LogTools.warn(logger, "AppController.fileClose() - Analysis should not be null but is.");
            return;
        }
        if (_controller == null) {
            LogTools.warn(logger, "AppController.fileClose() - Controller should not be null but is.");
            return;
        }
        _saveFile = null;
        closeAnalysis();
    }

    /**
     * Save current state
     */
    public void fileSave() {
        doFileSave();
    }

    private boolean doFileSave() {
        LogTools.trace(logger, 25, "AppController.fileSave()");
        if (_saveFile == null) {
            LogTools.trace(logger, 25, "AppController.fileSave() - _saveFile is null, using SaveAs");
            return doFileSaveAs();
        }
        try {
            LogTools.trace(logger, 25, "AppController.doFileSave(" + _saveFile.getAbsolutePath() + ")");
            File file = SoupUtil.journalFile(_saveFile);
            LogTools.trace(logger, 25, "AppController.doFileSave() - File after journalling=" + file.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(file);
            Document dom = ModelDOMFactory.createDOM(_analysis);
            DomUtil.writeDOM(dom, fos);
            fos.close();
        } catch (IOException ex) {
            InfoDialog info = new InfoDialog(_gui, "FILE SAVE ERROR", true, "FILE SAVE ERROR\n\n" + ex.getMessage());
            info.pack();
            info.setVisible(true);
            _saveFile = null;
            return false;
        } catch (ParserConfigurationException ex) {
            InfoDialog info = new InfoDialog(_gui, "FILE SAVE ERROR", true, "FILE SAVE ERROR\n\n" + ex.getMessage());
            info.pack();
            info.setVisible(true);
            _saveFile = null;
            return false;
        }
        LogTools.trace(logger, 25, "AppController.fileSave() - " + _saveFile + " SAVED SUCCESSFULLY.");
        LogTools.trace(logger, 25, "AppController.fileSave() - " + _saveFile + " SAVED SUCCESSFULLY.");
        LogTools.warn(logger, "Dirty logic disable for 1.3 conversion.");
        up.setProperty("dss.file.save.dir.location", _saveDir.getAbsolutePath(), getFrame());
        return true;
    }

    /**
     * Save current state as filename;
     */
    public void fileSaveAs() {
        doFileSaveAs();
    }

    private boolean doFileSaveAs() {
        LogTools.trace(logger, 25, "AppController.fileSaveAs()");
        JFileChooser chooser = FileChooserFactory.getInstance(FileChooserFactory.OPEN_SAVE);
        chooser.setDialogTitle(resources.getProperty("dss.gui.main.menu.file.save.title", "SAVE ANALYSIS"));
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setApproveButtonText(resources.getProperty("dss.gui.main.menu.file.save", "SAVE"));
        chooser.setCurrentDirectory(_saveDir);
        chooser.setSelectedFile(_saveFile);
        chooser.rescanCurrentDirectory();
        int choice;
        Frame f = getFrame();
        choice = chooser.showDialog(f, null);
        switch(choice) {
            case JFileChooser.APPROVE_OPTION:
                LogTools.trace(logger, 25, "AppController.fileSaveAs() - APPROVED.");
                break;
            case JFileChooser.CANCEL_OPTION:
                LogTools.trace(logger, 25, "AppController.fileSaveAs() - CANCELLED.");
                return false;
            default:
                LogTools.warn(logger, "AppController.fileSaveAs() - Unexpected choice from JFileChooser.showDialog(); " + choice);
                return false;
        }
        File chooserFile = chooser.getSelectedFile();
        File chooserDir = chooser.getCurrentDirectory();
        LogTools.trace(logger, 25, "AppController.fileSaveAs() - chooserFile=" + chooserFile);
        LogTools.trace(logger, 25, "AppController.fileSaveAs() - chooserDir=" + chooserDir);
        if (chooserFile == null) {
            LogTools.warn(logger, "AppController.fileSaveAs() - Null file specified.");
            return false;
        }
        chooserFile = FileChooserUtil.applyFilter(chooserFile, chooser.getFileFilter());
        if (chooserFile.isDirectory()) {
            LogTools.trace(logger, 25, "AppController.fileSaveAs() - chooser.getSelectedFile() is a directory, not a file - BAIL.");
            return false;
        }
        if (!okToOverwrite(chooserFile)) {
            LogTools.trace(logger, 25, "AppController.fileSaveAs() - User has chosen not to overwrite existing file.");
            return false;
        }
        LogTools.trace(logger, 25, "AppController.fileSaveAs() - Prior to save, _saveFile=" + _saveFile);
        LogTools.trace(logger, 25, "AppController.fileSaveAs() - Prior to save, _saveDir=" + _saveDir);
        File tmpSaveFile = _saveFile;
        File tmpSaveDir = _saveDir;
        _saveFile = chooserFile;
        _saveDir = chooserDir;
        boolean retVal;
        if (doFileSave()) {
            LogTools.trace(logger, 25, "AppController.fileSaveAs() - Save successful.");
            setTitleFilename(_saveFile.getName());
            retVal = true;
        } else {
            LogTools.trace(logger, 25, "AppController.fileSaveAs() - Save failed.");
            _saveFile = tmpSaveFile;
            _saveDir = tmpSaveDir;
            retVal = false;
        }
        LogTools.trace(logger, 25, "AppController.fileSaveAs() - After save, _saveFile=" + _saveFile);
        LogTools.trace(logger, 25, "AppController.fileSaveAs() - After save, _saveDir=" + _saveDir);
        return retVal;
    }

    /**
     * Produce a Report.
     */
    public void fileReport() {
        LogTools.trace(logger, 25, "AppController.fileReport()");
        _controller.fileReport();
    }

    /**
     * Matrix Import current analysis.
     */
    public void fileMatrixImport() {
        LogTools.trace(logger, 25, "AppController.fileMatrixImport() - START");
        LogTools.trace(logger, 25, "AppController.fileMatrixImport() - _importDir=" + _importDir);
        File file = _controller.fileMatrixImport(_importDir);
        if (file != null) {
            _importDir = new File(file.getParent());
            LogTools.trace(logger, 25, "AppController.fileMatrixImport() - New _importDir=" + _importDir);
            up.setProperty("dss.file.import.dir.location", _importDir.getAbsolutePath(), getFrame());
        }
        LogTools.trace(logger, 25, "AppController.fileMatrixImport() - END");
    }

    /**
     * Matrix Export current analysis.
     */
    public void fileMatrixExport() {
        LogTools.trace(logger, 25, "AppController.fileMatrixExport() - START");
        LogTools.trace(logger, 25, "AppController.fileMatrixExport() - _exportDir=" + _exportDir);
        File file = _controller.fileMatrixExport(_exportDir);
        if (file != null) {
            _exportDir = new File(file.getParent());
            LogTools.trace(logger, 25, "AppController.fileMatrixExport() - New _exportDir=" + _exportDir);
            up.setProperty("dss.file.export.dir.location", _exportDir.getAbsolutePath(), getFrame());
        }
        LogTools.trace(logger, 25, "AppController.fileMatrixExport() - END");
    }

    /**
     * Exit application
     */
    public void fileExit() {
        LogTools.trace(logger, 25, "AppController.fileExit()");
        if (!okToAbandon()) {
            return;
        }
        System.out.println("Bye :)");
        System.exit(1);
    }

    /**
     * External browser help.
     */
    public void helpDSSBrowser() {
        LogTools.trace(logger, 25, "AppController.helpDSSBrowser()");
        String offset = resources.getProperty("dss.userguide");
        helpMgr.showHelp(offset);
    }

    /**
     * Help online resources.
     */
    public void helpOnlineResources() {
        LogTools.trace(logger, 25, "AppController.helpOnlineResources()");
        String urlText = resources.getProperty("dss.url.resources");
        try {
            browserMgr.showURL(new URL(urlText));
        } catch (MalformedURLException mex) {
            LogTools.warn(logger, "AppController.helpOnlineResources() - Malformed URL " + urlText + ".  Reason: " + mex);
        }
    }

    /**
     * Help online updates.
     */
    public void helpOnlineUpdates() {
        LogTools.trace(logger, 25, "AppController.helpOnlineUpdates()");
        String urlText = resources.getProperty("dss.url.updates");
        try {
            browserMgr.showURL(new URL(urlText));
        } catch (MalformedURLException mex) {
            LogTools.warn(logger, "AppController.helpOnlineUpdates() - Malformed URL " + urlText + ".  Reason: " + mex);
        }
    }

    /**
     * About help.
     */
    public void helpAbout() {
        LogTools.trace(logger, 25, "AppController.helpAbout()");
        HelpAboutController controller = new HelpAboutController(_gui);
        _helpAboutGUI = controller.getDialog();
        launchWindow(_helpAboutGUI, _gui.getHelpAboutEnabler(), "HELP ABOUT WINDOW", getFrame());
    }

    /**
     * Fire up the options dialog.
     */
    public void windowOptions() {
        LogTools.trace(logger, 25, "AppController.windowOptions()");
        Properties p = resources.getAllProperties();
        LogTools.trace(logger, 25, "AppController.windowOptions() - properties " + p);
        options.init(p);
        optionsDialog = new SmarterDialog(getFrame(), resources.getProperty("dss.gui.main.menu.window.options", "PROPERTIES"), true);
        OkCancelPanel okcancel = new OkCancelPanel();
        Container content = optionsDialog.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(optionsComp.getUIComponent(), BorderLayout.CENTER);
        content.add(okcancel, BorderLayout.SOUTH);
        okcancel.getOkButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent aev) {
                optionsDialogOk();
            }
        });
        okcancel.getCancelButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent aev) {
                optionsDialogCancel();
            }
        });
        LogTools.info(logger, "AppController.windowOptions() - END");
        optionsDialog.pack();
        optionsDialog.setLocationRelativeTo(getFrame());
        optionsDialog.setVisible(true);
    }

    /**
     * Entry point for "OK" on options dialog.
     */
    void optionsDialogOk() {
        LogTools.trace(logger, 25, "AppController.optionsDialogOk()");
        Properties newProp = options.getProperties();
        LogTools.trace(logger, 25, "AppController.optionsDialogOk() - new properties " + newProp);
        up.setProperties(newProp, getFrame());
        optionsDialog.setVisible(false);
        Container content = optionsDialog.getContentPane();
        content.remove(optionsComp.getUIComponent());
    }

    /**
     * Entry point for "CANCEL" on options dialog.
     */
    void optionsDialogCancel() {
        LogTools.trace(logger, 25, "AppController.optionsDialogCancel() - CANCEL");
        optionsDialog.setVisible(false);
        Container content = optionsDialog.getContentPane();
        content.remove(optionsComp.getUIComponent());
    }

    private void closeAnalysis() {
        LogTools.trace(logger, 25, "AppController.closeAnalysis()");
        _controller.close();
        _controller = null;
        _analysis = null;
        setMenus(false);
        _gui.setActiveBackground(false);
        setTitleFilename(null);
    }

    private void initAnalysis() {
        LogTools.trace(logger, 25, "AppController.initAnalysis() - Creating new analysis controller.");
        _controller = new AnalysisController(_analysis, _gui, _gui);
        _adapter.setHandler(_controller);
        setMenus(true);
        _gui.setActiveComponent(_controller.getMatrixUI());
        _gui.setActiveBackground(true);
    }

    private boolean okToAbandon() {
        if (_analysis == null) {
            return true;
        }
        LogTools.warn(logger, "Reinstate dirty logic.");
        LogTools.trace(logger, 25, "AppController.okToAbandon() - Analysis is DIRTY.");
        int option = JOptionPane.showConfirmDialog(getFrame(), resources.getProperty("button.analysis.save.text", "SAVE CHANGES"), resources.getProperty("button.analysis.save.title", "AN�LISIS"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        switch(option) {
            case JOptionPane.YES_OPTION:
                return doFileSave();
            case JOptionPane.NO_OPTION:
                return true;
            default:
                return false;
        }
    }

    /**
     * Set the title based on a provided name (at the moment a filename).
     *
     * @param name the name to use in setting the title.
     */
    void setTitleFilename(String name) {
        String ext = "dss";
        if (name != null && name.regionMatches(true, name.length() - (ext.length() + 1), new String("." + ext), 0, (ext.length() + 1))) {
            LogTools.trace(logger, 25, "AppController.setTitleFilename() - Removing \"" + ext + "\" extension.");
            name = name.substring(0, name.length() - (ext.length() + 1));
        }
        title = resources.getProperty("dss.gui.main.title", "FACILITATOR") + ((name == null) ? "" : (": " + name));
        getFrame().setTitle(title);
    }

    /**
     * Get the title based on what is loaded.
     *
     * @return the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Open the specified file.  We expect this method to be called from
     * the startup (if a file is specified on the command line).
     *
     * @param file the file to open.
     */
    public void openFile(String filename) {
        LogTools.trace(logger, 25, "AppController.openFile(" + filename + ")");
        File file = new File(filename);
        ;
        if (!file.isAbsolute()) {
            File dir = new File(resources.getProperty("dss.user.dir"));
            LogTools.trace(logger, 25, "AppController.openFile(" + filename + ") - File is not absolute.  Prefixing with dss.user.dir=" + dir.getAbsolutePath());
            file = new File(dir, filename);
        }
        final File fileToOpen = file;
        LogTools.trace(logger, 25, "AppController.openFile().");
        runQ.submit(new Runnable() {

            public void run() {
                doOpen(fileToOpen);
            }
        });
    }

    /**
     * Launch a window.
     * 
     * @param d the dialog to launch.
     * @param enabler the menu enabler.
     * @param info information about the launch.
     * @param parent the application parent window/frame.
     */
    void launchWindow(JDialog d, final IEnabler enabler, String info, Frame parent) {
        LogTools.info(logger, "AppController.launchWindow(info=" + info + ")");
        try {
            windowMgr.add(d, info, parent, true);
        } catch (WindowManagerException wmex) {
            LogTools.warn(logger, "AppController.launchWindow() - Failed to add " + info + "to window manager.  Reason: " + wmex.getMessage());
        }
        d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        enabler.setEnabled(false);
        d.addWindowListener(new SmarterWindowAdapter(d) {

            public void windowClosed(WindowEvent wev) {
                enabler.setEnabled(true);
                super.windowClosing(wev);
            }

            public void windowClosing(WindowEvent wev) {
                enabler.setEnabled(true);
                super.windowClosed(wev);
            }
        });
        d.pack();
        WindowUtil.setLocationRelativeToCenter(d, parent);
        d.setVisible(true);
    }

    boolean okToOverwrite(File file) {
        LogTools.trace(logger, 25, "AppController.overwrite() - Perform overwrite checking on file " + file);
        if (!file.exists()) {
            LogTools.trace(logger, 25, "AppController.overwrite() - File " + file + " does not exist; ok to write");
            return true;
        }
        String message = resources.getProperty("dss.gui.main.file.overwrite.query.prefix", "OVERWRITE FILE ") + file.getAbsolutePath() + resources.getProperty("dss.gui.main.file.overwrite.query.suffix", "?");
        int option = overwriteOptionPane.showConfirmDialog(getFrame(), message, resources.getProperty("dss.gui.main.file.overwrite.query.title", "OVERWRITE VERIFICATION"), JOptionPane.YES_NO_OPTION);
        switch(option) {
            case JOptionPane.YES_OPTION:
                LogTools.trace(logger, 25, "AppController.okToOverwrite() - YES, overwrite.");
                return true;
            case JOptionPane.NO_OPTION:
                LogTools.trace(logger, 25, "AppController.okToOverwrite() - NO, don't overwrite.");
                return false;
            default:
                LogTools.warn(logger, "AppController.okToOverwrite() - Unexpected option; don't overwrite.");
                return false;
        }
    }

    /**
     * Create an empty analysis.
     *
     * @return an analysis reference.
     */
    private Analysis createAnalysis() {
        Describable describable = DescribableFactory.createMutable("", "", "");
        FullyMutableMatrix matrix = MatrixFactory.createMatrix(0, 0);
        MutableNotificationList cri = ListFactory.createList();
        MutableNotificationList alt = ListFactory.createList();
        Issue issue = ModelFactory.createIssue(describable, alt, cri, matrix);
        MutableNotificationList stakeholders = ListFactory.createList();
        MutableNotificationList cycles = ListFactory.createList();
        return ModelFactory.createAnalysis(issue, stakeholders, cycles);
    }

    /** Logger. */
    private static final Logger logger = LogFactory.getLogger();

    /** Resource provider. */
    private static final ResourceProvider resources = Singleton.Factory.getInstance().getResourceProvider();

    /** Window manager. */
    private static final WindowManager windowMgr = Singleton.Factory.getInstance().getWindowManager();

    /** Button provider. */
    private static final ButtonProvider buttons = Singleton.Factory.getInstance().getButtonProvider();

    /** User preferences. */
    private static final UserPreferences up = Singleton.Factory.getInstance().getUserPreferences();

    /** Help manager. */
    private static HelpManager helpMgr = Singleton.Factory.getInstance().getHelpManager();

    /** Browser manager. */
    private static BrowserManager browserMgr = Singleton.Factory.getInstance().getBrowserManager();
}
