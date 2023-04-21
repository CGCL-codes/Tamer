package com.google.code.kaptcha.text.impl;

import java.util.Properties;
import junit.framework.TestCase;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.util.Config;

/**
 * @author cliffano
 */
public class DefaultTextCreatorTest extends TestCase {

    private DefaultTextCreator creator;

    private Properties properties;

    public void setUp() {
        properties = new Properties();
        creator = new DefaultTextCreator();
        creator.setConfig(new Config(properties));
    }

    public void testGetTextCreatesRandomTextOfSpecifiedLengthAndOnlyConsistsSpecifiedChars() {
        properties.put(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING, "abc");
        properties.put(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "9");
        String text = creator.getText();
        assertEquals(9, text.length());
        for (int i = 0; i < text.length(); i++) {
            char currChar = text.charAt(i);
            assertTrue(currChar == 'a' || currChar == 'b' || currChar == 'c');
        }
    }

    public void testGetTextWithNullConfigValuesGivesDefaultCharsAndLength() {
        String text = creator.getText();
        assertEquals(5, text.length());
        for (int i = 0; i < text.length(); i++) {
            char currChar = text.charAt(i);
            assertTrue("abcde2345678gfynmnpwx".indexOf(currChar) >= 0);
        }
    }
}
