package toxTree.query;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.isomorphism.matchers.smarts.AnyAtom;
import org.openscience.cdk.isomorphism.matchers.smarts.SMARTSAtom;

/**
 * To be used instead of org.openscience.cdk.isomorphism.matchers.smarts.AnyAtom until its matcher code is fixed
<pre>
    public boolean matches(IAtom atom) {
        if (atom.getSymbol().equals("H")) {
            Integer massNumber = atom.getMassNumber();
            return massNumber != null;
        }
        return true;
    }
</pre>
 * @author nina
 *
 */
public class ReallyAnyAtom extends AnyAtom {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2338751806424429161L;

    @Override
    public boolean matches(IAtom atom) {
        return true;
    }

    public String toString() {
        return "AnyAtom()";
    }
}
