package urban.model;

import java.util.List;
import urban.shapes.Generator;
import urban.shapes.ShapeParameters;
import urban.transformers.RuleToGeneratorTransformer;

/**
 * Lines of the form "%gen: 'G' A(f~0) <-> A(f~1) := 0.5"
 * 
 */
public class GeneratorStatement extends AbstractStatement implements ShapeParameterStatement {

    private final String id;

    private final List<Agent> lhs;

    private final List<Agent> rhs;

    private final Double c;

    /**
	 * A line of the form "%gen: 'G' A(f~0) <-> A(f~1) := 0.5"
	 * @param id 'G' in the above example
	 * @param lhs A(f~0) in the above example
	 * @param rhs A(f~1) in the above example
	 * @param c 0.5 in the above example
	 */
    public GeneratorStatement(String id, List<Agent> lhs, List<Agent> rhs, Double c) {
        this.id = id.replace("['\"]", "");
        this.lhs = lhs;
        this.rhs = rhs;
        this.c = c;
    }

    @Override
    public void addParametersTo(ShapeParameters tmp) {
        Generator g = getGenerator();
        tmp.constants.put(g, c);
        tmp.generators.put(id, g);
    }

    /**
	 * @return returns the generator that this line represents.
	 */
    public Generator getGenerator() {
        return new RuleToGeneratorTransformer().transform(new Rule(null, lhs, rhs, true, 0.0, 0.0));
    }

    @Override
    public String toString() {
        return "#gen: " + id + " " + s(lhs) + " <-> " + s(rhs) + " := " + (new urban.util.DoubleRenderer().toString(c)) + "\n";
    }

    private String s(List<Agent> l) {
        String tmp = l.toString();
        return tmp.substring(1, tmp.length() - 1);
    }
}
