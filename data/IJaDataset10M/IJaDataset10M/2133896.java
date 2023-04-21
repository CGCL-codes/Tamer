package edu.ucdavis.genomics.metabolomics.binbase.algorythm.filter;

import edu.ucdavis.genomics.metabolomics.binbase.algorythm.validate.ValidateSpectra;
import edu.ucdavis.genomics.metabolomics.binbase.diagnostics.DiagnosticsService;
import edu.ucdavis.genomics.metabolomics.binbase.diagnostics.DiagnosticsServiceFactory;
import edu.ucdavis.genomics.metabolomics.util.sort.Quicksort;
import java.util.Map;

/**
 * filter massspecs by a specific mass that the massspec must contain
 * @author wohlgemuth
 */
public class MassesMassspecFilter implements MassSpecFilter {

    /**
     * welche ionen nur erlaubt werden sollen
     */
    int[] ionFilter = new int[] { 87, 147, 117 };

    /**
     * wie gross das gefilterte ion im Verh?ltniss (%) zum basepeak sein muss
     */
    double offsetBasePeak = 50;

    /**
     * die maximale purity die erlaubt ist
     */
    double purityFilter = 2;

    /**
     * @see edu.ucdavis.genomics.metabolomics.binbase.algorythm.filter.MassSpecFilter#accept(java.util.Map)
     */
    public boolean accept(Map map) {
        String spectra = (String) map.get("spectra");
        double[][] spectraArray = ValidateSpectra.convert(spectra);
        spectraArray = new Quicksort().sort(spectraArray, ValidateSpectra.FRAGMENT_ION_POSITION);
        for (int y = 0; y < ionFilter.length; y++) {
            if (spectraArray[ionFilter[y] - 1][ValidateSpectra.FRAGMENT_REL_POSITION] > offsetBasePeak) {
                if (Double.valueOf((String) map.get("purity")).doubleValue() < purityFilter) {
                    return true;
                }
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
