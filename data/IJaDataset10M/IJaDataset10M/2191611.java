package soma.rest.training.jerseySpringJDO.resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/rootContext.xml", "classpath:/rootContext-dev.xml" })
public class MessageResourceTest extends JerseyTest {

    public MessageResourceTest() throws Exception {
        super(new WebAppDescriptor.Builder("soma.rest.training.jerseySpringJDO.resource").contextPath("/").contextParam("contextConfigLocation", "classpath:rootContext-dev.xml").contextParam("log4jConfigLocation", "classpath:log4j.properties").contextParam("propertiesConfigLocation", "classpath:propertiesConfigContext.xml").contextListenerClass(ContextLoaderListener.class).requestListenerClass(RequestContextListener.class).servletClass(SpringServlet.class).build());
    }

    @Test
    public void testTable() throws Exception {
        WebResource webResource = resource();
        String responseMsg = webResource.path("/message/get").get(String.class);
        System.out.println(responseMsg);
    }
}
