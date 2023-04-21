package org.beanopen.app.engine.defaults;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.beanopen.app.engine.ExecutionChain;

/**
 * Action请求处理器
 * @author Bean
 *
 */
public class ActionExecution extends AbstractExecution {

    private final Logger logger = Logger.getLogger(ActionExecution.class);

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, ExecutionChain chain) throws IOException, ServletException {
        logger.debug("action...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
