package org.eiichiro.gig.appengine;

import java.util.HashSet;
import java.util.Set;
import org.eiichiro.jazzmaster.service.Provider;
import org.eiichiro.jazzmaster.service.Service;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

/**
 * {@code AppEngineURLFetchService} is a service definition of App Engine's 
 * low-level {@code URLFetchService} API.
 *
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class AppEngineURLFetchService extends Service {

    /** Returns {@code URLFetchService}. */
    @Override
    public Set<Class<?>> getInterfaces() {
        Set<Class<?>> interfaces = new HashSet<Class<?>>(1);
        interfaces.add(URLFetchService.class);
        return interfaces;
    }

    /**
	 * Just returns <code>null</code>.
	 * Implementation class is 
	 * <code>com.google.appengine.api.urlfetch.URLFetchServiceImpl</code>.
	 * However, this method cannot return it because it is nonpublic.
	 * 
	 * @see org.eiichiro.jazzmaster.service.Service#getImplementation()
	 */
    @Override
    public Class<?> getImplementation() {
        return null;
    }

    /** Returns {@code Provider} for {@code URLFetchService}. */
    @Override
    @SuppressWarnings("unchecked")
    public Provider<URLFetchService> getProvider() {
        return new Provider<URLFetchService>() {

            @Override
            public URLFetchService get() {
                URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();
                return urlFetchService;
            }
        };
    }
}
