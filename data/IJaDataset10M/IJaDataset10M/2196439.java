package mipt.math.sys.alt;

import java.util.LinkedList;
import java.util.List;

/**
 * Class is abstract because any real problem define extra info on alternatives and/or
 *  on what to do with them.
 * Subclass may or may not define default alternative set (in initAlternatives).
 * @author Evdokimov
 */
public abstract class AbstractAlternativesProblem implements AlternativesProblem {

    private List alternatives;

    private Object algorithmInfo;

    /**
	 * 
	 */
    public AbstractAlternativesProblem() {
    }

    /**
	 * This method is rarely used because it's better to add an alternative (its id)
	 *  together with information on it (e.g. its asessment).
	 * Can be overriden to check or update internal variables.
	 * @param alternatives
	 */
    public void setAlternatives(List alternatives) {
        this.alternatives = alternatives;
    }

    /**
	 * @return
	 */
    public final List getAlternatives() {
        if (alternatives == null) alternatives = initAlternatives();
        return alternatives;
    }

    /**
	 * Factory method. May return non-empty list.
	 */
    protected List initAlternatives() {
        return new LinkedList();
    }

    /**
	 * @see mipt.math.sys.alt.AlternativesProblem#getAlternative(int)
	 */
    public final Object getAlternative(int index) {
        return getAlternatives().get(index);
    }

    /**
	 * @see mipt.math.sys.alt.AlternativesProblem#getAlternativeCount()
	 */
    public final int getAlternativesCount() {
        return getAlternatives().size();
    }

    /**
	 * Can't be overriden: the sense of dimension in mipt.math.sys.alt package is fixed here.
	 * @see mipt.math.sys.Problem#getDimension()
	 */
    public final int getDimension() {
        return getAlternatives().size();
    }

    public final Object getAlgorithmInfo() {
        return algorithmInfo;
    }

    public void setAlgorithmInfo(Object algorithmInfo) {
        this.algorithmInfo = algorithmInfo;
    }

    /**
	 * Often used to implement get*(Object alternative, ..) through get*(int altIndex, ..)
	 * Override if you know the type of alternative (id) and it is convertable to int.
	 * @param alternative
	 */
    protected int getAlternativeIndex(Object alternative) {
        return getAlternatives().indexOf(alternative);
    }
}
