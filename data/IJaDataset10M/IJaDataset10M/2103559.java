package ua.org.nuos.sdms.clientgui.server;

import ua.org.nuos.sdms.clientgui.client.ClientApplication;
import ua.org.nuos.sdms.clientgui.client.context.AppData;
import ua.org.nuos.sdms.clientgui.client.context.UserContext;
import ua.org.nuos.sdms.clientgui.client.controller.AbstractController;
import ua.org.nuos.sdms.clientgui.client.controller.ControllerLocator;
import ua.org.nuos.sdms.clientgui.client.model.ModelLocator;
import ua.org.nuos.sdms.clientgui.client.view.ComponentLocator;
import ua.org.nuos.sdms.clientgui.server.services.ServiceLocator;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: dio
 * Date: 19.02.12
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class AppProxy implements Serializable {

    private static AppProxy instance;

    @Inject
    private Instance<ClientApplication> application;

    public AppProxy() {
        instance = this;
    }

    public UserContext getUserContext() {
        return application.get().getUserContext();
    }

    public ModelLocator getModelLocator() {
        return application.get().getUserContext().getModelLocator();
    }

    public ControllerLocator getControllerLocator() {
        return application.get().getUserContext().getControllerLocator();
    }

    public ServiceLocator getServiceLocator() {
        return application.get().getUserContext().getServiceLocator();
    }

    public ComponentLocator getComponentLocator() {
        return application.get().getUserContext().getComponentLocator();
    }

    public <T extends AbstractController> T getController(String id) {
        return (T) application.get().getUserContext().getControllerLocator().getController(id);
    }

    public AppData getAppTools() {
        return application.get().getUserContext().getAppData();
    }

    public static AppProxy getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Instance of application store is NULL");
        }
        return instance;
    }
}
