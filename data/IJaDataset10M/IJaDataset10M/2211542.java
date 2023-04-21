package org.vizzini.ui.ai.logic;

import static org.hamcrest.CoreMatchers.is;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowInterceptor;
import org.vizzini.ai.logic.ISentence;
import org.vizzini.ui.ApplicationSupport;
import java.util.List;

/**
 * Provides tests for the <code>VersusUI</code> class.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.4
 */
public class SentencesEditorTest extends UISpecTestCase {

    /** UI test helper. */
    SentencesEditorTestHelper _uiTestHelper;

    /**
     * Test the criterion editor.
     */
    public void testVersus0() {
        WindowInterceptor mainInterceptor = WindowInterceptor.init(new Trigger() {

            public void run() throws Exception {
                Window window = getMainWindow();
                assertNotNull(window);
                assertThat(window.titleEquals("Sentences Editor"));
            }
        });
    }

    /**
     * @see  org.uispec4j.UISpecTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        _uiTestHelper = new SentencesEditorTestHelper();
        setAdapter(new MainClassAdapter(SentencesEditor.class, new String[] { "Avg Jan Low", "Avg July High", "Candidate" }));
        ApplicationSupport.setStringBundleName("org.vizzini.ui.testResources");
        ApplicationSupport.setConfigBundleName("org.vizzini.ui.testConfig");
    }

    /**
     * @see  org.uispec4j.UISpecTestCase#tearDown()
     */
    @Override
    protected void tearDown() {
        _uiTestHelper = null;
    }

    /**
     * @param  window  Window.
     */
    void verifyInitialState(Window window) {
        verifySentenceCount(window, 0);
        SentencesEditor sentencesEditor = _uiTestHelper.getSentencesEditor(window);
        org.junit.Assert.assertThat(sentencesEditor.getComponents().length, is(4));
    }

    /**
     * @param  window  Window.
     * @param  count   Count.
     */
    void verifySentenceCount(Window window, int count) {
        SentencesEditor sentencesEditor = _uiTestHelper.getSentencesEditor(window);
        List<ISentence> sentences = sentencesEditor.getSentences();
        assertNotNull(sentences);
        org.junit.Assert.assertThat(sentences.size(), is(count));
    }
}
