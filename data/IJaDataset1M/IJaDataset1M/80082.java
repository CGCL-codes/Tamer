package org.slasoi.gslam.templateregistry.plog;

class _op {

    enum Type {

        PREFIX, INFIX, POSTFIX
    }

    public static final int MAX_PRIORITY = 1200;

    Clause cop = null;

    int pre_priority, priority, post_priority;

    boolean prex, postx;

    String name;

    String quoted;

    _op(Clause cop) throws Error {
        this.cop = cop;
        priority = cop.arg(0).asClause().asInteger();
        Clause specifier = cop.arg(1).asClause();
        Clause op = cop.arg(2).asClause();
        name = op.name;
        quoted = op.quoted;
        prex = specifier.is(PI.xf_0) || specifier.is(PI.xfx_0) || specifier.is(PI.xfy_0);
        postx = specifier.is(PI.fx_0) || specifier.is(PI.xfx_0) || specifier.is(PI.yfx_0);
        pre_priority = prex ? this.priority - 1 : this.priority;
        post_priority = postx ? this.priority - 1 : this.priority;
    }

    static Clause[] builtInOps() throws Error {
        PI op = PI.current_op_3;
        return new Clause[] { new Clause(op, 1200, PI.xfx_0, PI.rule_n), new Clause(op, 1200, PI.fx_0, PI.rule_n), new Clause(op, 1200, PI.fx_0, PI.query_n), new Clause(op, 1100, PI.xfy_0, PI.disjunction_2), new Clause(op, 1000, PI.xfy_0, PI.conjunction_2), new Clause(op, 900, PI.fy_0, PI.not_provable_1), new Clause(op, 700, PI.xfx_0, PI.unify_2), new Clause(op, 700, PI.xfx_0, PI.not_unify_2), new Clause(op, 700, PI.xfx_0, PI.constructor_2), new Clause(op, 700, PI.xfx_0, PI.is_2), new Clause(op, 700, PI.xfx_0, PI.equal_2), new Clause(op, 700, PI.xfx_0, PI.not_equal_2), new Clause(op, 700, PI.xfx_0, PI.gte_2), new Clause(op, 700, PI.xfx_0, PI.gt_2), new Clause(op, 700, PI.xfx_0, PI.lt_2), new Clause(op, 700, PI.xfx_0, PI.lte_2), new Clause(op, 500, PI.yfx_0, PI.add_2), new Clause(op, 500, PI.yfx_0, PI.subtract_2), new Clause(op, 400, PI.yfx_0, PI.multiply_2), new Clause(op, 400, PI.yfx_0, PI.divide_2), new Clause(op, 400, PI.yfx_0, PI.int_divide_2), new Clause(op, 400, PI.yfx_0, PI.mod_2), new Clause(op, 200, PI.fy_0, PI.negate_1), new Clause(op, 100, PI.xfy_0, PI.colon_2), new Clause(op, 700, PI.xfx_0, PI.stnd_equal_2), new Clause(op, 700, PI.xfx_0, PI.stnd_not_equal_2) };
    }
}
