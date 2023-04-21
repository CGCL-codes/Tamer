package jfun.yan.xfire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import jfun.yan.factory.Factory;
import org.codehaus.xfire.XFire;
import org.codehaus.xfire.aegis.AegisBindingProvider;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.ServiceFactory;
import org.codehaus.xfire.service.binding.ObjectInvoker;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.codehaus.xfire.soap.Soap11;
import org.codehaus.xfire.soap.SoapVersion;
import org.codehaus.xfire.wsdl11.builder.DefaultWSDLBuilderFactory;

/**
 * A convenience bean which creates a Service from a ServiceFactory instance. If there is no
 * ServiceFactory set, ServiceBean will create one from the ObjectServiceFactory.
 */
public class ServiceBean {

    private ServiceFactory serviceFactory;

    private XFire xFire;

    private String name;

    private String namespace;

    private Class serviceInterface;

    private Factory servant;

    private List inHandlers;

    private List outHandlers;

    private List faultHandlers;

    private List schemas;

    private Class implementationClass;

    private List properties = new ArrayList();

    /** Some properties to make it easier to work with ObjectServiceFactory */
    private SoapVersion soapVersion = Soap11.getInstance();

    private String use;

    private String style;

    private ScopePolicy scope;

    public Service buildService() {
        if (serviceFactory == null) {
            serviceFactory = new ObjectServiceFactory(xFire.getTransportManager(), new AegisBindingProvider());
        }
        final Class intf = getServiceClass();
        final Map properties = new HashMap();
        properties.put(ObjectServiceFactory.SOAP_VERSION, soapVersion);
        if (style != null) properties.put(ObjectServiceFactory.STYLE, style);
        if (use != null) properties.put(ObjectServiceFactory.USE, use);
        if (implementationClass != null) {
            properties.put(ObjectInvoker.SERVICE_IMPL_CLASS, implementationClass);
        }
        copyProperties(properties);
        final Service xfireService = serviceFactory.create(intf, name, namespace, properties);
        xFire.getServiceRegistry().register(xfireService);
        final Factory servant = getServant();
        if (servant != null) {
            xfireService.setInvoker(new FactoryInvoker(servant, scope));
        }
        if (schemas != null) {
            ObjectServiceFactory osf = (ObjectServiceFactory) serviceFactory;
            DefaultWSDLBuilderFactory wbf = (DefaultWSDLBuilderFactory) osf.getWsdlBuilderFactory();
            wbf.setSchemaLocations(schemas);
        }
        if (xfireService.getInHandlers() == null) xfireService.setInHandlers(getInHandlers()); else if (getInHandlers() != null) xfireService.getInHandlers().addAll(getInHandlers());
        if (xfireService.getOutHandlers() == null) xfireService.setOutHandlers(getOutHandlers()); else if (getOutHandlers() != null) xfireService.getOutHandlers().addAll(getOutHandlers());
        if (xfireService.getFaultHandlers() == null) xfireService.setFaultHandlers(getFaultHandlers()); else if (getFaultHandlers() != null) xfireService.getFaultHandlers().addAll(getFaultHandlers());
        return xfireService;
    }

    /**
     * Gets the Factory that creates the servant object who backs this service.
     * @return the Factory that creates the servant object.
     */
    public Factory getServant() {
        return servant;
    }

    /**
     * Sets the Factory object that creates the servant object who backs up the service.
     */
    public void setServant(Factory servant) {
        this.servant = servant;
    }

    /**
     * Set the service class. The service class is passed to the ServiceFactory's
     * create method and is used to determine the operations on the service.
     * @return the service class.
     */
    public Class getServiceClass() {
        return serviceInterface;
    }

    public void setServiceClass(Class serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public void setServiceFactory(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    public ServiceFactory getServiceFactory() {
        return this.serviceFactory;
    }

    /**
     * Sets the service name. Default is the bean name of this exporter.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the service default namespace. Default is a namespace based on the
     * package of the {@link #getServiceClass() service interface}.
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public List getFaultHandlers() {
        return faultHandlers;
    }

    public void setFaultHandlers(List faultHandlers) {
        this.faultHandlers = faultHandlers;
    }

    public List getInHandlers() {
        return inHandlers;
    }

    public void setInHandlers(List inHandlers) {
        this.inHandlers = inHandlers;
    }

    public List getOutHandlers() {
        return outHandlers;
    }

    public void setOutHandlers(List outHandlers) {
        this.outHandlers = outHandlers;
    }

    public void setXfire(XFire xFire) {
        this.xFire = xFire;
    }

    public XFire getXfire() {
        return xFire;
    }

    public Class getImplementationClass() {
        return implementationClass;
    }

    public void setImplementationClass(Class implementationClass) {
        this.implementationClass = implementationClass;
    }

    public List getProperties() {
        return properties;
    }

    public void setProperties(List properties) {
        this.properties = properties;
    }

    public ScopePolicy getScope() {
        return scope;
    }

    public void setScope(ScopePolicy scope) {
        this.scope = scope;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public SoapVersion getSoapVersion() {
        return soapVersion;
    }

    public void setSoapVersion(SoapVersion soapVersion) {
        this.soapVersion = soapVersion;
    }

    public List getSchemas() {
        return schemas;
    }

    public void setSchemas(List schemas) {
        this.schemas = schemas;
    }

    protected void copyProperties(Map properties) {
        for (Iterator iter = getProperties().iterator(); iter.hasNext(); ) {
            Object[] keyval = (Object[]) iter.next();
            String key = (String) keyval[0];
            Object value = keyval[1];
            properties.put(key, value);
        }
    }
}
