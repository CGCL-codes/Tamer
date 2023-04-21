package zeroj.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import zeroj.orm.ibatis.IbatisSupport;

/**
 * 
 * @author lyl
 *
 * 2008-9-12
 */
public class CloseSessionViewFilter implements Filter {

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(req, res);
            IbatisSupport.commit();
        } finally {
            IbatisSupport.closeSession();
        }
    }

    public void init(FilterConfig config) throws ServletException {
        IbatisSupport.existSession();
    }
}
