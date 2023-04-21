package cc.mallet.grmm.inference.gbp;

import java.util.*;
import cc.mallet.grmm.types.Factor;
import cc.mallet.grmm.types.Variable;
import gnu.trove.THashSet;

/**
 * Created: May 27, 2005
 *
 * @author <A HREF="mailto:casutton@cs.umass.edu>casutton@cs.umass.edu</A>
 * @version $Id: Region.java,v 1.1 2007/10/22 21:37:58 mccallum Exp $
 */
class Region {

    Set factors;

    List parents;

    List children;

    List vars;

    int index;

    boolean isRoot;

    int countingNumber;

    Set descendants;

    private Region() {
        children = new ArrayList(0);
        parents = new ArrayList(0);
        isRoot = true;
        index = -1;
    }

    Region(Variable var) {
        this();
        factors = new THashSet();
        vars = new ArrayList(1);
        vars.add(var);
    }

    Region(Factor ptl) {
        this();
        factors = new THashSet();
        factors.add(ptl);
        vars = new ArrayList(ptl.varSet());
    }

    Region(Variable[] vars, Factor[] factors) {
        this();
        this.factors = new THashSet(Arrays.asList(factors));
        this.vars = new ArrayList(Arrays.asList(vars));
    }

    Region(Collection vars, Collection factors) {
        this();
        this.factors = new THashSet(factors);
        this.vars = new ArrayList(vars);
    }

    Region(Collection vars) {
        this();
        this.vars = new ArrayList(vars);
        factors = new THashSet();
    }

    void addFactor(Factor ptl) {
        if (!factors.contains(ptl)) {
            factors.add(ptl);
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("REGION[");
        for (Iterator it = vars.iterator(); it.hasNext(); ) {
            Variable var = (Variable) it.next();
            buf.append(var);
            if (it.hasNext()) buf.append(" ");
        }
        buf.append("] nf:");
        buf.append(factors.size());
        return buf.toString();
    }
}
