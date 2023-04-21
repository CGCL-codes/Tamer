package org.modss.facilitator.model.v1.scoregraph;

import java.util.Locale;

/**
 * "More Is Worse (linear)" score graph.
 *
 * @author mag@netstorm.net.au
 */
public class MoreIsWorseLinear extends AbstractScoreGraph {

    /** Name of this score graph. */
    private String NAME = " ";

    private Locale locale = Locale.getDefault();

    /** Package private. */
    MoreIsWorseLinear() {
    }

    /** See {@link AbstractScoreGraph#doEvaluate}. */
    public double doEvaluate(double normalisedInput) {
        return (1 - normalisedInput);
    }

    /** See {@link ScoreGraph#getDisplayName}. */
    public String getDisplayName() {
        NAME = (locale.getLanguage().equals("es")) ? "M�s es Peor (linear)" : "More Is Worse (linear)";
        return NAME;
    }
}
