package sk.naive.talker.command;

import sk.naive.talker.*;
import sk.naive.talker.message.DefaultMessageFactory;
import sk.naive.talker.props.PropertyStoreException;
import sk.naive.talker.plugin.*;
import sk.naive.talker.util.Utils;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 * Plugin.
 * <p/>
 *
 * @author <a href="mailto:virgo@naive.deepblue.sk">Richard "Virgo" Richter</a>
 * @version $Revision: 1.10 $ $Date: 2005/03/21 21:07:18 $
 */
public class Plugin extends AbstractCommand {

    public void exec() throws CommandException, RemoteException {
        Utils.checkPermission(user, Consts.PERM_SUPERUSER);
        String[] sa = Utils.splitWords(params, 2);
        if (params == null || params.length() == 0 || sa.length == 0) {
            listPlugins(user);
            return;
        }
        String pCmd = sa[0].toLowerCase();
        String pluginName = null;
        if (sa.length >= 2) {
            pluginName = sa[1];
        }
        try {
            if ("add".startsWith(pCmd)) {
                addPlugin(pluginName, user);
            } else if ("remove".startsWith(pCmd)) {
                removePlugin(pluginName, user);
            } else if ("list".startsWith(pCmd)) {
                listPlugins(user);
            } else {
                sendMisusageWarning();
            }
        } catch (PropertyStoreException e) {
            throw new CommandException(e);
        }
    }

    private void listPlugins(User user) throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append(getString("plugin.listHead", user.getProperties()));
        for (TalkerPlugin plugin : getTalker().getPlugins()) {
            ctxSet(DefaultMessageFactory.CTXKEY_VAL, plugin.name());
            sb.append(getString("plugin.listItem", user.getProperties()));
        }
        sb.append(getString("plugin.listTail", user.getProperties()));
        user.send(sb.toString());
    }

    private void addPlugin(String pluginName, User user) throws RemoteException, PropertyStoreException {
        try {
            if (pluginName == null) {
                sendHelper().sendMessage(user, "plugin.addInvalid");
                return;
            }
            TalkerPlugin plugin = new PluginHelper().createPlugin(pluginName);
            if (plugin == null) {
                ctxSet(DefaultMessageFactory.CTXKEY_VAL, pluginName);
                sendHelper().sendMessage(user, "plugin.fileNotFound");
                return;
            }
            TalkerPlugin found = findPlugin(plugin.name());
            ctxSet(DefaultMessageFactory.CTXKEY_VAL, plugin.name());
            if (found != null) {
                sendHelper().sendMessage(user, "plugin.addAlreadyAdded");
                return;
            }
            getTalker().addPlugin(plugin);
            getTalker().set(Consts.TPROP_PLUGIN_NAMES, pluginNames());
            sendHelper().sendMessage(user, "plugin.added");
        } catch (PluginException e) {
            Utils.unexpectedExceptionWarning(e);
            sendHelper().sendMessage(user, "plugin.addError");
        }
    }

    private void removePlugin(String pluginName, User user) throws PropertyStoreException, RemoteException {
        if (pluginName == null) {
            sendHelper().sendMessage(user, "plugin.removeInvalid");
            return;
        }
        TalkerPlugin found = findPlugin(pluginName);
        if (found != null) {
            getTalker().removePlugin(found);
            getTalker().set(Consts.TPROP_PLUGIN_NAMES, pluginNames());
            ctxSet(DefaultMessageFactory.CTXKEY_VAL, found.name());
            sendHelper().sendMessage(user, "plugin.removed");
        } else {
            ctxSet(DefaultMessageFactory.CTXKEY_VAL, pluginName);
            sendHelper().sendMessage(user, "plugin.notFound");
        }
    }

    private TalkerPlugin findPlugin(String name) {
        TalkerPlugin found = null;
        for (TalkerPlugin plugin : getTalker().getPlugins()) {
            if (plugin.name().startsWith(name)) {
                found = plugin;
                break;
            }
        }
        return found;
    }

    private Collection<String> pluginNames() throws PropertyStoreException {
        return (Collection<String>) getTalker().get(Consts.TPROP_PLUGIN_NAMES);
    }
}
