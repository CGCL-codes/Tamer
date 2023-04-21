package org.piuframework.context.jboss;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.management.Notification;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.jboss.logging.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.piuframework.PiuFrameworkException;
import org.piuframework.config.ConfigProperties;
import org.piuframework.context.ApplicationContext;
import org.piuframework.context.ServiceContext;
import org.piuframework.context.impl.XMLApplicationContext;
import org.piuframework.j2ee.jndi.util.JNDIUtils;
import org.piuframework.service.config.FlavorConfig;
import org.piuframework.service.config.InterceptorChainConfig;
import org.piuframework.service.config.InvocationHandlerConfig;
import org.piuframework.service.config.LifecycleHandlerConfig;
import org.piuframework.service.config.ServiceConfig;
import org.piuframework.service.config.ServiceFactoryConfig;
import org.piuframework.util.ClassUtils;

/**
 * TODO
 *
 * @author Dirk Mascher
 */
public class PiuFrameworkContext extends ServiceMBeanSupport implements PiuFrameworkContextMBean {

    private static final Logger log = Logger.getLogger(PiuFrameworkContext.class);

    /**
     * notification type produced when the application context gets created
     */
    public static final String APPLICATION_CONTEXT_CREATE = "piuframework.applicationcontext.create";

    /**
     * notification type produced when the application context gets destroyed
     */
    public static final String APPLICATION_CONTEXT_DESTROY = "piuframework.applicationcontext.destroy";

    private String jndiName;

    private String resourceLoaderClassName;

    private String repositoryPath;

    private ApplicationContext context;

    public PiuFrameworkContext() {
    }

    public void setApplicationContextName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String getApplicationContextName() {
        return this.jndiName;
    }

    public void setResourceLoaderClassName(String className) {
        this.resourceLoaderClassName = className;
    }

    public String getResourceLoaderClassName() {
        return this.resourceLoaderClassName;
    }

    public void setRepositoryPath(String path) {
        this.repositoryPath = path;
    }

    public String getRepositoryPath() {
        return this.repositoryPath;
    }

    public void reconfigure() throws Exception {
        destroyApplicationContext();
        createApplicationContext();
    }

