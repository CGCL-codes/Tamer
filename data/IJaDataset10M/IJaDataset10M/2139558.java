package org.dago.atapp;

import java.util.*;
import org.apache.log4j.Logger;
import org.dago.atacom.*;
import org.dago.atacom.controller.Controller;
import org.dago.atacom.grammar.Device;
import org.dago.common.*;

/**
 * The module loader
 */
final class Loader implements ControllerFactory {

    /** The logger */
    private final Logger logger = Logger.getLogger(this.getClass());

    /** The list of modules to load */
    private final List<Module> modules = new ArrayList<Module>();

    /** The list of controller factories */
    private final Map<String, Class<? extends Controller>> factories = new HashMap<String, Class<? extends Controller>>();

    @Override
    public Controller createController(Device device) throws DagoException {
        Class<? extends Controller> clazz = this.factories.get(device.getType().toString());
        if (clazz == null) {
            throw new DagoException(I18N.format(I18NMessages.noControllerFound, device.getType()));
        }
        Controller controller;
        try {
            controller = clazz.newInstance();
            controller.initialize(device);
            return controller;
        } catch (Exception err) {
            throw new DagoException(I18N.format(I18NMessages.failToCreateController, device.getType(), err), err);
        }
    }

    @Override
    public void registerController(String type, Class<? extends Controller> clazz) {
        this.factories.put(type, clazz);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("++" + type + ": " + clazz);
        }
    }

    @Override
    public void unregisterController(String type) {
        Class<? extends Controller> factory = this.factories.remove(type);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("--" + type + ": " + factory);
        }
    }

    /**
	 * Loads the modules
	 * @return the number of loaded modules
	 */
    int loadModules() {
        ServiceLoader<Module> loader = ServiceLoader.load(Module.class);
        for (Module module : loader) {
            module.load(this);
            this.modules.add(module);
            this.logger.info(I18N.format(I18NMessages.moduleLoaded, module.getName()));
        }
        return this.modules.size();
    }

    /**
	 * Unloads the modules
	 */
    void unloadModules() {
        for (Module module : this.modules) {
            module.unload(this);
            this.logger.info(I18N.format(I18NMessages.moduleUnloaded, module.getName()));
        }
        this.modules.clear();
    }
}
