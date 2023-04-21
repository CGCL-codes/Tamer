package org.cleartk.syntax.opennlp.parser;

import java.util.List;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

/**
 * <br>
 * Copyright (c) 2012, Regents of the University of Colorado <br>
 * All rights reserved.
 */
public interface OutputTypesHelper<TOKEN_TYPE extends Annotation, SENTENCE_TYPE extends Annotation, PARSE_TYPE, TOP_NODE_TYPE extends Annotation> {

    public TOP_NODE_TYPE addParse(JCas jCas, PARSE_TYPE parse, SENTENCE_TYPE sentence, List<TOKEN_TYPE> tokens);
}
