package edu.neu.ccs.demeterf.inline;

import edu.neu.ccs.demeterf.inline.classes.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.*;
import edu.neu.ccs.demeterf.dispatch.Type;
import edu.neu.ccs.demeterf.demfgen.classes.*;
import edu.neu.ccs.demeterf.demfgen.DemFGenMain;
import edu.neu.ccs.demeterf.demfgen.Diff;
import edu.neu.ccs.demeterf.util.Util;
import edu.neu.ccs.demeterf.inline.GenControl.Edge;

public class Checker {

    static Typer check(List<TypeDef> types, final SubTyping subs, String func, String start, GenControl ctrl, Option<TypeUse> targ) {
        Util.setAllowNull(true);
        Class<?> funcClass = Type.classForName(func);
        FunctionClass fc = FunctionClass.fromClass(funcClass, FC.buildMethodName), ctxF = FunctionClass.fromClass(funcClass, FC.augMethodName);
        Help.print("Function " + fc + "\n");
        final Typer typer = new Typer(types, fc, ctrl, targ, subs, ctxF);
        TypeUse tu = TypeUse.makeType(start);
        TravRet ret = typer.recurse(defFor(tu, types), Set.<String>create());
        final List<AbstRec> recabs = typer.getRecAbstrs();
        Help.print("DONE\n");
        return typer;
    }

    static TypeDef defFor(final TypeUse tu, List<TypeDef> types) {
        try {
            return DemFGenMain.instantiate(tu, types);
        } catch (RE re) {
            throw new RuntimeException(" ** TypeDef in CD not found: \"" + tu.getName() + "\"");
        }
    }
}

class Typer extends ID {

    Traversal trav;

    List<TypeDef> types;

    FunctionClass func;

    FunctionClass ctxFunc;

    GenControl ctrl;

    Option<TypeUse> targ;

    List<AbstRec> recabstr = List.create();

    SubTyping subs;

    Set<EnvEntry> results = Set.<EnvEntry>create(new java.util.Comparator<EnvEntry>() {

        public int compare(EnvEntry a, EnvEntry b) {
            return a.getType().print().compareTo(b.getType().print());
        }
    });

    Map<String, List<Meth>> updates = Map.<String, List<Meth>>create();

    Typer(List<TypeDef> ts, FunctionClass fc, GenControl c, SubTyping s, FunctionClass ctxF) {
        this(ts, fc, c, Option.<TypeUse>none(), s, ctxF);
    }

    Typer(List<TypeDef> ts, FunctionClass fc, GenControl c, Option<TypeUse> ta, SubTyping s, FunctionClass ctxF) {
        types = ts;
        func = fc;
        ctrl = c;
        targ = ta;
        subs = s;
        trav = Traversal.onestep(this);
        ctxFunc = ctxF;
    }

    synchronized void addEnv(EnvEntry ent) {
        if (ent.getRet().equals(SubTyping.obj)) {
            Help.tell(" ** Warning: Object returned from " + ent.getType() + ent.getChoices().toString("", "\n     ") + "\n");
        }
        results = results.add(ent);
    }

    synchronized void addAbst(AbstRec abs) {
        recabstr = recabstr.push(abs);
    }

    public List<AbstRec> getRecAbstrs() {
        return recabstr;
    }

    TravRet recurse(String one, Set<String> recs) {
        Help.print(" Recurse: " + one + "\n");
        if (ctrl.isBuiltIn(one)) {
            TypeUse typ = TypeUse.makeType(one);
            List<Option<TypeUse>> argTs = List.create(Option.some(typ));
            if (targ.isSome()) argTs = argTs.append(targ);
            List<Meth> ap = func.possibleWithStar(argTs, subs);
            if (ap.isEmpty()) {
                addEnv(new EnvEntry(typ, typ, ap));
                return new TravRet(typ, ap);
            }
            TypeUse ret = unifyReturns(ap);
            addEnv(new EnvEntry(typ, ret, ap));
            return new TravRet(ret, ap);
        }
        TypeDef td = Checker.defFor(TypeUse.makeType(one), types);
        return recurse(td, recs);
    }

    TravRet recurse(TypeDef one, Set<String> recs) {
        return trav.traverse(one, recs);
    }

    TravRet combine(ClassDef d, DoGen g, ident n, TypeDefParams ps, PESubtypeList sts, FieldList fs, Impl i, Set<String> recs) {
        if (!sts.isEmpty()) {
            List<Meth> ms = (!fs.isEmpty() && Diff.optionSet(Diff.concretes)) ? check("" + n + ps.print(), fs, recs, true).meths : List.<Meth>create();
            return check("" + n + ps.print(), sts, recs, ms);
        }
        return check("" + n + ps.print(), fs, recs, true);
    }

    TravRet combine(IntfcDef d, DoGen g, ident n, TypeDefParams ps, PESubtypeList sts, Set<String> recs) {
        return check("" + n + ps.print(), sts, recs, List.<Meth>create());
    }

