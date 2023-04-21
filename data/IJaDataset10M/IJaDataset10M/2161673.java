package org.apache.shindig.gadgets.templates.tags;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import org.apache.shindig.common.xml.XmlUtil;
import org.apache.shindig.gadgets.templates.TagRegistry;
import org.apache.shindig.gadgets.templates.TemplateProcessor;
import org.apache.shindig.gadgets.templates.tags.AbstractTagHandler;
import org.apache.shindig.gadgets.templates.tags.DefaultTagRegistry;
import org.apache.shindig.gadgets.templates.tags.TagHandler;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.google.common.collect.ImmutableSet;

public class DefaultTagRegistryTest {

    public static final String TEST_NAMESPACE = "#test";

    public static final String TEST_NAME = "Tag";

    private TagHandler tag;

    private DefaultTagRegistry registry;

    @Before
    public void setUp() {
        tag = new AbstractTagHandler(TEST_NAMESPACE, TEST_NAME) {

            public void process(Node result, Element tag, TemplateProcessor processor) {
            }
        };
        registry = new DefaultTagRegistry(ImmutableSet.of(tag));
    }

    @Test
    public void getHandlerForWithElement() {
        Element el = XmlUtil.parseSilent("<Tag xmlns='#test'/>");
        assertSame(tag, registry.getHandlerFor(el));
    }

    @Test
    public void getHandlerForUsesNamespace() {
        Element el = XmlUtil.parseSilent("<Tag xmlns='#nottest'/>");
        assertNull(registry.getHandlerFor(el));
    }

    @Test
    public void getHandlerIsCaseSensitive() {
        Element el = XmlUtil.parseSilent("<tag xmlns='#test'/>");
        assertNull(registry.getHandlerFor(el));
    }

    @Test
    public void getHandlerForWithNSName() {
        TagRegistry.NSName nsName = new TagRegistry.NSName(TEST_NAMESPACE, TEST_NAME);
        assertSame(tag, registry.getHandlerFor(nsName));
    }
}
