package com.nhncorp.usf.core.result.template.directive;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Web Platform Development Team
 */
public class ResourceDirectiveTest {

    static final String TEST_FTL = "resource_directive_test.ftl";

    static final String TEST_UIO1 = "resource_directive_test.[uio1]";

    static final String TEST_UIO2 = "resource_directive_test.[uio2]";

    static final String TEST_UIO3 = "resource_directive_test.[uio3]";

    static Map<Object, Object> dataMap = new HashMap<Object, Object>();

    static Configuration configuration;

    @BeforeClass
    public static void init() throws IOException {
        configuration = new Configuration();
        TemplateLoader loader = new TemplateLoader() {

            public void closeTemplateSource(Object templateSource) throws IOException {
            }

            public Object findTemplateSource(String name) throws IOException {
                if (name.equals(TEST_FTL)) {
                    return "test start<@resource id=\"uio1\"/><@resource id=[\'uio2\',\'uio3\']/>test end";
                }
                if (name.equals(TEST_UIO1)) {
                    return "[UIO1]";
                }
                if (name.equals(TEST_UIO2)) {
                    return "[UIO2]";
                }
                if (name.equals(TEST_UIO3)) {
                    return "[UIO3]";
                }
                return null;
            }

            public long getLastModified(Object templateSource) {
                return System.currentTimeMillis();
            }

            public Reader getReader(Object templateSource, String encoding) throws IOException {
                return new StringReader((String) templateSource);
            }
        };
        configuration.setTemplateLoader(loader);
        dataMap.put("resource", new ResourceDirective());
    }

    @Test
    public void defaultTest() throws TemplateException, IOException {
        StringWriter writer = new StringWriter();
        configuration.getTemplate("resource_directive_test.ftl").process(dataMap, writer);
        assertEquals("test start[UIO1][UIO2][UIO3]test end", writer.toString());
    }
}
