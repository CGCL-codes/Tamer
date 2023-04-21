package org.vqwiki.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.vqwiki.Environment;
import org.vqwiki.Topic;
import org.vqwiki.WikiBase;

/**
 * The <code>ViewTopicController</code> servlet is the servlet which allows users to view topics and
 * makes sure that responses are dispatched to the appropriate views.
 *
 * This controller uses methods from the WikiBase class to parse the URI and determine which virtual wiki
 * and which topic are requested. The URI should be formed as below:
 *
 * http[s]://www.somesite.com/<context-root>/<virtualwiki>/<action>/<topic>.html
 */
@Controller
@RequestMapping("/**/css/*.css")
public class StyleSheetController {

    /** Logger for this class and subclasses. */
    private static Logger logger = LoggerFactory.getLogger(StyleSheetController.class);

    private List<String> sheetCheck = new ArrayList<String>(4);

    @Autowired
    private WikiBase wb;

    @Autowired
    private Environment environment;

    @Autowired
    public StyleSheetController(Environment environment) {
        sheetCheck.add(environment.getProperty(Environment.PROP_SPECIAL_ADMINSTYLE).getValue());
        sheetCheck.add(environment.getProperty(Environment.PROP_SPECIAL_CORESTYLE).getValue());
        sheetCheck.add(environment.getProperty(Environment.PROP_SPECIAL_CONTENTSTYLE).getValue());
        sheetCheck.add(environment.getProperty(Environment.PROP_SPECIAL_SETUPSTYLE).getValue());
    }

    /**
     * This method handles the request after its parent class receives control. It gets the topic's name and the
     * virtual wiki name from the uri, loads the topic and returns a view to the end user.
     *
     * @param request - Standard HttpServletRequest object.
     * @param response - Standard HttpServletResponse object.
     * @return A <code>ModelAndView</code> object to be handled by the rest of the Spring framework.
     */
    @RequestMapping(method = RequestMethod.GET)
    public final ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Map<String, String> model = new HashMap<String, String>();
        StringBuffer view = new StringBuffer();
        String vwiki = wb.getVirtualWikiFromURI(request.getRequestURI(), request.getContextPath());
        String stylesheetname = wb.getStyleSheetFromURI(request.getRequestURI());
        Topic stylesheet = null;
        if (sheetCheck.contains(stylesheetname)) {
            stylesheet = new Topic(stylesheetname);
            stylesheet.loadTopic(wb.getHandler(), vwiki);
            model.put("stylesheetcontent", stylesheet.getRawContent(request));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "bogus stylesheet name");
            return null;
        }
        logger.debug("Handling request for view of stylesheet: " + stylesheetname);
        view.append(environment.getProperty(Environment.PROP_BASE_DEFAULT_THEME).getValue()).append("/stylesheet");
        return new ModelAndView(view.toString(), "model", model);
    }
}
