package jmodnews.scoring.rules;

import jmodnews.db.Overview;

/**
 * An overview filter that filters overviews based on an
 * ExpressionTester testing the result of an OverviewExtractor.
 * 
 * Although that sounds complicated, it is a quite common case - 
 * e. g. filter an overview on a regular expression 
 * ({@link RegularExpressionTester}}) matched against the Subject header 
 * ({@link HeaderExtractor}).
 * 
 * @author Michael Schierl <schierlm@gmx.de>
 */
public class ExtractorOverviewFilter implements OverviewFilter {

    private final OverviewExtractor oe;

    private final ExpressionTester et;

    /**
	 * 
	 */
    public ExtractorOverviewFilter(OverviewExtractor oe, ExpressionTester et) {
        this.oe = oe;
        this.et = et;
    }

    public boolean matches(Overview ov) {
        return et.testExpression(oe.extract(ov));
    }
}
