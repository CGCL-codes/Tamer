package edu.ucdavis.genomics.metabolomics.binbase.algorythm.filter;

import edu.ucdavis.genomics.metabolomics.binbase.algorythm.validate.ValidateSpectra;
import edu.ucdavis.genomics.metabolomics.util.sort.Quicksort;
import java.util.Map;

/**
 * @author wohlgemuth
 * just filters bi purity
 */
public class PurityFilter {

    /**
     * die maximale purity die erlaubt ist
     */
    double purityFilter = 3;

    /**
     * @see edu.ucdavis.genomics.metabolomics.binbase.algorythm.filter.MassSpecFilter#accept(java.util.Map)
     */
    public boolean accept(Map map) {
        String spectra = (String) map.get("spectra");
        double[][] spectraArray = ValidateSpectra.convert(spectra);
        spectraArray = new Quicksort().sort(spectraArray, ValidateSpectra.FRAGMENT_ION_POSITION);
        if (Double.valueOf((String) map.get("purity")).doubleValue() < purityFilter) {
            return true;
        }
        return false;
    }
}
