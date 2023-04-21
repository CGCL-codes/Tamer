package de.sciss.eisenkraut.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import de.sciss.eisenkraut.Main;
import de.sciss.eisenkraut.io.AudioStake;
import de.sciss.eisenkraut.io.PrefCacheManager;
import de.sciss.eisenkraut.net.SuperColliderClient;
import de.sciss.eisenkraut.session.Session;
import de.sciss.eisenkraut.util.PrefsUtil;
import de.sciss.app.AbstractApplication;
import de.sciss.app.AbstractWindow;
import de.sciss.app.DynamicPrefChangeManager;
import de.sciss.common.BasicApplication;
import de.sciss.common.BasicMenuFactory;
import de.sciss.common.BasicWindowHandler;
import de.sciss.common.ProcessingThread;
import de.sciss.gui.AbstractWindowHandler;
import de.sciss.gui.BooleanPrefsMenuAction;
import de.sciss.gui.IntPrefsMenuAction;
import de.sciss.gui.MenuAction;
import de.sciss.gui.MenuCheckItem;
import de.sciss.gui.MenuGroup;
import de.sciss.gui.MenuItem;
import de.sciss.gui.MenuRadioGroup;
import de.sciss.gui.MenuRadioItem;
import de.sciss.gui.MenuSeparator;
import de.sciss.io.AudioFileDescr;
import de.sciss.io.AudioFileFormatPane;
import de.sciss.util.Flag;
import de.sciss.util.Param;
import de.sciss.jcollider.Server;

/**
 *  <code>JMenu</code>s cannot be added to more than
 *  one frame. Since on MacOS there's one
 *  global menu for all the application windows
 *  we need to 'duplicate' a menu prototype.
 *  Synchronizing all menus is accomplished
 *  by using the same action objects for all
 *  menu copies. However when items are added
 *  or removed, synchronization needs to be
 *  performed manually. That's the point about
 *  this class.
 *  <p>
 *  There can be only one instance of <code>MenuFactory</code>
 *  for the application, and that will be created by the
 *  <code>Main</code> class.
 *
 *  @author		Hanns Holger Rutz
 *  @version	0.70, 31-May-08
 *
 *  @see	de.sciss.eisenkraut.Main#menuFactory
 */
public class MenuFactory extends BasicMenuFactory {

    private ActionOpen actionOpen;

    private ActionOpenMM actionOpenMM;

    private ActionNewEmpty actionNewEmpty;

    /**
	 *  The constructor is called only once by
	 *  the <code>Main</code> class and will create a prototype
	 *  main menu from which all copies are
	 *  derived.
	 *
	 *  @param  root	application root
	 */
    public MenuFactory(BasicApplication app) {
        super(app);
        createActions();
    }

