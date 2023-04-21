package org.cleartk.util.ae.linewriter.block;

import org.apache.uima.UimaContext;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.cleartk.util.ae.linewriter.BlockWriter;

/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * @author Philip Ogren
 */
public class BlankLineBlockWriter implements BlockWriter<Annotation> {

    public void initialize(UimaContext context) throws ResourceInitializationException {
    }

    private static String newline = System.getProperty("line.separator");

    public String writeBlock(JCas cas, Annotation blockAnnotation) {
        return newline;
    }
}
