package com.volantis.xml.pipeline.sax.drivers.web.httpcache;

import com.volantis.cache.Cache;
import com.volantis.shared.net.http.HTTPCacheHeaders;
import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.HTTPMessageEntityIdentity;
import com.volantis.shared.net.http.SimpleHTTPMessageEntities;
import com.volantis.shared.net.http.cookies.Cookie;
import com.volantis.shared.net.http.headers.Header;
import com.volantis.shared.net.http.parameters.RequestParameter;
import com.volantis.shared.net.proxy.ProxyManager;
import com.volantis.shared.net.url.http.HttpContent;
import com.volantis.shared.net.url.http.RuntimeHttpException;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.dependency.Dependency;
import com.volantis.shared.dependency.UncacheableDependency;
import com.volantis.xml.pipeline.sax.drivers.web.AbstractPluggableHTTPManager;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPException;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPManagerUtilities;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestExecutor;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestType;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPResponseAccessor;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPVersion;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Category;

/**
 * Class that provides provides the HTTPRequestExecutor functionality that
 * a PluggableHTTPManager need. This class performs a number of tightly
 * coupled roles. It is used by users of the AbstractPluggableHTTPManager
 * to request that a http request be performed. In this case it contains
 * a reference to the HTTP cache and performs requests via said cache if
 * appropriate.
 * Its second role is to be the key in the cache. This is done so that
 * all information needed to make a HTTP request is avaiable within the
 * cache mechanism.
 */
public class CachingRequestExecutor implements HTTPRequestExecutor {

    /**
     * For logging information.
     */
    private static Category logger = Category.getInstance("com.volantis.xml.pipeline.sax.drivers.web.httpcache" + "CachingRequestExecutor");

    /**
     * A reference to the cache.
     */
    private Cache cache = null;

    /**
     * The target to delegate to if the request icannot be cached (for
     * example if it is a POST request)
     */
    private AbstractPluggableHTTPManager target = null;

    /**
     * HTTPRequestExecutor executor. If this is null then the cache
     * is performing the request. Otherwise we are performing a
     * non-cachable request such as a POST.
     */
    private HTTPRequestExecutor executor;

    /**
     * The response accessor to return when execture is called.
     */
    private HTTPResponseAccessor respAccessor = null;

    /**
     * The type of the reuqest (PUT/POST/HEAD)
     */
    private HTTPRequestType requestType = null;

    /**
     * The request url. This does not contain the request parameters
     */
    private String url = null;

    /**
     * The http version to use for the request.
     */
    private HTTPVersion version = null;

    /**
     * The proxy manager specified in the config
     */
    private ProxyManager proxyManager = null;

    /**
     * Store the request parameters. This is null until the first
     * parameter is added.
     */
    private ArrayList requestParameters = null;

    /**
     * Store the cookies. This is null until a cookie is set.
     */
    private HTTPMessageEntities requestCookies = null;

    /**
     * Store the request headers. This is null until the first header
     * is added.
     */
    private HTTPMessageEntities requestHeaders = null;

    /**
     * The pipeline context
     */
    private final XMLPipelineContext xmlPipelineContext;

    /**
     * Dependency context to be used with the {@link respAccessor}.
     */
    private Dependency dependency;

    /**
     * Constructor.
     *
     * @param cache The cache this Executor will use.
     * @param target the manager that this executor will delegate to in
     * order to populate the cache.
     * @param url the url to perform requests against.
     * @param requestType specify if the request will be a GET/HEAD/POST
     * @param version The http version to be used for the request.
     * @param proxy Information about the configured proxyManager (if any). May
     * @param xmlPipelineContext the current pipeline context
     */
    public CachingRequestExecutor(final Cache cache, final AbstractPluggableHTTPManager target, final String url, final HTTPRequestType requestType, final HTTPVersion version, final ProxyManager proxy, final XMLPipelineContext xmlPipelineContext) {
        if (cache == null) {
            throw new IllegalArgumentException("Cache must not be null");
        }
        if (target == null) {
            throw new IllegalArgumentException("Target must not be null");
        }
        if (url == null) {
            throw new IllegalArgumentException("Url must not be null");
        }
        if (requestType == null) {
            throw new IllegalArgumentException("requestType must not be null");
        }
        if (version == null) {
            throw new IllegalArgumentException("version must not be null");
        }
        this.cache = cache;
        this.target = target;
        this.url = url;
        this.requestType = requestType;
        this.version = version;
        this.proxyManager = proxy;
        this.xmlPipelineContext = xmlPipelineContext;
    }

    /**
     * @return the headers associated with this request
     */
    HTTPMessageEntities getRequestHeaders() {
        return requestHeaders;
    }

