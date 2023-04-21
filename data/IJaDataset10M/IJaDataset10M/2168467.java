package de.abg.jreichert.serviceqos.calc.adder.serviceimpl;

import org.jboss.wsf.spi.annotation.WebContext;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Implementation of Adder.
 */
@Stateless(name = "adder")
@WebService(endpointInterface = "de.abg.jreichert.serviceqos.calc.adder.serviceapi.AdderEndpoint", serviceName = "Adder")
@WebContext(contextRoot = "/calculator", urlPattern = "/Adder/WebDelegateEndPoint")
@Interceptors({ org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContextStoreInterceptor.class, org.fornax.cartridges.sculptor.framework.errorhandling.ErrorHandlingInterceptor.class })
public class AdderBean extends AdderBeanBase {

    private static final long serialVersionUID = 1L;

    public AdderBean() {
    }

    @Interceptors({ org.fornax.cartridges.sculptor.framework.errorhandling.JpaFlushEagerInterceptor.class })
    @WebMethod
    public int add(int a, int b) {
        return a + b;
    }
}
