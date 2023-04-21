package org.jcryptool.util.input;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;

/**
 * Represents one possibility where a key can fail to comply with the
 * requirements. Complex requirements are ment to be met with more than one
 * key verificator.
 * 
 * @author Simon L
 */
public abstract class KeyVerificator {

    /**
	 * severe results < easy taken results
	 */
    static Comparator<InputVerificationResult> severityComparator = new Comparator<InputVerificationResult>() {

        public int compare(InputVerificationResult o1, InputVerificationResult o2) {
            if (o1.getMessageType() != o2.getMessageType()) {
                if (o2.getMessageType() == InputVerificationResult.NONE) {
                    return -1;
                } else if (o2.getMessageType() == InputVerificationResult.INFORMATION) {
                    if (o1.getMessageType() != InputVerificationResult.NONE) return -1; else return 1;
                } else if (o2.getMessageType() == InputVerificationResult.WARNING) {
                    if (o1.getMessageType() == InputVerificationResult.ERROR) return -1; else return 1;
                } else return 1;
            } else if (o1.getMessage().length() != o2.getMessage().length()) {
                return o1.getMessage().length() >= o2.getMessage().length() ? -1 : 1;
            }
            return 0;
        }
    };

    /**
	 * Returns {@link InputVerificationResult#DEFAULT_RESULT_EVERYTHING_OK}, if the verificator found no failure, and an 
	 * appropriate MWizardMessage otherwise.
	 * 
	 * @param key  the key to verificate
	 * @return no failure: null; otherwise: The MWizardMessage of this Verificator
	 */
    public InputVerificationResult verify(String key, AbstractAlphabet alphabet) {
        if (!verifyKeyInput(key, alphabet)) return getFailResult(key, alphabet);
        return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
    }

    /**
	 * The result that shall be returned when this verification fails. Note, that the function
	 * {@link InputVerificationResult#isValid()} mustn't necessarily return false. 
	 */
    protected abstract InputVerificationResult getFailResult(String key, AbstractAlphabet alphabet);

    /**
	 * @param key they key to verify
	 * @param alphabet the alphabet that is currently used
	 * @return whether accept the key or not
	 */
    protected abstract boolean verifyKeyInput(String key, AbstractAlphabet alphabet);

    /**
	 * Verifies a key over a set of verificators, resulting in a InputVerificationResult
	 * 
	 * for parameters and return values, see {@link #verify(String, AbstractAlphabet)} 
	 */
    public static InputVerificationResult verify(String key, AbstractAlphabet alphabet, Collection<KeyVerificator> verificatorSet) {
        List<InputVerificationResult> resultList = new LinkedList<InputVerificationResult>();
        for (KeyVerificator verificator : verificatorSet) {
            resultList.add(verificator.verify(key, alphabet));
        }
        Collections.sort(resultList, severityComparator);
        return resultList.size() > 0 ? resultList.get(0) : InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
    }
}
