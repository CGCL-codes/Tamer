package org.apptools;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.prefs.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apptools.actions.*;
import org.apptools.console.*;
import org.apptools.data.*;
import org.apptools.plugin.*;
import org.apptools.prefs.EntryDescriptor;
import org.apptools.prefs.FileEntryDescriptor;
import org.apptools.prefs.PreferencesEntryTree;
import org.apptools.prefs.PreferencesEntryTree.Node;
import org.apptools.task.Task;
import org.apptools.task.TaskListener;
import org.apptools.ui.PlatformUI;

/**
 * A default implementation of the Platform interface. This platform runs atop of a 
 * CoreEngine, which takes care of plug-in management and Preferences. 
 *    
 * The Platform supports a single PlatformUI.
 *    
 * @author Johan Stenberg
 */
public class DefaultPlatform implements Platform, PreferenceChangeListener, PlugInManagerListener {

    static Log log = LogFactory.getLog(DefaultPlatform.class);

    static final String PREF_HOME_DIR = "HOME_DIR";

    static final String PREF_AUTO_ACTIVATE_FILE = "AUTO_ACTIVATE_FILE";

    /**The platform's home directory entry descriptor*/
    public static final EntryDescriptor HOME_DIR = new FileEntryDescriptor(PREF_HOME_DIR, PlatformTexts.getString("Platform.PREF_NAME_HOME_DIR"), PlatformTexts.getString("Platform.PREF_DESC_HOME_DIR"), "", EntryDescriptor.USER, true);

    /**The plug-in autoactivate file entry descriptor*/
    public static final EntryDescriptor AUTO_FILE = new FileEntryDescriptor(PREF_AUTO_ACTIVATE_FILE, PlatformTexts.getString("Platform.PREF_NAME_PLUGIN_FILE"), PlatformTexts.getString("Platform.PREF_DESC_PLUGIN_FILE"), "plugins/autoact.txt", EntryDescriptor.USER, false);

    /**The preference entry descriptors*/
    public static final EntryDescriptor[] PREF_ENTRIES = new EntryDescriptor[] { AUTO_FILE, HOME_DIR };

    /**The preferences entries model*/
    protected PreferencesEntryTree entryTree;

    /**The platform that this workbench is running on*/
    public CoreEngine coreEngine;

    /**A map of plugin class->lists of quick action groups*/
    protected Map quickActionSets = new WeakHashMap();

    /**A List of all registered action sets*/
    protected List actionGroups = new ArrayList();

    /**A map of actionSets->encapsulating plugins*/
    protected Map actionEncapsulations = new WeakHashMap();

    protected Map quickActionEncapsulations = new WeakHashMap();

    /**The plug-in autoactivate file*/
    protected File autoActivateFile;

    /**The homePath*/
    protected File homePath;

    /**List of listeners notified when action groups are added/removed*/
    protected List actionGroupListeners = new ArrayList();

    /**List of listeners notified when active plug-ins are activated/deactivated*/
    protected List activePlugInListeners = new ArrayList();

    /**List of listeners notified when Tasks are about to run/have stopped running*/
    protected List taskListeners = new ArrayList();

    /**The current user interface for this platform*/
    protected PlatformUI ui;

    /**A set of commands matching the actions for this platform*/
    protected CommandSet commands;

    /**The Interpreter of commands for this platform*/
    protected ActionInterpreter interpreter;

    /**The command parser used to parse command lines into commands*/
    protected CommandParser parser;

    /**Create a platform on top of the specified coreengine*/
    public DefaultPlatform(CoreEngine core) {
        coreEngine = core;
        interpreter = new ActionInterpreter();
        commands = new CommandSet();
        parser = new CommandParser(commands, interpreter);
        this.entryTree = new PreferencesEntryTree(this.coreEngine.getUserPreferences().name());
        this.entryTree.getRootNode().addEntries(this.coreEngine.getPreferencesEntries());
        this.addPreferenceEntries(PREF_ENTRIES, "", this);
        setHomeDirectory(coreEngine.getUserPreferences().get(HOME_DIR.getKey(), HOME_DIR.getDefault()));
        setAutoActivateFile(coreEngine.getUserPreferences().get(AUTO_FILE.getKey(), AUTO_FILE.getDefault()));
        log.info(HOME_DIR.getKey() + " = " + homePath);
        log.info(AUTO_FILE.getKey() + " = " + autoActivateFile);
        getPlugInManager().addListener(this);
        setupActions();
    }

