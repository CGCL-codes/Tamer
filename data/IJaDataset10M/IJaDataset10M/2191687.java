package plumber;

import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import plumber.coordinator.CoordinatorClient;
import plumber.model.Flow;

/**
 * subclass of Complete Test that uses Spring 
 * to configure Flows an Steps.
 * contexts can be found in test/resources
 * @author mgarber
 *
 */
public class SpringBasedCompleteTest extends CompleteTest {

    @Before
    public void setup() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "plumber/test-context.xml" });
        coordinator = (CoordinatorClient) context.getBean("Coordinator");
        flow = (Flow) context.getBean("TestFlow");
    }
}