    TravRet check(String t, FieldList fields, final Set<String> recs, boolean save) {
        Help.print(" Check Concrete: " + t + "\n");
        final TypeUse curr = TypeUse.makeType(t);
        List<Edge> fts = ToLst.<Field>toList(fields, Field.class).map(new List.Map<Field, Edge>() {

            public Edge map(Field f) {
                return GenControl.makeEdge(f.getType(), "" + f.getName());
            }
        });
        final Set<String> nextrecs = recs.add(t);
        final List<Option<TravRet>> rets = fts.map(new List.Map<Edge, Option<TravRet>>() {

            public Option<TravRet> map(Edge e) {
                String typ = e.getType().print();
                if (ctrl.skip(curr, e.getField())) return Option.some(new TravRet(e.getType(), List.<Meth>create()));
                if (targ.isSome()) {
                    String fldS = curr.getName() + "$" + e.getField();
                    TypeUse fldC = TypeUse.makeType(fldS);
                    List<Option<TypeUse>> argTs = List.create(Option.some(curr), Option.some(fldC), targ);
                    List<Meth> ap = ctxFunc.possibleWithStar(argTs, subs);
                    if (!ap.isEmpty()) {
                        if (!subs.subtype(unifyReturns(ap), targ.inner())) throw new TypeError("Update method return(s) are not subtypes\n" + ap.toString("\n", "   "));
                        updates = updates.put(fldS, ap);
                    }
                }
                if (nextrecs.contains(typ)) return Option.none();
                return Option.some(recurse(typ, nextrecs));
            }
        });
        List<Option<TypeUse>> argTs = rets.map(new List.Map<Option<TravRet>, Option<TypeUse>>() {

            public Option<TypeUse> map(Option<TravRet> o) {
                return o.isSome() ? Option.some(o.inner().ret) : Option.<TypeUse>none();
            }
        }).push(Option.some(TypeUse.makeType(t)));
        if (targ.isSome()) argTs = argTs.append(targ);
        List<Meth> poss = func.possibleWithStar(argTs, subs);
        String argsStr = signature(argTs);
        Help.print(" Selections for " + argsStr + "\n" + poss.toString("", "    ") + "\n");
        if (poss.length() == 0) throw new TypeError("No Possible Method for: " + argsStr);
        if (!Inline.RESIDUE && poss.length() > 1) throw new TypeError("Too Many Methods [" + poss.length() + "] For: " + argsStr);
        TypeUse ret = unifyReturns(poss);
        if (save) addEnv(new EnvEntry(curr, ret, poss));
        return new TravRet(ret, poss);
    }

    String signature(List<Option<TypeUse>> argTs) {
        return "(" + argTs.toString(new List.Stringer<Option<TypeUse>>() {

            public String toString(Option<TypeUse> f, List<Option<TypeUse>> r) {
                return (f.isSome() ? f.inner().toString() : "*") + (r.isEmpty() ? "" : ", ");
            }
        }) + ")";
    }

    TravRet check(String t, PESubtypeList subtypes, final Set<String> recs, List<Meth> ms) {
        Help.print(" Check Abstract: " + t + "\n");
        final TypeUse curr = TypeUse.makeType(t);
        List<TypeUse> sts = ToLst.<TypeUse>toList(subtypes, TypeUse.class);
        final Set<String> nextrecs = recs.add(t);
        List<Option<TravRet>> rets = sts.map(new List.Map<TypeUse, Option<TravRet>>() {

            public Option<TravRet> map(TypeUse t) {
                if (nextrecs.contains(t.print())) return Option.none();
                return Option.some(recurse(t.print(), nextrecs));
            }
        });
        if (rets.contains(Option.<TravRet>none())) addAbst(new AbstRec(curr, sts));
        final TypeUse upper = unifyResults(rets);
        if (upper == null) throw new TypeError("All Recursive Subtypes for: " + t);
        addEnv(new EnvEntry(curr, upper, ms));
        return new TravRet(upper, ms);
    }

    static <X> List<Option<X>> wrapSomes(List<X> lst) {
        return lst.map(new List.Map<X, Option<X>>() {

            public Option<X> map(X x) {
                return Option.some(x);
            }
        });
    }

    TypeUse unifyReturns(List<Meth> ms) {
        return unify(ms.map(new List.Map<Meth, TypeUse>() {

            public TypeUse map(Meth m) {
                return m.getRet();
            }
        }));
    }

    TypeUse unifyResults(List<Option<TravRet>> ors) {
        return unify(ors.fold(new List.Fold<Option<TravRet>, List<TypeUse>>() {

            public List<TypeUse> fold(Option<TravRet> p, List<TypeUse> tus) {
                return p.isSome() ? tus.push(p.inner().ret) : tus;
            }
        }, List.<TypeUse>create()));
    }

    TypeUse unify(List<TypeUse> tus) {
        if (tus.isEmpty()) return null;
        final TypeUse upper = tus.fold(new List.Fold<TypeUse, TypeUse>() {

            public TypeUse fold(TypeUse n, TypeUse t) {
                while (!subs.subtype(n, t)) t = subs.supertype(t);
                return t;
            }
        }, tus.top());
        return upper;
    }
}

class ToLst<X> extends ID {

    List<X> combine(EmptyList e) {
        return List.<X>create();
    }

    List<X> combine(ConsList c, X f, List<X> r) {
        return r.push(f);
    }

    public static <X> List<X> toList(Object o, Class<?> elm) {
        return new Traversal(new ToLst<X>(), Control.builtins(elm)).<List<X>>traverse(o);
    }
}

class AbstRec {

    TypeUse tu;

    List<TypeUse> subts;

    AbstRec(TypeUse t, List<TypeUse> ss) {
        tu = t;
        subts = ss;
    }

    public String toString() {
        return tu + " = " + subts.toString(" | ", "") + ".";
    }
}

class TravRet {

    TypeUse ret;

    List<Meth> meths;

    TravRet(TypeUse r, List<Meth> ms) {
        ret = r;
        meths = ms;
    }

    public String toString() {
        return ret.toString();
    }
}
