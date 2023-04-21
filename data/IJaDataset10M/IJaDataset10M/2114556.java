package rsp.target;

/**
 *  A template driven expression generator useful for library objects which
 *  vary little apart from insertion of arguments.
 */
public class FixedExprGenerator extends ExprGenerator {

    private String[] expr;

    public FixedExprGenerator(String name, String expr) {
        super(name);
        this.expr = new String[] { expr };
    }

    public FixedExprGenerator(String name, String[] expr) {
        super(name);
        this.expr = expr;
    }

    public String generate(CallContext c) {
        String templ0 = null;
        if (expr.length == 1) templ0 = expr[0]; else {
            assert expr.length >= c.args.length && expr[c.args.length] != null : this + " has no code generator for " + c.args.length + "arguments";
            templ0 = expr[c.args.length];
        }
        return performReplacement(c, '@', templ0);
    }
}
