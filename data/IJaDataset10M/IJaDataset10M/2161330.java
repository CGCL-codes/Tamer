package org.avis.router;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.avis.common.ElvinURI;
import org.avis.io.Net;
import org.avis.util.Filter;
import static java.lang.Runtime.getRuntime;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.mina.core.buffer.IoBuffer.setUseDirectBuffer;
import static org.avis.io.TLS.toPassphrase;
import static org.avis.logging.Log.diagnostic;
import static org.avis.logging.Log.warn;

/**
 * Handles management of network I/O, including pooling of I/O
 * processors, acceptor/connector setup, security (SSL
 * authentication), and common filter creation.
 * 
 * @author Matthew Phillips
 */
public class IoManager {

    private SimpleIoProcessorPool<NioSession> processorPool;

    private ExecutorService ioExecutor;

    private OrderedThreadPoolExecutor filterExecutor;

    private String keystorePassphrase;

    private KeyStore keystore;

    private Map<ElvinURI, Collection<NioSocketAcceptor>> uriToAcceptors;

    private Map<ElvinURI, NioSocketConnector> uriToConnectors;

    private LowMemoryProtectionFilter lowMemoryFilter;

    /**
   * Create a new instance.
   * 
   * @param keystoreUri The URI for the SSL keystore. May be null, in
   *          which case attempting to use a secure Elvin URI will
   *          fail.
   * @param keystorePassphrase The keystore passphrase.
   * @param maxFrameSize The maximum size of a frame. Used to help
   *          manage low memory checking.
   * @param lowMemoryProtectionMinFreeMemory Min free memory for 
   *          LowMemoryProtectionFilter. Use 0 for no protection.
   * @param useDirectBuffers True if MINA should use direct IO
   *          buffers.
   */
    public IoManager(URI keystoreUri, String keystorePassphrase, int maxFrameSize, int lowMemoryProtectionMinFreeMemory, boolean useDirectBuffers) throws IOException {
        this.uriToAcceptors = new HashMap<ElvinURI, Collection<NioSocketAcceptor>>();
        this.uriToConnectors = new HashMap<ElvinURI, NioSocketConnector>();
        this.keystorePassphrase = keystorePassphrase;
        if (keystoreUri != null) this.keystore = loadKeystore(keystoreUri);
        this.ioExecutor = newCachedThreadPool();
        this.filterExecutor = new OrderedThreadPoolExecutor(0, 16, 20, SECONDS);
        this.processorPool = new SimpleIoProcessorPool<NioSession>(NioProcessor.class, ioExecutor, getRuntime().availableProcessors() + 1);
        this.lowMemoryFilter = new LowMemoryProtectionFilter(this, lowMemoryProtectionMinFreeMemory);
        setUseDirectBuffer(useDirectBuffers);
    }

    public void close() {
        lowMemoryFilter.shutdown();
        for (Collection<NioSocketAcceptor> acceptors : uriToAcceptors.values()) {
            for (NioSocketAcceptor acceptor : acceptors) acceptor.dispose();
        }
        for (NioSocketConnector connector : uriToConnectors.values()) connector.dispose();
        ioExecutor.shutdown();
        try {
            if (!ioExecutor.awaitTermination(15, SECONDS)) warn("Failed to cleanly shut down thread pool", this);
        } catch (InterruptedException ex) {
            diagnostic("Interrupted while waiting for shutdown", this, ex);
        }
    }

    public IoFilter createThreadPoolFilter() {
        return new ExecutorFilter(filterExecutor);
    }

    public NioSocketAcceptor createAcceptor() {
        NioSocketAcceptor acceptor = new NioSocketAcceptor(processorPool);
        acceptor.setReuseAddress(true);
        return acceptor;
    }

