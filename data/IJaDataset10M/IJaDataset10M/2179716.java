package br.usp.iterador.plugin;

import java.awt.Component;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import br.usp.iterador.gui.FormulaPane;
import br.usp.iterador.gui.util.DataModel;

public class PanelManager {

    private static final Logger logger = Logger.getLogger(PanelManager.class);

    private final List<DataModel> models = new ArrayList<DataModel>();

    private final Map<Plugin, List<JPanel>> panels = new HashMap<Plugin, List<JPanel>>();

    private final PluginManager pluginManager;

    public PanelManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public void refresh() {
        this.models.clear();
        this.panels.clear();
        for (Plugin p : pluginManager.getActivatedPlugins()) {
            installPanels(p);
        }
    }

    private void installPanels(Plugin plugin) {
        Method[] declaredMethods = plugin.getClass().getDeclaredMethods();
        ArrayList<JPanel> panels = new ArrayList<JPanel>();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(FormulaPane.class)) {
                try {
                    JPanel panel = (JPanel) method.invoke(plugin, models);
                    panels.add(panel);
                } catch (Exception e) {
                    logger.error("Unable to install panels", e);
                }
            }
        }
        this.panels.put(plugin, panels);
    }

    public List<Component> getPanels() {
        List<Component> panels = new ArrayList<Component>();
        for (List<JPanel> internals : this.panels.values()) {
            panels.addAll(internals);
        }
        return panels;
    }

    public List<DataModel> getModels() {
        return new ArrayList<DataModel>(models);
    }
}
