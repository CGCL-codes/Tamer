package com.google.gwt.language.client.transliteration.control;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.language.client.LanguageUtils;
import com.google.gwt.language.client.transliteration.LanguageCode;
import com.google.gwt.language.client.transliteration.TransliterationUtils;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Tests for TransliterationControl class.
 */
public class TransliterationControlTest extends GWTTestCase {

    private static TransliterationControl control;

    private static final int MAX_TEST_FINISH_DELAY = 10000;

    private static final int TEST_INNER_VALUE = 5;

    private static final int TEST_OUTER_VALUE = 10;

    private HTML div;

    private TextArea textArea1;

    private TextArea textArea2;

    @Override
    public String getModuleName() {
        return "com.google.gwt.language.Language";
    }

    @Override
    public void gwtSetUp() {
        div = new HTML();
        RootPanel.get().add(div);
        textArea1 = new TextArea();
        RootPanel.get().add(textArea1);
        textArea2 = new TextArea();
        RootPanel.get().add(textArea2);
    }

    @Override
    public void gwtTearDown() {
        RootPanel.get().clear();
    }

    /**
   * Verifies that transliteration can be enabled or disabled using
   * Transliteration control.
   */
    public void testEnableTransliteration() {
        LanguageUtils.loadTransliteration(new Runnable() {

            public void run() {
                initialize();
                assertTrue("Expected Transliteration to be enabled", control.isTransliterationEnabled());
                control.disableTransliteration();
                assertFalse("Expected Transliteration to be disabled", control.isTransliterationEnabled());
                control.enableTransliteration();
                assertTrue("Expected Transliteration to be re-enabled", control.isTransliterationEnabled());
                control.toggleTransliteration();
                assertFalse("Expected transliteration to be disabled", control.isTransliterationEnabled());
                control.toggleTransliteration();
                assertTrue("Expected transliteration to be enabled", control.isTransliterationEnabled());
                finishTest();
            }
        });
        delayTestFinish(MAX_TEST_FINISH_DELAY);
    }

    /**
   * Tests that language change event listeners are working correctly.
   */
    public void testLanguageChangeEventListeners() {
        LanguageUtils.loadTransliteration(new Runnable() {

            int testOuterValue = TEST_OUTER_VALUE;

            public void run() {
                initialize();
                control.setLanguagePair(LanguageCode.ENGLISH, LanguageCode.HINDI);
                control.addEventListener(EventType.LANGUAGE_CHANGED, new TransliterationEventListener() {

                    int testInnerValue = TEST_INNER_VALUE;

                    @Override
                    protected void onEvent(TransliterationEventDetail result) {
                        assert (testInnerValue == TEST_INNER_VALUE);
                        assert (testOuterValue == TEST_OUTER_VALUE);
                        control.removeEventListener(EventType.LANGUAGE_CHANGED, this);
                        LanguageCode srcLang = result.getSourceLanguage();
                        LanguageCode destLang = result.getDestinationLanguage();
                        LanguageCode[] langPair = control.getLanguageCodePair();
                        finishTest();
                    }
                });
                control.setLanguagePair(LanguageCode.ENGLISH, LanguageCode.KANNADA);
            }
        });
        delayTestFinish(MAX_TEST_FINISH_DELAY);
    }

    /**
   * Tests that language pair may be correctly set and unset.
   */
    public void testLanguagePairSetting() {
        LanguageUtils.loadTransliteration(new Runnable() {

            public void run() {
                initialize();
                control.setLanguagePair(LanguageCode.ENGLISH, LanguageCode.KANNADA);
                LanguageCode[] langPair = control.getLanguageCodePair();
                control.setLanguagePair(LanguageCode.ENGLISH, LanguageCode.HINDI);
                LanguageCode[] langPair2 = control.getLanguageCodePair();
                finishTest();
            }
        });
        delayTestFinish(MAX_TEST_FINISH_DELAY);
    }

    /**
   * Just to verify that makeTransliteratable does not throw any exceptions.
   */
    public void testMakeTransliteratable() {
        LanguageUtils.loadTransliteration(new Runnable() {

            public void run() {
                initialize();
                TextArea[] textAreas = { textArea1, textArea2 };
                control.makeTransliteratable(textAreas);
                finishTest();
            }
        });
        delayTestFinish(MAX_TEST_FINISH_DELAY);
    }

    public void testMakeTransliteratableWithOptions() {
        LanguageUtils.loadTransliteration(new Runnable() {

            public void run() {
                initialize();
                TextElementOptions options = TextElementOptions.newInstance(true, false);
                TextArea[] textAreas = { textArea1, textArea2 };
                control.makeTransliteratable(textAreas, options);
                finishTest();
            }
        });
        delayTestFinish(MAX_TEST_FINISH_DELAY);
    }

    /**
   * Tests that State change event listeners are working correctly.
   */
    public void testStateChangeEventListeners() {
        LanguageUtils.loadTransliteration(new Runnable() {

            public void run() {
                initialize();
                control.addEventListener(EventType.STATE_CHANGED, new TransliterationEventListener() {

                    @Override
                    protected void onEvent(TransliterationEventDetail result) {
                        control.removeEventListener(EventType.STATE_CHANGED, this);
                        assertFalse("Expected transliteration to be disabled", control.isTransliterationEnabled());
                        assertFalse("Expected transliteration to be disabled", result.isTransliterationEnabled());
                        finishTest();
                    }
                });
                control.toggleTransliteration();
            }
        });
        delayTestFinish(MAX_TEST_FINISH_DELAY);
    }

    public void testTextElementOptions() {
        TextElementOptions options;
        options = TextElementOptions.newInstance(false);
        assertNotNull(options);
        options = TextElementOptions.newInstance(true);
        assertNotNull(options);
        options = TextElementOptions.newInstance(true, false);
        assertNotNull(options);
        options = TextElementOptions.newInstance(true, true);
        assertNotNull(options);
        options = TextElementOptions.newInstance(false, false);
        assertNotNull(options);
        options = TextElementOptions.newInstance(false, true);
        assertNotNull(options);
        assertNotNull(options.setTextAreaDirection(true));
        assertNotNull(options.setTextAreaDirection(false));
        assertNotNull(options.setTextAreaStyle(true));
        assertNotNull(options.setTextAreaStyle(false));
    }

    /**
   * Initializes Transliteration Control. Must be called after API loads.
   */
    private void initialize() {
        if (control == null) {
            TransliterationControlOptions options = TransliterationControlOptions.newInstance(LanguageCode.ENGLISH, TransliterationUtils.getDestinationLanguages(LanguageCode.ENGLISH), true, "ctrl+g");
            control = TransliterationControl.newInstance(options);
        }
        control.enableTransliteration();
        control.showControl(div);
    }
}
