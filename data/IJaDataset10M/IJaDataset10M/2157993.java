package net.sourceforge.viea.core;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import net.sourceforge.justin.core.PluginLoaderFacade;
import net.sourceforge.justin.core.exceptions.CantInstantiatePluginException;
import net.sourceforge.viea.core.controller.ConfigurationActionListener;
import net.sourceforge.viea.core.controller.ResetActionListener;
import net.sourceforge.viea.core.controller.RunActionListener;
import net.sourceforge.viea.core.controller.StepActionListener;
import net.sourceforge.viea.core.controller.VieAPlugin;
import net.sourceforge.viea.core.controller.VieAPluginControllerList;
import net.sourceforge.viea.core.controller.VieAPluginList;
import net.sourceforge.viea.core.helpers.VieASystemKeys;
import net.sourceforge.viea.core.view.VieAFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Main.
 * 
 * @author Xavier Detant <xavier.detant@gmail.com>
 * @version 0.0.1 Type creation
 */
public final class Main {

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * Instantiates a new main.
     */
    private Main() {
    }

    /**
     * The main method.
     * 
     * @param args the arguments
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws CantInstantiatePluginException the cant instantiate plugin
     *             exception
     */
    public static void main(final String[] args) throws IOException, CantInstantiatePluginException {
        LOGGER.trace("Starting");
        addAllPlugins();
        VieAPluginControllerList.init();
        final VieAFrame window = VieAFrame.getInstance();
        window.getButtons().addStepListener(new StepActionListener());
        window.getButtons().addRunListener(new RunActionListener());
        window.getButtons().addResetListener(new ResetActionListener());
        window.addConfigurationActionListener(new ConfigurationActionListener(window));
        window.setVisible(true);
    }

    /**
     * Adds the all plugins we know.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws CantInstantiatePluginException the cant instantiate plugin
     *             exception
     */
    protected static void addAllPlugins() throws IOException, CantInstantiatePluginException {
        LOGGER.trace("Loading plugins");
        final PluginLoaderFacade<VieAPlugin> loader = new PluginLoaderFacade<VieAPlugin>(VieAPlugin.class);
        final List<Set<VieAPlugin>> plugins = loader.load(System.getProperty("user.home") + System.getProperty("file.separator") + VieASystemKeys.pluginFolder.getStringValue());
        for (final Set<VieAPlugin> set : plugins) {
            VieAPluginList.addPlugin(set);
        }
    }
}
