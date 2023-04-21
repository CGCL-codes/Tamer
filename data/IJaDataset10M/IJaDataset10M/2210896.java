package org.owasp.esapi.waf.rules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.waf.actions.Action;
import org.owasp.esapi.waf.actions.DoNothingAction;
import org.owasp.esapi.waf.internal.InterceptingHTTPServletResponse;
import bsh.EvalError;
import bsh.Interpreter;

/**
 * This is the Rule subclass executed for &lt;bean-shell-script&gt; rules.
 * @author Arshan Dabirsiaghi
 *
 */
public class BeanShellRule extends Rule {

    private Interpreter i;

    private String script;

    private Pattern path;

    public BeanShellRule(String fileLocation, String id, Pattern path) throws IOException, EvalError {
        i = new Interpreter();
        i.set("logger", logger);
        this.script = getFileContents(ESAPI.securityConfiguration().getResourceFile(fileLocation));
        this.id = id;
        this.path = path;
    }

    public Action check(HttpServletRequest request, InterceptingHTTPServletResponse response, HttpServletResponse httpResponse) {
        if (path != null && !path.matcher(request.getRequestURI()).matches()) {
            return new DoNothingAction();
        }
        try {
            Action a = null;
            i.set("action", a);
            i.set("request", request);
            if (response != null) {
                i.set("response", response);
            } else {
                i.set("response", httpResponse);
            }
            i.set("session", request.getSession());
            i.eval(script);
            a = (Action) i.get("action");
            if (a != null) {
                return a;
            }
        } catch (EvalError e) {
            log(request, "Error running custom beanshell rule (" + id + ") - " + e.getMessage());
        }
        return new DoNothingAction();
    }

    private String getFileContents(File f) throws IOException {
        FileReader fr = new FileReader(f);
        StringBuffer sb = new StringBuffer();
        String line;
        BufferedReader br = new BufferedReader(fr);
        while ((line = br.readLine()) != null) {
            sb.append(line + System.getProperty("line.separator"));
        }
        return sb.toString();
    }
}
