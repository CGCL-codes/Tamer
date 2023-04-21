package org.travelfusion.xmlclient.module;

import org.travelfusion.xmlclient.TfXClient;
import org.travelfusion.xmlclient.cache.CacheProvider;
import org.travelfusion.xmlclient.handler.HandlerPipelineFactory;
import org.travelfusion.xmlclient.handler.HandlerPipelineTypeLookup;
import org.travelfusion.xmlclient.handler.HandlerPipelineTypeRegistry;
import org.travelfusion.xmlclient.impl.BaseClientFactory;
import org.travelfusion.xmlclient.impl.TfXClientImpl;
import org.travelfusion.xmlclient.impl.handler.HandlerPipelineFactoryImpl;
import org.travelfusion.xmlclient.impl.handler.HandlerPipelineTypeRegistryAndLookupImpl;
import org.travelfusion.xmlclient.login.LoginManager;
import org.travelfusion.xmlclient.transport.TfXTransport;
import org.travelfusion.xmlclient.util.ResourceLoader;
import com.google.inject.AbstractModule;

/**
 * This is a convenience Guice module that configures a default 'standard' set of component implementations. These
 * components should be sufficient for most applications.
 * <p>
 * This module binds:
 * <ul>
 * <li>{@link TfXClient} to {@link TfXClientImpl}</li>
 * <li>{@link HandlerPipelineTypeRegistry} to {@link HandlerPipelineTypeRegistryAndLookupImpl}</li>
 * <li>{@link HandlerPipelineTypeLookup} to {@link HandlerPipelineTypeRegistryAndLookupImpl}</li>
 * <li>{@link HandlerPipelineFactory} to {@link HandlerPipelineFactoryImpl}</li>
 * </ul>
 * <p>
 * Note that is does not configure a {@link TfXTransport}, {@link LoginManager}, {@link CacheProvider} or
 * {@link ResourceLoader}. Application code should create a module to configure these as required, or rely on a suitable
 * {@link BaseClientFactory} implementation. {@link StandardOptionalComponentsModule} may also be used.
 * 
 * @see StandardOptionalComponentsModule
 * 
 * @author Jesse McLaughlin (nzjess@gmail.com)
 */
public class StandardComponentsModule extends AbstractModule {

    @Override
    public void configure() {
        bind(TfXClient.class).to(TfXClientImpl.class);
        bind(HandlerPipelineTypeRegistry.class).to(HandlerPipelineTypeRegistryAndLookupImpl.class);
        bind(HandlerPipelineTypeLookup.class).to(HandlerPipelineTypeRegistryAndLookupImpl.class);
        bind(HandlerPipelineFactory.class).to(HandlerPipelineFactoryImpl.class);
    }
}
