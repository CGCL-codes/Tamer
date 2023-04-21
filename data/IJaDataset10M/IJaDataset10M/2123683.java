package de.sciss.common;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.Method;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import net.roydesign.app.AboutJMenuItem;
import net.roydesign.app.PreferencesJMenuItem;
import net.roydesign.app.QuitJMenuItem;
import de.sciss.app.AbstractWindow;
import de.sciss.app.DocumentEvent;
import de.sciss.app.DocumentHandler;
import de.sciss.app.DocumentListener;
import de.sciss.gui.AboutBox;
import de.sciss.gui.MenuAction;
import de.sciss.gui.MenuGroup;
import de.sciss.gui.MenuItem;
import de.sciss.gui.MenuRadioGroup;
import de.sciss.gui.MenuRadioItem;
import de.sciss.gui.MenuRoot;
import de.sciss.gui.MenuSeparator;
import de.sciss.gui.PathList;
import de.sciss.io.IOUtil;
import de.sciss.util.Flag;

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
 *  @version	0.71, 26-Aug-08
 */
public abstract class BasicMenuFactory extends MenuRoot implements DocumentListener {

    private static final String KEY_OPENRECENT = "recent";

    /**
	 *	<code>KeyStroke</code> modifier mask
	 *	representing the platform's default
	 *	menu accelerator (e.g. Apple-key on Mac,
	 *	Ctrl on Windows).
	 *
	 *	@see	Toolkit#getMenuShortcutKeyMask()
	 */
    public static final int MENU_SHORTCUT = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    private int uniqueNumber = 0;

    private MenuGroup mgWindow;

    private MenuRadioGroup mWindowRadioGroup;

    protected MenuGroup mgRecent;

    protected final PathList openRecentPaths;

    protected ActionOpenRecent actionOpenRecent;

    private Action actionClearRecent;

    private Action actionCloseAll;

    private final BasicApplication root;

    /**
	 *  The constructor is called only once by
	 *  the <code>Main</code> class and will create a prototype
	 *  main menu from which all copies are
	 *  derived.
	 */
    public BasicMenuFactory(BasicApplication app) {
        super();
        root = app;
        openRecentPaths = new PathList(8, root.getUserPrefs(), KEY_OPENRECENT);
    }

    public void init() {
        createActions();
        createProtoType();
        root.getDocumentHandler().addDocumentListener(this);
    }

    public BasicApplication getApplication() {
        return root;
    }

