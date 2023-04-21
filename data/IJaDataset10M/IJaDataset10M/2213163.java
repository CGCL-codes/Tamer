package com.simpledata.bc.datatools;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import org.apache.log4j.Logger;
import com.simpledata.bc.BC;
import com.simpledata.bc.Desktop;
import com.simpledata.bc.Params;
import com.simpledata.bc.Resources;
import com.simpledata.bc.SoftInfos;
import com.simpledata.bc.datamodel.Tarification;
import com.simpledata.bc.datamodel.TarificationHeader;
import com.simpledata.bc.tools.Lang;
import com.simpledata.bc.uicomponents.FileChooser;
import com.simpledata.bc.uicomponents.conception.CreatorGold;
import com.simpledata.bc.uicomponents.conception.CreatorLight;
import com.simpledata.bc.uicomponents.simulation.Simulator;
import com.simpledata.bc.uicomponents.tools.TarificationPropertiesPanel;
import com.simpledata.bc.uitools.ModalDialogBox;
import com.simpledata.bc.uitools.ModalJPanel;
import com.simpledata.filetools.FileUtilities;
import com.simpledata.filetools.Secu;
import com.simpledata.filetools.SecuSelf;
import com.simpledata.filetools.encoders.SelfDC_GZIP;
import com.simpledata.filetools.encoders.SelfDT_Serializable;

/**
 * Class with all tools including GUI for file Management
 */
public class FileManagement {

    /** Differents load / save methods */
    private static final int TYPE_ANY = -1;

    private static final int TYPE_OBJECT_COMPRESS = 0;

    private static final int TYPE_XML_COMPRESS = 1;

    /** Default load / save method */
    private static final int TYPE_PREFERED_METHOD = TYPE_XML_COMPRESS;

    /** Map between local constants and Secu saving method constants */
    private static final int[] methodSave = { Secu.METHOD_OBJECT_COMPRESS_DES, Secu.METHOD_XML_COMPRESS_DES };

    /** Map between save methods and file name extensions */
    static final String[] tarificationFileExt = { "tye", "tye" };

    /** Map between save methods and descriptions */
    static final String[] tarificationTitle = { "Tarification Quick Save", "Tarification Exchange Format" };

    /** 
	 * default icon dimensions for the icon on the filesystem 
	 * maybe directly changed 
	 */
    public static Dimension defaultIconDimension = new Dimension(32, 32);

    /** New tarification operation */
    public static final String CREATOR_NEW = "Creator:new";

    /** Open a tarification */
    public static final String CREATOR_OPEN = "Creator:open";

    /** New tarification using creator live */
    public static final String CREATOR_GOLD_NEW = "Creator:Gold:new";

    /** Open a tarification using creator live */
    public static final String CREATOR_GOLD_OPEN = "Creator:Gold:open";

    /** Start a new simulation */
    public static final String SIMULATOR_NEW = "Simulator:new";

    /** Open an existing portfolio */
    public static final String SIMULATOR_OPEN = "Simulator:open";

    /** Pusblish a tarification */
    public static final String CREATOR_PUBLISH = "Creator:Publish";

    /** Save a tarification */
    public static final String CREATOR_SAVE = "Creator:save";

    /** Save a porfolio */
    public static final String SIMULATOR_SAVE = "Simulator:save";

    /** Logger */
    private static final Logger m_log = Logger.getLogger(FileManagement.class);

    /** static instance to avoid double file chooser spawn */
    private static FileChooser uniqueChooser = null;

    /**
	 * Save the tarification, without poping a browser if possible.
	 * @param t The tarification to save
	 * @param savingType One of the constants. It describes where the save
	 * operation was called from. 
	 * @return true iif the save succeded.
	 */
    public static boolean save(Tarification t, String savingType) {
        if (savingType.equals(CREATOR_SAVE)) return saveTarification(t); else if (savingType.equals(SIMULATOR_SAVE)) return savePortofolio(t); else {
            m_log.error("Called save with unknown saving type");
            return false;
        }
    }

