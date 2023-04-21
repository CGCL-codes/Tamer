package ru.nosport.matrixaria.core;

import ru.nosport.matrixaria.modules.common.AppServerProcess;

/**
 *
 * User: vfabr
 * Date: 19.09.2006
 * Time: 16:28:55
 *
 */
class RegContainer implements RegistryContainer {

    private int linkCounter;

    private Object module;

    private Class moduleClass;

    private AppServerProcess moduleProcess;

    /**
	 * Registry container keeps a reference to the module.
	 *
	 * @param _instance - reference on module.
	 */
    public RegContainer(Object _instance) {
        if (_instance == null) {
            throw new IllegalArgumentException("_instance can not be null.");
        }
        if (!(_instance instanceof AppServerProcess)) {
            throw new IllegalArgumentException("_instance is not implemented AppServerProcess.");
        }
        this.moduleProcess = (AppServerProcess) _instance;
        this.linkCounter = 0;
        this.module = _instance;
        this.moduleClass = this.module.getClass();
    }

    private void incLinkCounter() {
        this.linkCounter++;
    }

    private void decLinkCounter() {
        if (this.linkCounter > 0) {
            this.linkCounter--;
        }
    }

    /**
	 * To get link counter.
	 *
	 * @return - link counter.
	 */
    public int getLinkCounter() {
        return this.linkCounter;
    }

    /**
	 * Get the  module class.
	 *
	 * @return module class
	 */
    public String getObjClassName() {
        return this.moduleClass.getName();
    }

    /**
	 * Get reference on module.
	 *
	 * @return - reference on module.
	 */
    public Object getModule() {
        if (this.module != null) {
            this.incLinkCounter();
        }
        return this.module;
    }

    /**
	 * Release module. 
	 *
	 */
    public void releaseModule() {
        this.decLinkCounter();
    }

    /**
	 * Stop module.
	 *
	 * @return - the result of stopping module.
	 */
    public boolean destroy() {
        if (this.linkCounter == 0) {
            if (this.moduleProcess.moduleStop()) {
                this.moduleProcess = null;
                this.module = null;
                this.moduleClass = null;
                return true;
            }
        }
        return false;
    }
}
