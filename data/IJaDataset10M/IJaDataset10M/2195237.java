package pcgen.core.term;

import pcgen.core.Equipment;
import pcgen.core.PlayerCharacter;

/**
 * The Class <code>EQAltPlusTotalTermEvaluator</code> is responsible for producing 
 * the value of the ALTPLUSTOTAL token for use in equipment and eqmod cost formulas. 
 * 
 * Last Editor: $Author: $
 * Last Edited: $Date:  $
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision:  $
 */
public class EQAltPlusTotalTermEvaluator extends BaseEQTermEvaluator implements TermEvaluator {

    /**
	 * Instantiates a new eQ plus term evaluator.
	 * 
	 * @param expressionString the expression string
	 */
    public EQAltPlusTotalTermEvaluator(String expressionString) {
        this.originalText = expressionString;
    }

    public Float resolve(Equipment eq, boolean primary, PlayerCharacter pc) {
        return convertToFloat(originalText, evaluate(eq, primary, pc));
    }

    public String evaluate(Equipment eq, boolean primary, PlayerCharacter pc) {
        return Integer.toString(eq.calcPlusForHead(false));
    }

    public boolean isSourceDependant() {
        return false;
    }

    public boolean isStatic() {
        return false;
    }
}
