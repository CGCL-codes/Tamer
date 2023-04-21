package net.sf.jtmt.httpwrapper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections15.map.CaseInsensitiveMap;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.handler.AbstractHandler;

/**
 * TODO: Class level Javadocs
 * @author Sujit Pal
 * @version $Revision: 28 $
 */
public class JohHandler extends AbstractHandler {

    private Map<String, Method> methodMap = new CaseInsensitiveMap<Method>();

    private Object javaObject;

    public JohHandler(Object obj) {
        super();
        this.javaObject = obj;
        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods) {
            methodMap.put(method.getName(), method);
        }
    }

    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
        Request req = (request instanceof Request ? (Request) request : HttpConnection.getCurrentConnection().getRequest());
        String methodName = request.getRequestURI().substring(1);
        if (methodMap.containsKey(methodName)) {
            Method method = methodMap.get(methodName);
            try {
                method.invoke(javaObject, new Object[] { request, response });
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (InvocationTargetException e) {
                Joh.error(e, request, response);
            } catch (IllegalAccessException e) {
                Joh.error(e, request, response);
            }
        } else {
            Joh.error(new Exception("No such method: " + methodName), request, response);
        }
        req.setHandled(true);
    }
}
