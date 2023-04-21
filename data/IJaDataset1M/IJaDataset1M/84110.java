package ajpf;

import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.ElementInfo;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.ClassInfo;
import ajpf.product.ProbabilisticModel;

/**
 * Special listener class for the MCAPL Project.
 * 
 * @author louiseadennis
 *
 */
public class MCAPLProbListener extends MCAPLListener {

    MethodInfo michoose;

    double overall_prob = 0;

    int automata_type = ajpf.product.Product.PROBABLISTIC_AUTOMATA;

    public void classLoaded(JVM vm) {
        ClassInfo ci = vm.getLastClassInfo();
        if (ci.getName().equals("ajpf.util.Choice")) {
            michoose = ci.getMethod("choose()I", false);
            assert michoose != null;
            log.fine("found choose() method: " + michoose);
        }
        super.classLoaded(vm);
    }

    public void methodExited(JVM vm) {
        JVM jvm = vm;
        MethodInfo mi = vm.getLastMethodInfo();
        if (mi == michoose) {
            ThreadInfo ti = jvm.getLastThreadInfo();
            int objref = ti.getThis();
            log.fine("Dealing with choices");
            ElementInfo ei = vm.getElementInfo(objref);
            double prob = ei.getDoubleField("thischoice");
            product_automata.annotate_edge(prob);
        }
    }

    /**
	  * Helper method for check.
	  * @param search
	  * @return
	  */
    public boolean check(Search search, JVM vm) {
        boolean violation = super.check(search, vm);
        if (!violation) {
            double pathprob = ((ProbabilisticModel) product_automata.getModel()).pathProb();
            overall_prob += pathprob;
            log.info("Violation!  Probability: " + pathprob);
            search.requestBacktrack();
        }
        return true;
    }

    public void searchFinished(Search search) {
        log.info("Probability of Violation is: " + overall_prob);
    }

    public int getAutomataType() {
        return automata_type;
    }
}
