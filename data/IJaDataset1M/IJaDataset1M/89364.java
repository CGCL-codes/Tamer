package net.sf.balm.workflow.jbpm3.support;

import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.JbpmException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.util.Assert;

/**
 * @author dz
 */
public class JbpmTemplate implements InitializingBean {

    private static Log logger = LogFactory.getLog(JbpmTemplate.class);

    private JbpmConfiguration jbpmConfiguration;

    private HibernateTemplate hibernateTemplate;

    private String contextName = JbpmContext.DEFAULT_JBPM_CONTEXT_NAME;

    public JbpmTemplate() {
    }

    public JbpmTemplate(JbpmConfiguration jbpmConfiguration) {
        setJbpmConfiguration(jbpmConfiguration);
    }

    public JbpmConfiguration getJbpmConfiguration() {
        return jbpmConfiguration;
    }

    public void setJbpmConfiguration(JbpmConfiguration jbpmConfiguration) {
        this.jbpmConfiguration = jbpmConfiguration;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public Object execute(final JbpmCallback callback) {
        final JbpmContext context = getContext();
        try {
            return hibernateTemplate.execute(new HibernateCallback() {

                public Object doInHibernate(Session session) throws HibernateException, SQLException {
                    context.setSession(session);
                    return callback.doInProcess(context);
                }
            });
        } catch (JbpmException ex) {
            throw convertJbpmException(ex);
        } finally {
            context.close();
        }
    }

    public JbpmContext getContext() {
        return jbpmConfiguration.createJbpmContext(contextName);
    }

    public RuntimeException convertJbpmException(JbpmException ex) {
        if (ex.getCause() instanceof HibernateException) {
            return SessionFactoryUtils.convertHibernateAccessException((HibernateException) ex.getCause());
        }
        return ex;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(jbpmConfiguration, "jbpmConfiguration must be set");
        JbpmContext jbpmContext = getContext();
        try {
            if (hibernateTemplate == null && jbpmContext.getSessionFactory() != null) {
                logger.debug("creating hibernateTemplate based on jBPM SessionFactory");
                hibernateTemplate = new HibernateTemplate(jbpmContext.getSessionFactory());
            } else {
                Assert.notNull(hibernateTemplate, "setup hibernateTemplate failed!");
            }
        } finally {
            jbpmContext.close();
        }
    }
}
