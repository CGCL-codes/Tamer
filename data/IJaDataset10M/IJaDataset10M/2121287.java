package javax.speech;

import javax.speech.test.DummySpeechEvent;
import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.SpeechEvent}.
 * 
 * @author Dirk Schnelle
 * 
 */
public class SpeechEventTest extends TestCase {

    /**
     * Test method for {@link javax.speech.SpeechEvent#getId()}.
     */
    public void testGetId() {
        SpeechEvent event = new DummySpeechEvent(new Object(), 42);
        assertEquals(42, event.getId());
    }

    /**
     * Test method for {@link javax.speech.SpeechEvent#paramString()}.
     */
    public void testParamString() {
        SpeechEvent event = new DummySpeechEvent(new Object(), 43);
        String str = event.paramString();
        assertTrue(str.indexOf("43") >= 0);
    }

    /**
     * Test method for {@link javax.speech.SpeechEvent#toString()}.
     */
    public void testToString() {
        SpeechEvent event = new DummySpeechEvent(new Object(), 44);
        String str = event.toString();
        assertTrue(str.indexOf("44") >= 0);
    }
}
