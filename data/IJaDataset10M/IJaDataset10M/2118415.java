package de.powerstaff.web.utils;

import com.sun.servicetag.SystemEnvironment;
import de.powerstaff.business.service.DomainHelper;
import de.powerstaff.business.service.ProfileIndexerService;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class StartupServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent aEvent) {
        WebApplicationContext theContext = WebApplicationContextUtils.getWebApplicationContext(aEvent.getServletContext());
        DomainHelper.getInstance().registerApplicationContext(theContext);
        ProfileIndexerService theService = (ProfileIndexerService) theContext.getBean("profileIndexerService");
        theService.rebuildIndex();
    }

    @Override
    public void contextDestroyed(ServletContextEvent aEvent) {
    }
}
