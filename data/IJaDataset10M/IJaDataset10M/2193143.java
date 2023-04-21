package org.jaffa.presentation.portlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServletWrapper;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.tiles.TilesRequestProcessor;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.apache.struts.util.ModuleUtils;
import org.apache.struts.util.RequestUtils;

/** This class extends the TilesRequestProcessor (since we use the Tiles plugin), which in turn extends the struts RequestProcessor.
 * The struts RequestProcessor invokes the reset() method on a form-bean in its processPopulate() method
 * Jaffa sets the component on a form-bean in the reset() method by obtaining the componentId from the request.
 * In MultiPart posts (i.e. when uploading files), the componentId is available only when the RequestUtils.populate() method is invoked.
 * Hence this class overrides the processPopulate() method of the RequestProcessor to not invoke the reset() method.
 * Instead a custom version of the RequestUtils.populate() method has been created to invoke the reset() method.
 * <p>
 * The customRequestUtilsPopulate() method also handles the scenario when the size of the file being uploaded exceeds the maximum allowed.
 * After it detects that condition, the struts' default implementation sets an attribute in the request stream and terminates form population.
 * The event handling in Jaffa depends on the value of the 'eventId' parameter. The 'eventId' cannot be determined since the form population is terminated,
 * and consequently the ActionHandler fails. Hence our customRequestUtilsPopulate() method throws a ServletException if the limit is breached.
 * An alternative would be continue form population and let the FormBase (via its doValidate() method) or the application handle the ATTRIBUTE_MAX_LENGTH_EXCEEDED.
 * But this requires a change to the MultipartRequestHandler implementation as well.
 * <p>
 * In summary, use this class if file-uploads do not work for you.
 * Add the following fragment to the struts-config file after the action-mappings element.
 *   <controller>
 *       <set-property  property="processorClass" value="org.jaffa.presentation.portlet.CustomRequestProcessor"/>
 *   </controller>
 */
public class CustomRequestProcessor extends TilesRequestProcessor {

    /**
     * <p>Populate the properties of the specified <code>ActionForm</code> instance from
     * the request parameters included with this request.  In addition,
     * request attribute <code>Globals.CANCEL_KEY</code> will be set if
     * the request was submitted with a button created by
     * <code>CancelTag</code>.</p>
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param form The ActionForm instance we are populating
     * @param mapping The ActionMapping we are using
     *
     * @exception ServletException if thrown by RequestUtils.populate()
     */
    protected void processPopulate(HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionMapping mapping) throws ServletException {
        if (form == null) return;
        if (log.isDebugEnabled()) log.debug(" Populating bean properties from this request");
        form.setServlet(this.servlet);
        if (mapping.getMultipartClass() != null) request.setAttribute(Globals.MULTIPART_KEY, mapping.getMultipartClass());
        customRequestUtilsPopulate(form, mapping.getPrefix(), mapping.getSuffix(), request, mapping);
        if ((request.getParameter(Constants.CANCEL_PROPERTY) != null) || (request.getParameter(Constants.CANCEL_PROPERTY_X) != null)) request.setAttribute(Globals.CANCEL_KEY, Boolean.TRUE);
    }

