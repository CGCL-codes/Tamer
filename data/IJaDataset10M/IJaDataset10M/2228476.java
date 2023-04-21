package org.openscience.cdk.formula;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.formula.rules.IRule;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.ILoggingTool;
import org.openscience.cdk.tools.LoggingToolFactory;

/**
 * <p> Validate a molecular formula given in IMolecularformula object. The
 * validation is based according different rules that you have to introduce before
 * see IRule.
 * 
 * @cdk.module  formula
 * @author      miguelrojasch
 * @cdk.created 2007-11-20
 * @cdk.keyword molecule, molecular formula
 * @see         IRule
 */
@TestClass("org.openscience.cdk.formula.MolecularFormulaCheckerTest")
public class MolecularFormulaChecker {

    private ILoggingTool logger = LoggingToolFactory.createLoggingTool(MolecularFormulaChecker.class);

    /** List of IRules to be applied in the validation.*/
    private List<IRule> rules;

    /**
	 * Construct an instance of MolecularFormulaChecker. It must be initialized
	 * with the rules to applied. 
	 * 
	 * @param rules  A List with IRule to be applied
	 */
    public MolecularFormulaChecker(List<IRule> rules) {
        this.rules = rules;
    }

    /**
	 * Get the IRules to be applied to validate the IMolecularFormula.
	 * 
	 * @return A List with IRule
	 */
    @TestMethod("testGetRules")
    public List<IRule> getRules() {
        return rules;
    }

    /**
	 * Validate if a IMolecularFormula is valid. The result more close to 1 means
	 * maximal probability to be valid. Opposite more close to 0 means minimal
	 * probability to be valid. To know the result in each IRule use 
	 * {@link #isValid(IMolecularFormula)} 
	 * 
	 * @param  formula      The IMolecularFormula value
	 * @return              The percent of the validity
	 * @see                 #isValid(IMolecularFormula)
	 */
    @TestMethod("testIsValidSum_IMolecularFormula")
    public Double isValidSum(IMolecularFormula formula) {
        double result = 1.0;
        IMolecularFormula formulaWith = isValid(formula);
        Map<Object, Object> properties = formulaWith.getProperties();
        Iterator<IRule> iterRules = rules.iterator();
        while (iterRules.hasNext()) {
            result *= (Double) properties.get(iterRules.next().getClass());
        }
        return result;
    }

    /**
	 * Validate if a IMolecularFormula is valid. The results of each IRule which
	 * has to be applied is put into IMolecularFormula as properties. To extract
	 * the result final as the product of rule's result use 
	 * {@link #isValidSum(IMolecularFormula)}.
	 * 
	 * @param  formula      The IMolecularFormula value
	 * @return formulaWith  The IMolecularFormula with the results for each
	 *                      IRule into properties
	 * @see                 #isValidSum(IMolecularFormula)
	 */
    @TestMethod("testIsValid_IMolecularFormula")
    public IMolecularFormula isValid(IMolecularFormula formula) {
        logger.info("Generating the validity of the molecular formula");
        if (formula.getIsotopeCount() == 0) {
            logger.error("Proposed molecular formula has not elements");
            return formula;
        }
        Iterator<IRule> iterRules = rules.iterator();
        try {
            while (iterRules.hasNext()) {
                IRule rule = iterRules.next();
                double result = rule.validate(formula);
                formula.setProperty(rule.getClass(), result);
            }
        } catch (CDKException e) {
            e.printStackTrace();
        }
        return formula;
    }
}
