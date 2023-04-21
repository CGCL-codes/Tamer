package mipt.math.function.arg;

import mipt.math.ScalarNumber;
import mipt.math.arg.Parameter;
import mipt.math.arg.func.Dependent;
import mipt.math.arg.func.formula.FormulaDependenceAnalyzer;
import mipt.math.function.impl.MinusOperator;
import mipt.math.function.impl.MultOperator;
import mipt.math.function.impl.PlusOperator;

/**
 * @author: Baburkin
 */
public class DependenceMultiFunctionTest {

    /**
 * SolverTest constructor comment.
 */
    public DependenceMultiFunctionTest() {
        super();
    }

    /**
 * Starts the application.
 * @param args an array of command-line arguments
 */
    public static void main(String[] args) {
        FormulaDependenceAnalyzer f = new FormulaDependenceAnalyzer();
        DependenceMultiFunction mf = new DependenceMultiFunction();
        ScalarNumber[] values = new ScalarNumber[7];
        values[0] = new ScalarNumber(1);
        values[1] = new ScalarNumber(4);
        values[2] = new ScalarNumber(20);
        values[3] = new ScalarNumber(5);
        values[4] = new ScalarNumber(5);
        values[5] = new ScalarNumber(-4);
        values[6] = new ScalarNumber(2);
        f.setOperator("+", new PlusOperator());
        f.setOperator("-", new MinusOperator());
        f.setOperator("*", new MultOperator());
        f.setFormula("y4+y2+y1-y6+x*y5-y3");
        ((Dependent) (f.getDependence().getArgument("y1"))).setArgument(new Parameter(values[0]));
        ((Dependent) (f.getDependence().getArgument("y2"))).setArgument(new Parameter(values[1]));
        ((Dependent) (f.getDependence().getArgument("y3"))).setArgument(new Parameter(values[2]));
        ((Dependent) (f.getDependence().getArgument("y4"))).setArgument(new Parameter(values[3]));
        ((Dependent) (f.getDependence().getArgument("y5"))).setArgument(new Parameter(values[4]));
        ((Dependent) (f.getDependence().getArgument("y6"))).setArgument(new Parameter(values[5]));
        ((Dependent) (f.getDependence().getArgument("x"))).setArgument(new Parameter(values[6]));
        mf.setDependence(f.getDependence());
        System.out.println(mf.calc(values).doubleValue());
        System.out.println((f.getDependence().getValue()).doubleValue());
    }
}
