package org.displaytag.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.UnhandledException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.Messages;

/**
 * Default RequestHelper implementation.
 * @author Fabrizio Giustina
 * @version $Revision: 956 $ ($Author: fgiust $)
 * @see org.displaytag.util.Href
 * @see org.displaytag.util.RequestHelper
 */
public class DefaultRequestHelper implements RequestHelper {

    /**
     * logger.
     */
    private static Log log = LogFactory.getLog(DefaultRequestHelper.class);

    /**
     * original HttpServletRequest.
     */
    private HttpServletRequest request;

    /**
     * original HttpServletResponse.
     */
    private HttpServletResponse response;

    /**
     * Construct a new RequestHelper for the given request.
     * @param servletRequest HttpServletRequest needed to generate the base href
     * @param servletResponse HttpServletResponse needed to encode generated urls
     */
    public DefaultRequestHelper(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        this.request = servletRequest;
        this.response = servletResponse;
    }

    /**
     * @see org.displaytag.util.RequestHelper#getHref()
     */
    public Href getHref() {
        String requestURI = this.request.getRequestURI();
        Href href = new DefaultHref(this.response.encodeURL(requestURI));
        href.setParameterMap(getParameterMap());
        return href;
    }

    /**
     * @see org.displaytag.util.RequestHelper#getParameter(java.lang.String)
     */
    public String getParameter(String key) {
        return this.request.getParameter(key);
    }

    /**
     * @see org.displaytag.util.RequestHelper#getIntParameter(java.lang.String)
     */
    public Integer getIntParameter(String key) {
        String value = this.request.getParameter(key);
        if (value != null) {
            try {
                return new Integer(value);
            } catch (NumberFormatException e) {
                log.debug(Messages.getString("RequestHelper.invalidparameter", new Object[] { key, value }));
            }
        }
        return null;
    }

    /**
     * @see org.displaytag.util.RequestHelper#getParameterMap()
     */
    public Map getParameterMap() {
        Map map = new HashMap();
        Enumeration parametersName = this.request.getParameterNames();
        while (parametersName.hasMoreElements()) {
            String paramName = (String) parametersName.nextElement();
            String[] originalValues = (String[]) ObjectUtils.defaultIfNull(this.request.getParameterValues(paramName), new String[0]);
            String[] values = new String[originalValues.length];
            for (int i = 0; i < values.length; i++) {
                try {
                    values[i] = URLEncoder.encode(StringUtils.defaultString(originalValues[i]), StringUtils.defaultString(response.getCharacterEncoding(), "UTF8"));
                } catch (UnsupportedEncodingException e) {
                    throw new UnhandledException(e);
                }
            }
            map.put(paramName, values);
        }
        return map;
    }
}
