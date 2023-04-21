package com.capgemini.archetype.webservices;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.ParameterizedType;
import javax.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.ConduitInitiatorManager;
import org.apache.cxf.transport.DestinationFactoryManager;
import org.apache.cxf.transport.local.LocalTransportFactory;
import org.junit.Before;

/**
 * @author ndeloof
 */
public abstract class AbstractWSTestCase<T> {

    @Before
    public final void setUp() throws Exception {
        Bus bus = BusFactory.getDefaultBus();
        LocalTransportFactory local = new LocalTransportFactory();
        DestinationFactoryManager dfm = bus.getExtension(DestinationFactoryManager.class);
        dfm.registerDestinationFactory("http://schemas.xmlsoap.org/soap/http", local);
        dfm.registerDestinationFactory("http://schemas.xmlsoap.org/wsdl/soap/http", local);
        dfm.registerDestinationFactory("http://cxf.apache.org/bindings/xformat", local);
        dfm.registerDestinationFactory("http://cxf.apache.org/transports/local", local);
        ConduitInitiatorManager extension = bus.getExtension(ConduitInitiatorManager.class);
        extension.registerConduitInitiator("http://cxf.apache.org/transports/local", local);
        extension.registerConduitInitiator("http://schemas.xmlsoap.org/wsdl/soap/http", local);
        extension.registerConduitInitiator("http://schemas.xmlsoap.org/soap/http", local);
        extension.registerConduitInitiator("http://cxf.apache.org/bindings/xformat", local);
        bus.getInInterceptors().add(new LoggingInInterceptor());
        bus.getInInterceptors().add(new LoggingOutInterceptor());
        bus.getInFaultInterceptors().add(new LoggingOutInterceptor());
        Endpoint.publish("local://service", getServiceBean());
    }

    /**
     * Build the server-side service implementation to be used in test
	 * with mock/stubs injected as business services.
     */
    protected abstract T getServiceBean();

    public Class<T> getServiceInterface() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        assertEquals("The test class MUST be parametrized with service interface", 1, type.getActualTypeArguments().length);
        return (Class<T>) type.getActualTypeArguments()[0];
    }

    protected T getClient() {
        JaxWsProxyFactoryBean cf = new JaxWsProxyFactoryBean();
        cf.setAddress("local://service");
        cf.setServiceClass(getServiceInterface());
        return (T) cf.create();
    }
}
