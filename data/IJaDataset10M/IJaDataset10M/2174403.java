package edu.psu.citeseerx.citematch.utils;

/**
 * Interface for citation text analyzers.
 *
 * @author Isaac Councill
 * @version $Rev: 850 $ $Date: 2008-12-22 12:46:44 -0500 (Mon, 22 Dec 2008) $
 */
public interface Analyzer {

    /**
     * Implementing classes should return a normalized version of the
     * specified String.
     * @param s
     * @return a normalized version of the specified String.
     */
    public String analyze(String s);
}
