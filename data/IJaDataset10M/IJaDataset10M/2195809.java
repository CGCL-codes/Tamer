package com.gorillalogic.dal.common.expr;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.common.*;
import com.gorillalogic.help.Help;
import java.util.*;
import java.io.PrintWriter;

public class FunctionFactory implements Help.Category {

    static final FunctionFactory theFunctionFactory = new FunctionFactory();

    private FunctionFactory() {
        Help.factory.gcl().register(this);
    }

    private Hashtable functors = new Hashtable();

    private int hiddenCount = 0;

    private void add(CommonExpr.Functor functor) {
        functors.put(functor.getName(), functor);
        if (functor.isHiddenFromHelp()) {
            hiddenCount++;
        }
    }

    {
        add(new SumFunction());
        add(new MaxFunction());
        add(new MinFunction());
        add(new AvgFunction());
        add(new LengthExpr());
        add(new TableCountExpr());
        add(new RoundOffExpr.Floor());
        add(new RoundOffExpr.Ceiling());
        add(new UtilityFunction.PathName());
        add(new UtilityFunction.Eval());
        add(new UtilityFunction.Raw());
        add(new UtilityFunction.FileSource());
        add(new UtilityFunction.Stored());
        add(new UtilityFunction.Testpoint());
        add(new UtilityFunction.Checkpoint());
        add(new UtilityFunction.Invert());
        add(new PreExpr());
    }

    static CommonExpr.Functor findFunctor(String fn) {
        return (CommonExpr.Functor) theFunctionFactory.functors.get(fn);
    }

    public static CommonExpr fileSource() {
        return new UtilityFunction.FileSource();
    }

    public String getHelpId() {
        return "Function";
    }

    public Help.Category getHelpCategory() {
        return Help.factory.gcl();
    }

    public void summary(Out out) {
        final String fn = out.ref("Function", "function");
        out.print("GCL Standard " + fn + " library");
    }

    public void detail(Help.Out out) {
        final String gsl = out.ref("GSL");
        final String gcl = out.ref("GCL");
        final String sum = out.ref("sum()");
        out.print("Standard, or built-in functions, are not user-definable and cannot be overwritten.");
        out.brk();
        out.print("These functions are invoked syntactically like user methods. However, their");
        out.print("arguments are evaluated in their immediate scope. For example, in the expression");
        out.print("\"x.func(y)\", the argument \"y\" evaluates to \"x.y\".");
        out.brk();
        out.print("Available standard functions are as follows:");
        out.brk();
        TreeMap map = new TreeMap(functors);
        Set entrySet = map.entrySet();
        Iterator itr = entrySet.iterator();
        while (itr.hasNext()) {
            Map.Entry next = (Map.Entry) itr.next();
            CommonExpr.Functor functor = (CommonExpr.Functor) next.getValue();
            if (!functor.isHiddenFromHelp()) {
                out.tab(functor.getHelpId());
            }
        }
        out.brk();
        out.print("Some of the above are \"aggregate\" functions that produce a single result from multiple inputs.");
        out.print("For example, " + sum + " adds multiple inputs. In gosh wherever you can");
        out.print("generate a multi-row list of numbers (i.e.");
        out.print("table with a single numeric column), you can sum or any numeric aggregate");
        out.print("function to produce a single numeric result.");
        out.brk();
        out.print("Aggregate functions also follow the rule that where they have an explicit");
        out.print("left-hand operand, they will produce a value for each row of that operand");
        out.print("For example, given an table of five Orders, then the following will produce");
        out.print("a five-row result represent the sum of lineItem quantities for each row:");
        out.brk();
        out.println(1, "ls aFiveRowOrderTable.sum(lineItem.quantity)");
        out.brk();
        out.print("Of the list provided earlier, the following are aggregate functions:");
        out.brk();
        out.tab("sum()");
        out.tab("min()");
        out.tab("max()");
        out.tab("avg()");
        out.tab("count()");
        out.brk();
    }

    public int numberOfHelpItems() {
        return functors.size() - hiddenCount;
    }

    public Help getHelpItem(int index) {
        int count = 0;
        Enumeration e = functors.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            CommonExpr.Functor functor = (CommonExpr.Functor) functors.get(key);
            if ((!functor.isHiddenFromHelp()) && index == count++) {
                return functor;
            }
        }
        return null;
    }

    public static void main(String[] argv) {
        Remain.go(argv);
    }
}
