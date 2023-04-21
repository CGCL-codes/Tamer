package gov.nasa.jpf.search.heuristic;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.util.CoverageManager;

/**
 * heuristic state prioritizer that maximizes ??
 */
public class PunishGlobalBranchCoverage implements Heuristic {

    public PunishGlobalBranchCoverage(Config config, HeuristicSearch hSearch) {
        hSearch.setCalcBranchCoverage(true);
    }

    public int heuristicValue() {
        int li = CoverageManager.getLastIncrementedGlobal();
        if (li > 0) {
            return li + 1;
        } else {
            return 1;
        }
    }

    public void processParent() {
    }
}
