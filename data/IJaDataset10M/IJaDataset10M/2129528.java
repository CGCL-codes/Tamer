package goalc;

import java.util.Iterator;
import java.util.LinkedList;
import ail.semantics.operationalrules.OSRule;
import ail.semantics.RCStage;
import gov.nasa.jpf.annotation.FilterField;

/**
 * A GOAL Reasoning stage - an example of how to implement a Languages
 * Specific reasoning stage.
 * 
 * @author louiseadennis
 *
 */
public class GOALRCStage implements RCStage {

    /**
	 * An integer representing the stage.  These are named in the
	 * Gwendolen Reasoning Cycle object.
	 */
    @FilterField
    private int stage;

    /**
	 * The name of the stage.
	 */
    @FilterField
    private String name;

    /**
	 * The disjunction of rules for this stage.
	 */
    @FilterField
    private LinkedList<OSRule> rules = new LinkedList<OSRule>();

    /**
	 * Construct a stage from an integer and stage name.
	 * 
	 * @param st an integer representing the stage.
	 * @param s The name of the stage.
	 */
    public GOALRCStage(int st, String s) {
        stage = st;
        name = s;
    }

    /**
	 * Getter method for the number representing the stage.
	 * 
	 * @return the number representing the stage.
	 */
    private int getStageNum() {
        return stage;
    }

    public String getStageName() {
        return name;
    }

    /**
	 * Compare two stages
	 * 
	 * @param arg0 a GOAL Reasoning Cycle Stage
	 * @return 1 if the stages are the same, 0 if they are not.
	 */
    public int compareTo(GOALRCStage arg0) {
        if (arg0.getStageNum() == stage) {
            return 1;
        }
        return 0;
    }

    public Iterator<OSRule> getStageRules() {
        return (rules.iterator());
    }

    public void setRule(OSRule rule) {
        rules.add(rule);
    }

    public String toString() {
        return getStageName();
    }
}
