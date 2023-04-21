package org.cleartk.classifier.feature.extractor;

import java.util.List;
import org.apache.uima.jcas.tcas.Annotation;
import org.cleartk.classifier.Feature;
import org.cleartk.classifier.feature.extractor.annotationpair.RelativePositionExtractor;
import org.cleartk.test.DefaultTestBase;
import org.junit.Assert;
import org.junit.Test;

/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * 
 * @author Steven Bethard
 */
public class RelativePositionExtractorTest extends DefaultTestBase {

    @Test
    public void testEquals() {
        this.testOne(5, 8, 5, 8, "EQUALS");
    }

    @Test
    public void testContains() {
        this.testOne(5, 8, 6, 7, "CONTAINS");
        this.testOne(5, 8, 5, 7, "CONTAINS");
        this.testOne(5, 8, 6, 8, "CONTAINS");
    }

    @Test
    public void testContainedBy() {
        this.testOne(5, 8, 3, 8, "CONTAINEDBY");
    }

    @Test
    public void testOverlapsLeft() {
        this.testOne(0, 3, 1, 4, "OVERLAPS_LEFT");
        this.testOne(0, 2, 1, 4, "OVERLAPS_LEFT");
    }

    @Test
    public void testOverlapsRight() {
        this.testOne(19, 21, 10, 20, "OVERLAPS_RIGHT");
        this.testOne(15, 25, 10, 20, "OVERLAPS_RIGHT");
    }

    @Test
    public void testLeftOf() {
        this.testOne(1, 3, 4, 6, "LEFTOF");
        this.testOne(2, 4, 4, 6, "LEFTOF");
    }

    @Test
    public void testRightOf() {
        this.testOne(6, 10, 4, 6, "RIGHTOF");
        this.testOne(6, 10, 2, 5, "RIGHTOF");
    }

    private void testOne(int begin1, int end1, int begin2, int end2, String expected) {
        Annotation annotation1 = new Annotation(jCas, begin1, end1);
        Annotation annotation2 = new Annotation(jCas, begin2, end2);
        RelativePositionExtractor extractor;
        List<Feature> features;
        extractor = new RelativePositionExtractor();
        features = extractor.extract(jCas, annotation1, annotation2);
        Assert.assertEquals(1, features.size());
        Assert.assertEquals("RelativePosition", features.get(0).getName());
        Assert.assertEquals(expected, features.get(0).getValue());
        extractor = new RelativePositionExtractor();
        features = extractor.extract(jCas, annotation1, annotation2);
        Assert.assertEquals(1, features.size());
        Assert.assertEquals("RelativePosition", features.get(0).getName());
        Assert.assertEquals(expected, features.get(0).getValue());
    }
}
