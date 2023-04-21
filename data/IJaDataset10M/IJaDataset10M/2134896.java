package org.cleartk.util.ae.linewriter;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * @author Philip Ogren
 */
public interface BlockWriter<BLOCK_TYPE extends Annotation> {

    public void initialize(UimaContext context) throws ResourceInitializationException;

    public String writeBlock(JCas jCas, BLOCK_TYPE blockAnnotation) throws AnalysisEngineProcessException;
}
