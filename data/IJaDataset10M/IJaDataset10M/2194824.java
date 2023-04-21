package com.divosa.eformulieren.web.core.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arjen
 */
public class SHA1SignatureTest {

    public SHA1SignatureTest() {
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

    /**
     * Test of calculateSHA1 method, of class SHA1Signature.
     */
    @Test
    public void testCalculateSHA1() throws Exception {
        System.out.println("calculateSHA1, test expected result that is mentioned in manual");
        String data = "12341500EURMyPSPID";
        String key = "Mysecretsig1875?!";
        String expResult = "837F18F9B6C1CD2AA89FFA6D1B6AAFAD8523E60E";
        String result = SHA1Signature.calculateSHA1(data, key);
        if (!expResult.equalsIgnoreCase(result)) {
            System.out.print("Input: " + data + key + ", output: " + result + ", Expected: " + "837F18F9B6C1CD2AA89FFA6D1B6AAFAD8523E60E)");
        }
        assertEquals(expResult.toUpperCase(), result.toUpperCase());
    }
}
