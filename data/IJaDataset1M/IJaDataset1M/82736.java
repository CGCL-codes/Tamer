package tbl.learner;

import tbl.data.CorpusVersion;
import tbl.data.TblRule;

/**
 * This is a data class encapsulating the results 
 * of a rule application to a corpus version.
 * The result comprises the rule that applied, the
 * new corpus version and the new accuracy.
 * 
 * @author bill
 *
 */
public class TestResults implements Comparable<TestResults> {

    TblRule _rule;

    CorpusVersion _version;

    double _accuracy;

    int _ruleGain;

    public TestResults(TblRule rule, CorpusVersion version, double accuracy, int gain) {
        super();
        this._rule = rule;
        this._version = version;
        this._accuracy = accuracy;
        this._ruleGain = gain;
    }

    /**
	 * Simplified use doesn't determine new corpus version or overall accuracy as
	 * rules are tested.
	 * @param rule
	 * @param gain
	 */
    public TestResults(TblRule rule, int gain) {
        super();
        this._rule = rule;
        this._ruleGain = gain;
    }

    /**
	 * @return the _rule
	 */
    public TblRule getRule() {
        return _rule;
    }

    /**
	 * @return the _version
	 */
    public CorpusVersion getVersion() {
        return _version;
    }

    /**
	 * @return the _accuracy
	 */
    public double getAccuracy() {
        return _accuracy;
    }

    /**
	 * @return the _ruleGain
	 */
    public int getRuleGain() {
        return _ruleGain;
    }

    public int compareTo(TestResults otr) {
        return (new Integer(otr.getRuleGain())).compareTo(new Integer(this.getRuleGain()));
    }
}
