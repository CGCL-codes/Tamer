package pedro.mda.config;

import java.util.ArrayList;

public class PluginConfigurableComponent {

    private ArrayList pluginConfigurations;

    public PluginConfigurableComponent() {
        pluginConfigurations = new ArrayList();
    }

    public PluginConfiguration[] getPluginConfigurations() {
        PluginConfiguration[] results = (PluginConfiguration[]) pluginConfigurations.toArray(new PluginConfiguration[0]);
        return results;
    }

    public void addPluginConfiguration(PluginConfiguration pluginConfiguration) {
        pluginConfigurations.add(pluginConfiguration);
    }

    public void setPluginConfigurations(ArrayList pluginConfigurations) {
        this.pluginConfigurations = pluginConfigurations;
    }
}
