package br.com.caelum.jambo.interceptors;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.vraptor.Interceptor;
import org.vraptor.LogicException;
import org.vraptor.LogicFlow;
import org.vraptor.view.ViewException;

public class TrimmerInterceptor implements Interceptor {

    private static final Logger logger = Logger.getLogger(TrimmerInterceptor.class);

    public void intercept(LogicFlow flow) throws LogicException, ViewException {
        HttpServletRequest request = flow.getLogicRequest().getRequest();
        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = request.getParameter(name);
            logger.debug("Trimming parameter " + name + " with value '" + value + "'");
            request.setAttribute("_" + name, value.trim());
        }
        flow.execute();
    }
}
