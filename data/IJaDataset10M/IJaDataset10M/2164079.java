package examples.page;

import javax.servlet.http.HttpServletRequest;
import org.t2framework.t2.annotation.core.ActionPath;
import org.t2framework.t2.annotation.core.Default;
import org.t2framework.t2.annotation.core.Page;
import org.t2framework.t2.contexts.Request;
import org.t2framework.t2.navigation.Forward;
import org.t2framework.t2.spi.Navigation;

@Page("/hello56")
public class HelloPage56 {

    @Default
    public Navigation index(Request request) {
        request.setAttribute("greet", "hello");
        return Forward.to("/jsp/hello56.jsp");
    }

    @ActionPath
    public Navigation request(HttpServletRequest request) {
        System.out.println("request.getContextPath() : " + request.getContextPath());
        request.setAttribute("greet", "hellos from request().");
        return Forward.to("/jsp/hello56.jsp");
    }
}
