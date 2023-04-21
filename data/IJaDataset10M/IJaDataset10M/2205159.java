package net.sourceforge.xconf.toolbox.spring.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * A default exception resolver that simply logs any exception and then
 * returns a the configured error view, as well as a reference number that
 * can be used to match the exception in the log file with an error reported
 * by a user upon viewing the error view.
 *
 * @author Tom Czarniecki
 */
public class UnhandledExceptionResolver implements HandlerExceptionResolver, Ordered {

    /**
     * Model key for the error reference value.
     */
    public static final String ERROR_REF = "errorRef";

    private final Log logger = LogFactory.getLog(UnhandledExceptionResolver.class);

    private String errorView;

    private int order = Integer.MAX_VALUE;

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setErrorView(String errorView) {
        this.errorView = errorView;
    }

    /**
     * Creates a random error reference, logs the given exception with the reference number and request URI,
     * and returns a <code>ModelAndView</code> with the configured error view and generated error reference.
     */
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String errorRef = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
        logger.error("errorRef: " + errorRef + ", requestURI: " + request.getRequestURI(), ex);
        return new ModelAndView(errorView, ERROR_REF, errorRef);
    }
}
