package bioweka.core.sequence;

/**
 * Interface to a class that handles cross product alphabets.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.1 $
 */
public interface CrossProductAlphabetHandler extends AlphabetHandler {

    String getCrossProductAlphabet();

    /**
     * Sets the cross product alphabet.
     * @param crossProductAlphabet the cross product alphabet
     * @throws Exception if <code>crossProductAlphabet</code> is invalid. 
     */
    void setCrossProductAlphabet(String crossProductAlphabet) throws Exception;

    String crossProductAlphabetTipText();
}
