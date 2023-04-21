package com.avaje.ebean.server.deploy;

import java.util.List;
import com.avaje.ebean.bean.BeanController;

/**
 * Factory for controlling the construction of BeanControllers.
 */
public interface BeanControllerManager {

    /**
	 * Return the number of beans with a registered controller.
	 */
    public int getRegisterCount();

    /**
     * Create the appropriate BeanController.
     */
    public int createBeanControllers(List<Class<?>> controllerList);

    /**
     * Return the BeanController for a given entity type.
     */
    public BeanController getBeanController(Class<?> entityType);
}
