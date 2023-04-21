package shake.filter;

import java.io.IOException;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import shake.annotation.FilterComponent;
import shake.annotation.Sort;
import shake.context.RedirectManager;
import shake.servlet.ShakeServletContext;
import com.google.inject.Inject;
import com.google.inject.Injector;

@Sort(within = GuiceFilter.class, around = Ajax4JsfFilter.class)
@FilterComponent
public class RedirectFilter extends AbstractFilter {

    @Inject
    public RedirectFilter(Filters filters) {
        super(filters);
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Injector injector = ShakeServletContext.getInjector();
        RedirectManager manager = injector.getInstance(RedirectManager.class);
        Map<String, Object> map = manager.restore(request.getParameter("redirect"));
        ResponseWrapper wrapper = new ResponseWrapper((HttpServletResponse) response);
        chain.doFilter(request, wrapper);
        if (wrapper.redirect == false) {
            manager.destory(map);
        }
    }

    public static class ResponseWrapper extends HttpServletResponseWrapper {

        boolean redirect = false;

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void sendRedirect(String location) throws IOException {
            Injector injector = ShakeServletContext.getInjector();
            String r = injector.getInstance(RedirectManager.class).addRedirectid(location);
            super.sendRedirect(r);
            redirect = true;
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
