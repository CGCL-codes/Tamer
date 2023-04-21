package com.rapidminer.operator.learner.tree;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.tools.Tools;

/**
 * A split condition for nominal values (equals).
 * 
 * @author Ingo Mierswa
 */
public class NominalSplitCondition extends AbstractSplitCondition {

    private static final long serialVersionUID = 3883155435836330171L;

    private double value;

    private String valueString;

    public NominalSplitCondition(Attribute attribute, String valueString) {
        super(attribute.getName());
        this.value = attribute.getMapping().getIndex(valueString);
        this.valueString = valueString;
    }

    public boolean test(Example example) {
        double currentValue = example.getValue(example.getAttributes().get(getAttributeName()));
        return Tools.isEqual(currentValue, value);
    }

    public String getRelation() {
        return "=";
    }

    public String getValueString() {
        return this.valueString;
    }
}
