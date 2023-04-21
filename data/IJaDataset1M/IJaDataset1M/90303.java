package org.p4pp.p3p.appel.ruleset;

import org.p4pp.util.connective.Matchable;
import org.p4pp.p3p.document.InvalidPolicyException;
import org.jdom.Element;
import java.util.List;
import org.p4pp.util.connective.NonsensicalMatchingException;

/**
 * Represents an enumeration of APPEL expressions contained by
 * a <RULE>...</RULE> Element. These may be
 * <code>OtherwiseExpression</code>s,
 * <code>RequestGroupExpression</code>s and/or
 * <code>P3pElementExpression</code>s.
 *
 * Also knows about
 * the connective (or, and,...) by which the expressions
 * shall be connected during evaluation/matching process.
 *
 * @author  <a href="mailto:budzyn@ti.informatik.uni-kiel.de">Nikolaj Budzyn</a>
 */
class ConnectedTopLevelExpressions extends ConnectedElementAndPcdataExpressions {

    /**
     * Creates a new <code>TopLevelExpressions</code> instance for the
     * APPEL expressions contained within the given father APPEL rule.
     *
     * @param  father an <code>AppelRule</code> instance functioning as father of
     * contained expressions.
     * @exception  InvalidRulesetException if <code>fatherElement</code> cannot be
     * parsed as a part of a valid APPEL ruleset.
     */
    public ConnectedTopLevelExpressions(AppelRule father) throws InvalidRulesetException {
        super(father, false);
    }

    /**
     *  Returns true if the contained expressions match the given
     * <code>Matchable</code>.
     * What this means in detail depends on the connective
     * (and, or, ...) and on the matchable.
     *
     * @param  matchable a <code>Matchable</code> value
     * @return  a <code>boolean</code> value
     * @exception  RuntimeException when <code>matchable</code> is
     * not an <code>Evidence</code> instance.
     * @exception  InvalidRulesetException The name says it all...
     * @exception  InvalidPolicyException The name says it all...
     */
    boolean match(Matchable matchable) throws RuntimeException, InvalidRulesetException, InvalidPolicyException {
        try {
            return match((Evidence) matchable);
        } catch (ClassCastException e) {
            throw new RuntimeException("Internal Error in TopLevelExpressions::match (Matchable): Tried to match TopLevelExpressions against a non-Evidence objects. This is not reasonable.");
        }
    }

    /**
     * Returns true if the contained expressions match the given
     * <code>Evidence</code>.
     * What this means in detail depends on the connective
     * (and, or, ...).
     *
     * @param  evidence an <code>Evidence</code> object
     * @return  a <code>boolean</code> value
     * @exception  InvalidRulesetException The name says it all...
     * @exception  InvalidPolicyException The name says it all...
     */
    private boolean match(Evidence evidence) throws InvalidRulesetException, InvalidPolicyException {
        if (isEmpty()) {
            return false;
        }
        try {
            return connective.doTheyMatch(this, evidence);
        } catch (NonsensicalMatchingException e) {
            throw new RuntimeException("Internal Error: NonsensicalMatchingException caught in TopLevelExpressions::matches (). As every NonsensicalMatchingException should have caught by the called methods themselves, this should not happen.");
        } catch (InvalidPolicyException e) {
            throw e;
        } catch (InvalidRulesetException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal Error: Unexpected Exception caught in TopLevelExpressions::match ().");
        }
    }
}
