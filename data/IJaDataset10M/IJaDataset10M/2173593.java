package ch.ethz.dcg.spamato.filter.ruleminator;

import java.util.*;

/**
 * @author simon
 */
public class RuleCheckResults implements Iterable<RuleCheckResult> {

    private int spam = 0;

    private Vector<RuleCheckResult> results = new Vector<RuleCheckResult>();

    public RuleCheckResults() {
    }

    public void addResult(RuleCheckResult result) {
        results.add(result);
        if (result.isSpam()) spam++;
    }

    public int getSpamNumber() {
        return spam;
    }

    public boolean isSpam() {
        return spam > 0;
    }

    public int getNumberOfRules() {
        return results.size();
    }

    public Iterator<RuleCheckResult> iterator() {
        return results.iterator();
    }
}