    /**Setup the actions intrinsic to the platform*/
    protected void setupActions() {
        String quitCaption = PlatformTexts.getString("Platform.CAPTION_QUIT");
        String quitTooltip = PlatformTexts.getString("Platform.TOOLTIP_QUIT");
        UserAction quit = new AbstractUserAction(quitCaption, quitTooltip) {

            public void execute() {
                close();
            }
        };
        String activatePluginsCaption = PlatformTexts.getString("Platform.CAPTION_ACT_PLUGIN");
        String activatePluginsTooltip = PlatformTexts.getString("Platform.TOOLTIP_ACT_PLUGIN");
        UserAction activatePlugins = new AbstractUserAction(activatePluginsCaption, activatePluginsTooltip) {

            public void execute() {
                activatePlugins();
            }
        };
        String deactivatePluginsCaption = PlatformTexts.getString("Platform.CAPTION_DEACT_PLUGIN");
        String deactivatePluginsTooltip = PlatformTexts.getString("Platform.TOOLTIP_DEACT_PLUGIN");
        UserAction deactivatePlugins = new AbstractUserAction(deactivatePluginsCaption, deactivatePluginsTooltip) {

            public void execute() {
                deActivatePlugins();
            }
        };
        UserAction exitConsole = new AbstractUserAction(PlatformTexts.getString("Platform.CAPTION_EXIT_UI"), PlatformTexts.getString("Platform.TOOLTIP_EXIT_UI")) {

            public void execute() {
                getUI().exit();
            }
        };
        UserAction listCommands = new AbstractUserAction(PlatformTexts.getString("Platform.CAPTION_LIST_COMMANDS"), PlatformTexts.getString("Platform.TOOLTIP_LIST_COMMANDS")) {

            public void execute() {
                getUI().sendMessage(CommandParser.getCommandList(commands.getCommands(), 0), PlatformTexts.getString("Platform.TITLE_LIST_COMMANDS"), PlatformUI.NO_QUERY);
            }
        };
        addActionSet(new ActionSet(new UserAction[] { exitConsole, listCommands }, null, null));
        addActionSet(new ActionSet(new UserAction[] { quit }, PlatformTexts.getString("Platform.MENU_FILE"), null, -1));
        addActionSet(new ActionSet(new UserAction[] { activatePlugins, deactivatePlugins }, PlatformTexts.getString("Platform.MENU_OPTIONS"), null, 900));
    }

    public PlatformUI getUI() {
        return ui;
    }

