package fi.mmmtike.tiira.invoker.http.client;

import java.util.Properties;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;

/**
 * ApplicationContext for registering service bean definitions (on client side).
 * 
 * @author Tomi Tuomainen
 */
@SuppressWarnings("unchecked")
public class TiiraHttpInvokerApplicationContext extends GenericApplicationContext {

    private PropertiesBeanDefinitionReader propertiesBeanDefinitionReader = new PropertiesBeanDefinitionReader(this);

    public TiiraHttpInvokerApplicationContext() {
        super();
    }

    /**
	 * Registers remote service definition by Properties.
	 * 
	 * An example of created properties for interface
	 * <p>
	 *  <code>fi.mmmtike.tiira.accounttest.common.AccountService"</code>
	 *  <p>
	 * <code> 
     * "accountService.(class)" "fi.mmmtike.tiira.TiiraHttpInvokerProxyFactoryBean"
     * "accountService.serviceInterface", "fi.mmmtike.tiira.accounttest.common.AccountService"
     * "accountService.serviceUrl", "http://localhost:8080/tiira/AccountService"
     * </code>
     */
    public void addBeanDefinition(Class interfaceClass, String beanName, String serviceUrl, Class proxyClass) {
        Properties props = new Properties();
        props.put(beanName + ".(class)", proxyClass.getName());
        props.put(beanName + ".serviceInterface", interfaceClass.getName());
        props.put(beanName + ".serviceUrl", serviceUrl);
        propertiesBeanDefinitionReader.registerBeanDefinitions(props);
    }
}
