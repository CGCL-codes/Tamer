package org.atlantal.api.portal.ptl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletResponse;
import org.atlantal.utils.wrapper.ObjectWrapper;

/**
 * @author f.masurel
 */
public interface RootPortlet extends Portlet {

    /** HTML_401_STRICT */
    int HTML_401_STRICT = 0;

    /** HTML_401_TRANSITIONAL */
    int HTML_401_TRANSITIONAL = 1;

    /**
     * @return Returns the docType.
     */
    String getDocType();

    /**
     * @return Returns the contentType.
     */
    String getContentType();

    /**
     * @return Returns the charSet.
     */
    String getCharSet();

    /**
     * @param request rundata
     * @return Returns the page title.
     */
    String getPageTitle(RenderRequest request);

    /**
     * getDefaultPortlet
     * @return PortletWrapper
     */
    String getDefaultPortlet();

    /**
     * getContentPortlet
     * @param request rundata
     * @return PortletWrapper
     */
    ObjectWrapper getContentPortlet(PortletRequest request);

    /**
     * getContentPortletObject
     * @param request rundata
     * @return PortletInstance
     */
    Portlet getContentPortletObject(PortletRequest request);

    /**
     * setContentPortlet
     * @param request rundata
     * @param contentptl contentptl
     */
    void setContentPortlet(PortletRequest request, ObjectWrapper contentptl);

    /**
     * HtmlIncludeScripts
     * @param html html
     * @throws IOException IOException
     */
    void htmlContentType(Writer html) throws IOException;

    /**
     * HtmlIncludeScripts
     * @param request TODO
     * @param response TODO
     * @param html html
     * @throws IOException IOException
     */
    void htmlURIScript(RenderRequest request, RenderResponse response, Writer html) throws IOException;

    /**
     * HtmlIncludeStyles
     * @param request TODO
     * @param html html
     * @throws IOException IOException
     */
    void htmlIncludedStyles(RenderRequest request, Writer html) throws IOException;

    /**
     * HtmlIncludeScripts
     * @param request TODO
     * @param html html
     * @throws IOException IOException
     */
    void htmlIncludedScripts(RenderRequest request, Writer html) throws IOException;

    /**
     * HtmlScripts
     * @param request TODO
     * @param html html
     * @throws IOException IOException
     */
    void htmlEventsScript(RenderRequest request, Writer html) throws IOException;

    /**
     * HtmlFunctionScripts
     * @param request TODO
     * @param html html
     * @throws IOException IOException
     */
    void htmlFunctionScripts(RenderRequest request, Writer html) throws IOException;

    /**
     * InitResponse
     * @param request rundata
     * @param response Http servlet response
     */
    void initResponse(PortletRequest request, HttpServletResponse response);

    /**
     * isBinary
     * @param request rundata
     * @return isBinary
     */
    boolean isBinary(PortletRequest request);

    /**
     * Write
     * @param request rundata
     * @param response TODO
     * @param os Http servlet output stream
     * @throws PortletException TODO
     */
    void write(RenderRequest request, RenderResponse response, OutputStream os) throws PortletException;
}
