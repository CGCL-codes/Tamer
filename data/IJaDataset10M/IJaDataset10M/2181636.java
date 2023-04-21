package org.springframework.web.servlet.view.json.writer.jsonlib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.view.json.mock.writer.jsonlib.MockSimpleJsonlibJsonWriterConfiguratorTemplate;

public class JsonlibJsonWriterConfiguratorTemplateTest {

    private MockSimpleJsonlibJsonWriterConfiguratorTemplate template;

    @Before
    public void setUp() throws Exception {
        template = new MockSimpleJsonlibJsonWriterConfiguratorTemplate();
    }

    @After
    public void tearDown() throws Exception {
        template = null;
    }

    @Test
    public void testGetConfigurator() {
        assertNotNull(template.getConfigurator());
    }

    @Test
    public void testGetRegistryName() {
        assertEquals(JsonlibJsonWriterConfiguratorTemplate.class.getName(), template.getRegistryName());
    }

    @Test
    public void testGetJsonConfig() {
        assertNotNull(template.getConfigurator());
    }
}
