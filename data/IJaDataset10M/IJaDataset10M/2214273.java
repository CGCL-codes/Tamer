package com.gusto.test.semsim.measures;

import junit.framework.TestCase;
import com.gusto.engine.semsim.exceptions.SimilarityException;
import com.gusto.engine.semsim.measures.impl.WordNetSimilarity;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.Resource;

public class TestWordNetSimilarity extends TestCase {

    private WordNetSimilarity similarity;

    private Model model;

    public void setUp() throws SimilarityException {
        String root = "src/test/resources/";
        similarity = new WordNetSimilarity(root + "config/wordnet/wordnet.xml", "file:" + root + "config/wordnet/ic-bnc-resnik-add1.dat", "file:" + root + "config/wordnet/domain_independent.txt");
        similarity.setFirstWordOnly(false);
        ModelMaker modelMaker = ModelFactory.createMemModelMaker();
        modelMaker.createModel("test");
        this.model = modelMaker.openModel("test", true);
        Resource r1 = model.createResource("event1");
        r1.addLiteral(model.getProperty("price"), 21000);
        Resource r2 = model.createResource("event2");
        r2.addLiteral(model.getProperty("price"), 35000);
    }

    public void test_value() throws SimilarityException {
        assertEquals(0.09100787382449436, similarity.getSimilarity("Pen", "Cow"));
        assertEquals(1.0, similarity.getSimilarity("Red", "Red"));
        assertEquals(0.3833557536014597, similarity.getSimilarity("Cat", "Dog"));
        assertEquals(0.5341833297994102, similarity.getSimilarity("Car", "Vehicle"));
        assertEquals(0.4167035760208963, similarity.getSimilarity("Flemish", "Dutch"));
    }
}
