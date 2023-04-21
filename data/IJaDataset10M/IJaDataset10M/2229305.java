package com.molecularnetworks.start.rules;

import java.util.Enumeration;
import org.openscience.cdk.interfaces.IAtomContainer;
import toxTree.core.IDecisionRule;
import toxTree.exceptions.DecisionMethodException;
import toxTree.tree.rules.smarts.RuleSMARTSubstructure;
import ambit2.smarts.query.ISmartsPattern;
import ambit2.smarts.query.SMARTSException;

/**
 * Biodegradation rule for unbranched chemicals with 
 * two terminal double-bonded carbons.
 * @version $Id: RuleTwoTerminalDoubleBondsOnUnbranched.java 936 2008-12-04 17:43:31Z joerg $
 * @author <a href="mailto:info@molecular-networks.com">Molecular Networks</a>
 * @author $Author: joerg $
 */
public class RuleTwoTerminalDoubleBondsOnUnbranched extends RuleSMARTSubstructure {

    /**
     * Default constructor
     */
    public RuleTwoTerminalDoubleBondsOnUnbranched() {
        super();
        try {
            super.addSubstructure("1", "[$([*]~C([!#1])~[*])]", true);
            super.addSubstructure("2", "[$([C&H2]=[C])]");
            super.setContainsAllSubstructures(true);
            super.setExplanation("Unbranched chemicals with two terminal " + "double-bonded carbons are associated with low biodegradability.");
            id = "11";
            title = "Two terminal double-bonded carbons on unbranched molecule";
            examples[0] = "C(C)CCC(C)C";
            examples[1] = "C=CCCCC=C";
            editable = false;
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }

    /**
     * Overrides the default {@link IDecisionRule} behaviour.
     * Returns TRUE, if the answer of the rule is YES for the analyzed 
     * molecule {@link org.openscience.cdk.interfaces.AtomContainer}
     * Returns FALSE, if the answer of the rule is NO for the analyzed 
     * molecule {@link org.openscience.cdk.interfaces.AtomContainer}
     * @param mol  {@link org.openscience.cdk.interfaces.AtomContainer}
     * @return rule result, boolean
     * @throws {@link DecisionMethodException}
     */
    @Override
    public boolean verifyRule(org.openscience.cdk.interfaces.IAtomContainer mol) throws DecisionMethodException {
        logger.info(getID());
        IAtomContainer moltotest = getObjectToVerify(mol);
        if (!isAPossibleHit(mol, moltotest)) {
            logger.debug("Not a possible hit due to the prescreen step.");
            return false;
        }
        Enumeration e = smartsPatterns.keys();
        boolean is_true = false;
        String temp_id = "";
        while (e.hasMoreElements()) {
            temp_id = e.nextElement().toString();
            ISmartsPattern pattern = smartsPatterns.get(temp_id);
            if (null == pattern) {
                throw new DecisionMethodException("ID '" + id + "' is missing in " + getClass().getName());
            }
            try {
                int matchCount = pattern.hasSMARTSPattern(moltotest);
                if ("2".equals(temp_id)) {
                    is_true = matchCount == 2;
                } else {
                    is_true = matchCount > 0;
                }
                logger.debug("SMARTS " + temp_id + "\t" + pattern.toString(), "\tmatches ", matchCount, "times\tresult is ", is_true);
            } catch (Exception x) {
                throw new DecisionMethodException(x);
            }
            if (pattern.isNegate()) {
                is_true = !is_true;
            }
            if (containsAllSubstructures && !is_true) {
                return false;
            } else if (!containsAllSubstructures && is_true) {
                is_true = true;
                break;
            }
        }
        return is_true;
    }
}
