package org.yajul.micro;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Helps make a list of modules to bootstrap Guice with.
 * <br>
 * User: josh
 * Date: Jan 28, 2009
 * Time: 10:01:07 AM
 */
public class ModuleList {

    private static final Logger log = LoggerFactory.getLogger(ModuleList.class);

    private List<Module> modules = new ArrayList<Module>();

    /**
     * Adds a module to the list.
     * @param module the module
     */
    public void add(Module module) {
        modules.add(module);
    }

    /**
     * Adds a module using reflection.
     * @param moduleClassName the class name of the module.
     */
    public void addClassName(String moduleClassName) {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> clazz = loader.loadClass(moduleClassName);
            Module module = (Module) clazz.newInstance();
            modules.add(module);
        } catch (RuntimeException e) {
            log.error("Unable to add " + moduleClassName + " due to :" + e, e);
            throw e;
        } catch (Exception e) {
            log.error("Unable to add " + moduleClassName + " due to :" + e, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a module that will register a specific instance.
     * @param key the component key
     * @param instance the component instance
     * @param <T> the key type
     */
    public <T> void addInstance(final Class<T> key, final T instance) {
        modules.add(new AbstractModule() {

            protected void configure() {
                log.info("Binding " + key + " to " + instance + " ...");
                bind(key).toInstance(instance);
            }
        });
    }

    /**
     * @return the number of modules in the list
     */
    public int size() {
        return modules.size();
    }

    /**
     * Creates a Guice injector with all of the modules.
     * @return the new Guice injector.
     */
    public Injector createInjector() {
        log.info("Creating injector with " + size() + " modules...");
        Injector injector = Guice.createInjector(getModules());
        log.info("Injector created.");
        return injector;
    }

    /**
     * Returns the modules in a way that can be used with Guice.createInjector()
     * @return an iterable list of modules
     */
    public Iterable<Module> getModules() {
        return modules;
    }
}
