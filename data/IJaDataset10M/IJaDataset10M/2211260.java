package com.jacum.cms.session;

import com.jacum.cms.session.content.Content;
import com.jacum.cms.source.ContentController;
import com.jacum.cms.source.ContentRepositorySession;
import com.jacum.cms.source.ContentSource;
import com.jacum.cms.source.ContentViewHelper;
import java.util.Map;

/**
 * This is an interface defining the context of content service request.
 * <p/>
 * The request context is a container with lifespan of a content request.
 * It contains all input parameters, holds everything that relates
 * to that request and finally the results to be retrieved by client application.
 * <p/>
 * The implementation has to be provided by the application, because content
 * service is designed to be independent of particular protocols.
 */
public interface ContentRequestContext {

    static final String STATUS_OK = "OK";

    static final String STATUS_REDIRECT = "REDIRECT";

    static final String STATUS_SKIP = "SKIP";

    static final String STATUS_NOT_FOUND = "NOT_FOUND";

    static final String STATUS_NOT_AUTHORIZED = "NOT_AUTHORIZED";

    static final String STATUS_APP_ERROR = "APP_ERROR";

    static final String ERROR_REDIRECT_NONE = "NONE";

    static final String ERROR_REDIRECT_IN_PROGRESS = "IN_PROGRESS";

    static final String ERROR_REDIRECT_ERROR = "ERROR";

    static final String ERROR_REDIRECT_DONE = "DONE";

    /**
     * Attribute name for the name of the controller to
     * be invoked (comes from client application)
     */
    static final String CONTROLLER_NAME_ATTRIBUTE = "invoke";

    /**
     * Unique request ID
     */
    String getRequestId();

    /**
     * Parse content request and retrieve requestId of content source.
     * <p/>
     * The client application is responsible to provide way of determining
     * content source by request.
     *
     * @return the requestId of content source to serve request
     */
    String getContentSourceId();

    void setContentSourceId(String contentSourceId);

    /**
     * Retrieve path to a content item being requested
     *
     * @return slash-separated path
     */
    String getContentPath();

    void setContentPath(String contentPath);

    /**
     * Retrieve attributes of content request.
     * <p/>
     * The client application has to provide the way to convert
     * parameters of an external protocol to attribute map.
     *
     * @return map of string keys and string values
     */
    Map getAttributes();

    /**
     * Retrieves current state of content
     *
     * @return an implementation of content interface
     */
    Content getCurrentContent();

    /**
     * Replaces content with new (transformed) content
     */
    void replaceContent(Content newContent);

    /**
     * Get request status
     *
     * @return content request completion status
     */
    String getStatus();

    void changeStatusTo(String status);

    /**
     * Get error handler redirection status
     *
     * @return error handler progress status
     */
    String getErrorRedirect();

    void changeErrorRedirectTo(String status);

    /**
     * Acquire associated content session
     *
     * @return content session, if present, null otherwise
     */
    ContentSession getSession();

    void setSession(ContentSession session);

    /**
     * Acquire associated content source (singleton!)
     *
     * @return content source
     */
    ContentSource getContentSource();

    /**
     * Set content source. Used by content session.
     *
     * @param session
     */
    void setContentSource(ContentSource session);

    /**
     * Get active controller.
     *
     * @return active controller for this request, or null if no controllers are active
     */
    ContentController getActiveController();

    /**
     * Set active controller. Can be used by client application to set
     * controller explicitly, or by content source to figure out controller
     * from request parameters.
     *
     * @param activeController
     */
    void setActiveController(ContentController activeController);

    ContentRepositorySession getContentRepositorySession();

    void setContentRepositorySession(ContentRepositorySession contentRepositorySession);

    /**
     * Retrieve custom view helpers
     * @return a map of view helpers
     */
    Map<String, ContentViewHelper> getViewHelpers();

    /**
     * Add new view helper
     * @param key a key to use in templates
     * @param viewHelper a view helper bean implementation
     */
    void addViewHelper(String key, ContentViewHelper viewHelper);

    /**
     * Return unique content ID. Used to pick content from cache.
     * @return string, representing content ID
     */
    String getContentId();

    /**
     * This method is used by caching interceptors to decide whether
     * caching is allowed or not. It can be set to true
     * in the client adapter (==don't check cache) or at the time of
     * request processing (==don't cache results)
     *
     * @return true if caching activity is prohibited
     */
    boolean isCachingVetoed();

    /**
     * Disable caching
     */
    void vetoCaching();

    /**
     * This method is used to decide whether to apply (further)
     * transformations. It has higher priority than transformer mappers.
     * @return
     */
    boolean isTransformationVetoed();
}
