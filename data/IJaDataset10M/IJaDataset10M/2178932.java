package org.cleartk.srl.propbank.util;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.cleartk.syntax.constituent.type.TopTreebankNode;

/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * 
 * 
 * <p>
 * A <em>PropbankRelation object</em> represents the relation (or a sub-relation) of one label of an
 * entry in Propbank.
 * </p>
 * 
 * @author Philipp Wetzler
 */
public abstract class PropbankRelation {

    /**
   * Parses a relation taken form a Propbank entry and returns its representation as the appropriate
   * <em>PropbankRelation</em> object.
   * 
   * @param s
   *          the textual representation of a relation taken from <tt>prop.txt</tt>
   * 
   * @return a <em>PropbankRelation</em> object representing <b>s</b>
   */
    public static PropbankRelation fromString(String s) {
        if (s.contains("*")) {
            return PropbankCorefRelation.fromString(s);
        } else if (s.contains(",")) {
            return PropbankSplitRelation.fromString(s);
        } else if (s.contains(":")) {
            return PropbankNodeRelation.fromString(s);
        } else {
            return PropbankTerminalRelation.fromString(s);
        }
    }

    /**
   * Convert to an appropriate ClearTK annotation and add it to <b>view</b> if necessary.
   * 
   * @param jCas
   * @param topNode
   *          the top node annotation of the corresponding Treebank parse
   * @return the corresponding annotation
   */
    public abstract Annotation convert(JCas jCas, TopTreebankNode topNode);

    /**
   * Re-generate the text that this object was parsed from.
   */
    @Override
    public abstract String toString();
}
