package siouxsie.mvc.xwork.objectfactory;

import java.util.HashMap;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import siouxsie.mvc.AbstractXWorkTest;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionProxyFactory;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.ConfigurationProvider;
import com.opensymphony.xwork2.config.providers.XWorkConfigurationProvider;
import com.opensymphony.xwork2.config.providers.XmlConfigurationProvider;
import com.opensymphony.xwork2.spring.SpringObjectFactory;

public class ObjectFactoryTest extends AbstractXWorkTest {

    /**
	 * Check the retrieval of a custom object factory.
	 * @throws Exception
	 */
    public void testMultiInject() throws Exception {
        ConfigurationManager confManager = new ConfigurationManager();
        confManager.addContainerProvider(new MyConfigurationProvider(MyObjectFactory.class));
        confManager.addContainerProvider(new XmlConfigurationProvider("siouxsie/mvc/xwork/objectfactory/object-factory.xml", true));
        Configuration conf1 = confManager.getConfiguration();
        assertEquals(MyObjectFactory.class, conf1.getContainer().getInstance(ObjectFactory.class).getClass());
        ActionProxyFactory fact1 = conf1.getContainer().getInstance(ActionProxyFactory.class);
        ActionProxy prox1 = fact1.createActionProxy("", "helloWorld", new HashMap());
        prox1.execute();
    }

    public void testSpringFactory() throws Exception {
        ConfigurationManager confManager = new ConfigurationManager();
        confManager.addContainerProvider(new MyConfigurationProvider(SpringObjectFactory.class));
        confManager.addContainerProvider(new XmlConfigurationProvider("siouxsie/mvc/xwork/objectfactory/spring-factory.xml", true));
        Configuration conf1 = confManager.getConfiguration();
        assertEquals(SpringObjectFactory.class, conf1.getContainer().getInstance(ObjectFactory.class).getClass());
        ActionProxyFactory fact1 = conf1.getContainer().getInstance(ActionProxyFactory.class);
        ActionProxy prox1 = fact1.createActionProxy("", "helloWorld", new HashMap());
        prox1.execute();
    }

    public void testStaticBasedSpringObjectFactory() throws Exception {
        ConfigurationManager confManager = new ConfigurationManager();
        siouxsie.mvc.spring.SpringObjectFactory.setApplicationContext(new ClassPathXmlApplicationContext("siouxsie/mvc/xwork/objectfactory/app-context-1.xml"));
        confManager.addContainerProvider(new MyConfigurationProvider(siouxsie.mvc.spring.SpringObjectFactory.class));
        confManager.addContainerProvider(new XmlConfigurationProvider("siouxsie/mvc/xwork/objectfactory/spring-factory.xml", true));
        Configuration conf1 = confManager.getConfiguration();
        assertEquals(siouxsie.mvc.spring.SpringObjectFactory.class, conf1.getContainer().getInstance(ObjectFactory.class).getClass());
        ActionProxyFactory fact1 = conf1.getContainer().getInstance(ActionProxyFactory.class);
        ActionProxy prox1 = fact1.createActionProxy("", "helloWorld", new HashMap());
        prox1.execute();
    }

    public void testTwoPhaseLoading() throws Exception {
    }
}