    public boolean setUI(PlatformUI newUI) {
        try {
            log.debug("setUI called with " + (newUI == null ? "null" : newUI.getClass().getName()));
            if (ui != null) ui.exit();
            ui = newUI;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public void addPreferenceEntries(EntryDescriptor[] entries, String nodePath, PreferenceChangeListener listener) {
        String path = nodePath.toLowerCase();
        Node n = path.equals("") ? entryTree.getRootNode() : this.entryTree.getNode(path);
        if (n == null) n = this.entryTree.createNode(path);
        n.addEntries(entries);
        Preferences uprefs = coreEngine.getUserPreferences();
        Preferences uNode = null;
        uNode = uprefs.node(path);
        if (listener != null) {
            uNode.addPreferenceChangeListener(listener);
        }
        for (int i = 0; i < entries.length; i++) {
            EntryDescriptor e = entries[i];
            String key = e.getKey();
            String defValue = e.getDefault();
            switch(e.getType()) {
                case EntryDescriptor.USER:
                    try {
                        if (!Arrays.asList(uNode.keys()).contains(key)) {
                            uNode.put(key, defValue);
                            uNode.flush();
                        } else if (listener != null) {
                            listener.preferenceChange(new PreferenceChangeEvent(uNode, key, uNode.get(key, defValue)));
                        }
                    } catch (BackingStoreException e1) {
                        uNode.put(key, defValue);
                    }
                    break;
            }
        }
    }

    public void removePreferenceEntries(EntryDescriptor[] entries, String nodePath, PreferenceChangeListener listener) {
        if (listener != null) {
            String path = nodePath.toLowerCase();
            Preferences uprefs = this.coreEngine.getUserPreferences();
            Preferences uNode = uprefs.node(path);
            uNode.removePreferenceChangeListener(listener);
        }
        Node n = this.entryTree.getNode(nodePath);
        if (n != null) {
            n.removeEntries(entries);
            if (n.getEntries().length == 0 && !n.hasChildren()) {
                this.entryTree.removeNode(n);
            }
        }
    }

    public void preferenceChange(PreferenceChangeEvent evt) {
        if (evt.getKey().equals(HOME_DIR.getKey()) && evt.getNode().isUserNode()) setHomeDirectory(evt.getNewValue()); else if (evt.getKey().equals(AUTO_FILE.getKey()) && evt.getNode().isUserNode()) setAutoActivateFile(evt.getNewValue());
    }

    public Preferences getUserPreferences() {
        return coreEngine.getUserPreferences();
    }

    public PreferencesEntryTree getPreferencesEntries() {
        return entryTree;
    }

    public void deactivatePlugIn(ActivePlugIn plugIn) {
        try {
            firePlugInClosing(plugIn);
            coreEngine.deactivatePlugIn(plugIn);
        } catch (Throwable e) {
            handleError(e.toString(), e);
        }
    }

    public ActivePlugIn activatePlugIn(Class plugInClass) {
        try {
            List apis = coreEngine.activatePlugIn(plugInClass);
            if (apis != null) {
                ActivePlugIn api = null;
                for (Iterator i = apis.iterator(); i.hasNext(); ) {
                    api = (ActivePlugIn) i.next();
                    api.setup(this);
                    firePlugInOpened(api);
                }
                return api;
            }
            return null;
        } catch (Throwable e) {
            handleError(e.toString(), e);
            return null;
        }
    }

    public Collection getOpenActivePlugIns() {
        return coreEngine.getActivePlugIns();
    }

    public PlugInManager getPlugInManager() {
        return coreEngine.getPlugInManager();
    }

    public void addActivePlugInListener(ActivePlugInListener listener) {
        activePlugInListeners.add(listener);
    }

    public void removeActivePlugInListener(ActivePlugInListener listener) {
        activePlugInListeners.remove(listener);
    }

    void firePlugInOpened(ActivePlugIn pi) {
        for (Iterator i = activePlugInListeners.iterator(); i.hasNext(); ) ((ActivePlugInListener) i.next()).plugInActivated(this, pi);
    }

    void firePlugInClosing(ActivePlugIn pi) {
        for (Iterator i = activePlugInListeners.iterator(); i.hasNext(); ) ((ActivePlugInListener) i.next()).plugInDeactiviating(this, pi);
    }

    public void autoActivatePlugIns() {
        if (autoActivateFile != null) {
            try {
                BufferedReader r = new BufferedReader(new FileReader(autoActivateFile));
                String line = "";
                while ((line = r.readLine()) != null) {
                    if (!line.startsWith("#")) {
                        try {
                            List l = new ArrayList(coreEngine.getPlugInManager().getPlugInList(CoreEngine.ACTIVE_PLUGIN));
                            boolean loaded = false;
                            for (Iterator i = l.iterator(); i.hasNext(); ) {
                                Class c = (Class) i.next();
                                if (c.getName().equals(line)) {
                                    activatePlugIn(c);
                                    loaded = true;
                                    break;
                                }
                            }
                            if (!loaded) {
                                throw new PlugInException(PlatformTexts.getString("Platform.ERROR_PLUGIN_NOT_LOADED"));
                            }
                        } catch (PlugInException e) {
                            String message = MessageFormat.format(PlatformTexts.getString("Platform.ERROR_PLUGIN_NOT_ACTIVATED"), new Object[] { line });
                            handleError(message, e);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                String message = MessageFormat.format(PlatformTexts.getString("Platform.ERROR_PLUGIN_FILE_NOT_FOUND"), new Object[] { autoActivateFile.getAbsolutePath() });
                handleError(message, e);
            } catch (IOException e) {
                String message = MessageFormat.format(PlatformTexts.getString("Platform.ERROR_PLUGIN_FILE_NOT_READ"), new Object[] { autoActivateFile.getAbsolutePath() });
                handleError(message, e);
            }
        } else {
            log.warn("No auto-activate file set");
        }
    }

    public ActivePlugIn getPlugInInstance(Class plugInClass) {
        log.debug("Getting instance of " + plugInClass);
        ActivePlugIn api = coreEngine.getPlugInInstance(plugInClass);
        return api;
    }

    /**Get the plug-in autoload file for the platform*/
    public File getPlugInAutoLoadFile() {
        return coreEngine.getPlugInFile();
    }

    /**Get the plug-in path for the platform*/
    File getPlugInPath() {
        return coreEngine.getPlugInPath();
    }

    /**Get the auto-activate file for the platform*/
    public File getAutoActivateFile() {
        return autoActivateFile;
    }

    public void addQuickActionSet(ActionSet actionSet) {
        ActivePlugIn pi = actionSet.getPlugin();
        Object o = quickActionSets.get(pi);
        List l;
        if (o != null) {
            l = (List) o;
        } else {
            l = new ArrayList();
            quickActionSets.put(pi, l);
        }
        l.add(actionSet);
    }

    public void removeQuickActionSets(ActivePlugIn pi) {
        List l = (List) quickActionSets.get(pi);
        if (l != null) {
            l.clear();
        }
    }

    public List getQuickActionSets(ActivePlugIn pi) {
        List l;
        List ol = (List) quickActionSets.get(pi);
        if (ol == null) l = new ArrayList(); else l = new ArrayList(ol);
        for (Iterator i = quickActionEncapsulations.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            if (e.getValue().equals(pi)) l.add(e.getKey());
        }
        Collections.sort(l, new Comparator() {

            public int compare(Object o1, Object o2) {
                int fo = ((ActionSet) o1).getOrder();
                int so = ((ActionSet) o2).getOrder();
                return fo - so;
            }
        });
        return l;
    }

    /**Override to take action when the home directory is 
   * set through the preferences.*/
    protected void setHomeDirectory(String fileName) {
        homePath = fileName.equals("") ? null : new File(fileName);
    }

    public File getHomeDirectory() {
        return homePath;
    }

    protected void setAutoActivateFile(String fileName) {
        autoActivateFile = fileName.equals("") ? null : new File(fileName);
    }

    public void plugInsChanged(PlugInManager pim) {
    }

    public void plugInTypesChanged(PlugInManager pim) {
        try {
            pim.loadPlugIns(coreEngine.getPreferredPlugInLoader(false), false);
        } catch (PlugInException e) {
            handleError(e.toString(), e);
        }
    }

    /**Exit the JVM*/
    public void close() {
        switch(getUI().sendMessage(PlatformTexts.getString("Platform.QUERY_EXIT_PROCEED"), PlatformTexts.getString("Platform.TITLE_EXIT_PROCEED"), PlatformUI.YES_NO_QUERY)) {
            case PlatformUI.YES_OPTION:
                break;
            case PlatformUI.NO_OPTION:
                return;
        }
        if (ui != null) ui.exit();
        System.exit(0);
    }

    public void shutDown() {
        System.exit(0);
    }

    /**Allow the user to activate a plugin*/
    protected void activatePlugins() {
        Collection open = getOpenActivePlugIns();
        List all;
        try {
            all = new ArrayList(getPlugInManager().getPlugInList(CoreEngine.ACTIVE_PLUGIN));
        } catch (PlugInException e1) {
            handleError(e1.toString(), e1);
            return;
        }
        for (Iterator i = open.iterator(); i.hasNext(); ) {
            Object o = i.next();
            all.remove(o.getClass());
        }
        DataDescriptor plugin = new PlugInClassListSelectionDescriptor(PlatformTexts.getString("Platform.MESSAGE_SELECT_PLUGIN_ACT"), null, "PLUGIN", all, true);
        DataDescriptor auto = new BooleanDescriptor(PlatformTexts.getString("Platform.MESSAGE_AUTO_ACTIVATE"), null, "AUTO");
        DataHolder dh = new DefaultDataHolder(new DataDescriptor[] { plugin, auto });
        boolean ok = getUI().inputData(dh, PlatformTexts.getString("Platform.TITLE_ACT_PLUGIN"));
        if (!ok) return;
        Class pic = (Class) dh.getData("PLUGIN");
        if (pic == null) return;
        boolean autoActivate = ((Boolean) dh.getData("AUTO")).booleanValue();
        activatePlugIn(pic);
        if (autoActivate) {
            File f = getAutoActivateFile();
            if (f != null) {
                try {
                    FileWriter fw = new FileWriter(f, true);
                    new PrintWriter(fw).println(pic.getName());
                    fw.close();
                } catch (IOException e) {
                    handleError(PlatformTexts.getString("Platform.ERROR_PLUGIN_FILE_NOT_ACCESSIBLE") + e.toString(), e);
                }
            }
        }
    }

    /**Allow the user to deactivate a plugin*/
    protected void deActivatePlugins() {
        List open = new ArrayList(getOpenActivePlugIns());
        open.remove(getUI());
        DataDescriptor dd = new PlugInListSelectionDescriptor(PlatformTexts.getString("Platform.MESSAGE_SELECT_PLUGIN_DEACT"), null, "PLUGIN", open, true);
        DataHolder dh = new DefaultDataHolder(new DataDescriptor[] { dd });
        boolean ok = getUI().inputData(dh, PlatformTexts.getString("Platform.TITLE_DEACT_PLUGIN"));
        if (!ok) return;
        ActivePlugIn ap = (ActivePlugIn) dh.getData("PLUGIN");
        if (ap != null) deactivatePlugIn(ap);
    }

    public PlugInLoader getPreferredPlugInLoader(boolean overrideNameFile) {
        return coreEngine.getPreferredPlugInLoader(overrideNameFile);
    }

    public void sendCommand(String command, PrintStream out) {
        parser.parseCommand(command, out == null ? System.out : out);
    }

    public List getAllActionSets() {
        return Collections.unmodifiableList(actionGroups);
    }

    public void encapsulateActions(ActivePlugIn encapsulator, ActivePlugIn encapsulatee) {
        List l = (List) quickActionSets.get(encapsulatee);
        if (l != null) {
            for (Iterator i = l.iterator(); i.hasNext(); ) {
                ActionSet s = (ActionSet) i.next();
                quickActionEncapsulations.put(s, encapsulator);
            }
        }
        l = getActionSets(encapsulatee);
        if (l != null) {
            for (Iterator i = l.iterator(); i.hasNext(); ) {
                ActionSet s = (ActionSet) i.next();
                actionEncapsulations.put(s, encapsulator);
            }
        }
    }

    public void deencapsulateActions(ActivePlugIn encapsulator, ActivePlugIn encapsulatee) {
        List l = (List) quickActionSets.get(encapsulatee);
        if (l != null) {
            for (Iterator i = l.iterator(); i.hasNext(); ) {
                ActionSet s = (ActionSet) i.next();
                if (encapsulator.equals(quickActionEncapsulations.get(s))) quickActionEncapsulations.remove(s);
            }
        }
        l = getActionSets(encapsulatee);
        if (l != null) {
            for (Iterator i = l.iterator(); i.hasNext(); ) {
                ActionSet s = (ActionSet) i.next();
                actionEncapsulations.remove(s);
            }
        }
    }

    public List getActionSets(ActivePlugIn api) {
        List l = new ArrayList();
        for (Iterator i = actionGroups.iterator(); i.hasNext(); ) {
            ActionSet as = (ActionSet) i.next();
            ActivePlugIn owner = as.getPlugin();
            ActivePlugIn encap = (ActivePlugIn) actionEncapsulations.get(as);
            if (encap != null) {
                owner = encap;
            }
            if (api.equals(owner)) l.add(as);
        }
        Collections.sort(l, new Comparator() {

            public int compare(Object o1, Object o2) {
                int fo = ((ActionSet) o1).getOrder();
                int so = ((ActionSet) o2).getOrder();
                return fo - so;
            }
        });
        return l;
    }

    protected void addCommands(String groupID, UserAction[] groupActions) {
        if (groupID == null) {
            for (int i = 0; i < groupActions.length; i++) {
                UserAction a = groupActions[i];
                Object nameO = a.getValue(UserAction.KEY_CAPTION);
                Object descO = a.getValue(UserAction.KEY_TOOL_TIP);
                String name = nameO == null ? PlatformTexts.getString("Platform.ACTION_NAME_UNKNOWN") : nameO.toString();
                String desc = descO == null ? PlatformTexts.getString("Platform.ACTION_TOOLTIP_UNKNOWN") : descO.toString();
                Command command = new DefaultCommand(name, desc);
                commands.addCommand(command);
                interpreter.putCommand(new ActionInterpreter.CommandKey(command, null), a);
            }
        } else if (groupID.indexOf('/') > 0) {
            Command topcommand;
            Command.Option subcommand;
            String[] ids = groupID.split("\\/");
            Command[] topcomms = CommandSet.getCommands(commands.getCommands(), ids[0]);
            if (topcomms.length == 0) {
                topcommand = new DefaultCommand(ids[0], ids[0] + " menu");
                commands.addCommand(topcommand);
            } else {
                topcommand = topcomms[0];
            }
            subcommand = topcommand.getOption(ids[1]);
            if (subcommand == null) {
                subcommand = new DefaultCommand.Option(ids[1], ids[1] + " submenu", true);
                topcommand.addOption(subcommand);
            }
            for (int i = 0; i < groupActions.length; i++) {
                UserAction a = groupActions[i];
                Object nameO = a.getValue(UserAction.KEY_CAPTION);
                Object descO = a.getValue(UserAction.KEY_TOOL_TIP);
                String name = nameO == null ? PlatformTexts.getString("Platform.ACTION_NAME_UNKNOWN") : nameO.toString();
                String desc = descO == null ? PlatformTexts.getString("Platform.ACTION_TOOLTIP_UNKNOWN") : descO.toString();
                Command.Option option = new DefaultCommand.Option(name, desc, true);
                subcommand.addOption(option);
                interpreter.putCommand(new ActionInterpreter.CommandKey(topcommand, subcommand, option), a);
            }
        } else {
            Command command;
            Command[] comms = CommandSet.getCommands(commands.getCommands(), groupID);
            if (comms.length == 0) {
                command = new DefaultCommand(groupID, groupID + " menu");
                commands.addCommand(command);
            } else {
                command = comms[0];
            }
            for (int i = 0; i < groupActions.length; i++) {
                UserAction a = groupActions[i];
                Object nameO = a.getValue(UserAction.KEY_CAPTION);
                Object descO = a.getValue(UserAction.KEY_TOOL_TIP);
                String name = nameO == null ? PlatformTexts.getString("Platform.ACTION_NAME_UNKNOWN") : nameO.toString();
                String desc = descO == null ? PlatformTexts.getString("Platform.ACTION_TOOLTIP_UNKNOWN") : descO.toString();
                Command.Option option = new DefaultCommand.Option(name, desc, true);
                command.addOption(option);
                interpreter.putCommand(new ActionInterpreter.CommandKey(command, option), a);
            }
        }
    }

    public void addActionSet(ActionSet actionSet) {
        addCommands(actionSet.getGroupID(), actionSet.getActions());
        actionGroups.add(actionSet);
        for (Iterator i = actionGroupListeners.iterator(); i.hasNext(); ) ((ActionSetListener) i.next()).actionSetAdded(actionSet);
    }

    protected void removeCommands(String groupID, UserAction[] groupActions) {
        if (groupID == null) {
            for (int i = 0; i < groupActions.length; i++) {
                UserAction a = groupActions[i];
                Object nameO = a.getValue(UserAction.KEY_CAPTION);
                Object descO = a.getValue(UserAction.KEY_TOOL_TIP);
                String name = nameO == null ? PlatformTexts.getString("Platform.ACTION_NAME_UNKNOWN") : nameO.toString();
                String desc = descO == null ? PlatformTexts.getString("Platform.ACTION_TOOLTIP_UNKNOWN") : descO.toString();
                Command command = new DefaultCommand.Option(name, desc, true);
                commands.removeCommand(command);
                interpreter.removeCommand(new ActionInterpreter.CommandKey(command, null));
            }
        } else if (groupID.indexOf('/') > 0) {
            Command topcommand;
            Command.Option subcommand;
            String[] ids = groupID.split("\\/");
            Command[] topcomms = CommandSet.getCommands(commands.getCommands(), ids[0]);
            if (topcomms.length > 0) {
                topcommand = topcomms[0];
            } else return;
            subcommand = topcommand.getOption(ids[1]);
            if (subcommand == null) {
                subcommand = new DefaultCommand.Option(ids[1], ids[1] + " submenu", true);
                topcommand.removeOption(subcommand);
            }
            for (int i = 0; i < groupActions.length; i++) {
                UserAction a = groupActions[i];
                Object nameO = a.getValue(UserAction.KEY_CAPTION);
                Object descO = a.getValue(UserAction.KEY_TOOL_TIP);
                String name = nameO == null ? PlatformTexts.getString("Platform.ACTION_NAME_UNKNOWN") : nameO.toString();
                String desc = descO == null ? PlatformTexts.getString("Platform.ACTION_TOOLTIP_UNKNOWN") : descO.toString();
                Command.Option option = new DefaultCommand.Option(name, desc, true);
                subcommand.removeOption(option);
                interpreter.removeCommand(new ActionInterpreter.CommandKey(topcommand, subcommand, option));
            }
        } else {
            Command command;
            Command[] comms = CommandSet.getCommands(commands.getCommands(), groupID);
            if (comms.length == 0) {
                command = new DefaultCommand(groupID, groupID + " menu");
                commands.removeCommand(command);
                for (int i = 0; i < groupActions.length; i++) {
                    UserAction a = groupActions[i];
                    Object nameO = a.getValue(UserAction.KEY_CAPTION);
                    Object descO = a.getValue(UserAction.KEY_TOOL_TIP);
                    String name = nameO == null ? PlatformTexts.getString("Platform.ACTION_NAME_UNKNOWN") : nameO.toString();
                    String desc = descO == null ? PlatformTexts.getString("Platform.ACTION_TOOLTIP_UNKNOWN") : descO.toString();
                    Command.Option option = new DefaultCommand.Option(name, desc, true);
                    command.removeOption(option);
                    interpreter.removeCommand(new ActionInterpreter.CommandKey(command, option));
                }
            }
        }
    }

    /**Remove a group of actions*/
    public void removeActionSets(ActivePlugIn api) {
        for (Iterator ai = actionGroups.iterator(); ai.hasNext(); ) {
            ActionSet as = (ActionSet) ai.next();
            if (api.equals(as.getPlugin())) {
                ai.remove();
                removeCommands(as.getGroupID(), as.getActions());
                for (Iterator i = actionGroupListeners.iterator(); i.hasNext(); ) ((ActionSetListener) i.next()).actionSetRemoved(as);
            }
        }
    }

    public void addActionGroupListener(ActionSetListener listener) {
        actionGroupListeners.add(listener);
    }

    public void removeActionGroupListener(ActionSetListener listener) {
        actionGroupListeners.remove(listener);
    }

    public void runTask(Task t) {
        boolean consumed = false;
        for (Iterator i = taskListeners.iterator(); i.hasNext(); ) if (!consumed) consumed = ((TaskListener) i.next()).taskStarting(t);
        if (!consumed) t.run();
        for (Iterator i = taskListeners.iterator(); i.hasNext(); ) ((TaskListener) i.next()).taskEnded(t);
    }

    public void addTaskListener(TaskListener listener) {
        taskListeners.add(listener);
    }

    public void removeTaskListener(TaskListener listener) {
        taskListeners.remove(listener);
    }

    public void handleError(String message, Throwable t) {
        log.error(message, t);
        String title = t instanceof Error ? PlatformTexts.getString("Platform.TITLE_ERROR") : PlatformTexts.getString("Platform.TITLE_EXCEPTION");
        if (getUI() != null) {
            getUI().sendMessage(message, title, PlatformUI.NO_QUERY);
        }
    }

    public boolean respondToError(String message, Throwable t) {
        log.error(message, t);
        String title = t instanceof Error ? PlatformTexts.getString("Platform.TITLE_ERROR") : PlatformTexts.getString("Platform.TITLE_EXCEPTION");
        if (getUI() != null) {
            byte result = getUI().sendMessage(message, title, PlatformUI.YES_NO_QUERY);
            return result == PlatformUI.YES_OPTION;
        }
        return true;
    }
}
