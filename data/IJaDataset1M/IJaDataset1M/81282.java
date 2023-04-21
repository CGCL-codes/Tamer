package org.slasoi.gslam.templateregistry.plog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * The core Prolog Engine.
 * @author Keven T. Kearney
 */
public class Engine {

    public static final int MAX_ARITY = 1000;

    public static final String USER_MODULE_NAME = "user";

    public static final String SYSTEM_MODULE_NAME = "system";

    public static final String DEFAULT_MODULE_TYPE = "default";

    public static final String SYSTEM_MODULE_TYPE = "system";

    public static final String VERSION = "Zen Prolog v9.0";

    _m_wrapper user = null;

    _m_wrapper system = null;

    private Map<String, _m_wrapper> ____M_WRAPPERS = Collections.synchronizedMap(new Hashtable<String, _m_wrapper>());

    public Engine() {
        try {
            user = new _m_wrapper(this, new Module.Impl(USER_MODULE_NAME, DEFAULT_MODULE_TYPE));
            ____M_WRAPPERS.put(USER_MODULE_NAME, user);
            system = new _m_wrapper(this, new Module.Impl(SYSTEM_MODULE_NAME, SYSTEM_MODULE_TYPE));
            ____M_WRAPPERS.put(SYSTEM_MODULE_NAME, user);
            Clause[] bi_ops = _op.builtInOps();
            for (Clause c : bi_ops) system.declareOperator(c, false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Shouldn't happen");
        }
    }

    public Clause parse(String s) throws Error {
        return user.parse(s);
    }

    public Session session(String query) throws Error {
        return session(parse(query));
    }

    public Session session(Clause query) throws Error {
        return Session.create(this, user, query);
    }

    public void consult(String module, Clause c) throws Error {
        _m_wrapper m = this.__GET_MODULE(module);
        _m_wrapper.consultClause(this, m, c, new ArrayList<_m_wrapper>());
    }

    public void unconsult(String module, Clause c) throws Error {
        _m_wrapper m = this.__GET_MODULE(module);
        _m_wrapper.unconsultClause(this, m, c);
    }

    public int getModuleCount() {
        return ____M_WRAPPERS.size();
    }

    public void addModule(Module m) throws Error {
        if (this.____M_WRAPPERS.get(m.name()) != null) {
            throw Error.permission(PI.create_0, PI.module_0, new Clause(m.name()));
        }
        _m_wrapper mw = new _m_wrapper(this, m);
        ____M_WRAPPERS.put(m.name(), mw);
    }

    public void removeModule(Module m) throws Error {
        if (this.____M_WRAPPERS.get(m.name()) == null) {
            throw Error.existence(PI.module_0, new Clause(m.name()));
        }
        ____M_WRAPPERS.remove(m.name());
    }

    public List<String> imported(String module_name) {
        _m_wrapper mw = this.____M_WRAPPERS.get(module_name);
        return mw != null ? mw.imported() : new ArrayList<String>();
    }

    _m_wrapper __GET_MODULE(String name) throws Error {
        _m_wrapper m = this.____M_WRAPPERS.get(name);
        if (m == null) {
            throw Error.existence(PI.module_0, new Clause(name));
        }
        return m;
    }

    public Clause.Iterator listClauses(String module_name) throws Error {
        return __GET_MODULE(module_name).listClauses();
    }

    public String write(Term t, boolean quoted) {
        try {
            return t != null ? system.parser.write(t, quoted, false) : "null";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    protected Clause.Iterator extendedBuiltinCandidates(Module m, Clause c) {
        return null;
    }

    Clause.Iterator __CANDIDATES(_m_wrapper m, Clause c) {
        if (m == null) m = user;
        Clause.Iterator ci = extendedBuiltinCandidates(m.module, c);
        if (ci == null) ci = ____SYS_CANDIDATES(m, c);
        if (ci == null) ci = m.controlledCandidates(c.pi(), true);
        return ci;
    }

    private Clause.Iterator ____SYS_CANDIDATES(final _m_wrapper m, final Clause c) {
        char first_char = c.name.charAt(0);
        switch(first_char) {
            case 'a':
                if (c.is(PI.and_n)) {
                    final int n = c.arity();
                    if (n > 0) {
                        return new Clause.OneShot(c) {

                            protected Clause once() throws Error {
                                Clause rule = new Clause(PI.rule_n);
                                rule.add(c);
                                for (int i = 0; i < n; i++) rule.add(c.arg(i));
                                return rule;
                            }
                        };
                    }
                    return null;
                } else if (c.is(PI.arg_3)) {
                    return new Clause.Iterator() {

                        int n = 0;

                        boolean initialised = false;

                        boolean done = false;

                        public Clause next() throws Error {
                            if (done) return null;
                            Clause term = c.arg(1).asClause();
                            if (!term.isType(PI.compound_0)) {
                                throw Error.type(PI.compound_1, term);
                            }
                            if (!initialised) {
                                initialised = true;
                                Clause index = c.arg(0).asClause();
                                n = index.asInteger();
                                if (n < 0) {
                                    throw Error.domain(PI.not_less_than_zero_0, index);
                                }
                            }
                            if (n > 0 && n < term.arity() + 1) {
                                Clause res = new Clause(PI.arg_3);
                                res.add(new Clause("" + n));
                                res.add(term);
                                res.add(term.arg(n - 1));
                                n++;
                                if (n > term.arity()) done = true;
                                return res;
                            }
                            done = true;
                            return null;
                        }
                    };
                } else if (c.is(PI.asserta_1)) {
                    return new Clause.OneShot(c) {

                        protected Clause once() throws Error {
                            Clause q = c.arg(0).asClause();
                            m.add(q, true, false);
                            return c;
                        }
                    };
                } else if (c.is(PI.assertz_1)) {
                    return new Clause.OneShot(c) {

                        protected Clause once() throws Error {
                            Clause q = c.arg(0).asClause();
                            m.add(q, true, true);
                            return c;
                        }
                    };
                } else if (c.is(PI.atomic_1)) {
                    return new Clause.OneShot(c.arg(0).isType(PI.atomic_0) ? c : null);
                }
                return null;
            case 'b':
                if (c.is(PI.bagof_3)) {
                    return _bi_all_solutions.solve(this, m, c);
                }
                return null;
            case 'c':
                if (c.is(PI.call_1)) {
                    return ____DO_CALL(m, c);
                } else if (c.is(PI.calling_context_1)) {
                    return new Clause.OneShot(c) {

                        protected Clause once() throws Error {
                            Clause res = new Clause(PI.calling_context_1);
                            res.add(new Clause(m.module.name()));
                            return res;
                        }
                    };
                } else if (c.is(PI.clause_2)) {
                    return new Clause.Iterator() {

                        Clause.Iterator candidates = null;

                        boolean done = false;

                        public Clause next() throws Error {
                            if (done) return null;
                            if (candidates == null) {
                                Clause HEAD = c.arg(0).asCallable();
                                PI pi = HEAD.pi();
                                if (__IS_BI(pi) || !m.module.isPublic(pi)) {
                                    throw Error.permission(PI.access_0, PI.private_procedure_0, pi.asClause());
                                }
                                Term BODY = c.arg(1);
                                if (!BODY.isType(PI.var_0)) {
                                    BODY.asCallable();
                                }
                                candidates = m.controlledCandidates(pi, true);
                            }
                            if (candidates == null) {
                                done = true;
                                return null;
                            }
                            Clause next = candidates.next();
                            if (next != null) {
                                Clause res = new Clause(PI.clause_2);
                                if (next.is(PI.rule_n)) {
                                    res.add(next.arg(0));
                                    if (next.arity() > 2) {
                                        List<Term> subgoals = new ArrayList<Term>();
                                        for (int i = 1; i < next.arity(); i++) subgoals.add(next.arg(i));
                                        Term body = Term.toPrologConjunction(subgoals);
                                        res.add(body);
                                    } else {
                                        res.add(next.arg(1));
                                    }
                                } else {
                                    res.add(next);
                                    res.add(new Clause(PI.true_0));
                                }
                                return res;
                            } else {
                                done = true;
                            }
                            done = true;
                            return null;
                        }
                    };
                } else if (c.is(PI.compound_1)) {
                    return new Clause.OneShot(c.arg(0).isType(PI.compound_0) ? c : null);
                } else if (c.is(PI.conc_3)) {
                    return _bi_lists.concat(c);
                } else if (c.is(PI.copy_term_2)) {
                    return new Clause.OneShot(c) {

                        public Clause once() throws Error {
                            Term X = c.arg(0);
                            Term t = X.isType(PI.var_0) ? Var.autogen() : Term.instantiate(X.asClause(), true);
                            Clause res = new Clause(PI.copy_term_2);
                            res.add(X);
                            res.add(t);
                            return res;
                        }
                    };
                } else if (c.is(PI.current_module_1) || c.is(PI.current_module_2)) {
                    return new Clause.Iterator() {

                        List<_m_wrapper> list = null;

                        public Clause next() throws Error {
                            if (list == null) {
                                list = new ArrayList<_m_wrapper>();
                                list.addAll(____M_WRAPPERS.values());
                            }
                            if (!list.isEmpty()) {
                                _m_wrapper mw = list.remove(0);
                                Clause res = new Clause(PI.current_module_1);
                                res.add(new Clause(mw.module.name()));
                                if (c.is(PI.current_module_2)) {
                                    res.add(new Clause(mw.module.moduleType()));
                                }
                                return res;
                            }
                            return null;
                        }
                    };
                }
                return null;
            case 'd':
                return null;
            case 'e':
                if (c.is(PI.stnd_equal_2)) {
                    return _bi_evaluator.compare(c);
                } else if (c.is(PI.exists_1)) {
                    return new Clause.OneShot(c) {

                        protected Clause once() throws Error {
                            Clause pi = c.arg(0).asClause();
                            if (!pi.ispi()) {
                                throw Error.type(PI.predicate_indicator_0, pi);
                            }
                            Clause q = new Clause(pi.arg(0).asClause().name);
                            int arity = pi.arg(1).asClause().asInteger();
                            for (int i = 0; i < arity; i++) q.add(Var.autogen());
                            Session session = Session.create(Engine.this, m, q);
                            try {
                                if (session.solveNext()) return c;
                            } catch (Error pe) {
                                if (!pe.error().is(PI.existence_error_2)) throw pe;
                            }
                            return null;
                        }
                    };
                }
                return null;
            case 'f':
                if (c.is(PI.findall_3)) {
                    return _bi_all_solutions.solve(this, m, c);
                } else if (c.is(PI.float_1)) {
                    return new Clause.OneShot(c.arg(0).isType(PI.float_0) ? c : null);
                } else if (c.is(PI.functor_3)) {
                    return new Clause.OneShot(c) {

                        protected Clause once() throws Error {
                            Term t = c.arg(0);
                            if (!t.isType(PI.var_0)) {
                                Clause term = t.asClause();
                                Clause res = new Clause(PI.functor_3);
                                res.add(term);
                                res.add(new Clause(term.quoted));
                                res.add(new Clause("" + term.arity()));
                                return res;
                            } else {
                                Clause name = c.arg(1).asClause();
                                if (name.isType(PI.compound_0)) {
                                    throw Error.type(PI.atomic_0, name);
                                }
                                Clause c_arity = c.arg(2).asClause();
                                int arity = c_arity.asInteger();
                                if (arity > MAX_ARITY) {
                                    throw Error.representation(PI.max_arity_0);
                                } else if (arity < 0) {
                                    throw Error.domain(PI.not_less_than_zero_0, c_arity);
                                }
                                if (arity > 0 && !name.isType(PI.atom_0)) {
                                    throw Error.type(PI.atom_0, name);
                                }
                                Clause T = new Clause(name.quoted);
                                for (int i = 0; i < arity; i++) T.add(Var.autogen());
                                Clause res = new Clause(PI.functor_3);
                                res.add(T);
                                res.add(name);
                                res.add(c_arity);
                                return res;
                            }
                        }
                    };
                }
                return null;
            case 'g':
                if (c.is(PI.stnd_gt_2) || c.is(PI.stnd_gte_2)) {
                    return _bi_evaluator.compare(c);
                }
                return null;
            case 'h':
                return null;
            case 'i':
                if (c.is(PI.integer_1)) {
                    return new Clause.OneShot(c.arg(0).isType(PI.integer_0) ? c : null);
                } else if (c.is(PI.is_2)) {
                    return _bi_evaluator.is(c);
                }
                return null;
            case 'l':
                if (c.is(PI.listing_0)) {
                    try {
                        Clause.Iterator ci = m.listClauses();
                        Clause k = ci.next();
                        while (k != null) {
                            System.out.println(k.toString());
                            k = ci.next();
                        }
                        ;
                    } catch (Error e) {
                        e.printStackTrace();
                    }
                } else if (c.is(PI.stnd_lt_2) || c.is(PI.stnd_lte_2)) {
                    return _bi_evaluator.compare(c);
                }
                return null;
            case 'm':
                if (c.is(PI.member_2)) {
                    return _bi_lists.member(c);
                }
                return null;
            case 'n':
                if (c.is(PI.stnd_not_equal_2)) {
                    return _bi_evaluator.compare(c);
                } else if (c.is(PI.nonvar_1)) {
                    return new Clause.OneShot(!c.arg(0).isType(PI.var_0) ? c : null);
                } else if (c.is(PI.not_1)) {
                    return ____DO_CALL(m, c);
                } else if (c.is(PI.number_1)) {
                    return new Clause.OneShot(c.arg(0).isType(PI.number_0) ? c : null);
                }
                return null;
            case 'o':
                if (c.is(PI.once_1)) {
                    return ____DO_CALL(m, c);
                } else if (c.is(PI.op_3)) {
                    return new Clause.OneShot(c) {

                        protected Clause once() throws Error {
                            m.declareOperator(c);
                            return c;
                        }
                    };
                } else if (c.is(PI.or_n)) {
                    final int n = c.arity();
                    if (n > 0) {
                        return new Clause.Iterator(true) {

                            List<Term> list = null;

                            public Clause next() throws Error {
                                if (list == null) {
                                    list = new ArrayList<Term>();
                                    for (int i = 0; i < n; i++) list.add(c.arg(i));
                                }
                                if (!list.isEmpty()) {
                                    Clause rule = new Clause(PI.rule_n);
                                    rule.add(c);
                                    rule.add(list.remove(0));
                                    return rule;
                                }
                                return null;
                            }
                        };
                    }
                    return null;
                }
                return null;
            case 'r':
                if (c.is(PI.reg_expr_match_2)) {
                    return _bi_evaluator.compare(c);
                } else if (c.is(PI.repeat_0)) {
                    return new Clause.Iterator() {

                        public Clause next() throws Error {
                            return c;
                        }
                    };
                } else if (c.is(PI.retract_1)) {
                    return new Clause.Iterator() {

                        List<Clause> matches = null;

                        boolean done = false;

                        public Clause next() throws Error {
                            if (done) return null;
                            if (matches == null) {
                                matches = new ArrayList<Clause>();
                                Clause arg = c.arg(0).asCallable();
                                if (arg.is(PI.rule_n)) arg = arg.arg(0).asCallable();
                                PI pi = arg.pi();
                                Clause.Iterator it = m.controlledCandidates(pi, false);
                                if (it == null) {
                                    done = true;
                                    return null;
                                }
                                Clause res = it.next();
                                while (res != null) {
                                    Clause r = Term.instantiate(res, true);
                                    Clause j = r.is(PI.rule_n) ? r.arg(0).asClause() : r;
                                    if (Session.unify(c.arg(0), j, new Hashtable<Var, Term>())) {
                                        matches.add(res);
                                    }
                                    res = it.next();
                                }
                            }
                            Clause result = null;
                            while (result == null) {
                                if (!matches.isEmpty()) {
                                    Clause r = matches.remove(0);
                                    if (m.module.retract(r)) {
                                        Clause res = new Clause(PI.retract_1);
                                        res.add(r.is(PI.rule_n) ? r.arg(0) : r);
                                        return res;
                                    }
                                } else {
                                    break;
                                }
                            }
                            done = true;
                            return null;
                        }
                    };
                }
                return null;
            case 's':
                if (c.is(PI.setof_3)) {
                    return _bi_all_solutions.solve(this, m, c);
                }
                return null;
            case 'v':
                if (c.is(PI.var_1)) {
                    return new Clause.OneShot(c.arg(0).isType(PI.var_0) ? c : null);
                }
                return null;
            default:
                if (c.is(PI.hat_2)) {
                    return new Clause.OneShot(null);
                } else if (c.is(PI.conjunction_2)) {
                    return new Clause.OneShot(c) {

                        protected Clause once() throws Error {
                            Clause rule = new Clause(PI.rule_n);
                            rule.add(c);
                            rule.add(c.arg(0));
                            rule.add(c.arg(1));
                            return rule;
                        }
                    };
                } else if (c.is(PI.disjunction_2)) {
                    if (c.arg(0).is(PI.if_2)) {
                        return __DO_IF_THEN_ELSE(m, c);
                    } else {
                        return new Clause.Iterator() {

                            List<Term> list = null;

                            public Clause next() throws Error {
                                if (list == null) {
                                    list = new ArrayList<Term>();
                                    list.add(c.arg(0));
                                    list.add(c.arg(1));
                                }
                                if (!list.isEmpty()) {
                                    Clause rule = new Clause(PI.rule_n);
                                    rule.add(c);
                                    rule.add(list.remove(0));
                                    return rule;
                                }
                                return null;
                            }
                        };
                    }
                } else if (c.is(PI.if_2)) {
                    return __DO_IF_THEN_ELSE(m, c);
                } else if (c.is(PI.not_provable_1)) {
                    return ____DO_CALL(m, c);
                } else if (c.is(PI.constructor_2)) {
                    return new Clause.OneShot(c) {

                        protected Clause once() throws Error {
                            Clause term = null;
                            Clause list = null;
                            try {
                                term = c.arg(0).asClause();
                                List<Term> x = new ArrayList<Term>();
                                x.add(new Clause(term.quoted));
                                for (int i = 0; i < term.arity(); i++) x.add(term.arg(i));
                                list = Term.toPrologList(x);
                            } catch (Error e) {
                                try {
                                    list = c.arg(1).asClause();
                                    List<Term> terms = Term.fromPrologList(list);
                                    if (terms.size() > 0) {
                                        Term t = terms.get(0);
                                        if (t.isType(PI.atom_0)) {
                                            term = new Clause(t.asClause().quoted);
                                            for (int i = 1; i < terms.size(); i++) {
                                                term.add(terms.get(i));
                                            }
                                        }
                                    }
                                } catch (Error e2) {
                                }
                            }
                            if (term != null && list != null) {
                                Clause res = new Clause(PI.constructor_2);
                                res.add(term);
                                res.add(list);
                                return res;
                            }
                            return null;
                        }
                    };
                } else if (c.is(PI.equal_2)) {
                    return _bi_evaluator.compare(c);
                } else if (c.is(PI.gt_2) || c.is(PI.gte_2)) {
                    return _bi_evaluator.compare(c);
                } else if (c.is(PI.lt_2) || c.is(PI.lte_2)) {
                    return _bi_evaluator.compare(c);
                } else if (c.is(PI.not_equal_2)) {
                    return _bi_evaluator.compare(c);
                }
                return null;
        }
    }

    private Clause.Iterator ____DO_CALL(final _m_wrapper m, final Clause c) {
        return new Clause.Iterator() {

            Session session = null;

            boolean done = false;

            public Clause next() throws Error {
                if (done) return null;
                if (session == null) {
                    Clause q = c.arg(0).asCallable();
                    session = Session.create(Engine.this, m, q);
                }
                done = !c.is(PI.call_1);
                boolean NOT_PROVABLE = c.is(PI.not_provable_1) || c.is(PI.not_1);
                try {
                    if (session.solveNext()) {
                        return NOT_PROVABLE ? null : c;
                    } else if (NOT_PROVABLE) {
                        return c;
                    }
                } catch (Error pe) {
                    if (NOT_PROVABLE && pe.error().is(PI.existence_error_2)) {
                        return c;
                    } else throw pe;
                }
                done = true;
                return null;
            }
        };
    }

    private Clause.Iterator __DO_IF_THEN_ELSE(final _m_wrapper m, Clause c) {
        return new Clause.OneShot(c) {

            protected Clause once() throws Error {
                boolean simple = c.is(PI.if_2);
                Clause _if_then = simple ? c : c.arg(0).asClause();
                Clause _if = _if_then.arg(0).asCallable();
                Term _then = _if_then.arg(1);
                Term _else = simple ? null : c.arg(1);
                Session session = Session.create(Engine.this, m, _if);
                Clause rule = new Clause(PI.rule_n);
                rule.add(c);
                if (session.solveNext()) {
                    rule.add(_then);
                } else {
                    if (simple) rule = null; else rule.add(_else);
                }
                return rule;
            }
        };
    }

    static boolean __IS_BI(PI pi) throws Error {
        Class<?>[] bis = new Class<?>[] { PI.class };
        for (Class<?> bi : bis) {
            try {
                Field[] fs = bi.getFields();
                for (Field f : fs) {
                    Object o = f.get(bi);
                    if (o instanceof PI) {
                        PI x = (PI) o;
                        String n = x.name();
                        int a = x.arity();
                        if (pi.name.equals(n) && (pi.arity == a || (a < 0 && pi.arity >= Math.abs(a)))) {
                            return true;
                        }
                    }
                }
            } catch (IllegalAccessException iae) {
                throw Error.system(iae);
            }
        }
        return false;
    }
}
