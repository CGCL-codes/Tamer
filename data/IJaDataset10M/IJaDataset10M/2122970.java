package lights.extensions.fuzzy.operators;

/**
 * @author Paolo Costa <paolo.costa@polimi.it>
 *
 * To change the templates for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SchweizerUnion1 extends OperatorWithParameter {

    /**
	 * @param parameter
	 * @throws OutOfRangeException
	 */
    public SchweizerUnion1(float parameter) throws OutOfRangeException {
        super(parameter);
        if (parameter <= 0) {
            throw new OutOfRangeException("SchweizerUnion: Parameter must be grater than zero");
        }
    }

    protected float calculateResult(float x, float y) {
        float result;
        result = (float) (Math.pow(1 - x, p) + Math.pow(1 - y, p) - 1);
        return (float) (0 > result ? 0 : Math.pow(result, 1 / p));
    }
}
