package org.omegahat.Simulation.MCMC.Expressions;

import org.omegahat.Simulation.MCMC.*;
import org.omegahat.Simulation.MCMC.Listeners.*;
import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Targets.*;
import org.omegahat.Models.Formulae.Expressions.*;
import org.omegahat.Environment.Interpreter.*;
import org.omegahat.Environment.Parser.Parse.*;
import org.omegahat.Environment.Databases.*;
import java.util.Enumeration;
import org.omegahat.Probability.Distributions.*;

public class ExpressionTarget extends ExpressionModel implements UnnormalizedDensity {

    protected List expressions;

    public List expressions() {
        return (expressions);
    }

    public List expressions(List l) {
        expressions = l;
        return (expressions());
    }

    public ExpressionTarget(MultiLineModelFormula model, Database environment) throws Exception {
        super(model, environment);
        expressions = createExpressions();
    }

    public ExpressionTarget(String model, Database environment) throws Exception {
        super(model, environment);
        expressions = createExpressions();
    }

    public ExpressionTarget(String[] model, Database environment) throws Exception {
        super(model, environment);
        expressions = createExpressions();
    }

    public List createExpressions() {
        List expressions = new List(model.size());
        for (int i = 0; i < model.size(); i++) {
            ModelFormula exp = (ModelFormula) model.elementAt(i);
            List l = (List) exp.structural();
            FunctionTransformationExpression f = (FunctionTransformationExpression) l.elementAt(0);
            Name n = (FormulaName) f.qualifier();
            List inArgs = f.args();
            List args = new List(inArgs.size() + 1);
            args.addElement(exp.response());
            for (int j = 0; j < inArgs.size(); j++) args.addElement(inArgs.elementAt(j));
            expressions.addElement(new CachingMethodCall(n, "logUnnormalizedPDF", args));
        }
        return (expressions);
    }

    public double logUnnormalizedPDF(Object state) {
        SearchPath s = new SearchPath();
        s.attach((MCMCStateDatabase) state, 1);
        s.attach(environment, 2);
        searchPath(s);
        List el;
        try {
            el = (List) expressions().eval(this);
        } catch (Throwable ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to evaluate: " + expressions());
        }
        double total = 0.0;
        for (Enumeration e = el.elements(); e.hasMoreElements(); ) total += ((Double) e.nextElement()).doubleValue();
        return (total);
    }

    public double unnormalizedPDF(Object state) {
        return Math.exp(logUnnormalizedPDF(state));
    }

    public double unnormalizedDensity(Object state) {
        return unnormalizedPDF(state);
    }

    public double logUnnormalezedDensity(Object state) {
        return logUnnormalizedPDF(state);
    }
}
