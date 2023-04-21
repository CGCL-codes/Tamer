package org.springframework.richclient.command.config;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import junit.framework.TestCase;
import org.springframework.richclient.image.EmptyIcon;

/**
 * @author Peter De Bruycker
 */
public class DefaultButtonConfigurerTests extends TestCase {

    private CommandFaceDescriptor descriptor;

    private TestableIconInfo iconInfo;

    private TestableLabelInfo labelInfo;

    protected void setUp() throws Exception {
        descriptor = new CommandFaceDescriptor();
        iconInfo = new TestableIconInfo();
        labelInfo = new TestableLabelInfo();
        descriptor.setIconInfo(iconInfo);
        descriptor.setLabelInfo(labelInfo);
        descriptor.setCaption("Tool tip");
    }

    public void testConfigureWithNullDescriptor() {
        DefaultCommandButtonConfigurer configurer = new DefaultCommandButtonConfigurer();
        try {
            configurer.configure(new JButton(), null, null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            pass();
        }
    }

    public void testConfigureWithNullButton() {
        DefaultCommandButtonConfigurer configurer = new DefaultCommandButtonConfigurer();
        try {
            configurer.configure(null, null, descriptor);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            pass();
        }
    }

    public void testConfigure() {
        JButton button = new JButton();
        DefaultCommandButtonConfigurer configurer = new DefaultCommandButtonConfigurer();
        configurer.configure(button, null, descriptor);
        assertEquals(labelInfo.configuredButton, button);
        assertEquals(iconInfo.configuredButton, button);
        assertEquals("Tool tip", button.getToolTipText());
    }

    private static void pass() {
    }

    private static class TestableLabelInfo extends CommandButtonLabelInfo {

        private AbstractButton configuredButton;

        public TestableLabelInfo() {
            super("test");
        }

        public AbstractButton configure(AbstractButton button) {
            configuredButton = button;
            return button;
        }
    }

    private static class TestableIconInfo extends CommandButtonIconInfo {

        private AbstractButton configuredButton;

        public TestableIconInfo() {
            super(EmptyIcon.SMALL);
        }

        public AbstractButton configure(AbstractButton button) {
            configuredButton = button;
            return button;
        }
    }
}
