package jfun.yan.xfire;

import java.beans.IntrospectionException;
import java.util.HashMap;
import java.util.List;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.soap.SoapVersion;
import jfun.yan.Component;
import jfun.yan.Components;
import jfun.yan.Creator;
import jfun.yan.factory.Factory;
import jfun.yan.factory.GlobalScope;
import jfun.yan.factory.PooledFactory;
import jfun.yan.xml.NutsUtils;
import jfun.yan.xml.nuts.DelegatingNut;

/**
 * The nut class that wraps up a component as a service.
 * <p>
 * @author Ben Yu
 * Feb 2, 2006 6:40:08 PM
 */
public class ServiceNut extends DelegatingNut {

    private Component default_xfire;

    public ServiceNut() {
        this(Components.useKey("xfire"));
    }

    /**
   * Create a ServiceNut object.
   * @param default_xfire the default xfire component if not specified.
   */
    public ServiceNut(Component default_xfire) {
        this.default_xfire = default_xfire;
    }

    private Component serviceFactory;

    protected Component xFire;

    private String name;

    private String namespace;

    private Class serviceInterface;

    private List inHandlers;

    private List outHandlers;

    private List faultHandlers;

    private List schemas;

    protected Class implementationClass;

    private List properties;

    /** Some properties to make it easier to work with ObjectServiceFactory */
    protected SoapVersion soapVersion;

    protected String use;

    protected String style;

    private ScopePolicy scope;

    public List getFaultHandlers() {
        return faultHandlers;
    }

    public void setFaultHandlers(List faultHandlers) {
        this.faultHandlers = faultHandlers;
    }

    public Class getImplementationClass() {
        return implementationClass;
    }

    public void setImplementationClass(Class implementationClass) {
        this.implementationClass = implementationClass;
    }

    public List getInHandlers() {
        return inHandlers;
    }

    public void setInHandlers(List inHandlers) {
        this.inHandlers = inHandlers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public List getOutHandlers() {
        return outHandlers;
    }

    public void setOutHandlers(List outHandlers) {
        this.outHandlers = outHandlers;
    }

    public List getProperties() {
        return properties;
    }

    public void setProperties(List properties) {
        this.properties = properties;
    }

    public List getSchemas() {
        return schemas;
    }

    public void setSchemas(List schemas) {
        this.schemas = schemas;
    }

    public ScopePolicy getScope() {
        return scope;
    }

    public void setScope(ScopePolicy scope) {
        this.scope = scope;
    }

    public Component getServiceFactory() {
        return serviceFactory;
    }

    public void setServiceFactory(Component serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    public Class getServiceClass() {
        return serviceInterface;
    }

    public void setServiceClass(Class serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public SoapVersion getSoapVersion() {
        return soapVersion;
    }

    public void setSoapVersion(SoapVersion soapVersion) {
        this.soapVersion = soapVersion;
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

    public Component getXFire() {
        return xFire;
    }

    public void setXFire(Component fire) {
        xFire = fire;
    }

    public Component eval() throws IntrospectionException {
        final Component cc = super.getComponent();
        Class impltype = getImplementationClass();
        if (impltype == null) {
            if (cc != null) {
                impltype = cc.getType();
            }
        }
        Class intf = getServiceClass();
        if (intf == null) {
            if (impltype != null) {
                intf = impltype;
            } else {
                throw new RuntimeException("Error creating service " + name + ". The service class or the service bean must be set!");
            }
        }
        final ServiceBuilder builder = new ServiceBuilder();
        builder.add("faultHandlers", this.faultHandlers).add("xfire", this.xFire == null ? this.default_xfire : this.xFire).add("servant", toLazyFactory(this.getProxyForService())).add("implementationClass", impltype).add("inHandlers", this.inHandlers).add("name", this.name == null ? this.getId() : this.name).add("namespace", this.namespace).add("outHandlers", this.outHandlers).add("properties", this.properties).add("schemas", this.schemas).add("scope", this.scope).add("serviceClass", intf).add("serviceFactory", this.serviceFactory).add("soapVersion", this.soapVersion).add("style", this.style).add("use", this.use);
        return builder.getResult();
    }

    private static final jfun.yan.Map laziness = new jfun.yan.Map() {

        public Object map(Object f) {
            return new PooledFactory((Factory) f, new GlobalScope());
        }
    };

    private static Component toLazyFactory(Component cc) {
        return cc.factory().map(laziness);
    }

    private class ServiceBuilder {

        private final HashMap props = new HashMap();

        ServiceBuilder add(String key, Object val) {
            props.put(key, toComponent(val));
            return this;
        }

        Component getResult() throws IntrospectionException {
            final Component c = Components.bean(ServiceBean.class).withProperties(props).map(new jfun.yan.Map() {

                public Object map(Object bean) {
                    return ((ServiceBean) bean).buildService();
                }
            });
            if (getId() == null && isGloballyDefined()) {
                registerEagerInstantiation(c);
                return c;
            } else return c.cast(Service.class);
        }
    }

    private static Component toListComponent(List l) {
        final Component[] elems = new Component[l.size()];
        for (int i = 0; i < elems.length; i++) {
            elems[i] = NutsUtils.asComponent(l.get(i));
        }
        return Components.list(elems);
    }

    private static Component toListComponent(Object[] arr) {
        final Component[] elems = new Component[arr.length];
        for (int i = 0; i < elems.length; i++) {
            elems[i] = NutsUtils.asComponent(arr[i]);
        }
        return Components.list(elems);
    }

    private static Component toComponent(Object obj) {
        if (obj == null) {
            return Components.useDefault();
        } else if (obj instanceof List) {
            return toListComponent((List) obj);
        } else if (obj instanceof Creator[]) {
            return Components.list((Creator[]) obj);
        } else if (obj instanceof Object[]) {
            return toListComponent((Object[]) obj);
        } else {
            return NutsUtils.asComponent(obj);
        }
    }

    protected Component getProxyForService() {
        return getServant();
    }

    /**
   * Get the Component for the actual servant.
   */
    public Component getServant() {
        final Component cc = getComponent();
        if (cc != null) return cc;
        if (this.implementationClass != null) return Components.ctor(this.implementationClass);
        if (this.serviceInterface != null) return Components.ctor(this.serviceInterface);
        throw raise("at least one of \"component\", \"serviceClass\" or \"implementationClass\" has to be specified.");
    }
}
