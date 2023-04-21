package org.golibri.common;

import junit.framework.*;
import org.golibri.iso.*;

public class TEST_Language extends TestCase {

    public void testConstructor1WithValidParameters() {
        Language language = new Language("aa");
        language = new Language("AB");
        Assert.assertTrue(true);
    }

    public void testConstructor2WithValidParameters() {
        Language language = new Language(new Alpha2LanguageCode("aa"));
        language = new Language(new Alpha2LanguageCode("AB"));
        Assert.assertTrue(true);
    }

    public void testConstructor1WithInvalidParameters() {
        try {
            String string = null;
            Language language = new Language(string);
            Assert.fail();
        } catch (NullPointerException exc) {
            Assert.assertTrue(true);
        }
        try {
            Language language = new Language("xxx");
            Assert.fail();
        } catch (IllegalArgumentException exc) {
            Assert.assertTrue(true);
        }
        try {
            Language language = new Language("x");
            Assert.fail();
        } catch (IllegalArgumentException exc) {
            Assert.assertTrue(true);
        }
    }

    public void testConstructor2WithInvalidParameters() {
        try {
            Alpha2LanguageCode languageCode = null;
            Language language = new Language(languageCode);
            Assert.fail();
        } catch (NullPointerException exc) {
            Assert.assertTrue(true);
        }
        try {
            Language language = new Language(new Alpha2LanguageCode("xxx"));
            Assert.fail();
        } catch (IllegalArgumentException exc) {
            Assert.assertTrue(true);
        }
        try {
            Language language = new Language(new Alpha2LanguageCode("x"));
            Assert.fail();
        } catch (IllegalArgumentException exc) {
            Assert.assertTrue(true);
        }
    }

    public void testGetAlpha2LanguageCode() {
        Language language = new Language("aa");
        Alpha2LanguageCode languageCode = new Alpha2LanguageCode("aa");
        Assert.assertTrue(language.getAlpha2LanguageCode().equals(languageCode));
    }

    public void testEquals() throws Exception {
        Language language1 = new Language("aa");
        Language language2 = new Language("AA");
        Language language3 = new Language("ab");
        Assert.assertTrue(language1.equals(language1));
        Assert.assertTrue(!language1.equals(null));
        Assert.assertTrue(!language1.equals(""));
        Assert.assertTrue(language1.equals(language2));
        Assert.assertTrue(language2.equals(language1));
        Assert.assertTrue(!language1.equals(language3));
    }

    public void testHashCode() throws Exception {
        Language language1 = new Language("aa");
        Language language2 = new Language("AA");
        Language language3 = new Language("ab");
        Assert.assertTrue(language1.hashCode() == language1.hashCode());
        Assert.assertTrue(language1.hashCode() == language2.hashCode());
        Assert.assertTrue(language1.hashCode() != language3.hashCode());
    }

    public void testToString() throws Exception {
        Language language1 = new Language("aa");
        Assert.assertTrue("aa".equalsIgnoreCase(language1.toString()));
    }
}
