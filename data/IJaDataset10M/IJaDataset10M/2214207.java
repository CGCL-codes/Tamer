package org.opencms.flex;

import org.opencms.file.CmsObject;
import org.opencms.file.CmsResource;
import org.opencms.main.CmsLog;
import org.opencms.util.CmsRequestUtil;
import java.util.List;
import java.util.Vector;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;

/**
 * Controller for getting access to the CmsObject, should be used as a 
 * request attribute.<p>
 * 
 * @author Alexander Kandzior 
 * 
 * @version $Revision: 1.42 $ 
 * 
 * @since 6.0.0 
 */
public class CmsFlexController {

    /** Constant for the controller request attribute name. */
    public static final String ATTRIBUTE_NAME = "org.opencms.flex.CmsFlexController";

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsFlexController.class);

    /** The CmsFlexCache where the result will be cached in, required for the dispatcher. */
    private CmsFlexCache m_cache;

    /** The wrapped CmsObject provides JSP with access to the core system. */
    private CmsObject m_cmsObject;

    /** List of wrapped RequestContext info object. */
    private List m_flexContextInfoList;

    /** List of wrapped CmsFlexRequests. */
    private List m_flexRequestList;

    /** List of wrapped CmsFlexResponses. */
    private List m_flexResponseList;

    /** Indicates if this controller is currently in "forward" mode. */
    private boolean m_forwardMode;

    /** Wrapped top request. */
    private HttpServletRequest m_req;

    /** Wrapped top response. */
    private HttpServletResponse m_res;

    /** The CmsResource that was initialized by the original request, required for URI actions. */
    private CmsResource m_resource;

    /** Indicates if the response should be streamed. */
    private boolean m_streaming;

    /** Exception that was caught during inclusion of sub elements. */
    private Throwable m_throwable;

    /** URI of a VFS resource that caused the exception. */
    private String m_throwableResourceUri;

    /** Indicates if the request is the top request. */
    private boolean m_top;

    /**
     * Creates a new controller form the old one, exchanging just the provided OpenCms user context.<p>
     * 
     * @param cms the OpenCms user context for this controller
     * @param base the base controller
     */
    public CmsFlexController(CmsObject cms, CmsFlexController base) {
        m_cmsObject = cms;
        m_resource = base.m_resource;
        m_cache = base.m_cache;
        m_req = base.m_req;
        m_res = base.m_res;
        m_streaming = base.m_streaming;
        m_top = base.m_top;
        m_flexRequestList = base.m_flexRequestList;
        m_flexResponseList = base.m_flexResponseList;
        m_flexContextInfoList = base.m_flexContextInfoList;
        m_forwardMode = base.m_forwardMode;
        m_throwableResourceUri = base.m_throwableResourceUri;
    }

    /**
     * Default constructor.<p>
     * 
     * @param cms the initial CmsObject to wrap in the controller
     * @param resource the file requested 
     * @param cache the instance of the flex cache
     * @param req the current request
     * @param res the current response
     * @param streaming indicates if the response is streaming
     * @param top indicates if the response is the top response
     */
    public CmsFlexController(CmsObject cms, CmsResource resource, CmsFlexCache cache, HttpServletRequest req, HttpServletResponse res, boolean streaming, boolean top) {
        m_cmsObject = cms;
        m_resource = resource;
        m_cache = cache;
        m_req = req;
        m_res = res;
        m_streaming = streaming;
        m_top = top;
        m_flexRequestList = new Vector();
        m_flexResponseList = new Vector();
        m_flexContextInfoList = new Vector();
        m_forwardMode = false;
        m_throwableResourceUri = null;
    }

    /**
     * Returns the wrapped CmsObject form the provided request, or <code>null</code> if the 
     * request is not running inside OpenCms.<p>
     * 
     * @param req the current request
     * @return the wrapped CmsObject
     */
    public static CmsObject getCmsObject(ServletRequest req) {
        CmsFlexController controller = (CmsFlexController) req.getAttribute(ATTRIBUTE_NAME);
        if (controller != null) {
            return controller.getCmsObject();
        } else {
            return null;
        }
    }

    /**
     * Returns the controller from the given request, or <code>null</code> if the 
     * request is not running inside OpenCms.<p>
     * 
     * @param req the request to get the controller from
     * 
     * @return the controller from the given request, or <code>null</code> if the request is not running inside OpenCms
     */
    public static CmsFlexController getController(ServletRequest req) {
        return (CmsFlexController) req.getAttribute(ATTRIBUTE_NAME);
    }

    /**
     * Provides access to a root cause Exception that might have occurred in a complex include scenario.<p>
     * 
     * @param req the current request
     * 
     * @return the root cause exception or null if no root cause exception is available
     * 
     * @see #getThrowable()
     */
    public static Throwable getThrowable(ServletRequest req) {
        CmsFlexController controller = (CmsFlexController) req.getAttribute(ATTRIBUTE_NAME);
        if (controller != null) {
            return controller.getThrowable();
        } else {
            return null;
        }
    }

    /**
     * Provides access to URI of a VFS resource that caused an exception that might have occurred in a complex include scenario.<p>
     * 
     * @param req the current request
     * 
     * @return to URI of a VFS resource that caused an exception, or <code>null</code>
     * 
     * @see #getThrowableResourceUri()
     */
    public static String getThrowableResourceUri(ServletRequest req) {
        CmsFlexController controller = (CmsFlexController) req.getAttribute(ATTRIBUTE_NAME);
        if (controller != null) {
            return controller.getThrowableResourceUri();
        } else {
            return null;
        }
    }

    /**
     * Checks if the provided request is running in OpenCms and the current users project is the online project.<p>
     *
     * @param req the current request
     * 
     * @return <code>true</code> if the request is running in OpenCms and the current users project is 
     *      the online project, <code>false</code> otherwise
     */
    public static boolean isCmsOnlineRequest(ServletRequest req) {
        if (req == null) {
            return false;
        }
        return getController(req).getCmsObject().getRequestContext().currentProject().isOnlineProject();
    }

    /**
     * Checks if the provided request is running in OpenCms.<p>
     *
     * @param req the current request
     * 
     * @return <code>true</code> if the request is running in OpenCms, <code>false</code> otherwise
     */
    public static boolean isCmsRequest(ServletRequest req) {
        return ((req != null) && (req.getAttribute(ATTRIBUTE_NAME) != null));
    }

    /**
     * Checks if the request has the "If-Modified-Since" header set, and if so,
     * if the header date value is equal to the provided last modification date.<p>
     * 
     * @param req the request to set the "If-Modified-Since" date header from
     * @param dateLastModified the date to compare the header with
     *  
     * @return <code>true</code> if the header is set and the header date is equal to the provided date
     */
    public static boolean isNotModifiedSince(HttpServletRequest req, long dateLastModified) {
        try {
            long lastModifiedHeader = req.getDateHeader(CmsRequestUtil.HEADER_IF_MODIFIED_SINCE);
            return ((lastModifiedHeader > -1) && (((dateLastModified / 1000) * 1000) == lastModifiedHeader));
        } catch (Exception ex) {
            LOG.warn(Messages.get().getBundle().key(Messages.ERR_HEADER_IFMODIFIEDSINCE_FORMAT_3, new Object[] { CmsRequestUtil.HEADER_IF_MODIFIED_SINCE, req.getHeader(CmsRequestUtil.HEADER_USER_AGENT), req.getHeader(CmsRequestUtil.HEADER_IF_MODIFIED_SINCE) }));
        }
        return false;
    }

    /**
     * Removes the controller attribute from a request.<p>
     * 
     * @param req the request to remove the controller from
     */
    public static void removeController(ServletRequest req) {
        CmsFlexController controller = (CmsFlexController) req.getAttribute(ATTRIBUTE_NAME);
        if (controller != null) {
            controller.clear();
        }
    }

    /** 
     * Stores the given controller in the given request (using a request attribute).<p>
     * 
     * @param req the request where to store the controller in 
     * @param controller the controller to store
     */
    public static void setController(ServletRequest req, CmsFlexController controller) {
        req.setAttribute(CmsFlexController.ATTRIBUTE_NAME, controller);
    }

    /**
     * Sets the <code>Expires</code> date header for a given http request.<p>
     * 
     * Also sets the <code>cache-control: max-age</code> header to the time of the expiration.
     * A certain upper limit is imposed on the expiration date parameter to ensure the resources are
     * not cached to long in proxies. This can be controlled by the <code>maxAge</code> parameter. 
     * If <code>maxAge</code> is lower then 0, then a default max age of 86400000 msec (1 day) is used.<p> 
     * 
     * @param res the response to set the "Expires" date header for
     * @param maxAge maximum amount of time in milliseconds the response remains valid
     * @param dateExpires the date to set (if this is not in the future, it is ignored)
     */
    public static void setDateExpiresHeader(HttpServletResponse res, long dateExpires, long maxAge) {
        long now = System.currentTimeMillis();
        if ((dateExpires > now) && (dateExpires != CmsResource.DATE_EXPIRED_DEFAULT)) {
            if (maxAge < 0L) {
                maxAge = 86400000;
            }
            if ((dateExpires - now) > maxAge) {
                dateExpires = now + maxAge;
            }
            res.setDateHeader(CmsRequestUtil.HEADER_EXPIRES, dateExpires);
            res.setHeader(CmsRequestUtil.HEADER_CACHE_CONTROL, CmsRequestUtil.HEADER_VALUE_MAX_AGE + (maxAge / 1000L));
        }
    }

    /**
     * Sets the "last modified" date header for a given http request.<p>
     * 
     * @param res the response to set the "last modified" date header for
     * @param dateLastModified the date to set (if this is lower then 0, the current time is set)
     */
    public static void setDateLastModifiedHeader(HttpServletResponse res, long dateLastModified) {
        if (dateLastModified > -1) {
            res.setDateHeader(CmsRequestUtil.HEADER_LAST_MODIFIED, (dateLastModified / 1000) * 1000);
        } else {
            res.setDateHeader(CmsRequestUtil.HEADER_LAST_MODIFIED, System.currentTimeMillis());
        }
    }

    /**
     * Clears all data of this controller.<p>
     */
    public void clear() {
        if (m_flexRequestList != null) {
            m_flexRequestList.clear();
        }
        m_flexRequestList = null;
        if (m_flexResponseList != null) {
            m_flexResponseList.clear();
        }
        m_flexResponseList = null;
        if (m_req != null) {
            m_req.removeAttribute(ATTRIBUTE_NAME);
        }
        m_req = null;
        m_res = null;
        m_cmsObject = null;
        m_resource = null;
        m_cache = null;
        m_throwable = null;
    }

    /**
     * Returns the CmsFlexCache instance where all results from this request will be cached in.<p>
     * 
     * This is public so that pages like the Flex Cache Administration page
     * have a way to access the cache object.<p>
     *
     * @return the CmsFlexCache instance where all results from this request will be cached in
     */
    public CmsFlexCache getCmsCache() {
        return m_cache;
    }

    /**
     * Returns the wrapped CmsObject.<p>
     * 
     * @return the wrapped CmsObject
     */
    public CmsObject getCmsObject() {
        return m_cmsObject;
    }

    /** 
     * This method provides access to the top-level CmsResource of the request
     * which is of a type that supports the FlexCache,
     * i.e. usually the CmsFile that is identical to the file uri requested by the user,
     * not he current included element.<p>
     * 
     * @return the requested top-level CmsFile
     */
    public CmsResource getCmsResource() {
        return m_resource;
    }

    /**
     * Returns the current flex request.<p>
     * 
     * @return the current flex request
     */
    public CmsFlexRequest getCurrentRequest() {
        return (CmsFlexRequest) m_flexRequestList.get(m_flexRequestList.size() - 1);
    }

    /**
     * Returns the current flex response.<p>
     * 
     * @return the current flex response
     */
    public CmsFlexResponse getCurrentResponse() {
        return (CmsFlexResponse) m_flexResponseList.get(m_flexResponseList.size() - 1);
    }

    /**
     * Returns the combined "expires" date for all resources read during this request.<p>
     * 
     * @return the combined "expires" date for all resources read during this request
     */
    public long getDateExpires() {
        int pos = m_flexContextInfoList.size() - 1;
        if (pos < 0) {
            return CmsResource.DATE_EXPIRED_DEFAULT;
        }
        return ((CmsFlexRequestContextInfo) m_flexContextInfoList.get(pos)).getDateExpires();
    }

    /**
     * Returns the combined "last modified" date for all resources read during this request.<p>
     * 
     * @return the combined "last modified" date for all resources read during this request
     */
    public long getDateLastModified() {
        int pos = m_flexContextInfoList.size() - 1;
        if (pos < 0) {
            return CmsResource.DATE_RELEASED_DEFAULT;
        }
        return ((CmsFlexRequestContextInfo) m_flexContextInfoList.get(pos)).getDateLastModified();
    }

    /**
     * Returns the size of the response stack.<p>
     * 
     * @return the size of the response stack
     */
    public int getResponseStackSize() {
        return m_flexResponseList.size();
    }

    /**
     * Returns an exception (Throwable) that was caught during inclusion of sub elements, 
     * or null if no exceptions where thrown in sub elements.<p>
     * 
     * @return an exception (Throwable) that was caught during inclusion of sub elements
     */
    public Throwable getThrowable() {
        return m_throwable;
    }

    /**
     * Returns the URI of a VFS resource that caused the exception that was caught during inclusion of sub elements,
     * might return null if no URI information was available for the exception.<p>
     * 
     * @return the URI of a VFS resource that caused the exception that was caught during inclusion of sub elements
     */
    public String getThrowableResourceUri() {
        return m_throwableResourceUri;
    }

    /**
     * Returns the current http request.<p>
     * 
     * @return the current http request
     */
    public HttpServletRequest getTopRequest() {
        return m_req;
    }

    /**
     * Returns the current http response.<p>
     * 
     * @return the current http response
     */
    public HttpServletResponse getTopResponse() {
        return m_res;
    }

    /**
     * Returns <code>true</code> if the controller does not yet contain any requests.<p>
     * 
     * @return <code>true</code> if the controller does not yet contain any requests
     */
    public boolean isEmptyRequestList() {
        return (m_flexRequestList != null) && m_flexRequestList.isEmpty();
    }

    /**
     * Returns <code>true</code> if this controller is currently in "forward" mode.<p>
     *
     * @return <code>true</code> if this controller is currently in "forward" mode
     */
    public boolean isForwardMode() {
        return m_forwardMode;
    }

    /**
     * Returns <code>true</code> if the generated output of the response should 
     * be written to the stream directly.<p>
     * 
     * @return <code>true</code> if the generated output of the response should be written to the stream directly
     */
    public boolean isStreaming() {
        return m_streaming;
    }

    /**
     * Returns <code>true</code> if this controller was generated as top level controller.<p>
     * 
     * If a resource (e.g. a JSP) is processed and it's content is included in 
     * another resource, then this will be <code>false</code>.   
     * 
     * @return <code>true</code> if this controller was generated as top level controller
     * 
     * @see org.opencms.loader.I_CmsResourceLoader#dump(CmsObject, CmsResource, String, java.util.Locale, HttpServletRequest, HttpServletResponse)
     * @see org.opencms.jsp.CmsJspActionElement#getContent(String)
     */
    public boolean isTop() {
        return m_top;
    }

    /**
     * Removes the topmost request/response pair from the stack.<p>
     */
    public void pop() {
        if ((m_flexRequestList != null) && !m_flexRequestList.isEmpty()) {
            m_flexRequestList.remove(m_flexRequestList.size() - 1);
        }
        if ((m_flexResponseList != null) && !m_flexRequestList.isEmpty()) {
            m_flexResponseList.remove(m_flexResponseList.size() - 1);
        }
        if ((m_flexContextInfoList != null) && !m_flexContextInfoList.isEmpty()) {
            CmsFlexRequestContextInfo info = (CmsFlexRequestContextInfo) m_flexContextInfoList.remove(m_flexContextInfoList.size() - 1);
            if (m_flexContextInfoList.size() > 0) {
                ((CmsFlexRequestContextInfo) m_flexContextInfoList.get(0)).merge(info);
                updateRequestContextInfo();
            }
        }
    }

    /**
     * Adds another flex request/response pair to the stack.<p>
     * 
     * @param req the request to add
     * @param res the response to add
     */
    public void push(CmsFlexRequest req, CmsFlexResponse res) {
        m_flexRequestList.add(req);
        m_flexResponseList.add(res);
        m_flexContextInfoList.add(new CmsFlexRequestContextInfo());
        updateRequestContextInfo();
    }

    /**
     * Sets the value of the "forward mode" flag.<p>
     *
     * @param value the forward mode to set
     */
    public void setForwardMode(boolean value) {
        m_forwardMode = value;
    }

    /**
     * Sets an exception (Throwable) that was caught during inclusion of sub elements.<p>
     * 
     * If another exception is already set in this controller, then the additional exception
     * is ignored.<p>
     * 
     * @param throwable the exception (Throwable) to set
     * @param resource the URI of the VFS resource the error occurred on (might be <code>null</code> if unknown)
     * 
     * @return the exception stored in the controller
     */
    public Throwable setThrowable(Throwable throwable, String resource) {
        if (m_throwable == null) {
            m_throwable = throwable;
            m_throwableResourceUri = resource;
        } else {
            if (LOG.isDebugEnabled()) {
                if (resource != null) {
                    LOG.debug(Messages.get().getBundle().key(Messages.LOG_FLEXCONTROLLER_IGNORED_EXCEPTION_1, resource));
                } else {
                    LOG.debug(Messages.get().getBundle().key(Messages.LOG_FLEXCONTROLLER_IGNORED_EXCEPTION_0));
                }
            }
        }
        return m_throwable;
    }

    /**
     * Puts the response in a suspended state.<p>  
     */
    public void suspendFlexResponse() {
        for (int i = 0; i < m_flexResponseList.size(); i++) {
            CmsFlexResponse res = (CmsFlexResponse) m_flexResponseList.get(i);
            res.setSuspended(true);
        }
    }

    /**
     * Updates the "last modified" date and the "expires" date 
     * for all resources read during this request with the given values.<p>
     * 
     * The currently stored value for "last modified" is only updated with the new value if
     * the new value is either larger (i.e. newer) then the stored value,
     * or if the new value is less then zero, which indicates that the "last modified"
     * optimization can not be used because the element is dynamic.<p>
     * 
     * The stored "expires" value is only updated if the new value is smaller
     * then the stored value.<p>
     * 
     * @param dateLastModified the value to update the "last modified" date with
     * @param dateExpires the value to update the "expires" date with
     */
    public void updateDates(long dateLastModified, long dateExpires) {
        int pos = m_flexContextInfoList.size() - 1;
        if (pos < 0) {
            return;
        }
        ((CmsFlexRequestContextInfo) m_flexContextInfoList.get(pos)).updateDates(dateLastModified, dateExpires);
    }

    /**
     * Updates the context info of the request context.<p>
     */
    private void updateRequestContextInfo() {
        if ((m_flexContextInfoList != null) && !m_flexContextInfoList.isEmpty()) {
            m_cmsObject.getRequestContext().setAttribute(CmsRequestUtil.HEADER_LAST_MODIFIED, m_flexContextInfoList.get(m_flexContextInfoList.size() - 1));
        }
    }
}
