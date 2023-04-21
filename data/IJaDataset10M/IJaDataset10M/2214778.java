package net.sourceforge.speedcontrol.azureus;

import net.sourceforge.speedcontrol.SpeedControl;
import net.sourceforge.speedcontrol.config.Configuration;
import net.sourceforge.speedcontrol.config.ConfigurationImpl;
import net.sourceforge.speedcontrol.decider.Decider;
import net.sourceforge.speedcontrol.decider.SimpleDecider;
import net.sourceforge.speedcontrol.util.LogAdapter;
import org.gudy.azureus2.plugins.Plugin;
import org.gudy.azureus2.plugins.PluginException;
import org.gudy.azureus2.plugins.PluginInterface;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

/**
 * 
 * Azureus plugin class implementation.
 * Uses PicoContainer to start main classes.
 * 
 */
public class SpeedControlPlugin implements Plugin {

    /**
   * Called by Azureus
   */
    public void initialize(PluginInterface pluginInterface) throws PluginException {
        MutablePicoContainer container = new DefaultPicoContainer();
        container.registerComponentInstance(PluginInterface.class, pluginInterface);
        container.registerComponentImplementation(SpeedControl.class);
        container.registerComponentImplementation(Configuration.class, ConfigurationImpl.class);
        container.registerComponentImplementation(LogAdapter.class);
        container.registerComponentImplementation(Decider.class, SimpleDecider.class);
        container.start();
    }
}
