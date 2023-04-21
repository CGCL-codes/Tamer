package com.diotalevi.ubernotes.plugins.core;

import com.diotalevi.ubernotes.plugins.api.ApplicationEvent;
import com.diotalevi.ubernotes.plugins.api.GenericPlugin;
import com.diotalevi.ubernotes.plugins.api.PluginRequirement;
import com.diotalevi.ubernotes.plugins.api.Trigger;
import com.diotalevi.ubernotes.util.DynamicStyleSheet;
import java.util.List;

/**
 *
 * @author Filippo Diotalevi
 */
public class FontDownscalingPlugin extends GenericPlugin {

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public String getName() {
        return "ubernotes.core.FontUpscalingPlugin";
    }

    @Override
    public void execute() {
        DynamicStyleSheet.downScale();
        Trigger trigger = new Trigger(ApplicationEvent.CALL_PLUGIN);
        trigger.setPluginName("ubernotes.core.SaveCurrentPagePlugin");
        getUbernotesManager().fireTrigger(trigger);
        trigger = new Trigger(ApplicationEvent.CALL_PLUGIN);
        trigger.setPluginName("ubernotes.core.OpenPagePlugin");
        getUbernotesManager().fireTrigger(trigger);
    }

    @Override
    public List<PluginRequirement> getRequirements() {
        List<PluginRequirement> reqs = super.getRequirements();
        reqs.add(new PluginRequirement("ubernotes.core.OpenPagePlugin", "0.0.1+"));
        reqs.add(new PluginRequirement("ubernotes.core.SaveCurrentPagePlugin", "0.0.1+"));
        return reqs;
    }
}
