package org.eiichiro.gig.appengine;

import java.util.HashSet;
import java.util.Set;
import org.eiichiro.jazzmaster.service.Provider;
import org.eiichiro.jazzmaster.service.Service;
import com.google.appengine.api.capabilities.CapabilitiesService;
import com.google.appengine.api.capabilities.CapabilitiesServiceFactory;

/**
 * {@code AppEngineCapabilitiesService} is a service definition of App Engine's 
 * {@code CapabilitiesService} API.
 *
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class AppEngineCapabilitiesService extends Service {

    /** Returns {@code CapabilitiesService}. */
    @Override
    public Set<Class<?>> getInterfaces() {
        Set<Class<?>> interfaces = new HashSet<Class<?>>(1);
        interfaces.add(CapabilitiesService.class);
        return interfaces;
    }

    /**
	 * Just returns <code>null</code>.
	 * Implementation class is 
	 * <code>com.google.appengine.api.capabilities.CapabilitiesServiceImpl</code>.
	 * However, this method cannot return it because it is nonpublic.
	 * 
	 * @see org.eiichiro.jazzmaster.service.Service#getImplementation()
	 */
    @Override
    public Class<?> getImplementation() {
        return null;
    }

    /** Returns {@code Provider} for {@code CapabilitiesService}. */
    @Override
    @SuppressWarnings("unchecked")
    public Provider<CapabilitiesService> getProvider() {
        return new Provider<CapabilitiesService>() {

            @Override
            public CapabilitiesService get() {
                CapabilitiesService capabilitiesService = CapabilitiesServiceFactory.getCapabilitiesService();
                return capabilitiesService;
            }
        };
    }
}
