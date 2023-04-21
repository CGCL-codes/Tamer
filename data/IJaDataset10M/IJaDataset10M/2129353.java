package be.fedict.eid.idp.webapp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration.Dynamic;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.EntityResolver;
import be.fedict.eid.idp.entity.AttributeEntity;
import be.fedict.eid.idp.model.AttributeService;
import be.fedict.eid.idp.model.AttributeServiceManager;
import be.fedict.eid.idp.model.IdentityService;
import be.fedict.eid.idp.model.ProtocolServiceManager;
import be.fedict.eid.idp.model.exception.KeyStoreLoadException;
import be.fedict.eid.idp.spi.DefaultAttribute;
import be.fedict.eid.idp.spi.IdentityProviderConfigurationFactory;
import be.fedict.eid.idp.spi.IdentityProviderProtocolService;
import be.fedict.eid.idp.spi.attribute.IdentityProviderAttributeType;
import be.fedict.eid.idp.spi.protocol.EndpointType;
import be.fedict.eid.idp.spi.protocol.EndpointsType;
import be.fedict.eid.idp.spi.protocol.IdentityProviderProtocolType;
import be.fedict.eid.idp.spi.protocol.WSEndpointType;
import be.fedict.eid.idp.spi.protocol.WSEndpointsType;
import com.sun.xml.ws.api.BindingID;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.server.Container;
import com.sun.xml.ws.api.server.SDDocumentSource;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import com.sun.xml.ws.server.EndpointFactory;
import com.sun.xml.ws.transport.http.DeploymentDescriptorParser;
import com.sun.xml.ws.transport.http.ResourceLoader;
import com.sun.xml.ws.transport.http.servlet.JAXWSRIDeploymentProbeProvider;
import com.sun.xml.ws.transport.http.servlet.ServletAdapter;
import com.sun.xml.ws.transport.http.servlet.ServletAdapterList;
import com.sun.xml.ws.transport.http.servlet.WSServlet;
import com.sun.xml.ws.transport.http.servlet.WSServletDelegate;
import com.sun.xml.ws.util.xml.XmlUtil;

public class IdentityProviderServletContextListener implements ServletContextListener {

    private static final Log LOG = LogFactory.getLog(IdentityProviderServletContextListener.class);

    private static final String JAXWS_WSDL_DD_DIR = "WEB-INF/wsdl";

    @EJB
    private ProtocolServiceManager protocolServiceManager;

    @EJB
    private AttributeServiceManager attributeServiceManager;

    @EJB
    private IdentityService identityService;

    @EJB
    private AttributeService attributeService;

    private WSServletDelegate delegate;

    private List<ServletAdapter> adapters;

    private final JAXWSRIDeploymentProbeProvider probe = new JAXWSRIDeploymentProbeProvider();

    private Container container;

    private ClassLoader classLoader;

    private ResourceLoader loader;

    private final Map<String, SDDocumentSource> docs = new HashMap<String, SDDocumentSource>();

