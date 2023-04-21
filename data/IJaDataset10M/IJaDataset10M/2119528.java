package wicket.contrib.tinymce;

import junit.framework.TestCase;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.JavaScriptReference;
import org.apache.wicket.util.tester.TestPanelSource;
import org.apache.wicket.util.tester.WicketTester;
import wicket.contrib.tinymce.settings.NoneditablePlugin;
import wicket.contrib.tinymce.settings.Plugin;
import wicket.contrib.tinymce.settings.TinyMCESettings;

/**
 * Tests of the TinyMCE panel.
 * 
 * @author Frank Bille Jensen (fbille@avaleo.net)
 */
public class TinyMCEPanelTest extends TestCase {

    WicketTester application;

    /**
	 * For each test case we provide a new WicketTester.
	 * @see junit.framework.TestCase#setUp()
	 */
    public void setUp() {
        application = new WicketTester();
    }

    /**
	 * Test if basic rendering of this panel works.
	 */
    public void testRender() {
        application.startPanel(new TestPanelSource() {

            private static final long serialVersionUID = 1L;

            public Panel getTestPanel(String panelId) {
                TinyMCESettings settings = new TinyMCESettings();
                return new TinyMCEPanel("panel", settings);
            }
        });
        assertCommonComponents();
        application.assertContains("theme : \"simple\"");
    }

    /**
	 * Test that the correct output is rendered when using the advanced theme.
	 */
    public void testRenderAdvanced() {
        application.startPanel(new TestPanelSource() {

            private static final long serialVersionUID = 1L;

            public Panel getTestPanel(String panelId) {
                TinyMCESettings settings = new TinyMCESettings(TinyMCESettings.Theme.advanced);
                settings.register(new NoneditablePlugin());
                return new TinyMCEPanel("panel", settings);
            }
        });
        assertCommonComponents();
        application.assertContains("theme : \"advanced\"");
        application.assertContains("plugins : \"noneditable\"");
    }

    /**
	 * Ensure that the correct javascript is written, to load the plugins
	 * needed.
	 */
    public void testRenderWithExternalPlugins() {
        final Plugin mockPlugin = new Plugin("mockplugin", "the/path/to/the/plugin") {

            private static final long serialVersionUID = 1L;
        };
        application.startPanel(new TestPanelSource() {

            private static final long serialVersionUID = 1L;

            public Panel getTestPanel(String panelId) {
                TinyMCESettings settings = new TinyMCESettings(TinyMCESettings.Theme.advanced);
                settings.register(mockPlugin);
                return new TinyMCEPanel("panel", settings);
            }
        });
        assertCommonComponents();
        application.assertContains("plugins : \"mockplugin\"");
        application.assertContains("tinyMCE\\.loadPlugin\\('" + mockPlugin.getName() + "','" + mockPlugin.getPluginPath() + "'\\);");
    }

    /**
	 * Ensure that the plugins additional javascript is actually rendered.
	 * 
	 */
    public void testAdditionalPluginJavaScript() {
        final Plugin mockPlugin = new Plugin("mockplugin") {

            private static final long serialVersionUID = 1L;

            protected void definePluginExtensions(StringBuffer buffer) {
                buffer.append("alert('Hello Mock World');");
            }
        };
        application.startPanel(new TestPanelSource() {

            private static final long serialVersionUID = 1L;

            public Panel getTestPanel(String panelId) {
                TinyMCESettings settings = new TinyMCESettings();
                settings.register(mockPlugin);
                return new TinyMCEPanel("panel", settings);
            }
        });
        assertCommonComponents();
        application.assertContains("tinyMCE.init\\(\\{[^\\}]+\\}\\);\nalert\\('Hello Mock World'\\);");
    }

    private void assertCommonComponents() {
        application.assertComponent("panel", TinyMCEPanel.class);
        application.assertComponent("panel:tinymce", JavaScriptReference.class);
        application.assertComponent("panel:initScript", WebComponent.class);
        application.assertContains("tinyMCE\\.init\\(\\{");
        application.assertContains("\\}\\);");
    }
}
