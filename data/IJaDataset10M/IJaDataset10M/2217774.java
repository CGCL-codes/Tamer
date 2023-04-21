package org.owasp.esapi.waf.rules;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.esapi.waf.actions.Action;
import org.owasp.esapi.waf.actions.DefaultAction;
import org.owasp.esapi.waf.actions.DoNothingAction;
import org.owasp.esapi.waf.internal.InterceptingHTTPServletResponse;

/**
 * This is the Rule subclass executed for &lt;restrict-method&gt; rules.
 * @author Arshan Dabirsiaghi
 *
 */
public class HTTPMethodRule extends Rule {

    private Pattern allowedMethods;

    private Pattern deniedMethods;

    private Pattern path;

    public HTTPMethodRule(String id, Pattern allowedMethods, Pattern deniedMethods, Pattern path) {
        this.allowedMethods = allowedMethods;
        this.deniedMethods = deniedMethods;
        this.path = path;
        setId(id);
    }

    public Action check(HttpServletRequest request, InterceptingHTTPServletResponse response, HttpServletResponse httpResponse) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        if (path == null || path.matcher(uri).matches()) {
            if (allowedMethods != null && allowedMethods.matcher(method).matches()) {
                return new DoNothingAction();
            } else if (allowedMethods != null) {
                log(request, "Disallowed HTTP method '" + request.getMethod() + "' found for URL: " + request.getRequestURL());
                return new DefaultAction();
            }
            if (deniedMethods != null && deniedMethods.matcher(method).matches()) {
                log(request, "Disallowed HTTP method '" + request.getMethod() + "' found for URL: " + request.getRequestURL());
                return new DefaultAction();
            }
        }
        return new DoNothingAction();
    }
}
