package edu.umn.cs.nlp.mt.chiang2007.intersection;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import edu.umn.cs.nlp.Scorable;
import edu.umn.cs.nlp.mt.TranslationRule;
import edu.umn.cs.nlp.mt.chiang2007.LoglinearTranslationFeatures;
import edu.umn.cs.nlp.mt.huangchiang2005.Hyperarc;

/**
 * Represents an application of a context-free rule in a CKY+ parse chart.
 * <p>
 * The k-best extraction technique used in Huang & Chiang (2005) requires that a parse chart be viewed as a hypergraph.
 * In that context, a RuleApplication serves as a hyperarc in a hypergraph.
 * 
 * @author Lane Schwartz
 * @version $LastChangedDate: 2008-02-27 13:00:33 -0500 (Wed, 27 Feb 2008) $
 * @see "Better k-best Parsing" by Liang Huang & David Chiang (IWPT, 2005)
 */
public class RuleApplication extends Hyperarc<ChartCellEntry, Translation> implements Comparable<RuleApplication>, Scorable {

    private final LoglinearTranslationFeatures ruleFeatures;

    private final double score;

    private final String targetLanguageRHS;

    /**
	 * Construct a RuleApplication.
	 * This objects represents the application of the specified rule,
	 * such that the new parse chart entry head spans over all of the child cells (specified by tail).
	 * 
	 * 
	 * @param rule
	 * @param head
	 * @param tail
	 */
    public RuleApplication(TranslationRule rule, ChartCellEntry head, List<ChartCellEntry> tail) {
        super(head, tail);
        ruleFeatures = rule.getFeatures();
        score = ruleFeatures.getScore();
        targetLanguageRHS = rule.getTargetLanguageRHS();
    }

    public RuleApplication(ChartCellEntry terminalEntry) {
        super(terminalEntry, Collections.<ChartCellEntry>emptyList());
        this.ruleFeatures = LoglinearTranslationFeatures.getDefaultFeatures();
        this.score = 0.0;
        this.targetLanguageRHS = "";
    }

    public int compareTo(RuleApplication r) {
        if (score > r.score) return 1; else if (score < r.score) return -1; else return 0;
    }

    protected Translation getDerivation(List<Translation> children) {
        return new Translation(this, children);
    }

    public LoglinearTranslationFeatures getRuleFeatures() {
        return ruleFeatures;
    }

    public LoglinearTranslationFeatures getFeatures() {
        return ruleFeatures;
    }

    /**
	 * Gets a score for this rule application using the log-linear feature weights of the associated rule.
	 * 
	 * @return a score for this rule application using the log-linear feature weights of the associated rule
	 */
    public double getWeight() {
        return score;
    }

    /**
	 * Get the score for this item.
	 * 
	 * @return the score for this item
	 */
    public double getScore() {
        return score;
    }

    public String toString() {
        return targetLanguageRHS;
    }

    /** Logger for this method. */
    private static final Logger logger = Logger.getLogger(RuleApplication.class.getName());

    /**
	 * Convenience method to turn on logging during testing and debugging.
	 * <p>
	 * For normal uses, a logging configuration file probably be used instead of this method.
	 * 
	 * @param level Level at which to begin logging
	 */
    static void enableLogging(Level level) {
        ConsoleHandler handler = new ConsoleHandler();
        try {
            handler.setEncoding("UTF-8");
        } catch (SecurityException e) {
        } catch (UnsupportedEncodingException e) {
        }
        handler.setFormatter(new Formatter() {

            public String format(LogRecord record) {
                return "\tRULE-APP: " + formatMessage(record) + "\n";
            }
        });
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
        logger.setLevel(level);
    }
}
