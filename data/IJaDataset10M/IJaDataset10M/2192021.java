package whitecat.core;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import whitecat.core.agents.IMethodForwarderGenerator;
import whitecat.core.role.IRoleRepository;
import whitecat.core.role.descriptors.IRoleDescriptorBuilder;
import whitecat.core.role.task.ITaskExecutionResult;
import whitecat.core.role.task.scheduling.ITaskScheduler;

/**
 * This is the main class of the whole system. This class has been created to
 * act as a front-end for the whole system.
 * 
 * @author Luca Ferrari - cat4hire (at) sourceforge.net
 * 
 */
public class WhiteCat {

    /**
	 * The spring xml bean factory, used to instantiate the beans.
	 */
    private static XmlBeanFactory xmlBeanFactory = null;

    static {
        final String springConfigurationPath = "whitecat.spring-beans.xml";
        final ClassPathResource classPathResource = new ClassPathResource(springConfigurationPath);
        xmlBeanFactory = new XmlBeanFactory(classPathResource);
    }

    /**
	 * Returns the default method forwarder generator.
	 * 
	 * @return a new method generator instance
	 */
    public static final IMethodForwarderGenerator getMethodForwarderGenerator() {
        return (IMethodForwarderGenerator) xmlBeanFactory.getBean(IMethodForwarderGenerator.class.getSimpleName());
    }

    /**
	 * Provides a new role operation. Please note that the role booster is
	 * always initialized with a new role operation, so you don't have to create
	 * one in order to manipulate a proxy.
	 * 
	 * @return a new role operation
	 */
    public static final IRoleOperation getNewRoleOperation() {
        return (IRoleOperation) xmlBeanFactory.getBean(IRoleOperation.class.getSimpleName());
    }

    /**
	 * Provides the default proxy handler for this configuration.
	 * 
	 * @return a new proxy handler instance
	 */
    public static final IProxyHandler getProxyHandler() {
        return (IProxyHandler) xmlBeanFactory.getBean(IProxyHandler.class.getSimpleName());
    }

    /**
	 * Provides the unique proxy storage for the running system.
	 * 
	 * @return the proxy storage to use
	 */
    public static final IProxyStorage getProxyStorage() {
        return (IProxyStorage) xmlBeanFactory.getBean(IProxyStorage.class.getSimpleName());
    }

    /**
	 * Provides a default role booster to use.
	 * 
	 * @return the role booster to use for role manipulations
	 */
    public static final IRoleBooster getRoleBooster() {
        return (IRoleBooster) xmlBeanFactory.getBean(IRoleBooster.class.getSimpleName());
    }

    /**
	 * Provides the default role descriptor builder for the system.
	 * 
	 * @return the role descriptor builder
	 */
    public static final IRoleDescriptorBuilder getRoleDescriptorBuilder() {
        return (IRoleDescriptorBuilder) xmlBeanFactory.getBean(IRoleDescriptorBuilder.class.getSimpleName());
    }

    /**
	 * Provides the unique role repository available in the system.
	 * 
	 * @return the role repository implementation
	 */
    public static final IRoleRepository getRoleRepository() {
        return (IRoleRepository) xmlBeanFactory.getBean(IRoleRepository.class.getSimpleName());
    }

    /**
	 * Provides the task execution result instance to use for a specific task.
	 * 
	 * @return the implementation of a task execution result
	 */
    public static final ITaskExecutionResult getTaskExecutionResult() {
        return (ITaskExecutionResult) xmlBeanFactory.getBean(ITaskExecutionResult.class.getSimpleName());
    }

    /**
	 * Provides the task scheduler for this installation.
	 * 
	 * @return the task scheduler
	 */
    public static final ITaskScheduler getTaskScheduler() {
        return (ITaskScheduler) xmlBeanFactory.getBean(ITaskScheduler.class.getSimpleName());
    }
}
