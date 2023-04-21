package eu.annocultor.tests;

import junit.framework.TestCase;
import eu.annocultor.common.Language.Lang;
import eu.annocultor.tagger.terms.CodeURI;
import eu.annocultor.tagger.terms.Term;

public class TermTest extends TestCase {

    String label = "label";

    String codeOk = "http://code";

    String codeFail = "zzzhttp://code";

    String voc = "vocab";

    public void testTransparency() throws Exception {
        Term t = new Term(label, Lang.en, new CodeURI(codeOk), voc);
        assertEquals(label, t.getLabel());
        assertEquals(codeOk, t.getCode());
        assertEquals(voc, t.getVocabularyName());
        assertEquals(Lang.en, t.getLang());
    }
}
