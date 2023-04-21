package org.neurpheus.nlp.morphology.baseimpl;

import org.neurpheus.nlp.morphology.AnalysisResult;
import org.neurpheus.nlp.morphology.MorphologyException;
import org.neurpheus.nlp.morphology.Stemmer;

/**
 * Basic, abstract implementation of a stemmer component.
 * 
 * If your class extends this class then it is enough to implement the {@link #getStemmingResults} method.
 *
 * @author Jakub Strychowski
 */
public abstract class AbstractStemmer extends AbstractMorphologicalComponent implements Stemmer {

    /**
     * Returns the best stem for the given word form.
     *
     * @param   wordForm    The word form which should be stemmed.
     *
     * @return  The stem of the given word form.
     * 
     * @throws org.neurpheus.nlp.morphology.MorphologyException if an error occurred 
     * during the analysis of the word form.
     */
    public String getStem(String wordForm) throws MorphologyException {
        if (wordForm == null) {
            throw new NullPointerException("The [wordForm] argument cannot be null.");
        }
        AnalysisResult[] results = getStemmingResults(wordForm);
        if (results == null || results.length == 0) {
            String className = this.getClass().getName();
            throw new NullPointerException("The stemmer " + className + " has returned empty or null result for the word form : " + wordForm);
        }
        return results[0].getForm();
    }

    /**
     * Returns possible stems for the given word form.
     * <p>
     * Stemmers may return many stems for a single analysed word form. 
     * This situation occurs when form belongs to many lexemes or it is 
     * impossible to perform stemming unambiguously. 
     * The final selection should be performed by a context or semantic analysis.
     * </p>
     * <p>
     * <b>Note:</b> 
     * You can use the {@link #getStemmingResults} method to receive
     * an accuracy of each returned stem.
     * </p>
     *
     * @param   wordForm    The word form which should be stemmed.
     * 
     * @return  The array of stems of the given word form sorted 
     *          according to accuracy (from the best to the worst).
     * 
     * @throws org.neurpheus.nlp.morphology.MorphologyException if an error occurred 
     * during the analysis of the word form.
     */
    public String[] getStems(String wordForm) throws MorphologyException {
        if (wordForm == null) {
            throw new NullPointerException("The [wordForm] argument cannot be null.");
        }
        AnalysisResult[] results = getStemmingResults(wordForm);
        if (results == null || results.length == 0) {
            String className = this.getClass().getName();
            throw new NullPointerException("The stemmer " + className + " has returned empty or null result for the word form : " + wordForm);
        }
        String[] r = new String[results.length];
        for (int i = results.length - 1; i >= 0; i--) {
            r[i] = results[i].getForm();
        }
        return r;
    }

    /**
     * Returns the best stem for the given word form and an accuracy 
     * of a stemming process.
     *
     * @param   wordForm    The word form which should be stemmed.
     *
     * @return  The stem and its accuracy.
     * 
     * @throws org.neurpheus.nlp.morphology.MorphologyException if an error occurred 
     * during the analysis of the word form.
     */
    public AnalysisResult getStemmingResult(String wordForm) throws MorphologyException {
        if (wordForm == null) {
            throw new NullPointerException("The [wordForm] argument cannot be null.");
        }
        AnalysisResult[] results = getStemmingResults(wordForm);
        if (results == null || results.length == 0) {
            String className = this.getClass().getName();
            throw new NullPointerException("The stemmer " + className + " has returned empty or null result for the word form : " + wordForm);
        }
        return results[0];
    }

    /**
     * Returns possible stems for the given word form and an accuracy
     * of a stemming process.
     * <p>
     * Stemmers may return many stems for a single analysed word form. 
     * This situation occurs when form belongs to many lexemes or it is 
     * impossible to perform stemming unambiguously. 
     * The final selection should be performed by a context or semantic analysis.
     * </p>
     *
     * @param   wordForm    The word form which should be stemmed.
     * 
     * @return  The array of stems of the given word form and their accuracy 
     *          sorted from the best to the worst. 
     * 
     * @throws org.neurpheus.nlp.morphology.MorphologyException if an error occurred 
     * during the analysis of the word form.
     */
    public abstract AnalysisResult[] getStemmingResults(String wordForm) throws MorphologyException;
}
