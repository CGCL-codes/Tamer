package com.acv.webapp.listener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.ProviderManager;
import org.acegisecurity.providers.encoding.Md5PasswordEncoder;
import org.acegisecurity.providers.rememberme.RememberMeAuthenticationProvider;
import org.apache.log4j.Logger;
import org.apache.struts2.views.freemarker.FreemarkerManager;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.acv.dao.common.Constants;
import com.acv.service.security.SecurityLookupManager;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * <p>StartupListener class used to initialize and database settings
 * and populate any application-wide drop-downs.
 *
 * <p>Keep in mind that this listener is executed outside of OpenSessionInViewFilter,
 * so if you're using Hibernate you'll have to explicitly initialize all loaded data at the
 * Dao or service level to avoid LazyInitializationException. Hibernate.initialize() works
 * well for doing this.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@SuppressWarnings("unchecked")
public class StartupListener extends ContextLoaderListener implements ServletContextListener {

    private static final Logger log = Logger.getLogger(StartupListener.class);

    public void contextInitialized(ServletContextEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("initializing context...");
        }
        super.contextInitialized(event);
        ServletContext context = event.getServletContext();
        Map<String, Object> config = (HashMap<String, Object>) context.getAttribute(Constants.CONFIG);
        if (config == null) {
            config = new HashMap<String, Object>();
        }
        if (context.getInitParameter(Constants.CSS_THEME) != null) {
            config.put(Constants.CSS_THEME, context.getInitParameter(Constants.CSS_THEME));
        }
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        boolean encryptPassword = false;
        try {
            ProviderManager provider = (ProviderManager) ctx.getBean("authenticationManager");
            for (Iterator it = provider.getProviders().iterator(); it.hasNext(); ) {
                AuthenticationProvider p = (AuthenticationProvider) it.next();
                if (p instanceof RememberMeAuthenticationProvider) {
                    config.put("rememberMeEnabled", Boolean.TRUE);
                }
            }
            if (ctx.containsBean("passwordEncoder")) {
                encryptPassword = true;
                config.put(Constants.ENCRYPT_PASSWORD, Boolean.TRUE);
                String algorithm = "SHA";
                if (ctx.getBean("passwordEncoder") instanceof Md5PasswordEncoder) {
                    algorithm = "MD5";
                }
                config.put(Constants.ENC_ALGORITHM, algorithm);
            }
        } catch (NoSuchBeanDefinitionException n) {
        }
        context.setAttribute(Constants.CONFIG, config);
        if (log.isDebugEnabled()) {
            log.debug("Remember Me Enabled? " + config.get("rememberMeEnabled"));
            log.debug("Encrypt Passwords? " + encryptPassword);
            if (encryptPassword) {
                log.debug("Encryption Algorithm: " + config.get(Constants.ENC_ALGORITHM));
            }
            log.debug("Populating drop-downs...");
        }
        setupContext(context);
        try {
            FreemarkerManager freemarkerManager = new FreemarkerManager();
            Configuration configuration = freemarkerManager.getConfiguration(context);
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            log.info("Freemarker exception handler is " + configuration.getTemplateExceptionHandler().getClass().getName() + " any exception during freemarker template processing will be logged in the log file but NEVER in the outputed HTML");
        } catch (TemplateException e) {
            log.error("Attempt to put FreeMarker exception mode into 'IGNORE' failed !", e);
        }
    }

    public static void setupContext(ServletContext context) {
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        SecurityLookupManager mgr = (SecurityLookupManager) ctx.getBean("lookupManager");
        context.setAttribute(Constants.AVAILABLE_ROLES, mgr.getAllRoles());
        if (log.isDebugEnabled()) {
            log.debug("Drop-down initialization complete [OK]");
        }
    }
}
