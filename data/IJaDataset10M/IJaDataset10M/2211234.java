package net.sourceforge.xconf.toolbox.spring.mvc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static java.util.Arrays.asList;
import java.util.Properties;
import java.util.Set;

public class RouteMappingsTest {

    @Test
    public void shouldCreateMappingsFromPropertiesWithSingleHandler() throws Exception {
        Properties props = new Properties();
        props.setProperty("/foo", "bar");
        RouteMappings mappings = new RouteMappings();
        mappings.setMappings(props);
        Set<URITemplate> templates = mappings.templates();
        assertThat(templates.size(), is(1));
        URITemplate template = templates.iterator().next();
        assertThat(template.getPath(), is("/foo"));
        assertThat(mappings.handlerNames(template), is(asList("bar")));
    }

    @Test
    public void shouldCreateMappingsFromPropertiesWithMultipleHandlers() throws Exception {
        Properties props = new Properties();
        props.setProperty("/test", "one,two");
        RouteMappings mappings = new RouteMappings();
        mappings.setMappings(props);
        Set<URITemplate> templates = mappings.templates();
        assertThat(templates.size(), is(1));
        URITemplate template = templates.iterator().next();
        assertThat(template.getPath(), is("/test"));
        assertThat(mappings.handlerNames(template), is(asList("one", "two")));
    }
}
