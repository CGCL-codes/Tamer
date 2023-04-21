package ro.pub.cs.stadium.plugin;

public interface Plugin {

    int PLUGIN_INPUT = 0x01;

    int PLUGIN_OUTPUT = 0x02;

    void initialise(PluginSettings settings);

    int getPluginImplementations();

    Object getPlugin(int implementation);
}
