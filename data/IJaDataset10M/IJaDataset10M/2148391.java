package graphlab.ui.xml;

import graphlab.library.util.Pair;
import graphlab.main.core.BlackBoard.BlackBoard;
import graphlab.main.core.Configuration;
import graphlab.main.core.action.AbstractAction;
import graphlab.ui.AbstractExtensionAction;
import graphlab.ui.UI;
import static graphlab.ui.actions.UIEventHandler.CONF;
import graphlab.ui.actions.UIEventData;
import graphlab.ui.components.*;
import graphlab.ui.components.gmenu.GMenuBar;
import graphlab.ui.components.gmenu.GMenuItem;
import graphlab.ui.components.gmenu.KeyBoardShortCut;
import graphlab.ui.components.gmenu.KeyBoardShortCutProvider;
import graphlab.ui.components.gsidebar.GSidebar;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;

public class UIHandlerImpl implements UIHandler {

    public static final boolean DEBUG = false;

    public GToolbar toolbar;

    public BlackBoard blackboard;

    public Configuration conf;

    Class resourceClass;

    HashMap<String, AbstractAction> actions = null;

    /**
     * determines the character which if put before a character in the string of the label, that character will be set to it's mnemonics
     */
    final char menueIndexChar = '_';

    public UIHandlerImpl(GFrame gFrame, BlackBoard bb, HashMap<String, AbstractAction> actions, Class resClass) {
        this.blackboard = bb;
        this.toolbar = gFrame.getToolbar();
        this.sidebar = gFrame.getSidebar();
        this.statusbar = gFrame.getStatusbar();
        this.conf = (Configuration) blackboard.get(CONF);
        this.menubar = gFrame.getMenu();
        this.frame = gFrame;
        this.actions = actions;
        this.resourceClass = resClass;
    }

    private JToolBar lastToolbar = new JToolBar();

    private int lastToolbarPlace;

    public void start_toolbar(final Attributes meta) throws SAXException {
        lastToolbar = toolbar.createToolBar();
        lastToolbarPlace = extractPlace(meta);
        if (DEBUG) System.err.println("start_toolbar: " + meta);
    }

    public void handle_tool(final Attributes meta) throws SAXException {
        String label = meta.getValue("label");
        String icon = meta.getValue("image");
        String action = meta.getValue("action");
        String _place = meta.getValue("place");
        int place = -1;
        if (_place != null) place = Integer.parseInt(_place);
        GButton b;
        if (resourceClass != null) System.out.println("[handle_tool]" + icon + " : " + resourceClass.getResource(icon));
        if (icon == null || icon.equals("") || resourceClass == null || resourceClass.getResource(icon) == null) b = new GButton(label, icon, blackboard, action); else b = new GButton(label, resourceClass.getResource(icon), blackboard, action);
        b.setBorder(new EmptyBorder(0, 1, 0, 2));
        lastToolbar.add(b);
        if (DEBUG) System.err.println("handle_tool: " + meta);
    }

    public void end_toolbar() throws SAXException {
        lastToolbar.add(new JSeparator(JSeparator.VERTICAL));
        toolbar.addIndexed(lastToolbar, lastToolbarPlace);
        if (DEBUG) System.err.println("end_toolbar()");
    }

    public void start_toolbars(Attributes meta) throws SAXException {
    }

    public void end_toolbars() throws SAXException {
    }

    public GStatusBar statusbar;

    public void handle_bar(Attributes meta) throws SAXException {
        String clazz = meta.getValue("class");
        String id = meta.getValue("id");
        System.out.println("Adding the Bar with id:" + id + " ,class:" + clazz);
        Component _ = getComponent(clazz);
        UI.setComponent(blackboard, id, _);
        statusbar.addComponent(_);
    }

    public GMenuBar menubar;

    private GFrame frame;

    private JMenu currentMenu;

    public void start_menues(Attributes meta) throws SAXException {
    }

    static HashMap<JMenuItem, Integer> places = new HashMap<JMenuItem, Integer>();

    private Pair<Integer, String> extractLabelInfo(String label) {
        int index = Math.max(label.indexOf(menueIndexChar), 0);
        label = label.replace(menueIndexChar + "", "");
        return new Pair<Integer, String>(index, label);
    }

    int lastMenuPlace;

    public void start_submenu(final Attributes meta) throws SAXException {
        String label = meta.getValue("label");
        String accel = meta.getValue("accelerator");
        Pair<Integer, String> lInfo = extractLabelInfo(label);
        lastMenuPlace = extractPlace(meta);
        int index = lInfo.first;
        label = lInfo.second;
        currentMenu = menubar.getUniqueMenu(label, lastMenuPlace);
        KeyBoardShortCut shortcut = KeyBoardShortCutProvider.registerKeyBoardShortcut(accel, label, index);
        if (shortcut != null) {
            if (!shortcut.isAccelerator()) {
                currentMenu.setMnemonic(shortcut.getKeyMnemonic());
                currentMenu.setDisplayedMnemonicIndex(shortcut.getKeyWordIndex());
            }
        }
        if (DEBUG) System.err.println("start_submenu: " + meta);
    }

    public void handle_menu(final Attributes meta) throws SAXException {
        String label = meta.getValue("label");
        String action = meta.getValue("action");
        String accel = meta.getValue("accelerator");
        int place = extractPlace(meta);
        Pair<Integer, String> lInfo = extractLabelInfo(label);
        int index = lInfo.first;
        label = lInfo.second;
        AbstractAction targetAction = actions.get(action);
        if (targetAction instanceof AbstractExtensionAction) {
            AbstractExtensionAction targetExt = (AbstractExtensionAction) targetAction;
            targetExt.removeCreatedUIComponents();
            targetExt.setEvent(UI.getUIEvent(action));
        }
        GMenuItem item = new GMenuItem(label, action, blackboard, accel, index);
        menubar.insert(currentMenu, item, place);
        if (DEBUG) System.err.println("handle_menu: " + meta);
    }

