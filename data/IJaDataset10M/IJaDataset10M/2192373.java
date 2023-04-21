package ec.gp;

import ec.*;
import ec.util.*;

/**
 * ADFContext is the object pushed onto an ADF stack which represents
 * the current context of an ADM or ADF function call, that is, how to
 * get the argument values that argument_terminals need to return.
 *
 * <p><i>adf</i> contains the relevant ADF/ADM node. 
 * If it's an ADF
 * function call, then <i>arguments[]</i> contains the evaluated arguments
 * to the ADF.  If it's an ADM function call,
 * then <i>arguments[]</i> is set to false.
 *
 * <p>You set up the ADFContext object for a given ADF or ADM node with
 * the prepareADF(...) and prepareADM(...) functions.
 *
 * <p>To evaluate an argument number from an ADFContext, call evaluate(...),
 * and the results are evaluated and copied into input.
 *
 <p><b>Parameter bases</b><br>
 <table>
 <tr><td valign=top><i>base</i><tt>.data</tt><br>
 <td valign=top>(the ADFContext's basic GPData type)</td></tr> 
 </table>
 * @author Sean Luke
 * @version 1.0 
 */
public class ADFContext implements Prototype {

    public static final String P_DATA = "data";

    public static final String P_ADFCONTEXT = "adf-context";

    public static final int INITIAL_ARGUMENT_SIZE = 2;

    /** The ADF/ADM node proper */
    public ADF adf;

    /** A prototypical GPData node. */
    public GPData arg_proto;

    /** An array of GPData nodes (none of the null, when it's used) 
        holding an ADF's arguments' return results */
    public GPData[] arguments;

    public Parameter defaultBase() {
        return GPDefaults.base().push(P_ADFCONTEXT);
    }

    public ADFContext() {
        arguments = new GPData[INITIAL_ARGUMENT_SIZE];
    }

    public Object clone() {
        try {
            ADFContext myobj = (ADFContext) (super.clone());
            myobj.arg_proto = (GPData) (arg_proto.clone());
            myobj.arguments = new GPData[arguments.length];
            for (int x = 0; x < myobj.arguments.length; x++) myobj.arguments[x] = (GPData) (arguments[x].clone());
            return myobj;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public void setup(final EvolutionState state, final Parameter base) {
        Parameter p = base.push(P_DATA);
        Parameter def = defaultBase();
        Parameter d = def.push(P_DATA);
        if (state.parameters.exists(p, d)) {
            state.output.warning("ADF Data is deprecated -- this parameter is no longer used.  Instead, we directly use the GPData.", p, d);
        }
        {
            Parameter pp = new Parameter(EvolutionState.P_EVALUATOR).push(Evaluator.P_PROBLEM).push(GPProblem.P_DATA);
            Parameter dd = GPDefaults.base().push(GPProblem.P_GPPROBLEM).push(GPProblem.P_DATA);
            arg_proto = (GPData) (state.parameters.getInstanceForParameter(pp, dd, GPData.class));
            arg_proto.setup(state, pp);
        }
        for (int x = 0; x < INITIAL_ARGUMENT_SIZE; x++) arguments[x] = (GPData) (arg_proto.clone());
    }

    /** Evaluates the argument number in the current context */
    public void evaluate(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem, final int argument) {
        if (argument >= adf.children.length || argument < 0) {
            individual.printIndividual(state, 0);
            state.output.fatal("Invalid argument number for " + adf.errorInfo());
        }
        if (adf == null) state.output.fatal("ADF is null for " + adf.errorInfo()); else if (adf instanceof ADF) arguments[argument].copyTo(input); else {
            if (stack.moveOntoSubstack(1) != 1) state.output.fatal("Substack prematurely empty for " + adf.errorInfo());
            adf.children[argument].eval(state, thread, input, stack, individual, problem);
            if (stack.moveFromSubstack(1) != 1) state.output.fatal("Stack prematurely empty for " + adf.errorInfo());
        }
    }

    /** Increases arguments to accommodate space if necessary.
        Sets adf to a.
        You need to then fill out the arguments yourself. */
    public final void prepareADF(ADF a) {
        if (a.children.length > arguments.length) {
            GPData[] newarguments = new GPData[a.children.length];
            System.arraycopy(arguments, 0, newarguments, 0, arguments.length);
            for (int x = arguments.length; x < newarguments.length; x++) newarguments[x] = (GPData) (arg_proto.clone());
            arguments = newarguments;
        }
        adf = a;
    }

    /** Sets adf to a */
    public final void prepareADM(ADM a) {
        adf = a;
    }
}
