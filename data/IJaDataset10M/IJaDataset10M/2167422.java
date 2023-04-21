package org.exist.http.urlrewrite;

import org.w3c.dom.Element;
import org.exist.xquery.Expression;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.XPathException;
import org.exist.xquery.Module;
import org.exist.xquery.UserDefinedFunction;
import org.exist.xquery.ExternalModule;
import org.exist.xquery.FunctionCall;
import org.exist.xquery.value.Sequence;
import org.exist.dom.QName;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;

public class ModuleCall extends URLRewrite {

    private static final Logger LOG = Logger.getLogger(ModuleCall.class);

    private FunctionCall call;

    public ModuleCall(Element config, XQueryContext context, String uri) throws ServletException {
        super(config, uri);
        String funcName = config.getAttribute("function");
        if (funcName == null || funcName.length() == 0) throw new ServletException("<exist:call> requires an attribute 'function'.");
        int arity = 0;
        int p = funcName.indexOf('/');
        if (p > -1) {
            String arityStr = funcName.substring(p + 1);
            try {
                arity = Integer.parseInt(arityStr);
            } catch (NumberFormatException e) {
                throw new ServletException("<exist:call>: could not parse parameter count in function attribute: " + arityStr);
            }
            funcName = funcName.substring(0, p);
        }
        try {
            QName fqn = QName.parse(context, funcName);
            Module module = context.getModule(fqn.getNamespaceURI());
            UserDefinedFunction func = null;
            if (module != null) func = ((ExternalModule) module).getFunction(fqn, arity); else func = context.resolveFunction(fqn, arity);
            call = new FunctionCall(context, func);
            call.setArguments(new ArrayList<Expression>());
        } catch (XPathException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doRewrite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Sequence result = call.eval(null);
            LOG.debug("Found: " + result.getItemCount());
            request.setAttribute(XQueryURLRewrite.RQ_ATTR_RESULT, result);
        } catch (XPathException e) {
            throw new ServletException("Called function threw exception: " + e.getMessage(), e);
        }
    }
}