    private int extractPlace(Attributes meta) {
        String _place = meta.getValue("place");
        int place = -1;
        try {
            if (_place != null) place = Integer.parseInt(_place);
        } catch (NumberFormatException e) {
            System.err.println("the place given for menu " + meta.getValue("label") + " is not a valid number:" + _place);
        }
        return place;
    }

    public void end_submenu() throws SAXException {
        if (DEBUG) System.err.println("end_submenu()");
    }

    public void end_menues() throws SAXException {
    }

    public void handle_action(Attributes meta) throws SAXException {
        String clazz = meta.getValue("class");
        String id = meta.getValue("id");
        String group = meta.getValue("group");
        System.out.println("  Adding action " + clazz + " (" + id + "," + group + ") ...");
        Class<?> clazzz = null;
        try {
            clazzz = Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            System.err.println("the given class name can't be loaded: " + clazz);
            e.printStackTrace();
            return;
        }
        boolean b = false;
        AbstractAction x = null;
        for (ExtensionHandler s : registeredExtensionHandlers) {
            x = s.Handle(blackboard, clazzz);
            b = b | x != null;
            if (x != null) {
                if (id == null) {
                    id = x.listeningEvents.iterator().next().getName();
                    id = id.replaceFirst(UIEventData.name(""), "");
                }
                addAction(id, x, group);
            }
        }
        if (b) {
            return;
        } else {
            x = loadAbstractAction(clazz);
        }
        if (x == null) {
            System.err.println("Error while loading " + clazz + ". skiped.");
            return;
        }
        addAction(id, x, group);
    }

    private void addAction(String id, AbstractAction x, String group) {
        if ((id != null) && !id.equals("")) actions.put(id, x);
        if (group != null && !group.equals("")) {
            conf.addToGroup(group, x);
        }
    }

    public GSidebar sidebar;

    public void start_sidebar(Attributes meta) throws SAXException {
    }

    public void handle_sidebar(Attributes meta) throws SAXException {
        String image = meta.getValue("image") + "";
        String clazz = meta.getValue("class");
        String id = meta.getValue("id");
        Component component = getComponent(clazz);
        UI.setComponent(blackboard, id, component);
        if (resourceClass == null || resourceClass.getResource(image) == null) {
            sidebar.addButton(image, component);
        } else sidebar.addButton(resourceClass.getResource(image), component);
    }

    public void end_sidebar() throws SAXException {
    }

    public void handle_body(Attributes meta) throws SAXException {
        String clazz = meta.getValue("class");
        Component gci = getComponent(clazz);
        frame.getBody().setBodyPane(gci);
    }

    AbstractAction loadAbstractAction(String abstractActionclazz) {
        String clazz = abstractActionclazz;
        if (!(clazz == null) && !(clazz.equals(""))) {
            Class t = clazz2Class(clazz);
            if (AbstractAction.class.isAssignableFrom(t)) {
                Object[] o = { blackboard };
                try {
                    Constructor c = t.getConstructor(BlackBoard.class);
                    Object _ = c.newInstance(o);
                    return (AbstractAction) _;
                } catch (Exception e) {
                    System.err.println("Error while loading " + clazz);
                    e.printStackTrace();
                }
            }
        }
        System.err.println("Error while loading " + clazz);
        return null;
    }

    Component getComponent(String GComponentInterfaceClassName) {
        String clazz = GComponentInterfaceClassName;
        if (!(clazz == null) && !(clazz.equals(""))) {
            Class t = clazz2Class(clazz);
            Constructor c = null;
            Object[] o = { blackboard };
            try {
                c = t.getConstructor(BlackBoard.class);
            } catch (NoSuchMethodException e) {
                try {
                    c = t.getConstructor(new Class[] {});
                    o = new Object[] {};
                } catch (NoSuchMethodException e1) {
                    System.err.println("the clazz " + clazz + "does not have a constructor(BlackBoard) or constructor(), how can i load it?");
                    e1.printStackTrace();
                }
            }
            try {
                Object _ = c.newInstance(o);
                if (_ instanceof GComponentInterface) {
                    return ((GComponentInterface) _).getComponent(blackboard);
                } else {
                    System.err.println("the class " + clazz + " doesn't implement the interface GComponentInterface, so it can't be put on the UI.");
                }
            } catch (InstantiationException e) {
                System.err.println("There was an error while initializing the class" + clazz + "may be in it's constructor or in one of classes it instantiate in its constructor");
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                System.err.println("There was an error while initializing the class" + clazz + "may be in it's constructor or in one of classes it instantiate in its constructor");
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                System.err.println("There was an error while initializing the class" + clazz + "may be in it's constructor or in one of classes it instantiate in its constructor");
                e.getTargetException().printStackTrace();
            }
        }
        return null;
    }

    private Class clazz2Class(String clazz) {
        Class t = null;
        try {
            t = Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            System.err.println("the Class" + clazz + "didn't found in class path");
            e.printStackTrace();
        }
        return t;
    }

    private static HashSet<ExtensionHandler> registeredExtensionHandlers = new HashSet<ExtensionHandler>();

    public static void registerExtensionHandler(ExtensionHandler sph) {
        registeredExtensionHandlers.add(sph);
    }
}
