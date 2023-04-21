package org.dbe.toolkit.proxyframework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.fada.FadaException;
import net.fada.toolkit.FadaHelper;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * This class enables the discovery of registered service proxy objects with a fada cloud network.
 *
 * @author <a href="mailto:David.McKitterick@cs.tcd.ie">David McKitterick</a>
 * @author <a href="mailto:andrew.edmonds@cs.tcd.ie">Andy Edmonds</a>
 */
public class ServiceProxyLocator {

    private static final String DEFAULT_FADA_URL = "localhost:2002";

    /** 
     * Constant for default minimum number of proxies 
     */
    private static final int MIN_PROXIES = 1;

    private List fadaUrls = new ArrayList();

    private long timeout = 2000L;

    private int maxProxies = 10;

    private static Logger logger = Logger.getLogger(ServiceProxyLocator.class.getName());

    /**
     * Initialises the object.
     *
     */
    public ServiceProxyLocator() {
        fadaUrls.add(DEFAULT_FADA_URL);
        BasicConfigurator.configure();
        logger = Logger.getRootLogger();
        logger.setLevel(Level.INFO);
    }

    /**
     * This method performs a search on a Fada network for one service proxy object which is registered with an entry
     *
     * @param singleEntry an entry under which the proxy was regsitered
     *
     * @return ServiceProxy
     * @throws FrameworkException
     *
     * @throws FadaException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ServiceProxy proxyLookupFirst(final String singleEntry) throws FrameworkException {
        String[] entries = new String[] { singleEntry };
        return proxyLookupFirst(entries);
    }

    /**
     * This method performs a search on a Fada network for one service proxy object which is registered with entries
     *
     * @param entries entries under which the proxy was regsitered
     * 
     * @return ServiceProxy
     * @throws FrameworkException
     *
     * @throws FadaException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ServiceProxy proxyLookupFirst(final String[] entries) throws FrameworkException {
        ServiceProxy proxy = null;
        maxProxies = MIN_PROXIES;
        proxy = (ServiceProxy) proxyLookup(entries, null).get(0);
        return proxy;
    }

    /**
     * This method performs a search on a Fada network for one service proxy object which is registered 
     * with an entry and implements a specific interface
     *
     * @param singleEntry an entry under which the proxy was regsitered
     *
     * @return ServiceProxy
     * @throws FrameworkException
     *
     * @throws FadaException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public ServiceProxy proxyLookupFirst(final String singleEntry, final String singleInterface) throws FrameworkException {
        String[] entries = new String[] { singleEntry };
        String[] interfaces = new String[] { singleInterface };
        ServiceProxy proxy = null;
        proxy = (ServiceProxy) proxyLookup(entries, interfaces).get(0);
        return proxy;
    }

    /**
     * This method performs a search on a Fada network for many service proxy objects which are registered with entries
     *
     * @param entries entries under which the proxy was regsitered
     *
     * @return ServiceProxy[]
     * @throws FrameworkException
     *
     * @throws FadaException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public List proxyLookup(final String[] entries) throws FrameworkException {
        return proxyLookup(entries, null);
    }

    /**
     * This method performs a search on a Fada network for one service proxy object which is registered 
     * with entries and implements some specific interfaces
     *
     * @param singleEntry an entry under which the proxy was regsitered
     *
     * @return ServiceProxy
     * @throws FrameworkException
     *
     * @throws FadaException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public List proxyLookup(final String[] entries, final String[] interfaces) throws FrameworkException {
        List proxiesFound = null;
        for (int i = 0; (i < fadaUrls.size()) && (proxiesFound == null); i++) {
            try {
                proxiesFound = lookup(entries, interfaces, (String) fadaUrls.get(i));
            } catch (FadaException e) {
                logger.debug("Connecting to: " + fadaUrls.get(i) + " Failed.\n\t Exception: " + e.toString());
            } catch (IOException e) {
                logger.debug("Connecting to: " + fadaUrls.get(i) + " Failed.\n\t Exception: " + e.toString());
            } catch (ClassNotFoundException e) {
                logger.debug("Connecting to: " + fadaUrls.get(i) + " Failed.\n\t Exception: " + e.toString());
            }
        }
        if (proxiesFound == null || proxiesFound.size() == 0) {
            logger.error("No Service Proxies found");
            throw new FrameworkException("No Service Proxies found");
        }
        return proxiesFound;
    }

    /**
     * @param entries
     * @param interfaces
     * @param i
     * @return
     * @throws FadaException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected List lookup(final String[] entries, final String[] interfaces, String fadaNode) throws FadaException, IOException, ClassNotFoundException {
        Object[] proxiesFound;
        proxiesFound = FadaHelper.lookup(fadaNode, entries, null, interfaces, maxProxies, timeout);
        return Arrays.asList(proxiesFound);
    }

    /**
     * Add URL for a new FADA node.
     *
     * @param  fadaUrl url of a new fada node
     * @throws IllegalArgumentException if any parameter is <tt>null</tt>
     *         or the connection type is unknown
     */
    public void addFadaUrl(final String fadaUrl) {
        if (fadaUrl != null) {
            fadaUrls.add(fadaUrl);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Add a list of URLs for new FADA nodes.
     *
     * @param  urls a list of urls of new fada nodes
     * @throws IllegalArgumentException if any parameter is <tt>null</tt>
     *         or the connection type is unknown
     */
    public void addFadaUrls(final List urls) {
        if (urls != null) {
            fadaUrls.add(urls);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getMaxProxies() {
        return maxProxies;
    }

    public void setMaxProxies(int maxProxies) {
        this.maxProxies = maxProxies;
    }
}
