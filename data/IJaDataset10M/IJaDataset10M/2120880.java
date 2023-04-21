package pl.common;

import java.util.Enumeration;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pl.common.console.Console;

public class ContexListener implements ServletContextListener {

    protected final Log log = LogFactory.getLog(getClass());

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.debug("contextDestroyed");
        try {
            Enumeration e = sce.getServletContext().getAttributeNames();
            WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
            log.debug("wac: " + wac);
            ((Console) wac.getBean("console")).stop();
        } catch (BeansException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("contextInitialized");
        try {
            WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
            log.debug("wac: " + wac);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
