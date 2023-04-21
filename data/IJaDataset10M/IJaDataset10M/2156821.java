package com.gorillalogic.dal.common.expr;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.common.*;
import com.gorillalogic.help.Help;

class MaxFunction extends AggregateExpr {

    MaxFunction() {
    }

    private MaxFunction(CommonExpr arg) throws AccessException {
        super(arg);
    }

    public CommonExpr make(CommonType scopeType, CommonExpr arg) throws AccessException {
        return new MaxFunction(arg);
    }

    public String getName() {
        return "max";
    }

    public void summary(Help.Out out) {
        out.print("Find maximum value among numerical inputs");
    }

    public void detail(Help.Out out) {
        final String agg = out.glossary("Aggregate function", "aggregate_function");
        out.print("This " + agg + ",given an input table with one numerical column, finds the maximum value.");
        out.print("The inputs must be numerical, the result type has a numerical column");
        out.print("that matches the input type -- for example int to int, float to float, etc.");
    }

    protected int readDirectInt(CommonScope scope) throws AccessException {
        CommonTable table = evalArg(scope);
        int rez = Integer.MIN_VALUE;
        if (table == null) return rez;
        Table.Itr itr = table.loopLock();
        while (itr.next()) {
            int next = itr.getInt(0);
            rez = Math.max(next, rez);
        }
        return rez;
    }

    protected long readDirectLong(CommonScope scope) throws AccessException {
        CommonTable table = evalArg(scope);
        long rez = Long.MIN_VALUE;
        if (table == null) return rez;
        Table.Itr itr = table.loopLock();
        while (itr.next()) {
            long next = itr.getLong(0);
            rez = Math.max(next, rez);
        }
        return rez;
    }

    protected float readDirectFloat(CommonScope scope) throws AccessException {
        CommonTable table = evalArg(scope);
        float rez = Float.MIN_VALUE;
        if (table == null) return rez;
        Table.Itr itr = table.loopLock();
        while (itr.next()) {
            float next = itr.getFloat(0);
            rez = Math.max(next, rez);
        }
        return rez;
    }

    protected double readDirectDouble(CommonScope scope) throws AccessException {
        CommonTable table = evalArg(scope);
        double rez = Double.MIN_VALUE;
        if (table == null) return rez;
        Table.Itr itr = table.loopLock();
        while (itr.next()) {
            double next = itr.getDouble(0);
            rez = Math.max(next, rez);
        }
        return rez;
    }

    protected String readDirectString(CommonScope scope) throws AccessException {
        CommonTable table = evalArg(scope);
        String rez = null;
        if (table == null) return rez;
        Table.Itr itr = table.loopLock();
        while (itr.next()) {
            String next = itr.getString(0);
            if (rez == null) {
                rez = next;
            } else if (next != null && rez.compareTo(next) < 0) {
                rez = next;
            }
        }
        return rez;
    }
}