    /**
	 * Open a File browser to save a Tarification
	 * @param t The tarification object to save
	 * @param operationType one of the constants. 
	 * It describe from where the save operation was called. 
	 * @return true iif the save succeded.
	 */
    public static boolean saveAs(Tarification t, String operationType) {
        TarificationHeader header = t.getHeader();
        if (operationType.equals(CREATOR_SAVE)) header.setDataType(TarificationHeader.TYPE_TARIFICATION_MODIFIED); else if (operationType.equals(SIMULATOR_SAVE)) header.setDataType(TarificationHeader.TYPE_PORTFOLIO); else if (operationType.equals(CREATOR_PUBLISH)) header.setDataType(TarificationHeader.TYPE_TARIFICATION_ORIGINAL); else {
            m_log.warn("Unknow operation type for SaveAs...");
            return false;
        }
        return saveDialog(t, operationType);
    }

    /**
	 * This save quickly a file. It uses the object save method,
	 * and show no progress bar.
	 * @param t the tarification object to save.
	 * @param autoSaveFile the file to overwrite.
	 */
    static void bgrObjectSave(Tarification t, File autoSaveFile) {
        TarificationHeader header = t.updateAndGetHeader();
        header.setIdLicense(SoftInfos.id());
        header.setLicensedCompanyName(BC.getParameterStr("companyName"));
        SecuSelf ss = new SecuSelf(new SelfDT_Serializable(header), new SelfDT_Serializable(t));
        ss.insertDataEncoder(new SelfDC_GZIP());
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(autoSaveFile);
            ss.commit(fos);
        } catch (Exception e) {
            m_log.error("Failed autosave : ", e);
        }
    }

    /**
	 * This method is called within the desktop (mainly) 
	 * to open in a determined context (i.e. creator, creator-gold, simulator)
	 * a new or an existing tarification... It loads a tarification and open
	 * it in the specified window.
	 * @param rootFrame the frame the file browser will be attached to
	 * @param openingType the type of app and tarification that has to be loaded
	 */
    public static void promptFileAndStartApp(final JFrame rootFrame, final String openingType) {
        String[] typesOfContent = TarificationHeader.tagsForTypeAny;
        String creatorApp = "creator";
        String simulatorApp = "simulator";
        String creatorGoldApp = "creatorGold";
        String app = creatorApp;
        File fileForAutoLoad = null;
        if (openingType == CREATOR_NEW) {
            app = creatorApp;
            fileForAutoLoad = Resources.getBlankTariffLocation();
        } else if (openingType == CREATOR_OPEN) {
            app = creatorApp;
            typesOfContent = TarificationHeader.tagsForTypeAnyTarifications;
        } else if (openingType == CREATOR_GOLD_NEW) {
            app = creatorGoldApp;
            fileForAutoLoad = Resources.getBlankTariffLocation();
        } else if (openingType == CREATOR_GOLD_OPEN) {
            app = creatorGoldApp;
            typesOfContent = TarificationHeader.tagsForTypeAny;
        } else if (openingType == SIMULATOR_NEW) {
            app = simulatorApp;
            typesOfContent = TarificationHeader.tagsForTypeAnyTarifications;
        } else if (openingType == SIMULATOR_OPEN) {
            app = simulatorApp;
            typesOfContent = TarificationHeader.tagsForTypeAnyPortfolio;
        } else {
            m_log.error("Got a wrong openingType parameter : '" + openingType + "'");
        }
        Tarification t = null;
        if (fileForAutoLoad != null) {
            t = loadTarification(fileForAutoLoad, rootFrame);
            if (t != null) {
                t.getHeader().setLoadingLocation(null);
                if (!t.getHeader().isOpenableInDemo()) {
                    t = null;
                    ModalDialogBox.alert(rootFrame, "Empty tariff is not valid");
                }
            }
        } else {
            t = openTarification(typesOfContent, rootFrame, openingType);
        }
        if (t == null) return;
        if (app == creatorApp) {
            CreatorLight.openTarification(t);
        } else if (app == creatorGoldApp) {
            CreatorGold.openTarification(t);
        } else {
            Simulator.openSimulation(t);
        }
    }

    private static final String[] toolsTitles = new String[] { "dummy", Desktop.MENU_TITLE_SIMULATION, Desktop.MENU_TITLE_CREATION, Desktop.MENU_TITLE_CREATION_LIVE };

    /**
	 * Retrive data from an autosave file, and deletes it after.
	 * 
	 * @param file autosave file.
	 * @param rootFrame component.
	 * @param tool 0:unkown, 1:simulation, 2:creation, 3:creationLive
	 */
    public static void openExternal(File file, final JFrame rootFrame, int tool) {
        if (file.isDirectory() || (!file.exists())) return;
        TarificationFileView tfv = new TarificationFileView(TYPE_ANY, TarificationHeader.tagsForTypeAny);
        TarificationHeader tarifHeader = tfv.getAcceptAndHeader(file, false);
        if (tarifHeader == null) {
            m_log.warn("User tried to open a forbiden file" + file);
            BC.bc.alertUser(Lang.translate("This file is not valid or you do not have the " + "credentials to open it"), "");
            return;
        }
        Tarification result = null;
        try {
            Secu.Monitor monitor = LoadingDialog.getJInternalFrame(rootFrame);
            result = IOLib.load(file, monitor);
        } catch (Exception e) {
            m_log.warn("The file " + file + " cannot be retrieved. Giving up.", e);
            return;
        }
        if (result == null) {
            m_log.warn("The file " + file + " cannot be retrieved. Giving up.");
            return;
        }
        result.setReadyForCalcul();
        boolean isPortfolio = TarificationHeader.TYPE_PORTFOLIO.equals(tarifHeader.getDataType());
        ArrayList tools = new ArrayList();
        switch(tool) {
            case -1:
                tools.add(new Integer(1));
                if (!isPortfolio) if (SoftInfos.canGoCreation()) tools.add(new Integer(2));
                if (SoftInfos.canGoCreationGold()) tools.add(new Integer(3));
                break;
            case 3:
                if (SoftInfos.canGoCreationGold()) {
                    tools.add(new Integer(3));
                    break;
                }
            case 2:
                if (SoftInfos.canGoCreation()) {
                    tools.add(new Integer(2));
                    break;
                }
            case 1:
                tools.add(new Integer(1));
                break;
        }
        if (tools.size() == 0) {
            m_log.warn("Cann find a suitable tool to open " + file);
            return;
        }
        int choosenTool = ((Integer) tools.get(0)).intValue();
        if (tools.size() > 1) {
            String m = "<HTML><B>" + Lang.translate("Choose a tool to open:") + "</B><BR>" + "<CENTER>" + tarifHeader.getTitle() + "<BR>" + file.getName() + "</CENTER></HTML>";
            String[] options = new String[tools.size() + 1];
            options[options.length - 1] = Lang.translate("Cancel");
            int j = 0;
            for (Iterator i = tools.iterator(); i.hasNext(); j++) {
                String t = toolsTitles[((Integer) i.next()).intValue()];
                options[j] = Lang.translate(t);
            }
            int choice = ModalDialogBox.custom(rootFrame, m, options, tarifHeader.getIcon());
            if (choice >= 0 && choice < tools.size()) {
                choosenTool = ((Integer) tools.get(choice)).intValue();
            } else {
                return;
            }
        }
        switch(choosenTool) {
            case 1:
                Simulator.openSimulation(result).needSave();
                break;
            case 2:
                CreatorLight.openTarification(result).needSave();
                break;
            case 3:
                CreatorGold.openTarification(result).needSave();
                break;
        }
    }

    /**
	 * Prompt the user with a chooser to select a tarification file.
	 * Load the file and returen the Tarification
	 * @param rootFrame the frame to go on top of
	 * @param typesOfContent one of TarificationHeader.tagsForXXXX
	 * else will open Simulator
	 * @return Tarification, null if it fails.
	 */
    public static Tarification openTarification(String[] typesOfContent, final JFrame rootFrame, String openingType) {
        if (uniqueChooser != null) {
            if (uniqueChooser.isShowing()) {
                return null;
            }
        }
        File openPath = retrieveUserPreferredPath(openingType);
        if (openingType.equals(SIMULATOR_OPEN)) {
            uniqueChooser = new FileChooser(openPath, false);
        } else {
            uniqueChooser = new FileChooser(openPath, true);
        }
        if (openingType.equals(SIMULATOR_NEW)) {
            uniqueChooser.switchToLibrary();
        }
        TarificationFileView tfv = new TarificationFileView(TYPE_ANY, typesOfContent);
        if (TarificationHeader.tagsForTypeAnyPortfolio.equals(typesOfContent)) {
            tfv.setFilterTitle(Lang.translate("Portofolio"));
        } else if (TarificationHeader.tagsForTypeAnyTarifications.equals(typesOfContent)) {
            tfv.setFilterTitle(Lang.translate("Tarification"));
        }
        uniqueChooser.addFileView(tfv, 0);
        int returnVal = uniqueChooser.showOpenDialog(rootFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = uniqueChooser.getSelectedFile().getAbsoluteFile();
            m_log.info("Selected file: " + file.getName());
            if (file != null && !uniqueChooser.fileFromLibrary()) {
                saveUserPath(openingType, file.getParentFile());
            }
            if (file.isDirectory()) {
                m_log.warn("user tried to open a directory");
                return null;
            }
            if (!tfv.accept(file)) {
                m_log.warn("user tried to open a forbiden file");
                return null;
            }
            if (!tfv.canDecrypt(file)) {
                ModalDialogBox.alert(rootFrame, Lang.translate("You do not have the credentials to open this file"));
                return null;
            }
            return loadTarification(file, rootFrame);
        }
        m_log.info("File open Canceled ");
        return null;
    }

    /** Save the tarificaiton as a not original tarification */
    private static boolean saveTarification(Tarification t) {
        boolean success;
        if (t == null) return false;
        TarificationHeader header = t.getHeader();
        String dataType = header.getDataType();
        if (dataType.equals(TarificationHeader.TYPE_TARIFICATION_MODIFIED)) {
            File location = t.getHeader().myLoadingLocation();
            if (location == null) success = saveDialog(t, CREATOR_SAVE); else {
                success = generateTyeFile(t, location, TYPE_PREFERED_METHOD, BC.bc.getMajorComponent());
            }
        } else if (dataType.equals(TarificationHeader.TYPE_TARIFICATION_ORIGINAL)) {
            header.setDataType(TarificationHeader.TYPE_TARIFICATION_MODIFIED);
            success = saveDialog(t, CREATOR_SAVE);
        } else {
            m_log.error("Trying to override a portofolio with a tarification");
            success = false;
        }
        return success;
    }

    /** Save the tarification as a portfolio */
    private static boolean savePortofolio(Tarification t) {
        boolean success;
        if (t == null) return false;
        TarificationHeader header = t.getHeader();
        String dataType = header.getDataType();
        if (dataType.equals(TarificationHeader.TYPE_PORTFOLIO)) {
            File location = header.myLoadingLocation();
            if (location == null) {
                success = saveDialog(t, SIMULATOR_SAVE);
            } else success = generateTyeFile(t, location, TYPE_PREFERED_METHOD, BC.bc.getMajorComponent());
        } else {
            header.setDataType(TarificationHeader.TYPE_PORTFOLIO);
            success = saveDialog(t, SIMULATOR_SAVE);
        }
        return success;
    }

    /** Asks the user where he wants to save his file. And save it */
    private static boolean saveDialog(Tarification t, String operationType) {
        boolean success;
        JFrame rootFrame = BC.bc.getMajorComponent();
        File userDir = retrieveUserPreferredPath(operationType);
        FileChooser fc;
        String buttonName = Lang.translate("Save");
        if (operationType.equals(CREATOR_PUBLISH)) {
            JPanel propPanel = new TarificationPropertiesPanel(t);
            JPanel[] options = { propPanel };
            fc = new FileChooser(userDir, false, options);
            fc.switchToLibrary();
            buttonName = Lang.translate("Publish");
        } else {
            fc = new FileChooser(userDir, false);
        }
        fc.addFileView(new TarificationFileView(TYPE_OBJECT_COMPRESS, null), 0);
        fc.addFileView(new TarificationFileView(TYPE_XML_COMPRESS, null), 0);
        int returnVal = fc.showDialog(rootFrame, buttonName, FileChooser.SAVE_DIALOG);
        if (returnVal == FileChooser.APPROVE_OPTION) {
            TarificationFileView tfv = (TarificationFileView) fc.getSelectedFileView();
            assert tfv.getType() >= 0;
            int choosenMethod = tfv.getType();
            File file = FileUtilities.forceExtension(fc.getSelectedFile(), tarificationFileExt[choosenMethod]);
            m_log.info("Selected file: " + file.getName());
            saveUserPath(operationType, file.getParentFile());
            if (file != null) {
                success = generateTyeFile(t, file, choosenMethod, rootFrame);
                TarificationHeader header = t.getHeader();
                if (!operationType.equals(CREATOR_PUBLISH)) {
                    header.setLoadingLocation(file);
                }
                m_log.debug("DataType after save: " + header.getDataType());
            } else {
                m_log.info("Cancel ");
                success = false;
            }
        } else {
            m_log.info("Cancel ");
            success = false;
        }
        return success;
    }

    /**
	 * Generate the tarification file. Overriding the file if it already exists. 
	 */
    private static boolean generateTyeFile(Tarification t, File file, int choosenMethod, JFrame rootFrame) {
        boolean success;
        if (file != null) {
            Secu.Monitor monitor = LoadingDialog.getJInternalFrame(rootFrame);
            int method = methodSave[choosenMethod];
            TarificationHeader header = t.updateAndGetHeader();
            header.setModificationDate(new Date());
            header.setIdLicense(SoftInfos.id());
            header.setLicensedCompanyName(BC.getParameterStr("companyName"));
            success = IOLib.saveSecure(header, t, file, method, monitor);
        } else {
            m_log.error("Cannot generate tye file for null tarification.");
            success = false;
        }
        return success;
    }

    /**
	 * Try to load a tarification directly from a determined file.
	 * @param file
	 * @param rootFrame
	 * @return tarification if found else null
	 */
    private static Tarification loadTarification(File file, final JFrame rootFrame) {
        if (file != null) {
            try {
                Secu.Monitor monitor = LoadingDialog.getJInternalFrame(rootFrame);
                Tarification t = IOLib.load(file, monitor);
                if (t != null) {
                    t.getHeader().setLoadingLocation(file);
                    m_log.debug("DataType after load: " + t.getHeader().getDataType());
                    t.setReadyForCalcul();
                    return t;
                }
            } catch (ClassCastException e) {
                m_log.error("failed loading data", e);
            } catch (Exception e) {
                m_log.error("failed loading data", e);
            }
        }
        return null;
    }

    /** Compute the save directory, using the user's preferences */
    private static File defaultSaveDir() {
        File saveDir = new File(BC.getParameterStr(Params.KEY_DEFAULT_SAVED_TARIFICATION_PATH));
        if (!saveDir.exists()) {
            m_log.warn("Current user dir has been moved or deleted. Use user home");
            String userHome = Resources.findMyDocumentFolder();
            BC.setParameter(Params.KEY_DEFAULT_SAVED_TARIFICATION_PATH, userHome);
            saveDir = new File(userHome);
        }
        if (!saveDir.exists()) {
            File[] fsRoot = File.listRoots();
            if (fsRoot.length > 0) {
                m_log.warn("The system user.home directory doesn't exist. Using a FS root");
                saveDir = fsRoot[0];
            } else m_log.error("The system havn't any root file system. Rebuy a computer.");
        }
        return saveDir;
    }

    /**
	 * Save the last path used
	 */
    private static void saveUserPath(String operationType, File dir) {
        if (!dir.exists()) return;
        if (operationType.equals(CREATOR_SAVE) || operationType.equals(CREATOR_OPEN) || operationType.equals(CREATOR_GOLD_OPEN) || operationType.equals(SIMULATOR_NEW)) BC.setParameter(Params.KEY_LAST_TARIFICATION_FOLDER, dir.getAbsolutePath()); else if (operationType.equals(SIMULATOR_OPEN) || operationType.equals(SIMULATOR_SAVE)) BC.setParameter(Params.KEY_LAST_SIMULATION_FOLDER, dir.getAbsolutePath());
    }

    /**
	 * Return the best path for tarification saving and loading
	 */
    private static File retrieveUserPreferredPath(String operationType) {
        File result = null;
        int userPref = ((Integer) BC.getParameter(Params.KEY_OPEN_FOLDER_PREF, Integer.class)).intValue();
        if (userPref == Params.PREF_LAST_FOLDER) {
            if (operationType.equals(SIMULATOR_OPEN) || operationType.equals(SIMULATOR_SAVE)) {
                String path = BC.getParameterStr(Params.KEY_LAST_SIMULATION_FOLDER);
                if (path != null) result = new File(path);
            } else if (operationType.equals(CREATOR_GOLD_OPEN) || operationType.equals(CREATOR_OPEN) || operationType.equals(CREATOR_SAVE) || operationType.equals(SIMULATOR_NEW)) {
                String path = BC.getParameterStr(Params.KEY_LAST_TARIFICATION_FOLDER);
                if (path != null) result = new File(path);
            }
        }
        if (result != null) if (!result.exists()) result = null;
        if (result == null) result = defaultSaveDir();
        return result;
    }
}