    /**
     * Execute the request
     * @return An accessor that allows access to the response.
     * @throws com.volantis.xml.pipeline.sax.drivers.web.HTTPException
     */
    public HTTPResponseAccessor execute() throws HTTPException {
        final String url = HTTPManagerUtilities.createQueryURL(this.url, requestParameters, target);
        final URL keyUrl;
        try {
            keyUrl = new URL(url);
        } catch (MalformedURLException e) {
            throw new HTTPException("Invalid URL: " + url, e);
        }
        if (respAccessor == null) {
            executor = target.createHTTPRequestExecutor(this.url, requestType, version, proxyManager, xmlPipelineContext);
            transferMetaData(executor);
            if (requestType.equals(HTTPRequestType.GET) && canRequestBeSatisfiedByCache(requestHeaders)) {
                HttpContent content = null;
                if (xmlPipelineContext != null) {
                    final DependencyContext dependencyContext = xmlPipelineContext.getDependencyContext();
                    if (dependencyContext != null) {
                        content = (HttpContent) dependencyContext.removeProperty(keyUrl);
                    }
                }
                if (content == null) {
                    try {
                        content = (HttpContent) cache.retrieve(keyUrl, new HttpCacheableObjectProvider(executor, cache.getRootGroup(), cache));
                    } catch (RuntimeHttpException e) {
                        throw (HTTPException) e.getCause();
                    }
                }
                respAccessor = new HttpContentWrapper(content);
                dependency = content.getDependency();
            } else {
                respAccessor = executor.execute();
                dependency = UncacheableDependency.getInstance();
            }
        }
        if (xmlPipelineContext != null) {
            final DependencyContext dependencyContext = xmlPipelineContext.getDependencyContext();
            if (dependencyContext != null && dependencyContext.isTrackingDependencies()) {
                dependencyContext.addDependency(dependency);
            }
        }
        return respAccessor;
    }

    /**
     * Copy headers, parameters and cookies from this Executor to the
     * delegate executor.
     *
     * @param executor The executor to copy meta data to.
     * @throws com.volantis.xml.pipeline.sax.drivers.web.HTTPException on
     * error.
     */
    void transferMetaData(HTTPRequestExecutor executor) throws HTTPException {
        if (requestParameters != null) {
            Iterator i = requestParameters.iterator();
            while (i.hasNext()) {
                executor.addRequestParameter((RequestParameter) i.next());
            }
        }
        if (requestHeaders != null) {
            Iterator i = requestHeaders.iterator();
            while (i.hasNext()) {
                executor.addRequestHeader((Header) i.next());
            }
        }
        if (requestCookies != null) {
            Iterator i = requestCookies.iterator();
            while (i.hasNext()) {
                executor.addRequestCookie((Cookie) i.next());
            }
        }
    }

    public void release() throws HTTPException {
        if (executor != null) {
            executor.release();
        }
    }

    public void addRequestParameter(RequestParameter parameter) throws HTTPException {
        if (parameter != null) {
            if (requestParameters == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Creating parameter holder");
                }
                requestParameters = new ArrayList();
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Adding parameter " + parameter);
            }
            requestParameters.add(parameter);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("null passed as parameter to " + "addRequstParameter: ignoring it");
            }
        }
    }

    public void addRequestHeader(Header header) throws HTTPException {
        if (header != null) {
            if (requestHeaders == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Creating header holder");
                }
                requestHeaders = new SimpleHTTPMessageEntities();
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Adding header " + header);
            }
            requestHeaders.add(header);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("null passed as parameter to addRequstHeader" + ": ignoring it");
            }
        }
    }

    public void addRequestCookie(Cookie cookie) throws HTTPException {
        if (cookie != null) {
            if (requestCookies == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Creating cookie holder");
                }
                requestCookies = new SimpleHTTPMessageEntities();
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Adding cookie " + cookie);
            }
            requestCookies.add(cookie);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("null passed as parameter to addCookie" + ": ignoring it");
            }
        }
    }

    /**
     * @return the url associated with this RequestExecutor.
     */
    String getUrl() {
        return url;
    }

    /**
     * @return the type of the request associated with this executor.
     */
    HTTPRequestType getRequestType() {
        return requestType;
    }

    /**
     * @return the http version associated with this executor.
     */
    HTTPVersion getVersion() {
        return version;
    }

    /**
     * @return the proxyManager configuration associated with this executor.
     */
    ProxyManager getProxyManager() {
        return proxyManager;
    }

    /**
     * Return true if the headers in the request allow the request to be
     * answered using a Cached response.
     *
     * @param headers the headers in which to search for no-cache
     * directive. This directive can be in Pragma or Cache-control
     * statements.
     * @return true if a no-cache directive is found in wither a Pragma or
     * Cache-control header.
     */
    private boolean canRequestBeSatisfiedByCache(HTTPMessageEntities headers) {
        boolean result = true;
        if (headers != null) {
            result = !findHeaderMatch(headers, HTTPCacheHeaders.CACHE_CONTROL.getIdentity(), HTTPCacheHeaders.NO_CACHE);
            if (!result && logger.isDebugEnabled()) {
                logger.debug("\"Cache-Control: no-cache\" found in request header");
            }
            if (result) {
                result = !findHeaderMatch(headers, HTTPCacheHeaders.PRAGMA.getIdentity(), HTTPCacheHeaders.NO_CACHE);
                if (!result && logger.isDebugEnabled()) {
                    logger.debug("\"Pragma: no-cache\" found in request header");
                }
            }
        }
        if (logger.isDebugEnabled()) {
            if (result) {
                logger.debug("Request can be satisified by cache.");
            } else {
                logger.debug("Request cannot be satisified by cache.");
            }
        }
        return result;
    }

    /**
     * Utility method that tries to find <code>match</code> in the values of
     * <code>headers</code> which match the <code>header</code> identity.
     *
     * @param headers the headers to search
     * @param header the identiy of the header type to find.
     * @param match the string to match against the start of the value.
     * @return true if a header is found whose value starts with the
     * <code>match</code> parameter and whose identity equals <code>header
     * </code>.
     */
    private boolean findHeaderMatch(HTTPMessageEntities headers, HTTPMessageEntityIdentity header, String match) {
        boolean result = false;
        Iterator cacheControl = headers.valuesIterator(header);
        while (!result && cacheControl.hasNext()) {
            String entity = (String) cacheControl.next();
            if (entity.startsWith(match)) {
                result = true;
            }
        }
        return result;
    }
}
