package com.volantis.mcs.context;

import com.volantis.mcs.runtime.RequestHeaders;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.styling.StylingFactoryMock;
import com.volantis.styling.compiler.InlineStyleSheetCompilerFactory;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.styling.engine.StylingEngineMock;
import com.volantis.styling.expressions.EvaluationContextMock;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.styling.sheet.CompiledStyleSheetMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link MarinerPageContext}.
 */
public class MarinerPageContextUnitTestCase extends TestCaseAbstract {

    protected StylingFactoryMock stylingFactoryMock;

    protected StylingEngineMock stylingEngineMock;

    protected void setUp() throws Exception {
        super.setUp();
        stylingFactoryMock = new StylingFactoryMock("stylingFactoryMock", expectations);
        stylingEngineMock = new StylingEngineMock("stylingEngineMock", expectations);
    }

    /**
     * Verifies that if a StylingEngine is requested from a MarinerPageContext
     * with no including page context, then a non null StylingEngine is returned.
     */
    public void testGetStylingEngine() throws Exception {
        final EvaluationContextMock evaluationContextMock = new EvaluationContextMock("evaluationContextMock", expectations);
        stylingFactoryMock.fuzzy.createStylingEngine(mockFactory.expectsAny()).returns(stylingEngineMock);
        stylingEngineMock.fuzzy.pushStyleSheet(mockFactory.expectsInstanceOf(CompiledStyleSheet.class)).returns().any();
        stylingEngineMock.expects.getEvaluationContext().returns(evaluationContextMock).any();
        evaluationContextMock.fuzzy.setProperty(FormatReferenceFinder.class, mockFactory.expectsInstanceOf(FormatReferenceFinder.class)).returns().any();
        MarinerPageContext context = new MarinerPageContext(stylingFactoryMock) {

            protected InlineStyleSheetCompilerFactory getInlineStyleSheetCompilerFactory() {
                return null;
            }
        };
        final MarinerRequestContextMock requestContextMock = new MarinerRequestContextMock("requestContext", expectations);
        final EnvironmentContextMock environmentContextMock = new EnvironmentContextMock("environmentContext", expectations);
        final RuntimeProjectMock projectMock = new RuntimeProjectMock("projectMock", expectations);
        requestContextMock.expects.getEnvironmentContext().returns(environmentContextMock).any();
        context.initialisePage(new Volantis() {

            public CompiledStyleSheet getDefaultStyleSheet() {
                return new CompiledStyleSheetMock("CompiledStyleSheetMock", expectations);
            }

            public RuntimeProject getDefaultProject() {
                return projectMock;
            }
        }, requestContextMock, new MarinerRequestContextMock("EnclosingRequestContext", expectations), new MarinerURL(), new RequestHeaders() {

            public String getHeader(String name) {
                return null;
            }
        });
        StylingEngine stylingEngine = context.getStylingEngine();
        assertSame("Styling engine", stylingEngineMock, stylingEngine);
    }
}
