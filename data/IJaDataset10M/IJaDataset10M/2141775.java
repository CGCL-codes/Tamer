package at.gp.web.jsf.extval.config.java;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * @author Gerhard Petracek
 */
public class ExtValModuleRegistry {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private List<ConfigurationListener> globalListeners;

    private List<AbstractExtValModule> modules;

    private ExtValModuleRegistry() {
        resetAndInitRegistry();
    }

    public static ExtValModuleRegistry startConfig() {
        return new ExtValModuleRegistry();
    }

    public ExtValModuleRegistry modules(AbstractExtValModule... extValModules) {
        this.modules.addAll(Arrays.asList(extValModules));
        return this;
    }

    public ExtValModuleRegistry globalListeners(ConfigurationListener... listeners) {
        List<ConfigurationListener> listenersToAdd = Arrays.asList(listeners);
        this.globalListeners.addAll(listenersToAdd);
        return this;
    }

    public void endConfig() {
        for (AbstractExtValModule extValModule : this.modules) {
            applyListeners(extValModule);
            extValModule.install();
        }
        resetAndInitRegistry();
    }

    private void applyListeners(AbstractExtValModule extValModule) {
        extValModule.addConfigurationListeners(this.globalListeners.toArray(new ConfigurationListener[this.globalListeners.size()]));
    }

    private void resetAndInitRegistry() {
        resetSetupListeners();
        resetModules();
    }

    private void resetSetupListeners() {
        this.globalListeners = new ArrayList<ConfigurationListener>();
        this.globalListeners.add(new ConfigurationListener() {

            public void before(AbstractExtValModule module) {
                if (logger.isLoggable(Level.INFO)) {
                    logger.info("start to install " + module.getClass().getName());
                }
            }

            public void after(AbstractExtValModule module) {
                if (logger.isLoggable(Level.INFO)) {
                    logger.info("finished to install " + module.getClass().getName());
                }
            }

            public Type[] getListenerTypes() {
                return Type.create(Type.SETUP);
            }
        });
        this.globalListeners.add(new ConfigurationListener() {

            public void before(AbstractExtValModule module) {
                if (logger.isLoggable(Level.INFO)) {
                    logger.info("start to change " + module.getClass().getName());
                }
            }

            public void after(AbstractExtValModule module) {
                if (logger.isLoggable(Level.INFO)) {
                    logger.info("finished to change " + module.getClass().getName());
                }
            }

            public Type[] getListenerTypes() {
                return Type.create(Type.UPDATE);
            }
        });
        this.globalListeners.add(new ConfigurationListener() {

            public void before(AbstractExtValModule module) {
                if (logger.isLoggable(Level.INFO)) {
                    logger.info("start to init ExtVal with " + module.getClass().getName());
                }
            }

            public void after(AbstractExtValModule module) {
                if (logger.isLoggable(Level.INFO)) {
                    logger.info("finished to init ExtVal with " + module.getClass().getName());
                }
            }

            public Type[] getListenerTypes() {
                return Type.create(Type.INIT);
            }
        });
    }

    private void resetModules() {
        this.modules = new ArrayList<AbstractExtValModule>();
    }
}
