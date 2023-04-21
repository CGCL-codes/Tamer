package net.livebookstore.web.core;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Template class for handling request that expect xml output.
 * 
 * @author xuefeng
 */
public abstract class AbstractXmlController extends AbstractBaseController {

    public final ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String xml = getXml(request, response);
        response.setContentType("text/xml;charset=UTF-8");
        int cache = getCacheTime(request, response);
        if (cache <= 0) response.setHeader("Cache-Control", "no-cache"); else {
            response.setHeader("Cache-Control", "Private");
            response.addDateHeader("Expires", System.currentTimeMillis() + 1000 * cache);
        }
        PrintWriter writer = response.getWriter();
        writer.write(xml);
        writer.flush();
        return null;
    }

    /**
     * Get xml content.
     */
    public abstract String getXml(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * Get cache time. How many seconds to cache in client.
     */
    public abstract int getCacheTime(HttpServletRequest request, HttpServletResponse response);
}
