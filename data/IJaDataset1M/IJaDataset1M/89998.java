package edu.udo.scaffoldhunter.plugins.dataimport.impl.example2;

import java.io.Serializable;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import edu.udo.scaffoldhunter.plugins.PluginSettingsPanel;
import edu.udo.scaffoldhunter.plugins.dataimport.AbstractImportPlugin;
import edu.udo.scaffoldhunter.plugins.dataimport.PluginResults;

/**
 * @author Bernhard Dick
 * 
 */
@PluginImplementation
public class Example2ImportPlugin extends AbstractImportPlugin {

    @Override
    public String getTitle() {
        return "Example 2";
    }

    @Override
    public String getID() {
        return "Example2ImportPlugin_v1";
    }

    @Override
    public String getDescription() {
        return "The second example in the manual";
    }

    @Override
    public PluginSettingsPanel getSettingsPanel(Serializable settings, Object arguments) {
        return new PluginSettingsPanel() {

            @Override
            public Serializable getSettings() {
                return null;
            }

            @Override
            public Object getArguments() {
                return null;
            }
        };
    }

    @Override
    public PluginResults getResults(Object arguments) {
        return new Example2ImportPluginResults();
    }

    @Override
    public String checkArguments(Object arguments) {
        return null;
    }
}
