package org.nexopenframework.workflow.providers.jbpm31;

import java.util.Map;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.jbpm.JbpmConfiguration;
import org.nexopenframework.jmx.support.MBeanServerLocator;
import org.nexopenframework.workflow.ProcessException;
import org.nexopenframework.workflow.ProcessService;
import org.nexopenframework.workflow.management.ProcessDefinitionMBean;
import org.nexopenframework.workflow.providers.Provider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.util.Assert;

/**
 * <p>NexTReT Open Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class JbpmProvider implements Provider, BeanFactoryAware {

    /**the Hibernate SessionFactory*/
    private SessionFactory sf;

    /**the jBPM configuration*/
    private JbpmConfiguration configuration;

    private ObjectName processDefinition;

    /** {@link org.apache.commons.logging} logging facility  */
    private Log logger = LogFactory.getLog(this.getClass());

    public void setConfiguration(JbpmConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setSessionFactory(SessionFactory sf) {
        this.sf = sf;
    }

    public void setProcessDefinition(ObjectName processDefinition) {
        this.processDefinition = processDefinition;
    }

    /**
	 * 
	 * @see org.nexopenframework.workflow.providers.Provider#createProcessManager(java.util.Map)
	 */
    public ProcessService createProcessService(Map properties) {
        JbpmService manager = new JbpmService();
        if (sf != null) {
            JbpmTemplate template = new JbpmTemplate();
            template.setJbpmConfiguration(configuration);
            template.setSessionFactory(sf);
            manager.setTemplate(template);
        } else {
            manager.setConfiguration(configuration);
        }
        if (this.processDefinition != null) {
            try {
                MBeanServer server = MBeanServerLocator.locateMBeanServer();
                JbpmProcessDefinition definition = new JbpmProcessDefinition();
                definition.setConfiguration(configuration);
                definition.setObjectName(this.processDefinition);
                StandardMBean mbean = new StandardMBean(definition, ProcessDefinitionMBean.class);
                server.registerMBean(mbean, this.processDefinition);
            } catch (NotCompliantMBeanException e) {
                throw new ProcessException(e);
            } catch (InstanceAlreadyExistsException e) {
                throw new ProcessException(e);
            } catch (MBeanRegistrationException e) {
                throw new ProcessException(e);
            }
        }
        return manager;
    }

    /**
	 * 
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
    public void setBeanFactory(BeanFactory beanFactory) {
        Assert.notNull(configuration, "JbpmConfiguration Must not be null");
        if (sf == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Not SessionFactory available. Auto-discovery process in Spring context");
            }
            try {
                Assert.isTrue(beanFactory instanceof ListableBeanFactory);
                ListableBeanFactory lbf = (ListableBeanFactory) beanFactory;
                this.sf = (SessionFactory) BeanFactoryUtils.beanOfTypeIncludingAncestors(lbf, SessionFactory.class);
            } catch (BeansException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Not SessionFactory available in Spring context", e);
                }
            }
        }
    }
}
