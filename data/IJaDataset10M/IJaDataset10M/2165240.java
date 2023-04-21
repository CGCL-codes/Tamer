package com.miden2ena.mogeci.collaudo;

import junit.framework.*;
import com.miden2ena.mogeci.pojo.*;
import com.miden2ena.mogeci.dao.*;
import com.miden2ena.mogeci.exceptions.*;
import com.miden2ena.mogeci.enumerazioni.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author petruu
 */
public class caricaDizionariTest extends TestCase {

    public caricaDizionariTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of generate method, of class com.miden2ena.mogeci.collaudo.caricaDizionari.
     */
    public void testGenerate() {
        System.out.println("generate");
        caricaDizionari.generate();
    }
}
