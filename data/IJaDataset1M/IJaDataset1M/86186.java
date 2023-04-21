package net.sf.simplelogviewer.test.plugin;

import net.sf.simplelogviewer.plugin.ILogParser;
import net.sf.simplelogviewer.plugin.IPlugin;
import net.sf.simplelogviewer.plugin.IPluginInfo;

/**
 * @author Hvan Konstantin Vladimirovich (dotidot)
 */
public class StubPlugin implements IPlugin {

    ILogParser logParser = new StubParser();

    IPluginInfo pluginInfo = new StubPluginInfo();

    public ILogParser getLogParser() {
        return logParser;
    }

    public IPluginInfo getPluginInfo() {
        return pluginInfo;
    }
}
