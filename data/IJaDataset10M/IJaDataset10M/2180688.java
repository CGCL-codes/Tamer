package ajpf.product;

import java.util.Set;
import java.util.HashSet;
import ajpf.psl.Proposition;

/**
 * A state in the model consists of the number of the JPF state and a Set of atomic propositions true in this state.
 * @author louiseadennis
 *
 */
public class ModelState implements Comparable<ModelState> {

    int JPFstatenum;

    Set<Proposition> props = new HashSet<Proposition>();

    /**
	 * Getter for the JPF state number.
	 * @return
	 */
    public int getNum() {
        return JPFstatenum;
    }

    /**
	 * Constructor.
	 * @param statenum
	 * @param buchi
	 */
    public ModelState(int statenum, Set<Proposition> product_props) {
        JPFstatenum = statenum;
        for (Proposition p : product_props) {
            if (p.check()) {
                props.add(p);
            }
        }
    }

    /**
	 * Equate states associated wit the same JPF state number;
	 */
    public int hashCode() {
        return JPFstatenum;
    }

    public boolean equals(Object o) {
        if (o instanceof ModelState) {
            ModelState ms = (ModelState) o;
            return hashCode() == ms.hashCode();
        }
        return false;
    }

    /**
	 * Getter for the propositions.
	 * @return
	 */
    public Set<Proposition> getProps() {
        return props;
    }

    public String toString() {
        String s = "" + JPFstatenum;
        return s;
    }

    public int compareTo(ModelState s) {
        return ((Integer) JPFstatenum).compareTo((Integer) s.getNum());
    }
}
