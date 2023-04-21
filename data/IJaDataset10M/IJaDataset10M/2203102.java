package javax.speech.synthesis;

import java.util.Locale;
import javax.speech.SpeechLocale;
import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.synthesis.Voice}.
 * 
 * @author Dirk Schnelle
 * 
 */
public class VoiceTest extends TestCase {

    /**
     * Test method for {@link javax.speech.synthesis.Voice#hashCode()}.
     */
    public void testHashCode() {
        final Voice voice1 = new Voice();
        final Voice voice2 = new Voice();
        assertTrue(voice1.hashCode() != voice2.hashCode());
    }

    /**
     * Test method for {@link javax.speech.synthesis.Voice#getAge()}.
     */
    public void testGetAge() {
        final Voice voice1 = new Voice();
        assertEquals(Voice.AGE_DONT_CARE, voice1.getAge());
        final Voice voice2 = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE, Voice.AGE_DONT_CARE, Voice.VARIANT_DEFAULT);
        assertEquals(Voice.AGE_DONT_CARE, voice2.getAge());
        final Voice voice3 = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE, 42, Voice.VARIANT_DEFAULT);
        assertEquals(42, voice3.getAge());
    }

    /**
     * Test method for {@link javax.speech.synthesis.Voice#getGender()}.
     */
    public void testGetGender() {
        final Voice voice1 = new Voice();
        assertEquals(Voice.GENDER_DONT_CARE, voice1.getGender());
        final Voice voice2 = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE, Voice.AGE_DONT_CARE, Voice.VARIANT_DEFAULT);
        assertEquals(Voice.GENDER_MALE, voice2.getGender());
        final Voice voice3 = new Voice(SpeechLocale.US, "mary", Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        assertEquals(Voice.GENDER_FEMALE, voice3.getGender());
    }

    /**
     * Test method for {@link javax.speech.synthesis.Voice#getSpeechLocale()}.
     */
    public void testGetSpeechLocale() {
        final Voice voice1 = new Voice();
        assertNull(voice1.getSpeechLocale());
        final Voice voice2 = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE, Voice.AGE_DONT_CARE, Voice.VARIANT_DEFAULT);
        assertEquals(SpeechLocale.US, voice2.getSpeechLocale());
        final Voice voice3 = new Voice(null, "john", Voice.GENDER_MALE, Voice.AGE_DONT_CARE, Voice.VARIANT_DEFAULT);
        assertNull(voice3.getSpeechLocale());
    }

    /**
     * Test method for {@link javax.speech.synthesis.Voice#getName()}.
     */
    public void testGetName() {
        final Voice voice1 = new Voice();
        assertNull(voice1.getName());
        final Voice voice2 = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE, Voice.AGE_DONT_CARE, Voice.VARIANT_DEFAULT);
        assertEquals("john", voice2.getName());
        final Voice voice3 = new Voice(SpeechLocale.US, "", Voice.GENDER_MALE, Voice.AGE_DONT_CARE, Voice.VARIANT_DEFAULT);
        assertEquals("", voice3.getName());
        final Voice voice4 = new Voice(SpeechLocale.US, null, Voice.GENDER_MALE, Voice.AGE_DONT_CARE, Voice.VARIANT_DEFAULT);
        assertNull(voice4.getName());
    }

    /**
     * Test method for {@link javax.speech.synthesis.Voice#getVariant()}.
     */
    public void testGetVariant() {
        final Voice voice1 = new Voice();
        assertEquals(Voice.VARIANT_DONT_CARE, voice1.getVariant());
        final Voice voice2 = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE, Voice.AGE_DONT_CARE, Voice.VARIANT_DEFAULT);
        assertEquals(Voice.VARIANT_DEFAULT, voice2.getVariant());
        final Voice voice3 = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE, Voice.AGE_DONT_CARE, Voice.VARIANT_DONT_CARE);
        assertEquals(Voice.VARIANT_DONT_CARE, voice3.getVariant());
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.Voice#equals(java.lang.Object)}.
     */
    public void testEqualsObject() {
        Voice voice1 = new Voice();
        assertFalse(voice1.equals("test"));
        Voice voice2 = new Voice();
        assertTrue(voice1.equals(voice2));
        Voice voice3 = new Voice(null, null, Voice.GENDER_DONT_CARE, Voice.AGE_DONT_CARE, Voice.VARIANT_DONT_CARE);
        assertTrue(voice1.equals(voice3));
        final Voice voice4 = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE, Voice.AGE_DONT_CARE, Voice.VARIANT_DEFAULT);
        assertFalse(voice1.equals(voice4));
        final Voice voice5 = new Voice(SpeechLocale.US, "mary", Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        assertFalse(voice1.equals(voice5));
        assertFalse(voice4.equals(voice5));
        final Voice voice6 = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE, Voice.AGE_DONT_CARE, Voice.VARIANT_DEFAULT);
        assertTrue(voice4.equals(voice6));
    }

    /**
     * Test method for {@link javax.speech.synthesis.Voice#toString()}.
     */
    public void testToString() {
        final Voice voice = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE, Voice.AGE_DONT_CARE, Voice.VARIANT_DEFAULT);
        final String str = voice.toString();
        assertTrue(str.indexOf("john") > 0);
        assertTrue(str.indexOf(Locale.US.toString()) > 0);
    }

    /**
     * Test method for
     * {@link javax.speech.synthesis.Voice#match(javax.speech.synthesis.Voice)}.
     */
    public void testMatch() {
        Voice voice1 = new Voice();
        assertTrue(voice1.match((Voice) null));
        Voice voice2 = new Voice();
        assertTrue(voice1.match(voice2));
        Voice voice3 = new Voice(null, null, Voice.GENDER_DONT_CARE, Voice.AGE_DONT_CARE, Voice.VARIANT_DONT_CARE);
        assertTrue(voice1.match(voice3));
        final Voice voice4 = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE, Voice.AGE_DONT_CARE, Voice.VARIANT_DEFAULT);
        assertTrue(voice4.match(voice1));
        assertFalse(voice1.match(voice4));
        final Voice voice5 = new Voice(SpeechLocale.US, "mary", Voice.GENDER_FEMALE, 41, Voice.VARIANT_DEFAULT);
        assertTrue(voice5.match(voice1));
        assertFalse(voice4.match(voice5));
        final Voice voice6 = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE, Voice.AGE_DONT_CARE, Voice.VARIANT_DEFAULT);
        assertTrue(voice4.match(voice6));
        final Voice voice7 = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE, Voice.AGE_MIDDLE_ADULT, Voice.VARIANT_DEFAULT);
        final Voice voice8 = new Voice(SpeechLocale.US, "john", Voice.GENDER_MALE, Voice.AGE_MIDDLE_ADULT + 4, Voice.VARIANT_DEFAULT);
        assertTrue(voice7.match(voice8));
    }
}
