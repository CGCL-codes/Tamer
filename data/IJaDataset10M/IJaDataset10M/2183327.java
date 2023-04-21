package nl.gridshore.samples.books.web.controller;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.HashMap;

/**
 * <p>Specific <code>PropertyController</code> for the config.properties request.</p>
 * <p>This controller returns a Map with three properties</p>
 * <ul>
 * <li>host</li>
 * <li>port</li>
 * <li>context-root</li>
 * </ul>
 * <p>The parameters are obtained from the provided <code>HttpServletRequest</code></p>
 *
 * @author jettro coenradie
 *         Date: Jan 23, 2009
 */
public class ConfigPropertyController extends PropertyController {

    protected Map<String, String> createExposedParamsMap(HttpServletRequest request) {
        Map<String, String> exposedParams = new HashMap<String, String>();
        exposedParams.put("host", request.getServerName());
        exposedParams.put("port", String.valueOf(request.getServerPort()));
        String contextRoot = request.getContextPath();
        if (contextRoot.length() > 0) {
            contextRoot = contextRoot.substring(1);
        }
        exposedParams.put("context-root", contextRoot);
        return exposedParams;
    }
}
