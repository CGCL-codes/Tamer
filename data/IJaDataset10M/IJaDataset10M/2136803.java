package org.xteam.sled.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xteam.sled.semantic.exp.IExpRewriter;

public class AbsoluteSequence implements ISequence {

    private List<RangeConstraint<AbsoluteField>> constaints;

    private Map<String, Integer> labels = new HashMap<String, Integer>();

    private int length;

    public AbsoluteSequence(List<RangeConstraint<AbsoluteField>> constraints, Map<String, Integer> labels, int length) {
        this.constaints = constraints;
        this.labels = labels;
        this.length = length;
    }

    public AbsoluteSequence(RangeConstraint<AbsoluteField> constraint, int length) {
        this.constaints = new ArrayList<RangeConstraint<AbsoluteField>>();
        this.constaints.add(constraint);
        this.length = length;
    }

    public AbsoluteSequence(int length) {
        this.constaints = new ArrayList<RangeConstraint<AbsoluteField>>();
        this.length = length;
    }

    public Map<String, Integer> getLabels() {
        return labels;
    }

    public List<RangeConstraint<AbsoluteField>> getConstraints() {
        return constaints;
    }

    @Override
    public int bitLength() {
        return length;
    }

    @Override
    public ISequence createSequence(boolean hasPrefix, boolean hasSuffix, List<ISequent> elements) {
        throw new RuntimeException();
    }

    @Override
    public List<ISequent> getElements() {
        throw new RuntimeException();
    }

    @Override
    public boolean hasPrefix() {
        return false;
    }

    @Override
    public boolean hasSuffix() {
        return false;
    }

    @Override
    public boolean isContradictory() {
        for (RangeConstraint<AbsoluteField> cnts : constaints) {
            if (cnts.isContradictory()) return true;
        }
        return false;
    }

    @Override
    public boolean isFieldBinding() {
        return false;
    }

    @Override
    public ISequence mergeLeft(ISequence o) {
        AbsoluteSequence other = (AbsoluteSequence) o;
        Map<AbsoluteField, RangeConstraint<AbsoluteField>> result = new HashMap<AbsoluteField, RangeConstraint<AbsoluteField>>();
        for (RangeConstraint<AbsoluteField> c : constaints) {
            if (result.containsKey(c.field())) {
                result.put(c.field(), c.intersection(result.get(c.field())));
            } else {
                result.put(c.field(), c);
            }
        }
        for (RangeConstraint<AbsoluteField> c : other.constaints) {
            if (result.containsKey(c.field())) {
                result.put(c.field(), c.intersection(result.get(c.field())));
            } else {
                result.put(c.field(), c);
            }
        }
        Map<String, Integer> labels = new HashMap<String, Integer>();
        return new AbsoluteSequence(new ArrayList<RangeConstraint<AbsoluteField>>(result.values()), labels, length);
    }

    @Override
    public ISequence mergeRight(ISequence o) {
        throw new RuntimeException();
    }

    @Override
    public ISequence subst(IExpRewriter subs) {
        throw new RuntimeException();
    }

    @Override
    public ISequence postDots() {
        throw new RuntimeException();
    }

    @Override
    public ISequence preDots() {
        throw new RuntimeException();
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[").append(length).append("]");
        buffer.append(labels);
        boolean isFirst = true;
        for (RangeConstraint<AbsoluteField> element : constaints) {
            if (!isFirst) buffer.append(", ");
            buffer.append(element.toString());
            isFirst = false;
        }
        return buffer.toString();
    }
}
