package com.aliasi.classify;

/**
 * A <code>Classification</code> provides a first-best category.
 * Subclasses provide n-best results with numerical scores of
 * various interpretations.
 *
 * @author Bob Carpenter
 * @version 3.8
 * @since   LingPipe2.0
 */
public class Classification {

    private final String mBestCategory;

    /**
     * Construct a classification with the specified first-best
     * category.  
     *
     * @param bestCategory Category of this classification.
     * @throws IllegalArgumentException If the category is null.
     */
    public Classification(String bestCategory) {
        if (bestCategory == null) {
            String msg = "Category cannot be null for classifiers.";
            throw new IllegalArgumentException(msg);
        }
        mBestCategory = bestCategory;
    }

    /**
     * Returns the best-scoring category.
     *
     * @return Best category for this classification.
     */
    public String bestCategory() {
        return mBestCategory;
    }

    /**
     * Returns a string-based representation of the first-best
     * category of this classification.
     *
     * @return A string-based representation of this classification.
     */
    @Override
    public String toString() {
        return "Rank    Category\n1=" + bestCategory();
    }
}
