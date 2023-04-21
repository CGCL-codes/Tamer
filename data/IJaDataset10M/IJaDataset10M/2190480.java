package is.hi.translator.tests;

import is.hi.translator.SimpleTranslator;
import is.hi.translator.Translator;
import junit.framework.TestCase;

public class SimpleTranslatorTest extends TestCase {

    Translator translator;

    public void setUp() {
        translator = new SimpleTranslator();
    }

    public void testWordFound() {
        String translatedWord = translator.translate("hestur");
        assertEquals("horse", translatedWord);
    }

    public void testWordNotFound() {
        assertEquals(null, translator.translate("hest"));
    }
}
