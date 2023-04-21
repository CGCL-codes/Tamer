package org.apache.shindig.social.core.config;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import junit.framework.TestCase;
import org.apache.shindig.auth.AuthenticationHandler;
import org.apache.shindig.common.PropertiesModule;
import org.apache.shindig.social.core.oauth.AuthenticationHandlerProvider;
import org.apache.shindig.social.opensocial.oauth.OAuthDataStore;
import org.easymock.EasyMock;
import java.util.List;

public class SocialApiGuiceModuleTest extends TestCase {

    private Injector injector;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injector = Guice.createInjector(new SocialApiGuiceModule(), new PropertiesModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(OAuthDataStore.class).toInstance(EasyMock.createMock(OAuthDataStore.class));
            }
        });
    }

    /**
   * Test default auth handler injection
   */
    public void testAuthHandler() {
        injector.getInstance(AuthenticationHandlerProvider.class).get();
        AuthenticationHandlerProvider provider = injector.getInstance(AuthenticationHandlerProvider.class);
        assertEquals(3, provider.get().size());
        List<AuthenticationHandler> handlers = injector.getInstance(Key.get(new TypeLiteral<List<AuthenticationHandler>>() {
        }));
        assertEquals(3, handlers.size());
    }
}
