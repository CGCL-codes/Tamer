package javax.speech.recognition;

import javax.speech.test.recognition.TestRecognizer;
import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.recognition.RecognizerEvent}.
 * 
 * @author Dirk Schnelle
 * 
 */
public class RecognizerEventTest extends TestCase {

    /** The recognizer. */
    private Recognizer recognizer;

    protected void setUp() throws Exception {
        super.setUp();
        recognizer = new TestRecognizer();
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.RecognizerEvent#getAudioPosition()}.
     */
    public void testGetAudioPosition() {
        final Throwable problem = new Exception();
        final GrammarException grammarException = new GrammarException();
        long audioPosition = 4834;
        final RecognizerEvent event = new RecognizerEvent(recognizer, 42, RecognizerEvent.CHANGES_COMMITTED, RecognizerEvent.ENGINE_ALLOCATING_RESOURCES, problem, grammarException, audioPosition);
        assertEquals(audioPosition, event.getAudioPosition());
    }

    /**
     * Test method for
     * {@link javax.speech.recognition.RecognizerEvent#getGrammarException()}.
     */
    public void testGetGrammarException() {
        final Throwable problem = new Exception();
        final GrammarException grammarException = new GrammarException();
        long audioPosition = 4834;
        final RecognizerEvent event = new RecognizerEvent(recognizer, 42, RecognizerEvent.CHANGES_COMMITTED, RecognizerEvent.ENGINE_ALLOCATING_RESOURCES, problem, grammarException, audioPosition);
        assertNull(event.getGrammarException());
        final Throwable problem2 = new Exception();
        final GrammarException grammarException2 = new GrammarException();
        long audioPosition2 = 4534;
        final RecognizerEvent event2 = new RecognizerEvent(recognizer, RecognizerEvent.CHANGES_REJECTED, RecognizerEvent.CHANGES_COMMITTED, RecognizerEvent.ENGINE_ALLOCATING_RESOURCES, problem2, grammarException2, audioPosition2);
        assertEquals(grammarException2, event2.getGrammarException());
    }

    /**
     * Test method for {@link javax.speech.AudioEvent#paramString()}.
     */
    public void testParamString() {
        final Throwable problem = new Exception();
        final GrammarException grammarException = new GrammarException();
        long audioPosition = 4834;
        final RecognizerEvent event = new RecognizerEvent(recognizer, 42, RecognizerEvent.CHANGES_COMMITTED, RecognizerEvent.ENGINE_ALLOCATING_RESOURCES, problem, grammarException, audioPosition);
        final String str = event.paramString();
        assertTrue("id not found in paramString", str.indexOf("42") >= 0);
    }

    /**
     * Test method for {@link javax.speech.AudioEvent#toString()}.
     */
    public void testToString() {
        final Throwable problem = new Exception();
        final GrammarException grammarException = new GrammarException();
        long audioPosition = 4834;
        final RecognizerEvent event = new RecognizerEvent(recognizer, 43, RecognizerEvent.CHANGES_COMMITTED, RecognizerEvent.ENGINE_ALLOCATING_RESOURCES, problem, grammarException, audioPosition);
        final String str = event.toString();
        assertTrue("id not found in toString", str.indexOf("43") >= 0);
        String paramString = event.paramString();
        assertTrue("toString not longer than paramString", str.length() > paramString.length());
    }
}
