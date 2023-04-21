package de.d3web.we.testsuite.kdom;

import de.d3web.we.core.KnowWERessourceLoader;
import de.d3web.we.kdom.Section;
import de.d3web.we.kdom.defaultMarkup.DefaultMarkup;
import de.d3web.we.kdom.defaultMarkup.DefaultMarkupType;
import de.d3web.we.kdom.rendering.KnowWEDomRenderer;
import de.d3web.we.testsuite.renderer.TestSuiteRunnerRenderer;

/**
 * DefaultMarkup-Type for the test suite result. This type needs the obligatory
 * annotation "TestSuite", that is the name of a KnowWEArticle containing a test
 * suite.
 * 
 * @author Sebastian Furth (denkbares GmbH)
 * @created 25/10/2010
 */
public class TestSuiteRunnerType extends DefaultMarkupType {

    private static final String ANNOTATION_TESTSUITE = "testsuite";

    private static final DefaultMarkup MARKUP;

    static {
        MARKUP = new DefaultMarkup("TestSuiteRunner");
        MARKUP.addAnnotation(ANNOTATION_TESTSUITE, true);
    }

    public TestSuiteRunnerType() {
        super(MARKUP);
        this.setCustomRenderer(this.getRenderer());
        KnowWERessourceLoader.getInstance().add("testsuite.js", KnowWERessourceLoader.RESOURCE_SCRIPT);
        KnowWERessourceLoader.getInstance().add("testsuite.css", KnowWERessourceLoader.RESOURCE_STYLESHEET);
    }

    public static String getText(Section<?> sec) {
        assert sec.getObjectType() instanceof TestSuiteRunnerType;
        return DefaultMarkupType.getContent(sec);
    }

    public static String getTestSuite(Section<?> section) {
        assert section.getObjectType() instanceof TestSuiteRunnerType;
        return DefaultMarkupType.getAnnotation(section, ANNOTATION_TESTSUITE);
    }

    @Override
    public KnowWEDomRenderer<TestSuiteRunnerType> getRenderer() {
        return new TestSuiteRunnerRenderer();
    }
}
