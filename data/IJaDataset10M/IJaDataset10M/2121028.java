package bioweka.core.sequence;

import bioweka.core.SimpleOptionHandler;

/**
 * Interface to a class that can handle sequences.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.1 $
 */
public interface SequenceHandler extends SimpleOptionHandler {

    String getSequenceAttribute();

    /**
     * Sets the index of the attribute holding the sequence.
	 * @param sequenceAttributeIndex string representation of the index
	 * @throws Exception if <code>sequenceAttributeIndex</code> is invalid.
     * @see weka.core.SingleIndex
	 */
    void setSequenceAttribute(String sequenceAttributeIndex) throws Exception;

    String sequenceAttributeTipText();
}