    /**
   * Bind to a set of URI's to network addresses.
   * 
   * @param uris The URI's to bind.
   * @param handler The IO handler.
   * @param filters The basic IO filters.
   * @param authenticationRequiredHosts Hosts matched by this filter
   *          must be successfully authenticated via TLS or will be
   *          refused access.
   * 
   * @throws IOException if an error occurred during binding.
   */
    public void bind(Set<? extends ElvinURI> uris, IoHandler handler, DefaultIoFilterChainBuilder filters, Filter<InetAddress> authenticationRequiredHosts) throws IOException {
        DefaultIoFilterChainBuilder secureFilters = null;
        for (ElvinURI uri : uris) {
            Set<InetSocketAddress> addresses = Net.addressesFor(uri);
            Collection<NioSocketAcceptor> uriAcceptors = new ArrayList<NioSocketAcceptor>(addresses.size());
            for (InetSocketAddress address : addresses) {
                NioSocketAcceptor acceptor = createAcceptor();
                if (uri.isSecure()) {
                    if (secureFilters == null) {
                        secureFilters = createSecureFilters(filters, authenticationRequiredHosts, false);
                    }
                    acceptor.setFilterChainBuilder(secureFilters);
                } else {
                    acceptor.setFilterChainBuilder(filters);
                }
                if (!acceptor.getFilterChain().contains("memory")) {
                    acceptor.getFilterChain().addLast("memory", lowMemoryFilter);
                    acceptor.getFilterChain().addLast("stats", StatsFilter.INSTANCE);
                }
                acceptor.setCloseOnDeactivation(false);
                acceptor.setHandler(handler);
                acceptor.setDefaultLocalAddress(address);
                acceptor.bind(address);
                uriAcceptors.add(acceptor);
            }
            uriToAcceptors.put(uri, uriAcceptors);
        }
    }

    /**
   * Reverse the effect of bind ().
   */
    public void unbind(Collection<? extends ElvinURI> uris) {
        for (ElvinURI uri : uris) {
            if (uriToAcceptors.containsKey(uri)) {
                for (NioSocketAcceptor acceptor : uriToAcceptors.get(uri)) acceptor.dispose();
            }
        }
        for (ElvinURI uri : uris) uriToAcceptors.remove(uri);
    }

    /**
   * Add the filters used for TLS-secured links.
   * 
   * @param filters The set of filters to add to.
   * @param authRequired The hosts for which authentication is
   *                required.
   * @param clientMode True if the TLS filter should be in client mode.
   */
    public void addSecureFilters(DefaultIoFilterChainBuilder filters, Filter<InetAddress> authRequired, boolean clientMode) {
        filters.addFirst("security", new SecurityFilter(keystore, keystorePassphrase, authRequired, clientMode));
    }

    /**
   * Create the filters used for TLS-secured links.
   * 
   * @param commonFilters The initial set of filters to add to.
   * @param authRequired The hosts for which authentication is
   *                required.
   * @param clientMode True if the TLS filter should be in client mode.
   * 
   * @return The new filter set.
   */
    private DefaultIoFilterChainBuilder createSecureFilters(DefaultIoFilterChainBuilder commonFilters, Filter<InetAddress> authRequired, boolean clientMode) {
        DefaultIoFilterChainBuilder secureFilters = new DefaultIoFilterChainBuilder(commonFilters);
        addSecureFilters(secureFilters, authRequired, clientMode);
        return secureFilters;
    }

    /**
   * Create the filters used for standard, non secured links.
   * 
   * @param commonFilters The initial set of filters to add to.
   * @param authRequired The hosts for which authentication is
   *                required. For standard link these hosts are denied
   *                connection.
   * @return The new filter set.
   */
    private DefaultIoFilterChainBuilder createStandardFilters(DefaultIoFilterChainBuilder commonFilters, Filter<InetAddress> authRequired) {
        if (authRequired == Filter.MATCH_NONE) {
            return commonFilters;
        } else {
            DefaultIoFilterChainBuilder blacklistFilters = new DefaultIoFilterChainBuilder(commonFilters);
            blacklistFilters.addFirst("blacklist", new BlacklistFilter(authRequired));
            return blacklistFilters;
        }
    }

    /**
   * Lazy load the SSL keystore.
   */
    private KeyStore loadKeystore(URI keystoreUri) throws IOException {
        if (keystoreUri == null) {
            throw new IOException("Cannot use TLS without a keystore: " + "see TLS.Keystore configuration option");
        }
        InputStream keystoreStream = keystoreUri.toURL().openStream();
        try {
            KeyStore newKeystore = KeyStore.getInstance("JKS");
            newKeystore.load(keystoreStream, toPassphrase(keystorePassphrase));
            return newKeystore;
        } catch (GeneralSecurityException ex) {
            throw new IOException("Failed to load TLS keystore: " + ex.getMessage());
        } finally {
            keystoreStream.close();
        }
    }

    /**
   * Get the addresses for all URI's bound with bind ().
   */
    public Set<InetSocketAddress> addressesFor(Collection<? extends ElvinURI> uris) {
        Set<InetSocketAddress> addresses = new TreeSet<InetSocketAddress>();
        for (ElvinURI uri : uris) {
            if (uriToAcceptors.containsKey(uri)) {
                for (NioSocketAcceptor acceptor : uriToAcceptors.get(uri)) addresses.add(acceptor.getLocalAddress());
            }
        }
        return addresses;
    }

