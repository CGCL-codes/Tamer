package edu.ucdavis.genomics.metabolomics.binbase.algorythm.filter;

import java.util.Map;
import edu.ucdavis.genomics.metabolomics.binbase.diagnostics.DiagnosticsService;
import edu.ucdavis.genomics.metabolomics.binbase.diagnostics.DiagnosticsServiceFactory;

/**
 * filter massspecs by unique mass
 * @author wohlgemuth
 *
 */
public class UniqueMassFilter implements MassSpecFilter {

    /**
     * welche ionen nur erlaubt werden sollen
     */
    int[] uniqueFilter = new int[] { 87, 117 };

    /**
     * @see edu.ucdavis.genomics.metabolomics.binbase.algorythm.filter.MassSpecFilter#accept(java.util.Map)
     */
    public boolean accept(Map map) {
        int unique = Integer.parseInt(map.get("uniquemass").toString());
        for (int y = 0; y < uniqueFilter.length; y++) {
            if (unique == uniqueFilter[y]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public DiagnosticsService getDiagnosticsService() {
        return service;
    }

    private DiagnosticsService service = DiagnosticsServiceFactory.newInstance().createService();
}
