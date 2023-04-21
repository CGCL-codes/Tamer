package rbcCycle.retrieve.localSimilarityFunctions;

import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

/**
 * This function evaluates if the arguments are similar. 
 * @author Joao Felipe.
 *
 */
public class Like implements LocalSimilarityFunction {

    /**
	 * Applies the similarity function, returning the similarity coefficient that belongs to [0,1].
	 * @param caseObject The object from the case to be analyzed.
	 * @param queryObject The object from the query to be analyzed.
	 * @return The similarity coefficient between the two objects.
	 */
    public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
        if (!(caseObject instanceof String)) {
            throw new NoApplicableSimilarityFunctionException(this.getClass(), caseObject.getClass());
        }
        if (!(queryObject instanceof String)) {
            throw new NoApplicableSimilarityFunctionException(this.getClass(), queryObject.getClass());
        }
        return (numberOfSimilarities((String) caseObject, (String) queryObject)) / ((String) caseObject).length();
    }

    /**
	 * Verify if the given arguments are compatibles for this function.
	 * @param caseObject The object from the case to be verified.
	 * @param queryObject The object from the query to be verified.
	 * @return A boolean value indicating if the two objects are compatibles for this similarity 
	 * function.
	 */
    public boolean isApplicable(Object caseObject, Object queryObject) {
        if ((caseObject == null) && (queryObject == null)) return true; else if (caseObject == null) return queryObject instanceof String; else if (queryObject == null) return caseObject instanceof String; else return (caseObject instanceof String) && (queryObject instanceof String);
    }

    /**
	 * Removes the unnecessary blank spaces in the passed string.s 
	 * @param toRemove The string to be processed.
	 * @return A string without redundant blank spaces.
	 */
    private String removeUnnecessaryBlankSpaces(String toRemove) {
        String result = toRemove.trim();
        boolean previousSpace = false;
        int index = 0;
        while (index < result.length()) {
            String charInPositioni = ((Character) result.charAt(index)).toString();
            if (charInPositioni.equals(" ") && previousSpace == false) {
                previousSpace = true;
                ++index;
            } else if (charInPositioni.equals(" ") && previousSpace == true) {
                result = result.substring(0, index) + result.substring(index + 1, result.length());
            } else {
                previousSpace = false;
                ++index;
            }
        }
        return result;
    }

    /**
	 * Verifies all the string to calculate the number of similarities between the two strings passed.
	 * @param first A string to compare.
	 * @param second Another string to compare.
	 * @return The number of equal characters from the strings. 
	 */
    private int numberOfSimilarities(String first, String second) {
        String firstOk = this.removeUnnecessaryBlankSpaces(first);
        String secondOk = this.removeUnnecessaryBlankSpaces(second);
        if (firstOk.length() != secondOk.length() || firstOk.split(" ").length != secondOk.split(" ").length) {
            return 0;
        }
        int similarities = 0;
        for (int i = 0; i < firstOk.length(); i++) {
            if (((Character) firstOk.charAt(i)).toString().equalsIgnoreCase(((Character) secondOk.charAt(i)).toString())) {
                ++similarities;
            }
        }
        return similarities;
    }
}
