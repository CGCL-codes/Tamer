package org.springframework.richclient.application.support;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import junit.framework.TestCase;

/**
 * testcase for {@link DefaultViewDescriptor}
 * 
 * @author Peter De Bruycker
 */
public class DefaultViewDescriptorTests extends TestCase {

    public void testConstructor() {
        DefaultViewDescriptor descriptor = new DefaultViewDescriptor("theView", TestView.class);
        assertEquals("theView", descriptor.getId());
        assertEquals(TestView.class, descriptor.getViewClass());
    }

    public void testViewCreation() {
        DefaultViewDescriptor descriptor = new DefaultViewDescriptor("theView", TestView.class);
        TestView view = (TestView) descriptor.createPageComponent();
        assertNotNull(view);
    }

    public void testViewCreationWithProperties() {
        Map<String, Object> viewProperties = new HashMap<String, Object>();
        viewProperties.put("stringProperty", "test value");
        DefaultViewDescriptor descriptor = new DefaultViewDescriptor("theView", TestView.class, viewProperties);
        TestView view = (TestView) descriptor.createPageComponent();
        assertNotNull(view);
        assertEquals("test value", view.getStringProperty());
    }

    public void testSetViewClass() throws Exception {
        DefaultViewDescriptor descriptor = new DefaultViewDescriptor();
        descriptor.setId("viewId");
        Class notAViewClass = String.class;
        try {
            descriptor.setViewClass(notAViewClass);
            fail("Must throw exception");
        } catch (IllegalArgumentException e) {
        }
    }

    public static class TestView extends AbstractView {

        private String stringProperty;

        @Override
        protected JComponent createControl() {
            return new JLabel("test");
        }

        public void setStringProperty(String stringProperty) {
            this.stringProperty = stringProperty;
        }

        public String getStringProperty() {
            return stringProperty;
        }
    }
}
