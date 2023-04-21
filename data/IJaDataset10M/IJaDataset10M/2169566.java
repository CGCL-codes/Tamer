package org.fulworx.core.rest.restlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.fulworx.core.config.xwork.FulworxObjectFactory;
import org.fulworx.core.config.xwork.FulworxObjectFactoryBuilder;
import org.fulworx.core.config.xwork.ObjectFactoryBuilder;
import org.restlet.Application;
import org.restlet.Context;
import com.noelios.restlet.ext.servlet.ServerServlet;
import com.opensymphony.xwork2.ObjectFactory;

/**
 * The ReSTServlet services http requests.
 * <p/>
 * This needs to be configured with an object factory builder, the default being spring.  This should
 * be initialized in the web.xml like:
 * <pre>
 * param-name=object.factory.builder.instance
 * param-value=org.fulworx.core.command.soa.spring.SpringObjectFactoryBuilder
 * </pre>
 * <p/>
 * Upon a service call, the object factory builder builds the factory and sets it in the ObjectFactory
 * singleton.  Restlet will create the application using the ApplicationBuilder set in web.xml as:
 * <pre>
 * param-name=org.restlet.application
 * param-value=org.fulworx.core.ui.rest.impl.RootApplicationBuilder
 * </pre>
 * <p/>
 * If you don't want any object factory other than the default, you must supply a comma separated list of fully
 * qualified Action names to build into the default object factory.
 * <pre>
 * param-name=actions
 * param-value=org.my.Action1,org.my.Action2
 * </pre>
 */
public class ReSTServlet extends ServerServlet {

    private ObjectFactoryBuilder builder;

    private FulworxObjectFactory objectFactory;

    @Override
    public void init() throws ServletException {
        log("Retrieving object factory builder:" + ObjectFactoryBuilder.OBJECT_FACTORY_BUILDER);
        String builderInstanceClassName = getInitParameter(ObjectFactoryBuilder.OBJECT_FACTORY_BUILDER, null);
        if (builderInstanceClassName != null) {
            log("Initializing restlet with builder:" + builderInstanceClassName);
            try {
                Class ofBuilder = buildClass(builderInstanceClassName);
                builder = (ObjectFactoryBuilder) ofBuilder.newInstance();
            } catch (Exception e) {
                throw new ServletException("Error creating objectFactory", e);
            }
        } else {
            log("No object factory specified, using default");
        }
        super.init();
    }

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        log("doGet");
        super.doGet(httpServletRequest, httpServletResponse);
    }

    public void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        createObjectFactory();
        objectFactory.createActionContext(httpServletRequest);
        super.service(httpServletRequest, httpServletResponse);
    }

    private void createObjectFactory() {
        if (objectFactory == null) {
            FulworxObjectFactoryBuilder factoryBuilder = new FulworxObjectFactoryBuilder();
            if (builder != null) {
                ObjectFactory wrappedObjectFactory = builder.createObjectFactory(this.getComponent().getContext(), this.getServletContext());
                factoryBuilder.setObjectFactory(wrappedObjectFactory);
            }
            try {
                factoryBuilder.setActionList(this.getServletContext().getInitParameter(FulworxObjectFactory.ANNOTATED_ACTIONS));
                objectFactory = factoryBuilder.build();
            } catch (Exception e) {
                log("Error building object factory", e);
            }
        }
    }

    /**
     * Create the application builder and generate the application.
     *
     * @param context of the rest servlet for configs
     * @return built application
     */
    public Application createApplication(final Context context) {
        createObjectFactory();
        Application application = null;
        try {
            application = (RootApplication) objectFactory.buildBean(RootApplication.class, null);
            application.setContext(context);
        } catch (Exception e) {
            log("The ReSTServlet couldn't retrieve the injected class:" + Application.class.getName(), e);
        }
        return application;
    }

    private Class buildClass(String className) throws ClassNotFoundException {
        Class targetClass;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader != null) {
            targetClass = Class.forName(className, false, loader);
        } else {
            targetClass = Class.forName(className);
        }
        return targetClass;
    }
}
