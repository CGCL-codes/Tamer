package net.jalisq.builder;

import net.jalisq.functions.Operation;
import net.jalisq.types.Expression;

class DefaultCondBuilder implements CondBuilder {

    private Expression<Boolean> exp;

    DefaultCondBuilder(Expression<Boolean> exp) {
        this.exp = exp;
    }

    @Override
    public CondBuilder and(Expression<Boolean> con) {
        if (exp != null && exp instanceof CondBuilder) {
            ((CondBuilder) exp).and(con);
        }
        DefaultCondBuilder ret = new DefaultCondBuilder(new CondOp("and", exp, con));
        exp = ret;
        return ret;
    }

    @Override
    public CondBuilder or(Expression<Boolean> con) {
        if (exp != null && exp instanceof CondBuilder) {
            ((CondBuilder) exp).or(con);
        }
        DefaultCondBuilder ret = new DefaultCondBuilder(new CondOp("or", exp, con));
        exp = ret;
        return ret;
    }

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }

    public String getRepr() {
        return exp.getRepr();
    }

    private static class CondOp extends Operation {

        private String rep;

        public CondOp(String rep, Expression lhand, Expression rhand) {
            super(lhand, rhand);
            this.rep = rep;
        }

        @Override
        protected String getOpSymbol() {
            return rep;
        }
    }
}
