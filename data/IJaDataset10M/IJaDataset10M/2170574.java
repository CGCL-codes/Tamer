package ExamplesJaCoP;

import java.util.ArrayList;
import JaCoP.constraints.Alldiff;
import JaCoP.constraints.XneqY;
import JaCoP.core.IntVar;
import JaCoP.core.Store;
import JaCoP.core.Var;

/**
 * 
 * It solves the PigeonHole problem. 
 * 
 * 
 * The problem is how to assign n pigeons into n-1 holes in 
 * such a way that each hole holds only one pigeons.
 * Clearly this problem is not satisfiable.
 * 
 * @author Radoslaw Szymanek
 * 
 */
public class PigeonHole extends Example {

    /**
	 * 
	 */
    public int noPigeons = 5;

    @Override
    public void model() {
        store = new Store();
        vars = new ArrayList<Var>();
        IntVar[] numbers = new IntVar[noPigeons];
        for (int i = 0; i < noPigeons; i++) numbers[i] = new IntVar(store, "h" + (i + 1), 1, noPigeons - 1);
        store.impose(new Alldiff(numbers));
        for (Var v : numbers) vars.add(v);
    }

    /**
	 * It specifies inefficient model which uses only 
	 * primitive constraints.
	 */
    public void modelBasic() {
        store = new Store();
        vars = new ArrayList<Var>();
        IntVar[] numbers = new IntVar[noPigeons];
        for (int i = 0; i < noPigeons; i++) numbers[i] = new IntVar(store, "h" + (i + 1), 1, noPigeons - 1);
        for (int i = 0; i < noPigeons; i++) for (int j = i + 1; j < noPigeons; j++) store.impose(new XneqY(numbers[i], numbers[j]));
        for (Var v : numbers) vars.add(v);
    }

    /**
	 * It executes the program to solve PigeonHole problem in two 
	 * different ways. The first approach uses global constraint, 
	 * the second approach uses only primitive constraints.
	 * 
	 * @param args the number of pigeons.
	 */
    public static void main(String args[]) {
        PigeonHole example = new PigeonHole();
        if (args.length > 1) example.noPigeons = Integer.parseInt(args[1]);
        example.model();
        if (example.search()) System.out.println("Solution(s) found");
        example = new PigeonHole();
        if (args.length > 1) example.noPigeons = Integer.parseInt(args[1]);
        example.modelBasic();
        if (example.search()) System.out.println("Solution(s) found");
    }
}
