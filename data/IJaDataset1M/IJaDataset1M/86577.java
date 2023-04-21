package com.controltier.ctl.tasks.conditions;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * Custom condition for number comparison
 * @ant.type name="numcompare"
 */
public class NumCompare implements Condition {

    private double arg1;

    private double arg2;

    private Op operation;

    public double getArg1() {
        return arg1;
    }

    public void setArg1(double arg1) {
        this.arg1 = arg1;
    }

    public double getArg2() {
        return arg2;
    }

    public void setArg2(double arg2) {
        this.arg2 = arg2;
    }

    public void setOp(final Operator op) {
        if (GreaterThanEquals.op.equals(op.getValue())) {
            operation = new GreaterThanEquals();
        } else if (GreaterThan.op.equals(op.getValue())) {
            operation = new GreaterThan();
        } else if (LessThanEquals.op.equals(op.getValue())) {
            operation = new LessThanEquals();
        } else if (LessThan.op.equals(op.getValue())) {
            operation = new LessThan();
        }
    }

    void validate() {
        if (null == operation) throw new BuildException("op attribute not set");
        if (-1 == arg1) throw new BuildException("arg1 attribute not set");
        if (-1 == arg2) throw new BuildException("arg2 attribute not set");
    }

    /**
     * Implements method in {@link Condition}
     * @return returns true if condition is met
     * @throws BuildException
     */
    public boolean eval() throws BuildException {
        validate();
        return operation.doIt(arg1, arg2);
    }

    abstract class Op {

        abstract boolean doIt(final double arg1, final double arg2);
    }

    class GreaterThanEquals extends Op {

        static final String op = "gte";

        boolean doIt(final double arg1, final double arg2) {
            return arg1 >= arg2;
        }
    }

    class GreaterThan extends Op {

        static final String op = "gt";

        boolean doIt(final double arg1, final double arg2) {
            return arg1 > arg2;
        }
    }

    class LessThanEquals extends Op {

        static final String op = "lte";

        boolean doIt(final double arg1, final double arg2) {
            return arg1 <= arg2;
        }
    }

    class LessThan extends Op {

        static final String op = "lt";

        boolean doIt(final double arg1, final double arg2) {
            return arg1 < arg2;
        }
    }

    public static class Operator extends EnumeratedAttribute {

        public String[] getValues() {
            return new String[] { GreaterThanEquals.op, GreaterThan.op, LessThanEquals.op, LessThan.op };
        }
    }
}
