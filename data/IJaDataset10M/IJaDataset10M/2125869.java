package org.neurpheus.nlp.morphology;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Represents a result which can be returned by morphological components.
 * <p>
 * Morphological components can return many possible results for a single analysed form. 
 * Each result item consist of a result form and an accuracy of the result. 
 * The accuracy is represented by a real number between 0.0 and 1.0. If this value 
 * is near 1.0, the produced result is correct with a high probability.
 * </p>
 * <p>
 * Morphological components can return many results having high accuracy.
 * This situation occurs when it is impossible to select single result analysing 
 * only the form of a word. The final selection should be performed by 
 * a context or semantic analysis.
 * </p>
 * 
 * @author Jakub Strychowski
 */
public interface AnalysisResult extends Serializable, Comparable {

    /**
     * Returns the result form of an analysed word form.
     *
     * @return The result form; for example a stem or lemma form.
     */
    String getForm();

    /**
     * Returns the accuracy of the produced form. 
     *
     * @return  The accuracy of analysis as a real number between 0.0 (very low 
     *          accuracy) and 1.0 (very high accuracy).
     */
    double getAccuracy();

    /**
     * Checks if this result is certain.
     * 
     * @return <code>true</code> if this result is for sure correct.
     */
    boolean isCertain();

    /**
     * Writes this object to a data stream.
     * 
     * @param out The data stream into which this object should be serialized.
     * 
     * @throws java.io.IOException If any output error occurred.
     */
    void write(DataOutputStream out) throws IOException;

    /**
     * Reads this object from a data stream.
     *
     * @param in The data stream from which this object should be serialized.
     *
     * @throws java.io.IOException If any input error occurred.
     */
    void read(DataInputStream in) throws IOException;
}
