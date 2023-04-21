package org.okkam.core.match.query.facets.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.okkam.core.match.query.Expansion;
import org.okkam.core.match.query.StopWordsEN;
import org.okkam.core.match.query.UserCondition;
import org.okkam.core.match.query.exceptions.MatchingFacetException;
import org.okkam.core.match.query.facets.IMatchingFacet;
import org.okkam.core.match.query.facets.IMatchingFacet.Complexity;
import org.okkam.core.match.query.facets.IMatchingFacet.MatchingType;

/**
 * Matching facet that generates expansions by checking whether a single condition
 * can be split into subconditions (e.g. a "full name" into its components).
 * Uses stopwords (for the English language) to eliminate unneccessary expansions.
 * @author stoermer
 * @version 0.0.1
 *
 */
public class Syntactic2 implements IMatchingFacet {

    private static final Log log = LogFactory.getLog(Syntactic2.class);

    private static final String version = "0.0.1";

    public List<Expansion> expand(UserCondition c) throws MatchingFacetException {
        log.debug("->expand()");
        List<Expansion> expansions = new ArrayList<Expansion>();
        int count = 0;
        if (c != null) {
            String value = c.getValueText();
            String[] tokens = value.trim().split("\\s+");
            for (String token : tokens) {
                if (token.length() > 1 && !(StopWordsEN.isStopWord(token))) {
                    Expansion exp = new Expansion();
                    exp.setValueText(token);
                    exp.setValueOperator(c.getValueOperator());
                    expansions.add(exp);
                    ++count;
                }
            }
        } else {
        }
        log.debug("<-expand(). Added " + count + " tokens");
        return expansions;
    }

    public Complexity getComplexity() {
        return Complexity.LOW;
    }

    public String getFacetDescription() {
        return "Creates a query expansion by attempting to split the values of query tokens into individual tokens";
    }

    public String getFacetUID() {
        return this.getClass().getName();
    }

    public MatchingType getMatchingType() {
        return MatchingType.SYNTACTIC;
    }

    public String getVersion() {
        return version;
    }
}