    public ProcessingThread closeAll(boolean force, Flag confirmed) {
        final DocumentHandler dh = root.getDocumentHandler();
        BasicDocument doc;
        ProcessingThread pt;
        while (dh.getDocumentCount() > 0) {
            doc = (BasicDocument) dh.getDocument(0);
            pt = doc.closeDocument(force, confirmed);
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
        actionOpenRecent = createOpenRecentAction(getResourceString("menuOpenRecent"), null);
        actionClearRecent = new ActionClearRecent(getResourceString("menuClearRecent"), null);
        actionCloseAll = new ActionCloseAll(getResourceString("menuCloseAll"), null);
    }

    protected ActionOpenRecent createOpenRecentAction(String name, File path) {
        return new ActionOpenRecent(name, path);
    }

    private void createProtoType() {
        MenuGroup mg;
        Action a;
        mg = new MenuGroup("file", getResourceString("menuFile"));
        mg.add(new MenuItem("open", getOpenAction()));
        mgRecent = new MenuGroup("openRecent", actionOpenRecent);
        if (openRecentPaths.getPathCount() > 0) {
            for (int i = 0; i < openRecentPaths.getPathCount(); i++) {
                mgRecent.add(new MenuItem(String.valueOf(uniqueNumber++), createOpenRecentAction(null, openRecentPaths.getPath(i))));
            }
            actionOpenRecent.setPath(openRecentPaths.getPath(0));
            actionOpenRecent.setEnabled(true);
            actionClearRecent.setEnabled(true);
        }
        mgRecent.addSeparator();
        mgRecent.add(new MenuItem("clearRecent", actionClearRecent));
        mg.add(mgRecent);
        if (root.getDocumentHandler().isMultiDocumentApplication()) {
            mg.add(new MenuItem("close", getResourceString("menuClose"), KeyStroke.getKeyStroke(KeyEvent.VK_W, MENU_SHORTCUT)));
            mg.add(new MenuItem("closeAll", actionCloseAll));
        }
        mg.add(new MenuSeparator());
        mg.add(new MenuItem("save", getResourceString("menuSave"), KeyStroke.getKeyStroke(KeyEvent.VK_S, MENU_SHORTCUT)));
        mg.add(new MenuItem("saveAs", getResourceString("menuSaveAs"), KeyStroke.getKeyStroke(KeyEvent.VK_S, MENU_SHORTCUT + InputEvent.SHIFT_MASK)));
        mg.add(new MenuItem("saveCopyAs", getResourceString("menuSaveCopyAs")));
        if (QuitJMenuItem.isAutomaticallyPresent()) {
            root.getQuitJMenuItem().setAction(root.getQuitAction());
        } else {
            mg.addSeparator();
            mg.add(new MenuItem("quit", root.getQuitAction()));
        }
        add(mg);
        mg = new MenuGroup("edit", getResourceString("menuEdit"));
        mg.add(new MenuItem("undo", getResourceString("menuUndo"), KeyStroke.getKeyStroke(KeyEvent.VK_Z, MENU_SHORTCUT)));
        mg.add(new MenuItem("redo", getResourceString("menuRedo"), KeyStroke.getKeyStroke(KeyEvent.VK_Z, MENU_SHORTCUT + InputEvent.SHIFT_MASK)));
        mg.addSeparator();
        mg.add(new MenuItem("cut", getResourceString("menuCut"), KeyStroke.getKeyStroke(KeyEvent.VK_X, MENU_SHORTCUT)));
        mg.add(new MenuItem("copy", getResourceString("menuCopy"), KeyStroke.getKeyStroke(KeyEvent.VK_C, MENU_SHORTCUT)));
        mg.add(new MenuItem("paste", getResourceString("menuPaste"), KeyStroke.getKeyStroke(KeyEvent.VK_V, MENU_SHORTCUT)));
        mg.add(new MenuItem("clear", getResourceString("menuClear"), KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0)));
        mg.addSeparator();
        mg.add(new MenuItem("selectAll", getResourceString("menuSelectAll"), KeyStroke.getKeyStroke(KeyEvent.VK_A, MENU_SHORTCUT)));
        a = new ActionPreferences(getResourceString("menuPreferences"), KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, MENU_SHORTCUT));
        if (PreferencesJMenuItem.isAutomaticallyPresent()) {
            try {
                final Class appClazz = Class.forName("com.apple.eawt.Application");
                final Method getAppM = appClazz.getMethod("getApplication", null);
                final Object appleApp = getAppM.invoke(null, null);
                final Method addPrefsM = appleApp.getClass().getMethod("addPreferencesMenuItem", null);
                addPrefsM.invoke(appleApp, null);
            } catch (Throwable t) {
            }
            root.getPreferencesJMenuItem().setAction(a);
        } else {
            mg.addSeparator();
            mg.add(new MenuItem("preferences", a));
        }
        add(mg);
        mWindowRadioGroup = new MenuRadioGroup();
        mgWindow = new MenuGroup("window", getResourceString("menuWindow"));
        mgWindow.addSeparator();
        mgWindow.add(new MenuItem("collect", ((BasicWindowHandler) root.getWindowHandler()).getCollectAction()));
        mgWindow.addSeparator();
        add(mgWindow);
        mg = new MenuGroup("help", getResourceString("menuHelp"));
        mg.add(new MenuItem("manual", new URLViewerAction(getResourceString("menuHelpManual"), KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, MENU_SHORTCUT + InputEvent.SHIFT_MASK), "index", false)));
        mg.add(new MenuItem("shortcuts", new URLViewerAction(getResourceString("menuHelpShortcuts"), null, "Shortcuts", false)));
        mg.addSeparator();
        mg.add(new MenuItem("website", new URLViewerAction(getResourceString("menuHelpWebsite"), null, getResourceString("appURL"), true)));
        a = new ActionAbout(getResourceString("menuAbout"), null);
        if (AboutJMenuItem.isAutomaticallyPresent()) {
            root.getAboutJMenuItem().setAction(a);
        } else {
            mg.addSeparator();
            mg.add(new MenuItem("about", a));
        }
        add(mg);
        addMenuItems();
    }

    protected abstract void addMenuItems();

    public abstract void showPreferences();

    protected abstract Action getOpenAction();

    public abstract void openDocument(File f);

    public void addRecent(File path) {
        int i;
        i = openRecentPaths.indexOf(path);
        if (i == 0) return;
        if ((i == -1) && (openRecentPaths.getCapacity() == openRecentPaths.getPathCount())) {
            i = openRecentPaths.getPathCount() - 1;
        }
        if (i > 0) {
            openRecentPaths.remove(path);
            mgRecent.remove(i);
        }
        openRecentPaths.addPathToHead(path);
        actionOpenRecent.setPath(path);
        actionClearRecent.setEnabled(true);
        mgRecent.add(new MenuItem(String.valueOf(uniqueNumber++), createOpenRecentAction(null, path)), 0);
    }

    public String getResourceString(String key) {
        return root.getResourceString(key);
    }

    public void addToWindowMenu(Action a) {
        final String id = "window" + String.valueOf(uniqueNumber++);
        mgWindow.add(new MenuRadioItem(mWindowRadioGroup, id, a));
    }

    public void removeFromWindowMenu(Action a) {
        final MenuRadioItem mri = (MenuRadioItem) mgWindow.getByAction(a);
        mgWindow.remove(mri);
    }

    public void setSelectedWindow(Action a) {
        final MenuRadioItem mri = (MenuRadioItem) mgWindow.getByAction(a);
        mri.setSelected(true);
    }

    public void documentAdded(DocumentEvent e) {
        if (!actionCloseAll.isEnabled()) actionCloseAll.setEnabled(true);
    }

    public void documentRemoved(DocumentEvent e) {
        if (root.getDocumentHandler().getDocumentCount() == 0) {
            actionCloseAll.setEnabled(false);
        }
    }

    public void documentFocussed(de.sciss.app.DocumentEvent e) {
    }

    protected static String abbrName(String name) {
        final int len = name.length();
        if (len <= 25) return name;
        final int idxLeft = name.lastIndexOf('[');
        if (idxLeft == -1) return name;
        final int idxRight = name.indexOf(']', idxLeft + 1);
        if ((idxRight - idxLeft) < 25) return name;
        return name.substring(0, idxLeft + 12) + "…" + name.substring(idxRight - 12);
    }

    protected class ActionOpenRecent extends MenuAction {

        private File path;

        public ActionOpenRecent(String text, File path) {
            super(text == null ? IOUtil.abbreviate(new File(path.getParentFile(), abbrName(path.getName())).getAbsolutePath(), 40) : text);
            setPath(path);
        }

        protected void setPath(File path) {
            this.path = path;
            setEnabled((path != null) && path.isFile());
        }

        /**
		 *  If a path was set for the
		 *  action and the user confirms
		 *  an intermitting confirm-unsaved-changes
		 *  dialog, the new session will be loaded
		 */
        public void actionPerformed(ActionEvent e) {
            openDocument(path);
        }
    }

    private class ActionClearRecent extends MenuAction {

        protected ActionClearRecent(String text, KeyStroke shortcut) {
            super(text, shortcut);
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            for (int i = openRecentPaths.getPathCount() - 1; i >= 0; i--) {
                mgRecent.remove(i);
            }
            openRecentPaths.clear();
            actionOpenRecent.setPath(null);
            setEnabled(false);
        }
    }

    private class ActionCloseAll extends MenuAction implements ProcessingThread.Listener {

        protected ActionCloseAll(String text, KeyStroke shortcut) {
            super(text, shortcut);
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            perform();
        }

        private void perform() {
            final ProcessingThread pt = closeAll(false, new Flag(false));
            if (pt != null) {
                pt.addListener(this);
                ((BasicDocument) pt.getClientArg("doc")).start(pt);
            }
        }

        public void processStarted(ProcessingThread.Event e) {
        }

        public void processStopped(ProcessingThread.Event e) {
            if (e.isDone()) {
                perform();
            }
        }
    }

    /**
	 *  Action to be attached to
	 *  the Preference item of the Edit menu.
	 *  Will bring up the Preferences frame
	 *  when the action is performed.
	 */
    public class ActionPreferences extends MenuAction {

        protected ActionPreferences(String text, KeyStroke shortcut) {
            super(text, shortcut);
        }

        public void actionPerformed(ActionEvent e) {
            perform();
        }

        /**
		 *  Opens the preferences frame
		 */
        public void perform() {
            showPreferences();
        }
    }

    private class ActionAbout extends MenuAction {

        protected ActionAbout(String text, KeyStroke shortcut) {
            super(text, shortcut);
        }

        /**
		 *  Brings up the About-Box
		 */
        public void actionPerformed(ActionEvent e) {
            JFrame aboutBox = (JFrame) getApplication().getComponent(AboutBox.COMP_ABOUTBOX);
            if (aboutBox == null) {
                aboutBox = new AboutBox();
            }
            aboutBox.setVisible(true);
            aboutBox.toFront();
        }
    }

    public class ActionShowWindow extends MenuAction {

        private final Object component;

        public ActionShowWindow(String text, KeyStroke shortcut, Object component) {
            super(text, shortcut);
            this.component = component;
        }

        /**
		 *  Tries to find the component using
		 *  the <code>Main</code> class' <code>getComponent</code>
		 *  method. It does not instantiate a
		 *  new object if the component is not found.
		 *  If the window is already open, this
		 *  method will bring it to the front.
		 */
        public void actionPerformed(ActionEvent e) {
            AbstractWindow w = (AbstractWindow) getApplication().getComponent(component);
            if (w != null) {
                w.setVisible(true);
                w.toFront();
            }
        }
    }
}
