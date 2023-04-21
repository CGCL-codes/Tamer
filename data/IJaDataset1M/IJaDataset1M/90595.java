package org.cloudlet.web.service.server.jpa;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Singleton
public class DatabaseConnectionFilter implements Filter {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final Provider<HttpServletRequest> req;

    @Inject
    public DatabaseConnectionFilter(final Provider<HttpServletRequest> req) {
        this.req = req;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            Connection conn = (Connection) req.get().getAttribute(DatabaseConnectionProvider.CONNECTION_KEY);
            if (conn == null) {
                return;
            }
            try {
                conn.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "关闭数据库连接出错", e);
            }
        }
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }
}
