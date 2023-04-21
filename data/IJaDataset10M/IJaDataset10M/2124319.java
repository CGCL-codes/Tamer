package unbfuzzy.fis.mamdani;

import java.util.ArrayList;
import java.util.List;
import unbfuzzy.defuzz.IDefuzzification;
import unbfuzzy.fuzzyvariable.FuzzyVariable;
import unbfuzzy.membershipfunctions.nondifferentiable.MaxMembershipFunction;

/**
 * Given the rules, uses the Max t-conorm as an ALSO operator.
 * 
 * @author Marcelo Vale Asari
 */
public class MaxOutputType extends Output {

    public MaxOutputType(IDefuzzification defuzzification, FuzzyVariable variable) {
        super(defuzzification, variable);
    }

    @Override
    public void calculateOutput() {
        MaxMembershipFunction mf = new MaxMembershipFunction();
        boolean wasActivated = false;
        for (MinMaxRule rule : rules) {
            if (rule.getFiringStrength() > 0) {
                mf.addMembershipFunction(rule.getRuleOutput());
                wasActivated = true;
            }
        }
        if (wasActivated) {
            output = defuzzification.defuzzify(mf, 0.0);
        } else {
            output = 0;
        }
    }
}
