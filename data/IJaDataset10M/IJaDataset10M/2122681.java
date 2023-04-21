package cc.mallet.grmm.inference.gbp;

import java.util.Iterator;
import cc.mallet.grmm.types.Factor;
import cc.mallet.grmm.types.FactorGraph;
import cc.mallet.grmm.types.Variable;

/**
 * Created: May 30, 2005
 *
 * @author <A HREF="mailto:casutton@cs.umass.edu>casutton@cs.umass.edu</A>
 * @version $Id: BPRegionGenerator.java,v 1.1 2007/10/22 21:37:58 mccallum Exp $
 */
public class BPRegionGenerator implements RegionGraphGenerator {

    public RegionGraph constructRegionGraph(FactorGraph mdl) {
        RegionGraph rg = new RegionGraph();
        for (Iterator it = mdl.factorsIterator(); it.hasNext(); ) {
            Factor ptl = (Factor) it.next();
            if (ptl.varSet().size() == 1) continue;
            Region parent = new Region(ptl);
            for (Iterator childIt = ptl.varSet().iterator(); childIt.hasNext(); ) {
                Variable var = (Variable) childIt.next();
                Factor childPtl = mdl.factorOf(var);
                Region child = rg.findRegion(childPtl, true);
                if (childPtl != null) {
                    parent.addFactor(childPtl);
                    child.addFactor(childPtl);
                }
                rg.add(parent, child);
            }
        }
        rg.computeInferenceCaches();
        return rg;
    }
}
