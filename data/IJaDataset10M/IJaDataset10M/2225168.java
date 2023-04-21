package org.apache.myfaces.trinidadinternal.context;

import java.util.List;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import org.apache.myfaces.trinidad.context.RequestContext;
import org.apache.myfaces.trinidad.context.RequestContextFactory;
import org.apache.myfaces.trinidad.util.ClassLoaderUtils;
import org.apache.myfaces.trinidadinternal.config.ConfigParser;
import org.apache.myfaces.trinidadinternal.context.external.ServletExternalContext;

/**
 */
public class RequestContextFactoryImpl extends RequestContextFactory {

    public RequestContextFactoryImpl() {
    }

    @Override
    @Deprecated
    public RequestContext createContext(Object context, Object request) {
        return createContext(new ServletExternalContext((ServletContext) context, (ServletRequest) request, null));
    }

    @Override
    public RequestContext createContext(ExternalContext externalContext) {
        RequestContextImpl impl = new RequestContextImpl(_getBean(externalContext));
        impl.init(externalContext);
        return impl;
    }

    private RequestContextBean _getBean(ExternalContext externalContext) {
        if (_bean == null) {
            synchronized (this) {
                if (externalContext != null) {
                    _bean = ConfigParser.parseConfigFile(externalContext);
                } else {
                    _bean = new RequestContextBean();
                }
            }
        }
        return _bean;
    }

    private volatile RequestContextBean _bean;
}
