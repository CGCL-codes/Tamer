package com.rapidminer.operator.learner.igss.hypothesis;

import java.util.Arrays;
import java.util.LinkedList;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;

/** Objects of this class represent a conjunctive rule.
 *  All abstract methods of the superclass Hypothesis are implemented.
 *   
 *  @author Dirk Dach 
 */
public class Rule extends Hypothesis {

    private static final long serialVersionUID = -1121263970366784202L;

    /** All literals possible with the given attributeset. */
    private Literal[][] allLiterals;

    /** The premise of the rule.*/
    private Literal[] literals;

    /** The index of the label class this rule predicts aka Y+ or Y-. */
    private int prediction;

    /** Creates a new rule,initializes the regularAttributes and the literals attribute. */
    public Rule(Attribute[] regularAttributes, Attribute label, boolean rejectionSampling, boolean createAll) {
        super(regularAttributes, label, rejectionSampling, createAll);
        allLiterals = new Literal[regularAttributes.length][];
        for (int attributeIndex = 0; attributeIndex < regularAttributes.length; attributeIndex++) {
            allLiterals[attributeIndex] = new Literal[regularAttributes[attributeIndex].getMapping().size()];
            for (int valueIndex = 0; valueIndex < regularAttributes[attributeIndex].getMapping().size(); valueIndex++) {
                allLiterals[attributeIndex][valueIndex] = new Literal(regularAttributes[attributeIndex], valueIndex, attributeIndex);
            }
        }
    }

    /** Clones the rule with covered and positive weight.*/
    @Override
    public Hypothesis clone() {
        Rule clone = new Rule(this.literals, this.prediction);
        clone.setCoveredWeight(this.getCoveredWeight());
        clone.setPositiveWeight(this.getPositiveWeight());
        return clone;
    }

    /** Construct a new rule with one literal. */
    public Rule(Literal literal, int prediction) {
        super();
        this.literals = new Literal[1];
        this.literals[0] = literal;
        this.prediction = prediction;
    }

    /** Construct a new rule with the given literals. */
    public Rule(Literal[] literals, int prediction) {
        super();
        this.literals = new Literal[literals.length];
        for (int i = 0; i < literals.length; i++) {
            this.literals[i] = literals[i];
        }
        this.prediction = prediction;
    }

    /** Applies the rule to the given examples. */
    @Override
    public void apply(Example e) {
        if (this.applicable(e)) {
            if (rejectionSampling) {
                coveredWeight++;
                if ((int) e.getLabel() == this.prediction) {
                    positiveWeight++;
                }
            } else {
                coveredWeight += e.getWeight();
                if ((int) e.getLabel() == this.prediction) {
                    positiveWeight += e.getWeight();
                }
            }
        }
    }

    /** Test if the rule is applicable to the given examples without updating the corresponding value. */
    @Override
    public boolean applicable(Example e) {
        boolean success = true;
        for (int i = 0; i < literals.length; i++) {
            int exampleValue = (int) e.getValue(literals[i].getAttribute());
            int ruleValue = literals[i].getValue();
            if (exampleValue != ruleValue) {
                success = false;
                break;
            }
        }
        return success;
    }

    /** Creates all rules with length<=minComplexity. */
    @Override
    public LinkedList<Hypothesis> init(int minComplexity) {
        LinkedList<Hypothesis> border = new LinkedList<Hypothesis>();
        LinkedList<Hypothesis> result = new LinkedList<Hypothesis>();
        for (int attributeIndex = 0; attributeIndex < allLiterals.length; attributeIndex++) {
            for (int valueIndex = 0; valueIndex < allLiterals[attributeIndex].length; valueIndex++) {
                border.addLast(new Rule(allLiterals[attributeIndex][valueIndex], POSITIVE_CLASS));
            }
        }
        while (!border.isEmpty()) {
            Rule rule = (Rule) border.removeFirst();
            result.addLast(rule);
            if (createAllHypothesis) {
                result.addLast(new Rule(rule.getLiterals(), NEGATIVE_CLASS));
            }
            if (rule.getComplexity() < minComplexity) {
                border.addAll(rule.refine());
            }
        }
        return result;
    }

    /** Creates all successors of the rule that have one more literal. */
    @Override
    public LinkedList<Hypothesis> refine() {
        LinkedList<Hypothesis> result = new LinkedList<Hypothesis>();
        Literal[] lits = new Literal[literals.length + 1];
        for (int i = 0; i < literals.length; i++) {
            lits[i] = literals[i];
        }
        int lastLiteralIndex = literals[literals.length - 1].getIndex();
        for (int literalIndex = lastLiteralIndex + 1; literalIndex < allLiterals.length; literalIndex++) {
            for (int valueIndex = 0; valueIndex < allLiterals[literalIndex].length; valueIndex++) {
                lits[lits.length - 1] = allLiterals[literalIndex][valueIndex];
                result.addLast(new Rule(lits, this.prediction));
            }
        }
        return result;
    }

    /** Returns true only if this hypothesis can still be refined.*/
    @Override
    public boolean canBeRefined() {
        if ((literals[literals.length - 1].getIndex() == allLiterals.length - 1)) {
            return false;
        } else {
            return true;
        }
    }

    /** Returns the index of prediction of this rule*/
    @Override
    public int getPrediction() {
        return this.prediction;
    }

    /** Returns the lenght of the premise of the rule. */
    @Override
    public int getComplexity() {
        return this.literals.length;
    }

    /** Returns true if the two rules have the same premise and make the same perdiction. */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Rule)) return false;
        Rule otherRule = (Rule) o;
        if (otherRule.literals.length != this.literals.length) {
            return false;
        }
        if (otherRule.prediction != this.prediction) {
            return false;
        }
        boolean result = true;
        for (int i = 0; i < this.literals.length; i++) {
            if (!(this.literals[i].equals(otherRule.literals[i]))) {
                result = false;
                break;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.literals) ^ Integer.valueOf(this.prediction).hashCode();
    }

    /** Returns a String representation of the rule. */
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer("IF ");
        for (int i = 0; i < literals.length - 1; i++) {
            result.append(literals[i].toString() + " AND ");
        }
        result.append(literals[literals.length - 1].toString());
        result.append(" THEN (" + getLabel().getName() + "=" + getLabel().getMapping().mapIndex(this.getPrediction()) + ")");
        return result.toString();
    }

    /** Returns the literals in the premise of this rule.*/
    public Literal[] getLiterals() {
        return literals;
    }
}
