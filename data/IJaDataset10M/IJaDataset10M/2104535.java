package uk.ac.ebi.rhea.util;

import uk.ac.ebi.rhea.mapper.util.ChebiHelperException;

/**
 * Implementations of this interface can normalise objects to Rhea's standards.
 * @param <T> the type of object to normalise.
 * @author rafalcan
 * @since 4.0
 */
public interface Normaliser<T> {

    /**
     * The current criterion for normalising compounds in Rhea.
     */
    public static final Object RHEA_COMPOUND_NORMALISATION = PhAdjuster.PHYSIOLOGICAL_PH;

    /**
	 * Normalises an object to the given standard.
	 * @param obj The object to normalise.
	 * @param param The parameter defining the standard conditions.
     * @return a normalised object.
     * @throws PhAdjustmentException in case of problems normalising pH.
     * @throws ChebiHelperException in case of problems normalising to neutral
     *      form.
	 */
    public T normalise(T obj, Object param) throws PhAdjustmentException, ChebiHelperException;
}
