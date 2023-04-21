package ExamplesJaCoP;

import java.util.ArrayList;
import JaCoP.constraints.Not;
import JaCoP.core.IntVar;
import JaCoP.core.Store;
import JaCoP.core.Var;
import JaCoP.search.DepthFirstSearch;
import JaCoP.search.Search;
import JaCoP.search.SelectChoicePoint;
import JaCoP.search.SimpleSelect;
import JaCoP.set.constraints.AeqB;
import JaCoP.set.constraints.AintersectBeqC;
import JaCoP.set.constraints.CardA;
import JaCoP.set.constraints.CardAeqX;
import JaCoP.set.core.BoundSetDomain;
import JaCoP.set.core.SetVar;
import JaCoP.set.search.IndomainSetMin;

/**
 * It specifies a simple Gardner problem which use set functionality from JaCoP. 
 *
 * @author Krzysztof Kuchcinski and Radoslaw Szymanek
 * @version 3.0
 */
public class Gardner extends Example {

    /**
	 * It executes the program which solves this gardner problem.
	 * @param args
	 */
    public static void main(String args[]) {
        Gardner example = new Gardner();
        example.model();
        example.search();
    }

    public void model() {
        int num_days = 35;
        int num_persons_per_meeting = 3;
        int persons = 15;
        System.out.println("Gardner dinner problem ");
        store = new Store();
        SetVar[] days = new SetVar[num_days];
        for (int i = 0; i < days.length; i++) days[i] = new SetVar(store, "days[" + i + "]", new BoundSetDomain(1, persons));
        vars = new ArrayList<Var>();
        for (Var d : days) vars.add(d);
        for (int i = 0; i < days.length - 1; i++) for (int j = i + 1; j < days.length; j++) store.impose(new Not(new AeqB(days[i], days[j])));
        for (int i = 0; i < days.length; i++) store.impose(new CardA(days[i], num_persons_per_meeting));
        for (int i = 0; i < days.length - 1; i++) for (int j = i + 1; j < days.length; j++) {
            SetVar intersect = new SetVar(store, "intersect" + i + "-" + j, new BoundSetDomain(1, persons));
            store.impose(new AintersectBeqC(days[i], days[j], intersect));
            IntVar card = new IntVar(store, 0, 1);
            store.impose(new CardAeqX(intersect, card));
        }
        System.out.println("\nVariable store size: " + store.size() + "\nNumber of constraints: " + store.numberConstraints());
    }

    public boolean search() {
        Thread tread = java.lang.Thread.currentThread();
        java.lang.management.ThreadMXBean b = java.lang.management.ManagementFactory.getThreadMXBean();
        long startCPU = b.getThreadCpuTime(tread.getId());
        long startUser = b.getThreadUserTime(tread.getId());
        boolean result = store.consistency();
        System.out.println("*** consistency = " + result);
        Search<SetVar> label = new DepthFirstSearch<SetVar>();
        SelectChoicePoint<SetVar> select = new SimpleSelect<SetVar>(vars.toArray(new SetVar[vars.size()]), null, new IndomainSetMin<SetVar>());
        label.getSolutionListener().searchAll(false);
        label.getSolutionListener().recordSolutions(false);
        result = label.labeling(store, select);
        if (result) {
            System.out.println("*** Yes");
            for (int i = 0; i < vars.size(); i++) {
                System.out.println(vars.get(i));
            }
        } else System.out.println("*** No");
        System.out.println("ThreadCpuTime = " + (b.getThreadCpuTime(tread.getId()) - startCPU) / (long) 1e+6 + "ms");
        System.out.println("ThreadUserTime = " + (b.getThreadUserTime(tread.getId()) - startUser) / (long) 1e+6 + "ms");
        return result;
    }
}
