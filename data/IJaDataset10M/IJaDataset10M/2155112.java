package net.sf.jsog.spring.ns;

import net.sf.jsog.client.UrlBuilder;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import static org.junit.Assert.*;

/**
 *
 * @author jrodriguez
 */
public class UrlBeanDefinitionParserTest {

    @Test
    public void test() {
        ApplicationContext ac = new GenericXmlApplicationContext(new ClassPathResource("net/sf/jsog/spring/ns/url.xml"));
        UrlBuilder url = ac.getBean(UrlBuilder.class);
        assertNotNull(url);
        assertEquals("http://www.example.com/?foo=bar&foo=baz&qux=quux", url.toString());
    }
}
