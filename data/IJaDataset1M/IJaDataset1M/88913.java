package org.jcryptool.crypto.classic.xor.algorithm;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmSpecification;
import org.jcryptool.util.input.InputVerificationResult;
import org.jcryptool.util.input.KeyVerificator;

/**
 * 
 * 
 * @author Simon L
 */
public class XorAlgorithmSpecification extends ClassicAlgorithmSpecification {

    @Override
    public boolean isValidPlainTextAlphabet(AbstractAlphabet alpha) {
        return alpha.getName().toLowerCase().contains("xor");
    }

    public char[] keyInputStringToDataobjectFormat(String keyInput) {
        return keyInput == null ? "".toCharArray() : keyInput.toCharArray();
    }

    /**
	 * @return the verificators for the key input, where the key is a file name.
	 */
    public List<KeyVerificator> getKeyFileVerificators() {
        List<KeyVerificator> verificators = new LinkedList<KeyVerificator>();
        verificators.add(new KeyVerificator() {

            @Override
            protected boolean verifyKeyInput(String key, AbstractAlphabet alphabet) {
                File f = new File(key);
                return f.exists();
            }

            @Override
            protected InputVerificationResult getFailResult(String key, AbstractAlphabet alphabet) {
                return new InputVerificationResult() {

                    public boolean isValid() {
                        return false;
                    }

                    public boolean isStandaloneMessage() {
                        return true;
                    }

                    public int getMessageType() {
                        return InputVerificationResult.ERROR;
                    }

                    public String getMessage() {
                        return "The key file does not exist.";
                    }
                };
            }
        });
        return verificators;
    }
}
