package org.geoforge.app;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Locale;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.geoforge.app.actioncontroller.AcrMainGtrOgcAbs;
import org.geoforge.guihlp.GfrCSH;
import org.geoforge.guihlp.GfrHelpBroker;
import org.geoforge.guihlp.GfrHelpSet;
import org.geoforge.guihlp.IHelpOnThisSection;
import org.geoforge.guillc.optionpane.GfrOptionPaneAbs;
import org.geoforge.guillc.AppAbs;
import org.geoforge.guillc.actioncontroller.AcrMainAbs;
import org.geoforge.guillc.frame.FrmGfrAbs;
import org.geoforge.guillc.frame.FrmGfrMainAbs;
import org.geoforge.guillc.panel.PnlStatusBarMain;
import org.geoforge.guitlc.dialog.mainframe.GfrDlgConfirmExitApp;
import org.geoforge.io.util.property.PrpMgrPublicPropertiesSystemWriter;
import org.geoforge.lang.IShrObj;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public abstract class AppOgcAbs extends AppAbs implements WindowListener, IShrObj {

    private static final String[] _F_STRS_PROP_SYS_REQUIRED_ = { "_geoforge.appli.version" };

    static {
        System.setProperty("sun.io.serialization.extendedDebugInfo", "true");
        System.setProperty("_geoforge.appli.cache", ".geoforge");
    }

    static {
        String strOs = System.getProperty("os.name").toLowerCase();
        if (strOs.toLowerCase().compareTo("linux") == 0) {
            WorldWindowGLCanvas wwd = new WorldWindowGLCanvas();
            wwd.setPreferredSize(new java.awt.Dimension(400, 300));
            wwd.setModel(new BasicModel());
        }
    }

    protected native void cppStartRoot(String strPathAbsLog);

    protected native void cppEndRoot();

    static {
        try {
            String str = System.getProperty("_geoforge.appli.language");
            if (str != null && str.equalsIgnoreCase("english")) Locale.setDefault(Locale.ENGLISH);
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("Exception caught, don't care");
        }
    }

    static {
        for (int i = 0; i < AppOgcAbs._F_STRS_PROP_SYS_REQUIRED_.length; i++) {
            if (System.getProperty(AppOgcAbs._F_STRS_PROP_SYS_REQUIRED_[i]) == null) {
                String str = "missing property: " + AppOgcAbs._F_STRS_PROP_SYS_REQUIRED_[i] + ", exiting";
                System.err.println(str);
                JOptionPane.showMessageDialog(FrmGfrAbs.s_getFrameOwner(PnlStatusBarMain.s_getInstance()), str, "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
    }

    private static final Logger _LOGGER_ = Logger.getLogger(AppOgcAbs.class.getName());

    static {
        String strKey = "_geoforge.lib.cpp";
        String strValue = null;
        try {
            strValue = System.getProperty(strKey);
            if (strValue != null) System.loadLibrary(strValue);
        } catch (Exception exc) {
            exc.printStackTrace();
            String str = "Exception caught" + "\n" + "failed to load library, strKey=" + strKey + ", strValue=" + strValue;
            str += "\n exiting";
            JOptionPane.showMessageDialog(FrmGfrAbs.s_getFrameOwner(PnlStatusBarMain.s_getInstance()), str, "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (UnsatisfiedLinkError err) {
            err.printStackTrace();
            String str = "Exception caught" + "\n" + "failed to load library, strKey=" + strKey + ", strValue=" + strValue + ", err.getMessage()" + err.getMessage();
            str += "\n exiting";
            JOptionPane.showMessageDialog(FrmGfrAbs.s_getFrameOwner(PnlStatusBarMain.s_getInstance()), str, "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    protected boolean _blnIsSerializedFrameMain = false;

    protected javax.help.HelpBroker _hbr_ = null;

    public javax.help.HelpBroker getHelpBroker() {
        return this._hbr_;
    }

    protected AcrMainAbs _acr = null;

    protected AppOgcAbs(boolean blnSetLAFSwing, boolean blnInternAllowed) {
        super();
        AppOgcAbs._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
        AppOgcAbs._LOGGER_.info(System.getProperty("_geoforge.appli.title") + " now starting ..." + "\n");
        String strDebug = System.getProperty("_geoforge.debug");
        if (strDebug != null && strDebug.equalsIgnoreCase("true")) {
            AppOgcAbs._LOGGER_.info("user.home" + "=" + System.getProperty("user.home") + "\n");
            AppOgcAbs._LOGGER_.info("java.version" + "=" + System.getProperty("java.version") + "\n");
        }
        if (System.getProperty("_geoforge.lib.cpp") != null) {
            String strPathAbsLog = FileHandlerLogger.s_getPathAbsFileLogCpp();
            cppStartRoot(strPathAbsLog);
        }
        this._blnSetLAFSwing = blnSetLAFSwing;
        this._blnInternAllowed = blnInternAllowed;
        this._altMyHelpBroker = new ArrayList<javax.help.HelpBroker>();
        _attachShutDownHook_();
    }

    public boolean doQuit() {
        GfrDlgConfirmExitApp dlg = new GfrDlgConfirmExitApp(this._frm_);
        dlg.init();
        dlg.setVisible(true);
        if (dlg.getCancelled()) return false;
        return true;
    }

    @Override
    public void doExit() {
        if (!this.doQuit()) return;
        destroy();
        if (System.getProperty("_geoforge.lib.cpp") != null) cppEndRoot();
        try {
            PrpMgrPublicPropertiesSystemWriter.s_doJob();
        } catch (Exception exc) {
            exc.printStackTrace();
            String str = exc.getMessage();
            AppOgcAbs._LOGGER_.severe(str);
            GfrOptionPaneAbs.s_showDialogError(super._frm_, str);
            System.exit(1);
        }
        String str = System.getProperty("_geoforge.appli.title");
        str += " is exiting normally ...";
        AppOgcAbs._LOGGER_.info(str);
        System.exit(0);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        doExit();
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public boolean init() {
        if (!this._frm_.init()) return false;
        if (!this._acr.init()) {
            String str = "this._acr.init()";
            AppOgcAbs._LOGGER_.severe(str);
            return false;
        }
        _delayDoHelpBroker_();
        return true;
    }

    private void _packAndShowSerialized_() throws Exception {
    }

    public void packAndShow() throws Exception {
        if (this._blnIsSerializedFrameMain) _packAndShowSerialized_(); else {
            _packAndShowUnserialized_();
            return;
        }
        super._frm_.setVisible(true);
    }

    protected void _packAndShowUnserialized_() throws Exception {
        super._frm_.pack();
        super._frm_.setVisible(true);
        ((AcrMainGtrOgcAbs) this._acr).displaySpace();
    }

    @Override
    public void destroy() {
        try {
            super._frm_.setVisible(false);
            super._frm_.removeWindowListener((WindowListener) this);
            if (this._acr != null) {
                this._acr.destroy();
                this._acr = null;
            }
            super._frm_.releaseUnserialized();
        } catch (Exception exc) {
            exc.printStackTrace();
            String str = exc.getMessage();
            AppOgcAbs._LOGGER_.severe(str);
        }
        if (this._frm_ != null) {
            this._frm_.destroy();
            this._frm_ = null;
        }
        if (this._altMyHelpBroker != null) {
            for (int i = 0; i < this._altMyHelpBroker.size(); i++) {
                GfrHelpBroker mhbCur = (GfrHelpBroker) this._altMyHelpBroker.get(i);
                if (mhbCur != null) {
                    mhbCur.destroy();
                    mhbCur = null;
                }
            }
            this._altMyHelpBroker.clear();
            this._altMyHelpBroker = null;
        }
    }

    /**
   a new document/project has been opened, updating JavaHelp's JHEditorPane's StyleSheet
   arrayList should contain at least one element: helpStandard
    **/
    protected boolean _updateJavaHelpStyleSheets_() {
        if (this._altMyHelpBroker == null) {
            AppOgcAbs._LOGGER_.severe("this._altMyHelpBroker == null");
            return false;
        }
        for (int i = 0; i < this._altMyHelpBroker.size(); i++) {
            GfrHelpBroker mhbCur = null;
            try {
                mhbCur = (GfrHelpBroker) this._altMyHelpBroker.get(i);
            } catch (ClassCastException excClassCast) {
                excClassCast.printStackTrace();
                AppOgcAbs._LOGGER_.log(Level.SEVERE, "Exception", excClassCast);
                return false;
            }
            if (!mhbCur.assignStyleSheet()) {
                AppOgcAbs._LOGGER_.severe("! mhbCur.assignStyleSheet()");
                return false;
            }
        }
        return true;
    }

    protected javax.help.HelpBroker _doHelpBroker_(String strNameDirHS) {
        if (strNameDirHS == null) {
            AppOgcAbs._LOGGER_.severe("strNameDirHS == null");
            return null;
        }
        if (this._blnInternAllowed) {
            Locale loc = Locale.getDefault();
            String strLocLang = loc.getLanguage();
            if (strLocLang == null) {
                AppOgcAbs._LOGGER_.severe("this._blnInternAllowed, strLocLang == null");
                return null;
            }
            String[] strsSupported = { "fr" };
            for (int i = 0; i < strsSupported.length; i++) {
                if (strLocLang.toLowerCase().compareTo(strsSupported[i]) == 0) break;
            }
        }
        String strHelpsetPathRelativeShort = strNameDirHS;
        GfrHelpSet hst = null;
        try {
            ClassLoader clr = this.getClass().getClassLoader();
            java.net.URL url = javax.help.HelpSet.findHelpSet(clr, strHelpsetPathRelativeShort);
            if (url == null) {
                AppOgcAbs._LOGGER_.severe("url == null, strHelpsetPathRelativeShort=" + strHelpsetPathRelativeShort);
                return null;
            }
            hst = new GfrHelpSet(clr, url);
            if (hst == null) {
                AppOgcAbs._LOGGER_.severe("nil hst, strHelpsetPathRelativeShort=" + strHelpsetPathRelativeShort);
                AppOgcAbs._LOGGER_.severe("... nil hst, url.toString()=" + url.toString());
                return null;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            AppOgcAbs._LOGGER_.severe(exc.getMessage());
            return null;
        } catch (ExceptionInInitializerError errExceptionInInitializer) {
            errExceptionInInitializer.printStackTrace();
            AppOgcAbs._LOGGER_.severe(errExceptionInInitializer.getMessage());
            return null;
        }
        javax.help.HelpBroker hbrMine = new GfrHelpBroker(hst, this._blnSetLAFSwing);
        if (!((GfrHelpBroker) hbrMine).init()) {
            AppOgcAbs._LOGGER_.severe("! ((GfrHelpBroker) hbrMine).init(), strHelpsetPathRelativeShort=" + strHelpsetPathRelativeShort);
            return null;
        }
        this._altMyHelpBroker.add(hbrMine);
        return hbrMine;
    }

    private boolean _blnSetLAFSwing = false;

    private boolean _blnInternAllowed = false;

    private ArrayList<javax.help.HelpBroker> _altMyHelpBroker = null;

    protected static boolean _BLN_FAILED_JAVAHELP = false;

    protected static synchronized void s_showDialogWarningJavaHelp() {
        if (AppOgcAbs._BLN_FAILED_JAVAHELP) return;
        AppOgcAbs._BLN_FAILED_JAVAHELP = true;
        String str = "Failed to load integrated help";
        str += "\n\n" + "Possible reasons: If launched by WebStart, you may have an obsolete WebStart version";
        str += "\n" + "Else please contact GeoForge project";
        str += "\n\n" + "Integrated help will be disabled in this session";
        GfrOptionPaneAbs.s_showDialogWarning(FrmGfrAbs.s_getFrameOwner(PnlStatusBarMain.s_getInstance()), str);
    }

    @Override
    protected void _delayDoHelpBroker_() {
        if (AppOgcAbs._BLN_FAILED_JAVAHELP) return;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                String strKey = "_geoforge.appli.helpset.path.contents";
                String strPathRelHelpset = System.getProperty(strKey);
                if (strPathRelHelpset == null) {
                    String str = "strPathRelHelpset == null, strKey=" + strKey;
                    AppOgcAbs._LOGGER_.severe(str);
                    GfrOptionPaneAbs.s_showDialogError(null, str);
                    System.exit(1);
                }
                _hbr_ = _doHelpBroker_(strPathRelHelpset);
                if (_hbr_ == null) {
                    String str = "_hbr_ == null, exiting, strPathRelHelpset=" + strPathRelHelpset;
                    AppOgcAbs._LOGGER_.severe(str);
                    AppOgcAbs.s_showDialogWarningJavaHelp();
                    return;
                }
                ((IHelpOnThisSection) _frm_).setEnabledHelpOnThisSection(_hbr_);
                javax.swing.SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        if (_frm_ != null && _hbr_ != null) _frm_.setEnabledHelpKey(_hbr_);
                    }
                });
                if (!GfrCSH.s_checkAndDumpCmp2ID(_hbr_.getHelpSet())) {
                    String str = "! GfrCSH.s_checkAndDumpCmp2ID(_hbr_.getHelpSet())";
                    AppOgcAbs._LOGGER_.severe(str);
                    GfrOptionPaneAbs.s_showDialogError(null, str);
                    System.exit(1);
                }
            }
        });
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                String strKey = "_geoforge.appli.helpset.path.gettingstarted";
                javax.help.HelpBroker hbrHelpGettingStarted = _doHelpBroker_(System.getProperty(strKey));
                if (hbrHelpGettingStarted == null) {
                    AppOgcAbs._LOGGER_.severe("hbrHelpGettingStarted == null, strKey=" + strKey);
                    AppOgcAbs.s_showDialogWarningJavaHelp();
                    return;
                }
                ((FrmGfrMainAbs) _frm_).setEnabledHelpGettingStarted(hbrHelpGettingStarted);
            }
        });
    }

    protected void _packAndShowFirstRun() throws Exception {
        this._acr.displaySpaceFirstRun();
        this._frm_.pack();
    }

    private void _attachShutDownHook_() {
        Thread thr = new Thread() {

            @Override
            public void run() {
            }
        };
        thr.setPriority(Thread.MAX_PRIORITY);
        thr.setDaemon(true);
        Runtime.getRuntime().addShutdownHook(thr);
    }
}
