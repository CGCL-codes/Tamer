package filtros;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class ConexionFilter
 */
public class ConexionFilter implements Filter {

    /**
     * Default constructor. 
     */
    public ConexionFilter() {
    }

    /**
	 * @see Filter#destroy()
	 */
    public void destroy() {
    }

    /**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Connection connection;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/libreria", "root", "root");
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            httpRequest.getSession().setAttribute("connection", connection);
            chain.doFilter(request, response);
            connection.close();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /**
	 * @see Filter#init(FilterConfig)
	 */
    public void init(FilterConfig fConfig) throws ServletException {
    }
}
