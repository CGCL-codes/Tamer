package com.platinum.dpv.test;

import com.platinum.dpv.DictionaryPasswordFileException;
import com.platinum.dpv.DictionaryPasswordValidator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jlucier
 */
public class DictionaryPasswordValidatorTest {

    public DictionaryPasswordValidatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void loadPasswordDictionaryValidator() {
        try {
            DictionaryPasswordValidator.getInstance();
            assertTrue(true);
        } catch (DictionaryPasswordFileException ex) {
            assertTrue(false);
            Logger.getLogger(DictionaryPasswordValidatorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void lookupDictionaryWord() {
        try {
            DictionaryPasswordValidator pDV = DictionaryPasswordValidator.getInstance();
            assertTrue(pDV.isDictionaryWord("word"));
        } catch (DictionaryPasswordFileException ex) {
            assertTrue(false);
            Logger.getLogger(DictionaryPasswordValidatorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testInvalidDictionaryPassword() {
        try {
            DictionaryPasswordValidator pDV = DictionaryPasswordValidator.getInstance();
            assertTrue(pDV.isPasswordDictionaryBased("Pa8!ss9wo4rd2"));
        } catch (DictionaryPasswordFileException ex) {
            assertTrue(false);
            Logger.getLogger(DictionaryPasswordValidatorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testValidDictionaryPassword() {
        try {
            DictionaryPasswordValidator pDV = DictionaryPasswordValidator.getInstance();
            assertFalse(pDV.isPasswordDictionaryBased("tstweb@234ya!"));
        } catch (DictionaryPasswordFileException ex) {
            assertTrue(false);
            Logger.getLogger(DictionaryPasswordValidatorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
