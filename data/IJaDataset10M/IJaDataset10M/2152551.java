package org.quantumleaphealth.screen;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import org.quantumleaphealth.model.patient.PatientHistory;

/**
 * An imperative eligibility criterion that describes how it 
 * matches to a patient's history.
 * This abstract class defines two inner classes for describing the match:<ol>
 * <li><code>Result</code>: The granular levels of the matching result</li> 
 * <li><code>CharacteristicResult</code> a localized description
 * of the patient's characteristic(s) that are relevant to the matching result</li> 
 * </ol>
 * @author Tom Bechtold
 * @version 2008-07-09
 */
public abstract class Matchable implements Serializable {

    /**
     * The result of a descriptive match between a
     * criterion and a patient's history.
     * This methodology is a simplification of a 
     * numerical relevance ranking system.
     * A <code>null</code> result means the criterion
     * is not supported by the engine.
     */
    public enum Result {

        /**
	 * Fails the match without subjective conditions.
	 */
        FAIL {

            @Override
            public String toString() {
                return "Red";
            }
        }
        , /**
	 * Fails the match with subjective conditions.
	 * If the conditions prove to be true then the match would fail.
	 * This result is also known as a <em>flagged alert</em>.
	 */
        FAIL_SUBJECTIVE {

            @Override
            public String toString() {
                return "Orange";
            }
        }
        , /**
	 * Passes the match with subjective conditions.
	 * If the conditions prove to be true then the match would pass.
	 * This result is also known as a <em>regular alert</em>.
	 */
        PASS_SUBJECTIVE {

            @Override
            public String toString() {
                return "Yellow";
            }
        }
        , /**
	 * Passes the match without subjective conditions.
	 */
        PASS {

            @Override
            public String toString() {
                return "Green";
            }
        }
        ;

        /**
	 * Returns the worse result.
	 * Note that this method assumes the enumeration definition is 
	 * ordered from worse to better and that <code>null</code> is worst.  
	 * @param other the other result
	 * @return the worse result
	 *         or <code>null</code> if <code>other</code> is <code>null</code>
	 */
        public Result worse(Result other) {
            if (other == null) return null;
            return compareTo(other) < 0 ? this : other;
        }

        /**
	 * Returns the better result.
	 * Note that this method assumes the enumeration definition is 
	 * ordered from worse to better and that <code>null</code> is worst.  
	 * @param other the other result
	 * @return the better result
	 *         or <code>this</code> if <code>other</code> is <code>null</code>
	 */
        public Result better(Result other) {
            if (other == null) return this;
            return compareTo(other) > 0 ? this : other;
        }

        /**
	 * Returns this result inverted
	 * @return this result inverted
	 */
        public Result invert() {
            switch(this) {
                case FAIL:
                    return PASS;
                case FAIL_SUBJECTIVE:
                    return PASS_SUBJECTIVE;
                case PASS_SUBJECTIVE:
                    return FAIL_SUBJECTIVE;
                case PASS:
                    return FAIL;
                default:
                    throw new AssertionError(this);
            }
        }
    }

    /**
     * Return the worse result.
     * Note that this method assumes the enumeration definition is 
     * ordered from worse to better.  
     * @param first the first result
     * @param second the second result
     * @return the worse result
     */
    public static Result worse(Result first, Result second) {
        return (first == null) ? second : first.worse(second);
    }

    /**
     * Return the better result.
     * Note that this method assumes the enumeration definition is 
     * ordered from worse to better.
     * @param first the first result
     * @param second the second result
     * @return the better result
     */
    public static Result better(Result first, Result second) {
        return (first == null) ? second : first.better(second);
    }

    /**
     * Returns how it matches to a patient's history
     * @param history the patient's history
     * @return how it matches to a patient's history
     */
    public abstract Result matchDescriptive(PatientHistory history);

    /**
     * Encapsulates a characteristic with a result.
     */
    public static class CharacteristicResult implements Serializable {

        /**
	 * The characteristic
	 */
        private String characteristic;

        /**
	 * The characteristic's result
	 */
        private Result result;

        /**
	 * Default constructor
	 */
        public CharacteristicResult() {
        }

        /**
	 * @param characteristic the characteristic
	 * @param result the characteristic's result
	 */
        public CharacteristicResult(String characteristic, Result result) {
            this.characteristic = characteristic;
            this.result = result;
        }

        /**
	 * @return the characteristic
	 */
        public String getCharacteristic() {
            return characteristic;
        }

        /**
	 * @param characteristic the characteristic to set
	 */
        public void setCharacteristic(String characteristic) {
            this.characteristic = characteristic;
        }

        /**
	 * @return the characteristic's result
	 */
        public Result getResult() {
            return result;
        }

        /**
	 * @param result the characteristic's result
	 */
        public void setResult(Result result) {
            this.result = result;
        }

        /**
	 * Returns whether or not another result matches the characteristic
	 * and if so, update this result.
	 * Note that a return value of <code>true</code> simply means the other
	 * characteristic matches this one, not that the result itself was updated.
	 * @param characteristicResult the other characteristic result
	 * @param isBetter <code>true</code> to update the result to a better one
	 *        or <code>false</code> to update the result to a worse one
	 * @return whether the other result's characteristic matches this one's
	 */
        public boolean matches(CharacteristicResult characteristicResult, boolean isBetter) {
            if ((characteristicResult == null) || (characteristicResult.characteristic != characteristic)) return false;
            result = isBetter ? better(result, characteristicResult.result) : worse(result, characteristicResult.result);
            return true;
        }

        /**
	 * @return the result, a colon and the characteristic
	 * @see java.lang.Object#toString()
	 */
        @Override
        public String toString() {
            return ((result == null) ? "n/a" : result.toString()) + ": " + ((characteristic == null) ? "n/s" : characteristic);
        }

        /**
	 * Version UID for serialized class
	 */
        private static final long serialVersionUID = 4142334381394637208L;
    }

    /**
     * Localized description(s) of the characteristic(s)
     * and their results that are relevant to this criterion.
     * Characteristics should not be duplicated in the list.
     * @param history the patient's history
     * @param resourceBundle contains localized descriptions
     * @return localized description(s) of the characteristic(s)
     *         and their results
     *         that are relevant to this criterion
     *         or <code>null</code> for none
     */
    public abstract List<CharacteristicResult> getCharacteristicResults(PatientHistory history, ResourceBundle resourceBundle);

    /**
     * Version UID for serializable class
     */
    private static final long serialVersionUID = -8097208094057113510L;
}
