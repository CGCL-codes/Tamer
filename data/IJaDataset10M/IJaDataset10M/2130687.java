package com.linguamathematica.translate4j;

import static com.linguamathematica.translate4j.Translator.translator;
import org.junit.Test;

public class TranslatorLifeCycleTest extends AbstractGoogleTranslateTest {

    @Test(expected = IllegalArgumentException.class)
    public void throwsIAEWhenProvidedWithAnEmptyKey() throws Exception {
        translator("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIAEWhenProvidedWithANonAlphanumericKey() throws Exception {
        translator("wr0ng k3y;");
    }

    @Test(expected = IllegalStateException.class)
    public void throwsISEWhenAttemptingToUseAfterDisposed() throws Exception {
        final Translator translator = translator(TEST_API_KEY);
        translator.dispose();
        translator.detect(ANY_TEXT);
    }

    @Test(expected = NullPointerException.class)
    public void throwsNPEWhenProvidedWithANullKey() throws Exception {
        translator(null);
    }
}
