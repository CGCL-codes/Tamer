package graph;

/**
 * @author Roo and Joey
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LinearEquationFactory implements EquationFactory {

    public static final LinearEquationFactory EQN = new LinearEquationFactory();

    private LinearEquationFactory() {
    }

    public Equation newInstance() {
        return new LinearEquation();
    }
}