    /**
   * All acceptors created with bind ().
   */
    public Collection<IoAcceptor> acceptors() {
        ArrayList<IoAcceptor> acceptors = new ArrayList<IoAcceptor>();
        for (Collection<NioSocketAcceptor> values : uriToAcceptors.values()) acceptors.addAll(values);
        return acceptors;
    }

    /**
   * All acceptors created for the given URI's.
   */
    public Collection<IoAcceptor> acceptorsFor(Set<? extends ElvinURI> uris) {
        ArrayList<IoAcceptor> acceptors = new ArrayList<IoAcceptor>();
        for (ElvinURI uri : uris) {
            Collection<NioSocketAcceptor> uriAcceptors = uriToAcceptors.get(uri);
            if (uriAcceptors != null) acceptors.addAll(uriAcceptors);
        }
        return acceptors;
    }

    /**
   * All sessions created by acceptors and/or connectors. This returns
   * a mutable copy.
   */
    public List<IoSession> sessions() {
        ArrayList<IoSession> sessions = new ArrayList<IoSession>();
        for (Collection<NioSocketAcceptor> acceptors : uriToAcceptors.values()) {
            for (NioSocketAcceptor acceptor : acceptors) sessions.addAll(acceptor.getManagedSessions().values());
        }
        for (NioSocketConnector connector : uriToConnectors.values()) sessions.addAll(connector.getManagedSessions().values());
        return sessions;
    }

    /**
   * Get the sessions for all URI's bound with bind ().
   */
    public Collection<IoSession> sessionsFor(Collection<? extends ElvinURI> uris) {
        Collection<IoSession> sessions = new ArrayList<IoSession>();
        for (ElvinURI uri : uris) {
            if (uriToAcceptors.containsKey(uri)) {
                for (NioSocketAcceptor acceptor : uriToAcceptors.get(uri)) sessions.addAll(acceptor.getManagedSessions().values());
            }
        }
        return sessions;
    }

    /**
   * Find the URI associated with a given IoService (an acceptor or
   * connector). This will only work with services created by @
   * #bind(Set, IoHandler, DefaultIoFilterChainBuilder, Filter)} or
   * {@link #createConnector(ElvinURI, IoHandler, DefaultIoFilterChainBuilder, Filter, long)}
   * .
   * @param service The service to lookup.
   * 
   * @return The URI for the service, or null.
   */
    public ElvinURI elvinUriFor(IoService service) {
        for (Map.Entry<ElvinURI, NioSocketConnector> e : uriToConnectors.entrySet()) {
            if (e.getValue() == service) return e.getKey();
        }
        for (Map.Entry<ElvinURI, Collection<NioSocketAcceptor>> e : uriToAcceptors.entrySet()) {
            if (e.getValue().contains(service)) return e.getKey();
        }
        return null;
    }

    public NioSocketConnector createConnector() {
        return new NioSocketConnector(processorPool);
    }

    /**
   * Create a MINA I/O connector for a URI.
   * 
   * @param uri The URI.
   * @param handler The IO handler.
   * @param filters The base filters.
   * @param authenticationRequiredHosts The hosts for which
   *          authentication is required. For standard link these
   *          hosts are denied connection.
   * @param timeout Connect timeout (in seconds).
   * 
   * @return The connector.
   */
    public NioSocketConnector createConnector(ElvinURI uri, IoHandler handler, DefaultIoFilterChainBuilder filters, Filter<InetAddress> authenticationRequiredHosts, long timeout) {
        NioSocketConnector connector = new NioSocketConnector(processorPool);
        uriToConnectors.put(uri, connector);
        if (uri.isSecure()) filters = createSecureFilters(filters, authenticationRequiredHosts, true); else filters = createStandardFilters(filters, authenticationRequiredHosts);
        if (!filters.contains("memory")) {
            filters.addLast("memory", lowMemoryFilter);
            filters.addLast("stats", StatsFilter.INSTANCE);
        }
        connector.setFilterChainBuilder(filters);
        connector.setHandler(handler);
        connector.setConnectTimeoutMillis(timeout);
        return connector;
    }

    public void disconnect(ElvinURI uri) {
        NioSocketConnector connector = uriToConnectors.remove(uri);
        if (connector != null) connector.dispose();
    }
}
