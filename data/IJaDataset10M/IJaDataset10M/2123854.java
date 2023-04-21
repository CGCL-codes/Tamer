package javax.speech.recognition;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.recognition.GrammarExceptionDetail}.
 * 
 * @author Dirk Schnelle
 */
public class GrammarExceptionDetailTest extends TestCase {

    /**
     * Test method for
     * {@link javax.speech.recognition.GrammarExceptionDetail#getCharNumber()}.
     */
    public void testGetCharNumber() {
        final GrammarExceptionDetail detail1 = new GrammarExceptionDetail(GrammarExceptionDetail.UNKNOWN_TYPE, "info1", "reference1", "ruleName1", 42, 43, "message1");
        assertEquals(43, detail1.getCharNumber());
        final GrammarExceptionDetail detail2 = new GrammarExceptionDetail(GrammarExceptionDetail.UNKNOWN_TYPE, null, null, null, 44, 45, null);
        assertEquals(45, detail2.getCharNumber());
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.GrammarExceptionDetail#getGrammarReference()}.
     */
    public void testGetGrammarReference() {
        final GrammarExceptionDetail detail1 = new GrammarExceptionDetail(GrammarExceptionDetail.UNKNOWN_TYPE, "info1", "reference1", "ruleName1", 42, 43, "message1");
        assertEquals("reference1", detail1.getGrammarReference());
        final GrammarExceptionDetail detail2 = new GrammarExceptionDetail(GrammarExceptionDetail.UNKNOWN_TYPE, "info1", null, null, 44, 45, null);
        assertNull(detail2.getGrammarReference());
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.GrammarExceptionDetail#getLineNumber()}.
     */
    public void testGetLineNumber() {
        final GrammarExceptionDetail detail1 = new GrammarExceptionDetail(GrammarExceptionDetail.UNKNOWN_TYPE, "info1", "reference1", "ruleName1", 42, 43, "message1");
        assertEquals(42, detail1.getLineNumber());
        final GrammarExceptionDetail detail2 = new GrammarExceptionDetail(GrammarExceptionDetail.UNKNOWN_TYPE, "info1", null, null, 44, 45, null);
        assertEquals(44, detail2.getLineNumber());
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.GrammarExceptionDetail#getMessage()}.
     */
    public void testGetMessage() {
        final GrammarExceptionDetail detail1 = new GrammarExceptionDetail(GrammarExceptionDetail.UNKNOWN_TYPE, "info1", "reference1", "ruleName1", 42, 43, "message1");
        assertEquals("message1", detail1.getMessage());
        final GrammarExceptionDetail detail2 = new GrammarExceptionDetail(GrammarExceptionDetail.UNKNOWN_TYPE, "info1", null, null, 44, 45, null);
        assertNull(detail2.getMessage());
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.GrammarExceptionDetail#getRuleName()}.
     */
    public void testGetRuleName() {
        final GrammarExceptionDetail detail1 = new GrammarExceptionDetail(GrammarExceptionDetail.UNKNOWN_TYPE, "info1", "reference1", "ruleName1", 42, 43, "message1");
        assertEquals("ruleName1", detail1.getRuleName());
        final GrammarExceptionDetail detail2 = new GrammarExceptionDetail(GrammarExceptionDetail.UNKNOWN_TYPE, "info1", null, null, 44, 45, null);
        assertNull(detail2.getRuleName());
    }
}
