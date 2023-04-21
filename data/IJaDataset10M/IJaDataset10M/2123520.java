package fuzzy.operators;

/**
 * @author Paolo Costa <paolo.costa@polimi.it>
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DombiIntersection extends OperatorWithParameter {

    /**
	 * @param parameter
	 * @throws OutOfRangeException
	 */
    public DombiIntersection(float parameter) throws OutOfRangeException {
        super(parameter);
        if (parameter <= 0) {
            throw new OutOfRangeException("DombiIntersection: Parameter must be grater than zero");
        }
    }

    protected float calculateResult(float x, float y) {
        return (float) (1 / (1 + Math.pow(((1 - x) / x) * p + ((1 - y) / y) * p, 1 / p)));
    }
}
