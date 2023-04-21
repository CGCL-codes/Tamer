package org.waveprotocol.box.server;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.waveprotocol.box.server.authentication.SessionManager;
import org.waveprotocol.box.server.authentication.SessionManagerImpl;
import org.waveprotocol.box.server.rpc.ProtoSerializer;
import org.waveprotocol.box.server.rpc.ServerRpcProvider;
import org.waveprotocol.box.server.waveserver.WaveServerImpl;
import org.waveprotocol.box.server.waveserver.WaveServerModule;
import org.waveprotocol.wave.federation.FederationHostBridge;
import org.waveprotocol.wave.federation.FederationRemoteBridge;
import org.waveprotocol.wave.federation.WaveletFederationListener;
import org.waveprotocol.wave.federation.WaveletFederationProvider;
import org.waveprotocol.wave.model.id.IdGenerator;
import org.waveprotocol.wave.model.id.IdGeneratorImpl;
import org.waveprotocol.wave.model.id.IdGeneratorImpl.Seed;
import org.waveprotocol.wave.model.id.TokenGenerator;
import org.waveprotocol.wave.model.id.TokenGeneratorImpl;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import javax.security.auth.login.Configuration;

/**
 * Guice Module for the prototype Server.
 *
 *
 */
public class ServerModule extends AbstractModule {

    private final boolean enableFederation;

    public ServerModule(boolean enableFederation) {
        this.enableFederation = enableFederation;
    }

    @Override
    protected void configure() {
        bind(WaveletFederationListener.Factory.class).annotatedWith(FederationRemoteBridge.class).to(WaveServerImpl.class);
        bind(WaveletFederationProvider.class).annotatedWith(FederationHostBridge.class).to(WaveServerImpl.class);
        install(new WaveServerModule(enableFederation));
        TypeLiteral<List<String>> certs = new TypeLiteral<List<String>>() {
        };
        bind(certs).annotatedWith(Names.named("certs")).toInstance(Arrays.<String>asList());
        bind(ProtoSerializer.class).in(Singleton.class);
        bind(Configuration.class).toInstance(Configuration.getConfiguration());
        bind(SessionManager.class).to(SessionManagerImpl.class).in(Singleton.class);
        bind(org.eclipse.jetty.server.SessionManager.class).to(HashSessionManager.class).in(Singleton.class);
        bind(ServerRpcProvider.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    @Inject
    public IdGenerator provideIdGenerator(@Named(CoreSettings.WAVE_SERVER_DOMAIN) String domain, Seed seed) {
        return new IdGeneratorImpl(domain, seed);
    }

    @Provides
    @Singleton
    public SecureRandom provideSecureRandom() {
        return new SecureRandom();
    }

    @Provides
    @Singleton
    @Inject
    public TokenGenerator provideTokenGenerator(SecureRandom random) {
        return new TokenGeneratorImpl(random);
    }

    @Provides
    @Singleton
    @Inject
    public Seed provideSeed(final SecureRandom random) {
        return new Seed() {

            @Override
            public String get() {
                return Long.toString(Math.abs(random.nextLong()), 36);
            }
        };
    }
}
