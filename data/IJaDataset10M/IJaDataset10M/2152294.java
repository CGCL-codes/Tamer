package net.grinder.engine.process;

import java.util.HashMap;
import java.util.Map;
import net.grinder.common.Logger;
import net.grinder.common.ThreadLifeCycleListener;
import net.grinder.engine.common.EngineException;
import net.grinder.plugininterface.GrinderPlugin;
import net.grinder.plugininterface.PluginException;
import net.grinder.plugininterface.PluginRegistry;
import net.grinder.script.Grinder.ScriptContext;
import net.grinder.statistics.StatisticsServices;
import net.grinder.util.TimeAuthority;

/**
 * Registry of live {@link GrinderPlugin} implementations. Responsible
 * for plugin process and thread initialisation.
 *
 * @author Philip Aston
 * @version $Revision: 3762 $
 */
final class PluginRegistryImplementation extends PluginRegistry implements ProcessLifeCycleListener {

    private final Logger m_logger;

    private final ScriptContext m_scriptContext;

    private final ThreadContextLocator m_threadContextLocator;

    private final StatisticsServices m_statisticsServices;

    private final TimeAuthority m_timeAuthority;

    private final Map m_plugins = new HashMap();

    /**
   * Constructor.
   */
    PluginRegistryImplementation(Logger logger, ScriptContext scriptContext, ThreadContextLocator threadContextLocator, StatisticsServices statisticsServices, TimeAuthority timeAuthority) {
        m_logger = logger;
        m_scriptContext = scriptContext;
        m_threadContextLocator = threadContextLocator;
        m_statisticsServices = statisticsServices;
        m_timeAuthority = timeAuthority;
        setInstance(this);
    }

    /**
   * Used to register a new plugin.
   *
   * @param plugin The plugin instance.
   * @exception EngineException if an error occurs
   */
    public void register(GrinderPlugin plugin) throws EngineException {
        synchronized (m_plugins) {
            if (!m_plugins.containsKey(plugin)) {
                final RegisteredPlugin registeredPlugin = new RegisteredPlugin(plugin, m_scriptContext, m_threadContextLocator, m_statisticsServices, m_timeAuthority);
                try {
                    plugin.initialize(registeredPlugin);
                } catch (PluginException e) {
                    throw new EngineException("An instance of the plug-in class '" + plugin.getClass().getName() + "' could not be initialised.", e);
                }
                m_plugins.put(plugin, registeredPlugin);
                m_logger.output("registered plug-in " + plugin.getClass().getName());
            }
        }
    }

    public void threadCreated(final ThreadContext threadContext) {
        threadContext.registerThreadLifeCycleListener(new ThreadLifeCycleListener() {

            public void beginThread() {
                final RegisteredPlugin[] registeredPlugins;
                synchronized (m_plugins) {
                    registeredPlugins = (RegisteredPlugin[]) m_plugins.values().toArray(new RegisteredPlugin[m_plugins.size()]);
                }
                for (int i = 0; i < registeredPlugins.length; ++i) {
                    try {
                        registeredPlugins[i].createPluginThreadListener(threadContext);
                    } catch (EngineException e) {
                    }
                }
            }

            public void beginRun() {
            }

            public void endRun() {
            }

            public void beginShutdown() {
            }

            public void endThread() {
            }
        });
    }
}
