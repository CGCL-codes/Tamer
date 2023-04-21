package com.technoetic.xplanner.tags;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import org.springframework.web.context.request.ServletRequestAttributes;

public class LinkTag extends org.apache.struts.taglib.html.LinkTag {

    private Logger log = Logger.getLogger(getClass());

    /**
     * The full HREF URL
     */
    private StringBuffer hrefURL = new StringBuffer();

    /**
     * Tag ID
     */
    private String id;

    /**
     * Foreign Key for returnto link
     */
    private int fkey;

    /**
     * Flag for determining if projectId parameter is included.
     */
    private boolean includeProjectId = true;

    private boolean useReturnto = false;

    private boolean removeQuotes = false;

    private char accessKey;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUseReturnto(boolean useReturnto) {
        this.useReturnto = useReturnto;
    }

    /**
     * Intialize the hyperlink.
     *
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        if (linkName != null) {
            StringBuffer results = new StringBuffer("<a name=\"");
            results.append(linkName);
            results.append("\">");
            return (EVAL_BODY_BUFFERED);
        }
        Map params = RequestUtils.computeParameters(pageContext, paramId, paramName, paramProperty, paramScope, name, property, scope, transaction);
        params = addNavigationParameters(params);
        String url = null;
        try {
            url = RequestUtils.computeURL(pageContext, forward, href, page, params, anchor, false);
        } catch (MalformedURLException e) {
            RequestUtils.saveException(pageContext, e);
            throw new JspException(messages.getMessage("rewrite.url", e.toString()));
        }
        hrefURL = new StringBuffer("<a href=\"");
        hrefURL.append(url);
        if (log.isDebugEnabled()) log.debug("hrefURL = '" + hrefURL.toString());
        this.text = null;
        return (EVAL_BODY_BUFFERED);
    }

    private Map addNavigationParameters(Map parameters) throws JspTagException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        if (parameters == null) {
            parameters = new HashMap();
        }
        String returnToUri = null;
        if (useReturnto) {
            returnToUri = request.getParameter("returnto");
            if (returnToUri == null) returnToUri = "/do/view/projects";
        } else {
            ActionMapping mapping = (ActionMapping) request.getAttribute(Globals.MAPPING_KEY);
            if (mapping == null) {
                returnToUri = ((ServletRequestAttributes) request.getAttribute("org.springframework.web.context.request.RequestContextListener.REQUEST_ATTRIBUTES")).getRequest().getRequestURI();
            } else {
                returnToUri = "/do" + mapping.getPath();
                String oid = request.getParameter("oid");
                if (oid != null) {
                    returnToUri += "?oid=" + oid;
                    parameters.put("fkey", fkey == 0 ? oid : Integer.toString(fkey));
                }
                if (includeProjectId) {
                    DomainContext context = DomainContext.get(request);
                    if (context != null) {
                        int projectId = context.getProjectId();
                        String projectIdParam = request.getParameter("projectId");
                        if (projectId == 0 && projectIdParam != null) {
                            projectId = Integer.parseInt(projectIdParam);
                        }
                        parameters.put("projectId", new Integer(projectId));
                    }
                }
            }
        }
        parameters.put("returnto", returnToUri);
        return parameters;
    }

    /**
     * Add a new parameter to the request
     *
     * @param name  the name of the request parameter
     * @param value the value of the request parameter
     */
    public void addRequestParameter(String name, String value) {
        if (log.isDebugEnabled()) log.debug("Adding '" + name + "' with value '" + value + "'");
        boolean question = (hrefURL.toString().indexOf('?') >= 0);
        if (question) {
            hrefURL.append('&');
        } else {
            hrefURL.append('?');
        }
        hrefURL.append(name);
        hrefURL.append('=');
        try {
            hrefURL.append(URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
        }
        if (log.isDebugEnabled()) log.debug("hrefURL = '" + hrefURL.toString() + "'");
    }

    /**
     * Render the href reference
     *
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {
        hrefURL.append("\"");
        if (target != null) {
            hrefURL.append(" target=\"");
            hrefURL.append(target);
            hrefURL.append("\"");
        }
        if (id != null) {
            hrefURL.append(" id=\"");
            hrefURL.append(id);
            hrefURL.append("\"");
        }
        hrefURL.append(AccessKeyTransformer.getHtml(text));
        hrefURL.append(prepareStyles());
        hrefURL.append(prepareEventHandlers());
        hrefURL.append(">");
        hrefURL.append(AccessKeyTransformer.removeMnemonicMarkers(text));
        hrefURL.append("</a>");
        if (log.isDebugEnabled()) log.debug("Total request is = '" + hrefURL.toString() + "'");
        String url = hrefURL.toString();
        if (removeQuotes) {
            url = url.replaceAll("\"", "");
        }
        ResponseUtils.write(pageContext, url);
        return (EVAL_PAGE);
    }

    /**
     * Release any acquired resources.
     */
    public void release() {
        super.release();
        forward = null;
        href = null;
        name = null;
        property = null;
        target = null;
        fkey = 0;
        includeProjectId = false;
        removeQuotes = false;
    }

    /**
     * Return the specified hyperlink, modified as necessary with optional
     * request parameters.
     *
     * @throws JspException if an error occurs preparing the hyperlink
     */
    protected String hyperlink() throws JspException {
        String href = this.href;
        if (forward != null) {
            ModuleConfig moduleConfig = TagUtils.getInstance().getModuleConfig(pageContext);
            if (moduleConfig == null) {
                throw new JspException(messages.getMessage("linkTag.forwards"));
            }
            ForwardConfig forward = moduleConfig.findForwardConfig(this.forward);
            ;
            if (forward == null) {
                throw new JspException(messages.getMessage("linkTag.forward"));
            }
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            href = request.getContextPath() + forward.getPath();
        }
        if ((property != null) && (name == null)) {
            throw new JspException(messages.getMessage("getter.name"));
        }
        if (name == null) {
            return (href);
        }
        Object bean = pageContext.findAttribute(name);
        if (bean == null) {
            throw new JspException(messages.getMessage("getter.bean", name));
        }
        Map map = null;
        if (property == null) {
            try {
                map = (Map) bean;
            } catch (ClassCastException e) {
                throw new JspException(messages.getMessage("linkTag.type"));
            }
        } else {
            try {
                map = (Map) PropertyUtils.getProperty(bean, property);
                if (map == null) {
                    throw new JspException(messages.getMessage("getter.property", property));
                }
            } catch (IllegalAccessException e) {
                throw new JspException(messages.getMessage("getter.access", property, name));
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                throw new JspException(messages.getMessage("getter.result", property, t.toString()));
            } catch (ClassCastException e) {
                throw new JspException(messages.getMessage("linkTag.type"));
            } catch (NoSuchMethodException e) {
                throw new JspException(messages.getMessage("getter.method", property, name));
            }
        }
        StringBuffer sb = new StringBuffer(href);
        boolean question = (href.indexOf("?") >= 0);
        Iterator keys = map.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            Object value = map.get(key);
            if (value instanceof String[]) {
                String values[] = (String[]) value;
                for (int i = 0; i < values.length; i++) {
                    if (question) {
                        sb.append('&');
                    } else {
                        sb.append('?');
                        question = true;
                    }
                    sb.append(key);
                    sb.append('=');
                    try {
                        sb.append(URLEncoder.encode(values[i], "UTF-8"));
                    } catch (UnsupportedEncodingException ex) {
                    }
                }
            } else {
                if (question) {
                    sb.append('&');
                } else {
                    sb.append('?');
                    question = true;
                }
                sb.append(key);
                sb.append('=');
                try {
                    sb.append(URLEncoder.encode(value.toString(), "UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                }
            }
        }
        return (sb.toString());
    }

    public int getFkey() {
        return fkey;
    }

    public void setFkey(int fkey) {
        this.fkey = fkey;
    }

    public String isIncludeProjectId() {
        return new Boolean(includeProjectId).toString();
    }

    public void setIncludeProjectId(String includeProjectId) {
        this.includeProjectId = new Boolean(includeProjectId).booleanValue();
    }

    public void setRemoveQuotes(boolean removeQuotes) {
        this.removeQuotes = removeQuotes;
    }
}