abstract class LoadingDialog implements Secu.Monitor {

    JPanel jpz = null;

    private HashMap monitorMap;

    public LoadingDialog() {
        jpz = new JPanel();
    }

    /** 
     * if an error occures, this method is called with the reason
     * message as parameter, and an Error code which is one one of
     * ERROR_XXXX<BR>
     * When an error occure the loading must be finished
     * @param code one of Monitor.ERROR_XXXX
     * @param textual informations about the error
     */
    public final void error(int code, String message, Throwable e) {
        message = "<HTML><B>" + Lang.translate("An error occured, operation cannot be completed.") + "</B><HR><SMALL>" + message + "</SMALL><HR>" + "<SMALL>Guru meditation:" + e + "</SMALL>" + "</HTML>";
        ModalDialogBox.alert(jpz, message);
        done();
    }

    /**
	 * @see Secu.Monitor#setMonitors(java.lang.String[])
	 */
    public void setMonitors(String[] monitors) {
        if (monitorMap != null) return;
        jpz.setLayout(new BoxLayout(jpz, BoxLayout.Y_AXIS));
        monitorMap = new HashMap();
        for (int i = 0; i < monitors.length; i++) {
            JPanel jp = new JPanel(new BorderLayout());
            JProgressBar jpb = new JProgressBar();
            jpb.setMinimumSize(new Dimension(20, 200));
            JLabel jb = new JLabel(Lang.translate(monitors[i]));
            jb.setAlignmentX(Component.LEFT_ALIGNMENT);
            jp.add(jb, BorderLayout.NORTH);
            jp.add(jpb, BorderLayout.CENTER);
            jpz.add(jp);
            monitorMap.put(monitors[i], jpb);
        }
        show();
    }

