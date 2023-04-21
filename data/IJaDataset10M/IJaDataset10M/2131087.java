package activator;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.PluginJAR;
import org.gjt.sp.util.Log;
import common.gui.actions.CustomAction;

class Reload extends CustomAction {

    private String name;

    private PluginJAR jar;

    public Reload(PluginJAR pluginJAR, String dispName) {
        super(dispName);
        name = dispName;
        jar = pluginJAR;
        setToolTipText(jEdit.getProperty("activator.Click_to_reload", "Click to reload:") + " " + name);
    }

    public Reload(PluginList.Plugin plugin) {
        super(plugin.getJAR().getPlugin().getClassName());
        jar = plugin.getJAR();
        name = jar.getPlugin().getClassName();
        setToolTipText(jEdit.getProperty("activator.Click_to_reload", "Click to reload:") + " " + name);
    }

    public void actionPerformed(ActionEvent event) {
        Log.log(Log.DEBUG, this, "Reloading " + jar);
        Stack<String> unloaded = PluginManager.unloadPluginJAR(jar);
        if (unloaded.empty()) {
            return;
        }
        Set<String> reloaded = new HashSet<String>();
        jar = null;
        String path = null;
        do {
            path = unloaded.pop();
            if (path != null && !reloaded.contains(path)) {
                PluginManager.loadPluginJAR(path);
                reloaded.add(path);
            }
        } while (path != null && !unloaded.empty());
    }
}
