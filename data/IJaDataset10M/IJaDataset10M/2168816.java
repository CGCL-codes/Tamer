package com.hp.hpl.jena.vocabulary.test;

import com.hp.hpl.jena.vocabulary.*;
import junit.framework.*;

/**
     @author kers
*/
public class TestVocabResultSet extends VocabTestBase {

    public TestVocabResultSet(String name) {
        super(name);
    }

    public static TestSuite suite() {
        return new TestSuite(TestVocabResultSet.class);
    }

    public void testResultSet() {
        String ns = "http://jena.hpl.hp.com/2003/03/result-set#";
        assertResource(ns + "ResultSolution", ResultSet.ResultSolution);
        assertResource(ns + "ResultBinding", ResultSet.ResultBinding);
        assertResource(ns + "ResultSet", ResultSet.ResultSet);
        assertProperty(ns + "value", ResultSet.value);
        assertProperty(ns + "resultVariable", ResultSet.resultVariable);
        assertProperty(ns + "variable", ResultSet.variable);
        assertProperty(ns + "size", ResultSet.size);
        assertProperty(ns + "binding", ResultSet.binding);
        assertProperty(ns + "solution", ResultSet.solution);
        assertResource(ns + "undefined", ResultSet.undefined);
    }
}
