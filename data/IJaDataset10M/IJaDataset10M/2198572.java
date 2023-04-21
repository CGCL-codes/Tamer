package main.core;

import org.springframework.beans.factory.BeanNameAware;

/**
 * General module class, any module must inherit this class
 * @author anrkaid
 */
public abstract class Module implements BeanNameAware {

    /**
   * Default initialization method, called after object creation
   * @return void
   */
    public void init() {
        log.setSource(this.getClass());
    }

    /**
   * Default shutdown method, called before module unloading
   * @return void
   */
    public void shutdown() {
    }

    /**
   * In this method module must re-red it's settings and refresh all cached values
   * @return void;
   */
    public void refresh() {
    }

    public final void setBeanName(String name) {
        myName = name;
    }

    public final void setLog(Logger log) {
        this.log = log;
    }

    public final void setModuleManager(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    protected String myName;

    protected ModuleManager moduleManager;

    protected Logger log;
}
