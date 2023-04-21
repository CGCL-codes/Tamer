package org.apache.myfaces.trinidadinternal.skins;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.ExternalContext;
import org.apache.myfaces.trinidad.config.Configurator;
import org.apache.myfaces.trinidad.context.RequestContext;
import org.apache.myfaces.trinidad.context.RequestContextFactory;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;
import org.apache.myfaces.trinidad.skin.SkinFactory;
import org.apache.myfaces.trinidad.util.ClassLoaderUtils;
import org.apache.myfaces.trinidadinternal.context.RequestContextFactoryImpl;
import org.apache.myfaces.trinidadinternal.skin.SkinFactoryImpl;
import org.apache.myfaces.trinidadinternal.skin.SkinUtils;
import org.apache.myfaces.trinidadinternal.util.ExternalContextUtils;

public class GlobalSkinConfiguratorImpl extends Configurator {

    /**
     * Returns a GlobalConfigurator instance for the current context's class loader. The
     * GlobalConfigurator is responsible for enforcing the contract on the other methods of this
     * class. This means that if {@link #init(ExternalContext)} is called multiple times, the global
     * configurator will call all subordinate configurators only once.
     *
     * Likewise, the GlobalConfigurator will return exceptions when the contract is expressly violated
     * (like if {@link #getExternalContext(ExternalContext)} is called before a {{@link #beginRequest(ExternalContext)}.
     *
     * @return a GlobalConfigurator or <code>null</code> is one was unable to be obtained.
     */
    public static final GlobalSkinConfiguratorImpl getInstance() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            _LOG.severe("CANNOT_FIND_CONTEXT_CLASS_LOADER");
        } else {
            synchronized (_CONFIGURATORS) {
                GlobalSkinConfiguratorImpl config = _CONFIGURATORS.get(loader);
                if (config == null) {
                    try {
                        config = new GlobalSkinConfiguratorImpl();
                        _CONFIGURATORS.put(loader, config);
                    } catch (final RuntimeException e) {
                        _LOG.severe(e);
                        throw e;
                    }
                    _LOG.fine("GlobalConfigurator has been created.");
                }
                return config;
            }
        }
        return null;
    }

    /**
     * Returns true if the request has not been stated for the current "virtual"
     * request.  In the servlet environment this will be true after
     * {@link #beginRequest(ExternalContext)} is executed and before
     * {@link #endRequest(ExternalContext)} is executed.  This will generally
     * happen once per request.  In the Portlet Environment, the request must be
     * be started and ended at the beginning and end of both the actionRequest
     * and the RenderRequest.
     *
     * @param ec
     * @return
     */
    public static boolean isRequestStarted(final ExternalContext ec) {
        return (RequestType.getType(ec) != null);
    }

    /**
     * Returns "true" if the services should be considered enabled or disabled.
     *
     * @param ec
     * @return
     */
    private static final boolean _isDisabled(final ExternalContext ec) {
        final Boolean inRequest = (Boolean) ec.getRequestMap().get(_IN_REQUEST);
        if (inRequest == null) {
            return isConfiguratorServiceDisabled(ec);
        } else {
            final boolean disabled = inRequest.booleanValue();
            if (disabled != isConfiguratorServiceDisabled(ec)) {
                _LOG.warning("Configurator services were disabled after beginRequest was executed.  Cannot disable configurator services");
            }
            return disabled;
        }
    }

    /**
     * Private default constructor. Right now this class is not serializable. If serialization is
     * required, we may wish to make this public. We really don't want people using this though.
     */
    private GlobalSkinConfiguratorImpl() {
    }

    /**
     * Executes the beginRequest methods of all of the configurator services. This method will also
     * initizlize the configurator if it has not already been initialized, so there may be no need to
     * call the {@link #init(ExternalContext)} method directly.
     *
     * This method also ensures that the requestContext is attached before the beginRequest methods
     * are called, so there is no reason to initialize the request context before calling this method.
     * In portal environments, it is important to execute this method once for each Portlet action and
     * render request so that the requestContext may be properly initialized even though the
     * underlying services will be called only once per physical request.
     *
     * @param externalContext
     *          the externalContext to use to begin the request.
     *
     * @see org.apache.myfaces.trinidad.config.Configurator#beginRequest(javax.faces.context.ExternalContext)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void beginRequest(final ExternalContext externalContext) {
        assert externalContext != null;
        if (RequestType.getType(externalContext) == null) {
            RequestType.setType(externalContext);
            if (!_isDisabled(externalContext)) {
                if (!_initialized) {
                    init(externalContext);
                }
                _attachRequestContext(externalContext);
                if (externalContext.getRequestMap().get(_IN_REQUEST) == null) {
                    _startConfiguratorServiceRequest(externalContext);
                }
            } else {
                _LOG.fine("GlobalConfigurator: Configurators have been disabled for this request.");
            }
        } else if (!RequestType.isCorrectType(externalContext)) {
            throw new IllegalStateException("The previous action request was not ended.");
        } else {
            _LOG.fine("BeginRequest called multiple times for this request");
        }
    }

    /**
     * Cleans up the current configurator. This will execute the destroy method on all of the
     * configurator services. Generally this will be called by Trinidad's context listener when the
     * context is destroyed, but it may be used manually to allow the configurator to be
     * re-initialized.
     *
     * Calling this method while the configurator is not initialized will not re-execute the destroy
     * methods on the services.
     *
     * @see org.apache.myfaces.trinidad.config.Configurator#destroy()
     */
    @Override
    public void destroy() {
        if (_initialized) {
            for (final Configurator config : _services) {
                try {
                    config.destroy();
                } catch (final Throwable t) {
                    _LOG.severe(t);
                }
            }
            _services = null;
            _initialized = false;
        }
    }

    /**
     * Ends the currently begun request. It is important to note that this should be executed only
     * once per physical request.
     *
     * @see org.apache.myfaces.trinidad.config.Configurator#endRequest(javax.faces.context.ExternalContext)
     */
    @Override
    public void endRequest(final ExternalContext externalContext) {
        if (RequestType.getType(externalContext) != null) {
            if (!_isDisabled(externalContext)) {
                final RequestType type = RequestType.getType(externalContext);
                if (type != RequestType.PORTAL_ACTION) {
                    _endConfiguratorServiceRequest(externalContext);
                }
                _releaseRequestContext(externalContext);
            }
            RequestType.clearType(externalContext);
        }
    }

    /**
     * Returns an externalContext for this configurator and all of the configurator services. If this
     * method is executed before {@link #beginRequest(ExternalContext)} then this method will call
     * beginRequest(). It is important to note, however, that even though beginRequest does not need
     * to be explicitly called, {{@link #endRequest(ExternalContext)} does need to be called when the
     * request has completed or the contract to the configurators will be broken.
     *
     * @param externalContext
     *          the ExternalContext object that should be wrapped.
     *
     * @return a decorated ExternalContext object
     */
    @Override
    public ExternalContext getExternalContext(ExternalContext externalContext) {
        if (RequestType.getType(externalContext) == null) {
            beginRequest(externalContext);
        }
        if (!_isDisabled(externalContext)) {
            for (final Configurator config : _services) {
                externalContext = config.getExternalContext(externalContext);
            }
        }
        return externalContext;
    }

    /**
     * Initializes the global configurator and the configurator services. This method need not be
     * called directly as it will be called from {@link #beginRequest(ExternalContext)} if needed. It
     * is also possible to execute this method more then once, although if initialization has already
     * happened then a call to this method will not do anything. To re-initialize this class, call
     * {@link #destroy()} first and then call this method.
     *
     * @param externalContext
     *          the externalContext needed to initialize this class
     *
     * @see org.apache.myfaces.trinidad.config.Configurator#init(javax.faces.context.ExternalContext)
     */
    @Override
    public void init(final ExternalContext externalContext) {
        assert externalContext != null;
        if (!_initialized) {
            _services = ClassLoaderUtils.getServices(Configurator.class.getName());
            if (RequestContextFactory.getFactory() == null) {
                RequestContextFactory.setFactory(new RequestContextFactoryImpl());
            }
            if (SkinFactory.getFactory() == null) {
                SkinFactory.setFactory(new SkinFactoryImpl());
            }
            SkinUtils.registerBaseSkins();
            for (final Configurator config : _services) {
                config.init(externalContext);
            }
            SkinUtils.registerSkinExtensions(externalContext);
            _initialized = true;
        } else {
            _LOG.warning("CONFIGURATOR_SERVICES_INITIALIZED");
        }
    }

    /**
     * @param externalContext
     * @return
     */
    @SuppressWarnings("unchecked")
    private void _attachRequestContext(final ExternalContext externalContext) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (context != null) {
            if (_LOG.isWarning()) {
                _LOG.warning("REQUESTCONTEXT_NOT_PROPERLY_RELEASED");
            }
            context.release();
        }
        final Object cachedRequestContext = externalContext.getRequestMap().get(_REQUEST_CONTEXT);
        if (cachedRequestContext instanceof RequestContext) {
            context = (RequestContext) cachedRequestContext;
            context.attach();
        } else {
            final RequestContextFactory factory = RequestContextFactory.getFactory();
            assert factory != null;
            context = factory.createContext(externalContext);
            externalContext.getRequestMap().put(_REQUEST_CONTEXT, context);
        }
        assert RequestContext.getCurrentInstance() == context;
    }

    private void _releaseRequestContext(final ExternalContext ec) {
        if (RequestType.getType(ec) != RequestType.PORTAL_ACTION) {
            ec.getRequestMap().remove(_REQUEST_CONTEXT);
        }
        final RequestContext context = RequestContext.getCurrentInstance();
        if (context != null) {
            context.release();
            assert RequestContext.getCurrentInstance() == null;
        }
    }

    private void _endConfiguratorServiceRequest(final ExternalContext ec) {
        ec.getRequestMap().remove(_IN_REQUEST);
        for (final Configurator config : _services) {
            config.endRequest(ec);
        }
    }

    @SuppressWarnings("unchecked")
    private void _startConfiguratorServiceRequest(final ExternalContext ec) {
        final boolean disabled = isConfiguratorServiceDisabled(ec);
        ec.getRequestMap().put(_IN_REQUEST, disabled);
        for (final Configurator config : _services) {
            config.beginRequest(ec);
        }
    }

    private boolean _initialized;

    private List<Configurator> _services;

    private static final Map<ClassLoader, GlobalSkinConfiguratorImpl> _CONFIGURATORS = new HashMap<ClassLoader, GlobalSkinConfiguratorImpl>();

    private static final String _IN_REQUEST = GlobalSkinConfiguratorImpl.class.getName() + ".IN_REQUEST";

    private static final String _REQUEST_CONTEXT = GlobalSkinConfiguratorImpl.class.getName() + ".REQUEST_CONTEXT";

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(GlobalSkinConfiguratorImpl.class);

    private enum RequestType {

        PORTAL_ACTION, PORTAL_RENDER, SERVLET;

        public static void clearType(final ExternalContext ec) {
            ec.getRequestMap().remove(_REQUEST_TYPE);
        }

        public static RequestType getType(final ExternalContext ec) {
            return (RequestType) ec.getRequestMap().get(_REQUEST_TYPE);
        }

        public static boolean isCorrectType(final ExternalContext ec) {
            return _findType(ec) == getType(ec);
        }

        @SuppressWarnings("unchecked")
        public static void setType(final ExternalContext ec) {
            ec.getRequestMap().put(_REQUEST_TYPE, _findType(ec));
        }

        private static final RequestType _findType(final ExternalContext ec) {
            if (ExternalContextUtils.isPortlet(ec)) {
                if (ExternalContextUtils.isAction(ec)) {
                    return PORTAL_ACTION;
                } else {
                    return PORTAL_RENDER;
                }
            } else {
                return SERVLET;
            }
        }

        private static final String _REQUEST_TYPE = GlobalSkinConfiguratorImpl.class.getName() + ".REQUEST_TYPE";
    }
}
