package org.broadleafcommerce.web;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This filter provides a method to override the default redirect behavior of Spring MVC,
 * which is to deliver a Temp Redirect (302), instead of a permanent redirect (301).
 * This filter is configured with one or more white-space delimited regular expression
 * patterns that match any request URIs whose response status should be checked for 302
 * and converted to 301, if applicable.
 * 
 * This filter should appear before the Spring Dispatch Servlet in the mapping configuration
 * in web.xml
 * 
 * @author jfischer
 *
 */
public class SpringTemporaryRedirectOverrideFilter implements Filter {

    private Pattern[] urlPatterns = new Pattern[] {};

    public void destroy() {
    }

    public boolean isUrlMatch(String url) {
        for (Pattern pattern : urlPatterns) {
            Matcher matcher = pattern.matcher(url);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        BroadleafResponseWrapper responseWrapper = new BroadleafResponseWrapper((HttpServletResponse) response);
        chain.doFilter(request, responseWrapper);
        if (response instanceof BroadleafResponseWrapper && 302 == ((BroadleafResponseWrapper) response).getStatus() && isUrlMatch(((HttpServletRequest) request).getRequestURI())) {
            ((HttpServletResponse) response).setStatus(301);
        }
    }

    public void init(FilterConfig config) throws ServletException {
        String massagedString = config.getInitParameter("urlPatterns").replaceAll("\\s+", " ");
        String[] temp = massagedString.split("\\s");
        urlPatterns = new Pattern[temp.length];
        for (int j = 0; j < urlPatterns.length; j++) {
            urlPatterns[j] = Pattern.compile(temp[j]);
        }
    }
}