    /**
     * <p>Populate the properties of the specified JavaBean from the specified
     * HTTP request, based on matching each parameter name (plus an optional
     * prefix and/or suffix) against the corresponding JavaBeans "property
     * setter" methods in the bean's class. Suitable conversion is done for
     * argument types as described under <code>setProperties</code>.</p>
     *
     * <p>If you specify a non-null <code>prefix</code> and a non-null
     * <code>suffix</code>, the parameter name must match <strong>both</strong>
     * conditions for its value(s) to be used in populating bean properties.
     * If the request's content type is "multipart/form-data" and the
     * method is "POST", the <code>HttpServletRequest</code> object will be wrapped in
     * a <code>MultipartRequestWrapper</code object.</p>
     *
     * @param bean The JavaBean whose properties are to be set
     * @param prefix The prefix (if any) to be prepend to bean property
     *               names when looking for matching parameters
     * @param suffix The suffix (if any) to be appended to bean property
     *               names when looking for matching parameters
     * @param request The HTTP request whose parameters are to be used
     *                to populate bean properties
     *
     * @exception ServletException if an exception is thrown while setting
     *            property values
     */
    protected static void customRequestUtilsPopulate(Object bean, String prefix, String suffix, HttpServletRequest request, ActionMapping mapping) throws ServletException {
        HashMap properties = new HashMap();
        Enumeration names = null;
        Map multipartParameters = null;
        String contentType = request.getContentType();
        String method = request.getMethod();
        boolean isMultipart = false;
        if ((contentType != null) && (contentType.startsWith("multipart/form-data")) && (method.equalsIgnoreCase("POST"))) {
            ActionServletWrapper servlet;
            if (bean instanceof ActionForm) {
                servlet = ((ActionForm) bean).getServletWrapper();
            } else {
                throw new ServletException("bean that's supposed to be " + "populated from a multipart request is not of type " + "\"org.apache.struts.action.ActionForm\", but type " + "\"" + bean.getClass().getName() + "\"");
            }
            MultipartRequestHandler multipartHandler = getMultipartHandler(request);
            ((ActionForm) bean).setMultipartRequestHandler(multipartHandler);
            if (multipartHandler != null) {
                isMultipart = true;
                servlet.setServletFor(multipartHandler);
                multipartHandler.setMapping((ActionMapping) request.getAttribute(Globals.MAPPING_KEY));
                multipartHandler.handleRequest(request);
                Boolean maxLengthExceeded = (Boolean) request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
                if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue())) {
                    throw new ServletException("The size of the file being uploaded exceeds the maximum allowed " + ModuleUtils.getInstance().getModuleConfig(request).getControllerConfig().getMaxFileSize());
                }
                multipartParameters = getAllParametersForMultipartRequest(request, multipartHandler);
                names = Collections.enumeration(multipartParameters.keySet());
            }
        }
        if (!isMultipart) {
            names = request.getParameterNames();
        }
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String stripped = name;
            if (prefix != null) {
                if (!stripped.startsWith(prefix)) {
                    continue;
                }
                stripped = stripped.substring(prefix.length());
            }
            if (suffix != null) {
                if (!stripped.endsWith(suffix)) {
                    continue;
                }
                stripped = stripped.substring(0, stripped.length() - suffix.length());
            }
            Object parameterValue = null;
            if (isMultipart) {
                parameterValue = multipartParameters.get(name);
            } else {
                parameterValue = request.getParameterValues(name);
            }
            if (!(stripped.startsWith("org.apache.struts."))) {
                properties.put(stripped, parameterValue);
            }
        }
        try {
            if (log.isDebugEnabled()) log.debug("Calling FormBean reset()");
            ((ActionForm) bean).reset(mapping, request);
        } catch (Exception e) {
            throw new ServletException("FormBean.reset", e);
        }
        try {
            BeanUtils.populate(bean, properties);
        } catch (Exception e) {
            throw new ServletException("BeanUtils.populate", e);
        }
    }

    /**
     * <p>Try to locate a multipart request handler for this request. First, look
     * for a mapping-specific handler stored for us under an attribute. If one
     * is not present, use the global multipart handler, if there is one.</p>
     *
     * @param request The HTTP request for which the multipart handler should
     *                be found.
     * @return the multipart handler to use, or null if none is
     *         found.
     *
     * @exception ServletException if any exception is thrown while attempting
     *                             to locate the multipart handler.
     */
    private static MultipartRequestHandler getMultipartHandler(HttpServletRequest request) throws ServletException {
        MultipartRequestHandler multipartHandler = null;
        String multipartClass = (String) request.getAttribute(Globals.MULTIPART_KEY);
        request.removeAttribute(Globals.MULTIPART_KEY);
        if (multipartClass != null) {
            try {
                multipartHandler = (MultipartRequestHandler) RequestUtils.applicationInstance(multipartClass);
            } catch (ClassNotFoundException cnfe) {
                log.error("MultipartRequestHandler class \"" + multipartClass + "\" in mapping class not found, " + "defaulting to global multipart class");
            } catch (InstantiationException ie) {
                log.error("InstantiationException when instantiating " + "MultipartRequestHandler \"" + multipartClass + "\", " + "defaulting to global multipart class, exception: " + ie.getMessage());
            } catch (IllegalAccessException iae) {
                log.error("IllegalAccessException when instantiating " + "MultipartRequestHandler \"" + multipartClass + "\", " + "defaulting to global multipart class, exception: " + iae.getMessage());
            }
            if (multipartHandler != null) {
                return multipartHandler;
            }
        }
        ModuleConfig moduleConfig = ModuleUtils.getInstance().getModuleConfig(request);
        multipartClass = moduleConfig.getControllerConfig().getMultipartClass();
        if (multipartClass != null) {
            try {
                multipartHandler = (MultipartRequestHandler) RequestUtils.applicationInstance(multipartClass);
            } catch (ClassNotFoundException cnfe) {
                throw new ServletException("Cannot find multipart class \"" + multipartClass + "\"" + ", exception: " + cnfe.getMessage());
            } catch (InstantiationException ie) {
                throw new ServletException("InstantiationException when instantiating " + "multipart class \"" + multipartClass + "\", exception: " + ie.getMessage());
            } catch (IllegalAccessException iae) {
                throw new ServletException("IllegalAccessException when instantiating " + "multipart class \"" + multipartClass + "\", exception: " + iae.getMessage());
            }
            if (multipartHandler != null) {
                return multipartHandler;
            }
        }
        return multipartHandler;
    }

    /**
     *<p>Create a <code>Map</code> containing all of the parameters supplied for a multipart
     * request, keyed by parameter name. In addition to text and file elements
     * from the multipart body, query string parameters are included as well.</p>
     *
     * @param request The (wrapped) HTTP request whose parameters are to be
     *                added to the map.
     * @param multipartHandler The multipart handler used to parse the request.
     *
     * @return the map containing all parameters for this multipart request.
     */
    private static Map getAllParametersForMultipartRequest(HttpServletRequest request, MultipartRequestHandler multipartHandler) {
        Map parameters = new HashMap();
        Hashtable elements = multipartHandler.getAllElements();
        Enumeration e = elements.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            parameters.put(key, elements.get(key));
        }
        if (request instanceof MultipartRequestWrapper) {
            request = ((MultipartRequestWrapper) request).getRequest();
            e = request.getParameterNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                parameters.put(key, request.getParameterValues(key));
            }
        } else {
            log.debug("Gathering multipart parameters for unwrapped request");
        }
        return parameters;
    }
}
