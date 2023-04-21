package org.freeform;

import java.io.IOException;
import org.freeform.exp.AnyExp;
import org.freeform.exp.CustomExp;
import org.freeform.exp.DecimalExp;
import org.freeform.exp.Exp;
import org.freeform.exp.Generalize;
import org.freeform.exp.ExpExp;
import org.freeform.exp.ExpRefExp;
import org.freeform.exp.GenericExpFactory;
import org.freeform.exp.IdExp;
import org.freeform.exp.IntExp;
import org.freeform.exp.LitExp;
import org.freeform.exp.LabelledExp;
import org.freeform.exp.NilExp;
import org.freeform.exp.NoExp;
import org.freeform.exp.PathExp;
import org.freeform.exp.PkgExp;
import org.freeform.exp.PopExp;
import org.freeform.exp.ProcessExp;
import org.freeform.exp.RefExp;
import org.freeform.exp.TraceExp;
import org.freeform.exp.func.Apply;
import org.freeform.exp.func.Drop;
import org.freeform.exp.func.Embed;
import org.freeform.exp.func.StringLex;
import org.freeform.exp.func.Use;
import org.freeform.func.Call;
import org.freeform.func.FuncForm;
import org.freeform.func.Function;
import org.freeform.func.PureFunction;
import org.freeform.func.Get;
import org.freeform.func.Name;
import org.freeform.func.TracedFunction;
import org.freeform.tokens.TokenLex;

public class Root extends Pkg {

    private Context fallback;

    public Root(Context fallback) throws IOException {
        super(null, null, ".");
        this.fallback = fallback;
        add(new Named(".", this));
        Pkg exp = new Pkg(this, null);
        Pkg ff = new Pkg(this, null);
        Pkg std = new Pkg(this, null);
        this.add(new Named("ff", ff));
        ff.add(new Named("exp", exp));
        ff.add(new Named("std", std));
        ff.add(new Named("func", new FuncForm(this)));
        Exp func = (Exp) ff.get("func.func");
        ExpExp expexp = new ExpExp(func);
        exp.add(new Named("exp", expexp));
        exp.add(new Named("gexp", ExpExp.CONSTRUCTOR));
        exp.add(new Named("pkg", PkgExp.CONSTRUCTOR));
        exp.add(new Named("process", ProcessExp.CONSTRUCTOR));
        exp.add(new Named("custom", new CustomExp(expexp)));
        exp.add(new Named("gcustom", CustomExp.CONSTRUCTOR));
        exp.add(new Named("generalize", Generalize.INSTANCE));
        exp.add(new Named("runctx", Call.arg(1)));
        exp.add(new Named("ref", RefExp.INSTANCE));
        exp.add(new Named("expref", ExpRefExp.INSTANCE));
        exp.add(new Named("id", IdExp.INSTANCE));
        exp.add(new Named("path", PathExp.INSTANCE));
        exp.add(new Named("lit", LitExp.INSTANCE));
        exp.add(new Named("int", IntExp.INSTANCE));
        exp.add(new Named("decimal", DecimalExp.INSTANCE));
        exp.add(new Named("any", AnyExp.INSTANCE));
        exp.add(new Named("nil", NilExp.INSTANCE));
        exp.add(new Named("noexp", NoExp.INSTANCE));
        exp.add(new Named("pop", PopExp.INSTANCE));
        exp.add(new Named("use", Use.INSTANCE));
        exp.add(new Named("drop", Drop.INSTANCE));
        exp.add(new Named("name", Name.INSTANCE));
        exp.add(new Named("embed", Embed.INSTANCE));
        exp.add(new Named("label", LabelledExp.CONSTRUCTOR));
        exp.add(new Named("trace", TraceExp.INSTANCE));
        exp.add(new Named("apply", Apply.INSTANCE));
        exp.add(new Named("strlex", StringLex.INSTANCE));
        exp.add(new Named("identity", new PureFunction() {

            public Object call(Object... args) {
                return args[0];
            }
        }));
        std.add(new Used(new Named("x", new Embedded(new Call(Get.INSTANCE, this, "ff.exp")))));
        std.add(new Used(new Named("f", new Embedded(new Call(Get.INSTANCE, this, "ff.func")))));
        String exparglist = "[x.id @[!',' @expargs]|!x.nil]";
        std.add(new Named("expargs", expexp.resolve(std.fullContext(), Util.lex(exparglist))));
        String processexpdef = "{x.exp>exp @{'-''>' x.trace>trace x.custom>func}|x.nil}";
        final Exp processexp = new ProcessExp(expexp.resolve(std.fullContext(), Util.lex(processexpdef)), new PureFunction() {

            public Object call(Object... args) {
                Obj obj = (Obj) args[0];
                Exp exp = (Exp) obj.get("exp");
                Function f = (Function) obj.get("func");
                if (f != null) {
                    exp = new ProcessExp(exp, new TracedFunction(f, obj.get("trace")));
                }
                return exp;
            }
        });
        std.add(new Named("lblprocessexp", new PureFunction() {

            public Object call(Object... args) {
                final String label = (String) args[0];
                return new Exp() {

                    public Object resolve(Context context, TokenLex tokens) throws IOException {
                        Object e = processexp.resolve(context, tokens);
                        if (e != null) {
                            e = new LabelledExp(label, (Exp) e);
                        }
                        return e;
                    }
                };
            }
        }));
        String expargs = "[!'(' @expargs|!x.nil !')']>args|x.nil>args";
        String expdef = "{'exp' x.id>name " + expargs + "'=' x.generalize(lblprocessexp(this.name), this.args)>exp ';'}";
        Exp stdexp = (Exp) expexp.resolve(std.fullContext(), Util.lex(expdef));
        std.add(new Named("exp", new LabelledExp("exp", new ProcessExp(stdexp, new PureFunction() {

            public Object call(Object... args) {
                Obj obj = (Obj) args[0];
                String name = (String) obj.get("name");
                Object exp = obj.get("exp");
                return new Named(name, exp);
            }
        }))));
        std.add(new Used(std.get("exp")));
        std.feed("exp lang = pkg|use|exp|name|x.custom;");
        std.feed("exp pkg = {'pkg' x.id>name @{'(' x.ref>lang ')'}|x.pop(lang)>lang '{' x.pkg(this.lang, this.name)>content '}'}" + "-> <f.lang> (this) => x.name(this.name, this.content);");
        std.feed("exp named = {x.id>name '=' x.custom>target|x.ref>target}" + "            -> <f.lang> (this) => x.name(this.name, this.target);");
        std.feed("exp use = {'use' named>use | x.ref>use ';'}" + "-> <f.lang> (this) => x.use(this.use);");
        std.feed("exp name = {'name' named>name ';'}" + "-> <f.lang> (this) => this.name;");
        add(new Used(std.get("lang")));
    }

    public Root() throws IOException {
        this(null);
    }

    public Exp stdExp() {
        return (Exp) get("ff.std.lang");
    }

    public Object get(String name) {
        Object o = super.get(name);
        if (o == null && fallback != null) {
            o = fallback.get(name);
        }
        return o;
    }
}
