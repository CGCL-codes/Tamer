package nl.gridshore.samples.books.web.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Abstract implementation to be used by concrete implementations that need to return a property file
 * to the client app calling.</p>
 * <p>This controller makes use of the <code>PropertyView</code> class. You can configure this in your configuration
 * by providing a view resolver</p>
 * <pre>
 *   &lt;bean id="defaultViewResolver" class="org.springframework.web.servlet.view.ResourceBundleViewResolver">
 *     &lt;property name="basename" value="views"/>
 *     &lt;property name="order" value="1"/>
 *   &lt;/bean>
 * </pre>
 * <p>This view resolver looks for a file called <strong>views.properties</strong> in the classpath. This file
 * combines the name of the view to the class.
 * <pre>
 * propertiesView.class=nl.gridshore.samples.books.web.view.PropertyView
 * </pre>
 *
 * @author jettro coenradie
 *         Date: Jan 23, 2009
 */
public abstract class PropertyController implements Controller {

    private static Logger logger = LoggerFactory.getLogger(PropertyController.class);

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("Handle the request for {}", request.getRequestURI());
        Map<String, String> exposedParams = createExposedParamsMap(request);
        return new ModelAndView("propertiesView", "exposedParams", exposedParams);
    }

    protected abstract Map<String, String> createExposedParamsMap(HttpServletRequest request);
}
