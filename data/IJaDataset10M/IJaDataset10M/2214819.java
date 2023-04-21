package org.fluidityproject.example;

import java.util.ArrayList;
import java.util.List;
import org.fluidityproject.controller.Controller;
import org.fluidityproject.example.controller.ExampleAsyncController;
import org.fluidityproject.example.controller.ExampleController;
import org.fluidityproject.example.controller.ExampleFreemarkerController;
import org.fluidityproject.example.controller.ExampleHibernateController;
import org.fluidityproject.example.controller.ExampleJSController;
import org.fluidityproject.hibernate.HibernateRequestManager;

/**
 * A fluidity manager is a class that extends from RequestManager.
 * 
 * You must register this manager in your web.xml with the request manager init
 * param.
 * 
 * <init-param> <param-name>requestManager</param-name>
 * <param-value>org.fluidityproject.example.FluidityExampleManager</param-value>
 * </init-param>
 * 
 * Then you must register every controller that you wish to use with this
 * fluidity instance in the getFluidityControllers method.
 * 
 * For each request that comes in, controllers are inspected in the order they
 * are placed in this list to see if they handle the current request. If they do
 * handle the current request, a new instance is created and the controller
 * life-cycle begins.
 * 
 * @author itaylor
 * 
 */
public class FluidityExampleManager extends HibernateRequestManager {

    @Override
    public List<Controller> getFluidityControllers() {
        ArrayList<Controller> controllers = new ArrayList<Controller>();
        controllers.add(new ExampleController());
        controllers.add(new ExampleAsyncController());
        controllers.add(new ExampleFreemarkerController());
        controllers.add(new ExampleHibernateController());
        controllers.add(new ExampleJSController());
        return controllers;
    }
}
