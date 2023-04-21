package siouxsie.desktop.tutorial;

import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.UIManager;
import siouxsie.plugin.PluginManager;
import siouxsie.plugin.PluginRegistry;

public class FirstDesktopEver {

    public static void main(String[] args) throws Exception {
        new FirstDesktopEver();
    }

    public FirstDesktopEver() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        InputStream in = getClass().getResourceAsStream("/siouxsie/desktop/tutorial/FirstDesktopEver.xml");
        PluginRegistry registry = new PluginRegistry();
        registry.loadPluginDescriptions(new InputStreamReader(in));
        PluginManager manager = new PluginManager();
        manager.init(registry);
        manager.startApplication();
    }
}