    private DeploymentDescriptorParser.AdapterFactory<ServletAdapter> adapterFactory;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        LOG.debug("contextInitialized");
        initIdentity();
        initAttributes();
        initAttributeServices();
        initProtocolServices(event);
        initIdentityProviderConfiguration(event);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        LOG.debug("contextDestroy");
        destroyWsEndpoints();
    }

    /**
	 * Initialize the default eID Attributes
	 */
    private void initAttributes() {
        for (DefaultAttribute defaultAttribute : DefaultAttribute.values()) {
            this.attributeService.saveAttribute(defaultAttribute.getName(), defaultAttribute.getDescription(), defaultAttribute.getUri());
        }
    }

    private void initIdentity() {
        if (this.identityService.isIdentityConfigured()) {
            try {
                this.identityService.reloadIdentity();
            } catch (KeyStoreLoadException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initIdentityProviderConfiguration(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        servletContext.setAttribute(IdentityProviderConfigurationFactory.IDENTITY_PROVIDER_CONFIGURATION_CONTEXT_ATTRIBUTE, this.identityService);
    }

    private void initAttributeServices() {
        List<IdentityProviderAttributeType> identityProviderAttributeTypes = this.attributeServiceManager.getAttributeServiceTypes();
        for (IdentityProviderAttributeType identityProviderAttributeType : identityProviderAttributeTypes) {
            String name = identityProviderAttributeType.getName();
            String description = identityProviderAttributeType.getDescription();
            String uri = identityProviderAttributeType.getURI();
            LOG.debug("initializating attribute service for: " + name);
            this.attributeService.saveAttribute(name, description, uri);
        }
    }

    private void initProtocolServices(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<IdentityProviderProtocolType> identityProviderProtocolTypes = this.protocolServiceManager.getProtocolServices();
        Map<String, String> wsEndpointsMap = new HashMap<String, String>();
        for (IdentityProviderProtocolType identityProviderProtocolType : identityProviderProtocolTypes) {
            String name = identityProviderProtocolType.getName();
            LOG.debug("initializing protocol service: " + name);
            EndpointsType endpoints = identityProviderProtocolType.getEndpoints();
            if (null != endpoints) {
                for (EndpointType endpoint : endpoints.getEndpoint()) {
                    String contextPath = endpoint.getContextPath();
                    String servletClassName = endpoint.getServletClass();
                    LOG.debug("initializing on context path: " + contextPath + " servlet " + servletClassName);
                    Class<?> servletClass;
                    try {
                        servletClass = classLoader.loadClass(servletClassName);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("could not load the servlet class: " + servletClassName);
                    }
                    if (!Servlet.class.isAssignableFrom(servletClass)) {
                        throw new RuntimeException("not a servlet class: " + servletClassName);
                    }
                    String servletName = name + contextPath;
                    LOG.debug("servlet name: " + servletName);
                    @SuppressWarnings("unchecked") Dynamic dynamic = servletContext.addServlet(servletName, (Class<? extends Servlet>) servletClass);
                    String urlPattern = IdentityProviderProtocolService.ENDPOINT_CONTEXT_PATH + contextPath;
                    dynamic.addMapping(urlPattern);
                }
            }
            WSEndpointsType wsEndpoints = identityProviderProtocolType.getWSEndpoints();
            if (null != wsEndpoints) {
                for (WSEndpointType wsEndpoint : wsEndpoints.getWSEndpoint()) {
                    String contextPath = wsEndpoint.getContextPath();
                    String wsImplClass = wsEndpoint.getWSImplClass();
                    LOG.debug("WS Endpoint: path=" + contextPath + " impl=" + wsImplClass);
                    wsEndpointsMap.put(contextPath, wsImplClass);
                }
            }
            LOG.debug("initializing protocol specific attribute URIs");
            IdentityProviderProtocolService protocolService = this.protocolServiceManager.getProtocolService(identityProviderProtocolType);
            for (AttributeEntity attribute : this.attributeService.listAttributes()) {
                this.attributeService.createAttributeUri(protocolService.getId(), attribute.getUri(), protocolService.findAttributeUri(attribute.getUri()));
            }
        }
        if (!wsEndpointsMap.isEmpty()) {
            Class<?> wsServletClass;
            try {
                wsServletClass = classLoader.loadClass("com.sun.xml.ws.transport.http.servlet.WSServlet");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            @SuppressWarnings("unchecked") Dynamic dynamic = servletContext.addServlet("WSServlet", (Class<? extends Servlet>) wsServletClass);
            String urlPattern = IdentityProviderProtocolService.WS_ENDPOINT_CONTEXT_PATH + "/*";
            dynamic.setLoadOnStartup(1);
            dynamic.addMapping(urlPattern);
            initWsEndpoints(servletContext, wsEndpointsMap);
        }
    }

    /**
	 * Initialize the WS endpoints
	 * 
	 * @param context
	 *            servlet context
	 * @param wsEndpointsMap
	 *            endpoints map
	 */
    private void initWsEndpoints(ServletContext context, Map<String, String> wsEndpointsMap) {
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = getClass().getClassLoader();
            }
            loader = new ServletResourceLoader(context);
            container = createContainer(context);
            adapterFactory = new ServletAdapterList(context);
            adapters = getAdapters(wsEndpointsMap);
            delegate = createDelegate(context);
            context.setAttribute(WSServlet.JAXWS_RI_RUNTIME_INFO, delegate);
            for (ServletAdapter adapter : adapters) {
                probe.deploy(adapter);
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }
    }

    private List<ServletAdapter> getAdapters(Map<String, String> wsEndpointsMap) throws ClassNotFoundException {
        List<ServletAdapter> adapters = new ArrayList<ServletAdapter>();
        for (Map.Entry<String, String> wsEndpoint : wsEndpointsMap.entrySet()) {
            adapters.add(getAdapter(wsEndpoint.getKey(), wsEndpoint.getValue()));
        }
        return adapters;
    }

    private ServletAdapter getAdapter(String urlPattern, String wsImplClassName) throws ClassNotFoundException {
        Class<?> implementorClass = Class.forName(wsImplClassName, true, classLoader);
        EndpointFactory.verifyImplementorClass(implementorClass);
        SDDocumentSource primaryWSDL = getPrimaryWSDL(implementorClass);
        QName serviceName = EndpointFactory.getDefaultServiceName(implementorClass);
        QName portName = EndpointFactory.getDefaultPortName(serviceName, implementorClass);
        WSBinding binding = createBinding(implementorClass);
        WSEndpoint<?> endpoint = WSEndpoint.create(implementorClass, true, null, serviceName, portName, container, binding, primaryWSDL, docs.values(), createEntityResolver(), false);
        if (null == endpoint) {
            throw new RuntimeException("Endpoint is null.");
        }
        return adapterFactory.createAdapter(urlPattern, IdentityProviderProtocolService.WS_ENDPOINT_CONTEXT_PATH + urlPattern, endpoint);
    }

    /**
	 * Checks the deployment descriptor or {@link @WebServiceProvider}
	 * annotation to see if it points to any WSDL. If so, returns the
	 * {@link com.sun.xml.ws.api.server.SDDocumentSource}.
	 * 
	 * @param implementorClass
	 *            WS impl class name
	 * @return The pointed WSDL, if any. Otherwise null.
	 */
    private SDDocumentSource getPrimaryWSDL(Class<?> implementorClass) {
        String wsdlFile = EndpointFactory.getWsdlLocation(implementorClass);
        if (wsdlFile != null) {
            if (!wsdlFile.startsWith(JAXWS_WSDL_DD_DIR)) {
                LOG.warn("Ignoring wrong wsdl=" + wsdlFile + ". It should start with " + JAXWS_WSDL_DD_DIR + ". Going to generate and publish a new WSDL.");
                return null;
            }
            URL wsdl;
            try {
                wsdl = loader.getResource('/' + wsdlFile);
            } catch (MalformedURLException e) {
                LOG.error(e);
                throw new RuntimeException(e);
            }
            if (wsdl == null) {
                LOG.error("No WSDL found");
                throw new RuntimeException("No WSDL found");
            }
            SDDocumentSource docInfo = docs.get(wsdl.toExternalForm());
            assert docInfo != null;
            return docInfo;
        }
        return null;
    }

    private static WSBinding createBinding(Class implClass) {
        WebServiceFeatureList features = new WebServiceFeatureList();
        BindingID bindingID = BindingID.parse(implClass);
        features.addAll(bindingID.createBuiltinFeatureList());
        return bindingID.createBinding(features.toArray());
    }

    /**
	 * Creates {@link com.sun.xml.ws.api.server.Container} implementation that
	 * hosts the JAX-WS endpoint.
	 * 
	 * @param context
	 *            servlet context
	 * @return container container holding endpoint
	 */
    protected Container createContainer(ServletContext context) {
        return new ServletContainer(context);
    }

    /**
	 * Creates {@link WSServletDelegate} that does the real work.
	 * 
	 * @param context
	 *            servlet context
	 * @return WS servlet delegate.
	 */
    protected WSServletDelegate createDelegate(ServletContext context) {
        return new WSServletDelegate(adapters, context);
    }

    private EntityResolver createEntityResolver() {
        try {
            return XmlUtil.createEntityResolver(loader.getCatalogFile());
        } catch (MalformedURLException e) {
            throw new WebServiceException(e);
        }
    }

    private void destroyWsEndpoints() {
        if (delegate != null) {
            delegate.destroy();
        }
        if (adapters != null) {
            for (ServletAdapter a : adapters) {
                try {
                    a.getEndpoint().dispose();
                } catch (Throwable e) {
                    LOG.error(e.getMessage(), e);
                }
                probe.undeploy(a);
            }
        }
    }
}