    /** called when needed to show up **/
    abstract void show();

    /**
	 * @see com.simpledata.filetools.Secu.Monitor#valueChange(String monitor, long value,long pos)
	 */
    public void valueChange(String monitor, long value, long pos) {
        if ((monitorMap == null) || (jpz == null)) {
            return;
        }
        JProgressBar jpb = (JProgressBar) monitorMap.get(monitor);
        if (jpb == null) {
            return;
        }
        int v = new Long(value).intValue();
        jpb.setIndeterminate(value < 0);
        jpb.setValue(v);
        jpz.repaint();
    }

    /**
	 * @see com.simpledata.filetools.Secu.Monitor#done()
	 */
    public abstract void done();

    /** create a modal JinternalFrame **/
    public static Secu.Monitor getJInternalFrame(final JFrame owner) {
        final JInternalFrame jif = new JInternalFrame("", true, false);
        jif.setFrameIcon(Resources.iconLoading);
        final ModalJPanel mjp = ModalJPanel.warpJInternalFrame(jif, owner, new Point(10, 60), Resources.modalBgColor);
        jif.pack();
        jif.show();
        LoadingDialog me = new LoadingDialog() {

            void show() {
                jif.pack();
                jif.show();
            }

            public void done() {
                jif.dispose();
                mjp.close();
            }
        };
        jif.getContentPane().add(me.jpz, BorderLayout.CENTER);
        return me;
    }

    /** create a JDialog **/
    public static Secu.Monitor getJDialog(JFrame owner) {
        final JDialog jd = new JDialog(owner, false);
        LoadingDialog me = new LoadingDialog() {

            void show() {
                jd.pack();
                jd.show();
            }

            public void done() {
                jd.dispose();
            }
        };
        jd.getContentPane().add(me.jpz, BorderLayout.CENTER);
        return me;
    }
}
