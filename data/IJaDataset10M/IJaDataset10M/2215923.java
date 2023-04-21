package edu.ucdavis.genomics.metabolomics.binbase.algorythm.filter;

import java.util.Map;
import edu.ucdavis.genomics.metabolomics.binbase.diagnostics.Diagnostics;

/**
 * definiert einen Filter der nur bestimmte spektren zul?sst
 * @author wohlgemuth
 */
public interface MassSpecFilter extends Diagnostics {

    /**
     * wird dieses spektrum aktzeptiert
     * @param spec
     * @return
     */
    boolean accept(Map spec);
}
