package de.schwarzrot.ui.action.support;

import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import de.schwarzrot.app.Application;
import de.schwarzrot.app.domain.AccessMode;
import de.schwarzrot.app.support.ApplicationServiceProvider;
import de.schwarzrot.ui.action.ActionHandler;
import de.schwarzrot.ui.action.ApplicationCommand;
import de.schwarzrot.ui.service.CommandFactoryBuilder;

/**
 * class to handle user-actions and to allow access to defined actions. The
 * class has a static repository, where all ActionHandler register their actions
 * and so it is possible to find an Action from an unknown but active
 * ActionHandler.
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 * 
 */
public abstract class AbstractActionHandler implements ActionHandler {

    protected static CommandFactoryBuilder facBuilder;

    private static Map<String, Map<String, AbstractAction>> handlers;

    public AbstractActionHandler(String name) {
        this.name = name;
        if (!handlers.containsKey(name)) {
            synchronized (handlers) {
                actionMap = new HashMap<String, AbstractAction>();
                handlers.put(name, actionMap);
            }
        } else actionMap = handlers.get(name);
    }

    public JToolBar createExtraToolBar() {
        return createToolBar(getExtraToolBarCommands(), JToolBar.VERTICAL);
    }

    @Override
    public JPopupMenu createPopup(Object context) {
        if (facBuilder == null) facBuilder = ApplicationServiceProvider.getService(CommandFactoryBuilder.class);
        ApplicationCommandFactory acf = facBuilder.createApplicationCommandFactory(new ActionHandler[] { this });
        if (acf != null) {
            init(context);
            return acf.createPopup(getName(), getPopupCommands(context));
        }
        return null;
    }

    public JPopupMenu createPopup(Object context, Enum<?>[][] cmdList) {
        if (facBuilder == null) facBuilder = ApplicationServiceProvider.getService(CommandFactoryBuilder.class);
        ApplicationCommandFactory acf = facBuilder.createApplicationCommandFactory(new ActionHandler[] { this });
        if (acf != null) {
            init(context);
            return acf.createPopup(getName(), cmdList);
        }
        return null;
    }

    public final JMenu createSubMenu(Enum<?> cmd) {
        return createSubMenu(cmd.name());
    }

    @Override
    public JMenu createSubMenu(String cmd) {
        return null;
    }

    public JToolBar createToolBar(Enum<?>[] commands, int orientation) {
        JToolBar tb = null;
        if (commands != null) {
            if (facBuilder == null) facBuilder = ApplicationServiceProvider.getService(CommandFactoryBuilder.class);
            ApplicationCommandFactory acf = facBuilder.createApplicationCommandFactory(new ActionHandler[] { this });
            tb = acf.createToolBar(getName(), commands, orientation);
        }
        return tb;
    }

    /**
     * enable a single action by identifier
     * 
     * @param key
     */
    @Override
    public void enableAction(String key) {
        enableAction(key, true);
    }

    /**
     * enable/disable a single action by identifier
     * 
     * @param key
     * @param enabled
     */
    @Override
    public void enableAction(String key, boolean enabled) {
        AbstractAction a = findAction(key);
        if (a != null) a.setEnabled(enabled);
    }

    @Override
    public AbstractAction findAction(String composed) {
        if (actionMap.containsKey(composed)) return actionMap.get(composed);
        String[] parts = composed.split("\\.");
        AbstractAction a = null;
        if (parts.length > 1) a = findAction(parts[0], parts[1]);
        return a;
    }

    @Override
    public AbstractAction findAction(String name, Enum<?> cmd) {
        return findAction(name, cmd.name());
    }

    @Override
    public AbstractAction findAction(String name, String key) {
        AbstractAction a = null;
        if (handlers.containsKey(name)) a = handlers.get(name).get(key);
        return a;
    }

    @Override
    public AbstractAction getAction(String key) {
        return actionMap.get(key);
    }

    public Enum<?>[] getExtraToolBarCommands() {
        return null;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("need to implement init() for class: " + getClass().getName());
    }

    public void init(Object context) {
        throw new UnsupportedOperationException("need to implement init(Object contextObj) for class: " + getClass().getName());
    }

    @Override
    public void putAction(String key, AbstractAction action) {
        actionMap.put(key, action);
    }

    /**
     * enable/disable all actions related to the handler.
     * 
     * @param enable
     */
    @Override
    public void setEnabled(boolean enable) {
        if (actionMap == null) return;
        for (String key : actionMap.keySet()) actionMap.get(key).setEnabled(enable);
    }

    public final void setName(String name) {
        this.name = name;
    }

    public void setupAction(Application<?> app, Enum<?> cmd, AbstractActionCallback cb) {
        setupAction(app, cmd, cb, 0, AccessMode.APP_ANY);
    }

    /**
     * the major reason for ActionHandler: have a central place to customize
     * actions and action related components.
     * 
     * @param cmd
     *            - the enum identifier for the action
     * @param cb
     *            - the anonymous callback
     * @param mnemonic
     *            - a mnemonic key
     */
    public void setupAction(Application<?> app, Enum<?> cmd, AbstractActionCallback cb, int mnemonic, AccessMode mode) {
        setupAction(app.getName(), cmd, cb, mnemonic, mode);
    }

    public void setupAction(String appID, Enum<?> cmd, AbstractActionCallback cb, int mnemonic) {
        setupAction(appID, cmd, cb, mnemonic, AccessMode.APP_ANY);
    }

    public void setupAction(String appID, Enum<?> cmd, AbstractActionCallback cb, int mnemonic, AccessMode mode) {
        String actionKey = cmd.name();
        setupAction(appID, actionKey, cb, mnemonic, mode);
    }

    public void setupAction(String appID, String actionKey, AbstractActionCallback cb, int mnemonic, AccessMode mode) {
        ApplicationCommand ac = new ApplicationCommand(actionKey, appID, cb, mnemonic, mode);
        putAction(actionKey, ac);
    }

    private Map<String, AbstractAction> actionMap;

    private String name;

    static {
        handlers = new HashMap<String, Map<String, AbstractAction>>();
    }
}
