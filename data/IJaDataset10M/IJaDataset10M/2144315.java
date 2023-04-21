package self.servlet.filter;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * Filter to provide a log of SVN requests and responses.
 */
public final class SvnFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(SvnFilter.class);

    /**
     * Place this filter into service.
     * @param filterConfig The filter configuration object
     */
    public void init(FilterConfig config) throws ServletException {
    }

    /**
     * Take this filter out of service.
     */
    public void destroy() {
    }

    /**
     * filter a request
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        SvnResponseWrapper wrapper = new SvnResponseWrapper(httpServletResponse);
        SvnRequestWrapper reqwrapper = new SvnRequestWrapper(httpServletRequest);
        try {
            chain.doFilter(reqwrapper, wrapper);
        } catch (IOException ioE) {
            LOGGER.info("Request [" + reqwrapper.getMethod() + "] <" + reqwrapper.getPathInfo() + "> " + reqwrapper.getContent());
            LOGGER.error(ioE, ioE);
            throw ioE;
        } catch (ServletException sE) {
            LOGGER.info("Request [" + reqwrapper.getMethod() + "] <" + reqwrapper.getPathInfo() + "> " + reqwrapper.getContent());
            LOGGER.error(sE, sE.getCause());
            throw sE;
        } catch (RuntimeException rE) {
            LOGGER.info("Request [" + reqwrapper.getMethod() + "] <" + reqwrapper.getPathInfo() + "> " + reqwrapper.getContent());
            LOGGER.error(rE, rE);
            throw rE;
        }
        String pageContent = wrapper.toString();
        String contentType = wrapper.getContentType();
        httpServletResponse.setContentType(contentType);
        httpServletResponse.setContentLength(pageContent.length());
        LOGGER.info("Request [" + reqwrapper.getMethod() + "] <" + reqwrapper.getPathInfo() + "> " + reqwrapper.getContent());
        LOGGER.info("Response Content:" + pageContent);
        PrintWriter out = httpServletResponse.getWriter();
        out.write(pageContent);
        out.close();
    }
}
