package fuzzy.operators;

/**
 * @author Paolo Costa <paolo.costa@polimi.it>
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class HamacherIntersection extends OperatorWithParameter {

    /**
	 * @param parameter
	 * @throws OutOfRangeException
	 */
    public HamacherIntersection(float parameter) throws OutOfRangeException {
        super(parameter);
        if (parameter < 0) {
            throw new OutOfRangeException("HamacherIntersection: Parameter must be grater than zero");
        }
    }

    protected float calculateResult(float x, float y) {
        return ((x * y) / (p + (1 - p) * (x + y - x * y)));
    }
}
