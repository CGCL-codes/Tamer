package org.uimafit.factory;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.apache.uima.UIMAException;
import org.junit.Test;
import org.uimafit.ComponentTestBase;
import org.uimafit.type.Token;
import org.uimafit.util.JCasUtil;

/**
 * @author Steven Bethard, Philip Ogren
 * @author Richard Eckart de Castilho
 */
public class JCasFactoryTest extends ComponentTestBase {

    @Test
    public void testXMI() throws IOException {
        JCasFactory.loadJCas(jCas, "src/test/resources/data/docs/test.xmi");
        assertEquals("Me and all my friends are non-conformists.", jCas.getDocumentText());
    }

    @Test
    public void testXCAS() throws IOException {
        JCasFactory.loadJCas(jCas, "src/test/resources/data/docs/test.xcas", false);
        assertEquals("... the more knowledge advances the more it becomes possible to condense it into little books.", jCas.getDocumentText());
    }

    @Test
    public void testFromPath() throws UIMAException {
        jCas = JCasFactory.createJCasFromPath("src/test/resources/org/uimafit/type/AnalyzedText.xml", "src/test/resources/org/uimafit/type/Sentence.xml", "src/test/resources/org/uimafit/type/Token.xml");
        jCas.setDocumentText("For great 20 minute talks, check out TED.com.");
        AnnotationFactory.createAnnotation(jCas, 0, 3, Token.class);
        assertEquals("For", JCasUtil.selectByIndex(jCas, Token.class, 0).getCoveredText());
    }

    @Test
    public void testCreate() throws UIMAException {
        jCas = JCasFactory.createJCas();
        jCas.setDocumentText("For great 20 minute talks, check out TED.com.");
        AnnotationFactory.createAnnotation(jCas, 0, 3, Token.class);
        assertEquals("For", JCasUtil.selectByIndex(jCas, Token.class, 0).getCoveredText());
    }
}