    public ProcessingThread closeAll(boolean force, Flag confirmed) {
        final de.sciss.app.DocumentHandler dh = AbstractApplication.getApplication().getDocumentHandler();
        Session doc;
        ProcessingThread pt;
        while (dh.getDocumentCount() > 0) {
            doc = (Session) dh.getDocument(0);
            if (doc.getFrame() == null) {
                System.err.println("Yukk, no doc frame for " + doc.getDisplayDescr().file);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e1) {
                }
                confirmed.set(true);
                return null;
            }
            pt = doc.getFrame().closeDocument(force, confirmed);
            if (pt == null) {
                if (!confirmed.isSet()) return null;
            } else {
                return pt;
            }
        }
        confirmed.set(true);
        return null;
    }

    private void createActions() {
        actionNewEmpty = new ActionNewEmpty(getResourceString("menuNewEmpty"), KeyStroke.getKeyStroke(KeyEvent.VK_N, MENU_SHORTCUT));
        actionOpen = new ActionOpen(getResourceString("menuOpen"), KeyStroke.getKeyStroke(KeyEvent.VK_O, MENU_SHORTCUT));
        actionOpenMM = new ActionOpenMM(getResourceString("menuOpenMM"), KeyStroke.getKeyStroke(KeyEvent.VK_O, MENU_SHORTCUT + InputEvent.SHIFT_MASK));
    }

    protected void addMenuItems() {
        final Preferences prefs = getApplication().getUserPrefs();
        MenuGroup mg, smg;
        MenuCheckItem mci;
        MenuRadioGroup rg;
        BooleanPrefsMenuAction ba;
        IntPrefsMenuAction ia;
        int i;
        final int myCtrl = MENU_SHORTCUT == InputEvent.CTRL_MASK ? InputEvent.CTRL_MASK | InputEvent.ALT_MASK : InputEvent.CTRL_MASK;
        mg = (MenuGroup) get("file");
        smg = new MenuGroup("new", getResourceString("menuNew"));
        smg.add(new MenuItem("empty", actionNewEmpty));
        smg.add(new MenuItem("fromSelection", getResourceString("menuNewFromSelection")));
        mg.add(smg, 0);
        i = mg.indexOf("open");
        mg.add(new MenuItem("openMultipleMono", actionOpenMM), i + 1);
        i = mg.indexOf("closeAll");
        mg.add(new MenuSeparator(), i + 3);
        i = mg.indexOf("saveCopyAs");
        mg.add(new MenuItem("saveSelectionAs", getResourceString("menuSaveSelectionAs")), i + 1);
        i = indexOf("edit");
        mg = new MenuGroup("timeline", getResourceString("menuTimeline"));
        mg.add(new MenuItem("trimToSelection", getResourceString("menuTrimToSelection"), KeyStroke.getKeyStroke(KeyEvent.VK_F5, MENU_SHORTCUT)));
        mg.add(new MenuItem("insertSilence", getResourceString("menuInsertSilence"), KeyStroke.getKeyStroke(KeyEvent.VK_E, MENU_SHORTCUT + InputEvent.SHIFT_MASK)));
        mg.add(new MenuItem("insertRecording", getResourceString("menuInsertRec")));
        add(mg, i + 1);
        mg = new MenuGroup("process", getResourceString("menuProcess"));
        mg.add(new MenuItem("again", getResourceString("menuProcessAgain"), KeyStroke.getKeyStroke(KeyEvent.VK_F, MENU_SHORTCUT)));
        mg.addSeparator();
        smg = new MenuGroup("fscape", getResourceString("menuFScape"));
        smg.add(new MenuItem("needlehole", getResourceString("menuFScNeedlehole")));
        mg.add(smg);
        smg = new MenuGroup("sc", getResourceString("menuSuperCollider"));
        mg.add(smg);
        mg.addSeparator();
        mg.add(new MenuItem("fadeIn", getResourceString("menuFadeIn"), KeyStroke.getKeyStroke(KeyEvent.VK_I, myCtrl)));
        mg.add(new MenuItem("fadeOut", getResourceString("menuFadeOut"), KeyStroke.getKeyStroke(KeyEvent.VK_O, myCtrl)));
        mg.add(new MenuItem("gain", getResourceString("menuGain"), KeyStroke.getKeyStroke(KeyEvent.VK_N, myCtrl)));
        mg.add(new MenuItem("invert", getResourceString("menuInvert")));
        mg.add(new MenuItem("reverse", getResourceString("menuReverse")));
        mg.add(new MenuItem("rotateChannels", getResourceString("menuRotateChannels")));
        add(mg, i + 2);
        mg = new MenuGroup("operation", getResourceString("menuOperation"));
        ba = new BooleanPrefsMenuAction(getResourceString("menuInsertionFollowsPlay"), null);
        mci = new MenuCheckItem("insertionFollowsPlay", ba);
        ba.setCheckItem(mci);
        ba.setPreferences(prefs, PrefsUtil.KEY_INSERTIONFOLLOWSPLAY);
        mg.add(mci);
        add(mg, i + 3);
        mg = new MenuGroup("view", getResourceString("menuView"));
        smg = new MenuGroup("timeUnits", getResourceString("menuTimeUnits"));
        ia = new IntPrefsMenuAction(getResourceString("menuTimeUnitsSamples"), null, PrefsUtil.TIME_SAMPLES);
        rg = new MenuRadioGroup();
        smg.add(new MenuRadioItem(rg, "samples", ia));
        ia.setRadioGroup(rg);
        ia.setPreferences(prefs, PrefsUtil.KEY_TIMEUNITS);
        ia = new IntPrefsMenuAction(getResourceString("menuTimeUnitsMinSecs"), null, PrefsUtil.TIME_MINSECS);
        smg.add(new MenuRadioItem(rg, "minSecs", ia));
        ia.setRadioGroup(rg);
        ia.setPreferences(prefs, PrefsUtil.KEY_TIMEUNITS);
        mg.add(smg);
        smg = new MenuGroup("vertscale", getResourceString("menuVertScale"));
        ia = new IntPrefsMenuAction(getResourceString("menuVertScaleAmpLin"), null, PrefsUtil.VSCALE_AMP_LIN);
        rg = new MenuRadioGroup();
        smg.add(new MenuRadioItem(rg, "amplin", ia));
        ia.setRadioGroup(rg);
        ia.setPreferences(prefs, PrefsUtil.KEY_VERTSCALE);
        ia = new IntPrefsMenuAction(getResourceString("menuVertScaleAmpLog"), null, PrefsUtil.VSCALE_AMP_LOG);
        smg.add(new MenuRadioItem(rg, "amplog", ia));
        ia.setRadioGroup(rg);
        ia.setPreferences(prefs, PrefsUtil.KEY_VERTSCALE);
        ia = new IntPrefsMenuAction(getResourceString("menuVertScaleFreqSpect"), null, PrefsUtil.VSCALE_FREQ_SPECT);
        smg.add(new MenuRadioItem(rg, "freqspect", ia));
        ia.setRadioGroup(rg);
        ia.setPreferences(prefs, PrefsUtil.KEY_VERTSCALE);
        final IntPrefsMenuAction freqSpectAction = ia;
        new DynamicPrefChangeManager(prefs.node(PrefsUtil.NODE_VIEW), new String[] { PrefsUtil.KEY_SONAENABLED }, new PreferenceChangeListener() {

            public void preferenceChange(PreferenceChangeEvent pce) {
                freqSpectAction.setEnabled(prefs.node(PrefsUtil.NODE_VIEW).getBoolean(PrefsUtil.KEY_SONAENABLED, false));
            }
        }).startListening();
        mg.add(smg);
        ba = new BooleanPrefsMenuAction(getResourceString("menuViewNullLinie"), null);
        mci = new MenuCheckItem("nullLinie", ba);
        ba.setCheckItem(mci);
        ba.setPreferences(prefs, PrefsUtil.KEY_VIEWNULLLINIE);
        mg.add(mci);
        ba = new BooleanPrefsMenuAction(getResourceString("menuViewVerticalRulers"), null);
        mci = new MenuCheckItem("verticalRulers", ba);
        ba.setCheckItem(mci);
        ba.setPreferences(prefs, PrefsUtil.KEY_VIEWVERTICALRULERS);
        mg.add(mci);
        ba = new BooleanPrefsMenuAction(getResourceString("menuViewChanMeters"), null);
        mci = new MenuCheckItem("channelMeters", ba);
        ba.setCheckItem(mci);
        ba.setPreferences(prefs, PrefsUtil.KEY_VIEWCHANMETERS);
        mg.add(mci);
        ba = new BooleanPrefsMenuAction(getResourceString("menuViewMarkers"), null);
        mci = new MenuCheckItem("markers", ba);
        ba.setCheckItem(mci);
        ba.setPreferences(prefs, PrefsUtil.KEY_VIEWMARKERS);
        mg.add(mci);
        add(mg, i + 4);
        mg = (MenuGroup) get("window");
        mg.add(new MenuItem("ioSetup", new ActionIOSetup(getResourceString("frameIOSetup"), null)), 0);
        mg.add(new MenuSeparator(), 1);
        mg.add(new MenuItem("main", new ActionShowWindow(getResourceString("frameMain"), null, Main.COMP_MAIN)), 2);
        mg.add(new MenuItem("observer", new ActionObserver(getResourceString("paletteObserver"), KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, MENU_SHORTCUT))), 3);
        mg.add(new MenuItem("ctrlRoom", new ActionCtrlRoom(getResourceString("paletteCtrlRoom"), KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, MENU_SHORTCUT))), 4);
        mg = new MenuGroup("debug", "Debug");
        mg.add(new MenuItem("dumpPrefs", PrefsUtil.getDebugDumpAction()));
        mg.add(new MenuItem("dumpRegions", "Dump Region Structure"));
        mg.add(new MenuItem("verifyRegions", "Verify Regions Consistency"));
        mg.add(new MenuItem("dumpCache", PrefCacheManager.getInstance().getDebugDumpAction()));
        mg.add(new MenuItem("dumpAudioStakes", AudioStake.getDebugDumpAction()));
        mg.add(new MenuItem("dumpNodeTree", SuperColliderClient.getInstance().getDebugNodeTreeAction()));
        mg.add(new MenuItem("dumpKillAll", SuperColliderClient.getInstance().getDebugKillAllAction()));
        i = indexOf("help");
        add(mg, i);
    }

    public void showPreferences() {
        PrefsFrame prefsFrame = (PrefsFrame) getApplication().getComponent(Main.COMP_PREFS);
        if (prefsFrame == null) {
            prefsFrame = new PrefsFrame();
        }
        prefsFrame.setVisible(true);
        prefsFrame.toFront();
    }

    protected Action getOpenAction() {
        return actionOpen;
    }

    protected ActionOpenRecent createOpenRecentAction(String name, File path) {
        return new ActionEisKOpenRecent(name, path);
    }

    public void openDocument(File f) {
        actionOpen.perform(f);
    }

    public void openDocument(File[] fs) {
        actionOpenMM.perform(fs);
    }

    public Session newDocument(AudioFileDescr afd) {
        return actionNewEmpty.perform(afd);
    }

    public void addSCPlugIn(Action a, String[] hierarchy) {
        System.err.println("addSCPlugIn : NOT YET WORKING");
    }

    public void removeSCPlugIn(Action a) {
        System.err.println("removeSCPlugIn : NOT YET WORKING");
    }

    protected Session findDocumentForPath(File f) {
        final de.sciss.app.DocumentHandler dh = AbstractApplication.getApplication().getDocumentHandler();
        Session doc;
        AudioFileDescr[] afds;
        for (int i = 0; i < dh.getDocumentCount(); i++) {
            doc = (Session) dh.getDocument(i);
            afds = doc.getDescr();
            for (int j = 0; j < afds.length; j++) {
                if ((afds[j].file != null) && afds[j].file.equals(f)) {
                    return doc;
                }
            }
        }
        return null;
    }

    private class ActionNewEmpty extends MenuAction {

        private JPanel p = null;

        private AudioFileFormatPane affp;

        protected ActionNewEmpty(String text, KeyStroke shortcut) {
            super(text, shortcut);
        }

        public void actionPerformed(ActionEvent e) {
            final AudioFileDescr afd = query();
            if (afd != null) {
                perform(afd);
            }
        }

        private AudioFileDescr query() {
            final AudioFileDescr afd = new AudioFileDescr();
            final String[] queryOptions = { getResourceString("buttonCreate"), getResourceString("buttonCancel") };
            final int result;
            final Server.Status status;
            final double sampleRate;
            final Param param;
            final Preferences audioPrefs;
            if (p == null) {
                affp = new AudioFileFormatPane(AudioFileFormatPane.NEW_FILE_FLAGS);
                p = new JPanel(new BorderLayout());
                p.add(affp, BorderLayout.NORTH);
                AbstractWindowHandler.setDeepFont(affp);
            }
            status = SuperColliderClient.getInstance().getStatus();
            if (status != null) {
                sampleRate = status.sampleRate;
            } else {
                audioPrefs = getApplication().getUserPrefs().node(PrefsUtil.NODE_AUDIO);
                param = Param.fromPrefs(audioPrefs, PrefsUtil.KEY_AUDIORATE, null);
                if (param != null) {
                    sampleRate = param.val;
                } else {
                    sampleRate = 0.0;
                }
            }
            if (sampleRate != 0.0) {
                affp.toDescr(afd);
                afd.rate = sampleRate;
                affp.fromDescr(afd);
            }
            final JOptionPane op = new JOptionPane(p, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, queryOptions, queryOptions[0]);
            result = BasicWindowHandler.showDialog(op, null, getValue(NAME).toString());
            if (result == 0) {
                affp.toDescr(afd);
                return afd;
            } else {
                return null;
            }
        }

        protected Session perform(AudioFileDescr afd) {
            final Session doc;
            try {
                doc = Session.newEmpty(afd);
                AbstractApplication.getApplication().getDocumentHandler().addDocument(this, doc);
                doc.createFrame();
                return doc;
            } catch (IOException e1) {
                BasicWindowHandler.showErrorDialog(null, e1, getValue(Action.NAME).toString());
                return null;
            }
        }
    }

    private class ActionOpen extends MenuAction {

        protected ActionOpen(String text, KeyStroke shortcut) {
            super(text, shortcut);
        }

        public void actionPerformed(ActionEvent e) {
            File f = queryFile();
            if (f != null) perform(f);
        }

        private File queryFile() {
            final FileDialog fDlg;
            final String strFile, strDir;
            final AbstractWindow w = (AbstractWindow) getApplication().getComponent(Main.COMP_MAIN);
            final Frame frame = (w.getWindow() instanceof Frame) ? (Frame) w.getWindow() : null;
            final Preferences prefs = getApplication().getUserPrefs();
            fDlg = new FileDialog(frame, getResourceString("fileDlgOpen"), FileDialog.LOAD);
            fDlg.setDirectory(prefs.get(PrefsUtil.KEY_FILEOPENDIR, System.getProperty("user.home")));
            fDlg.setVisible(true);
            strDir = fDlg.getDirectory();
            strFile = fDlg.getFile();
            if (strFile == null) return null;
            prefs.put(PrefsUtil.KEY_FILEOPENDIR, strDir);
            return (new File(strDir, strFile));
        }

        /**
		 *  Loads a new document file.
		 *  a <code>ProcessingThread</code>
		 *  started which loads the new session.
		 *
		 *  @param  path	the file of the document to be loaded
		 *  
		 *  @synchronization	this method must be called in event thread
		 */
        protected void perform(File path) {
            Session doc;
            doc = findDocumentForPath(path);
            if (doc != null) {
                doc.getFrame().setVisible(true);
                doc.getFrame().toFront();
                return;
            }
            try {
                doc = Session.newFrom(path);
                addRecent(doc.getDisplayDescr().file);
                AbstractApplication.getApplication().getDocumentHandler().addDocument(this, doc);
                doc.createFrame();
            } catch (IOException e1) {
                BasicWindowHandler.showErrorDialog(null, e1, getValue(Action.NAME).toString());
            }
        }
    }

    private class ActionOpenMM extends MenuAction {

        protected ActionOpenMM(String text, KeyStroke shortcut) {
            super(text, shortcut);
        }

        public void actionPerformed(ActionEvent e) {
            File[] fs = queryFiles();
            if (fs != null) {
                if (fs.length == 0) {
                    final JOptionPane op = new JOptionPane(getResourceString("errFileSelectionEmpty"), JOptionPane.ERROR_MESSAGE);
                    BasicWindowHandler.showDialog(op, null, getValue(NAME).toString());
                    return;
                }
                perform(fs);
            }
        }

        private File[] queryFiles() {
            final JFileChooser fDlg = new JFileChooser();
            final int result;
            final Component c = ((AbstractWindow) getApplication().getComponent(Main.COMP_MAIN)).getWindow();
            final Preferences prefs = getApplication().getUserPrefs();
            final File[] files;
            fDlg.setMultiSelectionEnabled(true);
            fDlg.setDialogTitle(getValue(Action.NAME).toString());
            fDlg.setCurrentDirectory(new File(prefs.get(PrefsUtil.KEY_FILEOPENDIR, System.getProperty("user.home"))));
            result = fDlg.showOpenDialog(c);
            if (result == JFileChooser.APPROVE_OPTION) {
                files = fDlg.getSelectedFiles();
                if (files.length > 0) {
                    prefs.put(PrefsUtil.KEY_FILEOPENDIR, files[0].getParent());
                }
                return files;
            } else {
                return null;
            }
        }

        /**
		 *  Loads a new document file.
		 *  a <code>ProcessingThread</code>
		 *  started which loads the new session.
		 *
		 *  @param  path	the file of the document to be loaded
		 *  
		 *  @synchronization	this method must be called in event thread
		 */
        protected void perform(File[] paths) {
            if (paths.length == 0) return;
            Session doc;
            for (int j = 0; j < paths.length; j++) {
                doc = findDocumentForPath(paths[j]);
                if (doc != null) {
                    doc.getFrame().setVisible(true);
                    doc.getFrame().toFront();
                    return;
                }
            }
            try {
                doc = Session.newFrom(paths);
                addRecent(doc.getDisplayDescr().file);
                AbstractApplication.getApplication().getDocumentHandler().addDocument(this, doc);
                doc.createFrame();
            } catch (IOException e1) {
                BasicWindowHandler.showErrorDialog(null, e1, getValue(Action.NAME).toString());
            }
        }
    }

    private class ActionEisKOpenRecent extends ActionOpenRecent {

        private File[] paths;

        protected ActionEisKOpenRecent(String text, File path) {
            super(text, path);
        }

        protected void setPath(File path) {
            paths = new File[] { path };
            boolean enable = false;
            try {
                if (path == null) return;
                if (path.isFile()) {
                    enable = true;
                    return;
                }
                final String name = path.getName();
                final int idxOpenBr = name.indexOf('[');
                final int idxCloseBr = name.indexOf(']', idxOpenBr + 1);
                if ((idxOpenBr < 0) || ((idxOpenBr + 1) >= (idxCloseBr - 1))) return;
                final File parent = path.getParentFile();
                final String pre = name.substring(0, idxOpenBr);
                final String post = name.substring(idxCloseBr + 1);
                final StringTokenizer tok = new StringTokenizer(name.substring(idxOpenBr + 1, idxCloseBr), ",");
                paths = new File[tok.countTokens()];
                enable = true;
                for (int i = 0; i < paths.length; i++) {
                    paths[i] = new File(parent, pre + tok.nextToken() + post);
                    enable &= paths[i].isFile();
                }
            } finally {
                setEnabled(enable);
            }
        }

        /**
		 *  If a path was set for the
		 *  action and the user confirms
		 *  an intermitting confirm-unsaved-changes
		 *  dialog, the new session will be loaded
		 */
        public void actionPerformed(ActionEvent e) {
            if (paths.length == 1) {
                if (paths[0] == null) return;
                openDocument(paths[0]);
            } else {
                openDocument(paths);
            }
        }
    }

    private class ActionIOSetup extends MenuAction {

        protected ActionIOSetup(String text, KeyStroke shortcut) {
            super(text, shortcut);
        }

        /**
		 *  Brings up the IOSetup
		 */
        public void actionPerformed(ActionEvent e) {
            IOSetupFrame f = (IOSetupFrame) getApplication().getComponent(Main.COMP_IOSETUP);
            if (f == null) {
                f = new IOSetupFrame();
            }
            f.setVisible(true);
            f.toFront();
        }
    }

    private class ActionCtrlRoom extends MenuAction {

        protected ActionCtrlRoom(String text, KeyStroke shortcut) {
            super(text, shortcut);
        }

        /**
		 *  Brings up the IOSetup
		 */
        public void actionPerformed(ActionEvent e) {
            ControlRoomFrame f = (ControlRoomFrame) getApplication().getComponent(Main.COMP_CTRLROOM);
            if (f == null) {
                f = new ControlRoomFrame();
            }
            f.setVisible(true);
            f.toFront();
        }
    }

    private class ActionObserver extends MenuAction {

        protected ActionObserver(String text, KeyStroke shortcut) {
            super(text, shortcut);
        }

        /**
		 *  Brings up the IOSetup
		 */
        public void actionPerformed(ActionEvent e) {
            ObserverPalette f = (ObserverPalette) getApplication().getComponent(Main.COMP_OBSERVER);
            if (f == null) {
                f = new ObserverPalette();
            }
            f.setVisible(true);
            f.toFront();
        }
    }
}
