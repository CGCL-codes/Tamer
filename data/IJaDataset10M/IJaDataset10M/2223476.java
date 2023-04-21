package com.hp.hpl.jena.vocabulary.test;

import com.hp.hpl.jena.rdf.model.test.ModelTestBase;
import com.hp.hpl.jena.rdf.model.*;

/**
 	@author kers
*/
public class VocabTestBase extends ModelTestBase {

    public VocabTestBase(String name) {
        super(name);
    }

    public void assertProperty(String uri, Property p) {
        assertResource(uri, p);
    }

    public void assertResource(String uri, Resource r) {
        assertEquals(uri, r.getURI());
    }
}