    private void bind() {
        InitialContext ctx = null;
        try {
            ctx = new InitialContext();
            JNDIUtils.bind(ctx, this.jndiName, this.context);
        } catch (NamingException e) {
            throw new PiuFrameworkException("Unable to bind ApplicationContext into JNDI", e);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Throwable ignore) {
                }
            }
        }
    }

    private void unbind() {
        InitialContext ctx = null;
        try {
            ctx = new InitialContext();
            JNDIUtils.unbind(ctx, jndiName);
        } catch (NamingException e) {
            throw new PiuFrameworkException("Unable to unbind ApplicationContext from JNDI", e);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Throwable ignore) {
                }
            }
        }
    }

    private void createApplicationContext() throws Exception {
        log.debug("creating ApplicationContext; " + this);
        Properties properties = new Properties();
        if (resourceLoaderClassName != null) {
            properties.put(XMLApplicationContext.PARAM_RESOURCE_LOADER_CLASS, resourceLoaderClassName);
        }
        if (repositoryPath != null) {
            properties.put(XMLApplicationContext.PARAM_CONFIG_REPOSITORY_PATH, repositoryPath);
        }
        this.context = new XMLApplicationContext(properties);
        bind();
        sendNotification(new Notification(APPLICATION_CONTEXT_CREATE, getServiceName(), getNextNotificationSequenceNumber()));
        log.info("ApplicationContext successfully created and bound into JNDI [" + jndiName + "]");
    }

    private void destroyApplicationContext() throws Exception {
        if (context != null) {
            unbind();
            context = null;
            sendNotification(new Notification(APPLICATION_CONTEXT_DESTROY, getServiceName(), getNextNotificationSequenceNumber()));
        }
    }

    public void changeServiceDefaultFlavor(String serviceInterfaceName, String defaultFlavor) throws Exception {
        log.debug("chaning default flavor for Piu service, serviceInterface = " + serviceInterfaceName + ", flavor = " + defaultFlavor);
        if (context != null) {
            ServiceContext serviceContext = (ServiceContext) context.getSubContext(ServiceContext.NAME);
            ServiceConfig service = serviceContext.getServiceFactoryConfig().getServiceConfig(serviceInterfaceName);
            if (service == null) {
                throw new PiuFrameworkException("Service is not registered, serviceInterface = " + serviceInterfaceName);
            }
            service.setDefaultFlavorName(defaultFlavor);
            service.setDefaultFlavorConfig();
        }
        log.debug("default flavor for Piu service changed, serviceInterface = " + serviceInterfaceName + ", flavor = " + defaultFlavor);
    }

    public String listRegisteredServices() throws Exception {
        StringBuffer buf = new StringBuffer();
        buf.append("<pre>");
        if (context == null) {
            buf.append("### no ApplicationContext ###\n");
        } else {
            ServiceContext serviceContext = (ServiceContext) context.getSubContext(ServiceContext.NAME);
            if (serviceContext == null) {
                buf.append("### no ServiceContext ###\n");
            } else {
                ServiceFactoryConfig factoryConfig = serviceContext.getServiceFactoryConfig();
                Set serviceNames = factoryConfig.serviceInterfaceNames();
                if (serviceNames == null || serviceNames.isEmpty()) {
                    buf.append("### no registered services ###\n");
                } else {
                    for (Iterator serviceIterator = serviceNames.iterator(); serviceIterator.hasNext(); ) {
                        ServiceConfig service = factoryConfig.getServiceConfig((String) serviceIterator.next());
                        buf.append("\n");
                        buf.append("+ ").append(service.getInterfaceName()).append("\n");
                        buf.append("\tname: ").append(service.getName()).append("\n");
                        buf.append("\tstateful: ").append(service.isStateful()).append("\n");
                        if (service.isStateful()) {
                            Class[] signature = service.getCreateSignature();
                            if (signature != null) {
                                buf.append("\t\tcreate signature: ");
                                for (int index = 0; index < signature.length; index++) {
                                    buf.append(signature[index].toString());
                                    if (index < signature.length - 1) {
                                        buf.append(", ");
                                    }
                                }
                                buf.append("\n");
                            }
                        }
                        FlavorConfig defaultFlavor = service.getDefaultFlavorConfig();
                        if (defaultFlavor != null) {
                            buf.append("\tdefault flavor: \"").append(defaultFlavor.getName()).append("\"\n");
                        }
                        Set flavorNames = service.flavorNames();
                        if (flavorNames != null) {
                            buf.append("\tlist of defined flavors:\n");
                            for (Iterator flavorIterator = flavorNames.iterator(); flavorIterator.hasNext(); ) {
                                FlavorConfig flavor = service.getFlavorConfig((String) flavorIterator.next());
                                buf.append("\t\tflavor: \"" + flavor.getName()).append("\"\n");
                                InterceptorChainConfig interceptorChainConfig = flavor.getInterceptorChainConfig();
                                if (interceptorChainConfig != null) {
                                    if (interceptorChainConfig.getName() == null) {
                                        buf.append("\t\t\tinterceptor chain: @anonymous\n");
                                    } else {
                                        buf.append("\t\t\tinterceptor chain: \"" + interceptorChainConfig.getName()).append("\"\n");
                                    }
                                }
                                LifecycleHandlerConfig lifecycleHandlerConfig = flavor.getLifecycleHandlerConfig();
                                if (lifecycleHandlerConfig != null) {
                                    if (lifecycleHandlerConfig.getName() == null) {
                                        buf.append("\t\t\tlifecycle handler: @anonymous\n");
                                    } else {
                                        buf.append("\t\t\tlifecycle handler: \"" + lifecycleHandlerConfig.getName()).append("\"\n");
                                    }
                                    ConfigProperties properties = flavor.getLifecycleHandlerProperties();
                                    dumpProperties(buf, service, properties);
                                }
                                InvocationHandlerConfig invocationHandlerConfig = flavor.getInvocationHandlerConfig();
                                if (invocationHandlerConfig != null) {
                                    if (invocationHandlerConfig.getName() == null) {
                                        buf.append("\t\t\tinvocation handler: @anonymous\n");
                                    } else {
                                        buf.append("\t\t\tinvocation handler: \"" + invocationHandlerConfig.getName()).append("\"\n");
                                    }
                                    ConfigProperties properties = flavor.getInvocationHandlerProperties();
                                    dumpProperties(buf, service, properties);
                                }
                            }
                        }
                    }
                }
            }
        }
        buf.append("</pre>");
        return buf.toString();
    }

    private void dumpProperties(StringBuffer buf, ServiceConfig service, ConfigProperties properties) {
        if (properties != null && !properties.isEmpty()) {
            Set names = properties.keySet();
            for (Iterator i = names.iterator(); i.hasNext(); ) {
                String name = (String) i.next();
                Object value = properties.getTypedProperty(name);
                if (value instanceof String) {
                    try {
                        Map substMap = new HashMap();
                        substMap.put("${service.name}", service.getName());
                        Class serviceInterface = ClassUtils.forName(service.getInterfaceName());
                        value = ClassUtils.substClassProperties("service", serviceInterface, (String) value, substMap);
                    } catch (Throwable t) {
                    }
                }
                buf.append("\t\t\t\t").append(name).append(" = ").append(value).append("\n");
            }
        }
    }

    protected void startService() throws Exception {
        log.debug("PiuFrameworkContext MBean starting; " + this);
        createApplicationContext();
    }

    protected void stopService() throws Exception {
        destroyApplicationContext();
    }

    public String toString() {
        return super.toString() + " [ServiceName=" + this.getServiceName() + ", JNDI=" + this.getApplicationContextName() + "]";
    }
}
